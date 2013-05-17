package com.dianping.dobby.book.biz;

import java.io.File;
import java.io.IOException;
import java.util.Collection;

import org.codehaus.plexus.personality.plexus.lifecycle.phase.Initializable;
import org.codehaus.plexus.personality.plexus.lifecycle.phase.InitializationException;
import org.unidal.helper.Files;

import com.dianping.dobby.book.model.entity.Book;
import com.dianping.dobby.book.model.entity.BookModel;
import com.dianping.dobby.book.model.transform.DefaultSaxParser;

public class DefaultBookManager implements BookManager, Initializable {
   private File m_modelFile;

   private BookModel m_model;

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
         throw new InitializationException("Unable to load book.xml!", e);
      }
   }

   @Override
   public void persistent(Book book) throws IOException {
      Files.forIO().writeTo(m_modelFile, book.toString());
   }

   @Override
   public Collection<Book> findAllBooks() {
      return m_model.getBooks().values();
   }
}
