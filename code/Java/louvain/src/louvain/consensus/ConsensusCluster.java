package louvain.consensus;

import java.io.IOException;
import java.util.Random;

public class ConsensusCluster {
    public static void main(String[] args) throws IOException{
        String filename, outputfile;
        double[][] con;
        Graph conNetwork;
        double resolution;


        //filename = "C:\\Users\\moer\\Desktop\\jupyterpy\\CommunityDetection\\data\\cd_no_demo.txt";
        filename = "E:\\Code\\jupyterpy\\tensorflow\\stock\\net.edgelist";
        outputfile = "E:\\Code\\jupyterpy\\tensorflow\\stock\\net.edgelist_consensus";
        con = ConMat.conFileMat(ConMat.conFileNet(filename," ", 0, false), 1);
        //con = ConMat.con2FileMat(filename, " ", 0, false);
        con = ConsensusCluster.conMatFinal(con, 0, 10);
        System.out.println("Complete!");
        Graph c = ConMat.conMatNet(con);
        c.initNetwork();
        c.runLouvain(c.getTotalEdgeWeights(), new Random(0));

        System.out.println();
        System.out.println(c.getNNodes());
        System.out.println(c.getNClusters());
        ReadInput.writeFile(outputfile, c.getCluster(), null, 0);
    }
    public static double[][] conMatFinal(double[][] con, int start, int iters) {
        boolean complete;
        double resolution, t, max_weight;
        int max_index, nNodes;

        nNodes = con.length;
        complete = false;
        OUT:{
            for (int i = start; i < nNodes; i++) {
                for (int j = start; j < nNodes; j++) {
                    if ((con[i][j] != 0) && (con[i][j] != 1.0)) {
                        complete = false;
                        break OUT;
                    }
                }
            }
        }
        t = 0.9997;
        max_weight = 0.0;
        max_index = -1;
        for (int i = start; i < nNodes; i++) {
            for (int j = start; j < nNodes; j++) {
                if (con[i][j] > max_weight) {
                    max_weight = con[i][j];
                    max_index = j;
                }
                if ((con[i][j] < t)) {
                    con[i][j] = 0.0;
                }

            }
            if ((max_weight < t)) {
                con[i][max_index] = max_weight;
            }
        }
        while (!complete) {
            con = ConMat.conFileMat(ConMat.conMatNet(con), iters);
            for (double[] d: con) {
            }
            OUT:{
                for (int i = start; i < nNodes; i++) {
                    for (int j = start; j < nNodes; j++) {
                        if ((con[i][j] != 0) && (con[i][j] != 1.0)) {
                            complete = false;
                            break OUT;
                        }
                    }
                }
            }
            complete = true;
        }
        return con;
    }
}
