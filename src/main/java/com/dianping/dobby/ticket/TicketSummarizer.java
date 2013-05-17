package com.dianping.dobby.ticket;

import com.dianping.dobby.model.entity.Ticket;

public interface TicketSummarizer {
	public void sendSummaryEmail(Ticket ticket);
}
