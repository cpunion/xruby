/** 
 * Copyright (c) 2005-2006 Xue Yong Zhi. All rights reserved.
 */

package com.xruby.runtime.builtin;

import com.xruby.runtime.lang.*;
import com.xruby.runtime.value.*;

class Module_AccessControl {

	static RubyValue run(int access, RubyValue receiver, RubyArray args, RubyBlock block) {

		RubyModule c = (RubyModule)receiver;

		if (null == args) {
			c.setAccessMode(access);
			return receiver;
		}
		
		for (RubyValue arg : args) {
			String method_name;
			if (arg instanceof RubyString) {
				method_name = ((RubyString)arg).toString();
			} else if (arg instanceof RubySymbol) {
				method_name = ((RubySymbol)arg).toString();
			} else {
				throw new RubyException(RubyRuntime.TypeErrorClass, arg.toString() + " is not a symbol");
			}
			
			if (c.setAccess(method_name, access) == null) {
				throw new RubyException(RubyRuntime.NameErrorClass, "undefined method `"+ method_name + "` for class `" + c.getName() + "`");
			}
		}
		
		return receiver;
	}
}

class Module_public extends RubyMethod {
	public Module_public() {
		super(-1);
		setAccess(PRIVATE);
	}

	protected RubyValue run(RubyValue receiver, RubyArray args, RubyBlock block) {
		return Module_AccessControl.run(PUBLIC, receiver, args, block);
	}
}

class Module_protected extends RubyMethod {
	public Module_protected() {
		super(-1);
		setAccess(PRIVATE);
	}

	protected RubyValue run(RubyValue receiver, RubyArray args, RubyBlock block) {
		return Module_AccessControl.run(PROTECTED, receiver, args, block);
	}
}

class Module_private extends RubyMethod {
	public Module_private() {
		super(-1);
		setAccess(PRIVATE);
	}

	protected RubyValue run(RubyValue receiver, RubyArray args, RubyBlock block) {
		return Module_AccessControl.run(PRIVATE, receiver, args, block);
	}
}

class Module_to_s extends RubyMethod {
	public Module_to_s() {
		super(-1);
	}
	
	protected RubyValue run(RubyValue receiver, RubyArray args, RubyBlock block) {
		RubyModule c = (RubyModule)receiver;
		return ObjectFactory.createString(c.getName());
	}
}

class Module_inspect extends RubyMethod {
	public Module_inspect() {
		super(-1);
	}
	
	protected RubyValue run(RubyValue receiver, RubyArray args, RubyBlock block) {
		return RubyAPI.callPublicMethod(receiver, args, block, "to_s");
	}
}

class AttrReader extends RubyMethod {

	private String methodName_;

	public AttrReader(String methodName) {
		super(0);
		methodName_ = "@" + methodName;
	}

	protected RubyValue run(RubyValue receiver, RubyArray args, RubyBlock block) {
		return receiver.getInstanceVariable(methodName_);
	}
}

class Module_attr_reader extends RubyMethod {
	public Module_attr_reader() {
		super(-1);
	}
	
	protected RubyValue run(RubyValue receiver, RubyArray args, RubyBlock block) {
		RubyModule m = (RubyModule)receiver;

		for (RubyValue v : args) {
			String s = convertToString(v);
			m.defineMethod(s, new AttrReader(s));
		}

		return ObjectFactory.nilValue;
	}
}

class AttrWriter extends RubyMethod {

	private String methodName_;

	public AttrWriter(String methodName) {
		super(1);
		methodName_ = "@" + methodName;
	}

	protected RubyValue run(RubyValue receiver, RubyArray args, RubyBlock block) {
		return receiver.setInstanceVariable(args.get(0), methodName_);
	}
}

class Module_attr_writer extends RubyMethod {
	public Module_attr_writer() {
		super(-1);
	}
	
	protected RubyValue run(RubyValue receiver, RubyArray args, RubyBlock block) {
		RubyModule m = (RubyModule)receiver;

		for (RubyValue v : args) {
			String s = convertToString(v);
			m.defineMethod(s + "=", new AttrWriter(s));
		}

		return ObjectFactory.nilValue;
	}
}

class Module_attr_accessor extends RubyMethod {
	public Module_attr_accessor() {
		super(-1);
	}
	
	protected RubyValue run(RubyValue receiver, RubyArray args, RubyBlock block) {
		RubyModule m = (RubyModule)receiver;

		for (RubyValue v : args) {
			String s = convertToString(v);
			m.defineMethod(s, new AttrReader(s));
			m.defineMethod(s + "=", new AttrWriter(s));
		}

		return ObjectFactory.nilValue;
	}
}

class Module_include extends RubyMethod {
	public Module_include() {
		super(-1);
	}
	
	protected RubyValue run(RubyValue receiver, RubyArray args, RubyBlock block) {
		RubyModule module = (RubyModule)receiver;
		if (args != null) {
			for(RubyValue m: args){
				module.includeModule((RubyModule)m);
			}
		}
		return ObjectFactory.nilValue;
	}
}

class Module_ancestors extends RubyMethod {
	public Module_ancestors() {
		super(0);
	}
	
	protected RubyValue run(RubyValue receiver, RubyArray args, RubyBlock block) {
		RubyModule module = (RubyModule)receiver;
		RubyArray r = new RubyArray();
		module.collectIncludedModuleNames(r);
		return r;
	}
}

class Module_operator_compare extends RubyMethod {
	public Module_operator_compare () {
		super(1);
	}
	
	protected RubyValue run(RubyValue receiver, RubyArray args, RubyBlock block) {
		RubyModule module = (RubyModule)receiver;
		if (!(args.get(0) instanceof RubyModule)) {
			return ObjectFactory.nilValue;
		}
		
		RubyModule other_module = (RubyModule)args.get(0);
		if (module == other_module) {
			return ObjectFactory.fixnum0;
		}
		
		if (module instanceof RubyClass || other_module instanceof RubyClass) {
			RubyClass c1 = (RubyClass)module;
			RubyClass c2 = (RubyClass)other_module;
			if (c1.isKindOf(c2)) {
				return ObjectFactory.createFixnum(-1);
			} else if (c2.isKindOf(c1)) {
				return ObjectFactory.fixnum1;
			}
		}
		
		return ObjectFactory.nilValue;
	}
}

public class ModuleClassBuilder {
	
	public static void initialize() {
		RubyClass c = RubyRuntime.ModuleClass;
		c.defineMethod("public", new Module_public());
		c.defineMethod("protected", new Module_protected());
		c.defineMethod("private", new Module_private());
		c.defineMethod("to_s", new Module_to_s());
		c.defineMethod("inspect", new Module_inspect());
		c.defineMethod("include", new Module_include());
		c.defineMethod("<=>", new Module_operator_compare());
		c.defineMethod("ancestors", new Module_ancestors());

		c.setAccessPrivate();
		c.defineMethod("attr_reader", new Module_attr_reader());
		c.defineMethod("attr_writer", new Module_attr_writer());
		c.defineMethod("attr_accessor", new Module_attr_accessor());
		c.setAccessPublic();
	}
}
