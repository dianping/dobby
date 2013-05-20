package com.dianping.dobby.book.biz;

import java.io.File;
import java.io.IOException;
import java.util.Collection;

import org.codehaus.plexus.personality.plexus.lifecycle.phase.Initializable;
import org.codehaus.plexus.personality.plexus.lifecycle.phase.InitializationException;
import org.unidal.helper.Files;

import com.dianping.cat.Cat;
import com.dianping.dobby.book.model.entity.Book;
import com.dianping.dobby.book.model.entity.BookModel;
import com.dianping.dobby.book.model.transform.DefaultSaxParser;

public class DefaultBookManager implements BookManager, Initializable {
   private File m_modelFile;

   private BookModel m_model;

   @Override
   public Collection<Book> findAllBooks() {
      return m_model.getBooks().values();
   }

   @Override
   public Book findBookById(int id) {
      return m_model.findBook(id);
   }

   @Override
   public void initialize() throws InitializationException {
      try {
         m_modelFile = new File("book.xml").getCanonicalFile();

         if (m_modelFile.canRead()) {
            String xml = Files.forIO().readFrom(m_modelFile, "utf-8");

            m_model = DefaultSaxParser.parse(xml);
         } else {
            m_model = new BookModel();
         }
      } catch (Exception e) {
         Cat.logError(e);

         throw new InitializationException(String.format("Unable to load books from %s!", m_modelFile), e);
      }
   }

   @Override
   public void save(Book book) {
      try {
      	m_model.addBook(book);
         Files.forIO().writeTo(m_modelFile, m_model.toString());
      } catch (IOException e) {
         Cat.logError(e);
      }
   }

	@Override
   public BookModel getModel() {
	   return m_model;
   }
}
