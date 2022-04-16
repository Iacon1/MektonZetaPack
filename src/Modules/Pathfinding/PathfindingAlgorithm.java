// By Iacon1
// Created 09/15/2021
//

package Modules.Pathfinding;

import java.util.LinkedList;

public interface PathfindingAlgorithm
{
	/** Finds an optimal path from a to b in graph g.
	 * 
	 *  @param g Graph to look through.
	 *  @param a Starting point.
	 *  @param b Ending point.
	 */
	public LinkedList<Integer> findOptimalPath(PathfindingGraph g, int a, int b);
}
