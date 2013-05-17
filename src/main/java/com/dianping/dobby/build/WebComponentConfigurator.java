package com.dianping.dobby.build;

import java.util.ArrayList;
import java.util.List;

import com.dianping.dobby.ticket.TicketModule;
import com.dianping.dobby.book.BookModule;

import org.unidal.lookup.configuration.Component;
import org.unidal.web.configuration.AbstractWebComponentsConfigurator;

class WebComponentConfigurator extends AbstractWebComponentsConfigurator {
	@SuppressWarnings("unchecked")
	@Override
	public List<Component> defineComponents() {
		List<Component> all = new ArrayList<Component>();

		defineModuleRegistry(all, BookModule.class, TicketModule.class,BookModule.class);

		return all;
	}
}
