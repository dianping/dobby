package com.dianping.dobby.email;

import org.unidal.helper.Threads.Task;
import org.unidal.lookup.annotation.Inject;

import com.dianping.cat.Cat;
import com.dianping.cat.message.Transaction;

public class EmailDispatcher implements Task {
   @Inject
   private MessageQueue m_queue;

   @Inject
   private MessageHandler m_handler;

   @Inject
   private EmailService m_service;
   
   @Override
   public String getName() {
      return getClass().getSimpleName();
   }

   @Override
   public void run() {
      while (true) {
         MessagePayload payload = m_queue.poll();

         if (payload != null) {
            Transaction t = Cat.newTransaction(getClass().getSimpleName(), m_handler.getId());

            try {
               m_handler.handle(payload);
               m_service.markRead(payload.getNum());
               
               t.setStatus(Transaction.SUCCESS);
            } catch (Exception e) {
               e.printStackTrace();
               Cat.logError(e);
               t.setStatus(e);
            } finally {
               t.complete();
            }
         }
      }
   }

   @Override
   public void shutdown() {
   }
}