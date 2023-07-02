// By Iacon1
// Created 1/7/2023
//


package Modules.MektonCore.StatsStuff.SystemTypes.MultiplierSystems;

import java.util.function.Supplier;

import GameEngine.MenuStuff.MenuSlate;
import GameEngine.MenuStuff.MenuSlate.DataFunction;

public class ACE extends MultiplierSystem
{
	public enum Level
	{
		level0(0, 0),
		level33(0.05, 0.33),
		level67(0.1, 0.67),
		level100(0.2, 1.0);
		
		private double costMult, MPModMult;

		private Level(double costMult,  double MPModMult)
		{
			this.costMult = costMult;
			this.MPModMult = MPModMult;
		}

		public double getCostMult() {return costMult;}
		public double getMPModMult() {return MPModMult;}
	}
	
	private Level level = Level.level0;
	@Override
	public double getMultiplier()
	{
		return level.getCostMult();
	}
	
	public double getMPModMult() {return level.getMPModMult();} // Multiplier to MP

	@Override
	public void populate(MenuSlate slate, Supplier<MenuSlate> supplier)
	{
		slate.setCells(20, 2);
		slate.addInfo(0, 0, "ACE", 4, 0, 2, () -> {return null;});
		slate.addOptions(5, 0, "Level: ", 2, 3, 2, Level.values(), new DataFunction<Level>()
		{
			@Override public Level getValue() {return level;}
			@Override public void setValue(Level data) {level = data;}	
		});
	}
}
