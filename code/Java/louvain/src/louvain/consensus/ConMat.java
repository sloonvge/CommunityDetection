package louvain.consensus;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class ConMat {

    public static Graph conFileNet(String filename, String sp, int start, boolean mess)  throws IOException {
        Graph network;
        if (mess) {
            network = ReadInput.readMessFile(filename, sp);
        }else {
            network = ReadInput.readInputFile(filename, sp, start);
        }
        return network;
    }
    public static Graph conMatNet(double[][] con){
        Graph network;
        network = ReadInput.readComMat(con);
        return network;
    }
    public static double[][] conFileMat(Graph network, int iters){

        double resolution;
        double[][] conFileMat;
        int[] cluster;
        ArrayList<Integer>[] cluBag;

        conFileMat = new double[network.getNNodes()][network.getNNodes()];
        for (int i = 0; i < iters; i++) {
            network.initNetwork();
            resolution = network.getTotalEdgeWeights();
            network.runLouvain(resolution, new Random(i));
            cluster = network.getCluster();
            cluBag = (ArrayList<Integer>[]) new ArrayList[network.getNClusters()];
            for (int j = 0; j < cluster.length; j++) {
                if (cluBag[cluster[j]] == null) {
                    cluBag[cluster[j]] = new ArrayList<>();
                }
                cluBag[cluster[j]].add(j);
            }
            conFileMat = setConsensusMat(cluBag, conFileMat, iters);
        }
        return conFileMat;
    }


    public static double[][] con2FileMat(String[] filenames,
                                         String sp, int start, boolean mess) throws IOException{
        double resolution;
        int nNodes, max_nNodes;
        double[][] conFileMat;
        int[] cluster;
        String[] keys;
        ArrayList<Integer>[] cluBag;
        Graph network;

        max_nNodes = 0;
        for (int i = 0; i < filenames.length; i++) {
            nNodes = getMaxId(filenames[i], sp, start);
            if (nNodes > max_nNodes) {
                max_nNodes = nNodes;
            }
        }
        conFileMat = new double[max_nNodes][max_nNodes];
        for (int i = 0; i < filenames.length; i++) {
            network = ConMat.conFileNet(filenames[i],sp,start, mess);
            network.initNetwork();
            resolution = network.getTotalEdgeWeights();
            network.runLouvain(resolution, new Random(10));
            cluster = network.getCluster();
            keys = network.getKeys();

            cluBag = (ArrayList<Integer>[]) new ArrayList[network.getNClusters()];
            for (int j = 0; j < cluster.length; j++) {
                if (cluBag[cluster[j]] == null) {
                    cluBag[cluster[j]] = new ArrayList<>();
                }
                cluBag[cluster[j]].add(Integer.parseInt(keys[j]));
            }
            conFileMat = setConsensusMat(cluBag, conFileMat, 2);
        }
        return conFileMat;
    }
    private static double[][] setConsensusMat(ArrayList<Integer>[] cluBag, double[][] conFileMat, int iters) {
        for (int i = 0; i < cluBag.length; i++) {
            for (int v: cluBag[i]) {
                for (int w: cluBag[i]) {
                    if (w != v) {
                        conFileMat[v][w] += 1.0 / iters;
                    }
                }
            }
        }
        return conFileMat;
    }
    public static void main(String[] args) throws IOException{
        String filename = "C:\\Users\\moer\\Desktop\\jupyterpy\\CommunityDetection\\data\\cd_no_demo.txt";
        double[][] con = ConMat.conFileMat(conFileNet(filename,",", 0, false), 10);
        for (double[] d: con) {
            for (double n : d)
                System.out.printf("%-5.1f", n, "\t");
            System.out.println();
        }
    }
    private static int getMaxId(String filename, String sp, int start) throws IOException{
        BufferedReader bufferedReader;
        String lineText;
        String[] splitLine;
        int node1, nNodes, node2;

        nNodes = 0;
        bufferedReader = new BufferedReader(new FileReader(filename));
        while ((lineText = bufferedReader.readLine()) != null) {
            splitLine = lineText.split(sp);
            node1 = Integer.parseInt(splitLine[0]);
            if (node1 > nNodes)
                nNodes = node1;
            node2 = Integer.parseInt(splitLine[1]);
            if (node2 > nNodes)
                nNodes = node2;
        }
        nNodes += 1;
        return nNodes;
    }
}
