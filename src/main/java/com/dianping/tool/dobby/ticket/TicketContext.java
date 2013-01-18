package com.dianping.tool.dobby.ticket;

import com.dianping.tool.dobby.model.entity.Ticket;

public interface TicketContext {
	public TicketListener getListener();

	public TicketState getNextState();

	public TicketState getState();

	public Ticket getTicket();
}
