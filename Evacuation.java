package advancedAlgorithmsAndComplexity;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.Arrays;
import java.util.*;

public class Evacuation {
    private static FastScanner in;

    public static void main(String[] args) throws IOException {
        in = new FastScanner();

        FlowGraph graph = readGraph();
        
        //List<Integer> edgeIDs = getEdgesFrom(graph, 0);
        //System.out.println(edgeIDs);
       
        
        System.out.println(maxFlow(graph, 0, graph.size() - 1));
    }

    
    private static int maxFlow(FlowGraph graph, int from, int to) {
        int flow = 0;
        /* your code goes here */
        HashMap<Integer, Integer> test =  bfs(graph, from, to);
        FlowGraph newGraph = null;
        while (null != test)
        {
        	//System.out.println("test");
        	newGraph = tracePath( graph, test, from, to);
        	test =  bfs(newGraph, from, to);
        	
        }
        //System.out.println("test3");
        if (null == newGraph){return 0;}
        List<Integer> edgeIDs = newGraph.getIds(0);
        //System.out.println(edgeIDs);
        for (int edgeID : edgeIDs)
        {
        	Edge curEdge = newGraph.getEdge(edgeID);
        	flow += curEdge.flow;
        	//System.out.println("Flow Test:  " + curEdge.from + " " + curEdge.to + " " +curEdge.capacity + " " + curEdge.flow);
        }
                                 
        return flow;
    }
    
    private static ArrayList<Integer> getEdgesFrom(FlowGraph graph, int curVertex)
    {
       	ArrayList<Integer> edgesFrom = new ArrayList<Integer>();
    	List<Integer> idsOfEdgesFromcurVertex = 	graph.getIds(curVertex);
    	for (int numOfEdge : idsOfEdgesFromcurVertex)
    	{
    		Edge neighborEdge = graph.getEdge(numOfEdge);
    		
    		if (0 < neighborEdge.capacity)
    		{
    			edgesFrom.add(numOfEdge);
    		}
       	}
    
    	return edgesFrom;
    	
    }
    
   // private static FlowGraph computeResidual()
    
    private static FlowGraph tracePath(FlowGraph graph, HashMap<Integer, Integer> pathMap, int from, int to )
    {
    	  List<Integer> edgeIDs = getEdgesFrom(graph, 0);
          //System.out.println(edgeIDs);
          
    	if (null != pathMap  )
    	{
    		int curNumOfEdge = pathMap.get(to);
    		Edge curEdge = graph.getEdge(curNumOfEdge);
    		int minMaxFlowForPath = Integer.MAX_VALUE;
    		while (curEdge.from != from)
    		{    			// System.out.println("Edge:  " + curEdge.from + " " + curEdge.to + " " +curEdge.capacity + " " + curEdge.flow);
    		if (curEdge.capacity <minMaxFlowForPath){
				 minMaxFlowForPath = curEdge.capacity;
			 } 
    			curNumOfEdge = pathMap.get(curEdge.from);
    			 curEdge = graph.getEdge(curNumOfEdge);
    			 
    		}
    		//execute for source edge
    		if (curEdge.capacity <minMaxFlowForPath){
				 minMaxFlowForPath = curEdge.capacity;
			 } 
    		
    		//System.out.println("minMax: " +  minMaxFlowForPath );
    	
    	
	    	curNumOfEdge = pathMap.get(to);
			curEdge = graph.getEdge(curNumOfEdge);
			while (curEdge.from != from)
			{   
			
			graph.addFlow(curNumOfEdge, minMaxFlowForPath);
			curEdge.capacity = curEdge.capacity - minMaxFlowForPath;
			Edge edgeMate = graph.edges.get(curNumOfEdge ^ 1);
			edgeMate.capacity += minMaxFlowForPath;
			//System.out.println("Edge2:  " + curEdge.from + " " + curEdge.to + " " +curEdge.capacity + " " + curEdge.flow);
			 
			curNumOfEdge = pathMap.get(curEdge.from);
			curEdge = graph.getEdge(curNumOfEdge);
			
			//System.out.println("EdgeMate:  " + edgeMate.from + " " + edgeMate.to + " " +edgeMate.capacity + " " + edgeMate.flow);
			 
			}
			
			// execute for source edge
			graph.addFlow(curNumOfEdge, minMaxFlowForPath);
			curEdge.capacity = curEdge.capacity - minMaxFlowForPath;
			Edge edgeMate = graph.edges.get(curNumOfEdge ^ 1);
			edgeMate.capacity += minMaxFlowForPath;
			
			//System.out.println("minMax: " +  minMaxFlowForPath );
    	}
    	return graph;
	}
    	
    	
    	//Add Flow to g
    	
    	
    
