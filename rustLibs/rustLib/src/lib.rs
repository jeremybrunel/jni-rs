use jni::JNIEnv;
use jni::objects::{JClass, JString};
use jni::sys::{jboolean, JNI_FALSE, JNI_TRUE};

// 2) Mark the function with #[no_mangle] to ensure the name is not mangled,
//    and use `extern "system"` so it matches the JNI calling convention.
#[no_mangle]
pub extern "system" fn Java_RustSearchTask_findStringInRust<'local>(
    // This is the JNI interface. It's used to call into Java or read arguments.
    mut env: JNIEnv<'local>,

    // Because our Java method is *static*, the second argument is a JClass
    // (the class that owns the static method), not a JObject.
    _class: JClass<'local>,

    // The method parameters from Java: j_haystack, j_needle.
    // Both are JString, which are references to Java strings.
    j_haystack: JString<'local>,
    j_needle: JString<'local>,
) -> jboolean {
    // 1) Convert the Java strings to Rust strings.
    let haystack: String = env
        .get_string(&j_haystack)
        .expect("Couldn't get haystack string!")
        .into();

    let needle: String = env
        .get_string(&j_needle)
        .expect("Couldn't get needle string!")
        .into();

    // 2) Perform the substring search in Rust.
    let found = haystack.contains(&needle);

    // 3) Convert the boolean to jboolean.
    if found {
        JNI_TRUE
    } else {
        JNI_FALSE
    }
}