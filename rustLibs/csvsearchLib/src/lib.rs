use jni::objects::{JClass, JString};
use jni::sys::jstring;
use jni::JNIEnv;
use polars::prelude::*;

#[no_mangle]
pub extern "system" fn Java_RustCsvSearchTaskWithPolars_findInRustCsvWithPolars(
    mut env: JNIEnv,
    _class: JClass,
    j_search_word: JString,
    j_csv_path: JString,
) -> jstring {
    // Convert JNI parameters to Rust strings
    let search_word: String = env
        .get_string(&j_search_word)
        .expect("Failed to get search word")
        .into();
    
    let csv_path: String = env
        .get_string(&j_csv_path)
        .expect("Failed to get csv path")
        .into();

    // Read CSV using Polars with optimized settings
    let df = CsvReader::from_path(&csv_path)
        .expect("Failed to create CSV reader")
        .has_header(true)
        .with_chunk_size(8192)
        .finish()
        .expect("Failed to read CSV");

    let mut matches = Vec::with_capacity(32);
    
    // Iterate through all columns
    for (col_idx, series) in df.get_columns().iter().enumerate() {
        // Convert series to strings and search
        if let Ok(str_arr) = series.cast(&DataType::String)
            .expect("Failed to cast to string")
            .str() {
            // Create a mask for matching strings
            let mask = str_arr.into_iter()
                .enumerate()
                .filter_map(|(idx, opt_str)| {
                    opt_str.map(|s| if s.contains(&search_word) { Some(idx + 1) } else { None }).flatten()
                })
                .collect::<Vec<usize>>();

            // Add matches for this column
            matches.extend(
                mask.into_iter()
                    .map(|row_idx| format!("({}, {})", row_idx, col_idx))
            );
        }
    }

    // Create result string without sorting
    let result_str = if matches.is_empty() {
        String::from("[]")
    } else {
        format!("[{}]", matches.join(", "))
    };

    // Convert to JNI string
    env.new_string(result_str)
       .expect("Failed to create jstring")
       .into_raw()
}

#[no_mangle]
pub extern "system" fn Java_RustCsvSearchTask_findInRustCsv(
    mut env: JNIEnv,
    _class: JClass,
    j_search_word: JString,
    j_csv_path: JString,
) -> jstring {
    let search_word: String = env
        .get_string(&j_search_word)
        .expect("Failed to get search word")
        .into();
    
    let csv_path: String = env
        .get_string(&j_csv_path)
        .expect("Failed to get csv path")
        .into();

    let mut matches = Vec::new();
    
    if let Ok(content) = std::fs::read_to_string(&csv_path) {
        let lines: Vec<&str> = content.split("\n").collect();
        
        for (row_idx, line) in lines.iter().enumerate() {
            let mut field_start = 0;
            let mut in_quotes = false;
            let mut col_idx = 0;
            
            for (i, c) in line.chars().enumerate() {
                match c {
                    '"' => {
                        in_quotes = !in_quotes;
                        if !in_quotes {
                            let field = &line[field_start..i].trim_matches('"').trim();
                            if field.contains(&search_word) {
                                matches.push(format!("({}, {})", row_idx, col_idx));
                            }
                            field_start = i + 1;
                        } else {
                            field_start = i + 1;
                        }
                    },
                    ',' if !in_quotes => {
                        let field = &line[field_start..i].trim_matches('"').trim();
                        if field.contains(&search_word) {
                            matches.push(format!("({}, {})", row_idx, col_idx));
                        }
                        field_start = i + 1;
                        col_idx += 1;
                    },
                    _ => continue,
                }
            }
            
            // Check last field (which would be in the last column)
            if field_start < line.len() {
                let field = &line[field_start..].trim_matches('"').trim();
                if field.contains(&search_word) {
                    matches.push(format!("({}, {})", row_idx, col_idx));
                }
            }
        }
    }

    let result_str = if matches.is_empty() {
        String::from("[]")
    } else {
        format!("[{}]", matches.join(", "))
    };

    env.new_string(result_str)
       .expect("Failed to create jstring")
       .into_raw()
}