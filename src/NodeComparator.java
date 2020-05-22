/*******************************************************************
 * Group #: 37
 * Member: Jessmer John Palanca
 * 		   Ajay Katoch
 * Assignment: 8-Puzzle with A* Algorithm Implementation
 * Course: ECE479 Artificial Intelligence, University of Arizona
 * Date: April 02, 2020
 * File: NodeComparator.java
 ******************************************************************/

import java.util.Comparator;

public class NodeComparator implements Comparator<Node> {

	@Override
	public int compare(Node n1, Node n2) {
		// TODO Auto-generated method stub
		if (n1.priority > n2.priority) {
			return 1;
		}
		else if(n1.priority < n2.priority) {
			return -1;
		}
		return 0;
	}

}
