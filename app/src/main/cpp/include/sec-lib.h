#ifndef ANDROID_DEMO_SEC_LIB_H
#define ANDROID_DEMO_SEC_LIB_H

#define HELLO_WORDS "Hello from C++"

#include "common.h"

#ifdef __cplusplus
extern "C" {
#endif

jstring
Java_com_fionera_demo_activity_ConstraintLayoutActivity_stringFromJNI(
        JNIEnv *env,
        jobject instance);

jint
Java_com_fionera_demo_activity_ConstraintLayoutActivity_addNumberUsingJNI(JNIEnv *env,
                                                                          jobject instance, jint a,
                                                                          jint b);

#ifdef __cplusplus
};
#endif

#endif //ANDROID_DEMO_SEC_LIB_H
