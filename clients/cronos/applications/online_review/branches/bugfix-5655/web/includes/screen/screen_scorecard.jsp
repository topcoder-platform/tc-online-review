<%@ taglib prefix="html" uri="/tags/struts-html" %>
					<h3>Screening Scorecard</h3>
					<table class="scorecard" cellpadding="0" width="100%" style="border-collapse: collapse;">
						<tr>
							<td class="title" colspan="3">Group</td>
						</tr>
						<tr>
							<td class="subheader">Section</td>
							<td class="subheader">Weight</td>
							<td class="subheader" nowrap align="center"><b>Response</b></td>
						</tr>
						<tr class="light">
							<td class="value">
								<html:img src="/i/or/plus.gif" alt="open" border="0" /></a>
								<b>Question x.x.x </b>Log File from successful execution of unit test cases. (Review the /log directory)
							</td>
							<td class="valueC">25</td>
							<td class="valueC">Yes</td>
						</tr>
						<tr class="light">
							<td class="value">
								<html:img src="/i/or/plus.gif" alt="open" border="0" /></a>
								<b>Question x.x.x </b>Source Code exists and compiles successfully. (Execute 'ant compile' / 'nant compile')
							</td>
							<td class="valueC">25</td>
							<td class="valueC">Yes</td>
						</tr>
						<tr class="light">
							<td class="value">
								<html:img src="/i/or/plus.gif" alt="open" border="0" /></a>
								<b>Question x.x.x </b>Unit Test cases exist for most or all of the public methods and compile successfully. (Execute 'ant_compile_tests' / 'nant compile_tests')
							</td>
							<td class="valueC">25</td>
							<td class="valueC">Yes</td>
						</tr>
						<tr class="light">
							<td class="value">
								<html:img src="/i/or/plus.gif" alt="open" border="0" /></a>
								<b>Question x.x.x </b>Component and unit test source code contain reasonably complete documentation (Java: Javadoc style / .NET: XML). Documentation may be missing from some places, but this should not be a widespread issue.
							</td>
							<td class="valueC">25</td>
							<td class="valueC">Yes</td>
						</tr>
						<tr>
							<td class="lastRowTD" colspan="3"></td>
						</tr>
					</table><br>
					<table class="scorecard" cellpadding="0" width="100%" style="border-collapse: collapse;">
						<tr>
							<td class="title" colspan="3">Group</td>
						</tr>
						<tr>
							<td class="subheader" width="100%">Section</td>
							<td class="subheader" width="45%">Weight</td>
							<td class="subheader" nowrap width="10%"><b>Response</b></td>
						</tr>
						<tr class="light">
							<td class="value" style="padding-top:6px; padding-bottom:6px; " width="100%">
								<html:img src="/i/or/plus.gif" alt="open" border="0" /></a>
								<b>Question x.x.x </b>All public methods are clearly commented.
							</td>
							<td class="valueC" style="padding-top:6px; padding-bottom:6px; ">34</td>
							<td class="value" nowrap style="padding-top:6px; padding-bottom:6px; ">4 - Strongly Agree</td>
						</tr>
						<tr class="dark">
							<td class="value" colspan="2">
								<html:img src="/i/or/plus.gif" alt="open" border="0" />
								<b> Response 1: Comment</b>&nbsp;Lorem ipsum dolor sit amet, consectetuer adipiscing elit, sed diam&nbsp; ut...
							</td>
							<td class="valueR" nowrap>&nbsp;</td>
						</tr>
						<tr class="light">
							<td class="value" style="padding-top:6px; padding-bottom:6px; ">
								<html:img src="/i/or/plus.gif" alt="open" border="0" /></a><b>Question x.x.x </b>Required tags are included.
							</td>
							<td class="valueC" style="padding-top:6px; padding-bottom:6px; ">33</td>
							<td class="value" nowrap style="padding-top:6px; padding-bottom:6px; ">4 - Strongly Agree</td>
						</tr>
						<tr class="dark">
							<td class="value" colspan="2">
								<html:img src="/i/or/plus.gif" alt="open" border="0" /><b> Response 1: Comment</b>&nbsp;
								Lorem ipsum dolor sit amet, consectetuer adipiscing elit, sed diam&nbsp; ut...
							</td>
							<td class="valueR" nowrap>&nbsp;</td>
						</tr>
						<tr class="light">
							<td class="value" style="padding-top:6px; padding-bottom:6px; ">
								<html:img src="/i/or/plus.gif" alt="open" border="0" /></a>
								<b>Question x.x.x </b>Copyright tag is populated: Copyright &copy; 2003, TopCoder, Inc. All rights reserved 
							</td>
							<td class="valueC" style="padding-top:6px; padding-bottom:6px; ">33</td>
							<td class="value" nowrap style="padding-top:6px; padding-bottom:6px; ">4 - Strongly Agree</td>
						</tr>
						<tr class="dark">
							<td class="value" colspan="2">
								<html:img src="/i/or/plus.gif" alt="open" border="0" /><b> Response 1: Comment</b>&nbsp;
								Lorem ipsum dolor sit amet, consectetuer adipiscing elit, sed diam&nbsp; ut...
							</td>
							<td class="valueR" nowrap>&nbsp;</td>
						</tr>
						<tr>
							<td class="subheader" width="45%">Section</td>
							<td class="subheader" width="45%">&nbsp;</td>
							<td class="subheader" nowrap width="10%"><b>Response</b></td>
						</tr>
						<tr class="light">
							<td class="value" style="padding-top:6px; padding-bottom:6px; ">
								<html:img src="/i/or/plus.gif" alt="open" border="0" /></a>
								<b>Question x.x.x </b>Adheres to coding standards.
							</td>
							<td class="valueC" style="padding-top:6px; padding-bottom:6px; ">50</td>
							<td class="value" nowrap style="padding-top:6px; padding-bottom:6px; ">4 - Strongly Agree</td>
						</tr>
						<tr class="dark">
							<td class="value" colspan="2">
								<html:img src="/i/or/plus.gif" alt="open" border="0" /><b> Response 1: Comment</b>&nbsp;
								Lorem ipsum dolor sit amet, consectetuer adipiscing elit, sed diam&nbsp; ut...
							</td>
							<td class="valueR" nowrap>&nbsp;</td>
						</tr>
						<tr class="light">
							<td class="value" style="padding-top:6px; padding-bottom:6px; ">
								<html:img src="/i/or/plus.gif" alt="open" border="0" /></a>
								<b>Question x.x.x </b>Code uses a 4 space indentation (not a tab)
							</td>
							<td class="valueC" style="padding-top:6px; padding-bottom:6px; ">50</td>
							<td class="value" nowrap style="padding-top:6px; padding-bottom:6px; ">4 - Strongly Agree</td>
						</tr>
						<tr class="dark">
							<td class="value" colspan="2">
								<html:img src="/i/or/plus.gif" alt="open" border="0" /><b> Response 1: Comment</b>&nbsp;
								Lorem ipsum dolor sit amet, consectetuer adipiscing elit, sed diam&nbsp; ut...
							</td>
							<td class="valueR" nowrap>&nbsp;</td>
						</tr>
						<tr>
							<td class="lastRowTD" colspan="3"></td>
						</tr>
					</table>