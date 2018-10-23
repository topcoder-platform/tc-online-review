<%@ page import="javax.naming.*" %>
<%@ page import="javax.ejb.CreateException" %>
<%@ page import="java.io.*" %>
<%@ page import="java.rmi.*" %>
<%@ page import="javax.rmi.*" %>
<%@ page import="java.util.*" %>
<%@ page import="java.sql.*" %>
<%@ page import="java.lang.reflect.*" %>

<%@ include file="/includes/util.jsp" %>
<%@ include file="/includes/session.jsp" %>
<%@ include file="/includes/formclasses.jsp" %>

<%
    // STANDARD PAGE VARIABLES
    String page_name = "registration.jsp";
    String action = request.getParameter("a");
%>

<% // PAGE SPECIFIC DECLARATIONS %>
<%@ page import="com.topcoder.dde.catalog.*" %>
<%@ page import="com.topcoder.util.idgenerator.IDGeneratorFactory"%>
<%@ page import="com.topcoder.dde.user.DuplicateUsernameException"%>
<%@ page import="com.topcoder.dde.user.User"%>
<%@ page import="com.topcoder.util.idgenerator.IDGenerationException"%>
<%

    Object objTechTypes = CONTEXT.lookup(CatalogHome.EJB_REF_NAME);

    // COMPANY SIZE LIST
    // COUNTRY CODE LIST
    Fields fields = new Fields();


    // TEXT FIELDS
    fields.add("username", new Field("Username", "uname", "", true));
    fields.add("password", new Field("Password", "pwd",  "", true));
    fields.add("confirmpassword", new Field("Confirm Password", "pwd2",  "", true));
    fields.add("email", new Field("Email Address", "email",  "", true));
    fields.add("confirmemail", new Field("Confirm Email", "email2",  "", true));
    fields.add("firstname", new Field("First name", "fname",  "", true));
    fields.add("lastname", new Field("Last name", "lname",  "", true));
    fields.add("companyname", new Field("Company name", "cname",  "", true));
    fields.add("address", new Field("Address", "addr",  "", true));
    fields.add("city", new Field("City", "city",  "", true));
    fields.add("postalcode", new Field("Postal code", "pcode",  "", true));
    fields.add("phonecountry", new Field("Country code", "phone_c",  "", false));
    fields.add("phonearea", new Field("Area code", "phone_a",  "", true));
    fields.add("phonenumber", new Field("Number", "phone_n",  "", true));
    fields.add("terms", new Field("Terms", "terms",  "", true));
    fields.add("state", new Field("State", "state",  "", true));

    // LOOKUP FIELDS
    long country_code = 0;
    long company_size = 0;
    boolean duplicateUsername = false;

    // RADIO FIELDS
    boolean useComponents = false;
    boolean useConsultants = false;
    boolean receiveNews = false;
    boolean newsInHTML = false;



    String strState = request.getParameter("state");
    if (strState == null) {
        strState = "";
    }

    boolean doRegister = true;
    if (action != null) {
        debug.addMsg("registration", "action occurred: " + action);

        if (action.equalsIgnoreCase("register")) {

            // ----- HANDLE TEXT FIELDS

            // GET FIELDS FROM REQUEST AND DO BASIC VALIDATION
            Enumeration enum = fields.elements();
            while (enum.hasMoreElements()) {
                Field f = (Field)enum.nextElement();
                f.getFromRequest(request, "");
                f.doBasicValidation();
                if (f.hasError()) {
                    doRegister = false;
                    debug.addMsg("registration", "error for field " + f.getLabel());
                }
            }

            // CUSTOM VALIDATION
            if (fields.get("password").getValue().trim().length() < 7 || fields.get("password").getValue().trim().length() > 15) {
                fields.get("password").setError("Password must be between 7 and 15 characters.");
                doRegister = false;
                debug.addMsg("registration", "error for field " + fields.get("password").getLabel());
            }
            if (!fields.get("password").getValue().equals(fields.get("confirmpassword").getValue())) {
                fields.get("password").setError("Password fields do not match.");
                doRegister = false;
                debug.addMsg("registration", "error for field " + fields.get("password").getLabel());
            }
            if (!fields.get("email").getValue().equals(fields.get("confirmemail").getValue())) {
                fields.get("email").setError("Email address fields do not match.");
                doRegister = false;
                debug.addMsg("registration", "error for field " + fields.get("email").getLabel());
            }

            // ------ HANDLE LOOKUP FIELDS
            // COUNTRY CODE
            String strCountryCode = request.getParameter("country");
            try {
                country_code = Long.parseLong(strCountryCode);
            } catch (NumberFormatException nfe) {
                country_code = -1;
                doRegister = false;
                debug.addMsg("registration", "error for field country");
            }

            // COMPANY SIZE
            company_size = 1;
/*
            String strCompanySize = request.getParameter("company_size");
            try {
                company_size = Long.parseLong(strCompanySize);
            } catch (NumberFormatException nfe) {
                company_size = -1;
                doRegister = false;
                debug.addMsg("registration", "error for field company_size");
            }
*/

            // ----- HANDLE RADIO FIELDS

            // CONSULTANTS
            //String strUseConsultants = request.getParameter("useConsultants");
            //useConsultants = strUseConsultants.equalsIgnoreCase("yes");

            // COMPONENTS
            //String strUseComponents = request.getParameter("useComponents");
            //useComponents = strUseComponents.equalsIgnoreCase("yes");

            // RECEIVE NEWS
            //String strReceiveNews = request.getParameter("receiveNews");
            //receiveNews = strReceiveNews.equalsIgnoreCase("yes");

            // NEWS FORMAT
            //String strNewsInHTML = request.getParameter("newsInHTML");
            //newsInHTML = strNewsInHTML.equalsIgnoreCase("yes");

            String address2 = request.getParameter("address2");
            String stateCode = request.getParameter("state");

            if (fields.get("terms").hasError()) {
                fields.get("terms").setError("To be a registered user, you must agree to our Terms of Use and Privacy Policy.");
                doRegister = false;
            }

            // ----- HANDLE REGISTRATION

            debug.addMsg("registration", "check for errors");
            if (doRegister) {
                debug.addMsg("registration", "no errors, go ahead with registration");

                RegistrationInfo ri = new RegistrationInfo();

                ri.setUsername(fields.get("username").getValue());
                    debug.addMsg("registration", "set username successfully");
                ri.setPassword(fields.get("password").getValue());
                    debug.addMsg("registration", "set password successfully");
                ri.setEmail(fields.get("email").getValue());
                    debug.addMsg("registration", "set email successfully");
                ri.setFirstName(fields.get("firstname").getValue());
                    debug.addMsg("registration", "set firstname successfully");
                ri.setLastName(fields.get("lastname").getValue());
                    debug.addMsg("registration", "set lastname successfully");
                ri.setCompany(fields.get("companyname").getValue());
                    debug.addMsg("registration", "set companyname successfully");
                ri.setAddress(fields.get("address").getValue());
                    debug.addMsg("registration", "set address successfully");
                ri.setCity(fields.get("city").getValue());
                    debug.addMsg("registration", "set city successfully");
                ri.setCountryCode(country_code);
                    debug.addMsg("registration", "set country_code successfully");
                ri.setPostalcode(fields.get("postalcode").getValue());
                    debug.addMsg("registration", "set postalcode successfully");
                ri.setPhoneCountry(fields.get("phonecountry").getValue());
                    debug.addMsg("registration", "set phonecountry successfully");
                ri.setPhoneArea(fields.get("phonearea").getValue());
                    debug.addMsg("registration", "set phonearea successfully");
                ri.setPhoneNumber(fields.get("phonenumber").getValue());
                    debug.addMsg("registration", "set phonenumber successfully");
                //ri.setUseComponents(useComponents);
                    debug.addMsg("registration", "set useComponents successfully");
                //ri.setUseConsultants(useConsultants);
                    debug.addMsg("registration", "set useConsultants successfully");
                //ri.setState(fields.get("state").getValue());
                    debug.addMsg("registration", "set state successfully");
               ri.setTechnologies(new Vector());
                    debug.addMsg("registration", "set vecTechnologies successfully");
               ri.setState(stateCode);
                    debug.addMsg("state", "set vecTechnologies successfully");
               ri.setAddress2(address2);
                    debug.addMsg("address2", "set vecTechnologies successfully");

                //ri.setReceiveNews(receiveNews);
                    debug.addMsg("registration", "set receiveNews successfully");
                //ri.setNewsInHtml(newsInHTML);
                    debug.addMsg("registration", "set newsInHTML successfully to " + newsInHTML);
                //ri.setCompanySize(company_size);
                    debug.addMsg("registration", "set company_size successfully to " + company_size);
                //PricingTier pt = new PricingTier(1, 5.0);
                //ri.setPricingTier(pt);
                debug.addMsg("registration", "set pricing tier successfully to 1 (hardcoded)");
                debug.addMsg("registration", "registering user");

                try {

                    long userId = IDGeneratorFactory.getIDGenerator("main_sequence").getNextID();
                    long emailId = IDGeneratorFactory.getIDGenerator("EMAIL_SEQ").getNextID();
                    long phoneId = IDGeneratorFactory.getIDGenerator("PHONE_SEQ").getNextID();
                    long addressId = IDGeneratorFactory.getIDGenerator("ADDRESS_SEQ").getNextID();
                    long companyId = IDGeneratorFactory.getIDGenerator("COMPANY_SEQ").getNextID();

                    User user = USER_MANAGER.register(ri, userId, emailId, phoneId, addressId, companyId);
                    debug.addMsg("registration", "registered user");

                    // REDIRECT SOMEWHERE if session requires it
                    response.sendRedirect("/registration/register_confirm.jsp");
                    return;

                } catch (DuplicateUsernameException due) {
                    debug.addMsg("registration", "duplicate username: " + fields.get("username").getValue());
                    fields.get("username").setError("Username already exists.  Please select another.");
                }
            }
        }
    } else {
        debug.addMsg("registration", "no action occurred");
    }
