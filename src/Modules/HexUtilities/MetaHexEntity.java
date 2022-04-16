// By Iacon1
// Created 09/11/2021
// An entity that occupies multiple hexes, where hexPos is the center

package Modules.HexUtilities;

import Modules.HexUtilities.HexStructures.HexCoord;

public abstract class MetaHexEntity<T extends HexCoord> extends HexEntity<T>
{
	private int radius;
	
	public void setRadius(int radius)
	{
		this.radius = radius;
	}
	
	@Override
	public boolean isPresentAt(T pos)
	{
		return hexPos.withinDistance(radius).contains(pos);
	}
}
