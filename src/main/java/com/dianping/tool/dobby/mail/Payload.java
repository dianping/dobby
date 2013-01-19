package com.dianping.tool.dobby.mail;

import java.util.Arrays;
import java.util.List;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMultipart;

import org.unidal.helper.Splitters;

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
					res.setId(subject.substring(startPos + 1, endPos));
				}
			}
		}

		res.setFrom(((InternetAddress) msg.getFrom()[0]).getAddress());

		String content = parseMailContent(msg.getContent());
		List<String> lines = Splitters.by("\n").noEmptyItem().split(content);
		if (lines != null && lines.size() > 0) {
			parseCommand(msg, res, lines);

			parseComment(res, lines);
		}

		String escapedSubject = subject.substring(subject.lastIndexOf("Re: ") + "Re: ".length());
		res.setSubject(escapedSubject);

		return res;
	}

	private static void parseComment(Payload res, List<String> lines) {
	   int i = 0;
	   for (i = 0; i < lines.size(); i++) {
	   	if (lines.get(i) == null) {
	   		continue;
	   	}

	   	String trimmed = lines.get(i).trim();
	   	if (!trimmed.startsWith("@") && (trimmed.endsWith(":") || trimmed.endsWith("："))) {
	   		if (lines.get(i + 1) != null && lines.get(i + 1).startsWith(">")) {
	   			break;
	   		}
	   	}
	   }

	   StringBuilder sb = new StringBuilder();
	   for (int j = 0; j < i; j++) {
	   	if (lines.get(j) != null && !lines.get(j).trim().startsWith("@")) {
	   		sb.append(lines.get(j)).append("\n");
	   	}
	   }
	   res.setComment(sb.toString());
   }

	private static void parseCommand(Message msg, Payload res, List<String> lines) throws Exception, MessagingException {
		for (String line : lines) {
			if (line != null && line.trim().length() > 0) {
				if (line.trim().charAt(0) == '@') {

					if (line.trim().charAt(1) == '@') {
						List<String> args = Splitters.by(" ").noEmptyItem().split(line.substring(2));
						if (args != null && !args.isEmpty()) {
							res.setCommand(args.get(0));

							String[] commandParams = args.subList(1, args.size()).toArray(new String[0]);
							res.setCommandParams(commandParams);
						}
					} else {
						List<String> args = Splitters.by(" ").noEmptyItem().split(line.trim().substring(1));
						res.setCommand("assignTo");
						String[] params = new String[args.size()];
						params[0] = convertAssignNameToEmail(args.get(0), msg.getAllRecipients());
						for (int i = 1; i < args.size(); i++) {
							params[i] = args.get(i);
						}
						res.setCommandParams(params);
					}
					break;
				}
			}
		}
	}

	private static String convertAssignNameToEmail(String assignToName, Address[] addresses) throws Exception {
		if (addresses != null) {
			for (Address address : addresses) {
				if (address instanceof InternetAddress) {
					InternetAddress ia = (InternetAddress) address;
					if (assignToName.equals(ia.getPersonal())) {
						return ia.getAddress();
					}
				}
			}
		}
		return null;
	}

	private static String parseMailContent(Object content) {
		StringBuilder sb = new StringBuilder();
		try {
			if (content instanceof Multipart) {
				Multipart mPart = (MimeMultipart) content;
				for (int i = 0; i < mPart.getCount(); i++) {
					extractPart(sb, (MimeBodyPart) mPart.getBodyPart(i));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sb.toString();
	}

	/**
	 * 抽取内容
	 * 
	 * @param part
	 */
	private static void extractPart(StringBuilder sb, MimeBodyPart part) {
		try {
			String disposition = part.getDisposition();

			if (disposition != null
			      && (disposition.equalsIgnoreCase(Part.ATTACHMENT) || disposition.equalsIgnoreCase(Part.INLINE))) {// 附件
				// attachment
			} else {// 正文
				if (part.getContent() instanceof String) {// 接收到的纯文本
					sb.append(part.getContent().toString());
				}
				if (part.getContent() instanceof MimeMultipart) {// 接收的邮件有附件时
					// BodyPart bodyPart = ((MimeMultipart) part.getContent()).getBodyPart(0);
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
