package com.dianping.dobby.book.page.home;

public enum JspFile {
	VIEW("/jsp/book/home.jsp"),

	;

	private String m_path;

	private JspFile(String path) {
		m_path = path;
	}

	public String getPath() {
		return m_path;
	}
}
