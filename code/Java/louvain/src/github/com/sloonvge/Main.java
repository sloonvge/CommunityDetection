package github.com.sloonvge;

import graph.io.ReadInput;
import louvain.original.Louvain;

import java.io.IOException;
import java.util.Date;
import java.util.Random;

public class Main {

    public static void main(String[] args) throws IOException {
	// write your code here
        double resolution;
        ReadInput readInput = new ReadInput();
        Random random;
        String file = "D:\\Code\\CommunityDetection\\data\\";
        random = new Random(100);
        String filename = "club.txt";
        String filepath = file + filename + "";
        String fileout = file + filename + "_out.txt";
        Louvain testNet = readInput.readFile(filepath, " ", 0);

        resolution = testNet.getTotalEdgeWeights();
        Date start = new Date();
        testNet.initNetwork();
        testNet.runLouvain(resolution, random);
        Date end = new Date();

        System.out.printf("Cost: %d\n", end.getTime() - start.getTime());
        System.out.printf("Nodes: %d, Edges: %d\n", testNet.getNNodes(), testNet.getNEdges());
        System.out.println(testNet.getNClusters());
        System.out.println(testNet.calcQuality(resolution));
        ReadInput.writeFile(fileout, testNet.getCluster(), testNet.getKeys(), 0);
    }
}
