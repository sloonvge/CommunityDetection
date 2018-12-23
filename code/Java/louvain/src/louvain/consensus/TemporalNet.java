package louvain.consensus;

import java.io.IOException;
import java.util.Random;

public class TemporalNet {
    public static void main(String[] args) throws IOException{
        String filename1, filename2, outputfile;
        String[] filenames;
        double[][] con, con2;
        boolean complete;
        double resolution, t, max_weight, tmp_weight;
        int max_index, nNodes;

        //filename = "C:\\Users\\moer\\Desktop\\jupyterpy\\CommunityDetection\\data\\cd_no_demo.txt";
        filename1 = "E:\\Data\\network\\community\\social_out\\CollegeMsg_out_9_11_out.txt";
        filename2 = "E:\\Data\\network\\community\\social_out\\CollegeMsg_out_10_11_out.txt";
        outputfile = "E:\\Data\\network\\community\\social_end\\CollegeMsg_out_9_11_10_11_out.txt";
        filenames = new String[2];
        filenames[0] = filename1;
        filenames[1] = filename2;

        con = ConMat.con2FileMat(filenames, ",", 1, true);
        con = ConsensusCluster.conMatFinal(con, 1, 30);
        System.out.println("Complete!");
        Graph c = ConMat.conMatNet(con);
        c.initNetwork();
        c.runLouvain(c.getTotalEdgeWeights(), new Random(0));

        System.out.println();
        System.out.println(c.getNNodes() - 1);
        System.out.println(c.getNClusters());
        ReadInput.writeFile(outputfile, c.getCluster(), null, 1);
    }
}
