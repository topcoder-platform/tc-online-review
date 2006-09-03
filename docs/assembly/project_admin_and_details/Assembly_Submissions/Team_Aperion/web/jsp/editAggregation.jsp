<%@ page language="java" isELIgnored="false" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="html" uri="/tags/struts-html" %>
<%@ taglib prefix="bean" uri="/tags/struts-bean" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>

<head>
	<!-- TopCoder CSS -->
	<link type="text/css" rel="stylesheet" href="http://www.topcoder.com/css/style.css">
	<link type="text/css" rel="stylesheet" href="http://www.topcoder.com/css/coders.css">
	<link type="text/css" rel="stylesheet" href="http://www.topcoder.com/css/stats.css">
	<link type="text/css" rel="stylesheet" href="http://www.topcoder.com/css/tcStyles.css">
	
	<!-- CSS and JS by Petar -->
	<link type="text/css" rel="stylesheet" href="../css/new_styles.css">
	<script language="JavaScript" type="text/javascript" src="../scripts/rollovers.js"></script>
</head>

<body>
	<jsp:include page="../includes/inc_header.jsp" />
	<table width="100%" border="0" cellpadding="0" cellspacing="0">
		<tr valign="top">
			<!-- Left Column Begins-->
			<td width="180"><jsp:include page="../includes/inc_leftnav.jsp" /></td>
			<!-- Left Column Ends -->
			
			<!-- Gutter Begins -->
			<td width="15"><img src="../i/clear.gif" width="15" height="1" border="0"/></td>
			<!-- Gutter Ends -->
			
			<!-- Center Column Begins -->
			<td class="bodyText">
				<jsp:include page="../includes/project/project_tabs.jsp" />
				<div id="mainMiddleContent">
					<div style="padding: 11px 6px 9px 0px;">
						<table border="0" cellpadding="0" cellspacing="0" width="100%" id="table1">
							<tr>
								<td>
									<table cellspacing="0" cellpadding="0" border="0">
										<tr valign="middle">
											<td><img src="../i/${categoryIconName}" border="0" /></td>
											<td><img src="../i/${rootCatalogIcon}" alt="${rootCatalogName}" border="0" /></td>
											<td>
												<span class="bodyTitle">${project.allProperties["Project Name"]}</span>
												<c:if test='${!(empty project.allProperties["Project Version"])}'>
													<font size="4"><bean:message key="global.version" />
														${project.allProperties["Project Version"]}</font>
												</c:if>
											</td>
										</tr>
									</table>
								</td>
								<td align="right" valign="top">
									<a href="javascript:showAll();"><bean:message key="global.expandAll" /></a>&#160;|
									<a href="javascript:hideAll();"><bean:message key="global.collapseAll" /></a>
								</td>
							</tr>
						</table>
					</div>
					
					&nbsp;<b><bean:message key="editReview.Aggregator" /></b> <a href="#" class="coderTextYellow">AggregatorHandle</a><br>
					&nbsp;<b><bean:message key="editReview.Submission" /></b> ${sid}<br />
					&nbsp;<b><bean:message key="editReview.MyRole" /></b> ${myRole}<br />
					<h3><bean:message key="editReview.EditAggregation.title" /></h3>
					
					<html:form action="/actions/SaveAggregation">
						<c:set var="itemIdx" value="0" />
						<html:hidden property="method" value="saveAggregation" />

						<c:forEach items="${scorecardTemplate.allGroups}" var="group" varStatus="groupStatus">
							<table cellpadding="0" border="0" width="100%" class="scorecard" style="border-collapse: collapse; border-bottom-style: solid" >
								<tr>
									<td class="title" colspan="7">${group.name}</td>
								</tr>
								<c:forEach items="${group.allSections}" var="section" varStatus="sectionStatus">
									<tr>
										<td  class="subheader" width="100%"   colspan="7">${section.name}</td>
									</tr>
									<c:forEach items="${section.allQuestions}" var="question" varStatus="questionStatus">
										<tr class="light">
											<td class="value" colspan="7">
											
											<div class="showText" id="shortQ_${itemIdx}">
												<a href="javascript:toggleDisplay('shortQ_${itemIdx}');toggleDisplay('longQ_${itemIdx}');" class="statLink"><html:img src="../i/plus.gif" altKey="global.plus.alt" border="0" /></a>
												<b><bean:message key="editReview.Question.title" /> ${groupStatus.index + 1}.${sectionStatus.index + 1}.${questionStatus.index + 1}</b>
												${question.description}
											</div>
											<div class="hideText" id="longQ_${itemIdx}">
												<a href="javascript:toggleDisplay('shortQ_${itemIdx}');toggleDisplay('longQ_${itemIdx}');" class="statLink"><html:img src="../i/minus.gif" altKey="global.minus.alt" border="0" /></a>
												<b><bean:message key="editReview.Question.title" /> ${groupStatus.index + 1}.${sectionStatus.index + 1}.${questionStatus.index + 1}</b>
												${question.description}<br />
												${question.guideline}
											</div>
										
											</td>
										</tr>
										<tr>
											<td class="header"><bean:message key="editReview.EditAggragation.Reviewer" /></td>
											<td class="headerC"><bean:message key="editReview.EditAggragation.CommentNumber" /></td>
											<td class="header"><bean:message key="editReview.EditAggragation.Response" /></td>
											<td class="header"><bean:message key="editReview.EditAggragation.Type" /></td>
											<td class="header"><bean:message key="editReview.EditAggragation.Rejected" /></td>
											<td class="header"><bean:message key="editReview.EditAggragation.Accepted" /></td>
											<td class="header"><bean:message key="editReview.EditAggragation.Duplicate" /></td>
										</tr>
										
										<%-- TODO: Continue work on the page --%>
					    <tr class="dark">
				            <td class="value">
							   <a href="#" class="coderTextGreen">qiucx0161</a><br>
							   <a href="pc_review_individual.jsp?role=public&projectTabIndex=0&resolved=true">View Review</a>
							</td>
							<td class="valueC">1</td>
							<td class="value" width="85%">
								<b>Reviewer Response:</b> Lorem ipsum dolor sit amet, consectetuer adipiscing elit, sed diam nonummy nibh euismod tincidunt ut laoreet dolore magna aliquam erat volutpat. Ut wisi enim ad minim veniam, quis nostrud exerci.<br>
								<div style="padding-top:4px;"><b>Response text:</b> Lorem adipiscing elit, sed diam nonummy nibh euismod tincidunt ut laoreet dolore magna aliquam erat volutpat. Ut wisi enim ad minim veniam, quis nostrud exerci.
							</td>
							<td class="value">Recommended</td>
							<td class="valueC"><input type="radio" value="V1" name="R1af" checked disabled></td>
							<td class="valueC"><input type="radio" value="V1" name="R1af" disabled></td>
							<td class="valueC"><input type="radio" value="V1" name="R1af" disabled></td>
						</tr>
						<tr class="dark">
							<td class="value"></td>
							<td class="valueC"></td>
							<td class="value" width="85%">
								<b>Manager Comment:</b><br>
								<textarea rows="2" name="S4" cols="20" style="font-size: 10px; font-family: sans-serif;width:100%;height:38px;border:1px solid #ccc;margin:0px 3px 3px 3px;"></textarea>
							</td>
							<td class="value"><br>
								<select size="1" name="D14" class="inputBox" >
									<option selected>Select</option>
									<option>Recommended</option>
									<option>Required</option>
									<option>Comment</option>
								</select>
							</td>
							<td class="valueC">Rejected<br><input type="radio" value="V1" name="R1a"></td>
							<td class="valueC">Accepted<br><input type="radio" value="V1" name="R1a"></td>
							<td class="valueC">Duplicate<br><input type="radio" value="V1" name="R1a"></td>
						</tr>
						<tr class="dark">
							<td class="value">
								<a href="#" class="coderTextYellow">mgmg</a><br>
								<a href="pc_review_individual.jsp?role=public&projectTabIndex=0&resolved=true">View Review</a>
							</td>
							<td class="valueC">1</td>
							<td class="value" width="85%">
								<b>Reviewer Response:</b> Lorem ipsum dolor sit amet, consectetuer adipiscing elit, sed diam 	nonummy nibh euismod tincidunt ut laoreet dolore magna aliquam erat volutpat. Ut wisi enim ad minim veniam, quis nostrud exerci.<br>
								<div style="padding-top:4px;"><b>Response text:</b> Lorem adipiscing elit, sed diam nonummy nibh euismod tincidunt ut laoreet dolore magna aliquam erat volutpat. Ut wisi enim ad minim veniam, quis nostrud exerci.
							</td>
							<td class="value">Recommended</td>
							<td class="valueC"><input type="radio" value="V1" name="R1bf" disabled></td>
							<td class="valueC"><input type="radio" value="V1" name="R1bf" checked disabled></td>
							<td class="valueC"><input type="radio" value="V1" name="R1bf" disabled></td>
						</tr>
						<tr class="dark">
							<td class="value"></td>
							<td class="valueC"></td>
							<td class="value" width="85%">
								<b>Manager Comment:</b><br>
								<textarea rows="2" name="S4" cols="20" style="font-size: 10px; font-family: sans-serif;width:100%;height:38px;border:1px solid #ccc;margin:0px 3px 3px 3px;"></textarea>
							</td>
							<td class="value"><br>
								<select size="1" name="D14" class="inputBox" >
									<option selected>Select</option>
									<option>Recommended</option>
									<option>Required</option>
									<option>Comment</option>
								</select>
							</td>
							<td class="valueC">Rejected<br><input type="radio" value="V1" name="R1b"></td>
							<td class="valueC">Accepted<br><input type="radio" value="V1" name="R1b"></td>
							<td class="valueC">Duplicate<br><input type="radio" value="V1" name="R1b"></td>
						</tr>
						<tr class="dark">
							<td class="value" nowrap>
								<a href="#" class="coderTextGreen">slion</a><br>
								<a href="pc_review_individual.jsp?role=public&projectTabIndex=0&resolved=true">View Review</a>
							</td>
							<td class="valueC">1</td>
							<td class="value" width="85%">
								<b>Reviewer Response:</b> Lorem ipsum dolor sit amet, consectetuer adipiscing elit, sed diam nonummy nibh euismod tincidunt ut laoreet dolore magna aliquam erat volutpat. Ut wisi enim ad minim veniam, quis nostrud exerci.<br>
								<div style="padding-top:4px;"><b>Response text:</b> Lorem adipiscing elit, sed diam nonummy nibh euismod tincidunt ut laoreet dolore magna aliquam erat volutpat. Ut wisi enim ad minim veniam, quis nostrud exerci.
							</td>
							<td class="value">Required</td>
							<td class="valueC"><input type="radio" value="V1" name="R1cf" checked disabled></td>
							<td class="valueC"><input type="radio" value="V1" name="R1cf" disabled></td>
							<td class="valueC"><input type="radio" value="V1" name="R1cf" disabled></td>
						</tr>
						<tr class="dark">
							<td class="value"></td>
							<td class="valueC"></td>
							<td class="value" width="85%">
								<b>Manager Comment:</b><br>
								<textarea rows="2" name="S4" cols="20" style="font-size: 10px; font-family: sans-serif;width:100%;height:38px;border:1px solid #ccc;margin:0px 3px 3px 3px;"></textarea>
							</td>
							<td class="value">
								<br>
								<select size="1" name="D14" class="inputBox" >
									<option selected>Select</option>
									<option>Recommended</option>
									<option>Required</option>
									<option>Comment</option>
								</select></td>
							<td class="valueC">Rejected<br><input type="radio" value="V1" name="R1c"></td>
							<td class="valueC">Accepted<br><input type="radio" value="V1" name="R1c"></td>
							<td class="valueC">Duplicate<br><input type="radio" value="V1" name="R1c"></td>
						</tr>
						
						<tr>
							<td class="lastRowTD" colspan="7"></td>
						</tr>
								</c:forEach>
							</c:forEach>
						</table>
					</c:forEach>
					
					<a href="#" onClick="parent.location='javascript:history.go(-1)'">
					<img src="../images/bttn_save_and_mark_complete.gif" height="18" border="0" align="right" vspace="11"></a><a href="#" onClick="parent.location='javascript:history.go(-1)'"><img src="../images/bttn_save_and_finish_later.gif" height="18" border="0" align="right" vspace="11" hspace="9"></a><a href="#" onClick="parent.location='javascript:history.go(-1)'"><img src="../images/bttn_preview.gif" height="18" border="0" align="right" vspace="11"></a><br><br>
					
					</html:form>
				</div>
				<p><br/><br/><br/></p>
			</td>
			<!-- Center Column Ends -->
			
			<!-- Gutter -->
			<td width="15"><img src="../i/clear.gif" width="25" height="1" border="0"></td>
			<!-- Gutter Ends -->

			<!-- Gutter -->
			<td width="2"><img src="../i/clear.gif" width="2" height="1" border="0"></td>
			<!-- Gutter Ends -->
		</tr>
	</table>
	
	<jsp:include page="../includes/inc_footer.jsp" />
</body>
</html>