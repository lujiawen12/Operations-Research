package vo;

public class CityInfo {
    private int cityNum;
    private double[] xCoords;
    private double[] yCoords;
    private double[][] dist;

    public CityInfo(int n) {
        cityNum = n;
        xCoords = new double[n];
        yCoords = new double[n];
        dist = new double[n][n];
    }

    public int getCityNum() {
        return cityNum;
    }

    public void setCityNum(int cityNum) {
        this.cityNum = cityNum;
    }

    public void setxCoords(double[] xCoords) {
        this.xCoords = xCoords;
    }

    public double[] getxCoords() {
        return xCoords;
    }

    public void setyCoords(double[] yCoords) {
        this.yCoords = yCoords;
    }

    public double[] getyCoords() {
        return yCoords;
    }

    public void setDist(double[][] dist) {
        this.dist = dist;
    }

    public double[][] getDist() {
        return dist;
    }
}
