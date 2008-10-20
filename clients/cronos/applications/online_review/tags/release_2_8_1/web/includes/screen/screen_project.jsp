<%@ taglib prefix="html" uri="/tags/struts-html" %>
<%
   String role = (String) request.getParameter("role");
%>
					<div style="padding: 11px 6px 9px 0px; ">
						<table border="0" cellpadding="0" cellspacing="0" width="100%">
							<tr>
								<td>
									<html:img src="/i/iconStatusSpecSm.gif" alt="" width="25" height="17" border="0" />
									<html:img src="/i/javaSm.gif" alt="" border="0" /><span class="bodyTitle">Ajax Timed Survey </span><font size="4">version 1.0</font><br>
								</td>
								<td align="right" valign="top">
							<% if (role.equalsIgnoreCase("manager")) {%>
									<a href="pc_edit_screening_scorecard.jsp?role=manager&projectTabIndex=0">Edit Scorecard</a>&#160;|
							<% } %>
									&#160;<A href="javascript:showAll();">Expand All</A>&#160;|
									&#160;<A href="javascript:hideAll();">Collapse All</A>
								</td>
							</tr>
						</table>
					</div>
					&nbsp;<b>Screener:</b> <a href="#" class="coderTextRed">ScreenerHandle</a><br>
					&nbsp;<b>Submission:</b> 21219629 <% if (role.equalsIgnoreCase("manager")) {%>(<a href="#" class="coderTextYellow">SubmitterHandle</a>)<% } %><br>
					&nbsp;<b>My Role:</b> <%=role%><br>