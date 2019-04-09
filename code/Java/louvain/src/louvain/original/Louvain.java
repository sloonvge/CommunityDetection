package louvain.original;

/**
 * Created by wanjx on 2018/12/22.
 */

import java.io.IOException;
import java.util.Date;
import java.util.Random;
import java.util.ArrayList;

import graph.io.ReadInputLouvain;

public class Louvain {
    public int nNodes, nEdges, nClusters;
    public double[][] weightsEdge;
    public ArrayList<Integer>[] adj;
    public int[] cluster;
    public double totalEdgeWeights;
    public double[] weightsNode, weightsCluster;
    public boolean initStatus;
    public int[] nNodesPerCluster;
    int[][] nodesPerCluster;
    public boolean clusterStats;
    public String[] keys;
    public Louvain() {}

    public int ii = 0;

    public Louvain(int nNodes, int nEdges) {
        this.nNodes = nNodes;
        this.nEdges = nEdges;
        this.weightsNode = new double[this.nNodes];
        this.weightsEdge = new double[this.nNodes][this.nNodes];
        this.adj = (ArrayList<Integer>[]) new ArrayList[this.nNodes];
    }

    public void threshold() {
        double t = 0.9480597664365985;
        double max_weight = 0.0;
        int max_index = -1;

        for (int i = 0; i < this.nNodes; i++) {
            for (int j: this.adj[i]) {
                if (this.weightsEdge[i][j] > max_weight) {
                    max_weight = this.weightsEdge[i][j];
                    max_index = j;
                }
                if ((this.weightsEdge[i][j] < t)) {
                    this.weightsEdge[i][j] = this.weightsEdge[j][i] =  0.0;
                }

            }
            if ((max_weight < t)) {
                this.weightsEdge[i][max_index] = this.weightsEdge[max_index][i] = max_weight;
            }
        }
    }
    public Louvain(int nNodes, double[] weightsNode, double[][] weightsEdge, ArrayList<Integer>[] adj, int nEdges) {
        this.nNodes = nNodes;
        this.weightsEdge = weightsEdge;
        this.weightsNode = weightsNode;
        this.adj = adj;
        this.nEdges = nEdges;
    }
    public Louvain(int nNodes, double[] weightsNode, double[][] weightsEdge,
                   ArrayList<Integer>[] adj, int nEdges, String[] keys) {
        this.nNodes = nNodes;
        this.weightsEdge = weightsEdge;
        this.weightsNode = weightsNode;
        this.adj = adj;
        this.nEdges = nEdges;
        this.keys = keys;
    }

    public void setNodeWeight(int index, double w) {
        this.weightsNode[index] += w;
    }

    public void setEdgeWeight(int i, int j, double w) {
        this.weightsEdge[i][j] = this.weightsEdge[j][i] += w;
    }

    public void setNodeAdj(int i, int j) {
        if (this.adj[i] == null) {
            this.adj[i] = new ArrayList<>();
        }
        if (this.adj[j] == null) {
            this.adj[j] = new ArrayList<>();
        }

        if (!this.adj[i].contains(j)) {
            this.adj[i].add(j);
            this.adj[j].add(i);
        }
    }

    public double getTotalEdgeWeights() {
        totalEdgeWeights = 0.0;
        for (double e: weightsNode)
            totalEdgeWeights += e;
        return totalEdgeWeights;
    }
    public int getNNodes() {return nNodes;}
    public int getNEdges() {return nEdges;}
    public int getNClusters() {return nClusters;}
    public int[] getCluster() {return cluster;}

    public String[] getKeys() {
        return keys;
    }

    public double calcQuality(double resolution) {
        double Q;
        int i, j;

        if (!clusterStats)
            setClusterStatus();
        if (nClusters == 1) {
            return 1.0;
        }
        Q = 0.0;
        for (i=0; i < nNodes; i++){
            j = cluster[i];

            for (int c: adj[i]) {
                if (j == cluster[c]){
                    Q += weightsEdge[i][c];
                }
            }
        }
        for (i=0; i < nClusters; i++){
            Q -= weightsCluster[i] * weightsCluster[i] / resolution;
        }
        Q /= resolution;

        return Q;
    }

    public void initNetwork() {
        cluster = new int[nNodes];
        nClusters = nNodes;
        for (int i=0; i < nNodes; i++) {
            cluster[i] = i;
        }
        initStatus = true;
    }

