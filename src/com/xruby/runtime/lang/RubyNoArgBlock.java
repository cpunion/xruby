/**
 * Copyright 2007 Ye Zheng
 * Distributed under the Apache License
 */

package com.xruby.runtime.lang;

import com.xruby.runtime.builtin.RubyArray;

public abstract class RubyNoArgBlock extends RubyBlock {
    public RubyNoArgBlock(RubyValue self, RubyValue arg, RubyArray args, RubyBlock block, RubyModule scope, RubyMethod owner, boolean definedInAnotherBlock) {
        super(0, false, 0, self, arg, args, block, scope, owner, definedInAnotherBlock);
    }

    protected RubyValue run(RubyValue receiver, RubyArray args) {
        return this.run(receiver);
    }

    protected RubyValue run(RubyValue receiver, RubyValue arg) {
        return this.run(receiver);
    }

    protected RubyValue run(RubyValue receiver, RubyValue arg0, RubyValue arg1) {
        return this.run(receiver);
    }

    protected abstract RubyValue run(RubyValue receiver);
}
