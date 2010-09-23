<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
				xmlns:fo="http://www.w3.org/1999/XSL/Format"
				version="1.0" > 
<xsl:output method="text" indent="yes"/>
<xsl:template match="/">
<xsl:for-each select="MAILDATA">
Your appeal has been resolved for the project <xsl:value-of select="PROJECT_NAME"/>, question <xsl:value-of select="QUESTION_NUMBER"/>

The appeal text is:
<xsl:value-of select="APPEAL_TEXT"/>

The appeal response is:
<xsl:value-of select="APPEAL_RESPONSE"/>

Any feedback regarding the project or the process can be directed to service@topcodersoftware.com.

Thanks,
TopCoder Software Service

</xsl:for-each>
</xsl:template>
</xsl:stylesheet>
