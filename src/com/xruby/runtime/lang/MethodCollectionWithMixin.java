package com.xruby.runtime.lang;

import java.util.*;

public class MethodCollectionWithMixin extends MethodCollection {
	private ArrayList<RubyModule> mixins_ = new ArrayList<RubyModule>();

	public void includeModule(RubyModule m) {
		if (!mixins_.contains(m)) {
			mixins_.add(0, m);
		}
	}

	RubyMethod findMethod(String method_name) {
		RubyMethod m = super.findMethod(method_name);
		if (null != m) {
			return m;
		}
		
		for (RubyModule module : mixins_) {
			m = module.findMethod(method_name);
			if (null != m) {
				return m;
			}
		}
		
		return null;
	}

	RubyMethod findPublicMethod(String method_name) {
		RubyMethod m = super.findMethod(method_name);
		if (null != m && m.isPublic()) {
			return m;
		}
		
		for (RubyModule module : mixins_) {
			m = module.findMethod(method_name);
			if (null != m && m.isPublic()) {
				return m;
			}
		}
		
		return null;
	}

	public RubyValue getClassVariable(String name) throws RubyException {
		RubyValue v = super.findClassVariable(name);
		if (null != v) {
			return v;
		}

		for (RubyModule module : mixins_) {
			v = module.findClassVariable(name);
			if (null != v) {
				return v;
			}
		}

		throw new RubyException(RubyRuntime.NameErrorClass,
					"uninitialized class variable " + name + " in " +  getName());
	}
}