%>

<html>
<head>
    <title>Register a New Account at TopCoder Software</title>

<link rel="stylesheet" type="text/css" href="/includes/tcs_style.css" />

<script language="JavaScript" type="text/javascript" src="/scripts/javascript.js">
</script>

<script language="JavaScript" type="text/javascript">
<!-- Hide script from old browsers
// TabNext()
// This function is designed to auto-tab the phone fields

function tabnext(obj,len,next_field) {
  if (obj.value.length >= len) {
    if (obj.value.length > len) {
      obj.value = obj.value.slice(0,len);
    }
    next_field.focus();
  }
}

// End hiding script from old browsers -->
</script>

<%--
var phone_field_length=0;
function TabNext(obj,event,len,next_field) {
    if (event == "down") {
        phone_field_length=obj.value.length;
        }
    else if (event == "up") {
        if (obj.value.length != phone_field_length) {
            phone_field_length=obj.value.length;
            if (phone_field_length == len) {
                next_field.focus();
                }
            }
        }
    }
--%>

</head>

<body class="body" marginheight="0" marginwidth="0">

<!-- Header begins -->
<jsp:include page="/includes/top.jsp">
    <jsp:param name="page_name" value="registration.jsp"/>
</jsp:include>
<jsp:include page="/includes/menu.jsp" >
    <jsp:param name="isSoftwarePage" value="true"/>
