package com.dianping.dobby.mail;

import javax.mail.Address;

public interface MailService {
	public void close();

	public void markRead(int messageId) throws Exception;

	public void send(Address[] to, Address[] cc, String subject, String content, String htmlContent) throws Exception;
}
