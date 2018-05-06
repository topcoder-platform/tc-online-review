#include<jni.h>
#ifndef _Test
#ifdef __cplusplus
extern "C" {
#endif
	JNIEXPORT jboolean JNICALL Java_com_topcoder_file_transfer_validator_FreeDiskSpaceNativeChecker_freeDiskSpaceExceedsSize(
		JNIEnv* env, jclass cl, jstring fileLocation, jlong fileSize);
#ifdef __cplusplus
}
#endif
#endif