package com.dianping.dobby.ticket.biz;

import com.dianping.dobby.ticket.model.entity.Ticket;

public interface TicketSummarizer {
	public void sendSummaryEmail(Ticket ticket);
}
