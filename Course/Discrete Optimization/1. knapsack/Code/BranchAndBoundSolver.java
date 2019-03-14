import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.PriorityQueue;

public class BranchAndBoundSolver extends KnapsackSolver {

    private class Node implements Comparable<Node>{
        int h;
        double bound;
        int weight;
        int value;
        List<Item> choseItems;

        Node() {
            h = -1;
            choseItems = new ArrayList<>();
        }

        Node(Node parent) {
            this.h = parent.h + 1;
            this.bound = parent.bound;
            this.value = parent.value;
            this.weight = parent.weight;
            this.choseItems = new ArrayList<>();
            for (Item item : parent.choseItems) {
                try {
                    this.choseItems.add((Item) item.clone());
                } catch (CloneNotSupportedException e) {
                    e.printStackTrace();
                }
            }
        }

        void computeBound(){
            int w = weight;
            bound = value;
            int i = Math.max(0,h);
            while(i < items.size()) {
                Item item = items.get(i);
                if ((w + item.getWeight()) > capacity) {
                    break;
                }
                w += item.getWeight();
                bound += item.getValue();
                i++;
            }
            if (i < items.size())
                bound += (capacity-w) * items.get(i).getRatio();
        }

        @Override
        public int compareTo(Node other) {
            return Double.compare(other.bound, this.bound);
        }
    }


    public BranchAndBoundSolver(List<Item> items, int capacity) {
        super(items, capacity);
    }

    @Override
    public KnapsackSolution solve() {
        Collections.sort(items, Item.byRatio());

        KnapsackSolution solution = new KnapsackSolution();
        solution.setApproach("Branch and Bound");
        solution.setItemsNum(items.size());

        Node best = new Node();
        Node root = new Node();
        root.computeBound();
        PriorityQueue<Node> queue = new PriorityQueue<>();
        queue.offer(root);
        while(!queue.isEmpty()) {
            Node parent = queue.poll();
            if (parent.bound > best.value && parent.h < items.size()-1) {
                Node with = new Node(parent);
                Item item = items.get(with.h);
                with.weight += item.getWeight();
                if (with.weight <= capacity) {
                    with.value += item.getValue();
                    with.choseItems.add(item);
                    with.computeBound();
                    if (with.value > best.value) {
                        best = with;
                    }
                    if (with.bound > best.value) {
                        queue.offer(with);
                    }
                }

                Node without = new Node(parent);
                without.computeBound();
                if (without.bound > best.value) {
                    queue.offer(without);
                }
            }
        }
        solution.setChoseItems(best.choseItems);
        return solution;
    }
}
