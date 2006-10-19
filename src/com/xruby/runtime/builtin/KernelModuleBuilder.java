/** 
 * Copyright (c) 2005-2006 Xue Yong Zhi. All rights reserved.
 */

package com.xruby.runtime.builtin;

import antlr.RecognitionException;
import antlr.TokenStreamException;

import com.xruby.runtime.lang.*;
import com.xruby.compiler.*;
import com.xruby.compiler.codegen.*;
import com.xruby.runtime.value.*;

import java.io.*;
import java.util.jar.*;
import java.util.*;

//TODO imcomplete
class Kernel_eval extends RubyMethod {
	public Kernel_eval() {
		super(-1);
	}

	protected RubyValue run(RubyValue receiver, ArrayValue args, RubyBlock block) throws RubyException {
		RubyCompiler compiler = new RubyCompiler();
		StringValue program_text = (StringValue)args.get(0).getValue();

		try {
			CompilationResults codes = compiler.compile(new StringReader(program_text.toString()));
			RubyValue v = codes.run();
			return v;
		} catch (RecognitionException e) {
			throw new RubyException(e.toString());
		} catch (TokenStreamException e) {
			throw new RubyException(e.toString());
		} catch (CompilerException e) {
			throw new RubyException(e.toString());
		} catch (InstantiationException e) {
			throw new RubyException(e.toString());
		} catch (IllegalAccessException e) {
			throw new RubyException(e.toString());
		}
	}
}

class Kernel_puts extends RubyMethod {
	public Kernel_puts() {
		super(-1);
	}

	protected RubyValue run(RubyValue receiver, ArrayValue args, RubyBlock block) throws RubyException {
		for (RubyValue arg : args) {
			if (ObjectFactory.nilValue == arg) {
				System.out.println("nil");
			} else if (RubyRuntime.StringClass == arg.getRubyClass()){
				StringValue value = (StringValue)arg.getValue();
				System.out.println(value.toString());
			} else {
				RubyValue str = RubyRuntime.callPublicMethod(arg, null, null, "to_s");
				StringValue value = (StringValue)str.getValue();
				System.out.println(value.toString());
			}
		}
		return ObjectFactory.nilValue;
	}
}

class Kernel_print extends RubyMethod {
	public Kernel_print() {
		super(-1);
	}

	protected RubyValue run(RubyValue receiver, ArrayValue args, RubyBlock block) throws RubyException {
		return _run(GlobalVariables.STDOUT, args, block);
	}
	
	protected RubyValue _run(RubyValue receiver, ArrayValue args, RubyBlock block) throws RubyException {
		// if no argument given, print `$_'
		if (null == args) {
			args = new ArrayValue(GlobalVariables.LAST_READ_LINE);
		}
		
		for (int i = 0; i < args.size(); ++i) {
			// insert the output field separator($,) if not nil
			if (i > 0 && GlobalVariables.OUTPUT_FIELD_SEPARATOR != ObjectFactory.nilValue) {
				RubyRuntime.callPublicMethod(receiver, GlobalVariables.OUTPUT_FIELD_SEPARATOR, "write");
			}
			
			if (args.get(i) == ObjectFactory.nilValue) {
				RubyRuntime.callPublicMethod(receiver, ObjectFactory.createString("nil"), "write");
			} else {
				RubyRuntime.callPublicMethod(receiver, args.get(i), "write");
			}
		}
		
		// if the output record separator($\) is not nil, it will be appended to the output.
		if ( GlobalVariables.OUTPUT_RECORD_SEPARATOR != ObjectFactory.nilValue) {
			RubyRuntime.callPublicMethod(receiver, GlobalVariables.OUTPUT_RECORD_SEPARATOR, "write");
		}

		return ObjectFactory.nilValue;
	}
}

class Kernel_printf extends RubyMethod {
	public Kernel_printf() {
		super(-1);
	}

	protected RubyValue run(RubyValue receiver, ArrayValue args, RubyBlock block) throws RubyException {
		Object[] raw_args = new Object[args.size() - 1];
		for (int i = 1; i < args.size(); ++i) {
			Object v = args.get(i).getValue();
			if (v instanceof IntegerValue) {
				raw_args[i - 1] = new Integer(((IntegerValue)v).intValue());
			} else {
				raw_args[i - 1] = v;
			}
		}

		String fmt = ((StringValue)args.get(0).getValue()).toString();
		System.out.printf(fmt, raw_args);
		return ObjectFactory.nilValue;
	}
}

