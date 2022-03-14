package path;

import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.traversal.*;
import java.util.*;
import static path.AwarePathExplorer.NodeFilter.*;

public class AwarePathExplorer {
	public static final Uniqueness UNIQUENESS = Uniqueness.RELATIONSHIP_PATH;
	public static final boolean BFS = true;


	private Uniqueness getUniqueness(String uniqueness) {
		for (Uniqueness u : Uniqueness.values()) {
			if (u.name().equalsIgnoreCase(uniqueness)) return u;
		}
		return UNIQUENESS;
	}


	public static Traverser traverse(TraversalDescription traversalDescription,
									 Iterable<Node> startNodes,
									 String pathFilter,
									 String labelFilter,
									 long minLevel,
									 long maxLevel,
									 Uniqueness uniqueness,
									 boolean bfs,
									 boolean filterStartNode,
									 EnumMap<NodeFilter, List<Node>> nodeFilter,
									 String sequence,
									 boolean beginSequenceAtStart,
									 BDDMapper bddMapper) {
		TraversalDescription td = traversalDescription;
		// based on the pathFilter definition now the possible relationships and directions must be shown

		td = bfs ? td.breadthFirst() : td.depthFirst();

		// if `sequence` is present, it overrides `labelFilter` and `relationshipFilter`
		if (sequence != null && !sequence.trim().isEmpty())	{
			String[] sequenceSteps = sequence.split(",");
			List<String> labelSequenceList = new ArrayList<>();
			List<String> relSequenceList = new ArrayList<>();

			for (int index = 0; index < sequenceSteps.length; index++) {
				List<String> seq = (beginSequenceAtStart ? index : index - 1) % 2 == 0 ? labelSequenceList : relSequenceList;
				seq.add(sequenceSteps[index]);
			}
			
			// create new relationship expander
			AwareRelationshipSequenceExpander reExpander = new AwareRelationshipSequenceExpander(relSequenceList, beginSequenceAtStart, bddMapper);
			td = td.expand(reExpander);
			
			// create new label sequence evaluator
			AwareLabelSequenceEvaluator labelSequenceEvaluator = new AwareLabelSequenceEvaluator(labelSequenceList, filterStartNode, beginSequenceAtStart, (int) minLevel, bddMapper);
			td = td.evaluator(labelSequenceEvaluator);
		} else {
			if (pathFilter != null && !pathFilter.trim().isEmpty()) {
				AwareRelationshipSequenceExpander reExpander = new AwareRelationshipSequenceExpander(pathFilter.trim(), beginSequenceAtStart, bddMapper);
				td = td.expand(reExpander);
			}

			if (labelFilter != null && sequence == null && !labelFilter.trim().isEmpty()) {
				AwareLabelSequenceEvaluator labelSequenceEvaluator = new AwareLabelSequenceEvaluator(labelFilter.trim(), filterStartNode, beginSequenceAtStart, (int) minLevel, bddMapper);
				td = td.evaluator(labelSequenceEvaluator);
			} 
			// if user does not specify labelfilters, consider it as accepting all labels
			// this is necesarry for the SAT check
			else if ( sequence == null && (labelFilter == null || labelFilter.trim().isEmpty() )) {
				AwareLabelSequenceEvaluator labelSequenceEvaluator = new AwareLabelSequenceEvaluator("*", filterStartNode, beginSequenceAtStart, (int) minLevel, bddMapper);
				td = td.evaluator(labelSequenceEvaluator);
			}
		}

		if (minLevel != -1) td = td.evaluator(Evaluators.fromDepth((int) minLevel));
		if (maxLevel != -1) td = td.evaluator(Evaluators.toDepth((int) maxLevel));


		if (nodeFilter != null && !nodeFilter.isEmpty()) {
			List<Node> endNodes = nodeFilter.getOrDefault(END_NODES, Collections.EMPTY_LIST);
			List<Node> terminatorNodes = nodeFilter.getOrDefault(TERMINATOR_NODES, Collections.EMPTY_LIST);
			List<Node> blacklistNodes = nodeFilter.getOrDefault(BLACKLIST_NODES, Collections.EMPTY_LIST);
			List<Node> whitelistNodes;

			if (nodeFilter.containsKey(WHITELIST_NODES)) {
				// need to add to new list since we may need to add to it later
				// encounter "can't add to abstractList" error if we don't do this
				whitelistNodes = new ArrayList<>(nodeFilter.get(WHITELIST_NODES));
			} else {
				whitelistNodes = Collections.EMPTY_LIST;
			}

			if (!blacklistNodes.isEmpty()) {
				td = td.evaluator(AwareNodeEvaluators.blacklistNodeEvaluator(filterStartNode, (int) minLevel, blacklistNodes, bddMapper));
			}

			Evaluator endAndTerminatorNodeEvaluator = AwareNodeEvaluators.endAndTerminatorNodeEvaluator(filterStartNode, (int) minLevel, endNodes, terminatorNodes, bddMapper);
			if (endAndTerminatorNodeEvaluator != null) {
				td = td.evaluator(endAndTerminatorNodeEvaluator);
			}

			if (!whitelistNodes.isEmpty()) {
				// ensure endNodes and terminatorNodes are whitelisted
				whitelistNodes.addAll(endNodes);
				whitelistNodes.addAll(terminatorNodes);
				td = td.evaluator(AwareNodeEvaluators.whitelistNodeEvaluator(filterStartNode, (int) minLevel, whitelistNodes, bddMapper));
			}
		}

		td = td.uniqueness(uniqueness); // this is how Cypher works !! Uniqueness.RELATIONSHIP_PATH
		// uniqueness should be set as last on the TraversalDescription
		return td.traverse(startNodes);
	}

	// keys to node filter map
	enum NodeFilter {
		WHITELIST_NODES,
		BLACKLIST_NODES,
		END_NODES,
		TERMINATOR_NODES
	}
}
