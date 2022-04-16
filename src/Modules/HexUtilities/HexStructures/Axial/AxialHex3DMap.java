// By Iacon1
// Created 09/13/2021
// Hex map with Z axis

package Modules.HexUtilities.HexStructures.Axial;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import Modules.HexUtilities.HexStructures.HexMap;

public class AxialHex3DMap<M extends AxialHexMap<T>, T> implements HexMap<AxialHexCoord3D, T>
{
	private List<M> maps;
	private transient Supplier<M> supplier; // Should be OK to transient
	
	private int columns; // Size of any one map
	private int rows; 
	private int levels; // # of maps
	
	public AxialHex3DMap(Supplier<M> supplier)
	{
		this.supplier = supplier;
	}
	
	
	/** Sets the width, length, and height of the map; Clears the map!
	 * 
	 * @param columns Length in q-axis.
	 * @param rows    Length in r-axis.
	 * @param levels  Length in z-axis.
	 */
	public void setDimensions(int columns, int rows, int levels) // Sets new dimensions for map
	{
		this.columns = columns;
		this.rows = rows;
		this.levels = levels;
		
		maps = new ArrayList<M>();
		for (int k = 0; k < levels; ++k)
		{
			M m = supplier.get();
			m.setDimensions(columns, rows);
			maps.add(m);
		}
	}
	public int getColumns()
	{
		return columns;
	}
	public int getRows()
	{
		return rows;
	}
	public int getLevels()
	{
		return levels;
	}
	
	/** Returns the first valid r-value for a given q-value
	 * 
	 * @param q Column we're looking at.
	 */
	public int firstRow(int q) {return maps.get(0).firstRow(q);}
	/** Returns the last valid r-value for a given q-value.
	 * 
	 *  @param q Column we're looking at.
	 */
	public int lastRow(int q) {return maps.get(0).lastRow(q);}
	
	@Override
	public boolean inBounds(AxialHexCoord3D coord)
	{
		if (0 < coord.z || coord.z >= levels) return false;
		else return maps.get(0).inBounds(coord);
	}
	@Override
	public void setHex(AxialHexCoord3D coord, T t)
	{
		if (!inBounds(coord)) return;
		else maps.get(coord.z).setHex(coord, t);
	}
	@Override
	public T getHex(AxialHexCoord3D coord)
	{
		if (!inBounds(coord)) return null;
		else return maps.get(coord.z).getHex(coord);
	}

}
