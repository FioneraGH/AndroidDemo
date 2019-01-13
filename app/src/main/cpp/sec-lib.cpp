#include <jni.h>
//#include <string>

#include "include/sec-lib.h"

extern "C"
jstring
Java_com_fionera_demo_activity_ConstraintLayoutActivity_stringFromJNI(
        JNIEnv *env,
        jobject instance) {
//    std::string hello = HELLO_WORDS;
    return env->NewStringUTF(HELLO_WORDS);
}

extern "C"
jint
Java_com_fionera_demo_activity_ConstraintLayoutActivity_addNumberUsingJNI(JNIEnv *env,
                                                                          jobject instance, jint a,
                                                                          jint b) {
    int result = a + b;
    return result;
}