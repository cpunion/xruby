/** 
 * Copyright (c) 2005-2006 Xue Yong Zhi. All rights reserved.
 */

package com.xruby.runtime.builtin;

import com.xruby.runtime.lang.*;
import com.xruby.runtime.value.*;

class String_capitalize extends RubyMethod {
	public String_capitalize() {
		super(0);
	}

	protected RubyValue run(RubyValue receiver, RubyArray args, RubyBlock block) {
		StringValue value = (StringValue)receiver.getValue();
		StringValue new_value = new StringValue(value.toString());
		new_value.capitalize();
		return ObjectFactory.createString(new_value);
	}
}

class String_operator_equal extends RubyMethod {
	public String_operator_equal() {
		super(1);
	}

	protected RubyValue run(RubyValue receiver, RubyArray args, RubyBlock block) {
		if (args.get(0).getRubyClass() != RubyRuntime.StringClass) {
			return ObjectFactory.falseValue;
		}

		StringValue a = (StringValue)receiver.getValue();
		StringValue b = (StringValue)args.get(0).getValue();
		if (a.toString().equals(b.toString())) {
			return ObjectFactory.trueValue;
		} else {
			return ObjectFactory.falseValue;
		}
	}
}

/// Upcases the contents of str, returning nil if no changes were made
class String_upcase_danger extends RubyMethod {
	public String_upcase_danger() {
		super(0);
	}

	protected RubyValue run(RubyValue receiver, RubyArray args, RubyBlock block) {
		StringValue value = (StringValue)receiver.getValue();
		StringValue new_value = new StringValue(value.toString().toUpperCase());
		if (new_value.toString().equals(value.toString())) {
			//no changes
			return ObjectFactory.nilValue;
		} else {
			receiver.setValue(new_value);
			return receiver;
		}
	}
}

///Modifies str by converting the first character to uppercase and the remainder to lowercase. Returns nil if no changes are made.
class String_capitalize_danger extends RubyMethod {
	public String_capitalize_danger() {
		super(0);
	}

	protected RubyValue run(RubyValue receiver, RubyArray args, RubyBlock block) {
		StringValue value = (StringValue)receiver.getValue();
		boolean changed = value.capitalize();
		if (changed) {
			return receiver;
		} else {
			return ObjectFactory.nilValue;
		}
	}
}

class String_upcase extends RubyMethod {
	public String_upcase() {
		super(0);
	}

	protected RubyValue run(RubyValue receiver, RubyArray args, RubyBlock block) {
		StringValue value = (StringValue)receiver.getValue();
		return ObjectFactory.createString(value.toString().toUpperCase());
	}
}

class String_downcase extends RubyMethod {
	public String_downcase() {
		super(0);
	}

	protected RubyValue run(RubyValue receiver, RubyArray args, RubyBlock block) {
		StringValue value = (StringValue)receiver.getValue();
		return ObjectFactory.createString(value.toString().toLowerCase());
	}
}

/// Downcases the contents of str, returning nil if no changes were made. 
class String_downcase_danger extends RubyMethod {
	public String_downcase_danger() {
		super(0);
	}

	protected RubyValue run(RubyValue receiver, RubyArray args, RubyBlock block) {
		StringValue value = (StringValue)receiver.getValue();
		StringValue new_value = new StringValue(value.toString().toLowerCase());
		if (new_value.toString().equals(value.toString())) {
			return ObjectFactory.nilValue;
		} else {
			receiver.setValue(new_value);
			return receiver;
		}
	}
}

class String_to_f extends RubyMethod {
	public String_to_f() {
		super(0);
	}

	protected RubyValue run(RubyValue receiver, RubyArray args, RubyBlock block) {
		StringValue value = (StringValue)receiver.getValue();
		return ObjectFactory.createFloat(Double.valueOf(value.toString()));
	}
}

class String_to_i extends RubyMethod {
	public String_to_i() {
		super(0);
	}

	protected RubyValue run(RubyValue receiver, RubyArray args, RubyBlock block) {
		StringValue value = (StringValue)receiver.getValue();
		return ObjectFactory.createFixnum(Integer.valueOf(value.toString()));
	}
}

class String_to_s extends RubyMethod {
	public String_to_s() {
		super(0);
	}

	protected RubyValue run(RubyValue receiver, RubyArray args, RubyBlock block) {
		StringValue value = (StringValue)receiver.getValue();
		return ObjectFactory.createString(value.toString());
	}
}

class String_length extends RubyMethod {
	public String_length() {
		super(0);
	}

	protected RubyValue run(RubyValue receiver, RubyArray args, RubyBlock block) {
		StringValue value = (StringValue)receiver.getValue();
		return ObjectFactory.createFixnum(value.length());
	}
}

class String_initialize extends RubyMethod {
	public String_initialize() {
		super(-1);
	}

	protected RubyValue run(RubyValue receiver, RubyArray args, RubyBlock block) {
		if (args == null) {
			receiver.setValue(new StringValue(""));
		} else if (args.get(0) != null) {
			receiver.setValue(args.get(0).getValue());
		}

		return receiver;
	}
}

