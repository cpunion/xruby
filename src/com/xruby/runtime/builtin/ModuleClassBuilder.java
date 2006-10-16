/** 
 * Copyright (c) 2005-2006 Xue Yong Zhi. All rights reserved.
 */

package com.xruby.runtime.builtin;

import com.xruby.runtime.lang.*;
import com.xruby.runtime.value.*;

class Module_AccessControl {

	static RubyValue run(int access, RubyValue receiver, ArrayValue args, RubyBlock block) throws RubyException {
		RubyClass c = (RubyClass)receiver.getValue();

		if (null == args) {
			c.setAccessMode(access);
			return receiver;
		}
		
		for (RubyValue arg : args) {
			String method_name;
			if (arg.getRubyClass() == RubyRuntime.StringClass) {
				method_name = ((StringValue)arg.getValue()).toString();
			} else if (arg.getRubyClass() == RubyRuntime.SymbolClass) {
				method_name = (String)arg.getValue();
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

	protected RubyValue run(RubyValue receiver, ArrayValue args, RubyBlock block) throws RubyException {
		return Module_AccessControl.run(PUBLIC, receiver, args, block);
	}
}

class Module_protected extends RubyMethod {
	public Module_protected() {
		super(-1);
		setAccess(PRIVATE);
	}

	protected RubyValue run(RubyValue receiver, ArrayValue args, RubyBlock block) throws RubyException {
		return Module_AccessControl.run(PROTECTED, receiver, args, block);
	}
}

class Module_private extends RubyMethod {
	public Module_private() {
		super(-1);
		setAccess(PRIVATE);
	}

	protected RubyValue run(RubyValue receiver, ArrayValue args, RubyBlock block) throws RubyException {
		return Module_AccessControl.run(PRIVATE, receiver, args, block);
	}
}

class Module_to_s extends RubyMethod {
	public Module_to_s() {
		super(-1);
	}
	
	protected RubyValue run(RubyValue receiver, ArrayValue args, RubyBlock block) throws RubyException {
		RubyModule c = (RubyModule)receiver.getValue();
		return ObjectFactory.createString(c.getName());
	}
}

class Module_inspect extends RubyMethod {
	public Module_inspect() {
		super(-1);
	}
	
	protected RubyValue run(RubyValue receiver, ArrayValue args, RubyBlock block) throws RubyException {
		return RubyRuntime.callPublicMethod(receiver, args, block, "to_s");
	}
}

class AttrReader extends RubyMethod {

	private String methodName_;

	public AttrReader(String methodName) {
		super(0);
		methodName_ = "@" + methodName;
	}

	protected RubyValue run(RubyValue receiver, ArrayValue args, RubyBlock block) throws RubyException {
		return receiver.getInstanceVariable(methodName_);
	}
}

class Module_attr_reader extends RubyMethod {
	public Module_attr_reader() {
		super(-1);
	}
	
	protected RubyValue run(RubyValue receiver, ArrayValue args, RubyBlock block) throws RubyException {
		RubyModule m = (RubyModule)receiver.getValue();

		for (RubyValue v : args) {
			String s = toString(v);
			m.defineMethod(s, new AttrReader(s));
		}

		return ObjectFactory.nilValue;
	}

	static String toString(RubyValue v) throws RubyException {
		if (v.getRubyClass() == RubyRuntime.StringClass) {
			return ((StringValue)v.getValue()).toString();
		} else if (v.getRubyClass() == RubyRuntime.SymbolClass) {
			return (String)v.getValue();
		} else {
			throw new RubyException(RubyRuntime.ArgumentErrorClass, inspect(v) + " is not a symbol");
		}
	}
	
	static String inspect(RubyValue value) throws RubyException {
		RubyValue v = RubyRuntime.callPublicMethod(value, null, "inspect");
		return ((StringValue)v.getValue()).toString();
	}
}

class AttrWriter extends RubyMethod {

	private String methodName_;

	public AttrWriter(String methodName) {
		super(1);
		methodName_ = "@" + methodName;
	}

	protected RubyValue run(RubyValue receiver, ArrayValue args, RubyBlock block) throws RubyException {
		return receiver.setInstanceVariable(args.get(0), methodName_);
	}
}

class Module_attr_writer extends RubyMethod {
	public Module_attr_writer() {
		super(-1);
	}
	
	protected RubyValue run(RubyValue receiver, ArrayValue args, RubyBlock block) throws RubyException {
		RubyModule m = (RubyModule)receiver.getValue();

		for (RubyValue v : args) {
			String s = Module_attr_reader.toString(v);
			m.defineMethod(s + "=", new AttrWriter(s));
		}

		return ObjectFactory.nilValue;
	}
}

class Module_attr_accessor extends RubyMethod {
	public Module_attr_accessor() {
		super(-1);
	}
	
	protected RubyValue run(RubyValue receiver, ArrayValue args, RubyBlock block) throws RubyException {
		RubyModule m = (RubyModule)receiver.getValue();

		for (RubyValue v : args) {
			String s = Module_attr_reader.toString(v);
			m.defineMethod(s, new AttrReader(s));
			m.defineMethod(s + "=", new AttrWriter(s));
		}

		return ObjectFactory.nilValue;
	}
}

public class ModuleClassBuilder {
	
	public static RubyClass create() {
		RubyClass c = RubyRuntime.GlobalScope.defineNewClass("Module", RubyRuntime.ObjectClass);
		c.defineMethod("public", new Module_public());
		c.defineMethod("protected", new Module_protected());
		c.defineMethod("private", new Module_private());
		c.defineMethod("to_s", new Module_to_s());
		c.defineMethod("inspect", new Module_inspect());

		c.setAccessPrivate();
		c.defineMethod("attr_reader", new Module_attr_reader());
		c.defineMethod("attr_writer", new Module_attr_writer());
		c.defineMethod("attr_accessor", new Module_attr_accessor());
		c.setAccessPublic();
		return c;
	}
}