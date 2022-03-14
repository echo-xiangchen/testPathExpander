package expr.composite;

import expr.visitor.*;

public class BoolTrue extends Const {
	public BoolTrue(String name) {
		super(name);
	}
	
	public void accept(Visitor v) {
		v.visitBoolTrue(this);
	}
}
