package com.dianping.dobby.build;

import java.util.ArrayList;
import java.util.List;

import org.unidal.initialization.DefaultModuleManager;
import org.unidal.initialization.Module;
import org.unidal.initialization.ModuleManager;
import org.unidal.lookup.configuration.AbstractResourceConfigurator;
import org.unidal.lookup.configuration.Component;

import com.dianping.dobby.TicketModule;
import com.dianping.dobby.TicketTask;
import com.dianping.dobby.book.biz.BorrowContext;
import com.dianping.dobby.book.biz.BorrowListener;
import com.dianping.dobby.book.biz.BookManager;
import com.dianping.dobby.book.biz.BookProcessor;
import com.dianping.dobby.book.biz.DefaultBorrowContext;
import com.dianping.dobby.book.biz.DefaultBorrowListener;
import com.dianping.dobby.book.biz.DefaultBookManager;
import com.dianping.dobby.book.biz.DefaultBookProcessor;
import com.dianping.dobby.mail.ContentBuilder;
import com.dianping.dobby.mail.DefaultMessageParser;
import com.dianping.dobby.mail.MessageParser;
import com.dianping.dobby.ticket.biz.DefaultTicketContext;
import com.dianping.dobby.ticket.biz.DefaultTicketListener;
import com.dianping.dobby.ticket.biz.DefaultTicketManager;
import com.dianping.dobby.ticket.biz.DefaultTicketProcessor;
import com.dianping.dobby.ticket.biz.DefaultTicketSummarizer;
import com.dianping.dobby.ticket.biz.TicketContext;
import com.dianping.dobby.ticket.biz.TicketListener;
import com.dianping.dobby.ticket.biz.TicketManager;
import com.dianping.dobby.ticket.biz.TicketProcessor;
import com.dianping.dobby.ticket.biz.TicketSummarizer;

public class ComponentsConfigurator extends AbstractResourceConfigurator {
	@Override
	public List<Component> defineComponents() {
		List<Component> all = new ArrayList<Component>();

		all.add(C(ContentBuilder.class));
		all.add(C(MessageParser.class, DefaultMessageParser.class) //
		      .req(ContentBuilder.class));

		all.add(C(TicketManager.class, DefaultTicketManager.class));
		all.add(C(TicketProcessor.class, DefaultTicketProcessor.class) //
		      .req(TicketManager.class));
		all.add(C(TicketContext.class, DefaultTicketContext.class).is(PER_LOOKUP) //
		      .req(TicketListener.class));
		all.add(C(TicketListener.class, DefaultTicketListener.class) //
		      .req(TicketManager.class, TicketSummarizer.class));
		all.add(C(TicketSummarizer.class, DefaultTicketSummarizer.class));

		all.add(C(TicketTask.class) //
		      .req(TicketProcessor.class, TicketManager.class));

		all.add(C(Module.class, TicketModule.ID, TicketModule.class));
		all.add(C(ModuleManager.class, DefaultModuleManager.class) //
		      .config(E("topLevelModules").value(TicketModule.ID)));

		// Please keep it as last
		all.addAll(new WebComponentConfigurator().defineComponents());
		
		// For book
		all.add(C(BorrowContext.class, DefaultBorrowContext.class));
		all.add(C(BookManager.class, DefaultBookManager.class));
		all.add(C(BorrowListener.class, DefaultBorrowListener.class));
		all.add(C(BookProcessor.class, DefaultBookProcessor.class).req(BookManager.class));

		return all;
	}

	public static void main(String[] args) {
		generatePlexusComponentsXmlFile(new ComponentsConfigurator());
	}
}
