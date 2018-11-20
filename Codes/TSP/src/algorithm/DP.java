package algorithm;

import vo.CityInfo;
import vo.Individual;

/**
 * Dynamic Programming for TSP
 * The number of cities is limited to 20
 */

public class DP {

    public static Individual solve(CityInfo cityInfo) {
        int n = cityInfo.getCityNum();
        if ( n > 20) {
            return null;
        }
        Individual individual = new Individual(n);
        double[][] cityDist = cityInfo.getDist();
        //动态规划表格，默认起点和终点为编号0的城市
        double[][] table = new double[n][1<<(n-1)];
        for (int i = 1; i < n; i++) {
            table[i][0] = cityDist[0][i];
        }
        double minDist;
        for (int j = 1; j < (1<<(n-1)); j++) {
            for (int i = 1; i < n; i++) {
                minDist = Integer.MAX_VALUE;
                //i不在集合j中
                if (((1<<(i-1)) & j) == 0) {
                    for (int k = 1; k < n; k++) {
                        if (((1<<(k-1)) & j) != 0) {
                            double tmp = cityDist[i][k] + table[k][j-(1<<(k-1))];
                            if (tmp < minDist) {
                                minDist = tmp;
                            }
                        }
                    }
                }
                table[i][j] = minDist;
            }
        }

        //加入返回0的一段路
        minDist = Integer.MAX_VALUE;
        int pre = -1;
        int v = (1<<(n-1))-1;
        for (int i = 1; i < n; i++) {
            double tmp = cityDist[i][0] + table[i][v-(1<<(i-1))];
            if (tmp < minDist) {
                minDist = tmp;
                pre = i;
            }
        }
        table[0][v] = minDist;
        individual.setFitness(table[0][v]);

        //解码出路径
        int[] perm = new int[n];
        perm[0] = 0;
        perm[1] = pre;
        v = (1<<(n-1)) - 1 - (1<<(pre-1));
        for (int i = 2; i < n; i++) {
            int cur = pre;
            minDist = Integer.MAX_VALUE;
            for (int k = 1; k < n; k++) {
                if (((1<<(k-1)) & v) != 0) {
                    double tmp = cityDist[cur][k] + table[k][v-(1<<(k-1))];
                    if (tmp < minDist) {
                        minDist = tmp;
                        pre = k;
                    }
                }
            }
            v -= (1<<(pre-1));
            perm[i] = pre;
        }
        individual.setIndvCode(perm);
        return individual;
    }


}