</jsp:include>
<!-- Header ends -->

<table width="100%" border="0" cellpadding="0" cellspacing="0" class="middle">
    <tr valign="top">

<!-- Left Column begins -->
        <td width="165" class="leftColumn">
        <jsp:include page="/includes/left.jsp" >
            <jsp:param name="level1" value="index"/>
        </jsp:include>
        </td>
<!-- Left Column ends -->

<!-- Gutter 1 begins -->
        <td width="15"><img src="/images/clear.gif" alt="" width="15" height="10" border="0" /></td>
<!-- Gutter 1 ends -->

<!-- Middle Column begins -->
        <td width="99%" align="center">
            <table border="0" cellpadding="0" cellspacing="0" width="545">
                <tr valign="top"><form name="frmRegister" action="<%= page_name %>" method="post">
                    <td colspan="3">
                        <table border="0" cellpadding="0" cellspacing="0">
                            <tr>
                                <td class="normal">
                                    <img src="/images/clear.gif" alt="" width="10" height="15" border="0" /><br/>
                                    <img src="/images/headRegisterSmall.gif" alt="Register New Account" width="265" height="35" border="0"><br/>
                                    <img src="/images/clear.gif" alt="" width="10" height="15" border="0" />
                                </td>
                            </tr>
                        </table>
                        <img src="/images/clear.gif" alt="" width="10" height="15" border="0" />
                    </td>
                </tr>

                <tr valign="top">
                <input type="hidden" name="a" value="register">

