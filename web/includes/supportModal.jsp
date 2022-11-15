<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page language="java" isELIgnored="false" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="or" uri="/or-tags" %>
<%@ taglib prefix="orfn" uri="/tags/or-functions" %>
<%@ taglib prefix="tc-webtag" uri="/tags/tc-webtags" %>

    <div id="supportModal" class="modal">
        <div class="modal__bg modal__exit"></div>
        <div class="modal__content modal__md">
            <div class="modal__header">
                <h5 class="modal__title">We're Here to Help</h5>
            </div>
            <hr class="modal__divider" />
            <div class="modal__body">
                <div class="support__info">
                    <p> Hi ${not empty userFirstName ? userFirstName : 'there'} , we're here to help.</p>
                    <p>Please describe what you'd like to discuss, and a Topcoder Solutions Expert will email you back&nbsp;at
                    <strong>${not empty userEmail ? userEmail : null}</strong>&nbsp;within one business day.</p>
                </div>
                <form id="contactSupport">
                    <div class="support__contact">
                        <div class="input__wrapper">
                            <div class="support__input support__inputText">
                                <label for="firstName"><or:text key="supportForm.firstName" /></label>
                                <input type="text" name="firstName" id="firstName" class="inputBox" onblur="validateField('firstName')" value="${userFirstName}"/>
                            </div>
                            <div class="supportInput__error hide">
                                <img src="/i/reskin/error.svg" alt="error" border="0"><span></span>
                            </div>
                        </div>
                        <div class="input__wrapper">
                            <div class="support__input support__inputText">
                                <label for="lastName"><or:text key="supportForm.lastName" /></label>
                                <input type="text" name="lastName" id="lastName" class="inputBox" value="${userLastName}" onblur="validateField('lastName')" />
                            </div>
                            <div class="supportInput__error hide">
                                <img src="/i/reskin/error.svg" alt="error" border="0"><span></span>
                            </div>
                        </div>
                        <div class="input__wrapper">
                            <div class="support__input support__inputText">
                                <label for="email"><or:text key="supportForm.email" /></label>
                                <input type="text" name="email" id="email" class="inputBox" value="${userEmail}"
                                    onblur="validateField('email');" />
                            </div>
                            <div class="supportInput__error hide">
                                <img src="/i/reskin/error.svg" alt="error" border="0"><span></span>
                            </div>
                        </div>
                        <div class="input__wrapper">
                            <div class="support__input support__message">
                                <label for="question"><or:text key="supportForm.textArea" /></label>
                                <textarea name="question" cols="20" rows="5" class="inputTextBox"  onblur="validateField('question');" id="question"></textarea>
                            </div>
                            <div class="supportInput__error hide">
                                <img src="/i/reskin/error.svg" alt="error" border="0"><span></span>
                            </div>
                        </div>
                    </div>
                    <hr class="modal__divider" style="margin: 16px 0;"/>
                    <div class="submitForm__error hide" id="submitFormErr">
                        <img src="/i/reskin/form-error.svg" alt="error" border="0"><span></span>
                    </div>
                    <div class="submit__container">
                        <button class="submit__btn secondary" id="submit" disabled>Submit</button>
                    </div>
                </form>
            </div>
            <a class="modal__close modal__exit">
                <img src="/i/reskin/modal-close.svg" alt="close" border="0">
            </a>
        </div>
    </div>

