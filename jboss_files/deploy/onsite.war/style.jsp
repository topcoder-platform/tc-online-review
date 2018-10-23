<%@ page contentType="text/html" %>

<%
    String key = request.getParameter("key");
%>
<% if (key.equals("tc_old")) { %>
<link type="text/css" rel="stylesheet" href="/css/style.css" />
<link type="text/css" rel="stylesheet" href="/css/coders.css" />
<link type="text/css" rel="stylesheet" href="/css/TCOstyle.css" />
<% } else if (key.equals("tc_home")) { %>
<link type="text/css" rel="stylesheet" href="/css/style.css" />
<link type="text/css" rel="stylesheet" href="/css/coders.css" />
<link type="text/css" rel="stylesheet" href="/css/tcStyles20080219.css" />
<link type="text/css" rel="stylesheet" href="/css/rightNav20080519.css" />
<% } else if (key.equals("tc_calendar")) { %>
<link type="text/css" rel="stylesheet" href="/css/coders.css" />
<link type="text/css" rel="stylesheet" href="/css/tcCalendar.css" />
<link type="text/css" rel="stylesheet" href="/css/tcStyles20080219.css" />
<% } else if (key.equals("crpf")) { %>
<link type="text/css" rel="stylesheet" href="/css/crpfStyle.css" />
<link type="text/css" rel="stylesheet" href="/css/style.css" />
<link type="text/css" rel="stylesheet" href="/css/coders.css" />
<link type="text/css" rel="stylesheet" href="/css/stats.css" />
<link type="text/css" rel="stylesheet" href="/css/tcStyles20080219.css" />
<% } else if (key.equals("tc_forums")) { %>
<link type="text/css" rel="stylesheet" href="/css/style.css" />
<link type="text/css" rel="stylesheet" href="/css/coders.css" />
<link type="text/css" rel="stylesheet" href="/css/roundTables.css" />
<link type="text/css" rel="stylesheet" href="/css/stats.css" />
<link type="text/css" rel="stylesheet" href="/css/tcStyles20080219.css" />
<% } else if (key.equals("tc_main")) { %>
<link type="text/css" rel="stylesheet" href="/css/style.css" />
<link type="text/css" rel="stylesheet" href="/css/coders.css" />
<link type="text/css" rel="stylesheet" href="/css/tcStyles20080219.css" />
<link type="text/css" rel="stylesheet" href="/css/jobsRemix.css" />
<% } else if (key.equals("tc_reg")) { %>
<link type="text/css" rel="stylesheet" href="/css/reg/regReskin20080904.css" />
<!--[if IE]>
<link type="text/css" rel="stylesheet" href="/css/reg/ie.css" />
<![endif]-->
<%-- old reg style 
<link type="text/css" rel="stylesheet" href="/css/tcStyles20080219.css" />
<link type="text/css" rel="stylesheet" href="/css/tcReg.css" />
--%>
<% } else if (key.equals("tc_copilot")) { %>
<link type="text/css" rel="stylesheet" href="/css/style.css" />
<link type="text/css" rel="stylesheet" href="/css/coders.css" />
<link type="text/css" rel="stylesheet" href="/css/copilot-pool.css" />
<link type="text/css" rel="stylesheet" href="/css/copilot-pool_ie6.css" />
<link type="text/css" rel="stylesheet" href="/css/tcStyles20080219.css" />
<% } else if (key.equals("tc_stats")) { %>
<link type="text/css" rel="stylesheet" href="/css/style.css" />
<link type="text/css" rel="stylesheet" href="/css/coders.css" />
<link type="text/css" rel="stylesheet" href="/css/stats.css" />
<link type="text/css" rel="stylesheet" href="/css/tcStyles20080219.css" />
<link type="text/css" rel="stylesheet" href="/css/login.css" />
<!--[if IE]>
<link type="text/css" rel="stylesheet" href="/css/ie.css" />
<![endif]-->
<% } else if (key.equals("tc_studio")) { %>
<link type="text/css" rel="stylesheet" href="/css/v2/main20080522.css" />
<% } else if (key.equals("tc_studio_forums")) { %>
<link type="text/css" rel="stylesheet" href="/css/v2/main20080522.css" />
<link type="text/css" rel="stylesheet" href="/css/forums.css" />
<% } else if (key.equals("studio_digitalrun")) { %>
<link type="text/css" rel="stylesheet" href="/css/v2/main20080522.css" />
<link type="text/css" rel="stylesheet" href="/css/digitalrun/main.css" />
<% } else if (key.equals("csf")) { %>
<link type="text/css" rel="stylesheet" href="/css/main.css" />
<% } else if (key.equals("csfforums")) { %>
<link type="text/css" rel="stylesheet" href="/css/main.css" />
<link type="text/css" rel="stylesheet" href="/css/forums.css" />
<% } else if (key.equals("tc_pressroom")) { %>
<link type="text/css" rel="stylesheet" href="/css/tcStyles20080219.css" />
<link type="text/css" rel="stylesheet" href="/css/coders.css" />
<link type="text/css" rel="stylesheet" href="/css/pressroomRemix.css" />
<% } else if (key.equals("digitalrun")) { %>
<link type="text/css" rel="stylesheet" href="/css/digitalrun/main.css" />
<link type="text/css" rel="stylesheet" href="/css/tcStyles20080219.css" />
<link type="text/css" rel="stylesheet" href="/css/coders.css" />
<% } else if (key.equals("tc_ep")) { %>
<link type="text/css" rel="stylesheet" href="/css/ep/main.css" />
<link type="text/css" rel="stylesheet" href="/css/coders.css" />
<% } else if (key.equals("tc_openaim")) { %>
<link type="text/css" rel="stylesheet" href="/css/openaim/main20080416.css" />
<link type="text/css" rel="stylesheet" href="/css/coders.css" />
<% } else if (key.equals("tc_openaim_forums")) { %>
<link type="text/css" rel="stylesheet" href="/css/openaim/forums.css" />
<link type="text/css" rel="stylesheet" href="/css/openaim/main20080416.css" />
<link type="text/css" rel="stylesheet" href="/css/coders.css" />
<% } else if (key.equals("tc_truveo")) { %>
<link type="text/css" rel="stylesheet" href="/css/truveo/main.css" />
<link type="text/css" rel="stylesheet" href="/css/coders.css" />
<% } else if (key.equals("tc_truveo_forums")) { %>
<link type="text/css" rel="stylesheet" href="/css/truveo/forums.css" />
<link type="text/css" rel="stylesheet" href="/css/truveo/main.css" />
<link type="text/css" rel="stylesheet" href="/css/coders.css" />
<% } else if (key.equals("tc_winformula")) { %>
<link type="text/css" rel="stylesheet" href="/css/winformula/screen.css" />
<link type="text/css" rel="stylesheet" href="/css/coders.css" />
<% } else if (key.equals("tc_winformula_forums")) { %>
<link type="text/css" rel="stylesheet" href="/css/winformula/forums.css" />
<link type="text/css" rel="stylesheet" href="/css/winformula/screen.css" />
<link type="text/css" rel="stylesheet" href="/css/coders.css" />
<% } else if (key.equals("tc_aolicq")) { %>
<link type="text/css" rel="stylesheet" href="/css/aolicq/main.css" />
<link type="text/css" rel="stylesheet" href="/css/coders.css" />
<% } else if (key.equals("tc_aolicq_forums")) { %>
<link type="text/css" rel="stylesheet" href="/css/aolicq/forums.css" />
<link type="text/css" rel="stylesheet" href="/css/aolicq/main.css" />
<link type="text/css" rel="stylesheet" href="/css/coders.css" />
<% } %>
