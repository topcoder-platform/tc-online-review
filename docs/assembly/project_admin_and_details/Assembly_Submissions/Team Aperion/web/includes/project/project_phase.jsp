<%
    //                        0  1  2  3  4  5  6  7  8  9  10
	int[] phases = new int[] {0, 1, 2, 3, 3, 4, 5, 5, 6, 7, 7};
	int phaseIndex = ((Integer)request.getAttribute("phaseIndex")).intValue();
	phaseIndex = phases[phaseIndex];
	String role = (String) request.getAttribute("role");
%>
					<a name="tabs"></a>
					<div id="tabcontentcontainer">
						<!-- REGISTRATION TAB -->
						<div id="sc1" class="tabcontent">
							<ul id="tablist">
								<li id="current"><a href="#tabs" onClick="return expandcontent('sc1', this)">Registration</a></li>
								<li><a href="#tabs" onClick="return expandcontent('sc2', this)">Submission/Screening</a></li>
								<li><a href="#tabs" onClick="return expandcontent('sc3', this)">Review/Appeals</a></li>
								<%if (phaseIndex == 7) {%> <li><a href="#tabs" onClick="return expandcontent('sc7', this)">Approval</a></li>  <% } %>
								<li><a href="#tabs" onClick="return expandcontent('sc4', this)">Aggregation/Review</a></li>
								<li><a href="#tabs" onClick="return expandcontent('sc5', this)">Final Fix/Review</a></li>
								<li><a href="#tabs" onClick="return expandcontent('sc6', this)">Approval<%if (phaseIndex == 7) {%> 2<% } %></a></li>
							</ul>
							<div style="clear:both;"></div>
<%if (phaseIndex == 1) {%>
							<table class="scorecard" style="border-collapse: collapse;"cellpadding="0" cellspacing="0" width="100%">
								<tr>
									<td class="title" colspan="5">Registrants</td>
								</tr>
								<tr>
									<td class="header">Handle</td>
									<td class="header">Email</td>
									<td class="header">Reliability</td>
									<td class="header">Rating</td>
									<td class="headerC">Registration Date</td>
								</tr>
								<tr class="light">
									<td class="value" nowrap><a href="#" class="coderTextYellow">henryouly</a></td>
									<td class="value"><a href="mailto:henryouly@email.com">henryouly@email.com</a></td>
									<td class="value" nowrap>83%</td>
									<td class="value" nowrap><span class="coderTextYellow">1532</span></td>
									<td class="valueC" nowrap>00.00.00 00:00 AM</td>
								</tr>
								<tr class="dark">
									<td class="value"><a href="#" class="coderTextBlack">Tavo</a></td>
									<td class="value"><a href="mailto:Tavo@email.com">Tavo@email.com</a></td>
									<td class="value" nowrap>N/A</td>
									<td class="value" nowrap><span class="coderTextBlack">Not Rated</td>
									<td class="valueC" nowrap>00.00.00 00:00 AM</td>
								</tr>
								<tr class="light">
									<td class="value" nowrap><a href="#" class="coderTextYellow">mayi</a></td>
									<td class="value" nowrap><a href="mailto:mayi@yahoo.com">mayi@yahoo.com</a></td>
									<td class="value" nowrap>80%</td>
									<td class="value" nowrap><span class="coderTextYellow">1599</span></td>
									<td class="valueC" nowrap>00.00.00 00:00 AM</td>
								</tr>
								<tr>
									<td class="lastRowTD" colspan="5"></td>
								</tr>
							</table>
<%}%>
						</div>

						<!-- SUBMISSION/SCREENING TAB -->
						<div id="sc2" class="tabcontent">
							<ul id="tablist">
								<li><a href="#tabs" onClick="return expandcontent('sc1', this)">Registration</a></li>
								<li id="current"><a href="#tabs" onClick="return expandcontent('sc2', this)">Submission/Screening</a></li>
								<li><a href="#tabs" onClick="return expandcontent('sc3', this)">Review/Appeals</a></li>
								<%if (phaseIndex == 7) {%> <li><a href="#tabs" onClick="return expandcontent('sc7', this)">Approval</a></li>  <% } %>
								<li><a href="#tabs" onClick="return expandcontent('sc4', this)">Aggregation/Review</a></li>
								<li><a href="#tabs" onClick="return expandcontent('sc5', this)">Final Fix/Review</a></li>
								<li><a href="#tabs" onClick="return expandcontent('sc6', this)">Approval<%if (phaseIndex == 7) {%> 2<% } %></a></li>
							</ul>
							<div style="clear:both;"></div>
