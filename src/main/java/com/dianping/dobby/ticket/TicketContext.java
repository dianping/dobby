package com.dianping.dobby.ticket;

import com.dianping.dobby.model.entity.Ticket;

public interface TicketContext {
	public TicketListener getListener();

	public TicketState getNextState();

	public TicketState getState();

	public Ticket getTicket();
}
