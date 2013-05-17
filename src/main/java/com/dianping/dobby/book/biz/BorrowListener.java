package com.dianping.dobby.book.biz;

public interface BorrowListener {
	public void beforeStateChange(BorrowContext ctx) throws Exception;
	public void afterStateChange(BorrowContext ctx) throws Exception;
}
