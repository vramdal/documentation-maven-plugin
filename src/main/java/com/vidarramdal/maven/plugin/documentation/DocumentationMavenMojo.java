package com.vidarramdal.maven.plugin.documentation;

import org.apache.maven.execution.MavenSession;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.BuildPluginManager;
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
import java.util.List;

@Mojo( name = "sayhi", inheritByDefault = false)
public class DocumentationMavenMojo extends AbstractMojo {

    @Component
    private MavenProject project;

    @Component
    private PluginDescriptor pluginDescriptor;

    @Parameter( property = "sayhi.greeting", defaultValue = "Hello World!" )
    private String greeting;

    @Parameter( property = "sayhi.outputDirectory", defaultValue = "documentation" )
    private String outputDirectory;

    @Parameter(property = "sayhi.documentationRoot", defaultValue = "documentation")
    private String documentationRoot;

    @Parameter( property = "reactorProjects", readonly = true, required = true )
    private List<?> reactorProjects;

    @Component
    private MavenSession session;

    @Component
    private BuildPluginManager buildPluginManager;

	private Markdown4jProcessor markdown4jProcessor = new Markdown4jProcessor();
    private File _outputDir;

    /**
     * Says "Hi" to the user.
     *
     */
    public void execute() throws MojoExecutionException {
        if (!project.isExecutionRoot()) {
            // Do not run for sub-modules - they are handled when the plugin is invoked on
            // the parent module, by iterating modules below
            return;
        }
        this._outputDir = getAbsoluteDirectory(new File(project.getBuild().getDirectory()), this.outputDirectory, "outputDirectory");
        if (!this._outputDir.exists() && !this._outputDir.mkdirs()) {
            throw new MojoExecutionException("Could not create directory " + this._outputDir.getAbsolutePath());
        }
        processProject(project);
	}

    private void processProject(final MavenProject project) throws MojoExecutionException {
        File documentationRootDir = getAbsoluteDirectory(project.getBasedir(), this.documentationRoot, "documentationRoot");
        getLog().info("Processing documentation for " + project.getName() );
        getLog().info("Fetching documentation files from " + documentationRootDir.getAbsolutePath());
        if (documentationRootDir.exists()) {
            try {
                File parentOutputDir = new File(this._outputDir, (project.isExecutionRoot() ? "" : project.getArtifactId()));
                if (!parentOutputDir.exists() && !parentOutputDir.mkdirs()) {
                    throw new MojoExecutionException("Unable to create directory " + parentOutputDir.getAbsolutePath());
                }
                processDirectory(documentationRootDir, parentOutputDir);
            } catch (Throwable e) {
                throw new MojoExecutionException("Error creating documentation", e);
            }
        } else {
            getLog().info("No documentation found for module " + project.getName() + " - skipping");
        }
        List<MavenProject> collectedProjects = project.getCollectedProjects();
        for (MavenProject collectedProject : collectedProjects) {
            if (collectedProject.getParent().equals(project)) { // Only process children - not deeper descendants
                processProject(collectedProject);
            }
        }
    }

    private void processDirectory(File inputDir, File parentOutputDir) throws IOException {
		final File[] files = inputDir.listFiles();
		if (files == null) {
			throw new RuntimeException("Could not get files from " + inputDir.getAbsolutePath());
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
                File htmlFile = new File(parentOutputDir, FileUtils.removeExtension(file.getName()) + ".html");
				FileUtils.fileWrite(htmlFile, "UTF-8", html);
			} else {
				FileUtils.copyFile(file, targetFile);
			}
		}
	}

	@SuppressWarnings("UnusedDeclaration")
	public void setDocumentationRoot(String documentationRootName) {
        this.documentationRoot = documentationRootName;
	}

	@SuppressWarnings("UnusedDeclaration")
	public void setOutputDirectory(String outputDirectoryName) {
        this.outputDirectory = outputDirectoryName;
	}

    public void setGreeting(String greeting) {
        this.greeting = greeting;
    }

	private File getAbsoluteDirectory(File basedir, String absoluteOrRelativeName, String propertyName) {
        File absoluteOrRelative = new File(absoluteOrRelativeName);
		if (!absoluteOrRelative.isAbsolute()) {
			absoluteOrRelative = new File(basedir, absoluteOrRelative.getPath());
		}
		if (absoluteOrRelative.exists() && !absoluteOrRelative.isDirectory()) {
			throw new Error(propertyName + " should be a directory");
		}
		return absoluteOrRelative;
	}

}
