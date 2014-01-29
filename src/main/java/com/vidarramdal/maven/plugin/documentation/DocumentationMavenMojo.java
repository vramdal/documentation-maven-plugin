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

@Mojo( name = "sayhi", inheritByDefault = true, aggregator = true)
public class DocumentationMavenMojo extends AbstractMojo {

    @Component
    private MavenProject project;

    @Component
    private PluginDescriptor pluginDescriptor;

    @Parameter( property = "sayhi.greeting", defaultValue = "Hello World!" )
    private String greeting;

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
		final File[] files = this._documentationRoot.listFiles();
		for (File file : files) {
			if ("md".equals(FileUtils.extension(file.getName()))) {
//				String html = markdown4jProcessor.process(file);

			}
		}
	}

    public void setDocumentationRoot(File documentationRoot) {
        if (!documentationRoot.isAbsolute()) {
            documentationRoot = new File(project.getBasedir(), documentationRoot.getPath());
        }
		if (!documentationRoot.isDirectory()) {
			throw new Error("DocumentationRoot should be a directory");
		}
        this._documentationRoot = documentationRoot;
    }
}
