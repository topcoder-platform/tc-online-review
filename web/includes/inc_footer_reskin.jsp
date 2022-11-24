<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="or" uri="/or-tags" %>
<%@ taglib prefix="orfn" uri="/tags/or-functions" %>
<%@ page import="com.topcoder.onlinereview.component.webcommon.ApplicationServer" %>
<script type="text/javascript">
    document.addEventListener("DOMContentLoaded", function() {
        const modals = document.querySelectorAll("[data-modal]");

        modals.forEach(function (trigger) {
            trigger.addEventListener("click", function (event) {
                event.preventDefault();
                const modal = document.getElementById(trigger.dataset.modal);
                initValidate();
                modal.querySelector(".modal__body").scrollTop = 0;
                modal.classList.add("show");
                const exits = modal.querySelectorAll(".modal__exit");
                exits.forEach(function (exit) {
                    exit.addEventListener("click", function (event) {
                        event.preventDefault();
                        document.getElementById("contactSupport").reset();
                        clearAllError(fields);
                        modal.classList.remove("show");
                    });
                });
            });
        });
    });
</script>

<script language="JavaScript" type="text/javascript" src="/js/or/validation.js"><!-- @ --></script>
<script language="JavaScript" type="text/javascript" src="/js/toasts.js"><!-- @ --></script>