class Kernel_p extends RubyMethod {
	public Kernel_p() {
		super(-1);
	}

	protected RubyValue run(RubyValue receiver, ArrayValue args, RubyBlock block) throws RubyException {
		for (RubyValue arg : args) {
			if (ObjectFactory.nilValue == arg) {
				System.out.println("nil");
			} else if (RubyRuntime.StringClass == arg.getRubyClass()){
				StringValue value = (StringValue)arg.getValue();
				System.out.println(value.toString());
			} else {
				RubyValue str = RubyRuntime.callPublicMethod(arg, null, null, "inspect");
				StringValue value = (StringValue)str.getValue();
				System.out.println(value.toString());
			}
		}
		return ObjectFactory.nilValue;
	}
}

class Kernel_class extends RubyMethod {
	public Kernel_class() {
		super(-1);
	}

	protected RubyValue run(RubyValue receiver, ArrayValue args, RubyBlock block) throws RubyException {
		return ObjectFactory.createClass(receiver.getRubyClass());
	}
}

class Kernel_operator_case_equal extends RubyMethod {
	public Kernel_operator_case_equal() {
		super(1);
	}

	protected RubyValue run(RubyValue receiver, ArrayValue args, RubyBlock block) throws RubyException {
		if (receiver == args.get(0)) {
			return ObjectFactory.trueValue;
		} else {
			return ObjectFactory.falseValue;
		}
	}
}

class Kernel_method_missing extends RubyMethod {
	public Kernel_method_missing() {
		super(-1);
	}

	protected RubyValue run(RubyValue receiver, ArrayValue args, RubyBlock block) throws RubyException {
		throw new RubyException("not implemented!");
	}
}

class Kernel_raise extends RubyMethod {
	public Kernel_raise() {
		super(-1);
	}

	protected RubyValue run(RubyValue receiver, ArrayValue args, RubyBlock block) throws RubyException {
		if (null == args) {
			//TODO With no arguments, raises the exception in $! or raises a RuntimeError if $! is nil.
			throw new RubyException("not implemented!");
		} else if (1 == args.size() && (args.get(0).getRubyClass() == RubyRuntime.StringClass)) {
			//With a single String argument, raises a RuntimeError with the string as a message.
			throw new RubyException(RubyRuntime.RuntimeErrorClass, ((StringValue)args.get(0).getValue()).toString());
		} else {
			//TODO Otherwise, the first parameter should be the name of an Exception class
			//(or an object that returns an Exception when sent exception). The optional second
			//parameter sets the message associated with the exception, and the third parameter
			//is an array of callback information.
			throw new RubyException("not implemented!");
		}
	}
}

class JarLoader extends ClassLoader
{
	public RubyProgram load(File filename)
	{
		JarFile jar = null;

		try {
			jar = new JarFile(filename);
			return _load(jar);
		} catch (IOException e) {
			return null;
		} catch (InstantiationException e) {
			return null;
		} catch (IllegalAccessException e) {
			return null;
		} finally {
			if (null != jar) {
				try {
					jar.close();
				} catch (IOException e) {
				}
			}
		}
	}

	private RubyProgram _load(JarFile jar) throws IOException, InstantiationException, IllegalAccessException
	{
		RubyProgram p = null;

		for (Enumeration<JarEntry> e = jar.entries(); e.hasMoreElements();) {
			JarEntry entry = e.nextElement();
			if (!entry.getName().endsWith(".class")) {
				continue;
			}

			BufferedInputStream stream = new BufferedInputStream(jar.getInputStream(entry));
			byte[] buffer = new byte[(int)entry.getSize()];
			stream.read(buffer);
			Class c = defineClass(NameFactory.filename2classname(entry.getName()), buffer, 0, buffer.length);
		
			if (entry.getName().endsWith("main.class")) {//FIXME better error checking
				p = (RubyProgram)c.newInstance();
			}
		}
		
		return p;
	}
}

