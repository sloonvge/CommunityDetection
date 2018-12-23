package CPM;

import java.util.HashMap;
import java.util.Vector;

/**
 * Created by wanjx on 2018/11/13.
 */

public class test {
    public static void main(String[] args) {
        int N = 18000;
        HashMap<String, Double>[] mat;
        //double [][]mat = new double[N][N];
        mat = (HashMap<String, Double>[]) new HashMap[N];
        mat[1] = new HashMap<>();
        mat[1].put("1", 1.0);
        System.out.println(mat[1].size());
        System.out.println(mat[1].get("1"));
    }
}