<footer class="webFooter">
    <div class="webFooter__inner">
        <div class="webFooter__links">
            <span>&copy; 2022 Topcoder</span>
            <a data-modal="supportModal">Support</a>
            <a data-modal="termsModal">Terms</a>
            <a data-modal="privacyPolicyModal">Privacy Policy</a>
        </div>

        <div class="webFooter__socials">
            <a href="https://www.facebook.com/topcoder" target="_blank" rel="noreferrer">
                <svg width="16" height="16" viewBox="0 0 16 16" fill="none" xmlns="http://www.w3.org/2000/svg"><path fill-rule="evenodd" clip-rule="evenodd" d="M8 1.33333C11.676 1.33333 14.6667 4.324 14.6667 8C14.6667 11.676 11.676 14.6667 8 14.6667C4.324 14.6667 1.33333 11.676 1.33333 8C1.33333 4.324 4.324 1.33333 8 1.33333ZM8 0C3.582 0 0 3.582 0 8C0 12.418 3.582 16 8 16C12.418 16 16 12.418 16 8C16 3.582 12.418 0 8 0ZM6.66667 6.66667H5.33333V8H6.66667V12H8.66667V8H9.88L10 6.66667H8.66667V6.11133C8.66667 5.79267 8.73067 5.66667 9.03867 5.66667H10V4H8.39733C7.19867 4 6.66667 4.528 6.66667 5.53867V6.66667Z" fill="#767676"></path></svg>
            </a>
            <a href="https://www.youtube.com/channel/UCFv29ANLT2FQmtvS9DRixNA" target="_blank" rel="noreferrer">
                <svg width="16" height="16" viewBox="0 0 16 16" fill="none" xmlns="http://www.w3.org/2000/svg"><path fill-rule="evenodd" clip-rule="evenodd" d="M10.82 4.73466C9.48534 4.64399 6.51201 4.64466 5.17934 4.73466C3.73601 4.83333 3.56667 5.70533 3.55534 7.99999C3.56667 10.2907 3.73534 11.1667 5.18001 11.2653C6.51267 11.3553 9.486 11.356 10.8207 11.2653C12.264 11.1667 12.434 10.294 12.4453 7.99999C12.4333 5.70933 12.2647 4.83333 10.82 4.73466ZM6.66667 9.4813V6.51864L9.852 7.9973L6.66667 9.4813ZM8 1.33333C11.676 1.33333 14.6667 4.324 14.6667 8C14.6667 11.676 11.676 14.6667 8 14.6667C4.324 14.6667 1.33333 11.676 1.33333 8C1.33333 4.324 4.324 1.33333 8 1.33333ZM8 0C3.582 0 0 3.582 0 8C0 12.418 3.582 16 8 16C12.418 16 16 12.418 16 8C16 3.582 12.418 0 8 0Z" fill="#767676"></path></svg>
            </a>
            <a href="https://www.linkedin.com/company/topcoder" target="_blank" rel="noreferrer">
                <svg width="16" height="16" viewBox="0 0 16 16" fill="none" xmlns="http://www.w3.org/2000/svg"><path fill-rule="evenodd" clip-rule="evenodd" d="M8 1.33333C11.676 1.33333 14.6667 4.324 14.6667 8C14.6667 11.676 11.676 14.6667 8 14.6667C4.324 14.6667 1.33333 11.676 1.33333 8C1.33333 4.324 4.324 1.33333 8 1.33333ZM8 0C3.582 0 0 3.582 0 8C0 12.418 3.582 16 8 16C12.418 16 16 12.418 16 8C16 3.582 12.418 0 8 0ZM6.66667 5.3333C6.66667 5.70463 6.36867 6.0053 6 6.0053C5.63133 6.0053 5.33333 5.7053 5.33333 5.3333C5.33333 4.96196 5.63133 4.6613 6 4.6613C6.36867 4.6613 6.66667 4.96263 6.66667 5.3333ZM6.66667 6.66667H5.33333V10.6667H6.66667V6.66667ZM8.66667 6.66669H7.33333V10.6667H8.66667V8.75935C8.66667 7.61135 10.0013 7.50535 10.0013 8.75935V10.6667H11.3333V8.42735C11.3333 6.23802 9.248 6.31802 8.66667 7.39535V6.66669Z" fill="#767676"></path></svg>
            </a>
            <a href="https://twitter.com/topcoder" target="_blank" rel="noreferrer">
                <svg width="16" height="16" viewBox="0 0 16 16" fill="none" xmlns="http://www.w3.org/2000/svg"><path fill-rule="evenodd" clip-rule="evenodd" d="M8 1.33333C11.676 1.33333 14.6667 4.324 14.6667 8C14.6667 11.676 11.676 14.6667 8 14.6667C4.324 14.6667 1.33333 11.676 1.33333 8C1.33333 4.324 4.324 1.33333 8 1.33333ZM8 0C3.582 0 0 3.582 0 8C0 12.418 3.582 16 8 16C12.418 16 16 12.418 16 8C16 3.582 12.418 0 8 0ZM12.3333 5.85203C12.0393 5.9827 11.7227 6.0707 11.3907 6.1107C11.73 5.90736 11.9893 5.58603 12.1127 5.2027C11.7953 5.3907 11.444 5.52736 11.07 5.6007C10.7713 5.28136 10.344 5.08203 9.87267 5.08203C8.81267 5.08203 8.034 6.0707 8.27333 7.09736C6.91 7.0287 5.7 6.37536 4.89067 5.3827C4.46067 6.12003 4.668 7.08536 5.39867 7.57403C5.13 7.56536 4.87733 7.49136 4.656 7.3687C4.638 8.1287 5.18333 8.84003 5.97267 8.9987C5.742 9.06137 5.48867 9.07603 5.23133 9.0267C5.44 9.6787 6.04733 10.1527 6.76467 10.166C6.07333 10.7074 5.20467 10.9494 4.33333 10.8467C5.06 11.3127 5.922 11.584 6.84867 11.584C9.89667 11.584 11.618 9.01003 11.514 6.70136C11.8353 6.4707 12.1133 6.18136 12.3333 5.85203Z" fill="#767676"></path></svg>
            </a>
            <a href="https://www.instagram.com/topcoder" target="_blank" rel="noreferrer">
                <svg width="16" height="16" viewBox="0 0 16 16" fill="none" xmlns="http://www.w3.org/2000/svg"><path fill-rule="evenodd" clip-rule="evenodd" d="M8 1.33333C11.676 1.33333 14.6667 4.324 14.6667 8C14.6667 11.676 11.676 14.6667 8 14.6667C4.324 14.6667 1.33333 11.676 1.33333 8C1.33333 4.324 4.324 1.33333 8 1.33333ZM8 0C3.582 0 0 3.582 0 8C0 12.418 3.582 16 8 16C12.418 16 16 12.418 16 8C16 3.582 12.418 0 8 0ZM8 4.72135C9.068 4.72135 9.19466 4.72535 9.61666 4.74469C10.7013 4.79402 11.2067 5.30802 11.2567 6.38402C11.2753 6.80602 11.2793 6.93202 11.2793 8.00002C11.2793 9.06802 11.2753 9.19469 11.2567 9.61602C11.2067 10.6914 10.702 11.2067 9.61666 11.256C9.19466 11.2747 9.06866 11.2794 8 11.2794C6.932 11.2794 6.80533 11.2754 6.384 11.256C5.29733 11.206 4.794 10.69 4.744 9.61602C4.72533 9.19469 4.72066 9.06802 4.72066 8.00002C4.72066 6.93202 4.72533 6.80535 4.744 6.38402C4.79333 5.30735 5.29866 4.79335 6.384 4.74402C6.80533 4.72469 6.932 4.72135 8 4.72135ZM8 4C6.91333 4 6.778 4.00467 6.35067 4.02467C4.89733 4.09133 4.09067 4.89733 4.024 6.35067C4.00467 6.778 4 6.914 4 8C4 9.08667 4.00467 9.22267 4.024 9.64933C4.09067 11.102 4.89733 11.9093 6.35067 11.976C6.778 11.9953 6.91333 12 8 12C9.08667 12 9.22267 11.9953 9.65 11.976C11.1007 11.9093 11.9107 11.1033 11.976 9.64933C11.9953 9.22267 12 9.08667 12 8C12 6.914 11.9953 6.778 11.976 6.35067C11.9107 4.89933 11.1033 4.09067 9.65 4.02467C9.22267 4.00467 9.08667 4 8 4ZM8 5.94596C6.866 5.94596 5.946 6.8653 5.946 7.99996C5.946 9.13463 6.866 10.054 8 10.054C9.134 10.054 10.054 9.13463 10.054 7.99996C10.054 6.86596 9.134 5.94596 8 5.94596ZM8 9.33333C7.26333 9.33333 6.66667 8.73667 6.66667 8C6.66667 7.264 7.26333 6.66667 8 6.66667C8.736 6.66667 9.334 7.26333 9.334 8C9.334 8.73667 8.736 9.33333 8 9.33333ZM10.1347 5.38534C9.87 5.38534 9.65466 5.6 9.65466 5.86534C9.65466 6.13 9.86933 6.34534 10.1347 6.34534C10.4 6.34534 10.6153 6.13067 10.6153 5.86534C10.6153 5.6 10.4007 5.38534 10.1347 5.38534Z" fill="#767676"></path></svg>
            </a>
        </div>
    </div>
