Hi, dear reviewer,

First of all, wish you have a nice day.

To run my test cases and demos, here are something you should know.

1. Make sure you tomcat home and cactus tmp directory path doesn't contain space. The jaxp parser will fail in this situation when it reads the schema file. (if it has, you can just use "set tmp={some dir not has space in path}" in the console to avoid this.).

2. Make sure your tomcat is with version 5.0+ .

3. I provide a sample build.xml in this directory, you can use it if you want :-)

4. I also provide a war that contains all the demos. You can simply put it into {TOMCAt_HOME}/webapps/ and then restart tomcat. The demo url is not very easy so I give a shortcut here. 

	http://localhost:8080/online_review_ajax_support-test/demoX.html (where X is from 1 to 6).

5. Note that, since the AjaxSupportServlet needs to read the configuration before tomcat load it, so I write another servlet called DemoServlet to load configuration first and then simply delegate the operations to the AjaxSupportServlet. This is the demo helper.

All right, that's all. Happy reviewing.