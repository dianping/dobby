package com.dianping.dobby.email;

import javax.mail.internet.InternetAddress;

import org.junit.Test;
import org.unidal.helper.Files;
import org.unidal.lookup.ComponentTestCase;

import com.dianping.dobby.DobbyConstants;

public class GmailEmailChannelTest extends ComponentTestCase {
   @Test
   public void test() throws Exception {
      EmailChannel emailService = lookup(EmailChannel.class, DobbyConstants.ID_BOOK);
      InternetAddress[] to = InternetAddress.parse("yong.you@dianping.com,qimin.wu@dianping.com");
      String content = Files.forIO().readFrom(getClass().getResourceAsStream("email1.html"), "utf-8");

      emailService.send(to, null, null, "中国1234", null, content);
   }
}
