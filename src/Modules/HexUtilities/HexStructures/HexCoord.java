// By Iacon1
// Created 09/13/2021
// Hex coord interface

package Modules.HexUtilities.HexStructures;

import java.util.List;

import GameEngine.IntPoint2D;
import Modules.HexUtilities.HexDirection;

public interface HexCoord
{
	/** Adds delta's individual coords to our own and returns the result.
	 * 
	 * @param delta Coord to add.
	 */
	public <T extends HexCoord> T rAdd(HexCoord delta);
	
	/** Multiplies our individual coords by factor and returns the result.
	 * 
	 * @param factor Factor to multiply by.
	 */
	public <T extends HexCoord> T rMultiply(int factor);
	
	/** Gets the unit vector for a particular direction.
	 *  
	 * @param dir Direction to get.
	 */
	public <T extends HexCoord> T getUnitVector(HexDirection dir);
	
	/** Gets the direction relative to this coord that a target is in.
	 * 
	 *  @Param target Target to point towards.
	 */
	public <T extends HexCoord> HexDirection getDirectionTo(HexCoord target);
	
	/** Gets our neighbor in a particular direction.
	 *  
	 * @param dir   Direction to get.
	 */
	public <T extends HexCoord> T getNeighbor(HexDirection dir);

	/** Gets the distance to the target.
	 *  
	 * @param target Coord to measure to.
	 */
	public int distance(HexCoord target);
	
	/** Gets a straight line to the target.
	 *  
	 * @param target Coord to go to.
	 */
	public <T extends HexCoord> List<T> straightLine(HexCoord target);

	/** Gets all hexes within a certain distance of us.
	 *
	 * @param r Radius.
	 */
	public <T extends HexCoord> List<T> withinDistance(int r);
	
	/** Converts to a pixel coordinate. Should return the top-left corner for a hex-shaped sprite,
	 *  which is actually *outside* the hex.
	 */
	public IntPoint2D toPixel();
	/** Converts a pixel coordinate to a hex coordinate.
	 * 
	 * @param point Point to convert.
	 */
	public <T extends HexCoord> T fromPixel(IntPoint2D point);
}