<%if (phaseIndex >=2) {%>
							<table class="scorecard" style="border-collapse: collapse;"cellpadding="0" cellspacing="0" width="100%">
								<tr>
									<td class="title" colspan="7">Submission/Screening</td>
								</tr>
								<tr>
									<td class="header" colspan="2"><b>Submission ID</b></td>
									<td class="header" width="22%"><b>Submission Date</b></td>
									<td class="headerC" nowrap width="14%"><b>Auto Screening</b></td>
									<td class="headerC" nowrap width="15%" style="text-align: left"><b>Screener</b></td>
									<td class="headerC" width="14%"><b>Screening Score</b></td>
									<td class="headerC" width="15%"><b>Screening&nbsp; Result</b></td>
								</tr>
								<tr class="light">
									<td class="value" nowrap width="10%" >
										<img ID="Out1" CLASS="Outline" border="0" src="../i/plus.gif" width="9" height="9" style="margin-right:5px" title="View Previous Submissions">
										<img border="0" src="../i/icon_gold.gif" class="Outline" alt="Winner">
											<a href="#" title="Download Submission">11889718 </a>(<a href="#" class="coderTextYellow">henryouly</a>)
									</td>
									<td class="value" width="5%"><a href="#"><img border="0" src="../i/icon_trash.gif" class="Outline" width="10" height="10" alt="Remove this Submission"></a></td>
									<td class="value" nowrap width="22%">00.00.0000 00:00 AM</td>
									<td class="valueC" nowrap width="14%"><a target="_top" href="pc-manager-screening_results.jsp">
										<img border="0" src="../i/icon_authorization.gif" class="Outline" alt="Passed" width="10" height="8"></a>
									</td>
									<td class="valueC" width="15%" style="text-align: left"><a href="#" class="coderTextRed">WishingBone</a></td>
									<td class="valueC" width="14%"><a href="pc-manager-screening_results.jsp">98.34</a></td>
									<td class="valueC" width="15%">Passed</td>
								</tr>
<%
	if (role.equalsIgnoreCase("manager")) {
%>
								<tr class="dark">
									<td class="value" nowrap width="10%">
										<img ID="Out1" CLASS="Outline" border="0" src="../i/plus.gif" width="9" height="9" style="margin-right:5px" title="View Previous Submissions"><img border="0" src="../i/icon_bronze.gif" class="Outline" alt="3rd Place">
										<a href="#" title="Download Submission">14818660 (Tavo)</a>
									</td>
									<td class="value" width="5%"><a href="#"><img border="0" src="../i/icon_trash.gif" class="Outline" width="10" height="10" alt="Remove this Submission"></a></td>
									<td class="value" nowrap width="22%">00.00.0000 00:00 AM</td>
									<td class="valueC" nowrap width="14%"><a target="_top" href="pc-manager-warnings.jsp">
										<img border="0" src="../i/icon_authorization2.gif" alt="Passed with Warnings" width="10" height="8"></a>
									</td>
									<td class="valueC" width="15%" style="text-align: left"><a href="#" class="coderTextRed">WishingBone</a></td>
									<td class="valueC" width="14%"><a href="pc-manager-screening_results.jsp">99.59</a></td>
									<td class="valueC" width="15%">Passed</td>
								</tr>
								<tr class="light">
									<td class="value" nowrap width="10%">
										<img ID="Out1" CLASS="Outline" border="0" src="../i/plus.gif" width="9" height="9" style="margin-right:5px" title="View Previous Submissions"><img border="0" src="../i/icon_silver.gif" class="Outline" alt="2nd Place">
										<a href="#" title="Download Submission">15655112 </a> (<a href="#" class="coderTextYellow">mayi</a>)
									</td>
									<td class="value" width="5%"><a href="#"><img border="0" src="../i/icon_trash.gif" class="Outline" width="10" height="10" alt="Remove this Submission"></a></td>
									<td class="value" nowrap width="22%">00.00.0000 00:00 AM</td>
									<td class="valueC" nowrap width="14%"><a target="_top" href="pc-manager-warnings.jsp">
										<img border="0" src="../i/icon_authorization2.gif" alt="Passed with Warnings" width="10" height="8"></a>
									</td>
									<td class="valueC" width="15%" style="text-align: left"><a href="#" class="coderTextRed">oldbig</a></td>
									<td class="valueC" width="14%"><a href="pc-manager-screening_results.jsp">100.00</a></td>
									<td class="valueC" width="15%">Passed</td>
								</tr>
<%
	}
%>
								<tr>
									<td class="lastRowTD" colspan="7"></td>
								</tr>
							</table>
<% } else {%>
							<table class="scorecard" style="border-collapse: collapse;"cellpadding="0" cellspacing="0" width="100%">
								<tr>
									<td class="title">Submission/Screening</td>
								</tr>
								<tr class="light">
									<td class="valueC" nowrap>This Phase is not yet Open.</td>
								</tr>
								<tr>
									<td class="lastRowTD"></td>
								</tr>
							</table>
<% } %>
						</div>

						<!-- REVIEW/APPEALS TAB-->
						<div id="sc3" class="tabcontent">
							<ul id="tablist">
								<li><a href="#tabs" onClick="return expandcontent('sc1', this)">Registration</a></li>
								<li><a href="#tabs" onClick="return expandcontent('sc2', this)">Submission/Screening</a></li>
								<li id="current"><a href="#tabs" onClick="return expandcontent('sc3', this)">Review/Appeals</a></li>
								<%if (phaseIndex == 7) {%> <li><a href="#tabs" onClick="return expandcontent('sc7', this)">Approval</a></li>  <% } %>
								<li><a href="#tabs" onClick="return expandcontent('sc4', this)">Aggregation/Review</a></li>
								<li><a href="#tabs" onClick="return expandcontent('sc5', this)">Final Fix/Review</a></li>
								<li><a href="#tabs" onClick="return expandcontent('sc6', this)">Approval<%if (phaseIndex == 7) {%> 2<% } %></a></li>
							</ul>
							<div style="clear:both;"></div>
