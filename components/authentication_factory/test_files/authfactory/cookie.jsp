<%
for (int i = 0; i < 10; ++i) {
	Cookie cookie = new Cookie("user" + i, "pwd" + i);
	cookie.setMaxAge(3600);
	response.addCookie(cookie);
}
response.setContentType("text/html");
out.println("<html><body>cookies</body></html>");
%>
