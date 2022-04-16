// By Iacon1
// Created 11/07/2021
//

package Modules.MektonCore.Enums;

public enum TerrainType
{
	open(1, 0), // X1 MA cost
	rough(2, 0), // X2 MA cost
	restrictive(3, 0), // X3 MA cost
	solid(-1, 0), // Cannot move through
	deepWater(-1, 0), // Deep enough to swim in
	magma(-1, 1) // Causes damage
	;
	
	// TODO public isn't optimal
	public int moveCost; //-1 for impassible
	public int damage; // How many hits per round (2 seconds) this causes, applied to all locations
	private TerrainType(int moveCost, int damage)
	{
		this.moveCost = moveCost;
		this.damage = damage;
	}
}