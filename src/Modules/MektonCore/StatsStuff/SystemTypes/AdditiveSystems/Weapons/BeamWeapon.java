// By Iacon1
// Created 12/03/2021
// A ranged beam weapon as defined in MZ+.

package Modules.MektonCore.StatsStuff.SystemTypes.AdditiveSystems.Weapons;

import java.util.function.Supplier;

import javax.swing.JPanel;

import GameEngine.MenuStuff.MenuSlate;
import Modules.MektonCore.Enums.Scale;
import Modules.MektonCore.StatsStuff.DamageTypes.Damage;
import Modules.MektonCore.StatsStuff.DamageTypes.EnergyDamage;
import Modules.MektonCore.StatsStuff.ScaledUnits.ScaledCostValue;
import Modules.MektonCore.StatsStuff.ScaledUnits.ScaledDistanceValue;
import Modules.MektonCore.StatsStuff.ScaledUnits.ScaledHitValue;

public class BeamWeapon extends Weapon
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
	public enum Range
	{
		r025(0.62, 0.25),
		r050(0.75, 0.50),
		r075(0.88, 0.75),
		r100(1.00, 1.00),
		r125(1.12, 1.25),
		r150(1.25, 1.50),
		r175(1.38, 1.75),
		r200(1.50, 2.00),
		r250(1.75, 2.50),
		r300(2.00, 3.00);
		
		private double value;
		private double multiplier;
		
		private Range(double multiplier, double value)
		{
			this.multiplier = multiplier;
			this.value = value;
		}
		
		public double value() {return value;}
		public double multiplier() {return multiplier;}
	}
	public enum MaxShots
	{
		m00(0.33,  0),
		m01(0.50,  1),
		m02(0.60,  2),
		m03(0.70,  3),
		m05(0.80,  5),
		m10(0.90, 10),
		inf(1.00, -1);
		
		private int value;
		private double multiplier;
		
		private MaxShots(double multiplier, int value)
		{
			this.multiplier = multiplier;
			this.value = value;
		}
		
		public int value() {return value;}
		public double multiplier() {return multiplier;}
	}
	public enum Warmup
	{
		w00(1.00,  0),
		w01(0.90,  1),
		w02(0.70,  2),
		w03(0.60,  3);
		
		private int value;
		private double multiplier;
		
		private Warmup(double multiplier, int value)
		{
			this.multiplier = multiplier;
			this.value = value;
		}
		
		public int value() {return value;}
		public double multiplier() {return multiplier;}		
	}
	public enum WideAngle
	{
		w000(1.00,   0),
		wHex(2.00,  -1),
		w060(3.00,  60),
		w180(5.00, 180),
		w300(7.00, 300),
		w360(9.00, 360);
		
		private int value;
		private double multiplier;
		
		private WideAngle(double multiplier, int value)
		{
			this.multiplier = multiplier;
			this.value = value;
		}
		
		public int value() {return value;}
		public double multiplier() {return multiplier;}		
	}
	public enum BurstValue
	{
		b00(1.0,  0),
		b02(1.5,  2),
		b03(2.0,  3),
		b04(2.5,  4),
		b05(3.0,  5),
		b06(3.5,  6),
		b07(4.0,  7),
		b08(4.5,  8),
		inf(5.0, -1);
		
		private int value;
		private double multiplier;
		
		private BurstValue(double multiplier, int value)
		{
			this.multiplier = multiplier;
			this.value = value;
		}
		
		public int value() {return value;}
		public double multiplier() {return multiplier;}
	}
	public enum Speciality
	{
		none(1.0),
		antiMissile(1.0),
		variableAntiMissile(1.8),
		antiPersonnel(1.0),
		variableAntiPersonnel(1.8),
		antiMissileAntiPersonnel(1.8),
		allPurpose(2.6);
		
		private double multiplier;
		
		private Speciality(double multiplier)
		{
			this.multiplier = multiplier;
		}

		public double multiplier() {return multiplier;}
	}
	private static final double clipFedFactor = 0.9;
	private static final double megaBeamFactor = 10.0;
	private static final double longRangeFactor = 1.33;
	private static final double fragileFactor = 1.0;
	private static final double disruptorFactor = 2.0;
	private static final double hydroFactor = 2.0;
	private ScaledHitValue damage;
	
	private Accuracy accuracy; // TODO implement usage
	private Range range;
	private MaxShots maxShots; // TODO implement usage
	private boolean clipFed; // TODO implement usage
	private Warmup warmup; // TODO implement usage
	private WideAngle wideAngle; // TODO implement usage
	private BurstValue burstValue; // TODO implement usage
	private Speciality speciality; // TODO implement usage
	private boolean megaBeam; // TODO implement usage
	private boolean longRange; // TODO implement usage
	private boolean fragile;
	private boolean disruptor; // TODO implement usage
	private boolean hydro; // TODO implement usage
	
	@Override
	protected ScaledCostValue baseCost()
	{
		return new ScaledCostValue(scale, damage.getValue(scale)).multiply(1.5);
	}

	@Override
	protected double getMultiplier()
	{
		double multiplier = 1;
		
		multiplier *= accuracy.multiplier();
		multiplier *= range.multiplier();
		multiplier *= maxShots.multiplier();
		if (clipFed) multiplier *= clipFedFactor;
		multiplier *= warmup.multiplier();
		multiplier *= wideAngle.multiplier();
		multiplier *= burstValue.multiplier();
		multiplier *= speciality.multiplier();
		if (megaBeam) multiplier *= megaBeamFactor;
		if (longRange) multiplier *= longRangeFactor;
		if (fragile) multiplier *= fragileFactor;
		if (disruptor) multiplier *= disruptorFactor;
		if (hydro) multiplier *= hydroFactor;
		
		return multiplier;
	}

	@Override
	public Damage getDamage()
	{
		// TODO variations
		return new EnergyDamage(damage);
	}

	@Override
	public ScaledHitValue getVolume()
	{
		return new ScaledHitValue(scale, getCost().getValue(scale));
	}

	@Override
	public ScaledHitValue getMaxHealth()
	{
		if (fragile) return new ScaledHitValue(scale, 1);
		else return new ScaledHitValue(scale, damage.getValue(scale));
	}

	@Override
	public ScaledDistanceValue getRange()
	{
		switch ((int) damage.getValue(scale))
		{
		case 1: return new ScaledDistanceValue(scale, 4);
		case 2: return new ScaledDistanceValue(scale, 6);
		case 3: return new ScaledDistanceValue(scale, 7);
		case 4: return new ScaledDistanceValue(scale, 8);
		case 5: return new ScaledDistanceValue(scale, 9);
		case 6: return new ScaledDistanceValue(scale, 10);
		case 7: case 8: return new ScaledDistanceValue(scale, 11);
		case 9: return new ScaledDistanceValue(scale, 12);
		case 10: case 11: return new ScaledDistanceValue(scale, 13);
		case 12: case 13: return new ScaledDistanceValue(scale, 14);
		case 14: case 15: return new ScaledDistanceValue(scale, 15);
		case 16: case 17: return new ScaledDistanceValue(scale, 16);
		case 18: case 19: return new ScaledDistanceValue(scale, 17);
		case 20: return new ScaledDistanceValue(scale, 18);
		default: return new ScaledDistanceValue(scale, 1);
		}
	}

	@Override
	public void populate(MenuSlate slate, Supplier<MenuSlate> supplier) {
		// TODO Auto-generated method stub
		
	}
}
