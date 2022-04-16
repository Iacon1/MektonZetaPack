// By Iacon1
// Created 09/13/2021
// Maps an AxialHexCoord to a T
// https://www.redblobgames.com/grids/hexagons/#map-storage

package Modules.HexUtilities.HexStructures.Axial;

import java.util.ArrayList;
import java.util.List;

import Modules.HexUtilities.HexStructures.HexMap;

public abstract class AxialHexMap<T> implements HexMap<AxialHexCoord, T>
{
	private List<ArrayList<T>> hexes;
	
	private int columns; // Size of array
	private int rows;
	
	/** Sets the columns & rows of the map; Clears the map!
	 * 
	 * @param columns Length in q-axis.
	 * @param rows Length in r-axis.
	 */
	public void setDimensions(int columns, int rows) // Sets new dimensions for map
	{
		this.columns = columns;
		this.rows = rows;
		
		hexes = new ArrayList<ArrayList<T>>();
		for (int i = 0; i < columns; ++i)
		{
			hexes.add(new ArrayList<T>());
			for (int j = 0; j < rows; ++j)
			{
				hexes.get(i).add(null); // !
			}
		}
	}
	public int getColumns()
	{
		return columns;
	}
	public int getRows()
	{
		return columns;
	}

	/** Returns the first valid r-value for a given q-value
	 * 
	 * @param q Column we're looking at.
	 */
	public abstract int firstRow(int q);	
	/** Returns the last valid r-value for a given q-value.
	 * 
	 *  @param q Column we're looking at.
	 */
	public int lastRow(int q) {return (rows - 1) + firstRow(q);}
	
	public boolean inBounds(AxialHexCoord coord)
	{
		if (coord.r < firstRow(coord.q) || coord.r > lastRow(coord.q)) // OOB r
			return false;
		else if (coord.q < 0 || coord.q >= columns) // OOB q
			return false;
		else return true;
	}
	public void setHex(AxialHexCoord coord, T t)
	{
		if (!inBounds(coord)) return;
		else
		{
			int r = coord.r - firstRow(coord.q);
			hexes.get(coord.q).set(r, t);
		}
	}
	public T getHex(AxialHexCoord coord)
	{
		if (!inBounds(coord)) return null;
		else
		{
			int r = coord.r - firstRow(coord.q);
			return hexes.get(coord.q).get(r);
		}
	}
}
