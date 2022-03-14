package path;

import static info.scce.addlib.cudd.Cudd.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.neo4j.graphdb.Entity;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Path;
import org.neo4j.graphdb.Relationship;

import expr.antlr.PCparserLexer;
import expr.antlr.PCparserParser;
import expr.Antlr2Expr;
import expr.composite.Expr;
import expr.visitor.BDDbuilder;

public class BDDMapper {
	
		// hashmap stores the <pc, bddAddress> Map
	 	private static Map<String, Long> bddMap = new LinkedHashMap<String, Long>();
	 	// hashmap stores the <path, bddAddress> Map
	 	private static Map<Path, Long> pathBDDMap = new LinkedHashMap<Path, Long>();
	 	
	 	// create static antlr2Expr and bddBuilder object
	 	private static Antlr2Expr antlr2Expr;
	 	private static BDDbuilder bddBuilder;
	    // create a false BDD for checking satisfiability
	    private static long FF;
	    
	    public BDDMapper(BDDbuilder bddBuilder, Antlr2Expr antlr2Expr) {
			this.bddBuilder = bddBuilder;
			this.antlr2Expr = antlr2Expr;
			
			this.FF = Cudd_ReadLogicZero(bddBuilder.ddManager);
		}
	    
	    public Map<String, Long> getBddMap() {
	    	return this.bddMap;
	    }
	    
	    public Map<Path, Long> getPathBddMap(){
	    	return this.pathBDDMap;
	    }
	    
	    public long getFF() {
	    	return this.FF;
	    }
	    
	    /**
	     * Parse the specific presence condition, and add it to BDDexprMap
	     * @author Xiang Chen
	     * @since 2021.9
	     * @param pc the String of presence condition
	     */
	    public void parsePC(String pc) {
	    	ANTLRInputStream input = new ANTLRInputStream(pc);
			PCparserLexer lexer = new PCparserLexer(input);
			CommonTokenStream tokens = new CommonTokenStream(lexer);
			PCparserParser parser = new PCparserParser(tokens);
	        parser.setBuildParseTree(true);      // tell ANTLR to build a parse tree
	        ParseTree tree = parser.stat(); // parse
	        
	        // generate the Expr hierarchy for initial string
	        Expr expr = antlr2Expr.visit(tree.getChild(0));
	        
	        // generate the BDD
	        expr.accept(bddBuilder);
	        
	        // store the BDD into the map
	        bddMap.put(pc, bddBuilder.getBDDaddress());
		}
	    
	    /*
	     * perform SAT check on the path
	     * @author Xiang Chen
	     * @since 2021.12
	     */
	    public void parsePath(Path path) {
	    	// get the iterator for the path
	    	Iterator<Entity> entities = path.iterator();
	    	
	    	String nextPC = (String) entities.next().getProperty("condition", "True");
	    	
	    	// if the bddMap does not contains current PC, it means this PC has not been parsed before
			// then parse it and add it to the map
			if (!bddMap.containsKey(nextPC)) {
				parsePC(nextPC);
			}
			
			long result = bddMap.get(nextPC);
	    	
	    	while (entities.hasNext()) {
	    		nextPC = (String) entities.next().getProperty("condition", "True");
	    		// if the bddMap does not contains current PC, it means this PC has not been parsed before
	    		// then parse it and add it to the map
	    		if (!bddMap.containsKey(nextPC)) {
	    			parsePC(nextPC);
	    		}
	    		
				result = Cudd_bddAnd(bddBuilder.ddManager, result, bddMap.get(nextPC));
			}
	    	// add the <path, bddAddress> to the map
	    	pathBDDMap.put(path, result);
	    }
	    
	    public boolean checkSAT(Path path, Node node) {
	    	// get the big conjunction of current path's pc's BDDs
	        if (!pathBDDMap.containsKey(path)) {
	        	parsePath(path);
			}
			
			// get the pc for this node
	        String pc = (String) node.getProperty("condition", "True");
	        
	        if (!bddMap.containsKey(pc)) {
				parsePC(pc);
			}
	        
	        /*
	         * check SAT and return the result
	         */
	        return (Cudd_bddAnd(bddBuilder.ddManager, pathBDDMap.get(path), bddMap.get(pc)) != FF);
	    }
	    
	    /*
	     * check SAT and create Iterator<Relationship>
	     * @author Xiang Chen
	     * @since 2021.12
	     */
	    public Iterable<Relationship> checkSATandGetRelationships (Iterable<Relationship> relationships, long pathConjunc){
	    	List<Relationship> reIterator = new ArrayList<Relationship>();
	    	
	    	String currentPC;
	    	for (Relationship relationship : relationships) {
	    		currentPC = (String) relationship.getProperty("condition", "True");
	    		
	    		// if the bddMap does not contains current PC, it means this PC has not been parsed before
	    		// then parse it and add it to the map
	    		if (!bddMap.containsKey(currentPC)) {
					parsePC(currentPC);
				}
	    		
				if (Cudd_bddAnd(bddBuilder.ddManager, pathConjunc, bddMap.get(currentPC)) != FF) {
					reIterator.add(relationship);
				}
			}
	    	return reIterator;
	    }
}
