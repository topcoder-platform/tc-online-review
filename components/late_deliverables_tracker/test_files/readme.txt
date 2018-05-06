1). create database tcs with buffered log

2). populate tables with test_files/all.sql

3). modify the following files according to your local database setting:

    config/DB_Factory.xml
    config/LateDeliverableProcessorImpl.xml
    config/LateDeliverablesTracker.xml
    config/LateDeliverablesTrackingUtility.xml
    invalid_config/LateDeliverableProcessorImpl*.xml
    invalid_config/LateDeliverablesTracker.xml
    invalid_config/LateDeliverablesTrackingUtility.xml
    
    
4). start devnullsmtp.jar


The tests for the LateDeliverablesTrackingUtility is not complete, please refer here

http://forums.topcoder.com/?module=Thread&threadID=686716&start=0

the codes change and there are only two hours left. Tests for old codes is useless(and it is impossible to have
the accuracy tests).