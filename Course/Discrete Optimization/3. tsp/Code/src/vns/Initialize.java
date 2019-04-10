package vns;

import vo.CityInfo;
import vo.Individual;
import vo.Population;

import java.util.*;

/**
 * The Initialization phase for evolution algorithms
 * Contains kNN currently
 */

public class Initialize {
    static class Node {
        double distance;
        int id;
    }

    //初始化种群
    public static Population initialPop(CityInfo cityInfo, int popNum, int k) {
        Population pop = new Population(popNum, cityInfo.getCityNum());
        Individual[] indvs = new Individual[popNum];
        for (int i = 0; i < popNum; i++) {
            indvs[i] = new Individual(cityInfo.getCityNum());
            knnInitial(cityInfo, k, indvs[i]);
        }
        pop.setIndvs(indvs);
        pop.updateIndv();
        return pop;
    }


    //k近邻初始化
    private static void knnInitial(CityInfo cityInfo, int k, Individual indv) {
        Random r = new Random();
        int cityNum = cityInfo.getCityNum();
        double[][] cityDist = cityInfo.getDist();
        int[] route = new int[cityNum];
        int index = 0;
        List<String> nodeLeft = new LinkedList<>();
        for (int i = 0; i < cityNum; i++) nodeLeft.add(String.valueOf(i));
        int curNode = r.nextInt(cityNum);
        nodeLeft.remove(String.valueOf(curNode));
        route[index++] = curNode;

        while(!nodeLeft.isEmpty()) {
            List<Node> nodes = new ArrayList<>();
            for (String str : nodeLeft) {
                Node node = new Node();
                node.distance = cityDist[curNode][Integer.valueOf(str)];
                node.id = Integer.valueOf(str);
                nodes.add(node);
            }
            if (nodes.size() > k) {
                curNode = selectNextNode(k, nodes);
                nodeLeft.remove(String.valueOf(curNode));
                route[index++] = curNode;
            }
            else {
                Collections.shuffle(nodeLeft);
                for (String str : nodeLeft) {
                    route[index++] = Integer.valueOf(str);
                }
                nodeLeft.clear();
            }
        }
        if (Tools.checkIndvCode(route, cityInfo)) {
            indv.setIndvCode(route);
            Tools.calIndvFit(indv, cityInfo);
        }
        else
            System.out.println("Error encoding");
    }

    private static int selectNextNode(int k, List<Node> nodes) {
        Comparator<Node> cmp = new Comparator<Node>() {
            @Override
            public int compare(Node o1, Node o2) {
                if (Math.abs(o1.distance-o2.distance) < 1E-7) return 0;
                else return Double.compare(o2.distance, o1.distance);
            }
        };
        //最大堆 Top k
        PriorityQueue<Node> q = new PriorityQueue<>(k, cmp);
        for (Node node : nodes) {
            if (q.size() < k)
                q.add(node);
            else {
                assert q.peek() != null;
                if(node.distance < q.peek().distance) {
                    q.remove();
                    q.add(node);
                }
            }
        }

        Random r = new Random();
        int selectIndx = r.nextInt(q.size());
        List<Node> list = new ArrayList<>(q);
        return list.get(selectIndx).id;
    }


}
