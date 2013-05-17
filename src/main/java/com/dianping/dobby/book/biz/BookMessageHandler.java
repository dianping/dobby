package com.dianping.dobby.book.biz;

import org.unidal.lookup.annotation.Inject;

import com.dianping.dobby.DobbyConstants;
import com.dianping.dobby.email.EmailService;
import com.dianping.dobby.email.MessageHandler;
import com.dianping.dobby.email.MessagePayload;

public class BookMessageHandler implements MessageHandler, DobbyConstants {
   public static final String ID = ID_BOOK;

   @Inject(ID_BOOK)
   private EmailService m_service;
   
   @Inject
   private BookManager m_manager;

   @Override
   public String getId() {
      return ID;
   }

   @Override
   public void handle(MessagePayload payload) throws Exception {
      BookCommand cmd = BookCommand.getByName(payload.getCommand(), BookCommand.HELP);
      BookCommandContext ctx = new BookCommandContext(m_service, m_manager, payload);

      cmd.execute(ctx);
   }
}
