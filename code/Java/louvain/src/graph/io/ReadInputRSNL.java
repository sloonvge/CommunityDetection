package graph.io;

/**
 * Created by wanjx on 2018/12/22.
 */

import louvain.original.Louvain;
import louvain.rsnl.RSNL;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

public class ReadInputRSNL {
    private BufferedReader bufferedReader;
    private double lineWeight;
    private int node1, node2;
    private String[] splitLine;
    private String lineText;

    private int nNodes, nEdges;
    private RSNL G;
    private HashMap<String, Integer> st;

    public ReadInputRSNL() {}

    private void initGraph(String filename, String sp, int start) throws IOException {
        this.bufferedReader = new BufferedReader(new FileReader(filename));
        this.nEdges = this.nNodes = 0;

        while ((this.lineText = this.bufferedReader.readLine()) != null) {
            this.splitLine = this.lineText.split(sp);
            this.node1 = Integer.parseInt(this.splitLine[0]);
            if (this.node1 > this.nNodes)
                this.nNodes = this.node1;
            this.node2 = Integer.parseInt(this.splitLine[1]);
            if (this.node2 > this.nNodes)
                this.nNodes = this.node2;
            if (this.node1 != this.node2)
                this.nEdges++;
        }
        this.nNodes += (start == 0) ? 1 : 0;
        this.bufferedReader.close();

        this.G = new RSNL(this.nNodes, this.nEdges);
    }

    public RSNL readFile (String filename, String sp, int start) throws IOException {
        AdjListRSNL adjList;

        initGraph(filename, sp, start);

        adjList = new AdjListRSNL(this.G);

        this.bufferedReader = new BufferedReader(new FileReader(filename));
        while ((this.lineText = this.bufferedReader.readLine()) != null) {
            this.splitLine = this.lineText.split(sp);

            this.node1 = Integer.parseInt(this.splitLine[0]) - start;
            this.node2 = Integer.parseInt(this.splitLine[1]) - start;
            if (this.node1 != this.node2) {
                this.lineWeight = (this.splitLine.length > 2) ? Double.parseDouble(this.splitLine[2]) : 1;

                adjList.CreateGraph(this.node1, this.node2, this.lineWeight);
            }
        }
        this.bufferedReader.close();
        return adjList.getGraph();
    }

    private void initGraphHashMap(String filename, String sp) throws IOException {
        String node1, node2;

        this.st = new HashMap<>();
        this.bufferedReader = new BufferedReader(new FileReader(filename));
        this.nEdges = 0;
        while ((this.lineText = this.bufferedReader.readLine()) != null) {
            this.splitLine = this.lineText.split(sp);
            node1 = this.splitLine[0];
            if (!this.st.containsKey(node1)){
                this.st.put(node1, this.st.size());
            }
            node2 = this.splitLine[1];
            if (!this.st.containsKey(node2)){
                this.st.put(node2, this.st.size());
            }
            if (node1 != node2)
                this.nEdges++;
        }
        this.bufferedReader.close();

        this.nNodes = st.size();
        this.G = new RSNL(this.nNodes, this.nEdges);
    }

    public RSNL readMessFile(String filename, String sp) throws IOException{
        String node1, node2;
        AdjListRSNL adjList;

        initGraphHashMap(filename, sp);
        adjList = new AdjListRSNL(this.G);

        this.bufferedReader = new BufferedReader(new FileReader(filename));
        while ((this.lineText = this.bufferedReader.readLine()) != null){
            this.splitLine = this.lineText.split(sp);
            node1 = this.splitLine[0];
            int v = this.st.get(node1);

            node2 = this.splitLine[1];
            int w = this.st.get(node2);

            if (v != w) {
                this.lineWeight = (this.splitLine.length > 2) ? Double.parseDouble(this.splitLine[2]) : 1;
                adjList.CreateGraph(v, w, this.lineWeight);
            }
        }
        this.bufferedReader.close();

        return adjList.getGraph();
    }

    public RSNL readMat(double[][] mat) {
        AdjListRSNL adjList;

        this.nNodes = mat.length;
        this.nEdges = 0;
        this.G = new RSNL(this.nNodes, this.nEdges);

        adjList = new AdjListRSNL(this.G);
        for (int i = 0; i < this.nNodes; i++){
            for (int j = i+1; j < this.nNodes; j++) {
                double w = mat[i][j];
                if (w != 0){
                    this.nEdges++;
                    adjList.CreateGraph(i, j, w);
                }
            }
        }

        return adjList.getGraph();
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