<!-- Begin Contact Info Column -->
                    <td width="266">
                        <table border="0" cellpadding="0" cellspacing="0">
                            <tr valign="top">
                                <td class="registerTitle"><a name="c">Contact Information</a></td>
                            </tr>
                        </table>

                        <img src="/images/clear.gif" alt="" width="10" height="5" border="0" />

                        <div align="center">
                        <table border="0" cellpadding="3" cellspacing="0" width="100">

<!-- First Name Error Text -->
                            <tr valign="middle">
                                <td class="errorText"><%= fields.get("firstname").getError() %></td>
                            </tr>

<!-- First Name -->
                            <tr valign="middle">
                                <td class="registerLabel">First Name <span class="registerLabelRequired">(required)</span><br/>
                                    <input class="registerElement" type="text" name="<%= fields.get("firstname").getName() %>" value ="<%= fields.get("firstname").getValue() %>" size="30" maxlength="30"></td>
                            </tr>

<!-- Last Name Error Text -->
                            <tr valign="middle">
                                <td class="errorText"><%= fields.get("lastname").getError() %></td>
                            </tr>

<!-- Last Name -->
                            <tr valign="middle">
                                <td class="registerLabel">Last Name <span class="registerLabelRequired">(required)</span><br/>
                                    <input class="registerElement" type="text" name="<%= fields.get("lastname").getName() %>" value ="<%= fields.get("lastname").getValue() %>" size="30" maxlength="30"></td>
                            </tr>

<!-- Company Error Text -->
                            <tr valign="middle">
                                <td class="errorText"><%= fields.get("companyname").getError() %></td>
                            </tr>

<!-- Company -->
                            <tr valign="middle">
                                <td class="registerLabel">Company <span class="registerLabelRequired">(required)</span><br/>
                                    <input class="registerElement" type="text" name="<%= fields.get("companyname").getName() %>" value ="<%= fields.get("companyname").getValue() %>" size="30" maxlength="30"></td>
                            </tr>

<!-- Address Error Text -->
                            <tr valign="middle">
                                <td class="errorText"><%= fields.get("address").getError() %></td>
                            </tr>

<!-- Address 1 -->
                            <tr valign="middle">
                                <td class="registerLabel">Address <span class="registerLabelRequired">(required)</span><br/>
                                    <input class="registerElement" type="text" name="<%= fields.get("address").getName() %>" value ="<%= fields.get("address").getValue() %>" size="30" maxlength="30"></td>
                            </tr>

<!-- Address 2 -->
                            <tr valign="middle">
                                <td class="registerLabel"><input class="registerElement" type="text" name="address2" value ="" size="30" maxlength="50"></td>
                            </tr>

<!-- City Error Text -->
                            <tr valign="middle">
                                <td class="errorText"><%= fields.get("city").getError() %></td>
                            </tr>

<!-- City -->
                            <tr valign="middle">
                                <td class="registerLabel">City <span class="registerLabelRequired">(required)</span><br/>
                                    <input class="registerElement" type="text" name="<%= fields.get("city").getName() %>" value ="<%= fields.get("city").getValue() %>" size="30" maxlength="30"></td>
                            </tr>

<!-- State Error Text -->
                            <tr valign="middle">
                                <td class="errorText"><%= fields.get("state").getError() %></td>
                            </tr>

<!-- State -->
                            <tr valign="middle">
                                <td class="registerLabel">State <span class="registerLabelRequired">(required)</span><br/>
                                    <%@ include file="/includes/stateList.jsp" %></td>
                            </tr>

