package com.dianping.dobby.book.biz;

import com.dianping.dobby.command.AbstractCommandContext;
import com.dianping.dobby.email.EmailService;
import com.dianping.dobby.email.MessagePayload;

public class BookCommandContext extends AbstractCommandContext {
   private MessagePayload m_payload;

   private BookManager m_manager;

   public BookCommandContext(EmailService service, BookManager manager, MessagePayload payload) {
      super(service);

      m_manager = manager;
      m_payload = payload;
   }

   public int getIntArg(int index, int defaultValue) {
      String[] params = m_payload.getCommandParams();

      try {
         Integer.parseInt(params[index]);
      } catch (Exception e) {
         // ignore it
      }

      return defaultValue;
   }

   public MessagePayload getPayload() {
      return m_payload;
   }

   public BookManager getManager() {
      return m_manager;
   }

   public String getFrom() {
      return m_payload.getFrom();
   }

   public void notify(BookMessageId id, Object... args) {
      switch (id) {
      case BOOK_NOT_FOUND:
         // TODO
         break;
      }
   }
}
