<%
    String action = (String) request.getParameter("action");
%>
<%if ("edit".equalsIgnoreCase(action)) {%>
					<h3>Edit Review Scorecard<br>
<%} else {%>
					<h3>Review Scorecard </h3>
<%}%>
					<table class="scorecard" cellpadding="0" width="100%" style="border-collapse: collapse;" id="table2">
						<tr>
							<td class="title" colspan="3">Group</td>
						</tr>
						<tr>
							<td class="subheader"><b>Section</b></td>
							<td class="subheader"><b>Weight</b></td>
							<td class="subheader">Response</td>
						</tr>
						<tr class="light">
							<td class="value">
								<img src="../images/plus.gif" alt="open" border="0" /><b> Question x.x.x</b>
								The design addresses all the requirements as specified in the
								requirements documents and the forum. The reviewer should be able to
								take each point in the requirements document and find the associated<br>
							</td>
							<td class="valueC">50</td>
							<td class="valueC">
								<select size="1" name="D46" class="inputBox" >
									<option <%if (null == action) {%>selected<%}%>>Select</option>
									<option value="1 - Strongly Disagree">1 - Strongly Disagree</option>
									<option value="2 - Somewhat Agree">2 - Somewhat Agree</option>
									<option value="3 - Agree">3 - Agree</option>
									<option value="4 - Strongly Agree" <%if ("edit".equalsIgnoreCase(action)) {%>selected<%}%>>4 - Strongly Agree</option>
								</select>
							</td>
						</tr>
						<tr class="highlighted">
							<td class="value" colspan="3"><b>Response 1: </b>
								<select size="1" name="D49" class="inputBox" >
									<option>Recommended</option>
									<option>Required</option>
									<option selected>Comment</option>
								</select><br>
<%if ("edit".equalsIgnoreCase(action)) {%>
								<textarea rows="2" name="S7" cols="20" class="inputTextBox">Morbi varius tempor nibh. Nam ultrices auctor elit. In orci. Etiam rutrum. Praesent vitae erat ac ante pulvinar adipiscing. Morbi nec felis. Integer scelerisque porta magna. Donec leo mi, aliquet quis, congue eu, pretium eu, turpis. Nullam quis ligula vel ligula rutrum blandit. </textarea><br>
								<b>Document Upload</b> (Required):  
								<input name="T57" size="20" class="inputBox" style="width: 144;" value="test.doc">
<%} else {%>
								<textarea rows="2" name="S7" cols="20" class="inputTextBox"></textarea><br>
								<b>Document Upload</b> (Required):
								<input name="T57" size="20" class="inputBox" style="width: 144;">
<%}%>
								<a href="#"><img src="../images/bttn_browse.gif" width="76" height="18" border="0"></a>
								<a href="#"><img src="../images/bttn_upload.gif" width="61" height="18" border="0"></a>
							</td>
						</tr>
						<tr>
							<td class="value">
								<img src="../images/plus.gif" alt="open" border="0" /><b> Question x.x.x</b>
								The design addresses all the requirements as specified in the
								requirements documents and the forum. The reviewer should be able to
								take each point in the requirements document and find the associated<br>
							</td>
							<td class="valueC">50</td>
							<td class="valueC">
								<select size="1" name="D47" class="inputBox" >
									<option selected>Select</option>
									<option <%if (null == action) {%>selected<%}%>>Select</option>
									<option value="1 - Strongly Disagree">1 - Strongly Disagree</option>
									<option value="2 - Somewhat Agree">2 - Somewhat Agree</option>
									<option value="3 - Agree">3 - Agree</option>
									<option value="4 - Strongly Agree" <%if ("edit".equalsIgnoreCase(action)) {%>selected<%}%>>4 - Strongly Agree</option>
								</select>
							</td>
						</tr>
						<tr class="highlighted">
							<td class="value" colspan="3"><b>Response 1: </b>
								<select size="1" name="D45" class="inputBox" >
									<option>Recommended</option>
									<option>Required</option>
									<option selected>Comment</option>
								</select><br>
