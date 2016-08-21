package advancedAlgorithmsAndComplexity;

import java.io.*;
import java.util.*;

import advancedAlgorithmsAndComplexity.Evacuation.Edge;
import advancedAlgorithmsAndComplexity.Evacuation.FlowGraph;

public class AirlineCrews { // Need to change back to the original class name
    private FastScanner in;
    private PrintWriter out;
    private HashMap<Integer, Integer> flow;

    public static void main(String[] args) throws IOException {
        new AirlineCrews().solve(); //need to change back to the original class name
    }

    public void solve() throws IOException {
        in = new FastScanner();
        out = new PrintWriter(new BufferedOutputStream(System.out));
        boolean[][] bipartiteGraph = readData();
        int[] matching = findMatching(bipartiteGraph);
        writeResponse(matching);
        out.close();
    }

    boolean[][] readData() throws IOException {
        int numLeft = in.nextInt();
        int numRight = in.nextInt();
        boolean[][] adjMatrix = new boolean[numLeft][numRight];
        for (int i = 0; i < numLeft; ++i)
            for (int j = 0; j < numRight; ++j)
                adjMatrix[i][j] = (in.nextInt() == 1);
        return adjMatrix;
    }
    
  private HashMap<Integer, HashSet<Integer>> computeResidual(HashMap<Integer, HashSet<Integer>> listEdges, 
		  HashMap<Integer, Integer> path)
  {
	  //System.out.println("beginTest");
	  if (null == path){return listEdges;}
	  int curVertex = -2;
	  while (curVertex!= -1)
	  {
		  int priorVertex = path.get(curVertex);
		  //System.out.println("prvtx " + priorVertex);
		  //System.out.println("crvtx" + curVertex);
		  HashSet<Integer> priorVertexEdges =  listEdges.get(priorVertex);
		  HashSet<Integer> curVertexEdges = listEdges.get(curVertex);
		  priorVertexEdges.remove(curVertex);
		  curVertexEdges.add(priorVertex);
		  this.flow.put(priorVertex, curVertex);
		  //this.flow.put(curVertex, -4);
		  curVertex = priorVertex;
		  
	  }
	  //System.out.println("------");
	  /*
	  int priorVertex = path.get(curVertex);
	  HashSet<Integer> priorVertexEdges =  listEdges.get(priorVertex);
	  HashSet<Integer> curVertexEdges = listEdges.get(curVertex);
	  priorVertexEdges.remove(curVertex);
	  curVertexEdges.add(priorVertex);
	  this.flow.put(priorVertex, curVertex);
	  this.flow.put(curVertex, -4);
	  */
	  	  
	  return listEdges;
  }
    
    private HashMap<Integer, HashSet<Integer>> convertToLinkedList(boolean[][] bipartiteGraph)
    {
    	this.flow = new HashMap<Integer, Integer>();
    	HashMap<Integer, HashSet<Integer>> listRepOfEdges = new HashMap<Integer, HashSet<Integer>>();
    	HashSet<Integer> sourceEdges = new HashSet<Integer>();
    	HashSet<Integer> terminusEdges = new HashSet<Integer>();
    	int lengthVal = bipartiteGraph.length;
    	
    	for (int i=0; i<bipartiteGraph.length; i++)
    	{
    		sourceEdges.add(i);
    	HashSet<Integer> edges = new HashSet<Integer>();
    		for (int j=0; j<bipartiteGraph[0].length; j++)
    		{
    			int jVal = j + lengthVal;
    			HashSet<Integer> terminalEdges = new HashSet<Integer>();
    			terminalEdges.add(-2);
    			listRepOfEdges.put(jVal, terminalEdges);
    			if (bipartiteGraph[i][j]){
    				edges.add(jVal);
    			}
    		}
    		
    		listRepOfEdges.put(i, edges);
    		listRepOfEdges.put(-1, sourceEdges);
    		listRepOfEdges.put(-2, terminusEdges);
    	}
    	return listRepOfEdges ;
    }
    
    private static HashMap<Integer, Integer> bfs(HashMap<Integer, HashSet<Integer>> listEdges, int from, int to)
    {
    	//ArrayList<Edge> path = new ArrayList<Edge> ();
    	HashSet<Integer> visited = new HashSet<Integer>();
    	HashMap<Integer, Integer> mapOfParentEdge = new HashMap<Integer, Integer>();
    	LinkedList<Integer> queue = new LinkedList<Integer>();
    	int curVertex = from;
    	queue.add(curVertex);
    	
    	while (!queue.isEmpty())
    	{
    		curVertex = queue.removeFirst();
    		visited.add(curVertex);
    		//System.out.println( curVertex);
    		if (curVertex==to){return mapOfParentEdge;}
    		
    		HashSet<Integer> curEdges = listEdges.get(curVertex);
    		for (Integer edgeVertex:curEdges){
    			
    			if (! visited.contains(edgeVertex))
    			{
    				mapOfParentEdge.put(edgeVertex, curVertex);
    				queue.add(edgeVertex);
    			}
    		}
    	}
    	return null;
    		
   	}

    private int[] findMatching(boolean[][] bipartiteGraph) {
        // Replace this code with an algorithm that finds the maximum
        // matching correctly in all cases.
        int numLeft = bipartiteGraph.length;
        int numRight = bipartiteGraph[0].length;
        
        int[] matching = new int[numLeft];
        Arrays.fill(matching, -1);
        
        HashMap<Integer, HashSet<Integer>> graph = convertToLinkedList( bipartiteGraph);
        for (int i=0; i<numLeft+numRight; i++)
        {
        	this.flow.put(i, -4);
        }
        
        HashMap<Integer, Integer> nextPath =  bfs(graph, -1, -2);
        while (null != nextPath)
        {
        	graph = computeResidual(graph, nextPath);
        	nextPath =  bfs(graph, -1, -2);
       
        }
        
        /*
        boolean[] busyRight = new boolean[numRight];
        for (int i = 0; i < numLeft; ++i)
            for (int j = 0; j < numRight; ++j)
                if (bipartiteGraph[i][j] && matching[i] == -1 && !busyRight[j]) {
                    matching[i] = j;
                    busyRight[j] = true;
                }
                */
        for (int i=0; i<numLeft; i++)
        {
        	int t = this.flow.get(i);
        	//System.out.println("t: "+t);
        	
        	if (t>-1)
        	{
        		matching[i] = t-numLeft;
        	}
        }
        return matching;
    }

    private void writeResponse(int[] matching) {
        for (int i = 0; i < matching.length; ++i) {
            if (i > 0) {
                out.print(" ");
            }
            if (matching[i] == -1) {
                out.print("-1");
            } else {
                out.print(matching[i] + 1);
            }
        }
        out.println();
    }

    static class FastScanner {
        private BufferedReader reader;
        private StringTokenizer tokenizer;

        public FastScanner() {
            reader = new BufferedReader(new InputStreamReader(System.in));
            tokenizer = null;
        }

        public String next() throws IOException {
            while (tokenizer == null || !tokenizer.hasMoreTokens()) {
                tokenizer = new StringTokenizer(reader.readLine());
            }
            return tokenizer.nextToken();
        }

        public int nextInt() throws IOException {
            return Integer.parseInt(next());
        }
    }
}
