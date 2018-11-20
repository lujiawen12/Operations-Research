import algorithm.DP;
import algorithm.Initialize;
import algorithm.Tools;
import vo.CityInfo;
import vo.Individual;
import vo.Population;

import java.io.*;
import java.util.*;

public class TSP {



    public static void main(String[] args) throws IOException {
        String fileName = "./data/BEN10.DAT";
        String resultName = "result.txt";
        CityInfo cityInfo = Tools.readData(fileName);
        Tools.calCityDist(cityInfo);

        //Dynamic Programming
        Individual indv1 = DP.solve(cityInfo);
        System.out.println(indv1.getFitness());
        indv1.printIndvCode();

        //Traverse
        Individual indv2 = new Individual(cityInfo.getCityNum());
        Tools.traverseSolve(indv2, cityInfo);
        System.out.println(indv2.getFitness());
        indv2.printIndvCode();

        //kNN + VNS
        int popNum = 200, k = 2;
        Population population = Initialize.initialPop(cityInfo, popNum, k);

        for (int i = 0; i < 500; i++) {
            for (Individual individual : population.getIndvs()) {
                algorithm.VNS.solve(individual, cityInfo);
            }
            population.updateIndv();
        }
        algorithm.Tools.writeResult(resultName, population);
    }


}