<%if (phaseIndex >= 3) {%>
							<table class="scorecard" style="border-collapse: collapse;"cellpadding="0" cellspacing="0" width="100%">
								<tr>
									<td class="title">Review</td>
								</tr>
							</table>
							<table class="scorecard" cellpadding="0" width="100%" style="border-collapse: collapse;" id="table14">
								<tr>
									<td class="valueC" nowrap>&nbsp;</td>
									<td class="valueC" nowrap width="4%">&nbsp;</td>
									<td class="valueC" nowrap width="7%">&nbsp;</td>
									<td class="valueC" nowrap width="13%">&nbsp;</td>
<% if (role.equalsIgnoreCase("reviewer")) {%>
									<td class="valueC" nowrap colspan="6">
										<b>Stress: </b><a href="#" title="Download Test Case">Test Case</a>(<a href="#" class="coderTextGreen">qiucx0161</a>)
<%} else {%>
									<td class="valueC" nowrap colspan="2">
										<b>Stress: </b><a href="#" title="Download Test Case">Test Case</a>(<a href="#" class="coderTextGreen">qiucx0161</a>)
									</td>
									<td class="valueC" nowrap colspan="2">
										<b>Failure: </b><a href="#" title="Download Test Case">Test Case</a>(<a href="#" class="coderTextYellow">mgmg</a>)
									</td>
									<td class="valueC" nowrap colspan="2">
										<b>Accuracy: </b><a href="#" title="Download Test Case">Test Case</a>(<a href="#" class="coderTextYellow">slion</a>)
									</td>
<%}%>
								</tr>
								<tr>
									<td class="header" nowrap>Submission ID</td>
									<td class="headerC" nowrap width="4%">Status</td>
									<td class="headerC" nowrap width="7%">Review <br>Date</td>
									<td class="headerC" nowrap width="13%"><b>Review<br>Score</b></td>
									<td class="headerC" nowrap width="9%"><b>Score</b></td>
									<td class="headerC" nowrap width="12%"><b>Appeals</b></td>
									<td class="headerC" nowrap width="8%"><b>Score</b></td>
									<td class="headerC" nowrap width="11%"><b>Appeals</b></td>
									<td class="headerC" nowrap width="9%"><b>Score</b></td>
									<td class="headerC" nowrap width="11%"><b>Appeals</b></td>
								</tr>
								<tr class="light">
									<td class="value" nowrap>
										<img border="0" src="../i/icon_gold.gif" class="Outline" alt="Winner">
										<a href="#" title="Download Submission">11889718 </a>(<a href="#" class="coderTextYellow">henryouly</a>)
									</td>
									<td class="valueC" nowrap width="4%">
										<img border="0" src="../i/icon_authorization.gif" class="Outline" alt="Complete" width="10" height="8">
									</td>
									<td class="valueC" nowrap width="7%">00.00.00<br>00:00 AM</td>
									<td class="valueC" nowrap width="13%"><a target="_top" href="pc-readonly-review_results.jsp">98.37</a></td>
									<td class="valueC" nowrap width="9%"><a href="pc-readonly-review_individual_all_resolved.jsp">9</a><a target="_top" href="pc-readonly-review_individual_all_resolved.jsp">9.31</a></td>
									<td class="valueC" nowrap width="12%" >[ <a target="_top" href="pc-readonly-review_individual_all_resolved.jsp" title="Unresolved">0</a> / <a target="_top" title="Total" href="pc-readonly-review_individual_all_resolved.jsp">7</a> ]</td>
									<td class="valueC" nowrap width="8%">
										<a href="pc-readonly-review_individual_all_resolved.jsp">9</a>
										<a target="_top" href="pc-readonly-review_individual_all_resolved.jsp">1.75</a>
									</td>
									<td class="valueC" nowrap width="11%">
										[ <a target="_top" href="pc-readonly-review_individual_all_resolved.jsp" title="Unresolved">0</a> /
										<a target="_top" title="Total" href="pc-readonly-review_individual_all_resolved.jsp">7</a> ]
									</td>
									<td class="valueC" nowrap width="9%">
										<a href="pc-readonly-review_individual_all_resolved.jsp">
										98</a><a target="_top" href="pc-readonly-review_individual_all_resolved.jsp">.00</a>
									</td>
									<td class="valueC" nowrap width="11%" >
										[ <a target="_top" href="pc-readonly-review_individual_all_resolved.jsp" title="Unresolved">0</a> /
										<a target="_top" title="Total" href="pc-readonly-review_individual_all_resolved.jsp">7</a> ]
									</td>
								</tr>
<%
	if (role.equalsIgnoreCase("manager") || role.equalsIgnoreCase("reviewer")) {
%>
								<tr class="dark">
									<td class="value" nowrap>
										<img border="0" src="../i/icon_bronze.gif" class="Outline" alt="3rd Place">
										<a href="#" title="Download Submission">14818660 </a>(<a href="#" class="coderTextBlack">Tavo</a>)
									</td>
									<td class="valueC" nowrap width="4%">
										<img border="0" src="../i/icon_authorization.gif" class="Outline" alt="Complete" width="10" height="8">
									</td>
									<td class="valueC" nowrap width="7%">00.00.00<br>00:00 AM</td>
									<td class="valueC" nowrap width="13%">
										<a target="_top" href="pc-readonly-review_results.jsp">88.21</a>
									</td>
									<td class="valueC" nowrap width="9%">
										<a target="_top" href="pc-readonly-review_individual_all_resolved.jsp">87.21</a>
									</td>
									<td class="valueC" nowrap width="12%" >
										[ <a target="_top" href="pc-readonly-review_individual_all_resolved.jsp" title="Unresolved">0</a> /
										<a target="_top" title="Total" href="pc-readonly-review_individual_all_resolved.jsp">7</a> ]
									</td>
									<td class="valueC" nowrap width="8%">
										<a href="pc-readonly-review_individual_all_resolved.jsp">8</a>
										<a target="_top" href="pc-readonly-review_individual_all_resolved.jsp">8.73</a>
									</td>
									<td class="valueC" nowrap width="11%">
										[ <a target="_top" href="pc-readonly-review_individual_all_resolved.jsp" title="Unresolved">0</a> /
										<a target="_top" title="Total" href="pc-readonly-review_individual_all_resolved.jsp">7</a> ]
									</td>
									<td class="valueC" nowrap width="9%">
										<a target="_top" href="pc-readonly-review_individual_all_resolved.jsp">89.06</a>
									</td>
									<td class="valueC" nowrap width="11%" >
										[ <a target="_top" href="pc-readonly-review_individual_all_resolved.jsp" title="Unresolved">0</a> /
										<a target="_top" title="Total" href="pc-readonly-review_individual_all_resolved.jsp">7</a> ]
									</td>
								</tr>
								<tr class="light">
									<td class="value" width="10%" nowrap>
										<img border="0" src="../i/icon_silver.gif" class="Outline" alt="2nd Place">
										<a href="#" title="Download Submission">15655112 </a>(<a href="#" class="coderTextYellow">mayi</a>)
									</td>
									<td class="valueC" nowrap width="4%">
										<img border="0" src="../i/icon_authorization.gif" class="Outline" alt="Complete" width="10" height="8">
									</td>
									<td class="valueC" nowrap width="7%">00.00.00<br>00:00 AM</td>
									<td class="valueC" nowrap width="13%">
										<a target="_top" href="pc-readonly-review_results.jsp">97.00</a>
									</td>
									<td class="valueC" nowrap width="9%">
										<a href="pc-readonly-review_individual_all_resolved.jsp">
										90</a><a target="_top" href="pc-readonly-review_individual_all_resolved.jsp">.00</a>
									</td>
									<td class="valueC" nowrap width="12%" >
										[ <a target="_top" href="pc-readonly-review_individual_all_resolved.jsp" title="Unresolved">0</a> /
										<a target="_top" title="Total" href="pc-readonly-review_individual_all_resolved.jsp">7</a> ]
									</td>
									<td class="valueC" nowrap width="8%">
										<a target="_top" href="pc-readonly-review_individual_all_resolved.jsp">92.00</a>
									</td>
									<td class="valueC" nowrap width="11%">
										[ <a target="_top" href="pc-readonly-review_individual_all_resolved.jsp" title="Unresolved">0</a> /
										<a target="_top" title="Total" href="pc-readonly-review_individual_all_resolved.jsp">7</a> ]
									</td>
									<td class="valueC" nowrap width="9%">
										<a href="pc-readonly-review_individual_all_resolved.jsp">
										92</a><a target="_top" href="pc-readonly-review_individual_all_resolved.jsp">.33</a>
									</td>
									<td class="valueC" nowrap width="11%" >
										[ <a target="_top" href="pc-readonly-review_individual_all_resolved.jsp" title="Unresolved">0</a> /
										<a target="_top" title="Total" href="pc-readonly-review_individual_all_resolved.jsp">7</a> ]
									</td>
								</tr>
<%}%>
								<tr>
									<td class="lastRowTD" colspan="10"></td>
								</tr>
							</table>
<% } else {%>
							<table class="scorecard" style="border-collapse: collapse;"cellpadding="0" cellspacing="0" width="100%">
								<tr>
									<td class="title">Review</td>
								</tr>
								<tr class="light">
									<td class="valueC" nowrap>This Phase is not yet Open.</td>
								</tr>
								<tr>
									<td class="lastRowTD"></td>
								</tr>
							</table>
<% } %>
						</div>

						<!-- AGGREGATION TAB-->
						<div id="sc4" class="tabcontent">
							<ul id="tablist">
								<li><a href="#tabs" onClick="return expandcontent('sc1', this)">Registration</a></li>
								<li><a href="#tabs" onClick="return expandcontent('sc2', this)">Submission/Screening</a></li>
								<li><a href="#tabs" onClick="return expandcontent('sc3', this)">Review/Appeals</a></li>
								<%if (phaseIndex == 7) {%> <li><a href="#tabs" onClick="return expandcontent('sc7', this)">Approval</a></li>  <% } %>
								<li id="current"><a href="#tabs" onClick="return expandcontent('sc4', this)">Aggregation/Review</a></li>
								<li><a href="#tabs" onClick="return expandcontent('sc5', this)">Final Fix/Review</a></li>
								<li><a href="#tabs" onClick="return expandcontent('sc6', this)">Approval<%if (phaseIndex == 7) {%> 2<% } %></a></li>

							</ul>
							<div style="clear:both;"></div>
