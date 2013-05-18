package com.dianping.dobby;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import com.dianping.dobby.book.BookModelTest;
import com.dianping.dobby.email.MessageParserTest;
import com.dianping.dobby.ticket.TicketModelTest;
import com.dianping.dobby.ticket.TicketProcessorTest;
import com.dianping.dobby.view.FreeMarkerViewTest;

@RunWith(Suite.class)
@SuiteClasses({

BookModelTest.class,

MessageParserTest.class,

TicketModelTest.class,

TicketProcessorTest.class,

FreeMarkerViewTest.class

})
public class AllTests {

}
