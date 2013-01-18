package com.dianping.tool.dobby.build;

import java.util.ArrayList;
import java.util.List;

import com.dianping.tool.dobby.conosle.ConosleModule;

import org.unidal.lookup.configuration.Component;
import org.unidal.web.configuration.AbstractWebComponentsConfigurator;

class WebComponentConfigurator extends AbstractWebComponentsConfigurator {
	@SuppressWarnings("unchecked")
	@Override
	public List<Component> defineComponents() {
		List<Component> all = new ArrayList<Component>();

		defineModuleRegistry(all, ConosleModule.class, ConosleModule.class);

		return all;
	}
}
