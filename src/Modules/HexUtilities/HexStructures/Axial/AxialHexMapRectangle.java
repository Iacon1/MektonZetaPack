// By Iacon1
// Created 09/13/2021
// Rectangular form
// https://www.redblobgames.com/grids/hexagons/#map-storage

package Modules.HexUtilities.HexStructures.Axial;

public class AxialHexMapRectangle<T> extends AxialHexMap<T>
{
	@Override
	public int firstRow(int q)
	{
		return -Math.floorDiv(q, 2);
	}
}
