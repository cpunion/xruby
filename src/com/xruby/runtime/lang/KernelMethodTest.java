package com.xruby.runtime.lang;

import com.xruby.runtime.value.RubyArray;
import com.xruby.runtime.value.RubyString;

import junit.framework.TestCase;

public class KernelMethodTest extends TestCase {
	public void testEqualMethod() {
		RubyClass objectClass = RubyRuntime.objectClass;
		RubyValue value = objectClass.newInstance();
		assertEquals(RubyConstant.QTRUE, value.callMethod("==", new RubyArray(value)));
		assertEquals(RubyConstant.QTRUE, value.callMethod("equal?", new RubyArray(value)));
		assertEquals(RubyConstant.QTRUE, value.callMethod("===", new RubyArray(value)));
		assertEquals(RubyConstant.QTRUE, value.callMethod("eql?", new RubyArray(value)));
		
		RubyValue value2 = objectClass.callMethod("new");
		assertEquals(RubyConstant.QFALSE, value.callMethod("==", new RubyArray(value2)));
		assertEquals(RubyConstant.QFALSE, value.callMethod("equal?", new RubyArray(value2)));
		assertEquals(RubyConstant.QFALSE, value.callMethod("===", new RubyArray(value2)));
		assertEquals(RubyConstant.QFALSE, value.callMethod("eql?", new RubyArray(value2)));
	}
	
	public void testClassMethod() {
		RubyClass objectClass = RubyRuntime.objectClass;
		RubyValue value = objectClass.callMethod("new");
		assertEquals(objectClass, value.callMethod("class"));
	}
	
	public void testToS() {
		RubyClass objectClass = RubyRuntime.objectClass;
		RubyString s = (RubyString)objectClass.callMethod("to_s");
		assertEquals("Class", s.getString().substring(2, 7));
		RubyValue value = objectClass.newInstance();
		s = (RubyString)value.callMethod("to_s");
		assertEquals("Object", s.getString().substring(2, 8));
	}
	
	public void testInstance() {
		RubyClass objectClass = RubyRuntime.objectClass;
		RubyValue value = objectClass.newInstance();
		assertEquals(RubyConstant.QTRUE, value.callMethod("instance_of?", new RubyArray(objectClass)));
		RubyClass classClass = RubyRuntime.classClass;
		assertEquals(RubyConstant.QFALSE, value.callMethod("instance_of?", new RubyArray(classClass)));
		
		assertEquals(RubyConstant.QTRUE, value.callMethod("kind_of?", new RubyArray(objectClass)));
		assertEquals(RubyConstant.QFALSE, value.callMethod("kind_of?", new RubyArray(classClass)));
		assertEquals(RubyConstant.QTRUE, value.callMethod("is_a?", new RubyArray(objectClass)));
		assertEquals(RubyConstant.QFALSE, value.callMethod("is_a?", new RubyArray(classClass)));
		
		assertEquals(RubyConstant.QTRUE, objectClass.callMethod("kind_of?", new RubyArray(classClass)));
		assertEquals(RubyConstant.QTRUE, objectClass.callMethod("is_a?", new RubyArray(classClass)));
		assertEquals(RubyConstant.QTRUE, objectClass.callMethod("kind_of?", new RubyArray(objectClass)));
		assertEquals(RubyConstant.QTRUE, objectClass.callMethod("is_a?", new RubyArray(objectClass)));
	}
	
	public void testMethods() {
		RubyClass objectClass = RubyRuntime.objectClass;
		RubyValue result = objectClass.callMethod("methods");
		assertTrue(result instanceof RubyArray);
		RubyArray resultArray = (RubyArray)result;
		boolean found = false;
		for (RubyValue value : resultArray) {
			assertTrue(value instanceof RubyString);
			RubyString str = (RubyString)value;
			if ("methods".equals(str.getString())) {
				found = true;
			}
		}
		
		assertTrue(found);
	}
}