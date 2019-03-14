import java.io.*;
import java.util.List;
import java.util.ArrayList;

/**
 * The class <code>Solver</code> is an implementation of a greedy algorithm to solve the knapsack problem.
 *
 */
public class Solver {
    
    /**
     * The main class
     */
    public static void main(String[] args) {
        try {
            solve(args);
        } catch (IOException | CloneNotSupportedException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Read the instance, solve it, and print the solution in the standard output
     */
    public static void solve(String[] args) throws IOException, CloneNotSupportedException {
        String fileName = "./data/ks_lecture_dp_2";
        if (fileName == null)
            return;

        // read the lines out of the file
        List<String> lines = new ArrayList<>();
        try (BufferedReader input = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = input.readLine()) != null) {
                lines.add(line);
            }
        }

        // parse the data in the file
        String[] firstLine = lines.get(0).split("\\s+");
        int itemsNum = Integer.parseInt(firstLine[0]);
        int capacity = Integer.parseInt(firstLine[1]);
        List<Item> items = new ArrayList<>(itemsNum);
        for (int i = 0; i < itemsNum; i++) {
            Item item = new Item();
            String line = lines.get(i+1);
            String[] parts = line.split("\\s+");
            item.setId(i);
            item.setValue(Integer.parseInt(parts[0]));
            item.setWeight(Integer.parseInt(parts[1]));
            items.add(item);
        }

        List<KnapsackSolver> solvers = new ArrayList<>();
        solvers.add(new GreedySolver(items, capacity));
        solvers.get(0).solve().print();
    }

}






