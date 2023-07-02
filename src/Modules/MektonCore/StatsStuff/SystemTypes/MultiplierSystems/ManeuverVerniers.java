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
	
	private int amountAllocated()
	{
		int sum = 0;
		for (Integer value : spaceAllocation.values()) sum += value;
		return sum;
	}
	private void populateLocationSlate(MenuSlate slate, HitLocation location)
	{
		slate.setCells(15, 2);
		slate.addOptions(0, 0, "", 0, 4, 2, ServoType.values(), new DataFunction<ServoType>()
		{
			@Override public ServoType getValue() {return location.type;}
			@Override public void setValue(ServoType data) {location.type = data;}
		});
		slate.addOptions(4, 0, "", 0, 4, 2, ServoSide.values(), new DataFunction<ServoSide>()
		{
			@Override public ServoSide getValue() {return location.side;}
			@Override public void setValue(ServoSide data) {location.side = data;}
		});
		slate.addIntegerWheel(8, 0, "#", 1, 1, 16, 2, 2, new DataFunction<Integer>()
		{
			@Override public Integer getValue() {return location.index + 1;}
			@Override public void setValue(Integer data) {location.index = data - 1;}
		});
		slate.addIntegerWheel(11, 0, "Spaces", 2, 1, 50, 2, 2, new DataFunction<Integer>()
		{
			@Override public Integer getValue() {return spaceAllocation.get(location);}
			@Override public void setValue(Integer data)
			{				
				if (amountAllocated() + data - spaceAllocation.get(location) <= getSpaces()) spaceAllocation.put(location, data);
				// cannot exceed actual needed spaces
			}
		});
	}
	
	public ManeuverVerniers()
	{
		level = 0;
		spaceAllocation = new HashMap<HitLocation, Integer>();
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
		slate.setCells(20, 4  + 2 * spaceAllocation.size());
		slate.addInfo(0, 0, "Maneuver Verniers", 5, 0, 2, () -> {return null;});
		slate.addIntegerWheel(6, 0, "Level:", 2, 0, 10, 2, 2, new DataFunction<Integer>()
		{
			@Override public Integer getValue() {return level;}
			@Override public void setValue(Integer data) {level = data;}	
		});
		slate.addInfo(10, 0, "Spaces allocated: ", 5, 3, 2, () -> {return Integer.toString(amountAllocated()) + " / " + Integer.toString(getSpaces());});
		
		slate.addButton(0, 2, "Add location", 6, 2, () -> 
		{
			if (amountAllocated() < getSpaces()) // Can't add new location if all spaces already allocated
			{
				spaceAllocation.put(new HitLocation(ServoType.torso, ServoSide.middle, null, null, 0), 1);
				populate(slate, supplier);
			}
		});
		
		ComponentHandle handle = null;
		
		for (HitLocation location : spaceAllocation.keySet())
		{
			MenuSlate locationSlate = supplier.get();
			populateLocationSlate(locationSlate, location);
			if (handle != null) handle = slate.addSubSlate(handle, Alignable.AlignmentPoint.southWest, 0, 0, locationSlate);
			else handle = slate.addSubSlate(0, 4, locationSlate);
			slate.addButton(handle, Alignable.AlignmentPoint.northEast, 0, 0, "Remove", 5, 2, () ->
			{
				spaceAllocation.remove(location);
				populate(slate, supplier);
			});
		}
	}
}