<%if (phaseIndex >= 4) {%>
							<table class="scorecard" style="border-collapse: collapse;"cellpadding="0" cellspacing="0" width="100%">
								<tr>
									<td class="title" colspan="6">Aggregation</td>
								</tr>
								<tr>
									<td class="header" nowrap>Submission ID</td>
									<td class="headerC" nowrap>Status</td>
									<td class="headerC" nowrap>Aggregation Date</td>
									<td class="headerC" nowrap>Aggregation</td>
									<td class="headerC" nowrap>Review Date</td>
									<td class="headerC" nowrap>Aggregation Review</td>
								</tr>
								<tr class="light">
									<td class="value" nowrap>
										<img border="0" src="../i/icon_goldstar.gif" width="12" height="12"> <a href="#" title="Download Submission">11889718 </a>(<a href="#" class="coderTextYellow">henryouly</a>)
									</td>
									<td class="valueC" nowrap>
										<img border="0" src="../i/icon_authorization.gif" class="Outline" alt="Complete" width="10" height="8">
									</td>
									<td class="valueC" nowrap>00.00.00 00:00 AM</td>
									<td class="valueC" nowrap><a href="pc-readonly-aggregation.jsp">View Results</a></td>
									<td class="valueC" nowrap>00.00.00 00:00 AM</td>
									<td class="valueC" nowrap>
										<u><font color="#000000"><a target="_top" href="pc-readonly-aggregation_results.jsp">View Results</a></font></u>
									</td>
								</tr>
								<tr>
									<td class="lastRowTD" colspan="6"></td>
								</tr>
							</table>
<%} else {%>
							<table class="scorecard" style="border-collapse: collapse;"cellpadding="0" cellspacing="0" width="100%">
								<tr>
									<td class="title">Aggregation</td>
								</tr>
								<tr class="light">
									<td class="valueC" nowrap>This Phase is not yet Open.</td>
								</tr>
								<tr>
									<td class="lastRowTD"></td>
								</tr>
							</table>
<%}%>
						</div>



						<!-- FINAL FIX/REVIEW TAB-->
						<div id="sc5" class="tabcontent">
							<ul id="tablist">
								<li><a href="#tabs" onClick="return expandcontent('sc1', this)">Registration</a></li>
								<li><a href="#tabs" onClick="return expandcontent('sc2', this)">Submission/Screening</a></li>
								<li><a href="#tabs" onClick="return expandcontent('sc3', this)">Review/Appeals</a></li>
								<%if (phaseIndex == 7) {%> <li><a href="#tabs" onClick="return expandcontent('sc7', this)">Approval</a></li>  <% } %>
								<li><a href="#tabs" onClick="return expandcontent('sc4', this)">Aggregation/Review</a></li>
								<li id="current"><a href="#tabs" onClick="return expandcontent('sc5', this)">Final Fix/Review</a></li>
								<li><a href="#tabs" onClick="return expandcontent('sc6', this)">Approval<%if (phaseIndex == 7) {%> 2<% } %></a></li>
							</ul>
							<div style="clear:both;"></div>
