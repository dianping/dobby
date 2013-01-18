package com.dianping.tool.dobby.ticket;

import java.util.Date;

import org.unidal.lookup.ContainerHolder;
import org.unidal.lookup.annotation.Inject;

import com.dianping.tool.dobby.model.entity.Assignment;
import com.dianping.tool.dobby.model.entity.Comment;
import com.dianping.tool.dobby.model.entity.Ticket;

public class DefaultTicketProcessor extends ContainerHolder implements TicketProcessor {
	@Inject
	private TicketManager m_manager;

	@Override
	public void createTicket(String id, String subject, String content, String createdBy) throws Exception {
		Ticket ticket = m_manager.getTicket(id);

		if (ticket != null) {
			throw new RuntimeException(String.format("Ticket(%s) is already existed!", id));
		} else {
			Date now = new Date();

			ticket = new Ticket(id);
			ticket.setSubject(subject);
			ticket.setState(TicketState.CREATED.name());
			ticket.setContent(content);
			ticket.setCreatedBy(createdBy);
			ticket.setCreationDate(now);
			ticket.setLastModifiedBy(createdBy);
			ticket.setLastModifiedDate(now);

			m_manager.addTicket(ticket);

			DefaultTicketContext ctx = createTicketContext(ticket);

			try {
				TicketState.CREATED.moveTo(ctx, null);
			} finally {
				release(ctx);
			}
		}
	}

	private DefaultTicketContext createTicketContext(Ticket ticket) {
		DefaultTicketContext ctx = (DefaultTicketContext) lookup(TicketContext.class);

		ctx.setTicket(ticket);
		return ctx;
	}

	@Override
	public void processTicket(String id, String by, String comment, String cmd, String... args) throws Exception {
		Ticket ticket = m_manager.getTicket(id);
		Date now = new Date();

		if (ticket == null) {
			throw new RuntimeException(String.format("Ticket(%s) is not found!", id));
		} else {
			TicketState state = TicketState.getByName(ticket.getState(), null);
			TicketState nextState = null;

			if ("assignTo".equals(cmd)) {
				String assignTo = args.length > 0 ? args[0] : null;

				if (assignTo == null) {
					throw new IllegalArgumentException("No assignTo found in the arguments!");
				}

				nextState = TicketState.ASSIGNED;
				ticket.setAssignedTo(assignTo);
				ticket.addAssignment(new Assignment().setAt(now).setBy(by).setTo(assignTo));
			} else if ("accepted".equals(cmd)) {
				nextState = TicketState.ACCEPTED;
			} else if ("done".equals(cmd)) {
				nextState = TicketState.RESOLVED;
			} else if ("ignored".equals(cmd)) {
				nextState = TicketState.IGNORED;
			}

			DefaultTicketContext ctx = createTicketContext(ticket);

			try {
				if (nextState != null) {
					ticket.setState(nextState.name());
				}

				ticket.setLastModifiedBy(by);
				ticket.setLastModifiedDate(now);

				if (comment != null) {
					ticket.addComment(new Comment().setAt(now).setBy(by).setText(comment));
				}

				state.moveTo(ctx, nextState);
			} finally {
				release(ctx);
			}
		}
	}
}
