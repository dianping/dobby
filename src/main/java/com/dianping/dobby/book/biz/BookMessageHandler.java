package com.dianping.dobby.book.biz;

import com.dianping.dobby.DobbyConstants;
import com.dianping.dobby.email.MessageHandler;
import com.dianping.dobby.email.MessagePayload;

public class BookMessageHandler implements MessageHandler, DobbyConstants {
   public static final String ID = ID_BOOK;

   @Override
   public String getId() {
      return ID;
   }

   @Override
   public void handle(MessagePayload payload) throws Exception {
      System.out.println(payload);
   }
}
