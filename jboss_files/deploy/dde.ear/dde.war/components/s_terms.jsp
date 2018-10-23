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
    String page_name = "s_terms.jsp";
    String action = request.getParameter("a");
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
        "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
    <title>TopCoder Software</title>
    <link rel="stylesheet" type="text/css" href="/includes/tcs_style.css" />
    <script language="JavaScript" type="text/javascript" src="/scripts/javascript.js"></script>
    <jsp:include page="/includes/header-files.jsp" />
</head>

<body class="body">

<!-- Header begins -->
<jsp:include page="/includes/top.jsp">
<jsp:param name="TCDlevel" value="software" />
</jsp:include>
<jsp:include page="/includes/menu.jsp" >
    <jsp:param name="isSoftwarePage" value="true"/>
</jsp:include>
<!-- Header ends -->

<!-- breadcrumb begins -->
<table width="100%" cellpadding="0" cellspacing="0" border="0">
    <tr><td width="100%" height="2" bgcolor="#000000"><img src="/images/clear.gif" alt="" width="10" height="2" border="0" /></td></tr>
</table>
<!-- breadcrumb ends -->

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
        <td width="25"><img src="/images/clear.gif" alt="" width="25" height="10" border="0" /></td>
<!-- Gutter 1 ends -->

<!-- Middle Column begins -->
        <td width="100%">
            <table width="100%" border="0" cellpadding="0" cellspacing="0">
                <tr><td height="15"><img src="/images/clear.gif" alt="" width="10" height="15" border="0" /></td></tr>
                <tr><td class="normal"><img src="/images/hd_terms.png" alt="Terms &amp; Conditions" border="0" /></td></tr>
                <tr><td><h3>Acceptance of Terms and Revisions</h3></td></tr>
                <tr><td class="normal">
                        <p>&nbsp;</p>

                        <p>By using this Web site you are indicating your agreement to these Terms of Use ("Terms"). If you do not agree to these Terms,
                        please do not use the site and exit now.</p>

                        <p>This site is controlled and operated by TopCoder Software, Inc. from its offices within the United States of America.  We make
                        no representation that materials in the site are appropriate or available for use in other locations. Those who choose to access this
                        site from other locations do so on their own initiative and are responsible for compliance with local laws, if and to the extent local
                        laws are applicable. Software from this site is subject to U.S. export controls.  No software from this site may be downloaded or
                        otherwise exported or reexported into (or to a national or resident of) Cuba, Libya, North Korea, Iran, Syria or any other
                        country to which the U.S. has embargoed goods or to anyone on the U.S. Treasury Department's list of Specially Designated National
                        or the U.S. Commerce Department's Table of Deny Orders.  By downloading or using the software, you represent and warrant that
                        you are not located in, under the control of, or a national or resident of any such country or on any such list.</p>

                        <p>We may revise these Terms at any time without prior notice by updating this page and such revisions will be effective upon
                        posting to this page. Please check this page periodically for any changes. Your continued use of this Web site following the
                        posting of any revisions to these Terms will mean you accept those changes. We reserve the right to alter, suspend or
                        discontinue any aspect of software.topcoder.com, including your access to it. Unless explicitly stated, any new features will
                        be subject to these terms and conditions.</p>

                        <h3>Privacy</h3>
                        <a href="../components/s_privacy.jsp">Click here</a> to see our complete privacy policy and statement.</p>

                        <h3>Copyright, Trademark And Other Intellectual Property</h3>

                        <p><strong>Protection</strong><br />
                        Except as otherwise indicated, this Web site and its entire contents (including, but not limited to, the text, information, software,
                        graphics, images, sound, and animation) are owned by us and are protected by domestic and international copyright, trademark,
                        patent, and other intellectual property laws. All copyrightable text and graphics, the selection, arrangement, and presentation of
                        all materials (including information in the public domain), and the overall design of this Web site are &#169;2002 TopCoder
                        Software. All rights reserved. Permission is granted to download and print materials from this Web site for the sole purposes of
                        viewing, reading, and retaining for reference. Any other copying, distribution, retransmission, or modification of information or
                        materials on this Web site, whether in electronic or other form, without our express prior written permission is strictly
                        prohibited.</p>
						<br />
                        <p><strong>Notice</strong><br />
                        All trademarks, service marks, and trade names are proprietary to us or other respective owners that have granted us the right
                        and license to use their marks.</p>
						<br />
                        <p><strong>Copyright Complaints</strong><br />
                            We respect the intellectual property of others, and we ask you to do the same. We may, in appropriate circumstances and at
                            our sole discretion, terminate the access of users who infringe the copyright rights of others.</p>

                        <p>If you believe that your work has been copied and is accessible at our Web site in a way that constitutes copyright
                        infringement, or that our Web site contains links or other references to another online location that contains material or
                        activity that infringes your copyright rights, you may notify us by providing our copyright agent the information required
                        by the U.S. Online Copyright Infringement Liability Limitation Act of the U.S. Digital Millennium Copyright Act, 17 U.S.C. ?512
                        Our agent for notice of claims of copyright infringement on or regarding this Web site can be reached as follows:</p>

                        <p>By email: <a href="mailto:service@topcodersoftware.com">service@topcodersoftware.com</a><br />
                        By mail: Tanya Horgan<br />
                        TopCoder Software, Inc.<br />
                        95 Glastonbury Blvd,<br />
                        Glastonbury, CT 06033<br />
                        BY PHONE: 860.633.5540</p>

                        <p>Repeat infringers will be blocked from accessing the TopCoder Software web site.</p>
						<br />
                        <p><strong>Indemnification and Release</strong><br />
                        By accessing our Web site, you agree to indemnify us and any parent, subsidiary or affiliated entities, our officers and
                        employees, and officers and employees of any parent, subsidiary or affiliated entities and hold them harmless from any and
                        all claims and expenses, including attorney's fees, arising from your use of our Web site including any material (including
                        third party material) that you post on our Web site and any services or products available through our Web site. In addition,
                        you hereby release us and any parent, subsidiary or affiliated entities, our officers and employees, and officers and employees
                        of any parent, subsidiary or affiliated entities from any and all claims, demands, debts, obligations, damages (actual or
                        consequential), costs, and expenses of any kind or nature whatsoever, whether known or unknown, suspected or unsuspected,
                        disclosed or undisclosed, that you may have against them arising out of or in any way related to such disputes and/or to any
                        services or products available at our Web site. You hereby agree to waive all laws that may limit the efficacy of such releases.</p>
						<br />
                        <p><strong>Disclaimer</strong><br />
                        The materials and services on our web site are provided "as is" and without warranties of any kind either
                        express or implied. To the fullest extent permissible pursuant to applicable law, we disclaim all
                        warranties, express or implied, including, but not limited to, implied warranties of merchantability and
                        fitness for a particular purpose. We do not warrant that the functions contained in the materials will be
                        uninterrupted or error-free, that defects will be corrected, or that this web site or the server(s) that
                        makes our web site available or any advertised or hyperlinked site are free of viruses or other harmful
                        components or that our site, server(s), advertised or hyperlinked sites will be accessible at all times.
                        We do not warrant that such errors, defects or interruptions in service will not affect the results of
                        our competitions, and we disclaim any responsibility for reduced performance in competitions due to such
                        problems. We do not warrant or make any representations regarding the use or the results of our web site
                        with respect to correctness, accuracy, reliability or otherwise. You assume the entire cost of all necessary
                        servicing, repair or correction. To the extent that applicable law may not allow the exclusion of implied
                        warranties, the above exclusions may not apply to you.</p>

                        <p>Documents, graphics and other materials appearing at our Web site may include technical inaccuracies, typographical errors, and out-of-date information and use of such documents, graphics or other materials is at your own risk.</p>
						<br />
                        <p><strong>Limitation of Liability</strong><br />
                        To the fullest extent permissible pursuant to applicable law, topcoder software shall not be liable for any damages (including, but not limited to, direct, indirect, incidental, special or consequential damages), including, but not limited to, data or other damage to any tangible or intangible property, even if topcoder software has been advised of the possibility of such damages, resulting from (i) the use or inability to use this web site, (ii) the disclosure of, unauthorized access to or alteration of any transmission or data, (iii) the statements or conduct of any third party or (iv) any other matter relating to topcoder software.</p>
						<br />
                        <p><strong>Links to Other Web Sites and Services</strong><br />
                        To the extent that our web site contains links to other web sites and outside services and resources, we do not control the availability and content of those web sites, services or resources, nor do we necessarily review or endorse materials available at or through such other web sites. Viewing other web sites or utilizing outside services and resources is done at your own risk. We shall not be liable for any loss or damage caused or alleged to be caused by or in connection with use of or reliance on any such content, goods or services available on or through any such site or resource.</p>
						<br />
                        <p><strong>Materials Posted By Visitors</strong><br />
                        You irrevocably and unconditionally agree to transfer and assign to topcoder software all right, title and interest you have, may have or acquire in or to any material posted, uploaded or otherwise sent to our web site by you.</p>

                        <p>You waive any and all moral rights, including, without limitation, any rights arising under Chapter 7 of the Copyright and Related Rights Act 2000 applicable to European Union residents, and all rights of a similar nature in any jurisdiction in any material, including source code, which you post, upload or otherwise send to TopCoder Software or it's website, such waivers being in favor of TopCoder Software.</p>

                        <p>You are allowed to register only once and you must provide true and accurate registration information. You are prohibited from misrepresenting your registration information or tampering with the registration process.</p>

                        <p>You are also prohibited from posting or otherwise uploading to our Web site: any material that infringes on any copyright, trademark or other proprietary rights of another (including publicity and privacy rights); material that is obscene, offensive, libelous, pornographic, threatening, abusive, contains illegal content, or is otherwise objectionable, that would constitute or encourage a criminal offense, or that would otherwise give rise to liability or violates any law. You also represent that you have all necessary rights to use any material that you post or otherwise upload to our Web site.</p>

                        <p>You are further forbidden from distributing or otherwise publishing any material on our Web site that contains any solicitation of funds, promotion, employment, advertising, or solicitation for goods or services; sending unsolicited commercial e-mail and other advertising, promotional materials or other forms of solicitation to other users of this site; harvesting names and e-mail addresses from other users of this site without their permission; soliciting passwords from other users; impersonating other users; or sending viruses or other destructive or expropriating content.</p>

                        <p>While we do not routinely review the contents of material posted or uploaded to our Web site, we nevertheless reserve the right to remove any postings or other uploaded materials in response to complaints of infringement, obscenity or defamation or to otherwise review or edit such materials as appropriate, in our sole discretion and without notice.</p>
						<br />
                        <p><strong>Other Restrictions On Conduct</strong><br />
                        You agree not to disrupt, modify or interfere with the functioning of our Web site or any services provided on or through our Web site or with any associated software, hardware or servers in any way and you agree not to impede or interfere with others' use of our Web site. You also agree not to alter or tamper with any information or materials on, or associated with our Web site or services provided on or through our Web site.</p>

                        <p>We do not necessarily endorse, support, sanction, encourage, verify or agree with the comments, opinions, or other statements made public at our Web site by users through our Collaborative Discussion groups and/or newsgroups, or other interactive services available at our Web site. Any information or material sent by users to such forums, including advice and opinions, represents the views and is the responsibility of those users and does not necessarily represent our views.</p>

                        <p>You agree that no impediment exists to you using the TopCoder Software website, and your participation in TopCoder Software's website will not interfere with your performance of any other agreement or obligation which has been or will be made with any third party.</p>
						<br />
                        <p><strong>Choice of Law and Forum</strong><br />
                        This agreement is governed by the laws of the Commonwealth of Massachusetts. You hereby agree to submit to the exclusive jurisdiction of the courts of the Commonwealth of Massachusetts. To the extent that applicable laws have mandatory application to this agreement or give you the right to bring action in any other courts, the above limitations may not apply to you. Use of this Web site is unauthorized in any jurisdiction that does not give full effect to all provisions of these Terms.</p>
						<br />
                        <p><strong>Severability and Enforceability</strong><br />
                        If any provision or portion of these Terms is held illegal, invalid, or unenforceable, in whole or in part, it shall be modified to the minimum extent necessary to correct any deficiencies or replaced with a provision which is as close as is legally permissible to the provision found invalid or unenforceable and shall not affect the legality, validity or enforceability of any other provisions or portions of these Terms.</p>
						<br />
                        <p><strong>Termination/Exclusion</strong><br />
                        We reserve the right, in our sole discretion, to revoke any and all privileges associated with accessing and/or using our web site, and to take any other action we deem appropriate including but not limited to terminating or suspending your use of software.topcoder.com, for no reason or any reason whatsoever, including improper use of this site or failure to comply with these Terms.</p>
						<br />
                        <p><strong>General</strong><br />
                        We may assign, novate or subcontract any or all of our rights and obligations under these terms and conditions at any time.</p>

                        <p>If you have any questions regarding these Terms, contact us at <a href="mailto:service@topcodersoftware.com">service@topcodersoftware.com</a>.</p>
                    </td>
                </tr>
<!-- Use the following two lines only when user has clicked on Terms & Conditions link on Register New Account page -->
                <tr><td height="20"><img src="/images/clear.gif" alt="" width="10" height="20" border="0" /></td></tr>
                <tr><td class="normalCenter"><strong><a href="javascript:history.back()">&lt; Go back</a></strong></td></tr>

                <tr><td height="40"><img src="/images/clear.gif" alt="" width="10" height="40" border="0" /></td></tr>
            </table>
        </td>

<!-- Middle Column ends -->

<!-- Gutter 3 begins -->
        <td width="10"><img src="/images/clear.gif" alt="" width="10" height="10" border="0" /></td>
<!-- Gutter 3 ends -->
    </tr>
</table>

<!-- Footer begins -->
<jsp:include page="/includes/foot.jsp" flush="true" />
<!-- Footer ends -->

</body>
</html>
