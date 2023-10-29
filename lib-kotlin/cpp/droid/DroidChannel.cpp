#include <droid/DroidChannel.h>
#include <jni.h>


extern "C"
JNIEXPORT jint JNICALL
Java_com_jetbrains_kmm_shared_Reaktor_readInt(JNIEnv *env, jobject thiz) {
    return 42;
}