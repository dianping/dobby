package com.dianping.dobby.email;

import java.util.Arrays;

public class MessagePayload {
	private int num;

	private String m_id;

	private String m_subject;

	private String m_from;

	private String m_comment;

	private String m_command;

	private String[] m_commandParams;

	public String getCommand() {
		return m_command;
	}

	public String[] getCommandParams() {
		return m_commandParams;
	}

	public String getComment() {
		return m_comment;
	}

	public String getFrom() {
		return m_from;
	}

	public String getId() {
		return m_id;
	}

	public int getNum() {
		return num;
	}

	public String getSubject() {
		return m_subject;
	}

	public void setCommand(String command) {
		m_command = command;
	}

	public void setCommandParams(String[] commandParams) {
		m_commandParams = commandParams;
	}

	public void setComment(String comment) {
		m_comment = comment;
	}

	public void setFrom(String from) {
		m_from = from;
	}

	public void setId(String id) {
		m_id = id;
	}

	public void setNum(int num) {
		this.num = num;
	}

	public void setSubject(String subject) {
		m_subject = subject;
	}

	@Override
	public String toString() {
		return "Payload [num=" + num + ", id=" + m_id + ", subject=" + m_subject + ", from=" + m_from + ", comment="
		      + m_comment + ", command=" + m_command + ", commandParams=" + Arrays.toString(m_commandParams) + "]";
	}
}
