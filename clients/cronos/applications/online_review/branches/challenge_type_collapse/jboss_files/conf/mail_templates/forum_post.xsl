<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	        xmlns:fo="http://www.w3.org/1999/XSL/Format"
		version="1.0" > 
<xsl:output method="text" indent="yes"/>
<xsl:template match="/">
<xsl:for-each select="MAILDATA">
Hello,

<xsl:if test="IS_NEW_THREAD = 0">A new message was posted in the thread "<xsl:value-of select="THREAD_NAME"/>"</xsl:if><xsl:if test="IS_NEW_THREAD = 1">The thread "<xsl:value-of select="THREAD_NAME"/>" was created </xsl:if> for the component <xsl:value-of select="COMPONENT_NAME"/> by <xsl:value-of select="WRITER_NAME"/>.

Please see https://software.topcoder.com<xsl:value-of select="LINK"/>

Thanks,
TopCoder Software Service

</xsl:for-each>
</xsl:template>
</xsl:stylesheet>
