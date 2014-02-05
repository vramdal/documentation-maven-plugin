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
				<xsl:copy-of select="$content"/>
			</body>
	    </html>
	</xsl:template>

</xsl:stylesheet>