<%
   String role = (String) request.getParameter("role");
%>
<% if (!role.equalsIgnoreCase("reviewer")) {%>
					<div style="padding: 11px 6px 9px 0px; ">
						<table border="0" cellpadding="0" cellspacing="0" width="100%">
							<tr>
								<td>
									<img src="../images/iconStatusSpecSm.gif" alt="" width="25" height="17" border="0" >
									<img src="../images/javaSm.gif" alt="" border="0"><span class="bodyTitle">Ajax Timed Survey </span><font size="4">version 1.0</font><br>
								</td>
								<td align="right" valign="top">
							<% if (role.equalsIgnoreCase("manager")) {%>
									<a href="pc_edit_review_scorecard.jsp?role=manager&projectTabIndex=0">Edit Scorecard</a>&#160;|
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
<% } else { %>
					<div style="padding: 11px 6px 9px 0px; ">
						<table border="0" width="100%" id="table1" cellspacing="0" cellpadding="0">
							<tr>
								<td width="6%">
									<img src="../images/iconStatusSpecSm.gif" alt="" width="25" height="17" border="0" >
									<img src="../images/javaSm.gif" alt="" border="0">&nbsp;
								</td>
								<td width="45%">
									<strong><font size="3" >Ajax Timed Survey</font></strong><font size="3">version 1.0</font><br>
								</td>
								<td width="45%">
									<p align="right">
									&nbsp;<A href="javascript:showAll();">Expand All</A>&#160;|
									&#160;<A href="javascript:hideAll();">Collapse All</A>
								</td>
							</tr>
						</table>
						<b><br>
						Submission:</b> 21219629 (<a href="#" class="coderTextYellow">henryouly</a>)&nbsp;<b><br>
						My Role:</b> Reviewer
					</div>
<%}%>