package com.dianping.tool.dobby.ticket;

import java.util.Date;

import org.unidal.lookup.ContainerHolder;
import org.unidal.lookup.annotation.Inject;

import com.dianping.tool.dobby.model.entity.Action;
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

			if (state == null) {
				throw new RuntimeException(String.format("Invalid Ticket state(%s)!", ticket.getState()));
			}

			Action action = new Action().setAt(now).setBy(by).setComment(comment);
			TicketState nextState = null;

			ticket.getActions().add(0, action);

			if ("assignTo".equalsIgnoreCase(cmd)) {
				String assignedTo = args.length > 0 ? args[0] : null;

				if (assignedTo == null) {
					throw new IllegalArgumentException("No assignTo found in the arguments!");
				}

				nextState = TicketState.ASSIGNED;
				ticket.setAssignedTo(assignedTo);
				action.setAssignedTo(assignedTo);
			} else if ("accepted".equalsIgnoreCase(cmd)) {
				nextState = TicketState.ACCEPTED;
			} else if ("done".equalsIgnoreCase(cmd)) {
				nextState = TicketState.RESOLVED;
			} else if ("ignored".equalsIgnoreCase(cmd)) {
				nextState = TicketState.IGNORED;
			}

			DefaultTicketContext ctx = createTicketContext(ticket);

			try {
				if (nextState != null) {
					String stateName = nextState.name();

					action.setState(stateName);
					ticket.setState(stateName);
				}

				ticket.setLastModifiedBy(by);
				ticket.setLastModifiedDate(now);

				state.moveTo(ctx, nextState);
			} finally {
				release(ctx);
			}
		}
	}
}
