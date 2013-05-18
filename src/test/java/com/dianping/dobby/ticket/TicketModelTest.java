package com.dianping.dobby.ticket;

import java.io.InputStream;

import junit.framework.Assert;

import org.junit.Test;
import org.unidal.helper.Files;

import com.dianping.dobby.ticket.model.entity.Model;
import com.dianping.dobby.ticket.model.transform.DefaultSaxParser;

public class TicketModelTest {
	@Test
	public void test() throws Exception {
		InputStream in = getClass().getResourceAsStream("ticket.xml");
		String xml = Files.forIO().readFrom(in, "utf-8");
		Model model = DefaultSaxParser.parse(xml);

		Assert.assertEquals(xml.replaceAll("\r", ""), model.toString().replaceAll("\r", ""));
	}
}
