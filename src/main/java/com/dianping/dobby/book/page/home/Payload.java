package com.dianping.dobby.book.page.home;

import com.dianping.dobby.book.BookPage;
import org.unidal.web.mvc.ActionContext;
import org.unidal.web.mvc.ActionPayload;
import org.unidal.web.mvc.payload.annotation.FieldMeta;

public class Payload implements ActionPayload<BookPage, Action> {
	private BookPage m_page;

	@FieldMeta("op")
	private Action m_action;

	@FieldMeta("id")
	private int m_id;

	public void setAction(String action) {
		m_action = Action.getByName(action, Action.VIEW);
	}

	@Override
	public Action getAction() {
		return m_action;
	}

	@Override
	public BookPage getPage() {
		return m_page;
	}

	public int getId() {
		return m_id;
	}

	public void setId(int id) {
		m_id = id;
	}

	@Override
	public void setPage(String page) {
		m_page = BookPage.getByName(page, BookPage.HOME);
	}

	@Override
	public void validate(ActionContext<?> ctx) {
		if (m_action == null) {
			m_action = Action.VIEW;
		}
	}
}
