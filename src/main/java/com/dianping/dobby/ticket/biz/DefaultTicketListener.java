package com.dianping.dobby.ticket.biz;

import org.unidal.lookup.annotation.Inject;

public class DefaultTicketListener implements TicketListener {
	@Inject
	private TicketManager m_manager;

	@Inject
	private TicketSummarizer m_summarizer;

	@Override
	public void beforeStateChange(TicketContext ctx) throws Exception {

	}

	@Override
	public void afterStateChange(TicketContext ctx) throws Exception {
		TicketState state = ctx.getState();

		switch (state) {
		case IGNORED:
		case RESOLVED:
			m_summarizer.sendSummaryEmail(ctx.getTicket());
			break;
		}

		m_manager.persist();
	}
}