<script type="text/javascript">

    var validationRules = {
        "firstName": function(value){
            return value == '';
        },
        "lastName": function(value){
            return value == '';
        },
        "email": function(value){
            return !checkEmail(value) || value == '';
        },
        "question": function(value){
            return value == '';
        }
    }

    var errorMsgMap = {
        "firstName": 'Required',
        "lastName": 'Required',
        "email": 'Invalid Email',
        "question": 'Required',
    };

    const fields = ['firstName', 'lastName', 'email', 'question'];

    const loaderContainer = document.querySelector('.loading-spinner');

    function validateField(field) {
        let input = document.getElementById(field);
        let inputParent = input.parentElement;
        if (validationRules[field](input.value)) {
            inputParent.classList.add("inputWrapper__error");
            setError(input, errorMsgMap[field]);
            return false;
        }
        return true;
    }

    function setError(inputElem, message) {
        let formControl = inputElem.parentElement;
        let wrapper = formControl.parentElement
        let error = wrapper.querySelector('.supportInput__error');
        let span = error.querySelector('span')
        span.innerText = message;
        error.classList.remove("hide");
    }

    function clearError(input) {
        let inputElem = document.getElementById(input);
        let field = inputElem.parentElement;
        let wrapper = field.parentElement;
        let error = wrapper.querySelector('.supportInput__error');
        error.classList.add("hide");
        if (field.classList.contains("inputWrapper__error")) {
            field.classList.remove("inputWrapper__error");
        }
        if (field.classList.contains("inputWrapper__error")) {
            field.classList.remove("inputWrapper__error");
        }
    }

    function clearAllError(fields) {
        let errElem = document.getElementById('submitFormErr')
        fields.forEach(function(field) {
            clearError(field)
        });
        if (!errElem.classList.contains("hide")) {
            errElem.classList.add("hide");
        }
    }

    var inputs = document.querySelectorAll('.support__input');
    let submitBtn = document.getElementById('submit');

    let inputValidator = {
        "firstName": false,
        "lastName": false,
        "email": false,
        "question": false,
    }

    fields.forEach(function(field) {
        let inputElem = document.getElementById(field);
        if (!validationRules[field](inputElem.value)) {
            inputValidator[field] = true;
        };
    });

    inputs.forEach(function(input) {
        input.addEventListener('input', function() {
            let name = event.target.getAttribute('name');
            clearError(name);
            if (!validationRules[name](event.target.value)) {
                inputValidator[name] = true;
            } else {
                inputValidator[name] = false;
            };

            let allValid = Object.keys(inputValidator).every((item) => {
                return inputValidator[item] === true
            });

            if (allValid) {
                submitBtn.disabled = false;
            } else {
                submitBtn.disabled = true;
            }
        });
    });

    function isFormValid() {
        let fields = ['firstName', 'lastName', 'email', 'question'];
        let valid = true;
        fields.forEach(function(field) {
            if (validateField(field)) {
                valid = valid && true;
            } else {
                valid = false;
            };
        });
        return valid;
    }

    function showSpinner() {
        loaderContainer.classList.remove('hide');
    }

    function removeSpinner() {
        loaderContainer.classList.add('hide');
    }

    function resetForm(form) {
        form.reset();
        document.getElementById('supportModal').classList.remove("show");
    }

    function showToast(msg) {
        toast({
        message: msg,
        type: "success",
        duration: 4000
        });
    }

    function supportReqError(err) {
        removeSpinner();
        const submitFormErr = document.getElementById('submitFormErr');
        submitFormErr.classList.remove('hide');
        const formErr = submitFormErr.querySelector('span');
        formErr.innerText = err;
    }

    function openSupportModal() {
        const privacyModal = document.getElementById('privacyPolicyModal');
        const supportModal = document.getElementById('supportModal');
        if (privacyModal.classList.contains("show")) {
            privacyModal.classList.remove("show");
        }
        supportModal.classList.add('show');
    }
</script>
<script type="text/javascript">
    let form = document.getElementById('contactSupport');
    form.addEventListener('submit', function(e){
        e.preventDefault();
        if (isFormValid()) {
            const payload = new FormData(form);
            showSpinner();
            fetch("<%=com.cronos.onlinereview.util.ConfigHelper.getSupportRequestUrlV5()%>", {
                method: "POST",
                body: payload,
            })
                .then((response) => response.json())
                .then((data) => {
                    removeSpinner();
                    resetForm(form);
                    showToast('Your request has been submitted');
                })
                .catch(err => supportReqError(err));
        }
    });
</script>
