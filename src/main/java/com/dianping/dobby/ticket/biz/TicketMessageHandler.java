package com.dianping.dobby.ticket.biz;

import org.unidal.lookup.annotation.Inject;

import com.dianping.dobby.DobbyConstants;
import com.dianping.dobby.email.MessageHandler;
import com.dianping.dobby.email.MessagePayload;
import com.dianping.dobby.ticket.model.entity.Ticket;

public class TicketMessageHandler implements MessageHandler, DobbyConstants {
   public static final String ID = ID_TICKET;

   @Inject
   private TicketProcessor m_processor;

   @Inject
   private TicketManager m_manager;

   @Override
   public String getId() {
      return ID;
   }

   @Override
   public void handle(MessagePayload payload) throws Exception {
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
}
