package com.dianping.dobby.email;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.codehaus.plexus.personality.plexus.lifecycle.phase.Initializable;
import org.codehaus.plexus.personality.plexus.lifecycle.phase.InitializationException;
import org.unidal.lookup.annotation.Inject;

import com.dianping.cat.Cat;

public class MessageQueue implements Initializable {
   @Inject
   private int m_capacity = 100;

   private BlockingQueue<MessagePayload> m_queue;

   @Override
   public void initialize() throws InitializationException {
      m_queue = new LinkedBlockingQueue<MessagePayload>(m_capacity);
   }

   public boolean offer(MessagePayload payload) {
      return m_queue.offer(payload);
   }

   public MessagePayload poll() {
      try {
         MessagePayload payload = m_queue.poll(5, TimeUnit.MILLISECONDS);

         return payload;
      } catch (Exception e) {
         Cat.logError(e);
      }

      return null;
   }

   public void setCapacity(int capacity) {
      m_capacity = capacity;
   }
}
