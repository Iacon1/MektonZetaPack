// By Iacon1
// Created 12/03/2021
// A system with a cost multiplier.

package Modules.MektonCore.StatsStuff.SystemTypes.MultiplierSystems;

import GameEngine.MenuStuff.MenuSlatePopulator;

public abstract class MultiplierSystem implements MenuSlatePopulator
{
	public abstract double getMultiplier();
}
