// By Iacon1
// Created 11/07/2021
//

package Modules.MektonCore.Enums;

public enum TerrainType
{
	open(1, 0), // X1 MA cost
	rough(2, 0), // X2 MA cost
	restrictive(3, 0), // X3 MA cost
	deepWater(1, 0), // Deep enough to swim in TODO ???
	;
	
	// TODO public isn't optimal
	public int moveCost; // Multiplier for movement cost.
	
	private TerrainType(int moveCost, int damage)
	{
		this.moveCost = moveCost;
	}
}