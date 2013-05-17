package com.dianping.dobby.ticket.page.home;

import java.util.List;

import org.unidal.web.mvc.ViewModel;

import com.dianping.dobby.ticket.TicketPage;
import com.dianping.dobby.ticket.model.entity.Ticket;

public class Model extends ViewModel<TicketPage, Action, Context> {
	private List<Ticket> m_tickets;

	private Ticket m_ticket;

	public Model(Context ctx) {
		super(ctx);
	}

	@Override
	public Action getDefaultAction() {
		return Action.VIEW;
	}

	public Ticket getTicket() {
		return m_ticket;
	}

	public List<Ticket> getTickets() {
		return m_tickets;
	}

	public void setTicket(Ticket ticket) {
		m_ticket = ticket;
	}

	public void setTickets(List<Ticket> tickets) {
		m_tickets = tickets;
	}
}
