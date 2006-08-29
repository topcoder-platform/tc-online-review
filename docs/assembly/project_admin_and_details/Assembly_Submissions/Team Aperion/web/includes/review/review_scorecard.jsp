<%
    boolean resolved = false;
	String role = (String) request.getParameter("role");
	if (request.getParameter("resolved") != null) {
		resolved = ((String) request.getParameter("resolved")).equalsIgnoreCase("true");
	}
%>
					<h3>Review Scorecard </h3>
					<!--    For the Design Checklist, mark each deliverable as Yes, it exists; No, it does not exist; N/A, this deliverable was not required. For the Design Review, provide a score for each statement according to the dropdown answers.<br>
					<br>
					<b>The answers to the questions should be limited to how the design meets the requirements (both business and technical) as described in the requirements document AND the forums for the component.</b><br> -->
					<table class="scorecard" cellpadding="0" cellspacing="0" width="100%">
						<tr>
							<td class="title" colspan="5">Group</td>
						</tr>
						<tr>
							<td class="subheader">Section</td>
							<td class="subheader"><b>Weight</b></td>
							<td class="subheader"><b>Response</b></td>
							<td class="subheader" nowrap><b>Modified Response</b></td>
							<td class="subheader" nowrap><b>Appeal Status</b></td>
						</tr>
						<tr class="light">
							<td class="value">
								<div class="showText" id="shortQ_0" style="width: 100%;">
									<a href="javascript:toggleDisplay('shortQ_0');toggleDisplay('longQ_0');" class="statLink">
									<img src="../images/plus.gif" alt="open" border="0" /></a> <b>Question x.x.x</b>
									The design addresses all the requirements as specified in the requirements documents and the forum. The reviewer should be able to take each point in the requirements document and find the associated implementation in the design.
								</div>
								<div class="hideText" id="longQ_0">
									<a href="javascript:toggleDisplay('shortQ_0');toggleDisplay('longQ_0');" class="statLink">
									<img src="../images/minus.gif" alt="close" border="0" /></a>
									<b>Question x.x.x</b>
									The design addresses all the requirements as specified in the
									requirements documents and the forum. The reviewer should be
									able to take each point in the requirements document and find
									the associated implementation in the design. The reviewer should ignore the quality aspects of the implementation at this point, and only verify whether a requirement is addressed or not. Other questions of the scorecard give you an opportunity to express your concerns about the quality of the design.
									<br><br>
									Rating 1 &#150; Missing requirement(s) prevent the design from fulfilling the goal of the requirements.
									<br><br>
									Rating 2 &#150; The design addresses all requirements to solve the problem for which the component was requested, but is missing major features from the requirements document.
									<br><br>
									Rating 3 &#150; The design addresses all of the requirements and potentially includes minor additional functionality. The design solves the problem for which the component was requested but either does not provide enhancements on top of the requirements, or the added functionality does not alter the customer’s experience with the component.
									<br><br>
									Rating 4 &#150; The design addresses all requirements and provides new and substantially useful and pertinent functionality. 
								</div>
							</td>
							<td class="valueC" nowrap>25</td>
							<td class="valueC" nowrap>4 - Strongly Agree</td>
							<td class="valueC" nowrap>&nbsp;</td>
							<td class="valueC" nowrap>&nbsp;</td>
						</tr>
						<tr class="dark">
							<td class="value">
								<img src="../images/plus.gif" alt="open" border="0" /><b> Response 1: Comment</b>&nbsp;
								Lorem ipsum dolor sit amet, consectetuer ...<br>
								<b>Document </b>(required)<b>: </b><a href="#">test.doc</a>
							</td>
							<td class="valueC" nowrap>&nbsp;</td>
							<td class="valueC" nowrap>&nbsp;</td>
							<td class="valueC" nowrap>&nbsp;</td>
							<td class="valueC" nowrap>&nbsp;</td>
						</tr>
						<tr>
							<td class="value">
								<div class="showText" id="shortQ_1" style="width: 100%;">
									<a href="javascript:toggleDisplay('shortQ_1');toggleDisplay('longQ_1');" class="statLink"><img src="../images/plus.gif" alt="open" border="0" /></a> <b>Question x.x.x</b>
									The component specification includes a clear and easily grasped explanation of algorithms used in the design.
								</div>
								<div class="hideText" id="longQ_1">
									<a href="javascript:toggleDisplay('shortQ_1');toggleDisplay('longQ_1');" class="statLink"><img src="../images/minus.gif" alt="close" border="0" /></a>
									<b>Question x.x.x</b>
									The component specification includes a clear and easily grasped explanation of algorithms used in the design.
									<br><br>
									The reviewer should judge whether the algorithm descriptions are complete, accurate and contain enough information for the developer to properly implement the algorithms. Please note that this review question should judge only the correctness of the algorithm and how well the designer relates the algorithm &#150; the grammar and spelling should NOT be included in this question. The algorithms stated can be actual code, pseudo-code or even simple textual descriptions, as long as the designer effectively relates the point of the algorithm.
									<br><br>
									Rating 1 &#150; The component specification is missing a description for any of the algorithms defined in the requirements, or external research is required to understand how to implement any of the algorithms.
									<br><br>
									Rating 2 &#150; The component specification includes all the algorithms defined in the requirements. However, the description contains logical errors or easily leads to misinterpretations or is generally written in a style that is hard to understand &#150; generally requiring many readings to fully grasp the algorithm. The developer may need external research to fully understand the algorithm.
									<br><br>
									Rating 3 &#150; The component specification includes all the algorithms defined in the requirements and optionally any ‘complicated’ sections of the design. The description is fairly clear with, at most, minor errors and includes everything needed by the developer to implement without any external research.
									<br><br>
									Rating 4 &#150; The component specification includes all the algorithms defined in the requirements and any ‘complicated’ sections of the design. The description is clear, contains no logical errors, and can be understood from a single reading.
								</div>
							</td>
							<td class="valueC" nowrap>25</td>
							<td class="valueC" nowrap>2 - Somewhat Agree</td>
							<td class="valueC" nowrap>&nbsp;</td>
							<td class="valueC" nowrap>&nbsp;</td>
						</tr>
						<tr class="dark">
							<td class="value">
								<img src="../images/plus.gif" alt="open" border="0" /><b> Response 1: Comment</b>&nbsp;
								Lorem ipsum dolor sit amet, consectetuer ...
							</td>
							<td class="valueC" nowrap>&nbsp;</td>
							<td class="valueC" nowrap>&nbsp;</td>
							<td class="valueC" nowrap>&nbsp;</td>
							<td class="valueC" nowrap>&nbsp;</td>
						</tr>
						<tr class="light">
							<td class="value">
								<div class="showText" id="shortQ_2" style="width: 100%;">
									<a href="javascript:toggleDisplay('shortQ_2');toggleDisplay('longQ_2');" class="statLink"><img src="../images/plus.gif" alt="open" border="0" /></a> <b>Question x.x.x</b>
									The use cases cover all the business requirements as stated in the requirements document.
									The reviewer should be able to take each requirement and find a use case covering it.
								</div>
								<div class="hideText" id="longQ_2">
									<a href="javascript:toggleDisplay('shortQ_2');toggleDisplay('longQ_2');" class="statLink"><img src="../images/minus.gif" alt="close" border="0" /></a>
									<b>Question x.x.x</b>
									The use cases cover all the business requirements as stated in the
									requirements document. The reviewer should be able to take each
									requirement and find a use case covering it. Use cases must not be too high-level or too low-level. Additional functionality beyond the requirements must be covered by use cases as well.
									
									These rules should be considered when judging the use cases:
									a) A use case is something an actor wants the system to do
									b) Use cases are always started by an actor
									c) Use cases are always defined from the point of view of an actor
									d) Use cases are always described by a verb phrase

									When a single use case covers multiple unrelated activities, it is considered too high-level. A good indicator of this is a single use case covering multiple points in the requirements document.

									Too low-level is defined as multiple use cases covering related activities. For example, providing a use case for each constructor or each version of an overloaded method are considered too low-level.
									<br><br>
									
									Use cases must not reveal the intended implementation. For example, a use case of “Read Configuration” should not specify how it reads the configuration (i.e. it should not say “Read Configuration via Configuration Manager,” unless the component provides other user-selectable alternatives for obtaining the configuration without the Configuration Manager).
									<br><br>
									This question does not address the presentation of the diagrams; only the logical content needs to be considered.
									
									<br><br>
									Rating 1 &#150; Only a minority (&lt;= 49%) of the requirements are covered by the use cases.
									<br><br>
									Rating 2 &#150; A majority (&gt; 49%) of the requirements are covered but at least one requirement is missing and/or the use cases are too high (or low) level in detail as explained above.
									<br><br>
									Rating 3 &#150; All the requirements are covered at a proper level of detail as explained above.
									<br><br>
									Rating 4 &#150; All the requirements are covered at a proper level of detail as explained above, and the designer made proper use of inherited
								</div>
							</td>
							<td class="valueC" nowrap>25</td>
							<td class="valueC" nowrap>4 - Strongly Agree</td>
							<td class="valueC" nowrap>&nbsp;</td>
							<td class="valueC" nowrap>&nbsp;</td>
						</tr>
						<tr class="dark">
							<td class="value">
								<img src="../images/plus.gif" alt="open" border="0" /><b> Response 1: Comment</b>&nbsp;
								Lorem ipsum dolor sit amet, consectetuer ...
							</td>
							<td class="valueC" nowrap>&nbsp;</td>
							<td class="valueC" nowrap>&nbsp;</td>
							<td class="valueC" nowrap>&nbsp;</td>
							<td class="valueC" nowrap>&nbsp;</td>
						</tr>
						<tr class="light">
							<td class="value">
								<div class="showText" id="shortQ_3" style="width: 100%;">
									<a href="javascript:toggleDisplay('shortQ_3');toggleDisplay('longQ_3');" class="statLink"><img src="../images/plus.gif" alt="open" border="0" /></a> <b>Question x.x.x</b>
									The sequence diagrams cover complicated use cases or other complicated sequences where the interactions of the objects are not obvious.
								</div>
								<div class="hideText" id="longQ_3">
									<a href="javascript:toggleDisplay('shortQ_3');toggleDisplay('longQ_3');" class="statLink"><img src="../images/minus.gif" alt="close" border="0" /></a>
									<b>Question x.x.x</b>
									The sequence diagrams cover complicated use cases or other
									complicated sequences where the interactions of the objects are not
									obvious. Sequence diagrams should be included when there are interactions between objects that need to be demonstrated or where interactions are not immediately obvious or where method documentation may benefit from a supplemental diagram.
									<br><br>

									Interactions involving five or more objects (both system and/or component) are considered complicated. Designers may include SDs with less than five objects, but they are not required to do so.
									<br><br>
									This question does not address the presentation of the diagrams; only the logical content needs to be considered for this question.
									<br><br>
									
									Rating 1 &#150; Only a minority (&lt;=49%) of the complicated interactions are covered by the sequence diagrams. Identify all complicated interactions without the diagrams in your comments.
									<br><br>
									Rating 2 &#150; At least one complicated interaction is missing a sequence diagram, or a majority (>49%) of the diagrams are misleading or incorrect. You must either identify the missing interaction or add explanations of how to fix misleading or incorrect diagrams in your comments.
									<br><br>
									Rating 3 &#150; All the complicated interactions are covered by sequence diagrams, but at least one of the diagrams is incorrect or misleading (missing participants or missing calls on any of the diagrams make that diagram grossly incorrect). You must add explanations of how to fix misleading or incorrect diagrams in your comments.
									<br><br>
									Rating 4 &#150; All ‘complicated’ interactions have been described by corresponding sequence diagrams, and none of the diagrams are incorrect or misleading. This rating permits minor issues such as mislabeled methods or mismatched parameters or return values to exist in the diagram.
								</div>
							</td>
							<td class="valueC" nowrap>25</td>
							<td class="valueC" nowrap>4 - Strongly Agree</td>
							<td class="valueC" nowrap>&nbsp;</td>
							<td class="valueC" nowrap>&nbsp;</td>
						</tr>
						<tr class="dark">
							<td class="value">
								<img src="../images/plus.gif" alt="open" border="0" /><b> Response 1: Comment</b>&nbsp;
								Lorem ipsum dolor sit amet, consectetuer ...
							</td>
							<td class="valueC" nowrap>&nbsp;</td>
							<td class="valueC" nowrap>&nbsp;</td>
							<td class="valueC" nowrap>&nbsp;</td>
							<td class="valueC" nowrap>&nbsp;</td>
						</tr>
						<!-- APPROACH SECTIONS STARTS HERE -->
						<tr>
							<td class="title" colspan="5">Group</td>
						</tr>
						<tr>
							<td class="subheader">Section</td>
							<td class="subheader"><b>Weight</b></td>
							<td class="subheader"><b>Response</b></td>
							<td class="subheader">Modified Response</td>
							<td class="subheader"><b>Appeal Status</b></td>
						</tr>
						<tr class="light">
							<td class="value"><a name="Q4"></a>
								<div class="showText" id="shortQ_4" style="width: 100%;">
									<a href="javascript:toggleDisplay('shortQ_4');toggleDisplay('longQ_4');" class="statLink"><img src="../images/plus.gif" alt="open" border="0" /></a> <b>Question x.x.x</b>
									The design incorporates standard design patterns and methodologies where applicable (e.g. Model-View-Controller, Factory Pattern, Object Oriented design principles, follows J2EE specs or Microsoft Design Guidelines for Class Library Developers).
								</div>
								<div class="hideText" id="longQ_4">
									<a href="javascript:toggleDisplay('shortQ_4');toggleDisplay('longQ_4');" class="statLink"><img src="../images/minus.gif" alt="close" border="0" /></a>
									<b>Question x.x.x</b>
									The design incorporates standard design patterns and methodologies where applicable (e.g. Model-View-Controller, Factory Pattern, Object Oriented design principles, follows J2EE specs or Microsoft Design Guidelines for Class Library Developers).
									<br><br>
									The reviewer should determine a few things: Are the patterns/methodologies used 'forced' (i.e. is there good justification for using the pattern or did the designer force the pattern into the design to say they used that pattern). Are the patterns/methodologies implemented correctly (this does not mean completely &#150; the design may justify a partial implementation)? Did the designer show an understanding of the pattern/methodology? Did the designer recognize and document all the patterns used?
									<br><br>
									
									Rating 1 &#150; The design consistently uses patterns/methodologies inappropriately, causing potential problems either in implementing the component or in implementing future enhancements to the component.
									<br><br>
									Rating 2 &#150; An appropriate pattern/methodology was used but the designer showed a lack of understanding in the implementation of it. A pattern/methodology should have been used but was not (name the missing patterns and explain how the design would benefit from using them). A pattern/methodology was 'forced' (name the inappropriate patterns and explain why they are not applicable to the design).
									<br><br>
									Rating 3 &#150; Appropriate patterns and/or methodologies were used but the designer missed some in the component specification (either missed completely or incorrectly labeled).
									<br><br>
									Rating 4 &#150; Appropriate patterns and/or methodologies were used and were identified correctly in the component specification. The reviewer should also assign this rating if the design, appropriately, does not need any patterns/methodologies. 
								</div>
							</td>
							<td class="valueC" nowrap>20</td>
							<td class="valueC" nowrap>2 - Somewhat Agree</td>
<% if (resolved) {%>
							<td class="valueC" nowrap>4 - Strongly Agree</td>
							<td class="valueC" nowrap>Resolved</td>
<%} else {%>
							<td class="valueC" nowrap>&nbsp;</td>
							<td class="valueC" nowrap>&nbsp;</td>
<%}%>
						</tr>
						<tr class="dark">
							<td class="value">
								<img src="../images/plus.gif" alt="open" border="0" /><b> Response 1: Comment</b>&nbsp;Lorem ipsum dolor sit amet, consectetuer ...<br>
								<img src="../images/plus.gif" alt="open" border="0" /><b> Appeal Text: </b>Lorem ipsum dolor sit amet, consectetuer&nbsp; nibh...
							</td>
							<td class="valueC" nowrap>&nbsp;</td>
							<td class="valueC" nowrap>&nbsp;</td>
							<td class="valueC" nowrap>&nbsp;</td>
							<td class="valueC" nowrap>&nbsp;</td>
						</tr>
						<tr>
							<td class="value">
								<div class="showText" id="shortQ_5" style="width: 100%;" >
									<a href="javascript:toggleDisplay('shortQ_5');toggleDisplay('longQ_5');" class="statLink"><img 	src="../images/plus.gif" alt="open" border="0" /></a> <b>Question x.x.x</b>
									The component makes effective use of the TopCoder component catalog.
								</div>
								<div class="hideText" id="longQ_5">
									<a href="javascript:toggleDisplay('shortQ_5');toggleDisplay('longQ_5');" class="statLink"><img src="../images/minus.gif" alt="close" border="0" /></a>
									<b>Question x.x.x</b>
									The component makes effective use of the TopCoder component catalog.
									<br><br>
									
									The reviewer should review the components recommended in the requirements documentation. The designer will need to provide justification (either in the component specification or in the component forum) if they choose not to use the recommendations. The reviewer should also review the TopCoder catalog for any components that could potentially be used in the component design. The reviewer should not be critical of the designer's use, assumptions or lack of details for components that are being proposed in the design or are currently in the design/development stage - as long as the designer has included verbiage in the component specification stating that the design may need to change based on the final 'look' of the dependent component. Please note that any components being proposed or changed by this design is dependent upon permission from the PM.
									<br><br>
									
									Rating 1 &#150; The design did not use any of the recommended components and did not give any justification as to why.
									<br><br>
									Rating 2 &#150; The design used at least one of the recommended components but not all and did not give any justification as to why. Likewise, assign this rating if the designer missed using likely components, used any other component incorrectly, or provided flawed justification as to why a component should not be used.
									<br><br>
									Rating 3 &#150; The design used all the recommended components correctly but missed using other components that would make the design easier to implement without changing its overall approach.
									<br><br>
									Rating 4 &#150; The design used all recommended and likely components that fit the designer’s approach correctly. Likewise, assign this rating if components were appropriately not used. 
								</div>
							</td>
							<td class="valueC" nowrap>20</td>
							<td class="valueC" nowrap>2 - Somewhat Agree</td>
<% if (resolved) {%>
							<td class="valueC" nowrap>4 - Strongly Agree</td>
							<td class="valueC" nowrap>Resolved</td>
<%} else {%>
							<td class="valueC" nowrap>&nbsp;</td>
							<td class="valueC" nowrap>&nbsp;</td>
<%}%>
						</tr>
						<tr class="dark">
							<td class="value">
								<img src="../images/plus.gif" alt="open" border="0" /><b> Response 1: Comment</b>&nbsp;
								Lorem ipsum dolor sit amet, consectetuer ...<br>
								<img src="../images/plus.gif" alt="open" border="0" /><b> Appeal Text: </b>Lorem ipsum dolor sit amet, consectetuer&nbsp; nibh...
							</td>
							<td class="valueC" nowrap>&nbsp;</td>
							<td class="valueC" nowrap>&nbsp;</td>
							<td class="valueC" nowrap>&nbsp;</td>
							<td class="valueC" nowrap>&nbsp;</td>
						</tr>
						<tr class="light">
							<td class="value">
								<div class="showText" id="shortQ_6" style="width: 100%;">
									<a href="javascript:toggleDisplay('shortQ_6');toggleDisplay('longQ_6');" class="statLink"><img src="../images/plus.gif" alt="open" border="0" /></a> <b>Question x.x.x</b>
									The component presents an easy-to-use API for the application to
									use. Class structure and method names make usage of the API apparent
									to a new user. Each method has a singular purpose and causes minimal
									side-effects.
								</div>
								<div class="hideText" id="longQ_6">
									<a href="javascript:toggleDisplay('shortQ_6');toggleDisplay('longQ_6');" class="statLink"><img src="../images/minus.gif" alt="close" border="0" /></a>
									<b>Question x.x.x</b>
									The component presents an easy-to-use API for the application to use. Class structure and method names make usage of the API apparent to a new user. Each method has a singular purpose and causes minimal side-effects. Convenience methods should be carefully chosen to simplify the overall API. Excessive use of convenience methods should be avoided.
									
									<br><br>
									The reviewer should look at the demo section of the component specification to see the designer's vision of how the component will be used. The reviewer should then analyze how the API addresses the requirements and how easy it is to use the API to accomplish the requirements.
									<br><br>
									
									Rating 1 &#150; The API demonstrates an obvious lack of consistency, cohesion, or is generally hard to use. Tasks (as defined by the requirements) take multiple API calls to accomplish what should be done atomically.
									<br><br>
									Rating 2 &#150; The API is not immediately obvious; one cannot understand how to accomplish the required tasks by looking at the class diagram and reading the demo section (provide the name(s) of the requirement task(s) not easily performed using the API).
									<br><br>
									Rating 3 &#150; The API is obvious (by looking at the class diagram and the demo section) but could have been made simpler or more powerful/flexible (provide specific modifications to the API that would make it easier to use).
									<br><br>
									Rating 4 &#150; The API is consistent, powerful, flexible, and is easy to understand. The design of the API is driven by a single concept, making the API easy to learn. The common tasks (as defined by the requirements) are easy to perform using the API.
								</div>
							</td>
							<td class="valueC" nowrap>20</td>
							<td class="valueC" nowrap>2 - Somewhat Agree</td>
<% if (resolved) {%>
							<td class="valueC" nowrap>4 - Strongly Agree</td>
							<td class="valueC" nowrap>Resolved</td>
<%} else {%>
							<td class="valueC" nowrap>&nbsp;</td>
							<td class="valueC" nowrap>Unresolved</td>
<%}%>
						</tr>
						<tr class="dark">
							<td class="value">
								<img src="../images/plus.gif" alt="open" border="0" /><b> Response 1: Comment</b>&nbsp;Lorem ipsum dolor sit amet, consectetuer ...<br>
								<img src="../images/plus.gif" alt="open" border="0" /><b> Appeal Text: </b>Lorem ipsum dolor sit amet, consectetuer&nbsp; nibh ...
							</td>
							<td class="valueC" nowrap>&nbsp;</td>
							<td class="valueC" nowrap>&nbsp;</td>
							<td class="valueC" nowrap>&nbsp;</td>
							<td class="valueC" nowrap>&nbsp;</td>
						</tr>
						<tr>
							<td class="value">
								<div class="showText" id="shortQ_7" style="width: 100%;">
									<a href="javascript:toggleDisplay('shortQ_7');toggleDisplay('longQ_7');" class="statLink"><img src="../images/plus.gif" alt="open" border="0" /></a> <b>Question x.x.x</b>
									The design addresses whether the component is thread safe or not.
								</div>
								<div class="hideText" id="longQ_7">
									<a href="javascript:toggleDisplay('shortQ_7');toggleDisplay('longQ_7');" class="statLink"><img src="../images/minus.gif" alt="close" border="0" /></a>
									<b>Question x.x.x</b>
									The design addresses whether the component is thread safe or not.
									<br><br>
									If the component is deemed thread safe, the designer should have documented their approach to thread safety in order to determine if the approach is sound. The approach can come in two forms:
									<br><br>
									
									a) A high level overview written in the component specification and specific detail included in the class/method/variable documentation tabs.
									<br><br>
									b) A detailed approach written in the component specification and references to it written in the class/method/variable documentation tabs.
									
									<br><br>
									If the component is deemed not to be thread safe, the designer should document any reasons why the component is not thread safe and, if applicable, provide a high level summary of what could be done to make the component thread safe.
									<br><br>
									
									Rating 1 &#150; There is no mention of whether the component is thread safe or not.
									<br><br>
									Rating 2 &#150; The designer mentions thread safety but fails to provide the reasoning
									<br><br>
									Rating 3 &#150; The designer mentions thread safety but is incorrect in the reasoning
									<br><br>
									Rating 4 &#150; The designer mentions thread safety and provides correct arguments to back it up. 
								</div>
							</td>
							<td class="valueC" nowrap>20</td>
							<td class="valueC" nowrap>2 - Somewhat Agree</td>
