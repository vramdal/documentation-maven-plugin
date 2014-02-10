<xsl:stylesheet
		version="1.0"
		xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
		>

	<xsl:output method="html"/>

	<xsl:param name="doctitle"/>
	<xsl:param name="content"/>
	<xsl:param name="menu"/>

	<xsl:template match="/">
	    <html>
			<head>
				<title><xsl:value-of select="$doctitle"/></title>
			</head>
			<body>
				<xsl:apply-templates select="body/*"/>
			</body>
	    </html>
	</xsl:template>

    <xsl:template match="*">
        <xsl:copy>
            <xsl:copy-of select="attribute::*"/>
            <xsl:apply-templates/>
        </xsl:copy>
    </xsl:template>

</xsl:stylesheet>