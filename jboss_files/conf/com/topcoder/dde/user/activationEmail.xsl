<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
<xsl:output method="text" media-type="text/rtf"/>
<xsl:template match="/">

Welcome to TopCoder Software!

To activate your new account either click on the following link or enter the URL into your browser:

<xsl:value-of select="activationEmail/url"/>

If you have additional questions or need assistance, please email service@topcodersoftware.com.

Best regards,

The TopCoder Software Team
</xsl:template>
</xsl:stylesheet>

