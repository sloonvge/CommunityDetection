package graph.io;

import louvain.original.Louvain;

/**
 * Created by wanjx on 2018/12/22.
 */

public class AdjList {

    private Louvain G;

    public Louvain getGraph() {
        return this.G;
    }

    public AdjList(Louvain G) {
        this.G = G;
    }

    public void CreateGraph(int n1, int n2, double w) {
        this.G.setNodeWeight(n1, w);
        this.G.setNodeWeight(n2, w);
        this.G.setEdgeWeight(n1, n2, w);
        this.G.setNodeAdj(n1, n2);
    }
}
