package vo;

import java.util.Arrays;
import java.util.Comparator;

public class Population {
    private int popNum;
    private int cityNum;
    private Individual[] indvs;
    private Individual bestIndv;
    private Individual[] sortIndvs;

    public Population(int popNum, int cityNum) {
        this.popNum = popNum;
        this.cityNum = cityNum;
        bestIndv = new Individual(cityNum);
        indvs = new Individual[popNum];
        for (int i = 0; i < popNum; i++) {
            indvs[i] = new Individual(cityNum);
        }
        sortIndvs = new Individual[popNum];
        for (int i = 0; i < popNum; i++) {
            sortIndvs[i] = new Individual(cityNum);
        }

    }

    public void updateIndv() {
        sortIndividual();
        if (sortIndvs[0].getFitness() < bestIndv.getFitness()) {
            bestIndv.setFitness(sortIndvs[0].getFitness());
            bestIndv.setIndvCode(sortIndvs[0].getIndvCode());
        }
    }

    public void sortIndividual() {
        for (int i = 0; i < popNum; i++) {
            sortIndvs[i].setIndvCode(indvs[i].getIndvCode());
            sortIndvs[i].setFitness(indvs[i].getFitness());
        }
        Arrays.sort(sortIndvs, new IndvComparator());
    }


    public void setCityNum(int cityNum) {
        this.cityNum = cityNum;
    }

    public int getCityNum() {
        return cityNum;
    }

    public int getPopNum() {
        return popNum;
    }

    public void setPopNum(int popNum) {
        this.popNum = popNum;
    }

    public Individual[] getIndvs() {
        return indvs;
    }

    public void setIndvs(Individual[] indvs) {
        this.indvs = indvs;
    }

    public Individual getBestIndv() {
        return bestIndv;
    }

    public void setBestIndv(Individual bestIndv) {
        this.bestIndv = bestIndv;
    }

    public Individual[] getSortIndvs() {
        return sortIndvs;
    }

    public void setSortIndvs(Individual[] sortIndvs) {
        this.sortIndvs = sortIndvs;
    }
}

class IndvComparator implements Comparator<Individual> {
    @Override
    public int compare(Individual o1, Individual o2) {
        if (o1.getFitness() < o2.getFitness())
            return -1;
        else if (o1.getFitness() > o2.getFitness())
            return 1;
        else
            return 0;
    }
}
