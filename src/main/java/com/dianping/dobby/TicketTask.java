package com.dianping.dobby;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.codehaus.plexus.personality.plexus.lifecycle.phase.Initializable;
import org.codehaus.plexus.personality.plexus.lifecycle.phase.InitializationException;
import org.unidal.helper.Threads.Task;
import org.unidal.lookup.annotation.Inject;

import com.dianping.cat.Cat;
import com.dianping.cat.message.Transaction;
import com.dianping.dobby.mail.GmailService;
import com.dianping.dobby.mail.Payload;
import com.dianping.dobby.model.entity.Ticket;
import com.dianping.dobby.ticket.TicketManager;
import com.dianping.dobby.ticket.TicketProcessor;

public class TicketTask implements Task, Initializable {
	@Inject
	private TicketProcessor m_processor;

	@Inject
	private TicketManager m_manager;

	private BlockingQueue<Payload> m_queue;

	@Override
	public String getName() {
		return getClass().getSimpleName();
	}

	private void handle(Payload payload) throws Exception {
		String id = payload.getId();
		Ticket ticket = m_manager.getTicket(id);
		String subject = payload.getSubject();
		String content = payload.getComment();
		String by = payload.getFrom();
		String cmd = payload.getCommand();
		String[] args = payload.getCommandParams();

		if (ticket == null) {
			m_processor.createTicket(id, subject, content, by);

			if (cmd != null) {
				m_processor.processTicket(id, by, null, cmd, args);
			}
		} else {
			m_processor.processTicket(id, by, content, cmd, args);
		}
	}

	@Override
	public void initialize() throws InitializationException {
		m_queue = new LinkedBlockingQueue<Payload>();
	}

	@Override
	public void run() {
		GmailService service = new GmailService(m_queue);

		System.out.println(service + " started"); // TODO

		try {
			while (true) {
				Payload payload = m_queue.poll(10, TimeUnit.MILLISECONDS);

				if (payload != null) {
					Transaction t = Cat.getProducer().newTransaction("Task", "Ticket");

					try {
						Cat.getProducer().logEvent(payload.getId(), payload.getFrom(), "0", null);

						System.out.println(payload);
						handle(payload);
						t.setStatus("0");
					} catch (Exception e) {
						e.printStackTrace();
						Cat.logError(e);
						t.setStatus(e);
					} finally {
						t.complete();
					}
				}
			}
		} catch (InterruptedException e) {
			// ignore it
		}
	}

	@Override
	public void shutdown() {
	}
}