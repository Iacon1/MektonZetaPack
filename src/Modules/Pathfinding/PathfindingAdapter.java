// By Iacon1
// Created 09/15/2021
// Adapts pathfinding for other things than integers

package Modules.Pathfinding;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Supplier;

public class PathfindingAdapter<T, A extends PathfindingAlgorithm>
{
	public static interface Cost<T>
	{
		public int cost(T a, T b);
	}
	protected Cost<T> costFunc;
	public static interface Distance<T>
	{
		public int cost(T a, T b);
	}
	protected Distance<T> distFunc;
	public static interface Neighbors<T>
	{
		public List<T> neighbors(T value);
	}
	protected Neighbors<T> nearFunc;
	
	private Supplier<A> supplier;
	
	public PathfindingAdapter(Cost<T> costFunc, Distance<T> distFunc, Neighbors<T> nearFunc, Supplier<A> supplier)
	{
		this.costFunc = costFunc;
		this.distFunc = distFunc;
		this.nearFunc = nearFunc;
		this.supplier = supplier;
	}
	
	public LinkedList<T> findOptimalPath(List<T> list, T a, T b)
	{
		PathfindingGraph graph = new PathfindingGraph(
				(x, y) -> (costFunc.cost(list.get(x), list.get(y))),
				(x, y) -> (distFunc.cost(list.get(x), list.get(y)))
				);
		
		graph.setSize(list.size());
		for (int i = 0; i < list.size(); ++i)
		{
			List<T> neighbors = nearFunc.neighbors(list.get(i));
			for (int j = 0; j < neighbors.size(); ++j)
			{
				int jIndex = list.indexOf(neighbors.get(j));
				if (jIndex == -1) continue;
				else graph.addLink(i, jIndex);
			}
		}
		
		A algo = supplier.get();
		LinkedList<Integer> path = algo.findOptimalPath(graph, list.indexOf(a), list.indexOf(b));
		if (path == null) return null;
		LinkedList<T> pathValue = new LinkedList<T>();
		for (int i = path.size() - 1; i >= 0; --i)
		{
			pathValue.add(list.get(path.getFirst()));
			path.remove();
		}
		
		return pathValue;
	}
}
