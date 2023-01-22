// By Iacon1
// Created 1/7/2023
//


package Modules.MektonCore.StatsStuff.SystemTypes.MultiplierSystems;

import java.util.function.Supplier;

import GameEngine.MenuSlate;
import GameEngine.MenuSlate.DataFunction;

public class Powerplant extends MultiplierSystem
{
	public enum Charge
	{
		undercharged(-.15, -.25, -1, -1, -1, 0),
		standard(0, -.1, 0, 0, 0, 0),
		overcharged(0.15, 0.15, 1, 1, 0, .33),
		supercharged(0.3, 0.3, 2, 2, 0, .67);
		
		private double coldCost, hotCost, MPModMult;
		private int MVMod, MAMod, MPModFlat;
		
		private Charge(double coldCost, double hotCost, int MVMod, int MAMod, int MPModFlat, double MPModMult)
		{
			this.coldCost = coldCost;
			this.hotCost = hotCost;
		}
		public double getCost(boolean isHot)
		{
			if (isHot) return hotCost;
			else return coldCost;
		}
		
		public int getMVMod() {return MVMod;}
		public int getMAMod() {return MAMod;}
		public int getMPModFlat() {return MPModFlat;}
		public double getMPModMult() {return MPModMult;}
		
	}

	public enum Source
	{
		combustion(-0.33),
		powercell(-0.15),
		fusion(1.0),
		bioenergy(1.5);
		
		private double cost;
		
		private Source(double cost)
		{
			this.cost = cost;
		}
		public double getCost()
		{
			return cost;
		}
	}
	
	private Charge charge = Charge.standard;
	private Source source = Source.fusion;
	private boolean isHot = false;
	
	@Override
	public double getMultiplier()
	{
		double chargeCost = charge.getCost(isHot), sourceCost = source.getCost();
		if (chargeCost < 1) chargeCost += 1;
		if (sourceCost < 1) sourceCost += 1;
	
		return chargeCost * sourceCost - 1; // Formula as per semiofficial rules
	}
	
	public int getMVMod() {return charge.getMVMod();}
	public int getMAMod() {return charge.getMAMod();}
	public int getMPModFlat() {return charge.getMPModFlat();} // Added to MP
	public double getMPModMult() {return charge.getMPModMult();} // Multiplier to MP

	@Override
	public void populate(MenuSlate slate, Supplier<MenuSlate> supplier)
	{
		slate.setCells(20, 1);
		slate.addInfo(0, 0, "Powerplant", 4, 0, 1, () -> {return null;});
		slate.addOptions(4, 0, "", 0, 5, 1, Source.values(), new DataFunction<Source>()
		{
			@Override public Source getValue() {return source;}
			@Override public void setValue(Source data) {source = data;}
		});
		slate.addOptions(9, 0, "", 0, 6, 1, Charge.values(), new DataFunction<Charge>()
		{
			@Override public Charge getValue() {return charge;}
			@Override public void setValue(Charge data) {charge = data;}
		});
		slate.addCheckbox(16, 0, "Hot", 2, 1, 1, new DataFunction<Boolean>()
		{
			@Override public Boolean getValue() {return isHot;}
			@Override public void setValue(Boolean data) {isHot = data;}
		});
	}
}
