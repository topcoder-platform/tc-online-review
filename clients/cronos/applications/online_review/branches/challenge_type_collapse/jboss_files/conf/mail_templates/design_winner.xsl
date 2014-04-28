<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	        xmlns:fo="http://www.w3.org/1999/XSL/Format"
		version="1.0" > 
<xsl:output method="text" indent="yes"/>
<xsl:template match="/">
<xsl:for-each select="MAILDATA">

<xsl:value-of select="CODER_HANDLE"/>,
Congratulations!!  The results are in from the design review of the <xsl:value-of select="PROJECT_NAME"/> Design.  Your design received a score of <xsl:value-of select="SCORE"/> points which put you in first place.  An email will be sent to the TopCoder member base notifying them that you won this design project.

During the development phase you are responsible for monitoring the Forum and providing answers to design related questions that developers may have.  In addition, before you receive payment, you must have the appropriate tax form on file with TopCoder.  See the following for details: http://www.topcoder.com/?t=support&#38;c=form_faq  
 
The required enhancements can be found in Project Submit &#38; Review by logging in to http://software.topcoder.com/review and clicking on this component under "View My Open Projects".  From the project summary page, click on the Aggregation Worksheet button.  Required enhancements are due by <xsl:value-of select="DEADLINE"/> at 6PM.  You can upload your final submission via the project summary page as well.  If you cannot meet this deadline, please notify me immediately.  If you have any questions, please feel free to send them to me.
 
Please note that as the winning designer, you are not eligible to submit a development solution for this component.  I would like to thank you for participating and hope you will continue to submit design solutions for future projects.  If you have any ideas on enhancing this process, please let us know.

Thanks and congratulations,
TopCoder Software Service

</xsl:for-each>
</xsl:template>
</xsl:stylesheet>
