// By Iacon1
// Created 09/16/2021
// Human states & skills
// TODO debuffs / buffs

package Modules.MektonCore.StatsStuff.SheetTypes;

import java.util.HashMap;
import java.util.Map;

import Modules.MektonCore.StatsStuff.AdditiveSystemList;

public class HumanSheet extends AdditiveSystemList
{
	public enum Stat
	{
		intelligence,
		education,
		cool,
		reflexes,
		attractiveness,
		empathy,
		techAbility,
		luck,
		movementAbility,
		body,
		psionics
	}
	
	private Map<Stat, Integer> stats;
	
	public HumanSheet()
	{
		super();
		this.stats = new HashMap<Stat, Integer>();
		initStats(0);
	}
	public void initStats(int baseline)
	{
		stats.put(Stat.intelligence, baseline);
		stats.put(Stat.education, baseline);
		stats.put(Stat.cool, baseline);
		stats.put(Stat.reflexes, baseline);
		stats.put(Stat.attractiveness, baseline);
		stats.put(Stat.empathy, baseline);
		stats.put(Stat.techAbility, baseline);
		stats.put(Stat.luck, baseline);
		stats.put(Stat.movementAbility, baseline);
		stats.put(Stat.body, baseline);
		stats.put(Stat.psionics, baseline);
	}
	
	/** Returns the value of a stat, either at base or with modifiers.
	 * 
	 * @param stat Stat to change.
	 * @param base Whether to check the base or overall (i. e. buffed or debuffed) value. 
	 */
	public int getStat(Stat stat, boolean base)
	{
		if (base) return stats.get(stat);
		else return stats.get(stat); // TODO
	}
	
	/** Sets a stat. If the stat isn't registered this does nothing.
	 * 
	 * @param stat Stat to change.
	 * @param value Value to set to.
	 */
	public void setStat(Stat stat, int value)
	{
		if (stats.get(stat) != null) stats.put(stat, value);
	}
	
	public int getStatTotal()
	{
		int sum = 0;
		for (int i = 0; i < Stat.values().length; ++i)
		{
			sum += getStat(Stat.values()[i], true);
		}
		return sum;
	}
}
