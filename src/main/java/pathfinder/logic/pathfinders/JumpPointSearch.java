package pathfinder.logic.pathfinders;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import pathfinder.logic.Graph;
import pathfinder.logic.Node;

/**
 * Implements the Jump point search (JPS) algorithm for finding the shortest
 * path between two nodes.
 *
 * @see
 * <a href="https://www.aaai.org/ocs/index.php/AAAI/AAAI11/paper/download/3761/4007">Harabor,
 * D. and Grastien, A. (2011). "Online Graph Pruning for Pathfinding on Grid
 * Maps", 25th National Conference on Artificial Intelligence, AAAI.</a>
 */
public class JumpPointSearch extends AbstractPathfinder {

    private static final double SQRT2 = Math.sqrt(2);

    private PriorityQueue<PriorityNode> q;
    private final NeighbourPruningRules prune;
    private final Jump jump;

    /**
     * Constructs a <code>JumpPointSearch</code> object with the specified
     * graph.
     *
     * @param g the graph to be used by this pathfinder
     */
    public JumpPointSearch(Graph g) {
        super(g);
        prune = new NeighbourPruningRules(g);
        jump = new Jump(g, prune);
    }

    @Override
    protected void init() {
        super.init();
        Node source = g.getSource();
        q = new PriorityQueue<>();
        q.add(new PriorityNode(source, 0));
        setDist(source, 0);
    }

    @Override
    public double run() {
        init();
        Node dest = g.getDest();
        while (!q.isEmpty()) {
            Node u = q.poll().node;
            if (getVisited(u)) continue;
            setVisited(u, true);

            if (u.equals(dest)) break;

            for (Node v : identifySuccessors(u)) {
                setPred(v, u);
                double priority = getDist(v) + heuristic(v, dest);
                q.add(new PriorityNode(v, priority));
            }
        }

        if (getDist(dest) == INFINITY) {
            return -1;
        }

        updatePath();
        return getDist(dest);
    }

    /**
     * Identifies the jump point successors of the specified node x.
     *
     * @param x the current node
     * @return jump point successors of the specified node
     */
    private List<Node> identifySuccessors(Node x) {
        List<Node> successors = new ArrayList<>();
        List<Node> neighbours = prune.getPrunedNeighbours(getPred(x), x);

        for (Node n : neighbours) {
            int dx = n.x() - x.x();
            int dy = n.y() - x.y();

            n = jump.jump(x, dx, dy);

            if (n != null) {
                successors.add(n);
                setDist(n, getDist(x) + dist(x, n));
            }
        }

        return successors;
    }

    private double heuristic(Node a, Node b) {
        return octileDist(a, b);
    }

    private double dist(Node x, Node n) {
        return octileDist(x, n);
    }

    private double octileDist(Node a, Node b) {
        int dx = Math.abs(b.x() - a.x());
        int dy = Math.abs(b.y() - a.y());
        return 1 * (dx + dy) + (SQRT2 - 2 * 1) * Math.min(dx, dy);
    }

}
