package com.dianping.tool.dobby;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import com.dianping.tool.dobby.ticket.ModelTest;
import com.dianping.tool.dobby.ticket.TicketProcessorTest;

@RunWith(Suite.class)
@SuiteClasses({

ModelTest.class,

TicketProcessorTest.class

})
public class AllTests {

}
