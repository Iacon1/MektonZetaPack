// By Iacon1
// Created 09/15/2021
// Graph for storing pathfinding data
// If cost is -1 then assume blocked

package Modules.Pathfinding;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class PathfindingGraph
{
	protected static interface Cost
	{
		public int cost(int a, int b);
	}
	private Cost costFunc;
	
	protected static interface Distance
	{
		public int cost(int a, int b);
	}
	private Distance distFunc;
	
	private static class Node
	{	
		public HashSet<Integer> neighbors;
		
		public Node()
		{
			neighbors = new HashSet<Integer>();
		}
		
		public void addNeighbor(int neighbor)
		{
			neighbors.add(neighbor);
		}
		public void removeNeighbor(int neighbor)
		{
			neighbors.remove(neighbor);
		}
	}
	
	private List<Node> nodes;
	
	public PathfindingGraph(Cost costFunc, Distance distFunc)
	{
		this.costFunc = costFunc;
		this.distFunc = distFunc;
		nodes = new ArrayList<Node>();
	}
	
	/** Resets the graph for a set size.
	 * 
	 *  @param size Size to reset to.
	 */
	public void setSize(int size)
	{
		nodes = new ArrayList<Node>();
		for (int i = 0; i < size; ++i)
		{
			nodes.add(new Node());
		}
	}
	
	/** Adds a link between a & b.
	 * 
	 * @ param a First node to link.
	 * @ param b Second node to link.
	 */
	public void addLink(int a, int b)
	{
		if (a >= nodes.size() || b >= nodes.size()) return;
		
		nodes.get(a).addNeighbor(b);
		nodes.get(b).addNeighbor(a);
	}
	
	/** Cuts a link between a & b.
	 * 
	 * @ param a First node to cut.
	 * @ param b Second node to cut.
	 */
	public void cutLink(int a, int b)
	{
		if (a >= nodes.size() || b >= nodes.size()) return;
		
		nodes.get(a).removeNeighbor(b);
		nodes.get(b).removeNeighbor(a);
	}
	
	/** Returns the list of links to node.
	 * 
	 * @param node The node to get neighbors of.
	 */
	public List<Integer> getNeighbors(int node)
	{
		List<Integer> neighbors = new ArrayList<Integer>();
		neighbors.addAll(nodes.get(node).neighbors);
		return neighbors;
	}
	
	/** Cost of travelling from two neighboring nodes
	 * 
	 * @param a First node.
	 * @param b Second node.
	 */
	public int getCost(int a, int b)
	{
		return costFunc.cost(a, b);
	}
	
	/** Distance, a seperate form of cost for farther-away nodes
	 * 
	 * @param a First node.
	 * @param b Second node.
	 */
	public int getDist(int a, int b)
	{
		return distFunc.cost(a, b);
	}
}
