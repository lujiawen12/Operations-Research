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
    public static void solve(String[] args) throws IOException {
//        String fileName = null;
//        for (String arg : args) {
//            if (arg.startsWith("-file="))
//                fileName = arg.substring(6);
//        }
//        if (fileName == null)
//            return;
        String fileName = "./data/gc_20_1";
        //String fileName = "./data/gc_10_1.txt";


        // read the lines out of the file
        List<String> lines = new ArrayList<>();
        BufferedReader input =  new BufferedReader(new FileReader(fileName));
        try {
            String line;
            while (( line = input.readLine()) != null){
                lines.add(line);
            }
        }
        finally {
            input.close();
        }

        // parse the data in the file & use the adjacency list to represent the undirected graph
        String[] firstLine = lines.get(0).split(" ");
        int V = Integer.parseInt(firstLine[0]);
        int E = Integer.parseInt(firstLine[1]);
        List<List<Integer>> adjList = new ArrayList<>();
        for (int i = 0; i < V; i++) {
            adjList.add(new ArrayList<Integer>());
        }
        for (int i = 1; i <= E; i++) {
            String[] parts = lines.get(i).split(" ");
            int pre = Integer.parseInt(parts[0]);
            int post = Integer.parseInt(parts[1]);
            adjList.get(pre).add(post);
            adjList.get(post).add(pre);
        }


        ColoringSolver solver = new BacktrackingSolver(V, E, adjList);
        solver.solve().print();

    }
}