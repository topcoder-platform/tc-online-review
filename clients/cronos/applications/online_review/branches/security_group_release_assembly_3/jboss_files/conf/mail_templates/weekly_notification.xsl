<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	        xmlns:fo="http://www.w3.org/1999/XSL/Format"
		version="1.0" > 
<xsl:output method="text" indent="yes"/>
<xsl:template match="/">
<xsl:for-each select="MAILDATA">
<xsl:text>Hello </xsl:text><xsl:value-of select="CODER_HANDLE"/>,

This email details the <xsl:value-of select="PROJECT_TYPE"/> Review Board assignments for the week of <xsl:value-of select="CURRENT_DATE"/>

Application Dates: Screening <xsl:value-of select="SCREENING_DATE"/>, Review <xsl:value-of select="REVIEW_DATE"/>, Aggregation <xsl:value-of select="AGGREGATION_DATE"/>, Final Review <xsl:value-of select="FINAL_REVIEW_DATE"/>
<xsl:for-each select="PROJECT">

Component: <xsl:value-of select="PROJECT_NAME"/><xsl:text>
</xsl:text>
<xsl:for-each select="REVIEWER">
<xsl:value-of select="REVIEWER_ROLE"/>: <xsl:value-of select="REVIEWER_HANDLE"/> $<xsl:value-of select="REVIEWER_PAYMENT"/><xsl:text> 
</xsl:text>
</xsl:for-each>
<xsl:text>Product Manager: </xsl:text><xsl:value-of select="PM_HANDLE"/>
</xsl:for-each>

---------------------------- 
As always, please let us know if you will be unavailable to participate in reviews in the coming weeks. 

Thank you,  
TopCoder Software 

</xsl:for-each>
</xsl:template>
</xsl:stylesheet>
