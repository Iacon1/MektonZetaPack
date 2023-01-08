// By Iacon1
// Created 1/1/2023
// List of things with HitLocation variables

package Modules.MektonCore.StatsStuff;

import java.util.ArrayList;
import java.util.List;

import Modules.MektonCore.StatsStuff.HitLocation.ServoSide;
import Modules.MektonCore.StatsStuff.HitLocation.ServoType;

@SuppressWarnings("serial")
public class LocationList<T extends LocatedItem> extends ArrayList<T>
{
	public List<Integer> getTypeIndices(ServoType type)
	{
		List<Integer> list = new ArrayList<>();
		
		for (int i = 0; i < size(); ++i)
		{
			if (get(i).getLocation().type == type) list.add(i);
		}
		
		return list;
	}
	public List<T> getType(ServoType type)
	{
		List<T> list = new ArrayList<>();
		
		for (int i = 0; i < size(); ++i)
		{
			if (get(i).getLocation().type == type) list.add(get(i));
		}
		
		return list;
	}
	public int getTypeCount(ServoType type) {return getType(type).size();}
	
	public List<Integer> getSideIndices(ServoSide side)
	{
		List<Integer> list = new ArrayList<>();
		
		for (int i = 0; i < size(); ++i)
		{
			if (get(i).getLocation().side == side) list.add(i);
		}
		
		return list;
	}
	public List<T> getSide(ServoSide side)
	{
		List<T> list = new ArrayList<>();
		
		for (int i = 0; i < size(); ++i)
		{
			if (get(i).getLocation().side == side) list.add(get(i));
		}
		
		return list;
	}
	public int getSideCount(ServoSide side) {return getSide(side).size();}
	
	public List<Integer> getTypeAndSideIndices(ServoType type, ServoSide side)
	{
		List<Integer> list = new ArrayList<>();
		
		for (int i = 0; i < size(); ++i)
		{
			if (get(i).getLocation().type == type && get(i).getLocation().side == side) list.add(i);
		}
		
		return list;
	}
	public List<T> getTypeAndSide(ServoType type, ServoSide side)
	{
		List<T> list = new ArrayList<>();
		
		for (int i = 0; i < size(); ++i)
		{
			if (get(i).getLocation().type == type && get(i).getLocation().side == side) list.add(get(i));
		}
		
		return list;
	}
	public int getTypeAndSideCount(ServoType type, ServoSide side) {return getTypeAndSide(type, side).size();}
	
	public T getLocation(HitLocation location)
	{
		for (int i = 0; i < size(); ++i)
		{
			if (get(i).getLocation().equals(location)) return get(i);
		}
		return null;
	}
}
