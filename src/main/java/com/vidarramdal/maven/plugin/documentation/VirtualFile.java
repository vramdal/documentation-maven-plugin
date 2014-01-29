package com.vidarramdal.maven.plugin.documentation;

import org.apache.commons.collections15.ListUtils;
import org.apache.commons.collections15.Transformer;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class VirtualFile extends File {

	private List<VirtualFile> children = new ArrayList<VirtualFile>();

	public VirtualFile(String pathname) {
		super(pathname);
	}

	public void add(VirtualFile child) {
		this.children.add(child);
	}

	@Override
	public String[] list() {
		return ListUtils.transformedList(children, new Transformer<VirtualFile, String>() {
			public String transform(VirtualFile virtualFile) {
				return virtualFile.getName();
			}
		}).toArray(new String[children.size()]);
	}


}
