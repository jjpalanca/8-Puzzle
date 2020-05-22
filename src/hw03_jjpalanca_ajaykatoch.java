/*******************************************************************
 * Group #: 37
 * Member: Jessmer John Palanca
 * 		   Ajay Katoch
 * Assignment: 8-Puzzle with A* Algorithm Implementation
 * Course: ECE479 Artificial Intelligence, University of Arizona
 * Date: April 02, 2020
 * File: Puzzle.java
 ******************************************************************/

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Scanner;
import java.util.Stack;


public class hw03_jjpalanca_ajaykatoch {

	public static void main(String[] args) throws IOException {
		Scanner userInput = new Scanner(System.in);
		ArrayList<Integer> initialState = new ArrayList<Integer>();			// Arraylist variable to store the initial state provided by the user input
		ArrayList<Integer> goalState = new ArrayList<Integer>();			// Arraylist variable to store the goal state provided by the user input
		Node[] states = new Node[4];										// four nodes to store the node states after U,D,L,R moves
		Node goalNodeFound = new Node();									// variable to store the goal node after it is found by traversing through the tree
		goalNodeFound = null;
		Stack<String> moves = new Stack<String>();							// stack variable that stores the moves taken to get from the initial state to the goal state
		Node current = new Node();											// Node variable to use for traversing through the queue
		LinkedList<ArrayList<?>> explored = new LinkedList<ArrayList<?>>();					// Linkedlist variable to store the explored nodes as we traverse through the tree
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));			// UI stream reader
		Comparator<Node> comparator = new NodeComparator();									// Comparator variable 
		PriorityQueue<Node> pQueue = new PriorityQueue<Node>(100, comparator);				// a Priority Queue to use for traversing through the node. It uses a custom 'comparator'.
		
		// Prompt user for the initial state input
		System.out.println("Enter the initial state:");
		for (int i = 0; i < 3; i++) {
			String  lines = br.readLine();    
			
		    String[] strs = lines.trim().split("\\s+");
			
			for (int j = 0; j < strs.length; j++) {
				if (strs[j].equals("_")) {
					initialState.add(0);
				}
				else {
					initialState.add(Integer.parseInt(strs[j]));
				}
			}
		}

		System.out.println();
		Node startNode = new Node();		// start node variable 
		startNode.state = initialState;		// instantiating the start node
		startNode.parentNode = null;
		startNode.move = null;
		startNode.priority = 0;
		startNode.distance = -1;
		
		// Prompt user for the goal state input
		System.out.println("Enter the goal state:");
		for (int i = 0; i < 3; i++) {
			String  lines = br.readLine();    
			
		    String[] strs = lines.trim().split("\\s+");
			
			for (int j = 0; j < strs.length; j++) {
				if (strs[j].equals("_")) {
					goalState.add(0);
				}
				else {
					goalState.add(Integer.parseInt(strs[j]));
				}
			}
		}
	  
		System.out.println();
		Node goalNode = new Node();		// goal node variable
		goalNode.state = goalState;		// instantiating the goal node
		goalNode.parentNode = null;
		goalNode.move = null;
		goalNode.priority = 0;
		goalNode.distance = -1;
		
		
		pQueue.add(startNode);			// add the starting node to the queue
		explored.add(startNode.state);	// adding the starting node to the explored node Linked list
		
		
		// Prompt user for heuristic method
		System.out.println("Select the heuristic \n "
				+ "\ta) Number of misplaced tiles \n"
				+ "\tb) Manhattan distance \n");
		
		
		String heuristicMode = userInput.nextLine();
		
		int numExploredNodes = 1;	// variable to store the number of explored nodes. Set to since the start node has already been explored
		
		// Traversing and expanding the tree using queue. 
		// Keeps expanding the current node until goal node is found.
		while(!pQueue.isEmpty()) {
			current = pQueue.remove();			// holds the least cost node, which is at the end of the queue (right end). This node will be expanded.
			states = nextStates(current);		// expanding the current node
			
			// iterate through the four expanded states/nodes
			for (int i = 0; i < 4; i++) {
				if (states[i] != null) {
					// check if the node is a goal node
					// breaks out of the for loop when goal node is found
					if (states[i].state.equals(goalNode.state)) {
						goalNodeFound = states[i];
						break;
					}
					else {
						// making sure the node havent been explored yet
						if(!explored.contains(states[i].state)){
							states[i].distance = current.distance + 1;			// setting the distance of this node from the start node
							explored.add(states[i].state);						// adding the node to the explored nodes
							states[i].priority = getHeuristicVal(states[i], goalNode, heuristicMode);	// calculate the heuristic value
							pQueue.add(states[i]);								// add this node to the queue
							numExploredNodes++;									// increment the number of explored nodes
						}
					}
						
				}
			}
			// goal node is found. Break out of the while loop
			if (goalNodeFound != null) {
				break;
			}
		}
		
		// add the moves taken to get the goal node into the stack
		while (goalNodeFound.parentNode != null) {
			if (goalNodeFound.move != null) {
				moves.push(goalNodeFound.move);
			}
			goalNodeFound = goalNodeFound.parentNode;
		}
		
		if (goalNodeFound.equals(null)) {
			System.out.println("For the above combination of the initial/goal states, there is no solution.");
		} 
		else {
			int numMoves = moves.size();
			System.out.println();
			System.out.println("Solution:");
			while(!moves.isEmpty()) {
				System.out.println("Move blank " + moves.pop());
			}
				
			System.out.println();
			System.out.println("Given the selected heuristic, the solution required "+numMoves+" moves.");
			System.out.println("The A* explored " + numExploredNodes + " number of nodes to find this solution.");
		}
		
	
		userInput.close();
		
	}
	
	// Function that expands the current state/node 
	// Parameter: current node
	// Return: 4 Node states
	private static Node[] nextStates(Node state) {
		Node state1, state2, state3, state4;
		
		state1 = moveUp(state);
		state2 = moveDown(state);
		state3 = moveLeft(state);
		state4 = moveRight(state);
		
		Node[] states = {state1, state2, state3, state4};
		return states;
	}
	
	// Calculates the heuristic value of the path taken.
	// Parameter: current node, goal node, heuristic mode
	// Return: heuristic value of type int
	private static int getHeuristicVal(Node currNode, Node goalNode, String heuristicMode) {

		switch (heuristicMode) {
		case "a": {
			return getMisplacedTiles(currNode, goalNode);
		}
		case "b": {
			 return getManhattan(currNode, goalNode);
		}
		default:
			throw new IllegalArgumentException("Unexpected value: " + heuristicMode);
		}
	}
	
	// Calculates the heuristic value of Misplaced Tiles.
	// Parameter: current node, goal node
	// Return: heuristic value of type int
	private static int getMisplacedTiles(Node currNode, Node goalNode) {
		int count = 0;
		
		for (int i = 0; i < 9; i++) {
			if (!currNode.state.get(i).equals(0)) {							// not counting the blank position
				if(currNode.state.get(i) != goalNode.state.get(i)) {		
					count++;												// increments the misplaced tiles
				}
			}
		}
		
		return currNode.distance + count;									// f(n) = distance from start node + # of misplaced tiles
	}
	
	// Calculates the heuristic value of Manhattan Distance.
	// Parameter: current node, goal node
	// Return: heuristic value of type int
	private static int getManhattan(Node currNode, Node goalNode) {
		int distance = 0;
		
		for (int i = 0; i < 9; i++) {
			int currNodeX = i % 3;
			int currNodeY = i / 3;
			if (!currNode.state.get(i).equals(0)) {							// ignore the blank tile position
				if(currNode.state.get(i) != goalNode.state.get(i)) {
					for (int j = 0; j < 9; j++) {
						if (currNode.state.get(i) == goalNode.state.get(j)) {
							int goalX = j % 3;
							int goalY = j / 3;
							int dx = Math.abs(currNodeX - goalX);
							int dy = Math.abs(currNodeY - goalY);
							distance += dx + dy;
							break;
						}
					}
				}
			}
		}
		return distance;
	}
	
	// Moves the blank tile up.
	// Parameter: current node
	// Return: new node where blank is moved up one position
	@SuppressWarnings("unchecked")
	private static Node moveUp(Node node) {
		int blank = node.state.indexOf(0);
		
		ArrayList<Integer> newState;
		int temp;
		
		Node childNode = new Node();
		
		// make sure the blank tile is not on the first row
		if(blank > 2) {
			newState = (ArrayList<Integer>)node.state.clone();
			temp = newState.get(blank - 3);
			// switching the two cell values
			newState.set(blank - 3, 0);
			newState.set(blank, temp);
			childNode.state = newState;
			childNode.parentNode = node;
			childNode.distance = node.distance + 1;
			childNode.move = "up";
			return childNode;
			
		}
		else {
			return null;
		}
	}
	
	// Moves the blank tile down.
	// Parameter: current node
	// Return: new node where blank is moved down one position
	@SuppressWarnings("unchecked")
	private static Node moveDown(Node node) {
		int blank = node.state.indexOf(0);
		
		ArrayList<Integer> newState;
		int temp;
		
		Node childNode = new Node();
		
		// make sure the blank tile is not on the last row
		if(blank < 6) {
			newState = (ArrayList<Integer>)node.state.clone();
			temp = newState.get(blank + 3);
			// switching the two cell values
			newState.set(blank + 3, 0);
			newState.set(blank, temp);
			childNode.state = newState;
			childNode.parentNode = node;
			childNode.distance = node.distance + 1;
			childNode.move = "down";
			return childNode;
			
		}
		else {
			return null;
		}
	}
	
	// Moves the blank tile left.
	// Parameter: current node
	// Return: new node where blank is moved left one position	
	@SuppressWarnings("unchecked")
	private static Node moveLeft(Node node) {
		int blank = node.state.indexOf(0);
		
		ArrayList<Integer> newState;
		int temp;
		
		Node childNode = new Node();
		
		// make sure the blank tile is not on the first column
		if(blank % 3 != 0) {
			newState = (ArrayList<Integer>)node.state.clone();
			temp = newState.get(blank - 1);
			// switching the two cell values
			newState.set(blank - 1, 0);
			newState.set(blank, temp);
			childNode.state = newState;
			childNode.parentNode = node;
			childNode.distance = node.distance + 1;
			childNode.move = "left";
			return childNode;
			
		}
		else {
			return null;
		}
	}
	
	// Moves the blank tile right.
	// Parameter: current node
	// Return: new node where blank is moved right one position
	@SuppressWarnings("unchecked")
	private static Node moveRight(Node node) {
		int blank = node.state.indexOf(0);
		
		ArrayList<Integer> newState;
		int temp;
		
		Node childNode = new Node();
		
		// make sure the blank tile is not on the last column
		if((blank +1) % 3 != 0) {
			newState = (ArrayList<Integer>)node.state.clone();
			temp = newState.get(blank + 1);
			// switching the two cell values
			newState.set(blank + 1, 0);
			newState.set(blank, temp);
			childNode.state = newState;
			childNode.parentNode = node;
			childNode.distance = node.distance + 1;
			childNode.move = "right";
			return childNode;
			
		}
		else {
			return null;
		}
	}
	

}
