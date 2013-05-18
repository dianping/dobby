package com.dianping.dobby.view;

import junit.framework.Assert;

import org.junit.Test;
import org.unidal.lookup.ComponentTestCase;

public class FreeMarkerViewTest extends ComponentTestCase {
   @Test
   public void test() throws Exception {
      FreeMarkerView view = lookup(FreeMarkerView.class);
      String actual = view.render("test.ftl");

      Assert.assertEquals("This is a test view.", actual);
   }
}
