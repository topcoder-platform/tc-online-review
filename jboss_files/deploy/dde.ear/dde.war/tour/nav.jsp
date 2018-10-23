<%
    String node = request.getParameter("node") == null ? "" : request.getParameter("node");
%>

<div align="center">
    <div style="position: relative; width: 600px; height: 3px; margin: 20px 0px 40px 0px;">
        <a href="/tcs?module=Static&amp;d1=tour&amp;d2=page1" class="marker" onfocus="this.blur();" style="left: -12px;"><img src="/i/tour/<% if (node.equals("page1")) {%>markerOn<% } else { %>marker<% } %>.png" alt="" onmouseover="postPopUpText('We build software.');popUp(this,'myPopUp')" onmouseout="popHide()" /></a>
        <a href="/tcs?module=Static&amp;d1=tour&amp;d2=page2" class="marker" onfocus="this.blur();" style="left: 48px;"><img src="/i/tour/<% if (node.equals("page2")) {%>markerOn<% } else { %>marker<% } %>.png" alt="" onmouseover="postPopUpText('Our process makes us different.');popUp(this,'myPopUp')" onmouseout="popHide()" /></a>
        <a href="/tcs?module=Static&amp;d1=tour&amp;d2=page3" class="marker" onfocus="this.blur();" style="left: 108px;"><img src="/i/tour/<% if (node.equals("page3")) {%>markerOn<% } else { %>marker<% } %>.png" alt="" onmouseover="postPopUpText('Engineering a better process.');popUp(this,'myPopUp')" onmouseout="popHide()" /></a>
        <a href="/tcs?module=Static&amp;d1=tour&amp;d2=page4" class="marker" onfocus="this.blur();" style="left: 168px;"><img src="/i/tour/<% if (node.equals("page4")) {%>markerOn<% } else { %>marker<% } %>.png" alt="" onmouseover="postPopUpText('Component-based Development');popUp(this,'myPopUp')" onmouseout="popHide()" /></a>
        <a href="/tcs?module=Static&amp;d1=tour&amp;d2=page5" class="marker" onfocus="this.blur();" style="left: 228px;"><img src="/i/tour/<% if (node.equals("page5")) {%>markerOn<% } else { %>marker<% } %>.png" alt="" onmouseover="postPopUpText('Let the world compete on your next application.');popUp(this,'myPopUp')" onmouseout="popHide()" /></a>
        <a href="/tcs?module=Static&amp;d1=tour&amp;d2=page6" class="marker" onfocus="this.blur();" style="left: 288px;"><img src="/i/tour/<% if (node.equals("page6")) {%>markerOn<% } else { %>marker<% } %>.png" alt="" onmouseover="postPopUpText('Design &amp; Development Competitions');popUp(this,'myPopUp')" onmouseout="popHide()" /></a>
        <a href="/tcs?module=Static&amp;d1=tour&amp;d2=page7" class="marker" onfocus="this.blur();" style="left: 348px;"><img src="/i/tour/<% if (node.equals("page7")) {%>markerOn<% } else { %>marker<% } %>.png" alt="" onmouseover="postPopUpText('Assembly &amp; Testing Competitions');popUp(this,'myPopUp')" onmouseout="popHide()" /></a>
        <a href="/tcs?module=Static&amp;d1=tour&amp;d2=page8" class="marker" onfocus="this.blur();" style="left: 408px;"><img src="/i/tour/<% if (node.equals("page8")) {%>markerOn<% } else { %>marker<% } %>.png" alt="" onmouseover="postPopUpText('The new drawing board.');popUp(this,'myPopUp')" onmouseout="popHide()" /></a>
        <a href="/tcs?module=Static&amp;d1=tour&amp;d2=page9" class="marker" onfocus="this.blur();" style="left: 468px;"><img src="/i/tour/<% if (node.equals("page9")) {%>markerOn<% } else { %>marker<% } %>.png" alt="" onmouseover="postPopUpText('Summary');popUp(this,'myPopUp')" onmouseout="popHide()" /></a>
        <a href="/tcs?module=Static&amp;d1=tour&amp;d2=page10" class="marker" onfocus="this.blur();" style="left: 528px;"><img src="/i/tour/<% if (node.equals("page10")) {%>markerOn<% } else { %>marker<% } %>.png" alt="" onmouseover="postPopUpText('What does this mean for you?');popUp(this,'myPopUp')" onmouseout="popHide()" /></a>
        <a href="/tcs?module=Static&amp;d1=tour&amp;d2=page11" class="marker" onfocus="this.blur();" style="left: 588px;"><img src="/i/tour/<% if (node.equals("page11")) {%>markerOn<% } else { %>marker<% } %>.png" alt="" onmouseover="postPopUpText('Any questions?');popUp(this,'myPopUp')" onmouseout="popHide()" /></a>

        <% if (node.equals("page1")) {%>
        <div class="markerText" style="top: 16px; left: -40px;">Page <strong>1</strong> of <strong>11</strong></div>
        <% } else if (node.equals("page2")) {%>
        <div class="markerText" style="top: 16px; left: 20px;">Page <strong>2</strong> of <strong>11</strong></div>
        <% } else if (node.equals("page3")) {%>
        <div class="markerText" style="top: 16px; left: 80px;">Page <strong>3</strong> of <strong>11</strong></div>
        <% } else if (node.equals("page4")) {%>
        <div class="markerText" style="top: 16px; left: 140px;">Page <strong>4</strong> of <strong>11</strong></div>
        <% } else if (node.equals("page5")) {%>
        <div class="markerText" style="top: 16px; left: 200px;">Page <strong>5</strong> of <strong>11</strong></div>
        <% } else if (node.equals("page6")) {%>
        <div class="markerText" style="top: 16px; left: 260px;">Page <strong>6</strong> of <strong>11</strong></div>
        <% } else if (node.equals("page7")) {%>
        <div class="markerText" style="top: 16px; left: 320px;">Page <strong>7</strong> of <strong>11</strong></div>
        <% } else if (node.equals("page8")) {%>
        <div class="markerText" style="top: 16px; left: 380px;">Page <strong>8</strong> of <strong>11</strong></div>
        <% } else if (node.equals("page9")) {%>
        <div class="markerText" style="top: 16px; left: 440px;">Page <strong>9</strong> of <strong>11</strong></div>
        <% } else if (node.equals("page10")) {%>
        <div class="markerText" style="top: 16px; left: 500px;">Page <strong>10</strong> of <strong>11</strong></div>
        <% } else if (node.equals("page11")) {%>
        <div class="markerText" style="top: 16px; left: 560px;">Page <strong>11</strong> of <strong>11</strong></div>
        <% } %>
    </div>
</div>
