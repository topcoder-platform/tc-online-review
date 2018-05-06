dear reviewer,

1.since there is no configuration requirement in this v2.0, so in cs, I have removed those matter related with 
configuration. And Log4j-1.2.12 or higher should be used now. It has no backward compatibility any more.
please see the forum, thanks.

2. I have added @since 2.0 to each new method(including those method signture is changed) and @since 1.2 to each overrided
or unchanged method. If the class is new, It only need to add @version 2.0 to the document class and is no need to add 
@since 2.0 to each method, otherwise, both @since 1.2 and @version 2.0 are added to the modifed class.

3. The configurations for jdk14log and log4j are done by programming, the files in test_files/config directory are only for
reviewers and users.

Thanks for your review job.