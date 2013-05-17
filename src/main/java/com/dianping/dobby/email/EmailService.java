package com.dianping.dobby.email;

import javax.mail.Address;

public interface EmailService {
   public void close();

   public void markRead(int... messageIds) throws Exception;

   public void pollUnread() throws Exception;

   public void send(Address[] to, Address[] cc, String subject, String content, String htmlContent) throws Exception;
}
