package com.dianping.tool.dobby.ticket;

import java.io.IOException;
import java.util.List;

import com.dianping.tool.dobby.model.entity.Ticket;

public interface TicketManager {
	public Ticket getTicket(String id);

	public void persist() throws IOException;

	public void addTicket(Ticket ticket);

	public List<Ticket> getTickets();
}
