package advancedAlgorithmsAndComplexity;

import java.io.*;
import java.util.*;

public class StockCharts {
    private FastScanner in;
    private PrintWriter out;
    private HashMap<Integer, Integer> flow;
    

    public static void main(String[] args) throws IOException {
        new StockCharts().solve();
    }

    public void solve() throws IOException {
        in = new FastScanner();
        out = new PrintWriter(new BufferedOutputStream(System.out));
        int[][] stockData = readData();
        int result = minCharts(stockData);
        writeResponse(result);
        out.close();
    }

    int[][] readData() throws IOException {
        int numStocks = in.nextInt();
        int numPoints = in.nextInt();
        int[][] stockData = new int[numStocks][numPoints];
        for (int i = 0; i < numStocks; ++i)
            for (int j = 0; j < numPoints; ++j)
                stockData[i][j] = in.nextInt();
        return stockData;
    }
    
    private HashMap<Integer, HashSet<Integer>> computeResidual(HashMap<Integer, HashSet<Integer>> listEdges, 
  		  HashMap<Integer, Integer> path)
    {
  	  
  	  if (null == path){return listEdges;}
  	  int curVertex = -2;
  	  while (curVertex!= -1)
  	  {
  		  int priorVertex = path.get(curVertex);
  		  HashSet<Integer> priorVertexEdges =  listEdges.get(priorVertex);
  		  HashSet<Integer> curVertexEdges = listEdges.get(curVertex);
  		  priorVertexEdges.remove(curVertex);
  		  curVertexEdges.add(priorVertex);
  		  this.flow.put(priorVertex, curVertex);
  		  curVertex = priorVertex;
  	 }

  	  	  
  	  return listEdges;
    }
    
    private static HashMap<Integer, Integer> bfs(HashMap<Integer, HashSet<Integer>> listEdges, int from, int to)
    {
    	
    	HashSet<Integer> visited = new HashSet<Integer>();
    	HashMap<Integer, Integer> mapOfParentEdge = new HashMap<Integer, Integer>();
    	LinkedList<Integer> queue = new LinkedList<Integer>();
    	int curVertex = from;
    	queue.add(curVertex);
    	
    	while (!queue.isEmpty())
    	{
    		curVertex = queue.removeFirst();
    		visited.add(curVertex);
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
    
    private HashMap<Integer, HashSet<Integer>> convertToLinkedList(int[][] stockData)
    {
    	this.flow = new HashMap<Integer, Integer>();
    	HashMap<Integer, HashSet<Integer>> listRepOfEdges = new HashMap<Integer, HashSet<Integer>>();
    	HashSet<Integer> sourceEdges = new HashSet<Integer>();
    	HashSet<Integer> terminusEdges = new HashSet<Integer>();
    	int numOfStocks = stockData.length;
    	
    	for (int i=0; i<numOfStocks; i++)
    	{
    		sourceEdges.add(i);
    	HashSet<Integer> edges = new HashSet<Integer>();
    		for (int j=0; j<numOfStocks; j++)
    		{
    			int jVal = j + numOfStocks;
    			HashSet<Integer> terminalEdges = new HashSet<Integer>();
    			terminalEdges.add(-2);
    			listRepOfEdges.put(jVal, terminalEdges);
    			if (compare(stockData[i], stockData[j])){
    				edges.add(jVal);
    			}
    		}
    		
    		listRepOfEdges.put(i, edges);
    		listRepOfEdges.put(-1, sourceEdges);
    		listRepOfEdges.put(-2, terminusEdges);
    	}
    	return listRepOfEdges ;
    }

    private int minCharts(int[][] stockData) {
        // Replace this incorrect greedy algorithm with an
        // algorithm that correctly finds the minimum number
        // of charts on which we can put all the stock data
        // without intersections of graphs on one chart.

        int numStocks = stockData.length;
        HashMap<Integer, HashSet<Integer>> graph = convertToLinkedList( stockData);
        // Each chart is a list of indices of its stocks.
        for (int i=0; i<2*numStocks; i++)
        {
        	this.flow.put(i, -4);
        }
        
        HashMap<Integer, Integer> nextPath =  bfs(graph, -1, -2);
        while (null != nextPath)
        {
        	graph = computeResidual(graph, nextPath);
        	nextPath =  bfs(graph, -1, -2);
       
        }
        int counter = numStocks;
        for (int i=0; i<numStocks; i++)
        {
        	int t = this.flow.get(i);
        	//System.out.println("t: "+t);
        	
        	if (t>-1)
        	{
        		counter -=1;
        	}
        }
        return counter;
        
        
    }

    boolean compare(int[] stock1, int[] stock2) {
        for (int i = 0; i < stock1.length; ++i)
            if (stock1[i] >= stock2[i])
                return false;
        return true;
    }

    private void writeResponse(int result) {
        out.println(result);
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