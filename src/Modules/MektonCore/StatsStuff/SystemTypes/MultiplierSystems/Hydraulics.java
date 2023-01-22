// By Iacon1
// Created 1/7/2023
//


package Modules.MektonCore.StatsStuff.SystemTypes.MultiplierSystems;

import java.util.function.Supplier;

import GameEngine.MenuSlate;
import GameEngine.MenuSlate.DataFunction;

public class Hydraulics extends MultiplierSystem
{
	public enum HydraulicsType
	{
		space(-.1, 1, -1, 0),
		marine(-.05, 1, 0, 0),
		standard(0, 1, 0, 0),
		heavy(.1, 1.5, 1, 1),
		superheavy(.2, 2, 2, 2);
		
		private double costMult;
		private double liftCapMult;
		private int spacePenalty;
		private int meleeBonus;
		
		private HydraulicsType(double costMult, double liftCapMult, int spacePenalty, int meleeBonus)
		{
			this.costMult = costMult;
			this.liftCapMult = liftCapMult;
			this.spacePenalty = spacePenalty;
			this.meleeBonus = meleeBonus;
		}
		
		public double getCostMult() {return costMult;}
		public double getLiftCapMult() {return liftCapMult;}
		public int getSpacePenalty() {return spacePenalty;}
		public int getMeleeBonus() {return meleeBonus;}
	}
	
	private HydraulicsType hydraulicsType = HydraulicsType.standard;
	@Override
	public double getMultiplier()
	{
		return hydraulicsType.getCostMult();
	}
	
	public double getLiftCapMult() {return hydraulicsType.getLiftCapMult();}
	public int getSpacePenalty() {return hydraulicsType.getSpacePenalty();}
	public int getMeleeBonus() {return hydraulicsType.getMeleeBonus();}

	@Override
	public void populate(MenuSlate slate, Supplier<MenuSlate> supplier)
	{
		slate.setCells(20, 1);
		slate.addInfo(0, 0, "Hydraulics", 4, 0, 1, () -> {return null;});
		slate.addOptions(4, 0, "", 0, 5, 1, HydraulicsType.values(), new DataFunction<HydraulicsType>()
		{
			@Override public HydraulicsType getValue() {return hydraulicsType;}
			@Override public void setValue(HydraulicsType data) {hydraulicsType = data;}
		});
	}
}