<%if ((phaseIndex >= 5 ) && !role.equalsIgnoreCase("aggregator")) {%>
							<table class="scorecard" style="border-collapse: collapse;"cellpadding="0" cellspacing="0" width="100%">
								<tr>
									<td class="title" colspan="6">Final Review / Fix</td>
								</tr>
								<tr>
									<td class="header" nowrap>Submission ID</td>
									<td class="headerC" nowrap>Status</td>
									<td class="headerC" nowrap>Final Fix Date</td>
									<td class="headerC" nowrap>Final Fix</td>
									<td class="headerC">Final Review Date</td>
									<td class="headerC">Final Review Results</td>
								</tr>
								<tr class="light">
									<td class="value" nowrap>
										<img border="0" src="../i/icon_goldstar.gif" width="12" height="12">
										<a href="#" title="Download Submission">11889718 </a>(<a href="#" class="coderTextYellow">henryouly</a>)&nbsp;
									</td>
									<td class="valueC" nowrap>
										<img border="0" src="../i/icon_authorization.gif" class="Outline" alt="Complete" width="10" height="8">
									</td>
									<td class="valueC" nowrap>00.00.00 00:00 AM</td>
									<td class="valueC" nowrap>
										<font color="#000000"><a target="_top" href="pc_aggregation.jsp?role=manager&projectTabIndex=0">Download</a></font>
									</td>
									<td class="valueC" nowrap>00.00.00 00:00 AM</td>
									<td class="valueC" nowrap>
										<font color="#000000"><a target="_top" href="pc_review_results_final.jsp?role=manager&projectTabIndex=0">View Results</a></font>
									</td>
								</tr>
								<tr>
									<td class="lastRowTD" colspan="6"></td>
								</tr>
							</table>
<%} else {%>
							<table class="scorecard" style="border-collapse: collapse;"cellpadding="0" cellspacing="0" width="100%">
								<tr>
									<td class="title">Final Review / Fix</td>
								</tr>
								<tr class="light">
									<td class="valueC" nowrap>This Phase is not yet Open.</td>
								</tr>
								<tr>
									<td class="lastRowTD"></td>
								</tr>
							</table>
<%}%>
						</div>

