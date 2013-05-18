package com.dianping.dobby.view;

import java.io.StringWriter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.codehaus.plexus.personality.plexus.lifecycle.phase.Initializable;
import org.codehaus.plexus.personality.plexus.lifecycle.phase.InitializationException;

import com.dianping.cat.Cat;

import freemarker.template.Configuration;
import freemarker.template.Template;

public class FreeMarkerView implements Initializable {
   private Configuration m_configuration;

   @Override
   public void initialize() throws InitializationException {
      m_configuration = new Configuration();
      m_configuration.setDefaultEncoding("UTF-8");

      try {
         m_configuration.setClassForTemplateLoading(FreeMarkerView.class, "/freemaker");
      } catch (Exception e) {
         Cat.logError(e);
      }
   }

   public String render(String templateName, Object... pairs) {
      int len = pairs.length;

      if (len % 2 != 0) {
         throw new IllegalArgumentException(String.format("Argument(%s) must be paired!", Arrays.asList(pairs)));
      }

      try {
         Template t = m_configuration.getTemplate(templateName);
         Map<Object, Object> root = new HashMap<Object, Object>();
         StringWriter writer = new StringWriter(2048);

         for (int i = 0; i < len; i += 2) {
            root.put(pairs[i], pairs[i + 1]);
         }

         t.process(root, writer);

         return writer.toString();
      } catch (Throwable e) {
         Cat.logError(e);
      }

      return "";
   }
}
