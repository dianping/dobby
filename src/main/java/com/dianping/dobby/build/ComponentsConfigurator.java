package com.dianping.dobby.build;

import java.util.ArrayList;
import java.util.List;

import org.unidal.initialization.DefaultModuleManager;
import org.unidal.initialization.Module;
import org.unidal.initialization.ModuleManager;
import org.unidal.lookup.configuration.AbstractResourceConfigurator;
import org.unidal.lookup.configuration.Component;

import com.dianping.dobby.DobbyConstants;
import com.dianping.dobby.DobbyModule;
import com.dianping.dobby.book.biz.BookManager;
import com.dianping.dobby.book.biz.BookMessageHandler;
import com.dianping.dobby.book.biz.BookProcessor;
import com.dianping.dobby.book.biz.BorrowContext;
import com.dianping.dobby.book.biz.BorrowListener;
import com.dianping.dobby.book.biz.DefaultBookManager;
import com.dianping.dobby.book.biz.DefaultBookProcessor;
import com.dianping.dobby.book.biz.DefaultBorrowContext;
import com.dianping.dobby.book.biz.DefaultBorrowListener;
import com.dianping.dobby.email.DefaultMessageParser;
import com.dianping.dobby.email.EmailDispatcher;
import com.dianping.dobby.email.EmailChannel;
import com.dianping.dobby.email.GmailEmailChannel;
import com.dianping.dobby.email.MessageContentExtractor;
import com.dianping.dobby.email.MessageHandler;
import com.dianping.dobby.email.MessageParser;
import com.dianping.dobby.email.MessageQueue;
import com.dianping.dobby.ticket.biz.DefaultTicketContext;
import com.dianping.dobby.ticket.biz.DefaultTicketListener;
import com.dianping.dobby.ticket.biz.DefaultTicketManager;
import com.dianping.dobby.ticket.biz.DefaultTicketProcessor;
import com.dianping.dobby.ticket.biz.DefaultTicketSummarizer;
import com.dianping.dobby.ticket.biz.TicketContext;
import com.dianping.dobby.ticket.biz.TicketListener;
import com.dianping.dobby.ticket.biz.TicketManager;
import com.dianping.dobby.ticket.biz.TicketMessageHandler;
import com.dianping.dobby.ticket.biz.TicketProcessor;
import com.dianping.dobby.ticket.biz.TicketSummarizer;
import com.dianping.dobby.view.FreeMarkerView;

public class ComponentsConfigurator extends AbstractResourceConfigurator implements DobbyConstants {
   @Override
   public List<Component> defineComponents() {
      List<Component> all = new ArrayList<Component>();

      all.add(C(FreeMarkerView.class));

      defineEmailComponents(all);
      defineTicketComponents(all);
      defineBookComponents(all);

      all.add(C(Module.class, DobbyModule.ID, DobbyModule.class));
      all.add(C(ModuleManager.class, DefaultModuleManager.class) //
            .config(E("topLevelModules").value(DobbyModule.ID)));

      // Please keep it as last
      all.addAll(new WebComponentConfigurator().defineComponents());

      return all;
   }

   private void defineEmailComponents(List<Component> all) {
      all.add(C(MessageContentExtractor.class));
      all.add(C(MessageParser.class, DefaultMessageParser.class) //
            .req(MessageContentExtractor.class));
   }

   private void defineBookComponents(List<Component> all) {
      all.add(C(EmailChannel.class, ID_BOOK, GmailEmailChannel.class) //
            .req(MessageQueue.class, ID_BOOK) //
            .req(MessageParser.class) //
            .config(E("name").value("book.robot.dianping@gmail.com"), // book.robot.dianping123
                  E("password").value("xudgtsnoxivwclna")));
      all.add(C(EmailDispatcher.class, ID_BOOK, EmailDispatcher.class) //
            .req(MessageQueue.class, ID_BOOK) //
            .req(MessageHandler.class, ID_BOOK) //
            .req(EmailChannel.class, ID_BOOK));
      all.add(C(MessageQueue.class, ID_BOOK, MessageQueue.class));
      all.add(C(MessageHandler.class, ID_BOOK, BookMessageHandler.class) //
            .req(EmailChannel.class, ID_BOOK) //
            .req(BookManager.class, FreeMarkerView.class));

      all.add(C(BorrowContext.class, DefaultBorrowContext.class) //
            .req(BorrowListener.class));
      all.add(C(BorrowListener.class, DefaultBorrowListener.class));
      all.add(C(BookProcessor.class, DefaultBookProcessor.class) //
            .req(BookManager.class));
      all.add(C(BookManager.class, DefaultBookManager.class));
   }

   private void defineTicketComponents(List<Component> all) {
      all.add(C(EmailChannel.class, ID_TICKET, GmailEmailChannel.class) //
            .req(MessageQueue.class, ID_TICKET) //
            .req(MessageParser.class) //
            .config(E("name").value("ticketmatetest@gmail.com"), //
                  E("password").value("xgeskoauugnqddyf")));
      all.add(C(EmailDispatcher.class, ID_TICKET, EmailDispatcher.class) //
            .req(MessageQueue.class, ID_TICKET) //
            .req(MessageHandler.class, ID_TICKET) //
            .req(EmailChannel.class, ID_TICKET));
      all.add(C(MessageQueue.class, ID_TICKET, MessageQueue.class));
      all.add(C(MessageHandler.class, ID_TICKET, TicketMessageHandler.class) //
            .req(TicketProcessor.class, TicketManager.class));

      all.add(C(TicketManager.class, DefaultTicketManager.class));
      all.add(C(TicketProcessor.class, DefaultTicketProcessor.class) //
            .req(TicketManager.class));
      all.add(C(TicketContext.class, DefaultTicketContext.class).is(PER_LOOKUP) //
            .req(TicketListener.class));
      all.add(C(TicketListener.class, DefaultTicketListener.class) //
            .req(TicketManager.class, TicketSummarizer.class));
      all.add(C(TicketSummarizer.class, DefaultTicketSummarizer.class));
   }

   public static void main(String[] args) {
      generatePlexusComponentsXmlFile(new ComponentsConfigurator());
   }
}
