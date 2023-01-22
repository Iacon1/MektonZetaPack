// By Iacon1
// Created 12/03/2021
// A weapon that deals damage and has scale.

package Modules.MektonCore.StatsStuff.SystemTypes.AdditiveSystems.Weapons;

import java.util.function.Supplier;

import GameEngine.MenuSlate;
import Modules.MektonCore.Enums.Scale;
import Modules.MektonCore.StatsStuff.DamageTypes.Damage;
import Modules.MektonCore.StatsStuff.ScaledUnits.ScaledCostValue;
import Modules.MektonCore.StatsStuff.ScaledUnits.ScaledDistanceValue;
import Modules.MektonCore.StatsStuff.SystemTypes.AdditiveSystems.AdditiveSystem;

public abstract class Weapon extends AdditiveSystem
{
	protected Scale scale;

	// The base cost of the weapon.
	protected abstract ScaledCostValue baseCost();
	// The weapon's cost multiplier.
	protected abstract double getMultiplier();
	@Override public ScaledCostValue getCost() {return baseCost().multiply(getMultiplier());}
	
	public abstract Damage getDamage();
	public abstract ScaledDistanceValue getRange();

	@Override public double getWeight() {return getMaxHealth().getValue(Scale.mekton) / 2;}
	
	@Override
	public void populate(MenuSlate slate, Supplier<MenuSlate> supplier) {
		// TODO Auto-generated method stub
		
	}
}
