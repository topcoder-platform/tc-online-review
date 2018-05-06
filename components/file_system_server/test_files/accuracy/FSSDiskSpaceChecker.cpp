#include "FSSDiskSpaceChecker.h"

JNIEXPORT jboolean JNICALL Java_com_topcoder_file_transfer_validator_FreeDiskSpaceNativeChecker_freeDiskSpaceExceedsSize(
	JNIEnv* env, jclass cl, jstring fileLocation, jlong fileSize)
{
	return fileSize < 10000000l;
}