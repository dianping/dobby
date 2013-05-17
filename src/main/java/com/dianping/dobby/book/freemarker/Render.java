package com.dianping.dobby.book.freemarker;

import java.io.StringWriter;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.plexus.personality.plexus.lifecycle.phase.Initializable;
import org.codehaus.plexus.personality.plexus.lifecycle.phase.InitializationException;

import com.dianping.cat.Cat;
import com.dianping.dobby.book.model.entity.Book;

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

	private String render(Map<Object, Object> root, StringWriter sw, String name) {
		try {
			Template t = m_configuration.getTemplate(name);

			t.process(root, sw);
		} catch (Exception e) {
			Cat.logError(e);
		}
		return sw.toString();
	}

	public String renderBorrow(Date date) {
		Map<Object, Object> root = new HashMap<Object, Object>();
		StringWriter sw = new StringWriter(5000);
		String name = "borrowSuccess.ftl";
		
		root.put("date", date);
		return render(root, sw, name);
	}

	public String renderDetail(Book book) {
		Map<Object, Object> root = new HashMap<Object, Object>();
		StringWriter sw = new StringWriter(5000);
		String name = "detail.ftl";

		root.put("book", book);
		return render(root, sw, name);
	}

	public String renderList(List<Book> books) {
		Map<Object, Object> root = new HashMap<Object, Object>();
		StringWriter sw = new StringWriter(5000);
		String name = "list.ftl";
		
		root.put("books", books);
		return render(root, sw, name);
	}

	public String renderReturn() {
		Map<Object, Object> root = new HashMap<Object, Object>();
		StringWriter sw = new StringWriter(5000);
		String name = "returnSuccess.ftl";

		return render(root, sw, name);
	}
}
