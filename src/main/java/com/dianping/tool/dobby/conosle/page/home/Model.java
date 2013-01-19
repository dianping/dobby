package com.dianping.tool.dobby.conosle.page.home;

import java.util.List;

import org.unidal.web.mvc.ViewModel;

import com.dianping.tool.dobby.conosle.ConoslePage;
import com.dianping.tool.dobby.model.entity.Ticket;

public class Model extends ViewModel<ConoslePage, Action, Context> {
	private List<Ticket> m_tickets;

	public Model(Context ctx) {
		super(ctx);
	}

	@Override
	public Action getDefaultAction() {
		return Action.VIEW;
	}

	public List<Ticket> getTickets() {
		return m_tickets;
	}

	public void setTickets(List<Ticket> tickets) {
		m_tickets = tickets;
	}
}
