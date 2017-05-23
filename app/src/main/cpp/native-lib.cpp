#include <jni.h>
#include <string>

extern "C"
jstring
Java_com_fionera_demo_activity_ConstraintLayoutActivity_stringFromJNI(
        JNIEnv *env,
        jobject instance) {
    std::string hello = "Hello from C++";
    return env->NewStringUTF(hello.c_str());
}

extern "C"
jint
Java_com_fionera_demo_activity_ConstraintLayoutActivity_addNumberUsingJNI(JNIEnv *env,
                                                                          jobject instance, jint a,
                                                                          jint b) {
    int result = a + b;
    return result;
}
