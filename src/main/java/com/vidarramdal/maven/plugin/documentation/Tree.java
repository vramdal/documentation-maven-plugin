package com.vidarramdal.maven.plugin.documentation;

import java.util.ArrayList;

public class Tree<T> extends ArrayList<Tree<T>> {

	private T value;

	public Tree(T value) {
		this.value = value;
	}

	public void addValue(T value) {
		this.add(new Tree<T>(value));
	}

	public T getValue() {
		return this.value;
	}

}

