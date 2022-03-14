package expr.composite;

import expr.visitor.*;

public abstract class Expr {
	// variable name
	public String name;
	
	public void accept(Visitor v){};
}
