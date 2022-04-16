// By Iacon1
// Created 06/05/2021
// Mekton tile

package Modules.MektonCore;

import GameEngine.Point2D;
import Modules.MektonCore.Enums.TerrainType;

public class MektonHex
{
	// Texture properties (tileset & size defined elsewhere, not controllable per tile)
	
	public Point2D texturePos = new Point2D(0, 0);
	
	public TerrainType type = TerrainType.open;
	
	public static int getCost(TerrainType type) // Gets cost of moving through for pathfinding; TODO swimming mecha and magma-meks
	{
		return type.moveCost;
	}
	
	public int getCost()
	{
		return getCost(type);
	}
}
