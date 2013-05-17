package com.dianping.dobby.ticket.page.home;

import org.unidal.web.mvc.ActionContext;
import org.unidal.web.mvc.ActionPayload;
import org.unidal.web.mvc.payload.annotation.FieldMeta;

import com.dianping.dobby.ticket.TicketPage;

public class Payload implements ActionPayload<TicketPage, Action> {
	private TicketPage m_page;

	@FieldMeta("op")
	private Action m_action;

	@FieldMeta("id")
	private String m_id;

	@FieldMeta("noHeader")
	private boolean m_noHeader;

	@Override
	public Action getAction() {
		return m_action;
	}

	public String getId() {
		return m_id;
	}

	@Override
	public TicketPage getPage() {
		return m_page;
	}

	public boolean isNoHeader() {
		return m_noHeader;
	}

	public void setAction(String action) {
		m_action = Action.getByName(action, Action.VIEW);
	}

	@Override
	public void setPage(String page) {
		m_page = TicketPage.getByName(page, TicketPage.HOME);
	}

	@Override
	public void validate(ActionContext<?> ctx) {
		if (m_action == null) {
			m_action = Action.VIEW;
		}
	}
}
