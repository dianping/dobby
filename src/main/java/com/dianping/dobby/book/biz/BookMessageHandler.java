package com.dianping.dobby.book.biz;

import org.unidal.lookup.annotation.Inject;

import com.dianping.dobby.DobbyConstants;
import com.dianping.dobby.email.MessageHandler;
import com.dianping.dobby.email.MessagePayload;

public class BookMessageHandler implements MessageHandler, DobbyConstants {
   public static final String ID = ID_BOOK;

   @Inject
   private BookManager m_manager;

   @Inject
   private BookProcessor m_processor;

   @Override
   public String getId() {
      return ID;
   }

   @Override
   public void handle(MessagePayload payload) throws Exception {
      System.out.println(payload);
      
      System.out.println(m_manager);
      System.out.println(m_processor);
   }
}
