package com.dianping.dobby.ticket.biz;

import com.dianping.dobby.ticket.model.entity.Ticket;

public interface TicketContext {
	public TicketListener getListener();

	public TicketState getNextState();

	public TicketState getState();

	public Ticket getTicket();
}