</footer>
<div class="loading-spinner">
    <div class="spinner"></div>
</div>

<div id="toast"></div>

<jsp:include page="/includes/privacyPolicyModal.jsp" />
<jsp:include page="/includes/termsModal.jsp" />
<jsp:include page="/includes/supportModal.jsp" />

<%-- Analytics --%>

<script>
  !function(){var analytics=window.analytics=window.analytics||[];if(!analytics.initialize)if(analytics.invoked)window.console&&console.error&&console.error("Segment snippet included twice.");else{analytics.invoked=!0;analytics.methods=["trackSubmit","trackClick","trackLink","trackForm","pageview","identify","reset","group","track","ready","alias","debug","page","once","off","on","addSourceMiddleware","addIntegrationMiddleware","setAnonymousId","addDestinationMiddleware"];analytics.factory=function(e){return function(){var t=Array.prototype.slice.call(arguments);t.unshift(e);analytics.push(t);return analytics}};for(var e=0;e<analytics.methods.length;e++){var key=analytics.methods[e];analytics[key]=analytics.factory(key)}analytics.load=function(key,e){var t=document.createElement("script");t.type="text/javascript";t.async=!0;t.src="https://cdn.segment.com/analytics.js/v1/" + key + "/analytics.min.js";var n=document.getElementsByTagName("script")[0];n.parentNode.insertBefore(t,n);analytics._loadOptions=e};analytics.SNIPPET_VERSION="4.13.1";
  analytics.load("CSmkPqh4LsMPA5TVTxQSx98M7OvbuUQl");
  analytics.page();
  }}();
</script>

<script>
analytics.identify('', {
  <%--name: '<tc-webtag:handle coderId="${orfn:getLoggedInUserId(pageContext.request)}" />' --%>
  id: '${orfn:getLoggedInUserId(pageContext.request)}'
});
</script>

<script>
function prepareFrame() {
    var ifrm = document.createElement("iframe");
    ifrm.setAttribute("src", "<%=com.cronos.onlinereview.util.ConfigHelper.getNewAuthUrl()%>");
    ifrm.style.width = "0px";
    ifrm.style.height = "0px";
    document.body.appendChild(ifrm);
}
window.onload = prepareFrame;
</script>

