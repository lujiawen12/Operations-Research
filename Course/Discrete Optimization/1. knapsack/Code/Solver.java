import java.io.*;
import java.util.List;
import java.util.ArrayList;


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
    private static void solve(String[] args) throws IOException{
        String fileName = null;
        for (String arg : args) {
            if (arg.startsWith("-file="))
                fileName = arg.substring(6);
        }
        if (fileName == null)
            return;
        //String fileName = "./data/ks_106_0";   //just for local test

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
        solvers.add(new BranchAndBoundSolver(items, capacity));
        if (capacity * itemsNum < 0.5*10e9) {
            solvers.add(new DynamicProgrammingSolver(items, capacity));
        }
        for (KnapsackSolver solver : solvers) {
            solver.solve().print();
        }
    }

}