<!-- Postal Code Error Text -->
                            <tr valign="middle">
                                <td class="errorText"><%= fields.get("postalcode").getError() %></td>
                            </tr>

<!-- Postal Code -->
                            <tr valign="middle">
                                <td class="registerLabel">Zip / Postal Code <span class="registerLabelRequired">(required)</span><br/>
                                    <input class="registerElement" type="text" name="<%= fields.get("postalcode").getName() %>" value ="<%= fields.get("postalcode").getValue() %>" size="10" maxlength="10"></td>
                            </tr>

<!-- Country Error Text -->
                            <tr valign="middle">
                                <td class="errorText"><%= (country_code == -1 && action != null ? " No country was chosen." : "") %></td>
                            </tr>

<!-- Country -->
                            <tr valign="middle">
                                <td class="registerLabel">Country <span class="registerLabelRequired">(required)</span><br/>
                                    <%@ include file="/includes/countryList.jsp" %></td>
                            </tr>

<!-- Telphone: Country Code Error Text -->
                            <tr valign="middle">
                            <% if (fields.get("phonecountry").getError().length() > 0) { %>
                                <td class="errorText"><%= fields.get("phonecountry").getError() %></td>
                            <% } else { %>
                                <td class="errorText"></td>
                            <% } %>
                            </tr>

<!-- Telephone Country Code-->
                            <tr valign="middle">
                                <td class="registerLabel">Telephone Country Code<br/><span class="registerLabelRequired">(international members only)</span><br/>
                                    <input class="registerElement" type="text" name="<%= fields.get("phonecountry").getName() %>" value ="<%= fields.get("phonecountry").getValue() %>" size="4" maxlength="3" onKeyUp="tabnext(this,3,this.form.phone_a);"></td>
                            </tr>

<!-- Telephone Error Text -->
                            <tr valign="middle">
                            <% if (fields.get("phonearea").getError().length() > 0 || fields.get("phonenumber").getError().length() > 0) { %>
                                <td class="errorText"><%= fields.get("phonearea").getError() %><BR><%= fields.get("phonenumber").getError() %></td>
                            <% } else { %>
                                <td class="errorText"></td>
                            <% } %>
                            </tr>

<!-- Telephone -->
                            <tr valign="middle">
                                <td class="registerLabel">Telephone Number<br/>
                                    (<input class="registerElement" type="text" name="<%=fields.get("phonearea").getName()%>" value="<%=fields.get("phonearea").getValue()%>" size="4" maxlength="3" onKeyUp="tabnext(this,3,this.form.phone_n);">) <input class="registerElement" type="text" name="<%=fields.get("phonenumber").getName()%>" value="<%=fields.get("phonenumber").getValue()%>" size="10" maxlength="8"></td>
                            </tr>
                        </table>
                        </div>
<!-- End Contact Info Column -->

                    <td width="15"><img src="/images/clear.gif" alt="" width="15" height="10" border="0" /></td>

<!-- Begin User Name Column -->
                    <td width="266">
                        <table border="0" cellpadding="0" cellspacing="0">
                            <tr valign="top">
                                <td class="registerTitle"><a name="u">User Name & Password</a></td>
                            </tr>
                        </table>

                        <img src="/images/clear.gif" alt="" width="10" height="5" border="0" />

                        <div align="center">
                        <table border="0" cellpadding="3" cellspacing="0" width="100">

<!-- User Name Error Text -->
                            <tr valign="middle">
                                <td class="errorText"><%= fields.get("username").getError() %></td>
                            </tr>

<!-- User Name -->
                            <tr valign="middle">
                                <td class="registerLabel">User Name<br/>
                                    <input class="registerElement" type="text" name="<%= fields.get("username").getName() %>" value ="<%= fields.get("username").getValue() %>" size="30" maxlength="30"></td>
                            </tr>

<!-- Password Error Text -->
                            <tr valign="middle">
                                <td class="errorText"><%= fields.get("password").getError() %></td>
                            </tr>

<!-- Password -->
                            <tr valign="middle">
                                <td class="registerLabel">Password<br/>
                                    <input class="registerElement" type="password" name="<%= fields.get("password").getName() %>" value ="<%= fields.get("password").getValue() %>" size="30" maxlength="15"><br/>
                                    <span class="registerLabelRequired">(min 7 characters, max 15)</span></td>
                            </tr>

