package com.dianping.dobby.mail;

import junit.framework.Assert;

import org.junit.Test;
import org.unidal.lookup.ComponentTestCase;

import com.dianping.dobby.email.MessageParser;

public class MessageParserTest extends ComponentTestCase {
	@Test
	public void testParseId() throws Exception {
		MessageParser parser = lookup(MessageParser.class);

		Assert.assertEquals("TKT001", parser.parseId("Re: Re: Fwd: [ TKT001 ] [MobileApi] Too many problem"));
	}

	@Test
	public void testParseSubject() throws Exception {
		MessageParser parser = lookup(MessageParser.class);

		Assert.assertEquals("[ TKT001 ] [MobileApi] Too many problem",
		      parser.parseSubject("Re: Re: Fwd: [ TKT001 ] [MobileApi] Too many problem"));
	}
}
