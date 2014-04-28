<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	        xmlns:fo="http://www.w3.org/1999/XSL/Format"
		version="1.0" > 
<xsl:output method="text" indent="yes"/>
<xsl:template match="/">
<xsl:for-each select="MAILDATA">
Hello <xsl:value-of select="USER_NAME"/>,

The project <xsl:value-of select="PROJECT_NAME"/> has failed to pass final review.

<xsl:if test="NOT_FIXED_ITEMS &gt; 0">
<xsl:value-of select="NOT_FIXED_ITEMS"/> item(s) were not fixed.
	<xsl:if test="IS_COMMENTED = 1">
The following was commented about it:

<xsl:value-of select="COMMENT"/>	   
	</xsl:if>
</xsl:if>
<xsl:if test="NOT_FIXED_ITEMS = 0 and IS_COMMENTED = 1">
All the items were fixed. However, it was rejected with the following comment:

<xsl:value-of select="COMMENT"/>
</xsl:if>

Thanks,
TopCoder Software Service

</xsl:for-each>
</xsl:template>
</xsl:stylesheet>
