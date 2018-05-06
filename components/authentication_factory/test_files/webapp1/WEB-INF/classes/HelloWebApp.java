import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;

/** Simple servlet used from a Web application.
 *  <P>
 *  Taken from More Servlets and JavaServer Pages
 *  from Prentice Hall and Sun Microsystems Press,
 *  http://www.moreservlets.com/.
 *  &copy; 2002 Marty Hall; may be freely used or adapted. 
 */

public class HelloWebApp extends HttpServlet {
  public void doGet(HttpServletRequest request,
                    HttpServletResponse response)
      throws ServletException, IOException {
    response.setContentType("text/html");
	response.sendRedirect("redirectServlet1.html");
    /*PrintWriter out = response.getWriter();
    String docType =
      "<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.0 " +
      "Transitional//EN\">\n";
    String title = "Servlet: Hello Web App";
    out.println(docType +
                "<HTML>\n" +
                "<HEAD><TITLE>" + title + "</TITLE></HEAD>\n" +
                "<BODY BGCOLOR=\"#FDF5E6\">\n" +
                "<H1>" + title + "</H1>\n" +
                "</BODY></HTML>");*/
	
  }
}
