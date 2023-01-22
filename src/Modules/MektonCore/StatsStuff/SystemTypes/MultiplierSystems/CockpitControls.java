// By Iacon1
// Created 1/7/2023
//


package Modules.MektonCore.StatsStuff.SystemTypes.MultiplierSystems;

import java.util.function.Supplier;

import GameEngine.MenuSlate;
import GameEngine.MenuSlate.DataFunction;
import Modules.MektonCore.StatsStuff.HitLocation;
import Modules.MektonCore.StatsStuff.HitLocation.ServoSide;
import Modules.MektonCore.StatsStuff.HitLocation.ServoType;

public class CockpitControls extends MultiplierSystem
{
	public enum Control
	{
		manual(0, -0.05, -2, 0),
		screen(0, 0, 0, 0),
		virtual(0, 0, 0, 0),
		reflex(0, .1, 0, .67),
		slave(0, .07, 0, 0),
		submecha(5, 0, 0, 0);
		
		private double costFlat, costMult, MPModMult;
		private int MPModFlat;
		
		private Control(double costFlat, double costMult, int MPModFlat, double MPModMult)
		{
			this.costFlat = costFlat;
			this.costMult = costMult;
			this.MPModFlat = MPModFlat;
			this.MPModMult = MPModMult;
		}

		public double getCostFlat() {return costFlat;}
		public double getCostMult() {return costMult;}
		public int getMPModFlat() {return MPModFlat;}
		public double getMPModMult() {return MPModMult;}
		
	}

	public enum Enclosure
	{
		armored,
		canopy,
		saddle
	}
	
	private Control control = Control.screen;
	private Enclosure enclosure = Enclosure.armored;
	private HitLocation location = new HitLocation(ServoType.torso, ServoSide.middle, null, null, 0);
	
	@Override
	public double getMultiplier()
	{
		return getCostMult();
	}
	
	public double getCostFlat() {return control.getCostFlat();}
	public double getCostMult() {return control.getCostMult();}
	public double getMPModFlat() {return control.getMPModFlat();} // Added to MP
	public double getMPModMult() {return control.getMPModMult();} // Multiplier to MP

	@Override
	public void populate(MenuSlate slate, Supplier<MenuSlate> supplier)
	{
		slate.setCells(20, 1);
		slate.addInfo(0, 0, "Cockpit", 4, 0, 1, () -> {return null;});
		slate.addOptions(4, 0, "", 0, 5, 1, Control.values(), new DataFunction<Control>()
		{
			@Override public Control getValue() {return control;}
			@Override public void setValue(Control data) {control = data;}
		});
		slate.addOptions(9, 0, "", 0, 6, 1, Enclosure.values(), new DataFunction<Enclosure>()
		{
			@Override public Enclosure getValue() {return enclosure;}
			@Override public void setValue(Enclosure data) {enclosure = data;}
		});
		// TODO location
	}
}