    //Hashmap maps each integer representing a vertex to the integer id of the edge that was traversed to get to that vertex
    private static HashMap<Integer, Integer> bfs(FlowGraph graph, int from, int to)
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
    		
    		ArrayList<Integer> curEdges = getEdgesFrom(graph, curVertex);
    		for (Integer curNumOfEdge:curEdges){
    			Edge curEdge = graph.getEdge(curNumOfEdge);
    			if (! visited.contains(curEdge.to))
    			{
    				mapOfParentEdge.put(curEdge.to, curNumOfEdge);
    				queue.add(curEdge.to);
    			}
    		}
    		
   	}
      	
    	
    	
    	return null;
    }

    static FlowGraph readGraph() throws IOException {
        int vertex_count = in.nextInt();
        int edge_count = in.nextInt();
        FlowGraph graph = new FlowGraph(vertex_count);

        for (int i = 0; i < edge_count; ++i) {
            int from = in.nextInt() - 1, to = in.nextInt() - 1, capacity = in.nextInt();
            graph.addEdge(from, to, capacity);
        }
        return graph;
    }

    static class Edge {
        int from, to, capacity, flow;

        public Edge(int from, int to, int capacity) {
            this.from = from;
            this.to = to;
            this.capacity = capacity;
            this.flow = 0;
           // System.out.println(from + " " + to + " " + capacity);
        }
    }

    /* This class implements a bit unusual scheme to store the graph edges, in order
     * to retrieve the backward edge for a given edge quickly. */
    static class FlowGraph {
        /* List of all - forward and backward - edges */
        private List<Edge> edges;

        /* These adjacency lists store only indices of edges from the edges list */
        private List<Integer>[] graph;

        public FlowGraph(int n) {
            this.graph = (ArrayList<Integer>[])new ArrayList[n];
            for (int i = 0; i < n; ++i)
                this.graph[i] = new ArrayList<>();
            this.edges = new ArrayList<>();
        }

        public void addEdge(int from, int to, int capacity) {
            /* Note that we first append a forward edge and then a backward edge,
             * so all forward edges are stored at even indices (starting from 0),
             * whereas backward edges are stored at odd indices. */
            Edge forwardEdge = new Edge(from, to, capacity);
            Edge backwardEdge = new Edge(to, from, 0);
            graph[from].add(edges.size());
            edges.add(forwardEdge);
            graph[to].add(edges.size());
            edges.add(backwardEdge);
        }

        public int size() {
            return graph.length;
        }

        public List<Integer> getIds(int from) {
            return graph[from];
        }

        public Edge getEdge(int id) {
            return edges.get(id);
        }

        public void addFlow(int id, int flow) {
            /* To get a backward edge for a true forward edge (i.e id is even), we should get id + 1
             * due to the described above scheme. On the other hand, when we have to get a "backward"
             * edge for a backward edge (i.e. get a forward edge for backward - id is odd), id - 1
             * should be taken.
             *
             * It turns out that id ^ 1 works for both cases. Think this through! */
            edges.get(id).flow += flow;
            edges.get(id ^ 1).flow -= flow;
        }
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

