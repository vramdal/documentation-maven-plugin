package com.vidarramdal.maven.plugin.documentation;

import org.apache.maven.project.MavenProject;

import java.util.ArrayList;
import java.util.List;

public class ProjectNode extends MavenProject {

    private MavenProject project;
    private List<MavenProject> children = new ArrayList<>();

    public ProjectNode(MavenProject project) {
        this.project = project;
    }

    public void addChild(MavenProject child) {
        this.children.add(child);
    }

    public List<MavenProject> getChildren() {
        return children;
    }

    public MavenProject getProject() {
        return project;
    }
}
