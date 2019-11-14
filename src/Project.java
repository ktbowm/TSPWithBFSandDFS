import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedList;

public class Project {
	//file reading variables
	private static final String FILE = System.getProperty("user.dir") + "\\src\\data\\11PointDFSBFS.tsp";
	private static BufferedReader reader;
	
	//structures for storing information
	static ArrayList<Coordinate> locations = new ArrayList<Coordinate>();
	static LinkedList<Integer> bfsQueue = new LinkedList<Integer>();
	static ArrayList<Integer> shortestPath = new ArrayList<Integer>();
	static ArrayList<Integer> dfsPath = new ArrayList<Integer>();
	static int prev[] = new int[11];
	
	static int s;
	
	//adjacency matrix[from][to] (0=no edge, 1=edge)
	static int matrix[][] = new int[][] {
		{0,1,1,1,0,0,0,0,0,0,0},
		{0,0,1,0,0,0,0,0,0,0,0},
		{0,0,0,1,1,0,0,0,0,0,0},
		{0,0,0,0,1,1,1,0,0,0,0},
		{0,0,0,0,0,0,1,1,0,0,0},
		{0,0,0,0,0,0,0,1,0,0,0},
		{0,0,0,0,0,0,0,0,1,1,0},
		{0,0,0,0,0,0,0,0,1,1,1},
		{0,0,0,0,0,0,0,0,0,0,1},
		{0,0,0,0,0,0,0,0,0,0,1}
	};
	
	//function for reading in file information
	public static void getFileContent(File file) throws IOException {
		FileInputStream stream = new FileInputStream(file);
		reader = new BufferedReader(new InputStreamReader(stream));
		
		String line = null;
		while (!(line = reader.readLine()).equals("NODE_COORD_SECTION")) {
			//ignore metadata
		}
		
		while ((line = reader.readLine()) != null) {
			//split each location line on spaces to separate information
			String[] components = line.split("\\s+");
			
			//store each location using the Coordinate class
			Coordinate coord = new Coordinate();
			coord.setIdentifier(Integer.parseInt(components[0]));
			coord.setXCoord(Float.parseFloat(components[1]));
			coord.setYCoord(Float.parseFloat(components[2]));
			
			//add each location to the locations ArrayList
			locations.add(coord);
		}
		
		reader.close();
	}
	
	//breadth-first search function
	//used http://www.geeksforgeeks.org/breadth-first-traversal-for-a-graph/
	//to help with writing BFS function
	public static void BFS() {		
		//array to keep track of visited locations
		boolean visited[] = new boolean[11];
		
		//add starting location
		bfsQueue.add(locations.get(0).getIdentifier());
		visited[0] = true;
		
		while(bfsQueue.size() != 0) {
			//take location from head of list
			s = bfsQueue.poll();
			
			if(s != 11) {
				for(int x = 0; x < 11; x++) {
					//add unvisited successors of s to the end of the list
					if((matrix[s - 1][x] == 1) && (visited[x] == false)) {
						bfsQueue.add(x + 1);
						visited[x] = true;
						//keep track of predecessors
						prev[x] = s;
					}
				}
			}
		}
		
	}
	
	//depth first search function, does initial steps of dfs
	//used http://www.geeksforgeeks.org/depth-first-traversal-for-a-graph/
	//for implementing DFS function
	public static void DFS() {
		//array to keep track of visited locations
		boolean visited[] = new boolean[11];
		
		//call main dfs function on first location
		DFSUtil(1, visited);
	}
	
	//main dfs implementation
	public static void DFSUtil(int v, boolean visited[]) {
		//add location that is passed to this function
		visited[v - 1] = true;
		dfsPath.add(v);
		
		if(v != 11) {
			for(int x = 0; x < 11; x++) {
				//recursively call this function on unvisited successors of v
				if((matrix[v - 1][x] == 1) && (visited[x] == false)) {
					DFSUtil(x + 1, visited);
				}
			}
		}
		
	}
	
	public static void main(String[] args) throws IOException {
		
		//read in file information
		File file = new File(FILE);
		getFileContent(file);
		
		//variables for calculating the time each algorithm takes
		long startTime;
		long endTime;
		
		//BFS
		System.out.println("BFS: ");
		startTime = System.currentTimeMillis();
		BFS();
		
		//find shortest path from BFS results from prev[]
		int current = 11;
		shortestPath.add(11);
		for(int j = 10; j > 0; j--) {
			if(j == current - 1) {
				shortestPath.add(prev[j]);
				current = prev[j];
			}
		}
		
		//print out BFS shortest path
		for(int j = shortestPath.size() - 1; j >= 0; j--) {
			System.out.print(shortestPath.get(j) + " ");
		}
		
		//print out BFS run time
		System.out.println();
		endTime = System.currentTimeMillis();
		System.out.println("Run time: " + (endTime - startTime));
		
		//DFS
		System.out.println("DFS: ");
		startTime = System.currentTimeMillis();
		DFS();
		
		//print out DFS shortest path
		int iter = 0;
		while(dfsPath.get(iter) != 11) {
			System.out.print(dfsPath.get(iter) + " ");
			iter++;
		}
		System.out.print(dfsPath.get(iter));
		
		//print out DFS run time
		System.out.println();
		endTime = System.currentTimeMillis();
		System.out.println("Run time: " + (endTime - startTime));
		
	}
	
}