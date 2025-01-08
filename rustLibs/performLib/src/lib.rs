use jni::JNIEnv;
use jni::objects::{JClass, JByteArray};
use jni::sys::jbyteArray;

#[no_mangle]
pub extern "system" fn Java_src_MyTask_performInRust(
    env: JNIEnv,
    _class: JClass,
    input: JByteArray,
) -> jbyteArray {
    // Convert the input byte array to Rust Vec<u8>
    let input_bytes = match env.convert_byte_array(input) {
        Ok(bytes) => bytes,
        Err(e) => {
            eprintln!("Failed to convert input byte array: {:?}", e);
            return std::ptr::null_mut();
        }
    };

    // Print the input as a string (if valid UTF-8)
    match String::from_utf8(input_bytes.clone()) {
        Ok(input_str) => println!("Received input: {}", input_str),
        Err(_) => println!("Received non-UTF-8 input: {:?}", input_bytes),
    }

    // Create the response JSON structure
    let response = r#"{ "status": "ok", "message": "Rust func reached" }"#;
    let response_bytes = response.as_bytes();

    // Create output byte array
    match env.byte_array_from_slice(response_bytes) {
        Ok(output) => output.into_raw(),
        Err(e) => {
            eprintln!("Failed to create output byte array: {:?}", e);
            std::ptr::null_mut()
        }
    }
}


