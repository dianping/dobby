package com.dianping.dobby.book.freemarker;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import org.codehaus.plexus.personality.plexus.lifecycle.phase.Initializable;
import org.codehaus.plexus.personality.plexus.lifecycle.phase.InitializationException;

import com.dianping.cat.Cat;

import freemarker.template.Configuration;
import freemarker.template.Template;

public class Render implements Initializable {
	public Configuration m_configuration;

	@Override
	public void initialize() throws InitializationException {
		m_configuration = new Configuration();
		m_configuration.setDefaultEncoding("UTF-8");
		try {
			m_configuration.setClassForTemplateLoading(Render.class, "/freemaker");
		} catch (Exception e) {
			Cat.logError(e);
		}
	}

	public String renderList() {
		Map<Object, Object> root = new HashMap<Object, Object>();
		StringWriter sw = new StringWriter(5000);
		String name = "list.ftl";

		return render(root, sw, name);
	}

	private String render(Map<Object, Object> root, StringWriter sw, String name) {
		try {
			Template t = m_configuration.getTemplate(name);

			t.process(root, sw);
		} catch (Exception e) {
			Cat.logError(e);
		}
		return sw.toString();
	}

	public String renderDetail() {
		Map<Object, Object> root = new HashMap<Object, Object>();
		StringWriter sw = new StringWriter(5000);
		String name = "list.ftl";

		return render(root, sw, name);
	}

	public String renderBorrow() {
		Map<Object, Object> root = new HashMap<Object, Object>();
		StringWriter sw = new StringWriter(5000);
		String name = "list.ftl";

		return render(root, sw, name);
	}

	public String renderReturn() {
		Map<Object, Object> root = new HashMap<Object, Object>();
		StringWriter sw = new StringWriter(5000);
		String name = "list.ftl";

		return render(root, sw, name);
	}
}
