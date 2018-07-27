package testModule;

import org.ujmp.core.DenseMatrix;
import org.ujmp.core.Matrix;
import org.ujmp.core.calculation.Calculation;

import java.math.BigDecimal;

public class SimplexMethod {

    private static double[][] a = {{0,5,1,0,0},
            {6,2,0,1,0},
            {1,1,0,0,1}};                           //系数矩阵
    private static double[] b = {15, 24, 5};        //资源常数
    private static double[] c = {2, 1, 0, 0, 0};    //价值系数

    private static int m = a.length;    //m个约束
    private static int n = a[0].length; //n个决策变量
    private static Matrix A = DenseMatrix.Factory.importFromArray(a);       //系数矩阵 m*n
    private static Matrix b_hat = DenseMatrix.Factory.importFromArray(b).transpose();   //资源常数 m*1
    private static Matrix C = DenseMatrix.Factory.importFromArray(c);       //价值系数 1*n
    private static Matrix theta = DenseMatrix.Factory.zeros(m, 1);  //b的检验数 m*1
    private static int[] basedVar = new int[m];     //基变量，存基变量下标
    private static Matrix sigma = DenseMatrix.Factory.zeros(1, n);  //检验数
    private static Matrix B = DenseMatrix.Factory.zeros(m, m);
    private static Matrix B_inv = DenseMatrix.Factory.zeros(m, m);
    private static Matrix C_B = DenseMatrix.Factory.zeros(1, m);

    private static double result = -1;              //结果
    private static int idxOfIn = -1;                //换入变量的下标
    private static int idxOfOut = -1;               //换出变量的下标


    public static void main(String[] args) {

        //输入数据
        inputNums();
        //找初始基变量
        findInitBasedVariables();

        //判断是否最优解
        while(!isOptimum()) {
            //找换入变量
            idxOfIn = getVariableIn();
            //找换出变量
            idxOfOut = getVariableOut();
            //如果idxOfOut返回-1, 该线性规划问题存在无界解
            if(idxOfOut == -1) {
                return;
            }
            update();
            printVector();

            System.out.println("\n");
        }

        printOptimum();

    }

    //输入数据，先在代码中写入数据，后期再加，先把初始检验数赋值为价值系数
    private static void inputNums() {
        sigma = C.clone();
        theta = b_hat.clone();
        System.out.println();
    }

    //找基变量，简单的拿最后m个决策变量，后期可优化，存储在basedVar数组中
    private static void findInitBasedVariables() {
        //取n个决策变量的最后m个作为基变量
        for (int i = 0; i < m; i++) {
            basedVar[m-i-1] = n-i-1;
        }

        System.out.println("基变量为：");
        for (int i = 0; i < basedVar.length; i++) {
            System.out.print("x" + (basedVar[i]) + "\t");
        }

        //赋值B矩阵，B逆矩阵, C_B
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < m; j++) {
                B.setAsDouble(A.getAsDouble(j, basedVar[i]), j, i);
            }
            C_B.setAsDouble(C.getAsDouble(0, basedVar[i]),0, i);
        }
        B_inv = B.inv();
        System.out.println();
    }

    //判断是否最优解，并计算检验数yita向量
    private static boolean isOptimum() {

        boolean hasPossitiveSigma= false;
        for (int i = 0; i < n; i++) {
            BigDecimal data1 = new BigDecimal(sigma.getAsDouble(0, i));
            BigDecimal data2 = new BigDecimal(0.00001);
            if( data1.compareTo(data2) > 0) {
                hasPossitiveSigma = true;
                break;
            }
        }
        System.out.println("是否最优解：" + !hasPossitiveSigma);
        return !hasPossitiveSigma;

    }

    //确定换入变量，返回换入变量的下标
    private static int getVariableIn() {
        //遍历检验数
        int index = 0;
        System.out.println("检验数如下：");
        for (int i = 0; i < sigma.getColumnCount(); i++) {
            System.out.print(sigma.getAsDouble(0, i) + "\t");
            if (sigma.getAsDouble(0,i) > sigma.getAsDouble(0, index)) {
                index = i;
            }
        }
        System.out.println();
        System.out.println("换入变量是x" + (index));

        return index;
    }

    // 确定换出变量，返回换出变量在基变量向量中的下标
    private static int getVariableOut() {

        Matrix b_bar = B_inv.mtimes(b_hat);
        Matrix P = B_inv.mtimes(A);

        System.out.println("theta：");
        for (int i = 0; i < m; i++) {
            if (Double.compare(P.getAsDouble(i, idxOfIn), 0) != 0) {
                double kesi = b_bar.getAsDouble(i, 0) / P.getAsDouble(i, idxOfIn);
                theta.setAsDouble(kesi,i, 0);
            }
            else {
                theta.setAsDouble(Integer.MAX_VALUE, i, 0);
            }
            System.out.print(theta.getAsDouble(i, 0) + "\t");
        }
        System.out.println();

        int index = 0;
        for (int i = 0; i < theta.getRowCount(); i++) {
            if(theta.getAsDouble(i, 0) < 0){
                System.out.println("该方程有无界解...");
                return -1;
            }else {
                if(theta.getAsDouble(i, 0) < theta.getAsDouble(index, 0))
                    index = i;
            }
        }
        System.out.println("换出变量是:x" + (basedVar[index]));
        return index;
    }

    //更新函数 需要进一步优化
    private static void update() {
        //换入变量代替换出变量
        if(idxOfIn != -1 && idxOfOut != -1){
            //第idxOfOut个基变量换为x idxOfIn
            basedVar[idxOfOut] = idxOfIn;
        }

        //更新B矩阵，B逆矩阵, C_B
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < m; j++) {
                B.setAsDouble(A.getAsDouble(j, basedVar[i]), j, i);
            }
            C_B.setAsDouble(C.getAsDouble(0, basedVar[i]),0, i);
        }

        Matrix P = B_inv.mtimes(A);
        Matrix gamma = P.selectColumns(Calculation.Ret.NEW, idxOfIn);
        double item = -1 * gamma.getAsDouble(idxOfOut, 0);
        gamma = gamma.divide(item);
        gamma.setAsDouble(-1 / item, idxOfOut, 0);
        Matrix Gamma = DenseMatrix.Factory.eye(m, m);
        for (int i = 0; i < m; i++) {
            Gamma.setAsDouble(gamma.getAsDouble(i,0), i, idxOfOut);
        }
        B_inv = Gamma.mtimes(B_inv);

        //B_inv = B.inv();  //备选方案

        //更新检验数
        sigma = C.minus((C_B.mtimes(B_inv)).mtimes(A));

        System.out.println();
    }

    //输出系数矩阵
    private static void printVector() {
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                System.out.print(A.getAsDouble(i,j) + "\t");
            }
            System.out.println(b_hat.getAsDouble(i,0));
        }
        System.out.println("-----------------------------------------------");
    }

    //输出最优解
    private static void printOptimum() {
        result = (C_B.mtimes(B_inv)).mtimes(b_hat).getAsDouble(0,0);
        System.out.println("最优解：z = " + result);
    }

}