<%if ("edit".equalsIgnoreCase(action)) {%>
								<textarea rows="2" name="S7" cols="20" class="inputTextBox">Morbi varius tempor nibh. Nam ultrices auctor elit. In orci. Etiam rutrum. Praesent vitae erat ac ante pulvinar adipiscing. Morbi nec felis. Integer scelerisque porta magna. Donec leo mi, aliquet quis, congue eu, pretium eu, turpis. Nullam quis ligula vel ligula rutrum blandit. </textarea><br>
								<b>Document Upload</b> (Required):  
								<input name="T57" size="20" class="inputBox" style="width: 144;" value="test.doc">
<%} else {%>
								<textarea rows="2" name="S7" cols="20" class="inputTextBox"></textarea><br>
								<b>Document Upload</b> (Required):
								<input name="T57" size="20" class="inputBox" style="width: 144;">
<%}%>
							</td>
						</tr>
						<tr>
							<td class="value">
								<img src="../images/plus.gif" alt="open" border="0" /><b> Question x.x.x</b>
								The design addresses all the requirements as specified in the
								requirements documents and the forum. The reviewer should be able to
								take each point in the requirements document and find the associated<br>
							</td>
							<td class="valueC">50</td>
							<td class="valueC">
								<select size="1" name="D48" class="inputBox" >
									<option <%if (null == action) {%>selected<%}%>>Select</option>
									<option value="1 - Strongly Disagree">1 - Strongly Disagree</option>
									<option value="2 - Somewhat Agree">2 - Somewhat Agree</option>
									<option value="3 - Agree">3 - Agree</option>
									<option value="4 - Strongly Agree" <%if ("edit".equalsIgnoreCase(action)) {%>selected<%}%>>4 - Strongly Agree</option>
								</select>
							</td>
						</tr>
						<tr class="highlighted">
							<td class="value" colspan="3"><b>Response 1: </b>
								<select size="1" name="D50" class="inputBox" >
									<option>Recommended</option>
									<option>Required</option>
									<option selected>Comment</option>
								</select><br>
<%if ("edit".equalsIgnoreCase(action)) {%>
								<textarea rows="2" name="S7" cols="20" class="inputTextBox">Morbi varius tempor nibh. Nam ultrices auctor elit. In orci. Etiam rutrum. Praesent vitae erat ac ante pulvinar adipiscing. Morbi nec felis. Integer scelerisque porta magna. Donec leo mi, aliquet quis, congue eu, pretium eu, turpis. Nullam quis ligula vel ligula rutrum blandit. </textarea><br>
								<b>Document Upload</b> (Required):  
								<input name="T57" size="20" class="inputBox" style="width: 144;" value="test.doc">
<%} else {%>
								<textarea rows="2" name="S7" cols="20" class="inputTextBox"></textarea><br>
								<b>Document Upload</b> (Required):
								<input name="T57" size="20" class="inputBox" style="width: 144;">
<%}%>
							</td>
						</tr>
						<tr>
							<td class="lastRowTD" colspan="3"></td>
						</tr>
					</table><br>
					<table class="scorecard" cellpadding="0" width="100%" style="border-collapse: collapse;" id="table3">
						<tr>
							<td class="title">Group</td>
							<td class="title"><center>&nbsp;</center></td>
							<td class="title" style="text-align: center">&nbsp;</td>
						</tr>
						<tr>
							<td class="subheader"><b>Section</b></td>
							<td class="subheader"><b>Weight</b></td>
							<td class="subheader">Response</td>
						</tr>
						<tr class="light">
							<td class="value">
								<img src="../images/plus.gif" alt="open" border="0" /><b> Question x.x.x</b>
								The design addresses all the requirements as specified in the
								requirements documents and the forum. The reviewer should be able to
								take each point in the requirements document and find the associated<br>
							</td>
							<td class="valueC">33</td>
							<td class="valueC">
								<select size="1" name="D52" class="inputBox" >
									<option <%if (null == action) {%>selected<%}%>>Select</option>
									<option value="1 - Strongly Disagree">1 - Strongly Disagree</option>
									<option value="2 - Somewhat Agree">2 - Somewhat Agree</option>
									<option value="3 - Agree">3 - Agree</option>
									<option value="4 - Strongly Agree" <%if ("edit".equalsIgnoreCase(action)) {%>selected<%}%>>4 - Strongly Agree</option>
								</select>
							</td>
						</tr>
						<tr class="highlighted">
							<td class="value" colspan="3"><b>Response 1: </b>
								<select size="1" name="D51" class="inputBox" >
									<option>Recommended</option>
									<option>Required</option>
									<option selected>Comment</option>
								</select><br>
