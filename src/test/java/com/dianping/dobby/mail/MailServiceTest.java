package com.dianping.dobby.mail;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.commons.mail.EmailException;
import org.junit.Test;
import org.unidal.lookup.ComponentTestCase;

import com.site.helper.Files;

public class MailServiceTest extends ComponentTestCase{

	@Test
	public void test() throws EmailException, IOException{
		GmailService service = new GmailService(new LinkedBlockingQueue<Payload>());
		
		List<String> tos = Arrays.asList("yong.you@dianping.com","youyong205@126.com","qimin.wu@dianping.com");
		//service.sendMail("中国", "中国", tos, tos);
		String content = Files.forIO().readFrom(getClass().getResourceAsStream("html.html"), "utf-8");
		System.out.println(content);
		service.sendHtmlMail("中国12", content , tos, tos);
	}
	
}