<%if (phaseIndex != 7) {%>
						<!-- APPROVAL TAB-->
						<div id="sc6" class="tabcontent">
							<ul id="tablist">
								<li><a href="#tabs" onClick="return expandcontent('sc1', this)">Registration</a></li>
								<li><a href="#tabs" onClick="return expandcontent('sc2', this)">Submission/Screening</a></li>
								<li><a href="#tabs" onClick="return expandcontent('sc3', this)">Review/Appeals</a></li>
								<li><a href="#tabs" onClick="return expandcontent('sc4', this)">Aggregation/Review</a></li>
								<li><a href="#tabs" onClick="return expandcontent('sc5', this)">Final Fix/Review</a></li>
								<li id="current"><a href="#tabs" onClick="return expandcontent('sc6', this)">Approval</a></li>
							</ul>
							<div style="clear:both;"></div>
<%if (phaseIndex >= 6) {%>
	<% if (!"observer".equalsIgnoreCase(role) || "manager".equalsIgnoreCase(role)) {%>
							<table class="scorecard" style="border-collapse: collapse;"cellpadding="0" cellspacing="0" width="100%">
								<tr>
									<td class="title" colspan="4">Approval</td>
								</tr>
								<tr>
									<td class="header" nowrap>Handle</td>
									<td class="headerC" nowrap>Status</td>
									<td class="headerC" nowrap>Approval Date</td>
									<td class="headerC" nowrap>Approval</td>
								</tr>
								<tr class="highlighted">
									<td class="value" nowrap>
										<img border="0" src="../i/icon_goldstar.gif" width="12" height="12">
										<a href="#" title="Download Submission">11889718 </a>(<a href="#" class="coderTextYellow">henryouly</a>)</a>&nbsp;
									</td>
									<td class="valueC" nowrap>
										<img border="0" src="../i/icon_notification.gif" class="Outline" alt="Deadline near" width="10" height="10">
									</td>
									<td class="valueC" nowrap>00.00.00 00:00 AM</td>
									<td class="valueC" nowrap><b><a href="pc_edit_approval_scorecard.jsp?role=approver&projectTabIndex=0">Submit Approval</a></b></td>
								</tr>
								<tr>
									<td class="lastRowTD" colspan="4"></td>
								</tr>
							</table>
	<%} else {%>
							<table class="scorecard" style="border-collapse: collapse;"cellpadding="0" cellspacing="0" width="100%">
								<tr>
									<td class="title" colspan="6">Final Review / Fix</td>
								</tr>
								<tr>
									<td class="header" nowrap>Handle</td>
									<td class="headerC" nowrap>Status</td>
									<td class="headerC" nowrap>Final Fix Date</td>
									<td class="headerC" nowrap>Final Fix</td>
									<td class="headerC">Final Review Date</td>
									<td class="headerC">Final Review Results</td>
								</tr>
								<tr class="light">
									<td class="value" nowrap>
										<img border="0" src="../i/icon_goldstar.gif" width="12" height="12">
										<a href="#" title="Download Submission">11889718 </a>(<a href="#" class="coderTextYellow">henryouly</a>)&nbsp;
									</td>
									<td class="valueC" nowrap>
										<img border="0" src="../i/icon_authorization.gif" class="Outline" alt="Complete" width="10" height="8">
									</td>
									<td class="valueC" nowrap>00.00.00 00:00 AM</td>
									<td class="valueC" nowrap>
										<font color="#000000">
										<a target="_top" href="pc_aggregation_results.jsp?role=readonly&projectTabIndex=0">Download</a></font>
									</td>
									<td class="valueC" nowrap>00.00.00 00:00 AM</td>
									<td class="valueC" nowrap>
										<font color="#000000">
										<a target="_top" href="pc_review_results_final.jsp?role=readonly&projectTabIndex=0">View Results</a></font>
									</td>
								</tr>
								<tr>
									<td class="lastRowTD" colspan="6"></td>
								</tr>
							</table>
	<%}%>
<%} else {%>
							<table class="scorecard" style="border-collapse: collapse;"cellpadding="0" cellspacing="0" width="100%">
								<tr>
									<td class="title">Approval</td>
								</tr>
								<tr class="light">
									<td class="valueC" nowrap>This Phase is not yet Open.</td>
								</tr>
								<tr>
									<td class="lastRowTD"></td>
								</tr>
							</table>
<%} }%>
						</div>
