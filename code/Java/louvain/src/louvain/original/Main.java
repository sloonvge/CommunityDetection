package louvain.original;

import graph.io.ReadInput;

import java.io.IOException;
import java.util.Date;
import java.util.Random;

/**
 * Created by wanjx on 2018/12/22.
 */

public class Main {
    public static void main(String[] args) throws IOException {
        double resolution;
        Random random;
//        String file = "E:\\Code\\CommunityDetection\\data\\";
        String file = "E:\\Code\\jupyterpy\\tensorflow\\stock\\VGG16ConvolutionalAutoEncoder\\";
        random = new Random(100);
        String filename = "club.txt_";
//        String filename = "club.txt_full";
        String filepath = file + filename + "";
        String fileout = file + filename + "_out.txt";

        ReadInput readInput = new ReadInput();
        Louvain testNet = readInput.readFile(filepath, " ", 0);
//        testNet.threshold();
        resolution = testNet.getTotalEdgeWeights();

        System.out.printf("Total Weights: %.3f\n", resolution);

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
