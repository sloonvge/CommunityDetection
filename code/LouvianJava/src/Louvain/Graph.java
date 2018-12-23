package Louvain;



import java.io.IOException;
import java.util.Date;
import java.util.Random;
import java.util.ArrayList;

public class Graph {
    private int nNodes, nEdges, nClusters;
    private double[][] weightsEdge;
    private ArrayList<Integer>[] adj;
    private int[] cluster;
    private double totalEdgeWeights;
    private double[] weightsNode, weightsCluster;
    private boolean initStatus;
    private int[] nNodesPerCluster;
    int[][] nodesPerCluster;
    private boolean clusterStats;
    private String[] keys;
    private Graph() {}

    public Graph(int nNodes, double[] weightsNode, double[][] weightsEdge, ArrayList<Integer>[] adj, int nEdges) {
        this.nNodes = nNodes;
        this.weightsEdge = weightsEdge;
        this.weightsNode = weightsNode;
        this.adj = adj;
        this.nEdges = nEdges;
    }
    public Graph(int nNodes, double[] weightsNode, double[][] weightsEdge,
                 ArrayList<Integer>[] adj, int nEdges, String[] keys) {
        this.nNodes = nNodes;
        this.weightsEdge = weightsEdge;
        this.weightsNode = weightsNode;
        this.adj = adj;
        this.nEdges = nEdges;
        this.keys = keys;
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

    private void deleteClusterStats() {
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

    public Graph getRebuildGraph() {
        Graph reNet;
        //double[] reWeightsNode;
        //double[][] reWeightsEdge;
        //int reNNodes;

        if (!clusterStats) {
            setClusterStatus();
        }

        reNet = new Graph();
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
        Graph re;
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
                              boolean clusterRe, String filename) throws IOException{
        Graph re;
        boolean update, update2;

        if ((cluster == null) || (nNodes == 1))
            return false;
        re  = new Graph();

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
        System.out.println(re.weightsEdge.length);
        ReadInput.writeFile(filename, re.weightsEdge, 0);
        deleteClusterStats();
        return update;

    }
    public static void main(String[] args) throws IOException{
        double resolution;
        Random random;
        String file = "E:\\Code\\CommunityDetection\\data\\louvain\\";
        random = new Random(100);
        String filename = "web-NotreDame";
        String filepath = file + filename + ".txt";
        String fileout = file + filename + "_out.txt";
        Graph testNet = ReadInput.readMessFile(filepath, "\t");
        resolution = testNet.getTotalEdgeWeights();

        Date start = new Date();
        testNet.initNetwork();
        testNet.runLouvain(resolution, random);
        Date end = new Date();

        System.out.printf("Cost: %d\n", end.getTime() - start.getTime());
        System.out.printf("Nodes: %d, Edges: %d\n", testNet.getNNodes(), testNet.getNEdges());
        System.out.println(testNet.getNClusters());
        System.out.println(testNet.calcQuality(resolution));
        ReadInput.writeFile(fileout, testNet.getCluster(), testNet.getKeys(), 1);
    }
}
