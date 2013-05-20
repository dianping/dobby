package com.dianping.dobby.email;

import javax.mail.internet.InternetAddress;

public interface EmailChannel {
   public void close();

   public String getAddress();

   public void markRead(int... messageIds) throws Exception;

   public void pollUnread() throws Exception;

   public void send(InternetAddress[] to, InternetAddress[] cc, String originalMessageId, String subject, String content, String htmlContent)
         throws Exception;
}