class Kernel_require extends RubyMethod {
	public Kernel_require() {
		super(1);
	}
	
	protected RubyValue run(RubyValue receiver, ArrayValue args, RubyBlock block) throws RubyException {
		StringValue required_file = (StringValue)args.get(0).getValue();
		File filename = NameFactory.find_corresponding_jar_file(required_file.toString(), null);//TODO search $:
		if (null == filename) {
			return ObjectFactory.falseValue;
		}
		
		JarLoader jarloader = new JarLoader();
		RubyProgram p = jarloader.load(filename);
		if (null == p) {
			return ObjectFactory.falseValue;
		}
	
		p.run();
		return ObjectFactory.trueValue;
	}
}

class Kernel_load extends RubyMethod {
	public Kernel_load() {
		super(-1);
	}

	protected RubyValue run(RubyValue receiver, ArrayValue args, RubyBlock block) throws RubyException {
		throw new RubyException("not implemented!");
		/*
		 * Loads and executes the Ruby program in the file aFileName.
		 * If the filename does not resolve to an absolute path, the
		 * file is searched for in the library directories listed in $:.
		 * If the optional wrap parameter is true, the loaded script will
		 * be executed under an anonymous module, protecting the calling
		 * program's global namespace. Any local variables in the loaded
		 * file will not be propagated to the loading environment. 
		 */
	}
}

class Kernel_to_s extends RubyMethod {
	public Kernel_to_s() {
		super(-1);
	}
	
	protected RubyValue run(RubyValue receiver, ArrayValue args, RubyBlock block) throws RubyException {
		String className = receiver.getRubyClass().getName();
		return ObjectFactory.createString("#<" + className + ":0x" + Integer.toHexString(receiver.hashCode()) + "x>");
	}
}

class Kernel_lambda extends RubyMethod {
	public Kernel_lambda() {
		super(0);
	}
	
	protected RubyValue run(RubyValue receiver, ArrayValue args, RubyBlock block) throws RubyException {
		return new RubyValue(RubyRuntime.ProcClass, block);
	}
}

class Kernel_loop extends RubyMethod {
	public Kernel_loop() {
		super(0);
	}
	
	protected RubyValue run(RubyValue receiver, ArrayValue args, RubyBlock block) throws RubyException {
		if (null == block) {
			throw new RubyException(RubyRuntime.LocalJumpErrorClass, "in `loop': no block given");
		}

		for (;;) {
			block.invoke(receiver, args);
			if (block.getBreakValue() != null) {
				return block.getBreakValue();
			}
		}
	}
}

class Kernel_open extends RubyMethod {
	public Kernel_open() {
		super(-1);
	}
	
	protected RubyValue run(RubyValue receiver, ArrayValue args, RubyBlock block) throws RubyException {
		StringValue filename = (StringValue)args.get(0).getValue();
		StringValue mode = (StringValue)args.get(1).getValue();
		return ObjectFactory.createFile(filename.toString(), mode.toString());
	}
}

class Kernel_kind_of extends RubyMethod {
	public Kernel_kind_of() {
		super(1);
	}
	
	protected RubyValue run(RubyValue receiver, ArrayValue args, RubyBlock block) throws RubyException {
		if (RubyRuntime.isKindOf(args.get(0), receiver)) {
			return ObjectFactory.trueValue;
		} else {
			return ObjectFactory.falseValue;
		}
	}
}

class Kernel_instance_of extends RubyMethod {
	public Kernel_instance_of() {
		super(1);
	}
	
	protected RubyValue run(RubyValue receiver, ArrayValue args, RubyBlock block) throws RubyException {
		if (RubyRuntime.isInstanceOf(args.get(0), receiver)) {
			return ObjectFactory.trueValue;
		} else {
			return ObjectFactory.falseValue;
		}
	}
}

class Kernel_respond_to extends RubyMethod {
	public Kernel_respond_to() {
		super(-1);
	}
	
