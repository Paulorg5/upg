package io.github.paulorg5.vo;

import java.util.ArrayList;
import java.util.List;

public class ArvoreAnaliseVO {

	private String text;

	private List<ArvoreAnaliseVO> children = new ArrayList<>();

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public List<ArvoreAnaliseVO> getChildren() {
		return children;
	}

	public void setChildren(List<ArvoreAnaliseVO> children) {
		this.children = children;
	}

}
