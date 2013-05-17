package com.dianping.dobby.email;

public interface MessageHandler {
   public String getId();

   public void handle(MessagePayload payload) throws Exception;
}
