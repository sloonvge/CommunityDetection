package Louvain;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

public class ReadInput {

    public static Graph readInputFile(String filename, String sp, int start) throws IOException{

        BufferedReader bufferedReader;
        String[] splitLine;
        int nNodes, nEdges, node1, node2;
        double[][] weightsEdge;
        double lineWeight;
        ArrayList<Integer>[] adj;
        double[] weightsNode;
        String lineText;
        Graph network;

        /*
         *获取文件的行数，即边数
         */
        bufferedReader = new BufferedReader(new FileReader(filename));
        nEdges = 0;
        nNodes = 0;
        node1 = -1;
        node2 = -1;
        while ((lineText = bufferedReader.readLine()) != null) {
            splitLine = lineText.split(sp);
            node1 = Integer.parseInt(splitLine[0]);
            if (node1 > nNodes)
                nNodes = node1;
            node2 = Integer.parseInt(splitLine[1]);
            if (node2 > nNodes)
                nNodes = node2;
            nEdges++;
            if (node1 == node2){
                nEdges--;
            }
        }
        nNodes += (start == 0) ? 1: 0;
        bufferedReader.close();
        /*
         *构建网络
         */
        System.out.println(nNodes);
        weightsEdge = new double[nNodes][nNodes];
        adj = (ArrayList<Integer>[]) new ArrayList[nNodes];

        weightsNode = new double[nNodes];
        bufferedReader = new BufferedReader(new FileReader(filename));
        while ((lineText = bufferedReader.readLine()) != null){
            splitLine = lineText.split(sp);
            node1 = Integer.parseInt(splitLine[0]) - start;

            node2 = Integer.parseInt(splitLine[1]) - start;
            if (node1 != node2){
                lineWeight = (splitLine.length > 2) ? Double.parseDouble(splitLine[2]) : 1;
                weightsNode[node1] += lineWeight;
                weightsNode[node2] += lineWeight;
                weightsEdge[node1][node2] += lineWeight;
                weightsEdge[node2][node1] += lineWeight;
                if (adj[node1] == null)
                    adj[node1] = new ArrayList<>();
                if (adj[node2] == null)
                    adj[node2] = new ArrayList<>();
                if (!adj[node1].contains(node2)) {
                    adj[node1].add(node2);
                    adj[node2].add(node1);
                }
            }


        }
        bufferedReader.close();
        //每个节点的邻居个数

        //


        network = new Graph(nNodes, weightsNode, weightsEdge, adj, nEdges);

        return network;
    }
    public static Graph readMessFile(String filename, String sp) throws IOException{

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
        Graph network;

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

            weightsNode[v] += lineWeight;
            weightsNode[w] += lineWeight;
            weightsEdge[v][w] += lineWeight;
            weightsEdge[w][v] += lineWeight;
            if (adj[v] == null)
                adj[v] = new ArrayList<>();
            if (adj[w] == null)
                adj[w] = new ArrayList<>();
            if (!adj[v].contains(w)) {
                adj[v].add(w);
                adj[w].add(v);
                nEdges++;
            }

        }
        bufferedReader.close();
        //每个节点的邻居个数

        //


        network = new Graph(nNodes, weightsNode, weightsEdge, adj, nEdges, keys);

        return network;
    }
    public static Graph readComMat(double[][] conMat) {
        Graph network;
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
        network = new Graph(nNodes, weightsNode, weightsEdge, adj, nEdges);
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
