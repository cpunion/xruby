package com.xruby.newruntime.builtin;

import com.xruby.newruntime.lang.*;

public class MarshalBuilder implements ExtensionBuilder {
	public void initialize() {
		RubyModule marshalModule = RubyUtil.defineModule("Marshal");
		
		/*
		 * s_dump = rb_intern("_dump");
    s_load = rb_intern("_load");
    s_mdump = rb_intern("marshal_dump");
    s_mload = rb_intern("marshal_load");
    s_dump_data = rb_intern("_dump_data");
    s_load_data = rb_intern("_load_data");
    s_alloc = rb_intern("_alloc");
    s_getc = rb_intern("getc");
    s_read = rb_intern("read");
    s_write = rb_intern("write");
    s_binmode = rb_intern("binmode");

    rb_define_module_function(rb_mMarshal, "dump", marshal_dump, -1);
    rb_define_module_function(rb_mMarshal, "load", marshal_load, -1);
    rb_define_module_function(rb_mMarshal, "restore", marshal_load, -1);

    rb_define_const(rb_mMarshal, "MAJOR_VERSION", INT2FIX(MARSHAL_MAJOR));
    rb_define_const(rb_mMarshal, "MINOR_VERSION", INT2FIX(MARSHAL_MINOR));
		 */		
	}
}