package com.dianping.dobby.book;

import java.io.InputStream;

import org.junit.Test;
import org.unidal.helper.Files;

import com.dianping.dobby.book.model.entity.BookModel;
import com.dianping.dobby.book.model.transform.DefaultSaxParser;

public class BookModelTest {
	@Test
	public void test() throws Exception {
		InputStream in = getClass().getResourceAsStream("book.xml");
		String xml = Files.forIO().readFrom(in, "utf-8");
		BookModel model = DefaultSaxParser.parse(xml);
		
		System.out.println(model);
	}
}
