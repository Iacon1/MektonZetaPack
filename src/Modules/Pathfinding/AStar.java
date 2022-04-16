// By Iacon1
// Created 09/15/2021
//

package Modules.Pathfinding;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

public class AStar implements PathfindingAlgorithm // https://www.redblobgames.com/pathfinding/a-star/introduction.html#astar
{
	private static class PriNode
	{
		public int node;
		public int pri;
		
		public PriNode(int node, int pri)
		{
			this.node = node;
			this.pri = pri;
		}
	}
	@Override
	public LinkedList<Integer> findOptimalPath(PathfindingGraph g, int a, int b)
	{
		Map<Integer, Integer> prevNode = new HashMap<Integer, Integer>(); // -1 -> no node
		Map<Integer, Integer> bestCost = new HashMap<Integer, Integer>(); 
		
		PriorityQueue<PriNode> queue = new PriorityQueue<PriNode>(1, (x, y) -> x.pri - y.pri); // Init
		queue.add(new PriNode(a, 0));
		prevNode.put(a, -1);
		bestCost.put(a, 0);
		
		boolean found = false;
		
		while (!queue.isEmpty()) // While more to look at
		{
			int node = queue.poll().node; // Grab the next one
			if (node == b)
			{
				found = true;
				break; // We found the target!
			}
			
			List<Integer> neighbors = g.getNeighbors(node);
			for (int i = 0; i < neighbors.size(); ++i) // Look at its neighbors
			{
				int ind = neighbors.get(i);
				if (g.getCost(node,  ind) < 0) continue; // Negative cost can cause a loop, and we need walls somehow
				int cost = bestCost.get(node) + g.getCost(node, ind);
				if (bestCost.get(ind) == null || cost < bestCost.get(ind)) // Found a record for fastest way to get to i!
				{
					bestCost.put(ind, cost);
					prevNode.put(ind, node);
					int dist = cost + g.getDist(ind, b);
					queue.add(new PriNode(ind, dist));
				}
			}	
		}
		if (!found) return null;
		
		LinkedList<Integer> path = new LinkedList<Integer>();
		
		int i = b;
		while (i != a)
		{
			path.addFirst(i);
			i = prevNode.get(i);
		}
		return path;
	}

}
