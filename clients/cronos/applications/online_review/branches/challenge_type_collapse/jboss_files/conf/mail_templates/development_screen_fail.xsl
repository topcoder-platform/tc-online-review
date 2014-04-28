<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	        xmlns:fo="http://www.w3.org/1999/XSL/Format"
		version="1.0" > 
<xsl:output method="text" indent="yes"/>
<xsl:template match="/">
<xsl:for-each select="MAILDATA">
The results are in from the Development Review Board for the <xsl:value-of select="PROJECT_NAME"/> project.  Your submission did not pass screening and will not pass through to the review phase.  You may view your screening scorecard in the Submit &#38; Review application.  If you have any questions, please feel free to send them to me.  In addition, I would like to thank you for participating and hope you will continue to submit solutions for future projects.

Any feedback regarding the project or the process can be directed to service@topcodersoftware.com.

Thanks,
TopCoder Software Service

</xsl:for-each>
</xsl:template>
</xsl:stylesheet>