<!-- Re-Type Password -->
                            <tr valign="middle">
                                <td class="registerLabel">Re-type Password<br/>
                                    <input class="registerElement" type="password" name="<%= fields.get("confirmpassword").getName() %>" value ="<%= fields.get("confirmpassword").getValue() %>" size="30" maxlength="15"></td>
                            </tr>

<!-- Email Error Text -->
                            <tr valign="middle">
                                <td class="errorText"><%= fields.get("email").getError() %></td>
                            </tr>

<!-- Email -->
                            <tr valign="middle">
                                <td class="registerLabel">Email<br/>
                                    <input class="registerForm" type="text" name="<%= fields.get("email").getName() %>" value ="<%= fields.get("email").getValue() %>" size="30" maxlength="100"><br/>
                                    <span class="registerLabelRequired">An email will be sent to activate your account.</span></td>
                            </tr>

<!-- Re-Type Email -->
                            <tr valign="middle">
                                <td class="registerLabel">Re-type Email<br/>
                                    <input class="registerElement" type="text" name="<%= fields.get("confirmemail").getName() %>" value ="<%= fields.get("confirmemail").getValue() %>" size="30" maxlength="100"></td>
                            </tr>

<!-- Terms & Conditions Error Text -->
                            <tr valign="middle">
                                <td class="errorText"><a name="t" /><%= fields.get("terms").getError() %></td>
                            </tr>

<!-- Terms & Conditions -->
                            <tr valign="middle">
                                <td class="registerLabel" nowrap><input type="checkbox" name="terms" value="iagree"><img src="/images/clear.gif" alt="" width="3" height="10" border="0" /><strong>I Agree to the <a class="registerLinks" href="../components/s_terms.jsp">Terms and Conditions</a></strong></td>
                            </tr>

                            <tr valign="middle">
                                <td class="registerTextCenter"><input type="reset" name="clear" value="Clear Fields"></input><img src="/images/clear.gif" alt="" width="5" height="5" border="0" /><input type="submit" name="a" value="Continue"></input></td>
                            </tr>
                        </table>
                        </div>

                    </td></form>
                </tr>
            </table>

            <p><br/></p>

        </td>

<!--Middle Column ends -->

<!-- Gutter 2 begins -->
        <td width="15"><img src="/images/clear.gif" alt="" width="15" height="10" border="0" /></td>
<!-- Gutter 2 ends -->

<!-- Right Column begins -->
        <td width="170">
            <img src="/images/clear.gif" alt="" width="170" height="10" border="0" />
        </td>
<!--Right Column ends -->

<!-- Gutter 3 begins -->
        <td width="10"><img src="/images/clear.gif" alt="" width="10" height="10" border="0"></td>
<!-- Gutter 3 ends -->
    </tr>
</table>

<!-- Footer begins -->
<jsp:include page="/includes/foot.jsp" flush="true" />
<!-- Footer ends -->

</body>
<%  if (fields.get("firstname").hasError() ||
        fields.get("lastname").hasError() ||
        fields.get("companyname").hasError() ||
        fields.get("address").hasError() ||
        fields.get("city").hasError() ||
        fields.get("postalcode").hasError() ||
        fields.get("phonecountry").hasError() ||
        fields.get("phonearea").hasError() ||
        fields.get("phonenumber").hasError()) {
%>
            <script>
                window.location = "#c";
                frmRegister.fname.focus();
            </script>
<%  } else if (fields.get("username").hasError() ||
                fields.get("password").hasError() ||
                fields.get("confirmpassword").hasError() ||
                fields.get("email").hasError() ||
                fields.get("confirmemail").hasError()) {
%>
            <script>
                window.location = "#u";
                frmRegister.uname.focus();
            </script>
<%  } else if (fields.get("terms").hasError()) { %>
            <script>
                window.location = "#t";
                frmRegister.terms.focus();
            </script>
<%  } else { %>
            <script>
                frmRegister.fname.focus();
            </script>
<%  } %>
</html>