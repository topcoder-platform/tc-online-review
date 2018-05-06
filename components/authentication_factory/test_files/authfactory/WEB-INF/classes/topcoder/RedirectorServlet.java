package topcoder;
import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;


/** Servlet that simply redirects users to the
* Web application home page. Registered with the
* default servlet URL to prevent access to servlets
* through URLs that have no security settings.
*/
public class RedirectorServlet extends HttpServlet {
    public void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        response.sendRedirect(request.getContextPath());
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        doGet(request, response);
    }
}