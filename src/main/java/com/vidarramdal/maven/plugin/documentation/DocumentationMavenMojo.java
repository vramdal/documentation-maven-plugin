package com.vidarramdal.maven.plugin.documentation;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.descriptor.PluginDescriptor;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

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
    /**
     * Says "Hi" to the user.
     *
     */
    public void execute() throws MojoExecutionException {
        getLog().info( "Hello, world." + greeting );
        getLog().info("Henter dokumentasjonsfiler fra " + this._documentationRoot.getAbsolutePath());

    }

    public void setDocumentationRoot(File documentationRoot) {
        if (!documentationRoot.isAbsolute()) {
            documentationRoot = new File(project.getBasedir(), documentationRoot.getPath());
        }
        this._documentationRoot = documentationRoot;
    }
}
