<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page language="java" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="or" uri="/or-tags" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>

<head>
    <jsp:include page="/includes/project/project_title.jsp">
        <jsp:param name="thirdLevelPageKey" value="contactManager.title" />
    </jsp:include>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>

    <!-- TopCoder CSS -->
    <link type="text/css" rel="stylesheet" href="/css/style.css" />
    <link type="text/css" rel="stylesheet" href="/css/coders.css" />
    <link type="text/css" rel="stylesheet" href="/css/stats.css" />

    <!-- JS from wireframes -->
    <script language="javascript" type="text/javascript"
        src="/js/or/popup.js"><!-- @ --></script>
    <script language="javascript" type="text/javascript"
        src="/js/or/expand_collapse.js"><!-- @ --></script>

    <!-- CSS and JS by Petar -->
    <link type="text/css" rel="stylesheet" href="/css/or/new_styles.css" />
    <script language="JavaScript" type="text/javascript"
        src="/js/or/rollovers2.js"><!-- @ --></script>

    <!-- Reskin -->
    <link type="text/css" rel="stylesheet" href="/css/reskin-or/reskin.css">
    <link type="text/css" rel="stylesheet" href="/css/reskin-or/toasts.css">
    <script type="text/javascript">
        function checkMessage() {
            let message = document.querySelector('.inputTextBox');
            let submitButton = document.querySelector('.contactManager__submit');
            if(message.value.length > 0) {
                submitButton.disabled = false;
            } else {
                submitButton.disabled = true;
            }
        }

        document.addEventListener("DOMContentLoaded", function(){
            let avatar = document.querySelector('.webHeader__avatar a');
            let avatarImage = document.createElement('div');
            avatarImage.className = "webHeader__avatarImage";
            let twoChar = avatar.text.substring(0, 2);
            avatarImage.innerText = twoChar;
            avatar.innerHTML = avatarImage.outerHTML;

            for (const dropdown of document.querySelectorAll(".custom-select-wrapper")) {
                dropdown.addEventListener('click', function () {
                    this.querySelector('.custom-select').classList.toggle('open');
                });
            }

            for (const options of document.querySelectorAll('.custom-options')) {
                let selected = options.querySelector('.custom-option[selected]')
                if (!selected) {
                    let option = options.querySelector('.custom-option');
                    option.selected = true;
                    option.classList.add('selected');
                } else {
                    selected.classList.add('selected');
                }
            }

            for (const option of document.querySelectorAll(".custom-option")) {
                const selectedSpan = option.closest('.custom-select').querySelector('.custom-select__trigger span');
                if (option.classList.contains('selected')) {
                    const currentSelected = option.textContent;
                    selectedSpan.classList.add('custom-select__trigger--unselected');
                    if (selectedSpan.textContent == '') {
                        selectedSpan.textContent = currentSelected;
                    }
                }
                option.addEventListener('click', function (e) {
                    if (!this.classList.contains('selected')) {
                        this.parentNode.querySelector('.custom-option.selected').classList.remove('selected');
                        this.classList.add('selected');
                        if (this.textContent == 'Select') {
                            selectedSpan.classList.add('custom-select__trigger--unselected');
                        } else {
                            selectedSpan.classList.remove('custom-select__trigger--unselected');
                        }
                        selectedSpan.textContent = this.textContent;
                        document.getElementById('category').value = this.getAttribute('data-value');
                    }
                });
            }

            window.addEventListener('click', function (e) {
                for (const select of document.querySelectorAll('.custom-select')) {
                    if (!select.contains(e.target)) {
                        select.classList.remove('open');
                    }
                }
            });

            document.querySelector(".contactManager__submit").addEventListener("click", function(e){
                e.preventDefault();
                document.getElementById('ContactManager').submit();
            });
        });
    </script>
</head>

<body>

<jsp:include page="/includes/inc_header_reskin.jsp" />

<jsp:include page="/includes/project/project_tabs_reskin.jsp" />

<div class="content">
    <div class="content__inner">
        <div class="projectInfo">
            <h1 class="projectInfo__projectName">${project.allProperties["Project Name"]}</h1>
            <div class="projectInfo__info">
                <c:if test='${!(empty project.allProperties["Project Version"])}'>
                    <p class="projectInfo__projectVersion"><or:text key="global.version" /> ${project.allProperties["Project Version"]}</p>
                </c:if>
                <p class="projectInfo__projectStatus"><or:text key="viewProjectDetails.ProjectStatus" /> ${projectStatus}</p>
            </div>
        </div>
        <div class="divider"></div>
        <div class="contactManager">
            <s:actionerror  escape="false"/>
            <s:form action="ContactManager" namespace="/actions">
                <div class="contactManager__title">Contact Managers</div>
                <input type="hidden" name="postBack" value="y" />
                <input type="hidden" name="pid" value="${project.id}" />
                <div class="contactManager__category">
                    <input type="hidden" id="category" name="cat" value="">
                    <div class="custom-select-wrapper">
                        <div class="custom-select">
                            <span class="custom-select__label"><or:text key="contactManager.Category" /></span>
                            <div class="custom-select__trigger"><span></span>
                                <div class="arrow"></div>
                            </div>
                            <div class="custom-options">
                                <c:set var="OR_FIELD_TO_SELECT" value="cat"/>
                                <span class="custom-option" data-value="" <or:selected value=""/>><or:text key="contactManager.Category.no" /></span>
                                <span class="custom-option" data-value="Question" <or:selected value="Question"/>><or:text key="contactManager.Category.Question" /></span>
                                <span class="custom-option" data-value="Comment" <or:selected value="Comment"/>><or:text key="contactManager.Category.Comment" /></span>
                                <span class="custom-option" data-value="Complaint" <or:selected value="Complaint"/>><or:text key="contactManager.Category.Complaint" /></span>
                                <span class="custom-option" data-value="Other" <or:selected value="Other"/>><or:text key="contactManager.Category.Other" /></span>
                            </div>
                        </div>
                    </div>
                </div>

                <div class="contactManager__message">
                    <label for="message">Message</label>
                    <textarea name="msg" cols="20" rows="5" class="inputTextBox" placeholder="Type your message" onkeyup="checkMessage()" id="message"><or:fieldvalue field="msg" /></textarea>
                </div>

                <div class="contactManager__button">
                    <a href="<or:url value='/actions/ViewProjectDetails?pid=${project.id}' />" class="contactManager__cancel">
                        <or:text key='btnCancel.alt' />
                    </a>
                    <input type="submit" value="<or:text key='btnSubmit.alt' />" class="contactManager__submit" disabled/>
                </div>
            </s:form>
        </div>
    </div>
</div>

<jsp:include page="/includes/inc_footer_reskin.jsp" />

</body>
</html>
