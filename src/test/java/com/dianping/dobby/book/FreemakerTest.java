package com.dianping.dobby.book;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.codehaus.plexus.personality.plexus.lifecycle.phase.InitializationException;
import org.junit.Test;

import com.dianping.dobby.book.freemarker.Render;
import com.dianping.dobby.book.model.entity.Book;
import com.dianping.dobby.book.model.entity.Borrow;

public class FreemakerTest {

	@Test
	public void test() throws InitializationException {
		Render render = new Render();
		render.initialize();
		List<Book> books = new ArrayList<Book>();

		for (int i = 0; i < 10; i++) {
			books.add(createBook());
		}
		System.out.println("========================1");
		System.out.println(render.renderList(books));
		System.out.println("========================2");
		System.out.println(render.renderDetail(createBook()));
		System.out.println("========================3");
		System.out.println(render.renderBorrow(new Date()));
		System.out.println("========================4");
		System.out.println(render.renderReturn());
	}

	private Book createBook() {
		Book b = new Book();
		b.setAuthor("老吴");
		b.setCategory("技术");
		b.setCreatedDate(new Date());
		b.setDescription("20年精通java");
		b.setId(1);
		b.setPress("点评出版社");
		b.setTitle("老吴教写代码");
		b.setTotal(10);
		b.setRemaining(5);

		for (int i = 0; i < 5; i++) {
			Borrow br = new Borrow();

			br.setBorrower("尤勇");
			br.setBorrowTime(new Date());
			b.addBorrow(br);
		}
		return b;
	}
}
