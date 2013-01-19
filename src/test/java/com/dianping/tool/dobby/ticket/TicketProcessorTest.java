package com.dianping.tool.dobby.ticket;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.Assert;

import org.junit.Test;
import org.unidal.lookup.ComponentTestCase;

import com.dianping.tool.dobby.model.entity.Ticket;

public class TicketProcessorTest extends ComponentTestCase {
	@Test
	public void test() throws Exception {
		TicketProcessor processor = lookup(TicketProcessor.class);
		MockTicketManager manager = (MockTicketManager) lookup(TicketManager.class);

		processor.createTicket("ID001", "This is subject", "This is content", "Test");

		Ticket ticket = manager.getTicket("ID001");

		Assert.assertEquals("This is subject", ticket.getSubject());
		Assert.assertEquals("This is content", ticket.getContent());
		Assert.assertEquals("Test", ticket.getCreatedBy());
		Assert.assertEquals(null, ticket.getAssignedTo());
		Assert.assertEquals(0, ticket.getActions().size());
		Assert.assertEquals(true, manager.isPersisted());

		manager.setPersisted(false);
		processor.processTicket("ID001", "Test2", "This is comment", "assignTo", "Test3");

		Assert.assertEquals("Test", ticket.getCreatedBy());
		Assert.assertEquals("Test2", ticket.getLastModifiedBy());
		Assert.assertEquals("Test3", ticket.getAssignedTo());
		Assert.assertEquals(1, ticket.getActions().size());
		Assert.assertEquals("Test2", ticket.getActions().get(0).getBy());
		Assert.assertEquals("Test3", ticket.getActions().get(0).getAssignedTo());
		Assert.assertEquals("This is comment", ticket.getActions().get(0).getComment());
		Assert.assertEquals(true, manager.isPersisted());
	}

	public static class MockTicketManager implements TicketManager {
		private Map<String, Ticket> m_tickets = new HashMap<String, Ticket>();

		private boolean m_persisted;

		@Override
		public void addTicket(Ticket ticket) {
			m_tickets.put(ticket.getId(), ticket);
		}

		@Override
		public Ticket getTicket(String id) {
			return m_tickets.get(id);
		}

		@Override
      public List<Ticket> getTickets() {
	      return new ArrayList<Ticket>(m_tickets.values());
      }

		public boolean isPersisted() {
			return m_persisted;
		}

		@Override
		public void persist() throws IOException {
			m_persisted = true;
		}

		public void setPersisted(boolean persisted) {
			m_persisted = persisted;
		}
	}
}
