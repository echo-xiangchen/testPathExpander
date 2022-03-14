package path;

import org.neo4j.graphalgo.impl.util.PathImpl;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Path;
import org.neo4j.graphdb.Relationship;
import org.neo4j.internal.helpers.collection.Iterables;
import org.neo4j.procedure.Description;
import org.neo4j.procedure.Name;
import org.neo4j.procedure.UserFunction;

import static info.scce.addlib.cudd.Cudd.*;

import expr.Antlr2Expr;
import expr.visitor.BDDbuilder;

import java.util.Iterator;
import java.util.List;

/**
 * @author mh
 * @since 19.02.18
 */
public class AwarePaths {

    @UserFunction("apoc.aware.path.create")
    @Description("apoc.aware.path.create(startNode,[rels]) - creates a path instance of the given elements")
    public Path create(@Name("startNode") Node startNode, @Name(value = "rels", defaultValue = "[]") List<Relationship> rels) {
        if (startNode == null) return null;
        PathImpl.Builder builder = new PathImpl.Builder(startNode);
        if (rels != null) {
            for (Relationship rel : rels) {
                if(rel != null) {
                    builder = builder.push(rel);
                }
            }
        }
        return builder.build();
    }

    @UserFunction("apoc.aware.path.slice")
    @Description("apoc.aware.path.slice(path, [offset], [length]) - creates a sub-path with the given offset and length")
    public Path slice(@Name("path") Path path, @Name(value = "offset", defaultValue = "0") long offset,@Name(value = "length", defaultValue = "-1") long length) {
        if (path == null) return null;
        if (offset < 0) offset = 0;
        if (length == -1) length = path.length() - offset;
        if (offset == 0 && length >= path.length()) return path;

        Iterator<Node> nodes = path.nodes().iterator();
        Iterator<Relationship> rels = path.relationships().iterator();
        for (long i = 0; i < offset && nodes.hasNext() && rels.hasNext(); i++) {
            nodes.next();
            rels.next();
        }
        if (!nodes.hasNext()) return null;

        PathImpl.Builder builder = new PathImpl.Builder(nodes.next());
        for (long i = 0; i < length && rels.hasNext(); i++) {
            builder = builder.push(rels.next());
        }
        return builder.build();
    }

    @UserFunction("apoc.aware.path.combine")
    @Description("apoc.aware.path.combine(path1, path2) - combines the paths into one if the connecting node matches")
    public Path combine(@Name("first") Path first, @Name("second") Path second) {
        if (first == null || second == null) return null;

        if (!first.endNode().equals(second.startNode()))
            throw new IllegalArgumentException("Paths don't connect on their end and start-nodes "+first+ " with "+second);

        // initialize antlr2Expr and bddbuilder for parsing
		final Antlr2Expr antlr2Expr = new Antlr2Expr();
		final BDDbuilder bddBuilder = new BDDbuilder();
		
		// initialize BDDMapper
		final BDDMapper bddMapper = new BDDMapper(bddBuilder, antlr2Expr);
		
		// get the big conjunction of first's PC and its BDD
        if (!bddMapper.getPathBddMap().containsKey(first)) {
        	bddMapper.parsePath(first);
		}
		long conj1 = bddMapper.getPathBddMap().get(first);
		
		// get the big conjunction of first's PC and its BDD
        if (!bddMapper.getPathBddMap().containsKey(second)) {
        	bddMapper.parsePath(second);
		}
		long conj2 = bddMapper.getPathBddMap().get(second);
		
		if (Cudd_bddAnd(bddBuilder.ddManager, conj1, conj2) != bddMapper.getFF()) {
			PathImpl.Builder builder = new PathImpl.Builder(first.startNode());
	        for (Relationship rel : first.relationships()) builder = builder.push(rel);
	        for (Relationship rel : second.relationships()) builder = builder.push(rel);
	        return builder.build();
		}
		return null;
    }

    @UserFunction("apoc.aware.path.elements")
    @Description("apoc.aware.path.elements(path) - returns a list of node-relationship-node-...")
    public List<Object> elements(@Name("path") Path path) {
        if (path == null) return null;
        return Iterables.asList((Iterable)path);
    }
}
