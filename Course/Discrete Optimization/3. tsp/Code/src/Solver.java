import vns.Initialize;
import vns.Tools;
import vns.VNS;
import vo.CityInfo;
import vo.Individual;
import vo.Population;

import java.io.*;


public class Solver {

    public static void main(String[] args) {
        try {
            solve(args);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Read the instance, solve it, and print the solution in the standard output
     */
    public static void solve(String[] args) throws IOException {
//        String fileName = null;
//        for (String arg : args) {
//            if (arg.startsWith("-file="))
//                fileName = arg.substring(6);
//        }
//        if (fileName == null)
//            return;
        //String fileName = "./data/tsp_5_1";
        String fileName = "./data/tsp_33810_1";

        // read the data
        CityInfo cityInfo = Tools.readData(fileName);
        Tools.calCityDist(cityInfo);


        //kNN + vns
//        int popNum = 100, k = 2;
//        Population population = Initialize.initialPop(cityInfo, popNum, k);
//        for (int i = 0; i < 500; i++) {
//            for (Individual individual : population.getIndvs()) {
//                VNS.solve(individual, cityInfo);
//            }
//            population.updateIndv();
//        }
//
//        System.out.println(population.getBestIndv().getFitness()+" 0");
//        for (int cityId : population.getBestIndv().getIndvCode()) {
//            System.out.print(cityId + " ");
//        }
//        System.out.println();


    }
}