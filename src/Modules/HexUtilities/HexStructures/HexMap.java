// By Iacon1
// Created 09/13/2021
// Ultra-generic hex map

package Modules.HexUtilities.HexStructures;

public interface HexMap<C extends HexCoord, T>
{
	/** Returns whether the coordinate is in bounds.
	 *
	 * @Param coord Coordinate to check.
	 */
	public boolean inBounds(C coord);
	/** Sets the coord to a value if it's valid.
	 *
	 * @Param coord Coordinate to set.
	 * @Param t     Value to set to.
	 */
	public void setHex(C coord, T t);
	/** Returns the value corresponding to coord.
	 *
	 * @Param coord Coordinate to get.
	 */
	public T getHex(C coord);
}
