package com.dianping.dobby.book.biz;

public class DefaultBorrowListener implements BorrowListener{

	@Override
   public void beforeStateChange(BorrowContext ctx) throws Exception { 
		//do nothing
   }

	@Override
   public void afterStateChange(BorrowContext ctx) throws Exception {
	   BorrowState state = ctx.getState();
	   switch(state){
	   case RETURNED:
	   	//send book returned successfully email
	   	;
	   case BORROWING:
	   	//send book borrowed successfully email
	   	;
	   }
	   
   }

}
