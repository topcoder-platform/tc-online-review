mock classes in stress_mocks.jar


StressTest Strategy:

in NextRunStressTests:
I calculated the nextRun time slowly but accurately, that is, to add SECOND/MINUTE/HOUR/DATE/MONTH/YEAR one by one.
whenever I met a correct date, I decrease the IntervalValue by 1.
When IntervalValue is 0, I have come to the correct date.

in ExecutionStressTests:
I forced the scheduled nextRun time to be one second later. And all the TIMES(=30) jobs must be executed then.

in TriggerStressTests:
I opened TIMES(=30) jobs, each one with a single dependent job set. A total of 60 jobs are expected to be executed.