<%if (phaseIndex == 7) {%>
<%if (((Integer)request.getAttribute("phaseIndex")).intValue() == 9) { %>
						<!-- APPROVAL 2 TAB-->
						<div id="sc6" class="tabcontent">
							<ul id="tablist">
								<li><a href="#tabs" onClick="return expandcontent('sc1', this)">Registration</a></li>
								<li><a href="#tabs" onClick="return expandcontent('sc2', this)">Submission/Screening</a></li>
								<li><a href="#tabs" onClick="return expandcontent('sc3', this)">Review/Appeals</a></li>
								<li><a href="#tabs" onClick="return expandcontent('sc7', this)">Approval</a></li>
								<li><a href="#tabs" onClick="return expandcontent('sc4', this)">Aggregation/Review</a></li>
								<li><a href="#tabs" onClick="return expandcontent('sc5', this)">Final Fix/Review</a></li>
								<li id="current"><a href="#tabs" onClick="return expandcontent('sc6', this)">Approval 2</a></li>
							</ul>
							<div style="clear:both;"></div>
							<table class="scorecard" style="border-collapse: collapse;"cellpadding="0" cellspacing="0" width="100%">
								<tr>
									<td class="title" colspan="4">Approval 2</td>
								</tr>
								<tr>
									<td class="header" nowrap>Submission ID</td>
									<td class="headerC" nowrap>Status</td>
									<td class="headerC" nowrap>Approval 2 Date</td>
									<td class="headerC" nowrap>Approval 2</td>
								</tr>
								<tr class="light">
									<td class="value" nowrap>
										<img border="0" src="../i/icon_goldstar.gif" width="12" height="12">
										<a href="#" title="Download Submission">11889718 </a>(<a href="#" class="coderTextYellow">henryouly</a>)</a>&nbsp;
									</td>
									<td class="valueC" nowrap>
										<img border="0" src="../i/icon_authorization.gif" class="Outline" alt="Complete" width="10" height="8">
									</td>
									<td class="valueC" nowrap>00.00.00 00:00 AM</td>
									<td class="valueC" nowrap>
										<u><font color="#000000"><a target="_top" href="pc_approval_scorecard.jsp?role=observer&projectTabIndex=0">View Results</a></font></u>
									</td>
								</tr>
								<tr>
									<td class="lastRowTD" colspan="4"></td>
								</tr>
							</table>
						</div>
<%} else {%>
						<!-- APPROVAL 2 TAB-->
						<div id="sc6" class="tabcontent">
							<ul id="tablist">
								<li><a href="#tabs" onClick="return expandcontent('sc1', this)">Registration</a></li>
								<li><a href="#tabs" onClick="return expandcontent('sc2', this)">Submission/Screening</a></li>
								<li><a href="#tabs" onClick="return expandcontent('sc3', this)">Review/Appeals</a></li>
								<li><a href="#tabs" onClick="return expandcontent('sc7', this)">Approval</a></li>
								<li><a href="#tabs" onClick="return expandcontent('sc4', this)">Aggregation/Review</a></li>
								<li><a href="#tabs" onClick="return expandcontent('sc5', this)">Final Fix/Review</a></li>
								<li id="current"><a href="#tabs" onClick="return expandcontent('sc6', this)">Approval 2</a></li>
							</ul>
							<div style="clear:both;"></div>
							<table class="scorecard" style="border-collapse: collapse;"cellpadding="0" cellspacing="0" width="100%">
								<tr>
									<td class="title" colspan="4">Approval 2</td>
								</tr>
								<tr>
									<td class="header" nowrap>Submission ID</td>
									<td class="headerC" nowrap>Status</td>
									<td class="headerC" nowrap>Approval 2 Date</td>
									<td class="headerC" nowrap>Approval 2</td>
								</tr>
								<tr class="light">
									<td class="value" nowrap>
										<img border="0" src="../i/icon_goldstar.gif" width="12" height="12">
										<a href="#" title="Download Submission">11889718 </a>(<a href="#" class="coderTextYellow">henryouly</a>)</a>&nbsp;
									</td>
									<td class="valueC" nowrap>
										<img border="0" src="../i/icon_authorization.gif" class="Outline" alt="Complete" width="10" height="8">
									</td>
									<td class="valueC" nowrap>00.00.00 00:00 AM</td>
									<td class="valueC" nowrap>
										<u><font color="#000000">
											<a target="_top" href="pc_approval_scorecard.jsp?role=observer&projectTabIndex=0">View Results</a></font></u>
									</td>
								</tr>
								<tr>
									<td class="lastRowTD" colspan="4"></td>
								</tr>
							</table>
						</div>
<%    }%>

						<!-- APPROVAL TAB -->
						<div id="sc7" class="tabcontent">
							<ul id="tablist">
								<li><a href="#tabs" onClick="return expandcontent('sc1', this)">Registration</a></li>
								<li><a href="#tabs" onClick="return expandcontent('sc2', this)">Submission/Screening</a></li>
								<li><a href="#tabs" onClick="return expandcontent('sc3', this)">Review/Appeals</a></li>
								<li id="current"><a href="#tabs" onClick="return expandcontent('sc7', this)">Approval</a></li>
								<li><a href="#tabs" onClick="return expandcontent('sc4', this)">Aggregation/Review</a></li>
								<li><a href="#tabs" onClick="return expandcontent('sc5', this)">Final Fix/Review</a></li>
								<li><a href="#tabs" onClick="return expandcontent('sc6', this)">Approval 2</a></li>
							</ul>
							<div style="clear:both;"></div>
							<table class="scorecard" style="border-collapse: collapse;"cellpadding="0" cellspacing="0" width="100%">
								<tr>
									<td class="title" colspan="4">Approval</td>
								</tr>
								<tr>
									<td class="header" nowrap>Submission ID</td>
									<td class="headerC" nowrap>Status</td>
									<td class="headerC" nowrap>Approval Date</td>
									<td class="headerC" nowrap>Approval</td>
								</tr>
								<tr class="light">
									<td class="value" nowrap>
										<img border="0" src="../i/icon_goldstar.gif" width="12" height="12">
										<a href="#" title="Download Submission">11889718 </a>(<a href="#" class="coderTextYellow">henryouly</a>)</a>&nbsp;
									</td>
									<td class="valueC" nowrap>
										<img border="0" src="../i/icon_authorization.gif" class="Outline" alt="Complete" width="10" height="8">
									</td>
									<td class="valueC" nowrap>00.00.00 00:00 AM</td>
									<td class="valueC" nowrap>
										<u><font color="#000000"><a target="_top" href="pc_approval_scorecard.jsp?role=observer&projectTabIndex=0">View Results</a></font></u>
									</td>
								</tr>
								<tr>
									<td class="lastRowTD" colspan="4"></td>
								</tr>
							</table>
						</div>
<%}%>