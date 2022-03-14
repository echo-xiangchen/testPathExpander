package expr.composite;

import expr.visitor.Visitor;

public class BoolFalse extends Const {
	public BoolFalse(String name) {
		super(name);
	}
	
	public void accept(Visitor v) {
		v.visitBoolFalse(this);
	}
}
