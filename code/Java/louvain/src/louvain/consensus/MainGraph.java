package louvain.consensus;

import java.io.IOException;
import java.util.Random;

public class MainGraph {
    public static void main(String[] args) throws IOException {
        Graph network;
        String filename, outputfile;
        double resolution;
        Random random;
        random = new Random(100);
        filename = "E:\\Data\\network\\community\\social\\CollegeMsg_out_5_7.txt";
        outputfile = "E:\\Data\\network\\community\\social_out\\CollegeMsg_out_5_7_out.txt";
        //filename = "E:\\Data\\network\\community\\benchmark\\benchmark\\Debug\\network.dat";

        network = ConMat.conFileNet(filename,",", 1, true);
        resolution = network.getTotalEdgeWeights();
        network.initNetwork();
        System.out.println(network.getNEdges() + "--" + network.getNClusters());
        //System.out.println(network.calcQuality(resolution));


        network.runLouvain(resolution, random);
        System.out.println(network.getNClusters());
        System.out.println(network.calcQuality(resolution));

        ReadInput.writeFile(outputfile, network.getCluster(), network.getKeys(), 0);













    }
}
