package com.dianping.tool.dobby.mail;

import javax.mail.Address;
import javax.mail.Message;

import org.unidal.tuple.Pair;

public interface MessageParser {
	public MessagePayload parse(Message message);

	public void parseCommand(Address[] addresses, String content, Pair<String, String[]> result);

	public String parseFromAddress(Address[] addresses);

	public String parseId(String subject);

	public String parseSubject(String rawSubject);
}
