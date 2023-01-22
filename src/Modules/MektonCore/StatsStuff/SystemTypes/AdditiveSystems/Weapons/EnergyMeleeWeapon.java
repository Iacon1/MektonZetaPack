// By Iacon1
// Created 12/03/2021
// A ranged beam weapon as defined in MZ+.

package Modules.MektonCore.StatsStuff.SystemTypes.AdditiveSystems.Weapons;

import java.util.function.Supplier;

import javax.swing.JPanel;

import GameEngine.MenuSlate;
import Modules.MektonCore.StatsStuff.DamageTypes.Damage;
import Modules.MektonCore.StatsStuff.DamageTypes.EnergyMeleeDamage;
import Modules.MektonCore.StatsStuff.ScaledUnits.ScaledCostValue;
import Modules.MektonCore.StatsStuff.ScaledUnits.ScaledDistanceValue;
import Modules.MektonCore.StatsStuff.ScaledUnits.ScaledHitValue;

public class EnergyMeleeWeapon extends Weapon
{
	// Enums
	public enum Accuracy
	{
		aN2(0.6, -2),
		aN1(0.8, -1),
		a00(0.9,  0),
		aP1(1.0,  1),
		aP2(1.5,  2),
		aP3(2.0,  3);
		
		private int value;
		private double multiplier;
		
		private Accuracy(double multiplier, int value)
		{
			this.multiplier = multiplier;
			this.value = value;
		}
		
		public int value() {return value;}
		public double multiplier() {return multiplier;}
	}
	public enum AttackFactor
	{
		a0(1.0, 0),
		a1(1.5, 1),
		a2(2.0, 2),
		a3(2.5, 3),
		a4(3.0, 4),
		a5(3.5, 5);
		
		private int value;
		private double multiplier;
		
		private AttackFactor(double multiplier, int value)
		{
			this.multiplier = multiplier;
			this.value = value;
		}
		
		public int value() {return value;}
		public double multiplier() {return multiplier;}
	}
	public enum TurnsInUse
	{
		t01(0.3,  1),
		t02(0.4,  2),
		t03(0.5,  3),
		t04(0.6,  4),
		t05(0.7,  5),
		t07(0.8,  7),
		t10(0.9, 10),
		inf(1.0, -1);
		private int value;
		private double multiplier;
		
		private TurnsInUse(double multiplier, int value)
		{
			this.multiplier = multiplier;
			this.value = value;
		}
		
		public int value() {return value;}
		public double multiplier() {return multiplier;}
	}
	public enum BeamShield
	{
		none(1.0),
		shield(1.5),
		variable(2.0);
		
		private double multiplier;
		
		private BeamShield(double multiplier)
		{
			this.multiplier = multiplier;
		}

		public double multiplier() {return multiplier;}
	}
	private static final double rechargableFactor = 1.1;
	private static final double thrownFactor = 1.2;
	private static final double quickFactor = 2.0;
	private static final double hyperFactor = 7.5;

	private ScaledHitValue damage;
	
	private Accuracy accuracy; // TODO implement usage
	private AttackFactor attackFactor; // TODO implement usage
	private TurnsInUse turnsInUse; // TODO implement usage
	private boolean rechargable; // TODO implement usage
	private boolean thrown; // TODO implement usage
	private boolean quick; // TODO implement usage
	private boolean hyper; // TODO implement usage
	private BeamShield beamShield; // TODO implement usage
	
	@Override
	protected ScaledCostValue baseCost()
	{
		return new ScaledCostValue(scale, damage.getValue(scale));
	}

	@Override
	protected double getMultiplier()
	{
		double multiplier = 1;
		
		multiplier *= accuracy.multiplier();
		multiplier *= attackFactor.multiplier();
		multiplier *= turnsInUse.multiplier();
		if (rechargable) multiplier *= rechargableFactor;
		if (thrown) multiplier *= thrownFactor;
		if (quick) multiplier *= quickFactor;
		if (hyper) multiplier *= hyperFactor;
		multiplier *= beamShield.multiplier();
		
		return multiplier;
	}

	@Override
	public Damage getDamage() {return new EnergyMeleeDamage(damage);}

	@Override public ScaledHitValue getVolume() {return new ScaledHitValue(scale, getCost().getValue(scale));}

	@Override public ScaledHitValue getMaxHealth() {return new ScaledHitValue(scale, damage.getValue(scale)).divide(4);}

	@Override public ScaledDistanceValue getRange() {return new ScaledDistanceValue(scale, 1);}
	

	@Override
	public void populate(MenuSlate slate, Supplier<MenuSlate> supplier) {
		// TODO Auto-generated method stub
		
	}
}
