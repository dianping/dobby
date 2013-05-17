package com.dianping.tool.dobby.mail;

import javax.mail.BodyPart;
import javax.mail.Multipart;
import javax.mail.internet.MimeMultipart;

public class ContentBuilder {
	public String build(Object messageContent) {
		if (messageContent instanceof MimeMultipart) {
			try {
				Multipart parts = (MimeMultipart) messageContent;
				int count = parts.getCount();
				String html = null;
				String plain = null;

				for (int i = 0; i < count; i++) {
					BodyPart part = parts.getBodyPart(i);
					String contentType = part.getContentType().toLowerCase();

					if (contentType.contains("text/html")) {
						html = (String) part.getContent();
					} else if (contentType.contains("text/plain")) {
						plain = (String) part.getContent();
					}
				}

				return html != null ? html : plain;
			} catch (Exception e) {
				throw new RuntimeException(String.format("Error when extracting content: %s!", messageContent), e);
			}
		} else {
			return String.valueOf(messageContent);
		}
	}
}
