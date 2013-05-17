package com.dianping.dobby.email;

import javax.mail.BodyPart;
import javax.mail.Multipart;
import javax.mail.internet.MimeMultipart;

public class MessageContentExtractor {
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

            if (html != null) {
               int pos = html.indexOf("<div class=\"gmail_extra\">");

               if (pos > 0) {
                  return html.substring(0, pos);
               } else {
                  return html;
               }
            } else {
               return plain;
            }
         } catch (Exception e) {
            throw new RuntimeException(String.format("Error when extracting content: %s!", messageContent), e);
         }
      } else {
         return String.valueOf(messageContent);
      }
   }
}
