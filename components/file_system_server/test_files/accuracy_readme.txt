For test only
the freeDiskSpaceExceedsSize in FSSDiskSpaceChecker.dll 
is simple return fileSize < 10000000l, not call the API
to check the physical free disk space.

if change of freeDiskSpaceExceedsSize,
please update AccuracyTestHelper.MAX_FREE_DISKSPACE