// By Iacon1
// Created 1/7/2023
//


package Modules.MektonCore.StatsStuff.SystemTypes.MultiplierSystems;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import GameEngine.EntityTypes.Alignable;
import GameEngine.MenuStuff.MenuSlate;
import GameEngine.MenuStuff.MenuSlate.ComponentHandle;
import GameEngine.MenuStuff.MenuSlate.DataFunction;
import Modules.MektonCore.StatsStuff.HitLocation;
import Modules.MektonCore.StatsStuff.HitLocation.ServoSide;
import Modules.MektonCore.StatsStuff.HitLocation.ServoType;

public class ManeuverVerniers extends MultiplierSystem
{
	private int level; // No more than mek's MV penalty
	private Map<HitLocation, Integer> spaceAllocation;
	
	public ManeuverVerniers()
	{
		level = 0;
		spaceAllocation = new HashMap<HitLocation, Integer>();
	}
	private void populateLocationSlate(MenuSlate slate, HitLocation location)
	{
		slate.setCells(20, 2);
		slate.addOptions(0, 0, "", 0, 5, 2, ServoType.values(), new DataFunction<ServoType>()
		{
			@Override public ServoType getValue() {return location.type;}
			@Override public void setValue(ServoType data) {location.type = data;}
		});
		slate.addOptions(5, 0, "", 0, 5, 2, ServoSide.values(), new DataFunction<ServoSide>()
		{
			@Override public ServoSide getValue() {return location.side;}
			@Override public void setValue(ServoSide data) {location.side = data;}
		});
		slate.addIntegerWheel(10, 0, "# ", 1, 1, 16, 2, 2, new DataFunction<Integer>()
		{
			@Override public Integer getValue() {return location.index + 1;}
			@Override public void setValue(Integer data) {location.index = data - 1;}
		});
	}
	@Override
	public double getMultiplier()
	{
		return getCostMult();
	}
	
	public int getLevel() {return level;}
	public double getCostMult() {return 0.1 * level;}
	public int getSpaces() {return 5 * level;}

	@Override
	public void populate(MenuSlate slate, Supplier<MenuSlate> supplier)
	{
		slate.clear();
		slate.setCells(20, 4 * (1 + spaceAllocation.size()));
		slate.addInfo(0, 0, "Maneuver Verniers", 4, 0, 2, () -> {return null;});
		slate.addIntegerWheel(5, 0, "Level: ", 2, 0, 10, 2, 2, new DataFunction<Integer>()
		{
			@Override public Integer getValue() {return level;}
			@Override public void setValue(Integer data) {level = data;}	
		});
		slate.addInfo(9, 0, "Spaces: ", 2, 2, 2, () -> {return Integer.toString(getSpaces());});
		
		slate.addButton(0, 2, "Add location", 5, 2, () -> 
		{
			spaceAllocation.put(new HitLocation(ServoType.torso, ServoSide.middle, null, null, 0), 0);
			populate(slate, supplier);
		});
		
		ComponentHandle handle = null;
		
		for (HitLocation location : spaceAllocation.keySet())
		{
			MenuSlate locationSlate = supplier.get();
			populateLocationSlate(locationSlate, location);
			if (handle != null) handle = slate.addSubSlate(handle, Alignable.AlignmentPoint.southWest, 0, 0, 28, 3, locationSlate);
			else handle = slate.addSubSlate(0, 4, 28, 3, locationSlate);
			slate.addButton(handle, Alignable.AlignmentPoint.northEast, 0, 0, "Remove", 5, 3, () ->
			{
				spaceAllocation.remove(location);
				populate(slate, supplier);
			});
		}
	}
}
