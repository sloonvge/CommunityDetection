package louvain.rsnl;

import graph.io.ReadInputLouvain;
import graph.io.ReadInputRSNL;
import louvain.original.Louvain;

import java.io.IOException;
import java.util.Date;
import java.util.Random;

/**
 * Created by wanjx on 2019/3/22.
 */

public class RSNL extends Louvain {
    private double Pest;

    public RSNL() {
        super();
    }
    public RSNL(int nNodes, int nEdges) {
        super(nNodes, nEdges);
    }

    public void initPest() {
        if (this.nNodes <= 1 || this.adj == null) {
            return ;
        }
        int maxN = -1;
        for (int i=0; i < this.nNodes; i++) {
            int t = this.adj[i].size();
            if (t > maxN) {
                maxN = t;
            }
        }
        this.Pest = 1.0 / maxN;
        return ;
    }

    public boolean moveNode(double resolution, Random random) {
        boolean update;
        double maxDeltaQ, deltaQ;
        int[] nodeOrder, nNodesOfCluster, neighborClusters, castClusters, newCluster;
        //double[] weightsNodeOfCluster, weightsClusterOfCluster;
        double[] weightsEdgeOfTempCluster;
        int i, j, k, nNeighborClusters, bestCluster, nStableNodes, nCastClusters;

        if (!initStatus) {
            return false;
        }
        if ((cluster == null) || (nNodes == 1))
            return false;

        update = false;
        weightsCluster = new double[nClusters];
        nNodesOfCluster = new int[nClusters];
        for (i = 0; i < nNodes; i++) {
            weightsCluster[cluster[i]] += weightsNode[i];
            nNodesOfCluster[cluster[i]]++;
        }

        nCastClusters = 0;
        castClusters = new int[nNodes];
        for (i = 0; i < nNodes; i++){
            if (nNodesOfCluster[i] == 0) {
                castClusters[nCastClusters] = i;
                nCastClusters++;
            }
        }

        nodeOrder = new int[nNodes];
        for (i = 0; i < nNodes; i++)
            nodeOrder[i] = i;
        for (i=0; i < nNodes; i++){
            j = random.nextInt(nNodes);
            k = nodeOrder[i];
            nodeOrder[i] = nodeOrder[j];
            nodeOrder[j] = k;
        }

        weightsEdgeOfTempCluster = new double[nClusters];
        neighborClusters =  new int[nClusters - 1];
        nStableNodes = 0;
        i = 0;
        do {
            j = nodeOrder[i];
            nNeighborClusters = 0;
            for (int c : adj[j]) {
                if (weightsEdgeOfTempCluster[cluster[c]] == 0) {
                    neighborClusters[nNeighborClusters] = cluster[c];
                    nNeighborClusters++;
                }
                //weightsEdgeOfTempCluster[cluster[c]] += weightsEdge[j][c];
                weightsEdgeOfTempCluster[cluster[c]] += weightsEdge[c][j];
                //System.out.println(weightsEdgeOfCluster[cluster[c]]);
            }
            weightsCluster[cluster[j]] -= weightsNode[j];
            nNodesOfCluster[cluster[j]]--;
            if (nNodesOfCluster[cluster[j]] == 0) {
                castClusters[nCastClusters] = cluster[j];
                nCastClusters++;
            }
            bestCluster = -1;
            maxDeltaQ = 0.0;
            for (k = 0; k < nNeighborClusters; k++) {
                int l = neighborClusters[k];
                deltaQ = weightsEdgeOfTempCluster[l] - weightsNode[j] * weightsCluster[l] / resolution;
                //System.out.println(weightsCluster[l] +"===" +l + "===" + weightsEdgeOfTempCluster[l] + "===" + j);
                if ((deltaQ > maxDeltaQ) || ((deltaQ == maxDeltaQ) && (l < bestCluster))) {
                    bestCluster = l;
                    ii += 1;
                    maxDeltaQ = deltaQ;
                }
                weightsEdgeOfTempCluster[l] = 0.0;
            }
            if (maxDeltaQ == 0.0) {
                bestCluster = castClusters[nCastClusters - 1];
                nCastClusters--;

            }
            weightsCluster[bestCluster] += weightsNode[j];
            nNodesOfCluster[bestCluster]++;
            if (bestCluster == cluster[j]) {
                nStableNodes++;
            } else {
                cluster[j] = bestCluster;
                nStableNodes = 1;
                update = true;
            }
            i = (i < nNodes - 1) ? i + 1: 0;
        } while(nStableNodes < nNodes);

        //for (int c: cluster)
        //System.out.print(c);
        //System.out.println();

        newCluster = new int[nNodes];
        nClusters = 0;
        for (i = 0; i < nNodes; i++){
            if (nNodesOfCluster[i] > 0) {
                newCluster[i] = nClusters;
                //System.out.println(newCluster[i]);
                nClusters++;
            }
        }
        for (i = 0; i < nNodes; i++){
            cluster[i] = newCluster[cluster[i]];
            //System.out.print(cluster[i]);
        }
        //System.out.println(nClusters);
        deleteClusterStats();
        return update;
    }

    public void initNetwork() {
        cluster = new int[nNodes];
        nClusters = nNodes;
        for (int i=0; i < nNodes; i++) {
            cluster[i] = i;
        }
        initStatus = true;
        initPest();
    }

    public static void main(String[] args) throws IOException {
        double resolution;
        Random random;
        String file = "D:\\Code\\CommunityDetection\\data\\";
        random = new Random(100);
        String filename = "football.txt";
        String filepath = file + filename + "";
        String fileout = file + filename + "_rsnl_out.txt";

        ReadInputRSNL readInput = new ReadInputRSNL();

//        RSNL testNet = new RSNL();
//        readInput.readFileG(filepath, " ", 0, testNet);
        RSNL testNet = readInput.readFile(filepath, " ", 0);
        resolution = testNet.getTotalEdgeWeights();

        Date start = new Date();
        testNet.initNetwork();
        testNet.runLouvain(resolution, random);
        Date end = new Date();

        System.out.printf("Cost: %d\n", end.getTime() - start.getTime());
        System.out.printf("Nodes: %d, Edges: %d\n", testNet.getNNodes(), testNet.getNEdges());
        System.out.println(testNet.getNClusters());
        System.out.println(testNet.calcQuality(resolution));
        ReadInputLouvain.writeFile(fileout, testNet.getCluster(), testNet.getKeys(), 0);
    }
}
