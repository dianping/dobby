package com.dianping.tool.dobby.mail;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.internet.InternetAddress;

import org.unidal.helper.Splitters;
import org.unidal.lookup.annotation.Inject;
import org.unidal.tuple.Pair;

public class DefaultMessageParser implements MessageParser {
	private static final String[] SUBJECT_PREFIXES = { "Re:", "Fwd:", "»Ø¸´:", "×ª·¢:" };

	@Inject
	private ContentBuilder m_builder;

	private Map<String, String> buildNameToEmailMap(Address[] addresses) {
		// name => email
		Map<String, String> map = new HashMap<String, String>();

		for (Address address : addresses) {
			if (address instanceof InternetAddress) {
				InternetAddress ia = (InternetAddress) address;
				String name = ia.getPersonal();
				String email = ia.getAddress();
				int pos = email.indexOf('@');

				if (name != null && name.length() > 0) {
					map.put(name.toLowerCase(), email);
				}

				map.put(email.substring(0, pos), email);
			}
		}

		return map;
	}

	private String getFirstLine(String content) {
		StringBuilder sb = new StringBuilder(64);
		int len = content == null ? 0 : content.length();

		for (int i = 0; i < len; i++) {
			char ch = content.charAt(i);

			switch (ch) {
			case '\r':
			case '\n':
				return sb.toString();
			case '<': // strip HTML tags
				int off = i;

				while (i + 1 < len) {
					i++;

					if (content.charAt(i) == '>') {
						if ("br".equalsIgnoreCase(content.substring(off + 1, i))) {
							return sb.toString();
						}
						
						break;
					}
				}
				break;
			default:
				sb.append(ch);
				break;
			}
		}

		return sb.toString();
	}

	@Override
	public MessagePayload parse(Message message) {
		try {
			MessagePayload payload = new MessagePayload();

			int num = message.getMessageNumber();
			Address[] fromAddresses = message.getFrom();
			String rawSubject = message.getSubject();
			String subject = parseSubject(rawSubject);
			String id = parseId(subject);
			String from = parseFromAddress(fromAddresses);
			String content = m_builder.build(message.getContent());

			Pair<String, String[]> result = new Pair<String, String[]>();

			parseCommand(message.getAllRecipients(), content, result);

			payload.setNum(num);
			payload.setId(id);
			payload.setSubject(subject);
			payload.setFrom(from);
			payload.setCommand(result.getKey());
			payload.setCommandParams(result.getValue());
			payload.setComment(content);

			return payload;
		} catch (Exception e) {
			throw new RuntimeException(String.format("Error when parsing mail message: %s!", message), e);
		}
	}

	@Override
	public void parseCommand(Address[] addresses, String content, Pair<String, String[]> result) {
		String line = getFirstLine(content);
		List<String> parts = Splitters.by(' ').noEmptyItem().split(line);

		if (!parts.isEmpty()) {
			String first = parts.remove(0);

			if (first.startsWith("@@")) {
				String cmd = first.substring(2);

				if (cmd.length() > 0) {
					result.setKey(cmd);
					result.setValue(parts.toArray(new String[0]));
				}
			} else if (first.startsWith("@")) {
				String name = first.substring(1).toLowerCase();
				Map<String, String> map = buildNameToEmailMap(addresses);
				String email = map.get(name);

				if (email == null) {
					String prefix1 = name + ".";
					String prefix2 = name + " ";

					for (Map.Entry<String, String> e : map.entrySet()) {
						String key = e.getKey();

						if (key.startsWith(prefix1) || key.startsWith(prefix2)) {
							email = e.getValue();
							break;
						} else if (key.indexOf('.') < 0 && key.endsWith(name)) {
							email = e.getValue();
							break;
						}
					}
				}

				if (email != null) {
					result.setKey("assignTo");
					result.setValue(new String[] { email });
				}
			}
		}
	}

	@Override
	public String parseFromAddress(Address[] addresses) {
		if (addresses != null) {
			for (Address address : addresses) {
				if (address instanceof InternetAddress) {
					return ((InternetAddress) address).getAddress();
				}
			}
		}

		return null;
	}

	@Override
	public String parseId(String subject) {
		if (subject != null) {
			int off = subject.indexOf('[');
			int pos = subject.indexOf(']', off + 1);

			if (off >= 0 && pos > 0) {
				return subject.substring(off + 1, pos).trim();
			}
		}

		return null;
	}

	@Override
	public String parseSubject(String rawSubject) {
		if (rawSubject == null) {
			return null;
		}

		String subject = rawSubject;

		while (true) {
			boolean matched = false;

			for (String prefix : SUBJECT_PREFIXES) {
				int len = prefix.length();

				if (subject.regionMatches(true, 0, prefix, 0, len)) {
					subject = subject.substring(len).trim();
					matched = true;
					break;
				}
			}

			if (!matched) {
				break;
			}
		}

		return subject;
	}
}
