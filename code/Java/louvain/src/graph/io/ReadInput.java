package graph.io;

/**
 * Created by wanjx on 2018/12/22.
 */

import louvain.original.Louvain;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

public class ReadInput {
    private int nNodes, nEdges;
    private Louvain G;


    public Louvain readFile(String filename, String sp, int start) throws IOException {
        BufferedReader bufferedReader;
        double lineWeight;
        int node1, node2;
        String[] splitLine;
        String lineText;
        AdjList adjList;

        bufferedReader = new BufferedReader(new FileReader(filename));
        this.nEdges = this.nNodes = 0;

        while ((lineText = bufferedReader.readLine()) != null) {
            splitLine = lineText.split(sp);
            node1 = Integer.parseInt(splitLine[0]);
            if (node1 > this.nNodes)
                this.nNodes = node1;
            node2 = Integer.parseInt(splitLine[1]);
            if (node2 > this.nNodes)
                this.nNodes = node2;
            if (node1 != node2)
                this.nEdges++;
        }
        this.nNodes += (start == 0) ? 1 : 0;
        bufferedReader.close();

        this.G = new Louvain(this.nNodes, this.nEdges);
        adjList = new AdjList(G);

        bufferedReader = new BufferedReader(new FileReader(filename));
        while ((lineText = bufferedReader.readLine()) != null) {
            splitLine = lineText.split(sp);

            node1 = Integer.parseInt(splitLine[0]) - start;
            node2 = Integer.parseInt(splitLine[1]) - start;
            if (node1 != node2) {
                lineWeight = (splitLine.length > 2) ? Double.parseDouble(splitLine[2]) : 1;

                adjList.CreateGraph(node1, node2, lineWeight);
            }
        }
        bufferedReader.close();
        return adjList.getGraph();
    }

    public static Louvain readMessFile(String filename, String sp) throws IOException{

        BufferedReader bufferedReader;
        String[] splitLine;
        HashMap<String, Integer> st;
        int nNodes, nEdges;
        String node1, node2;
        int[] cluster;
        double[][] weightsEdge;
        double lineWeight;
        ArrayList<Integer>[] adj;
        double[] weightsNode, weightsCluster;
        String lineText;
        Louvain network;

        /*
         *获取文件的行数，即边数
         */
        st = new HashMap<String, Integer>();
        bufferedReader = new BufferedReader(new FileReader(filename));
        nEdges = 0;
        while ((lineText = bufferedReader.readLine()) != null) {
            splitLine = lineText.split(sp);
            node1 = splitLine[0];
            if (!st.containsKey(node1)){
                st.put(node1, st.size());
            }
            node2 = splitLine[1];
            if (!st.containsKey(node2)){
                st.put(node2, st.size());
            }
        }
        nNodes = st.size();
        bufferedReader.close();

        String[] keys = new String[st.size()];
        for (String node: st.keySet()) {
            keys[st.get(node)] = node;
        }
        /*
         *构建网络
         */
        weightsEdge = new double[nNodes][nNodes];
        adj = (ArrayList<Integer>[]) new ArrayList[nNodes];


        weightsNode = new double[nNodes];
        bufferedReader = new BufferedReader(new FileReader(filename));
        while ((lineText = bufferedReader.readLine()) != null){
            splitLine = lineText.split(sp);
            node1 = splitLine[0];
            int v = st.get(node1);

            node2 = splitLine[1];
            int w = st.get(node2);

            lineWeight = (splitLine.length > 2) ? Double.parseDouble(splitLine[2]) : 1;

            if (v == w) {
                continue;
            }

            weightsNode[v] += lineWeight;
            weightsNode[w] += lineWeight;
            weightsEdge[v][w] += lineWeight;
            weightsEdge[w][v] += lineWeight;
            if (adj[v] == null)
                adj[v] = new ArrayList<>();
            if (adj[w] == null)
                adj[w] = new ArrayList<>();
            if (!adj[v].contains(w) && (lineWeight > 0.05)) {
                adj[v].add(w);
                adj[w].add(v);
                nEdges++;
            }

        }
        bufferedReader.close();
        //每个节点的邻居个数

        //


        network = new Louvain(nNodes, weightsNode, weightsEdge, adj, nEdges, keys);

        return network;
    }
    public static Louvain readComMat(double[][] conMat) {
        Louvain network;
        int nNodes, nEdges;
        double[] weightsNode;
        double[][] weightsEdge;
        ArrayList<Integer>[] adj;

        nNodes = conMat.length;
        nEdges = 0;
        weightsNode = new double[nNodes];
        adj = (ArrayList<Integer>[]) new ArrayList[nNodes];
        weightsEdge = conMat;
        for (int i = 0; i < nNodes; i++){
            adj[i] = new ArrayList<>();
            for (int j = 0; j < nNodes; j++) {
                if (conMat[i][j] != 0){
                    adj[i].add(j);
                    nEdges++;
                    weightsNode[i] += conMat[i][j];
                }
            }
        }
        network = new Louvain(nNodes, weightsNode, weightsEdge, adj, nEdges);
        return network;

    }
    public static void writeFile(String filename, int[] cluster, String[] keys, int start) throws IOException{
        BufferedWriter bufferedWriter;
        int i;

        bufferedWriter = new BufferedWriter(new FileWriter(filename));
        if (keys != null) {
            for (i = start; i < cluster.length; i++) {
                bufferedWriter.write(keys[i] + "," + Integer.toString(cluster[i]));
                bufferedWriter.newLine();
            }
        }else {
            for (i = start; i < cluster.length; i++) {
                bufferedWriter.write(Integer.toString(cluster[i]));
                bufferedWriter.newLine();
            }
        }

        bufferedWriter.close();
    }
    public static void writeFile(String filename, double[][] weightsEdge, int start) throws IOException{
        BufferedWriter bufferedWriter;
        int i, j;

        bufferedWriter = new BufferedWriter(new FileWriter(filename));
        if (weightsEdge != null) {
            for (i = start; i < weightsEdge.length; i++) {
                for (j = start; j < weightsEdge.length; j++) {
                    if ((weightsEdge[i][j] != 0) && (i < j)) {
                        bufferedWriter.write(Integer.toString(i) + "," + Integer.toString(j) + "," +
                                + weightsEdge[i][j]);
                        bufferedWriter.newLine();
                    }
                }
            }
        }

        bufferedWriter.close();
    }

}
