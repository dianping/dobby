package com.dianping.tool.dobby.ticket;

import com.dianping.tool.dobby.model.entity.Ticket;

public interface TicketSummarizer {
	public void sendSummaryEmail(Ticket ticket);
}
