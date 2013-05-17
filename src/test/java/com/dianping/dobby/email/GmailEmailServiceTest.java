package com.dianping.dobby.email;

import javax.mail.internet.InternetAddress;

import org.junit.Test;
import org.unidal.lookup.ComponentTestCase;

import com.dianping.dobby.DobbyConstants;
import com.site.helper.Files;

public class GmailEmailServiceTest extends ComponentTestCase {
   @Test
   public void test() throws Exception {
      EmailService emailService = lookup(EmailService.class, DobbyConstants.ID_BOOK);
      InternetAddress[] to = InternetAddress.parse("yong.you@dianping.com,qimin.wu@dianping.com");
      String content = Files.forIO().readFrom(getClass().getResourceAsStream("html.html"), "utf-8");

      emailService.send(to, null, "中国1234", null, content);
   }
}
