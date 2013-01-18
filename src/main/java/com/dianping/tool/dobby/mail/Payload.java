package com.dianping.tool.dobby.mail;

import java.util.Arrays;

import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMultipart;

public class Payload {
	private int num;

	private String id;

	private String subject;

	private String from;

	private String comment;

	private String command;

	private String[] commandParams;

	public static Payload valueOf(Message msg) throws Exception {
		Payload res = new Payload();

		String subject = msg.getSubject();
		res.setNum(msg.getMessageNumber());

		if (subject != null && subject.trim().length() != 0) {
			int startPos = subject.indexOf('[');
			if (startPos >= 0) {
				int endPos = subject.indexOf(']');
				if (endPos >= 0 && endPos >= startPos) {
					res.setId(subject.substring(startPos, endPos));
				}
			}
		}

		res.setFrom(((InternetAddress) msg.getFrom()[0]).getAddress());
		
		parseMailContent(msg.getContent());
		
		res.setSubject(subject);

		return res;
	}
	
	private static void parseMailContent(Object content) {
		try {
			if (content instanceof Multipart) {
				Multipart mPart = (MimeMultipart) content;
				for (int i = 0; i < mPart.getCount(); i++) {
					extractPart((MimeBodyPart) mPart.getBodyPart(i));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 抽取内容
	 * 
	 * @param part
	 */
	public static void extractPart(MimeBodyPart part) {
		try {
			String disposition = part.getDisposition();

			if (disposition != null
			      && (disposition.equalsIgnoreCase(Part.ATTACHMENT) || disposition.equalsIgnoreCase(Part.INLINE))) {// 附件
				// attachment
			} else {// 正文
				if (part.getContent() instanceof String) {// 接收到的纯文本
					System.out.println(part.getContent());
				}
				if (part.getContent() instanceof MimeMultipart) {// 接收的邮件有附件时
					BodyPart bodyPart = ((MimeMultipart) part.getContent()).getBodyPart(0);
					System.out.println(bodyPart.getContent());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Payload() {
		super();
	}

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getCommand() {
		return command;
	}

	public void setCommand(String command) {
		this.command = command;
	}

	public String[] getCommandParams() {
		return commandParams;
	}

	public void setCommandParams(String[] commandParams) {
		this.commandParams = commandParams;
	}

	@Override
	public String toString() {
		return "Payload [num=" + num + ", id=" + id + ", subject=" + subject + ", from=" + from + ", comment=" + comment
		      + ", command=" + command + ", commandParams=" + Arrays.toString(commandParams) + "]";
	}

}
