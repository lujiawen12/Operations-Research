package vo;

public class Individual {
    private int cityNum;
    private int[] indvCode;
    private double fitness;

    public Individual(int cityNum) {
        this.cityNum = cityNum;
        indvCode = new int[cityNum];
        fitness = Integer.MAX_VALUE;
    }

    public void printIndvCode() {
        for (int i = 0; i < cityNum; i++) {
            System.out.print(indvCode[i] + "\t");
        }
        System.out.println();
    }


    public int[] getIndvCode() {
        return indvCode;
    }

    public void setIndvCode(int[] indvCode) {
        if (indvCode.length != this.indvCode.length)    return;
        for (int i = 0; i < indvCode.length; i++) {
            this.indvCode[i] = indvCode[i];
        }
    }

    public double getFitness() {
        return fitness;
    }

    public void setFitness(double fitness) {
        this.fitness = fitness;
    }

    public int getCityNum() {
        return cityNum;
    }

    public void setCityNum(int cityNum) {
        this.cityNum = cityNum;
    }
}