	protected RubyValue run(RubyValue receiver, ArrayValue args, RubyBlock block) throws RubyException {
		if (args.size() < 1) {
			throw new RubyException(RubyRuntime.ArgumentErrorClass, "wrong number of arguments (0 for 1)");
		}

		boolean include_private = (ObjectFactory.trueValue == args.get(1));
		if (hasMethod(receiver, convertToString(args.get(0)), include_private)) {
			return ObjectFactory.trueValue;
		} else {
			return ObjectFactory.falseValue;
		}
	}

	private boolean hasMethod(RubyValue receiver, String method_name, boolean include_private) {
		if (include_private) {
			return (null != receiver.findMethod(method_name));
		} else {
			return (null != receiver.findPublicMethod(method_name));
		}
	}
}

class Kernel_send extends RubyMethod {
	public Kernel_send() {
		super(-1);
	}
	
	protected RubyValue run(RubyValue receiver, ArrayValue args, RubyBlock block) throws RubyException {
		if (args.size() < 1) {
			throw new RubyException(RubyRuntime.ArgumentErrorClass, "no method name given");
		}

		RubyValue method_name = args.remove(0);
		return RubyRuntime.callMethod(receiver, args, block, convertToString(method_name));
	}
}

class Kernel_method extends RubyMethod {
	public Kernel_method() {
		super(1);
	}
	
	protected RubyValue run(RubyValue receiver, ArrayValue args, RubyBlock block) throws RubyException {
		String method_name = convertToString(args.get(0));
		RubyMethod  m = receiver.findPublicMethod(method_name);
		if (null == m) {
			throw new RubyException(RubyRuntime.NameErrorClass, "public method '" +  method_name + "' can not be found in '" + receiver.getRubyClass().getName() + "'");
		}
		return ObjectFactory.createMethod(receiver, method_name, m);
	}
}

class Kernel_methods extends RubyMethod {
	public Kernel_methods() {
		super(-1);
	}
	
	protected RubyValue run(RubyValue receiver, ArrayValue args, RubyBlock block) throws RubyException {
		ArrayValue a = new ArrayValue();
		receiver.collectMethodNames(a);
		return ObjectFactory.createArray(a);
	}
}

class Kernel_at_exit extends RubyMethod {
	public Kernel_at_exit() {
		super(0);
	}
	
	protected RubyValue run(RubyValue receiver, ArrayValue args, RubyBlock block) throws RubyException {
		if (null == block) {
			throw new RubyException(RubyRuntime.ArgumentErrorClass, "called without a block");
		}
		
		AtExitBlocks.registerBlock(block);
		return new RubyValue(RubyRuntime.ProcClass, block);
	}
}

public class KernelModuleBuilder {
	public static RubyModule create() {
		RubyModule m = RubyRuntime.GlobalScope.defineNewModule("Kernel");
		m.defineMethod("class", new Kernel_class());
		m.defineMethod("raise", new Kernel_raise());
		m.defineMethod("===", new Kernel_operator_case_equal());
		m.defineMethod("to_s", new Kernel_to_s());
		m.defineMethod("loop", new Kernel_loop());
		m.defineMethod("open", new Kernel_open());
		m.defineMethod("kind_of?", new Kernel_kind_of());
		m.defineMethod("instance_of?", new Kernel_instance_of());
		m.defineMethod("respond_to?", new Kernel_respond_to());
		RubyMethod send = new Kernel_send();
		m.defineMethod("send", send);
		m.defineMethod("__send__", send);
		m.defineMethod("method", new Kernel_method());
		m.defineMethod("methods", new Kernel_methods());
		
		m.setAccessPrivate();
		m.defineMethod("puts", new Kernel_puts());
		m.defineMethod("print", new Kernel_print());
		m.defineMethod("printf", new Kernel_printf());
		m.defineMethod("p", new Kernel_p());
		m.defineMethod("method_missing", new Kernel_method_missing());
		m.defineMethod("eval", new Kernel_eval());
		m.defineMethod("require", new Kernel_require());
		m.defineMethod("load", new Kernel_load());
		RubyMethod lambda = new Kernel_lambda();
		m.defineMethod("lambda", lambda);
		m.defineMethod("proc", lambda);
		m.defineMethod("at_exit", new Kernel_at_exit());
		m.setAccessPublic();

		RubyRuntime.ObjectClass.includeModule(m);
		return m;
	}
}
