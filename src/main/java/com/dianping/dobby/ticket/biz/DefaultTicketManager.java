package com.dianping.dobby.ticket.biz;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.codehaus.plexus.personality.plexus.lifecycle.phase.Initializable;
import org.codehaus.plexus.personality.plexus.lifecycle.phase.InitializationException;
import org.unidal.helper.Files;

import com.dianping.dobby.ticket.model.entity.Model;
import com.dianping.dobby.ticket.model.entity.Ticket;
import com.dianping.dobby.ticket.model.transform.DefaultSaxParser;

public class DefaultTicketManager implements TicketManager, Initializable {
	private File m_modelFile;

	private Model m_model;

	@Override
	public void initialize() throws InitializationException {
		try {
			m_modelFile = new File("ticket.xml").getCanonicalFile();

			if (m_modelFile.canRead()) {
				String xml = Files.forIO().readFrom(m_modelFile, "utf-8");

				m_model = DefaultSaxParser.parse(xml);
			} else {
				m_model = new Model();
			}
		} catch (Exception e) {
			throw new InitializationException("Unable to load ticket.xml!", e);
		}
	}

	@Override
	public Ticket getTicket(String id) {
		return m_model.findTicket(id);
	}

	@Override
	public void persist() throws IOException {
		Files.forIO().writeTo(m_modelFile, m_model.toString());
	}

	@Override
	public void addTicket(Ticket ticket) {
		m_model.addTicket(ticket);
	}

	@Override
	public List<Ticket> getTickets() {
		return new ArrayList<Ticket>(m_model.getTickets().values());
	}
}
