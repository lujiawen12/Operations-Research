package algorithm;

import vo.CityInfo;
import vo.Individual;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class VNS {

    //VNS框架，串行结构，shaking & VND
    public static void solve(Individual individual, CityInfo cityInfo) {
        int k = 1;
        int kMax = 4;
        Individual copyIndv = new Individual(individual.getCityNum());
        while(k < kMax) {
            Tools.copyIndividual(copyIndv, individual);
            shaking(copyIndv, k, cityInfo);
            vnd(copyIndv, cityInfo);
            if (copyIndv.getFitness() < individual.getFitness()) {
                Tools.copyIndividual(individual, copyIndv);
                k = 1;
            }
            else {
                k += 1;
            }
        }
    }

    //shaking：reinsert k nodes in a greedy way, enlarge the neighborhood structure
    private static void shaking(Individual individual, int k, CityInfo cityInfo){
        int[] route = individual.getIndvCode();
        int n = route.length;
        Random r = new Random();
        List<Integer> selectNode = new ArrayList<>(k);
        selectNode.add(route[r.nextInt(n)]);
        for (int i = 1; i < k; i++) {
            int x;
            do {
                x = r.nextInt(n);
            }while(selectNode.contains(route[x]));
            selectNode.add(route[x]);
        }

        List<Integer> newRoute = new ArrayList<>(n);
        for (int item : route) {
            if (!selectNode.contains(item)){
                newRoute.add(item);
            }
        }

        double[][] cityDist = cityInfo.getDist();
        for (Integer it : selectNode) {
            double insertDist = cityDist[newRoute.get(newRoute.size()-1)][it]
                    +cityDist[it][newRoute.get(0)];
            int insertIndx = 0;
            for (int i = 1; i < newRoute.size(); i++) {
                double tmp = cityDist[newRoute.get(i-1)][it]
                        +cityDist[it][newRoute.get(i)];
                if (tmp < insertDist) {
                    insertDist = tmp;
                    insertIndx = i;
                }
            }
            newRoute.add(insertIndx, it);
        }

        for (int i = 0; i < n; i++) {
            route[i] = newRoute.get(i);
        }
        Tools.calIndvFit(individual, cityInfo);
    }

    //VND 2-opt -> Or-opt
    private static void vnd(Individual individual, CityInfo cityInfo) {
        int k = 0;
        Individual copyIndv = new Individual(individual.getCityNum());
        boolean flag = true;
        while (flag) {
            switch(k){
                case 0:
                    Tools.copyIndividual(copyIndv, individual);
                    twoOpt(copyIndv, cityInfo);
                    if (copyIndv.getFitness() < individual.getFitness()) {
                        k = 0;
                        Tools.copyIndividual(individual, copyIndv);
                    }
                    else {
                        k++;
                    }
                    break;
                case 1:
                    Tools.copyIndividual(copyIndv, individual);
                    orOpt(copyIndv, cityInfo);
                    if (copyIndv.getFitness() < individual.getFitness()) {
                        k = 0;
                        Tools.copyIndividual(individual, copyIndv);
                    }
                    else {
                        k++;
                    }
                    break;
                case 2:
                    flag = false;
                    break;
            }
        }

    }

    //VND: 2-opt & or-opt
    //2-opt
    private static void twoOpt(Individual individual, CityInfo cityInfo) {
        double[][] cityDist = cityInfo.getDist();
        int[] route = individual.getIndvCode();
        if (route.length < 4)   return;
        double oldArcLen, newArcLen;
        for (int i = 0; i < route.length-3; i++) {
            for (int j = i+2; j < route.length-1; j++) {
                oldArcLen = cityDist[route[i]][route[i+1]] + cityDist[route[j]][route[j+1]];
                newArcLen = cityDist[route[i]][route[j]] + cityDist[route[i+1]][route[j+1]];
                if (newArcLen < oldArcLen) {
                    twoOptInverse(route, i+1, j);
                }
            }
        }

        if (Tools.checkIndvCode(route, cityInfo)) {
            Tools.calIndvFit(individual, cityInfo);
        }
    }
    //[x,y] inverse
    private static void twoOptInverse(int[] route, int x, int y) {
        int[] perm = new int[y-x+1];
        int index = 0;
        for (int i = y; i >= x; i--) perm[index++] = route[i];
        for (int i = x; i <= y; i++) route[i] = perm[i-x];
    }
    //Or-opt
    private static void orOpt(Individual individual, CityInfo cityInfo) {
        double[][] cityDist = cityInfo.getDist();
        int[] route = individual.getIndvCode();
        if (route.length < 6)   return;
        double oldArcLen, newArcLen;
        for (int i = 1; i < route.length-4; i++) {
            for (int j = i+3; j < route.length-1; j++) {
                oldArcLen = cityDist[route[i-1]][route[i]] + cityDist[route[i+1]][route[i+2]]
                        + cityDist[route[j]][route[j+1]];
                newArcLen = cityDist[route[i-1]][route[i+2]] + cityDist[route[j]][route[i]]
                        + cityDist[route[i+1]][route[j+1]];
                if (newArcLen < oldArcLen) {
                    orOptReLocate(route, i, j);
                }
            }
        }
        if (Tools.checkIndvCode(route, cityInfo)) {
            Tools.calIndvFit(individual, cityInfo);
        }
    }
    //(i,i+1) 插入到 (j, j+1)
    private static void orOptReLocate(int[] route, int x, int y) {
        List<Integer> newRoute = new ArrayList<>(route.length);
        for (int i = 0; i < x; i++) {
            newRoute.add(route[i]);
        }
        for (int i = x+2; i <= y; i++) {
            newRoute.add(route[i]);
        }
        newRoute.add(route[x]);
        newRoute.add(route[x+1]);
        for (int i = y+1; i < route.length; i++) {
            newRoute.add(route[i]);
        }

        for (int i = 0; i < route.length; i++) {
            route[i] = newRoute.get(i);
        }
    }

}
