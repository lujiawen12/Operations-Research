package vns;

import vo.CityInfo;
import vo.Individual;
import vo.Population;

import java.io.*;

public class Tools {

    public static void copyIndividual(Individual destIndv, Individual srcIndv) {
        destIndv.setCityNum(srcIndv.getCityNum());
        destIndv.setIndvCode(srcIndv.getIndvCode());
        destIndv.setFitness(srcIndv.getFitness());
    }

    //检查个体合法性
    public static boolean checkIndvCode(Individual indv, CityInfo cityInfo) {
        return checkIndvCode(indv.getIndvCode(), cityInfo);
    }

    public static boolean checkIndvCode(int[] route, CityInfo cityInfo) {
        int cityNum = cityInfo.getCityNum();
        int[] flag = new int[cityNum];
        for (int i = 0; i < cityNum; i++) flag[i] = 0;
        for (int item : route) {
            if (item < 0 || item >= cityNum)
                return false;
            flag[item] += 1;
            if (flag[item] > 1)
                return false;
        }
        int sum = 0;
        for (int i = 0; i < cityNum; i++) sum += flag[i];
        return sum == cityNum;
    }

    //计算个体的适配值
    public static void calIndvFit(Individual indv, CityInfo cityInfo) {
        double[][] dist = cityInfo.getDist();
        int[] route = indv.getIndvCode();
        double fitness = dist[route[route.length-1]][route[0]];
        for (int i = 0; i < route.length-1; i++) fitness += dist[route[i]][route[i + 1]];
        indv.setFitness(fitness);
    }

    public static double calDistOnRoute(int[] route, CityInfo cityInfo) {
        double[][] dist = cityInfo.getDist();
        double fitness = dist[route[route.length-1]][route[0]];
        for (int i = 0; i < route.length-1; i++) fitness += dist[route[i]][route[i + 1]];
        return fitness;
    }

    //从txt文件读取数据
    public static CityInfo readData(String fileName) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(fileName));
        String line = br.readLine();
        int cityNum = Integer.parseInt(line);
        CityInfo cityInfo = new CityInfo(cityNum);
        double[] x = new double[cityNum];
        double[] y = new double[cityNum];
        int index = 0;
        line = br.readLine();
        String[] strs = line.split(" ");
        while(strs != null && strs.length > 1) {
            x[index] = Double.parseDouble(strs[0]);
            y[index] = Double.parseDouble(strs[1]);
            index++;
            line = br.readLine();
            if (line != null)
                strs = line.split(" ");
            else
                strs = null;
        }
        br.close();
        cityInfo.setxCoords(x);
        cityInfo.setyCoords(y);
        return cityInfo;
    }

    //计算城市之间距离
    public static void calCityDist(CityInfo cityInfo) {
        int n = cityInfo.getCityNum();
        double[] x = cityInfo.getxCoords();
        double[] y = cityInfo.getyCoords();
        double[][] dist = new double[n][n];
        for (int i = 0; i < n; i++) {
            dist[i][i] = 0;
            for(int j = i+1; j < n; j++) {
                dist[i][j] = Math.sqrt(Math.pow(x[i]-x[j],2) + Math.pow(y[i]-y[j],2));
                dist[j][i] = dist[i][j];
            }
        }
        cityInfo.setDist(dist);
    }


//    //全排列
//    public static void traverseSolve(Individual individual, CityInfo cityInfo) {
//        int n = individual.getCityNum();
//        int[] perm = new int[n];
//        for (int i = 0; i < n; i++) {
//            perm[i] = i;
//        }
//        traverse(perm, 0, perm.length-1, individual, cityInfo);
//    }
//    private static void traverse(int[] perm, int low, int high, Individual individual, CityInfo cityInfo) {
//        if (low == high) {
//            double fitness = calDistOnRoute(perm, cityInfo);
//            if (fitness < individual.getFitness()) {
//                individual.setIndvCode(perm);
//                individual.setFitness(fitness);
//            }
//        }
//        else {
//            for (int i = low; i <= high; i++) {
//                swap(perm, low, i);
//                traverse(perm, low + 1, high, individual, cityInfo);
//                swap(perm, low, i);
//            }
//        }
//    }
//    private static void swap(int[] perm, int a, int b) {
//        int tmp = perm[a];
//        perm[a] = perm[b];
//        perm[b] = tmp;
//    }

}
