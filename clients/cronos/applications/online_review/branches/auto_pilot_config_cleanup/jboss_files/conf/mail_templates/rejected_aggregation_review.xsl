<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	        xmlns:fo="http://www.w3.org/1999/XSL/Format"
		version="1.0" >
<xsl:output method="text" indent="yes"/>
<xsl:template match="/">
<xsl:for-each select="MAILDATA">
Hello <xsl:value-of select="USER_NAME"/>,

The project <xsl:value-of select="PROJECT_NAME"/> has failed to pass final fixes due to rejected aggregation review.
<xsl:for-each select="REVIEWER">
    Reviewer <xsl:value-of select="REVIEWER_HANDLE"/><xsl:if test="REVIEWER_AGG_ACCEPTED = 1"> accepted Aggregation Review.
</xsl:if>
<xsl:if test="REVIEWER_AGG_ACCEPTED = 0"> Rejected Aggregation Review:
        - <xsl:value-of select="REVIEWER_AGG_COMMENT"/>
</xsl:if>
</xsl:for-each>

The project will be moved back to Aggregation.

Thank you,
TopCoder Software

</xsl:for-each>
</xsl:template>
</xsl:stylesheet>
