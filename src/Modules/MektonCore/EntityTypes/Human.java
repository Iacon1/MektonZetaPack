// By Iacon1
// Created 09/26/2021
// A human

package Modules.MektonCore.EntityTypes;

import Modules.MektonCore.StatsStuff.SheetTypes.HumanSheet;
import Modules.MektonCore.StatsStuff.SheetTypes.HumanSheet.Stat;

public abstract class Human extends MektonActor
{
	private HumanSheet stats;
	
	private String name;
	
	public Human()
	{
		super();
	}
	public Human(MektonMap map)
	{
		super(map);
		this.stats = new HumanSheet();
		this.stats.initStats(5);
	}

	public String statSummary()
	{
		String summary =
				"Intelligence:     " + stats.getStat(HumanSheet.Stat.intelligence, true) + "\n" +
				"Education:        " + stats.getStat(HumanSheet.Stat.education, true) + "\n" +
				"Cool:             " + stats.getStat(HumanSheet.Stat.cool, true) + "\n" +
				"Reflexes:         " + stats.getStat(HumanSheet.Stat.reflexes, true) + "\n" +
				"Attractiveness:   " + stats.getStat(HumanSheet.Stat.attractiveness, true) + "\n" +
				"Empathy:          " + stats.getStat(HumanSheet.Stat.empathy, true) + "\n" +
				"Tech Ability:     " + stats.getStat(HumanSheet.Stat.techAbility, true) + "\n" +
				"luck:             " + stats.getStat(HumanSheet.Stat.luck, true) + "\n" +
				"Movement Ability: " + stats.getStat(HumanSheet.Stat.movementAbility, true) + "\n" +
				"Body:             " + stats.getStat(HumanSheet.Stat.body, true) + "\n" +
				"Psionics:         " + stats.getStat(HumanSheet.Stat.psionics, true);
		
		return summary;
	}
	@Override
	protected int getMA()
	{
		return stats.getStat(Stat.movementAbility, false);
	}
	
	@Override
	public String getName()
	{
		return name;
	}
}
