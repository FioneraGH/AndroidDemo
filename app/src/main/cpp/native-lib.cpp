#include <jni.h>
#include <string>

extern "C"
jstring
Java_com_fionera_demo_activity_ConstraintLayoutActivity_stringFromJNI(
        JNIEnv* env,
        jobject /* this */) {
    std::string hello = "Hello from C++";
    return env->NewStringUTF(hello.c_str());
}