<% if (resolved) {%>
							<td class="valueC" nowrap>2 - Somewhat Agree</td>
							<td class="valueC" nowrap>Resolved</td>
<%} else {%>
							<td class="valueC" nowrap>&nbsp;</td>
							<td class="valueC" nowrap>Resolved</td>
<%}%>
						</tr>
						<tr class="dark">
							<td class="value">
								<img src="../images/plus.gif" alt="open" border="0" /><b> Response 1: Comment</b>&nbsp;Lorem ipsum dolor sit amet, consectetuer ...<br>
								<img src="../images/plus.gif" alt="open" border="0" /><b> Appeal Text: </b>Lorem ipsum dolor sit amet, consectetuer&nbsp; nibh...<br>
								<img src="../images/plus.gif" alt="open" border="0" /><b> Response 1: Comment</b>&nbsp;
								Lorem ipsum dolor sit amet, consectetuer ...
							</td>
							<td class="valueC" nowrap>&nbsp;</td>
							<td class="valueC" nowrap>&nbsp;</td>
							<td class="valueC" nowrap>&nbsp;</td>
							<td class="valueC" nowrap>&nbsp;</td>
						</tr>
						<tr class="light">
							<td class="value">
								<div class="showText" id="shortQ_8" style="width: 100%;">
									<a href="javascript:toggleDisplay('shortQ_8');toggleDisplay('longQ_8');" class="statLink"><img src="../images/plus.gif" alt="open" border="0" /></a> <b>Question x.x.x</b>
									The classes are well scoped.
								</div>
								<div class="hideText" id="longQ_8">
									<a href="javascript:toggleDisplay('shortQ_8');toggleDisplay('longQ_8');" class="statLink"><img src="../images/minus.gif" alt="close" border="0" /></a>
									<b>Question x.x.x</b>
									
									The classes are well scoped.
									<br><br>
									The reviewer should determine whether the classes in the design attempt have too large of a scope (it tries to do too many tasks) or has too small of a scope (the task was broken down into too many classes). A good clue to a violation of this is whether the name of the class adequately encompasses the API of the class. If so, then the class is probably properly scoped (unless the name is too generic or too high-level).
									
									<br><br>
									Rating 1 &#150; Multiple unrelated groups of related classes violate scope and must be refactored into smaller or larger classes. Describe in your comments the purpose of refactoring for each class or a group of related classes, and state its tangible benefits to the design.
									
									Rating 2 &#150; A single group of related classes violates scope and must be refactored into smaller or larger classes. Describe in your comments the purpose of refactoring, and state its tangible benefits to the design.

									Rating 3 &#150; The classes seem fine but at least one class could potentially benefit from being refactored. Describe in your comments the proposed refactoring, and explain what makes the refactored solution preferable to the designer’s solution.
									
									Rating 4 &#150; All classes are scoped well. 
								</div>
							</td>
							<td class="valueC" nowrap>20</td>
							<td class="valueC" nowrap>4 - Strongly Agree</td>
							<td class="valueC" nowrap>&nbsp;</td>
							<td class="valueC" nowrap>&nbsp;</td>
						</tr>
						<tr class="dark">
							<td class="value">
								<img src="../images/plus.gif" alt="open" border="0" /><b> Response 1: Comment</b>&nbsp;
								Lorem ipsum dolor sit amet, consectetuer ...
							</td>
							<td class="valueC" nowrap>&nbsp;</td>
							<td class="valueC" nowrap>&nbsp;</td>
							<td class="valueC" nowrap>&nbsp;</td>
							<td class="valueC" nowrap>&nbsp;</td>
						</tr>
						<tr>
							<td class="header"></td>
							<td class="headerC"><b>Total</b></td>
							<td class="headerC">&nbsp;</td>
							<td class="headerC">&nbsp;</td>
							<td class="headerC">&nbsp;</td>
						</tr>
						<tr>
							<td class="value"></td>
							<td class="valueC" nowrap><b>92.07</b></td>
							<td class="valueC" nowrap>&nbsp;</td>
							<td class="valueC" nowrap>&nbsp;</td>
							<td class="valueC" nowrap>&nbsp;</td>
						</tr>
						<tr>
							<td class="lastRowTD" colspan="5"></td>
						</tr>
					</table>