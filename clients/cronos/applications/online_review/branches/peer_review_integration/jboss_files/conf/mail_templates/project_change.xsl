<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	        xmlns:fo="http://www.w3.org/1999/XSL/Format"
		version="1.0" > 
<xsl:output method="text" indent="yes"/>
<xsl:template match="/">
<xsl:for-each select="MAILDATA">
<xsl:text>Hello </xsl:text><xsl:value-of select="CODER_HANDLE"/>,

The <xsl:value-of select="PROJECT_NAME"/> project status has changed, please review the following information.

Project phase: <xsl:value-of select="PROJECT_PHASE"/>
Component: <xsl:value-of select="PROJECT_NAME"/>
Submissions: <xsl:value-of select="SUBMISSIONS"/><xsl:text>
</xsl:text>
<xsl:for-each select="REVIEWER">
<xsl:value-of select="REVIEWER_ROLE"/>: <xsl:value-of select="REVIEWER_HANDLE"/> $<xsl:value-of select="REVIEWER_PAYMENT"/><xsl:text> 
</xsl:text>
</xsl:for-each>
<xsl:text>Product Manager: </xsl:text><xsl:value-of select="PM_HANDLE"/>

----------------------------
Please let us know if you have any questions on the changes.

Thank you, 
TopCoder Software

</xsl:for-each>
</xsl:template>
</xsl:stylesheet>
