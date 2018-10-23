<%@ page import="com.topcoder.sql.wrapper.*,
                 java.io.StringWriter,
                 java.io.PrintWriter,
                 java.util.List,
                 java.util.ArrayList"%>
<%
    String task = request.getParameter("t");

    if ("stackTrace".equalsIgnoreCase(task)) {
        ConnectionWrapper c = null;
        try {
            c = ConnectionWrapper.getConnectionWrapper(new Long(request.getParameter("connectionId")));
        } catch (Exception ignore) {}
        StatementWrapper s = null;
        try {
            s = StatementWrapper.getStatementWrapper(new Long(request.getParameter("statementId")));
        } catch (Exception ignore) {}
        ResultSetWrapper r = null;
        try {
            r = ResultSetWrapper.getResultSetWrapper(new Long(request.getParameter("resultSetId")));
        } catch (Exception ignore) {}
        if (c != null) {
            out.print("<pre>");
            out.println("Age: " + c.getAge() + " millis");
            out.println(c.toString() + "(" + c.getConnection().toString() + ")");
            c.getCreationStackTrace().printStackTrace(new PrintWriter(out));
            out.print("</pre>");
        } else if (s != null) {
            out.print("<pre>");
            out.println("Age: " + s.getAge() + " millis     Status: " + (s.isClosed() ? "CLOSED" : "OPEN"));
            out.println(s.toString() + "(" + s.getStatement().toString() + ")");
            s.getCreationStackTrace().printStackTrace(new PrintWriter(out));
            out.print("</pre>");
        } else if (r != null) {
            out.print("<pre>");
            out.println("Age: " + r.getAge() + " millis     Status: " + (r.isClosed() ? "CLOSED" : "OPEN"));
            out.println(r.toString() + "(" + r.getStatement().toString() + ")");
            r.getCreationStackTrace().printStackTrace(new PrintWriter(out));
            out.print("</pre>");
        }
    } else {
        if ("close".equalsIgnoreCase(task)) {
            String id[] = request.getParameterValues("resultSetId");
            if (id != null) {
                for (int i = 0; i < id.length; i++) {
                    try {
                        ResultSetWrapper rsw = ResultSetWrapper.getResultSetWrapper(new Long(id[i]));
                        try {
                            if (rsw != null && !rsw.isClosed()) rsw.close();
                        } catch (Exception e) {
                            out.println("<pre>");
                            out.println("Exception closing ResultSetWrapper with id: " + id[i]);
                            e.printStackTrace(new PrintWriter(out));
                            out.println("=========================================================</pre>");
                        }
                    } catch (Exception ignore) {}
                }
            }
            id = request.getParameterValues("statementId");
            if (id != null) {
                for (int i = 0; i < id.length; i++) {
                    try {
                        StatementWrapper sw = StatementWrapper.getStatementWrapper(new Long(id[i]));
                        try {
                            if (sw != null && !sw.isClosed()) sw.close(true);
                        } catch (Exception e) {
                            out.println("<pre>");
                            out.println("Exception closing StatementWrapper with id: " + id[i]);
                            e.printStackTrace(new PrintWriter(out));
                            out.println("=========================================================</pre>");
                        }
                    } catch (Exception ignore) {}
                }
            }
            // Don't do connections
        }
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        Utility util = new Utility();
        util.printAll(printWriter);
%>
Automatically selected all objects older than <%= util.getMaxAge() %> milliseconds.<p>
<form action="driverWrapper.jsp?t=close" method="post">
<input type="submit" value="Close Selected">
<table><%= stringWriter.toString() %></table>
<input type="submit" value="Close Selected">
<% } %>