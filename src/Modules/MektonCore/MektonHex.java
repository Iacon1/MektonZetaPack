// By Iacon1
// Created 06/05/2021
// Mekton tile

// Each wall is either solid or not solid.
// The hex's type can be rough, restrictive, etc.
// An entity in the hex can only move to neighboring hexes where solid walls aren't blocking them.
// The movement cost for this is based off the terrain type.

package Modules.MektonCore;

import java.util.HashMap;
import java.util.Map;

import GameEngine.IntPoint2D;
import Modules.HexUtilities.HexDirection;
import Modules.MektonCore.Enums.TerrainType;

public class MektonHex
{
	// Texture properties (tileset & size defined elsewhere, not controllable per tile)
	public IntPoint2D texturePos;
	public Map<HexDirection, Boolean> walls;
	public TerrainType type;
	
	public MektonHex()
	{
		texturePos = new IntPoint2D(0, 0); // TODO animated hexes
		walls = new HashMap<HexDirection, Boolean>();
		for (int i = 0; i < HexDirection.values().length; ++i) walls.put(HexDirection.values()[i], false);
		type = TerrainType.open;
	}
	
	public static int getCost(TerrainType type) // Gets cost of moving through for pathfinding; TODO swimming mecha and magma-meks
	{
		return type.moveCost;
	}
	
	public int getCost()
	{
		return getCost(type);
	}
}
