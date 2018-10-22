<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
<xsl:output method="text" media-type="text/rtf"/>
<xsl:template match="/">

Dear <xsl:value-of select="passwordEmail/firstname"/>

You have requested that your password be sent back to you.

username: <xsl:value-of select="passwordEmail/username"/>
password: <xsl:value-of select="passwordEmail/password"/>

Best,
The TopCoder Software Team
</xsl:template>
</xsl:stylesheet>