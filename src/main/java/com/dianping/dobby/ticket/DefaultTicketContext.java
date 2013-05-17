package com.dianping.dobby.ticket;

import org.unidal.lookup.annotation.Inject;

import com.dianping.dobby.model.entity.Ticket;

public class DefaultTicketContext implements TicketContext {
	@Inject
	private TicketListener m_listener;

	private Ticket m_ticket;

	private TicketState m_state;

	private TicketState m_nextState;

	@Override
	public TicketListener getListener() {
		return m_listener;
	}

	@Override
	public TicketState getNextState() {
		return m_nextState;
	}

	@Override
	public TicketState getState() {
		return m_state;
	}

	@Override
	public Ticket getTicket() {
		return m_ticket;
	}

	public void setNextState(TicketState nextState) {
		m_nextState = nextState;
	}

	public void setState(TicketState state) {
		m_state = state;
	}

	public void setTicket(Ticket ticket) {
		m_ticket = ticket;
	}
}
