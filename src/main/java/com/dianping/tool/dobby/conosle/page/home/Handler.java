package com.dianping.tool.dobby.conosle.page.home;

import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.servlet.ServletException;

import org.unidal.lookup.annotation.Inject;
import org.unidal.web.mvc.PageHandler;
import org.unidal.web.mvc.annotation.InboundActionMeta;
import org.unidal.web.mvc.annotation.OutboundActionMeta;
import org.unidal.web.mvc.annotation.PayloadMeta;

import com.dianping.tool.dobby.conosle.ConoslePage;
import com.dianping.tool.dobby.model.entity.Ticket;
import com.dianping.tool.dobby.ticket.TicketManager;

public class Handler implements PageHandler<Context> {
	@Inject
	private JspViewer m_jspViewer;

	@Inject
	private TicketManager m_manager;

	@Override
	@PayloadMeta(Payload.class)
	@InboundActionMeta(name = "home")
	public void handleInbound(Context ctx) throws ServletException, IOException {
		// display only, no action here
	}

	@Override
	@OutboundActionMeta(name = "home")
	public void handleOutbound(Context ctx) throws ServletException, IOException {
		Model model = new Model(ctx);
		Payload payload = ctx.getPayload();

		model.setAction(payload.getAction());
		model.setPage(ConoslePage.HOME);

		switch (payload.getAction()) {
		case VIEW:
			model.setTickets(getSortedTickets());
			break;
		case SUMMARY:
			Ticket ticket = m_manager.getTicket(payload.getId());
			
			model.setTicket(ticket);
			break;
		}

		m_jspViewer.view(ctx, model);
	}

	private List<Ticket> getSortedTickets() {
		List<Ticket> tickets = m_manager.getTickets();

		// order by last modified date, descend
		Collections.sort(tickets, new Comparator<Ticket>() {
			@Override
			public int compare(Ticket t1, Ticket t2) {
				return (int) (t2.getLastModifiedDate().getTime() - t1.getLastModifiedDate().getTime());
			}
		});

		return tickets;
	}
}