    public boolean moveNode(double resolution) {
        return moveNode(resolution, new Random());
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

    public void deleteClusterStats() {
        weightsCluster = null;
        nNodesPerCluster = null;
        nodesPerCluster = null;
        clusterStats = false;
    }

    private void setClusterStatus() {
        weightsCluster = new double[nClusters];
        nNodesPerCluster = new int[nClusters];
        nodesPerCluster = new int[nClusters][];

        for (int i = 0; i < nNodes; i++) {
            nNodesPerCluster[cluster[i]]++;
            weightsCluster[cluster[i]] += weightsNode[i];
        }
        for (int i = 0; i < nClusters; i++) {
            nodesPerCluster[i] = new int[nNodesPerCluster[i]];
            nNodesPerCluster[i] = 0;
        }
        for (int i = 0; i < nNodes; i++) {
            int j = cluster[i];
            nodesPerCluster[j][nNodesPerCluster[j]] = i;
            nNodesPerCluster[j]++;
        }
        clusterStats = true;
    }

    public Louvain getRebuildGraph() {
        Louvain reNet;
        //double[] reWeightsNode;
        //double[][] reWeightsEdge;
        //int reNNodes;

        if (!clusterStats) {
            setClusterStatus();
        }

        reNet = new Louvain();
        reNet.nNodes = nClusters;
        reNet.weightsNode = new double[nClusters];
        reNet.weightsEdge = new double[nClusters][nClusters];
        reNet.adj = (ArrayList<Integer>[]) new ArrayList[nClusters];

        for (int i = 0; i < nClusters; i++) {
            reNet.adj[i] = new ArrayList<>();
        }
        for (int i = 0; i < nClusters; i++){
            boolean[] visIndex = new boolean[nClusters];
            for (int j = 0; j < nodesPerCluster[i].length; j++) {
                int l = nodesPerCluster[i][j];
                int k = cluster[l];
                reNet.weightsNode[k] += weightsNode[l];
                for (int n : adj[l]) {
                    if (cluster[n] != k) {
                        reNet.weightsEdge[k][cluster[n]] += weightsEdge[l][n];
                        //reNet.weightsEdge[cluster[n]][k] += weightsEdge[n][l];
                        if (!visIndex[cluster[n]]) {
                            reNet.adj[k].add(cluster[n]);
                            visIndex[cluster[n]] = true;
                        }
                    }
                }
            }
        }
        return reNet;
    }
    public int[] mixmCluster(int[] newCluster) {
        int c = 0;
        int[]cc = new int[nNodes];
        for (int i = 0; i < nNodes; i++) {
            int j = cluster[i];
            int k = newCluster[j];
            if (k > c) {
                c = k;
            }
            cc[i] = k;
        }
        return cc;


    }
    public void mixCluster(int[] newCluster) {
        int c = 0;
        for (int i = 0; i < nNodes; i++) {
            int j = cluster[i];
            int k = newCluster[j];
            if (k > c) {
                c = k;
            }
            cluster[i] = k;
        }
        nClusters = c + 1;
        deleteClusterStats();
    }

    public boolean runLouvain(double resolution, Random random) {
        Louvain re;
        boolean update, update2;

        if ((cluster == null) || (nNodes == 1))
            return false;

        update = moveNode(resolution, random);

        if (nClusters < nNodes) {
            re = getRebuildGraph();
            re.initNetwork();

            update2 = re.runLouvain(resolution, random);
            if (update2) {
                update = true;
                mixCluster(re.getCluster());
            }
        }
        deleteClusterStats();
        return update;

    }
    public boolean runLouvain(double resolution, Random random,
                              boolean clusterRe, String filename) {
        Louvain re;
        boolean update, update2;

        if ((cluster == null) || (nNodes == 1))
            return false;
        re  = new Louvain();

        update = moveNode(resolution, random);

        if (nClusters < nNodes) {
            re = getRebuildGraph();
            re.initNetwork();

            update2 = re.runLouvain(resolution, random);
            if (update2) {
                update = true;
                mixCluster(re.getCluster());
            }

        }
        re = getRebuildGraph();
        re.initNetwork();

        re.runLouvain(resolution, random);
        deleteClusterStats();
        return update;

    }
    public static void main(String[] args) throws IOException{
        double resolution;
        Random random;
        String file = "D:\\Code\\CommunityDetection\\data\\";
        random = new Random(100);
        String filename = "football.txt";
        String filepath = file + filename + "";
        String fileout = file + filename + "_out.txt";

        ReadInputLouvain readInput = new ReadInputLouvain();
        Louvain testNet = readInput.readFile(filepath, " ", 0);
        testNet.threshold();
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

