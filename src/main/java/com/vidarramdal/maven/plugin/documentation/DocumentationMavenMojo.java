package com.vidarramdal.maven.plugin.documentation;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.descriptor.PluginDescriptor;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.util.FileUtils;
import org.markdown4j.Markdown4jProcessor;

import java.io.File;
import java.io.IOException;

@Mojo( name = "sayhi", inheritByDefault = true, aggregator = true)
public class DocumentationMavenMojo extends AbstractMojo {

    @Component
    private MavenProject project;

    @Component
    private PluginDescriptor pluginDescriptor;

    @Parameter( property = "sayhi.greeting", defaultValue = "Hello World!" )
    private String greeting;

    @Parameter( property = "sayhi.outputDirectory", defaultValue = "documentation" )
    private File _outputDir;

    @Parameter(property = "sayhi.documentationRoot", defaultValue = "documentation")
    private File _documentationRoot;
	private Markdown4jProcessor markdown4jProcessor = new Markdown4jProcessor();

	/**
     * Says "Hi" to the user.
     *
     */
    public void execute() throws MojoExecutionException {
        getLog().info( "Hello, world." + greeting );
        getLog().info("Henter dokumentasjonsfiler fra " + this._documentationRoot.getAbsolutePath());
		File parentOutputDir = this._outputDir;
		try {
			processDirectory(this._documentationRoot, parentOutputDir);
		} catch (Throwable e) {
			throw new MojoExecutionException("Error creating documentation", e);
		}
	}

	private void processDirectory(File inputDir, File parentOutputDir) throws IOException {
		final File[] files = inputDir.listFiles();
		if (files == null) {
			throw new RuntimeException("Could not get files from " + this._documentationRoot.getAbsolutePath());
		}
		for (File file : files) {
			final File targetFile = new File(parentOutputDir, file.getName());
			if (file.isDirectory()) {
				final boolean mkdirResult = targetFile.mkdir();
				if (!mkdirResult) {
					throw new RuntimeException("Could not create directory " + targetFile.getAbsolutePath());
				}
				processDirectory(file, targetFile);
			} else if ("md".equals(FileUtils.extension(file.getName()))) {
				String html = markdown4jProcessor.process(file);
				FileUtils.fileWrite(targetFile, "UTF-8", html);
			} else {
				FileUtils.copyFile(file, targetFile);
			}
		}
	}

	@SuppressWarnings("UnusedDeclaration")
	public void setDocumentationRoot(File documentationRoot) {
		this._documentationRoot = getAbsoluteDirectory(project.getBasedir(), documentationRoot, "documentationRoot");
	}

	@SuppressWarnings("UnusedDeclaration")
	public void setOutputDirectory(File outputDirectory) {
		this._outputDir = getAbsoluteDirectory(new File(project.getBuild().getDirectory()), outputDirectory, "outputDirectory");
	}

	private File getAbsoluteDirectory(File basedir, File absoluteOrRelative, String propertyName) {
		if (!absoluteOrRelative.isAbsolute()) {
			absoluteOrRelative = new File(basedir, absoluteOrRelative.getPath());
		}
		if (!absoluteOrRelative.isDirectory()) {
			throw new Error(propertyName + " should be a directory");
		}
		return absoluteOrRelative;
	}

}
