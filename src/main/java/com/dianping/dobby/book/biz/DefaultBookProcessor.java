package com.dianping.dobby.book.biz;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.time.DateUtils;
import org.unidal.lookup.ContainerHolder;
import org.unidal.lookup.annotation.Inject;

import com.dianping.dobby.book.model.entity.Book;
import com.dianping.dobby.book.model.entity.Borrow;

public class DefaultBookProcessor extends ContainerHolder implements BookProcessor{
	
	@Inject
	BookManager manager;

	@Override
   public Collection<Book> listAllAvailbleBooks() {
	   return manager.findAllBooks();
   }

	@Override
   public void process(int bookId, String by, String cmd, int expireDuration) throws Exception {
		Book book = manager.findBookById(bookId);
		BorrowState state = null;
		BorrowState nextState = null;
		Error error = Error.INPUT_COMMAND_ERROR;
		Borrow borrow = getBorrowByStatus(book.getBorrowHistory(), by, BorrowState.BORROWING);
		if(cmd.equals(BookCmds.BORROW)){
			if(borrow == null){
				state = BorrowState.RETURNED;
				nextState = BorrowState.BORROWING;
				addNewBorrowItemToHistory(by, book, nextState,expireDuration);
			}
			error = Error.BORROW_SAME_BOOK_ALREADY_BORROWED;
		}
		else if(cmd.equals(BookCmds.RETURN)){
			if(borrow != null){
				state = BorrowState.BORROWING;
				nextState = BorrowState.RETURNED;
				borrow.setReturnTime(new Date(System.currentTimeMillis()));
				borrow.setStatus(nextState.name());
			}
			error = Error.RETURN_BOOK_NOT_BORROWED;
		}
		
		DefaultBorrowContext ctx = createBookContext(book);
		if(state == null && nextState == null ){
			ctx.setErrorMessage(error);
		}
		
		try {
			state.moveTo(ctx, nextState);
		} finally {
			release(ctx);
		}	
   }
	
	private Borrow getBorrowByStatus(List<Borrow> list, String by, BorrowState state){
		for(Borrow b: list){
			if(b.getBorrower().equals(by) && BorrowState.getByName(b.getStatus(), null) == state){
				return b;
			}
		}
		return null;
	}

	private void addNewBorrowItemToHistory(String by, Book book, BorrowState nextState, int expireDuration) {
	   List<Borrow> borrowList = book.getBorrowHistory();
	   Borrow borrow = new Borrow();
	   borrow.setBorrower(by);
	   borrow.setStatus(nextState.name());
	   Date currentTime = new Date(System.currentTimeMillis());
	   borrow.setBorrowTime(currentTime);
	   Date expiredTime = DateUtils.addDays(currentTime, expireDuration);
	   borrow.setExpiredTime(expiredTime);
	   borrowList.add(borrow);
   }
	
	private DefaultBorrowContext createBookContext(Book book){
		DefaultBorrowContext ctx = (DefaultBorrowContext)lookup(BorrowContext.class);
		ctx.setBook(book);
		return ctx;
		
	}

	@Override
   public void help() {
	   // TODO
   }


}
