package com.dianping.dobby.ticket;

import java.util.ArrayList;
import java.util.List;

import org.unidal.lookup.configuration.AbstractResourceConfigurator;
import org.unidal.lookup.configuration.Component;

import com.dianping.dobby.ticket.TicketProcessorTest.MockTicketManager;
import com.dianping.dobby.ticket.biz.TicketManager;

public class TicketProcessorTestConfigurator extends AbstractResourceConfigurator {
	@Override
	public List<Component> defineComponents() {
		List<Component> all = new ArrayList<Component>();

		all.add(C(TicketManager.class, MockTicketManager.class));

		return all;
	}

	@Override
	protected Class<?> getTestClass() {
		return TicketProcessorTest.class;
	}

	public static void main(String[] args) {
		generatePlexusComponentsXmlFile(new TicketProcessorTestConfigurator());
	}
}
