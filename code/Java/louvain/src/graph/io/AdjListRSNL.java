package graph.io;

import louvain.original.Louvain;
import louvain.rsnl.RSNL;

/**
 * Created by wanjx on 2018/12/22.
 */

public class AdjListRSNL {

    private RSNL G;

    public RSNL getGraph() {
        return this.G;
    }

    public AdjListRSNL(RSNL G) {
        this.G = G;
    }

    public void CreateGraph(int n1, int n2, double w) {
        this.G.setNodeWeight(n1, w);
        this.G.setNodeWeight(n2, w);
        this.G.setEdgeWeight(n1, n2, w);
        this.G.setNodeAdj(n1, n2);
    }
}