<%if ("edit".equalsIgnoreCase(action)) {%>
								<textarea rows="2" name="S7" cols="20" class="inputTextBox">Morbi varius tempor nibh. Nam ultrices auctor elit. In orci. Etiam rutrum. Praesent vitae erat ac ante pulvinar adipiscing. Morbi nec felis. Integer scelerisque porta magna. Donec leo mi, aliquet quis, congue eu, pretium eu, turpis. Nullam quis ligula vel ligula rutrum blandit. </textarea><br>
								<b>Document Upload</b> (Required):  
								<input name="T57" size="20" class="inputBox" style="width: 144;" value="test.doc">
<%} else {%>
								<textarea rows="2" name="S7" cols="20" class="inputTextBox"></textarea><br>
								<b>Document Upload</b> (Required):
								<input name="T57" size="20" class="inputBox" style="width: 144;">
<%}%>
							</td>
						</tr>
						<tr>
							<td class="value">
								<img src="../images/plus.gif" alt="open" border="0" /><b> Question x.x.x</b>
								The design addresses all the requirements as specified in the
								requirements documents and the forum. The reviewer should be able to
								take each point in the requirements document and find the associated<br>
							</td>
							<td class="valueC">33</td>
							<td class="valueC">
								<select size="1" name="D55" class="inputBox" >
									<option <%if (null == action) {%>selected<%}%>>Select</option>
									<option value="1 - Strongly Disagree">1 - Strongly Disagree</option>
									<option value="2 - Somewhat Agree">2 - Somewhat Agree</option>
									<option value="3 - Agree">3 - Agree</option>
									<option value="4 - Strongly Agree" <%if ("edit".equalsIgnoreCase(action)) {%>selected<%}%>>4 - Strongly Agree</option>
								</select>
							</td>
						</tr>
						<tr class="highlighted">
							<td class="value" colspan="3" nowrap><b>Response 1: </b>
								<select size="1" name="D54" class="inputBox" >
									<option>Recommended</option>
									<option>Required</option>
									<option selected>Comment</option>
								</select><br>
								<textarea rows="2" name="S11" cols="20" class="inputTextBox"></textarea><br>
								<b>Document Upload</b> (Optional):
								<input name="T56" size="20" class="inputBox" style="width: 144;">
								<a href="#"><img src="../images/bttn_browse.gif" width="76" height="18" border="0"></a>
								<a href="#"><img src="../images/bttn_upload.gif" width="61" height="18" border="0"></a>
							</td>
						</tr>
						<tr>
							<td class="value">
								<img src="../images/plus.gif" alt="open" border="0" /><b> Question x.x.x</b>
								The design addresses all the requirements as specified in the
								requirements documents and the forum. The reviewer should be able to
								take each point in the requirements document and find the associated<br>
							</td>
							<td class="valueC">34</td>
							<td class="valueC">
								<select size="1" name="D57" class="inputBox" >
									<option <%if (null == action) {%>selected<%}%>>Select</option>
									<option value="1 - Strongly Disagree">1 - Strongly Disagree</option>
									<option value="2 - Somewhat Agree">2 - Somewhat Agree</option>
									<option value="3 - Agree">3 - Agree</option>
									<option value="4 - Strongly Agree" <%if ("edit".equalsIgnoreCase(action)) {%>selected<%}%>>4 - Strongly Agree</option>
								</select>
							</td>
						</tr>
						<tr class="highlighted">
							<td class="value" colspan="3"><b>Response 1: </b>
								<select size="1" name="D56" class="inputBox" >
									<option>Recommended</option>
									<option>Required</option>
									<option selected>Comment</option>
								</select><br>
								<textarea rows="2" name="S12" cols="20" class="inputTextBox"></textarea>
							</td>
						</tr>
						<tr>
							<td class="lastRowTD" colspan="3"></td>
						</tr>
					</table>