class String_initialize_copy extends RubyMethod {
	public String_initialize_copy() {
		super(1);
	}

	protected RubyValue run(RubyValue receiver, RubyArray args, RubyBlock block) {
		receiver.setValue(args.get(0).getValue());
		return receiver;
	}
}

class String_plus extends RubyMethod {
	public String_plus() {
		super(1);
	}

	protected RubyValue run(RubyValue receiver, RubyArray args, RubyBlock block) {
		StringValue v1 = (StringValue)receiver.getValue();
		StringValue v2 = (StringValue)args.get(0).getValue();
		return ObjectFactory.createString(v1.toString() + v2.toString());
	}
}

class String_gsub extends RubyMethod {
	public String_gsub() {
		super(-1);
	}

	protected void checkParameters(RubyArray args) {
		if (args.size() < 2) {
			throw new RubyException(RubyRuntime.ArgumentErrorClass, "wrong number of arguments (" + args.size() + " for 2)");
		}
		
		if (args.get(0).getRubyClass() != RubyRuntime.RegexpClass) {
			throw new RubyException(RubyRuntime.ArgumentErrorClass, "wrong argument type " + args.get(0).getRubyClass().getName() + " (expected Regexp)");
		}
		
		if (args.get(1).getRubyClass() != RubyRuntime.StringClass) {
			throw new RubyException(RubyRuntime.ArgumentErrorClass, "can't convert " + args.get(1).getRubyClass().getName() + " into String");
		}
	}
	
	protected RubyValue run(RubyValue receiver, RubyArray args, RubyBlock block) {
		checkParameters(args);
		
		StringValue g = (StringValue)receiver.getValue();
		RegexpValue r = (RegexpValue)args.get(0).getValue();
		StringValue s = (StringValue)args.get(1).getValue();

		return ObjectFactory.createString(r.gsub(g, s));
	}
}

class String_gsub_danger extends String_gsub {
	protected RubyValue run(RubyValue receiver, RubyArray args, RubyBlock block) {
		checkParameters(args);

		StringValue g = (StringValue)receiver.getValue();
		RegexpValue r = (RegexpValue)args.get(0).getValue();
		StringValue s = (StringValue)args.get(1).getValue();

		String result = r.gsub(g, s);
		if (g.toString().equals(result)) {
			return ObjectFactory.nilValue;
		} else {
			return ObjectFactory.createString(result);
		}
	}
}

class String_split extends RubyMethod {
	public String_split() {
		super(2, false, 1);
	}
	
	protected RubyValue run(RubyValue receiver, RubyArray args, RubyBlock block) {
		StringValue g = (StringValue)receiver.getValue();
		RegexpValue r = (RegexpValue)args.get(0).getValue();
		String[] splitResult;
		
		if (args.size() == 1){
			splitResult = r.getValue().split(g.toString());
		}else{
			IntegerValue i = (IntegerValue)args.get(1).getValue();
			splitResult = r.getValue().split(g.toString(), i.intValue());
		}
		
		RubyArray value = new RubyArray(splitResult.length);
		for(String str : splitResult){
			value.add(ObjectFactory.createString(str));
		}
		return ObjectFactory.createArray(value);
	}
}

class String_operator_compare extends RubyMethod {
	public String_operator_compare() {
		super(1);
	}
	
	protected RubyValue run(RubyValue receiver, RubyArray args, RubyBlock block) {
		StringValue value1 = (StringValue)receiver.getValue();
		Object arg = args.get(0).getValue();
		if (!(arg instanceof StringValue)){
			return ObjectFactory.nilValue;
		}
		StringValue value2 = (StringValue)args.get(0).getValue();
		int compare = value1.toString().compareTo(value2.toString());
		if (compare > 0){
			compare = 1;
		}else if(compare < 0){
			compare = -1;
		}
		return ObjectFactory.createFixnum(compare);
	}
}

public class StringClassBuilder {
	public static RubyClass create() {
		RubyClass c = RubyRuntime.GlobalScope.defineNewClass("String", RubyRuntime.ObjectClass);
		c.defineMethod("capitalize", new String_capitalize());
		c.defineMethod("==", new String_operator_equal());
		c.defineMethod("upcase!", new String_upcase_danger());
		c.defineMethod("capitalize!", new String_capitalize_danger());
		c.defineMethod("upcase", new String_upcase());
		c.defineMethod("downcase", new String_downcase());
		c.defineMethod("to_f", new String_to_f());
		c.defineMethod("to_i", new String_to_i());
		c.defineMethod("to_s", new String_to_s());
        c.defineMethod("length", new String_length());
		c.defineMethod("downcase!", new String_downcase_danger());
		c.defineMethod("initialize_copy", new String_initialize_copy());
		c.defineMethod("initialize", new String_initialize());
		c.defineMethod("+", new String_plus());
		c.defineMethod("gsub", new String_gsub());
		c.defineMethod("gsub!", new String_gsub_danger());
		c.defineMethod("split", new String_split());
		c.defineMethod("<=>", new String_operator_compare());
		return c;
	}
}


