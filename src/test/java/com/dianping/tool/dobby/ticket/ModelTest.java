package com.dianping.tool.dobby.ticket;

import java.io.InputStream;

import junit.framework.Assert;

import org.junit.Test;
import org.unidal.helper.Files;

import com.dianping.tool.dobby.model.entity.Model;
import com.dianping.tool.dobby.model.transform.DefaultSaxParser;

public class ModelTest {
	@Test
	public void test() throws Exception {
		InputStream in = getClass().getResourceAsStream("ticket.xml");
		String xml = Files.forIO().readFrom(in, "utf-8");
		Model model = DefaultSaxParser.parse(xml);

		Assert.assertEquals(xml, model.toString());
	}
}
