// By Iacon1
// Created 09/16/2021
// An organic servo, i. e. a limb

package Modules.MektonCore.StatsStuff.SystemTypes.AdditiveSystems.Servos;

import java.util.function.Supplier;

import GameEngine.MenuSlate;
import Modules.MektonCore.Enums.Scale;
import Modules.MektonCore.StatsStuff.HitLocation.ServoType;
import Modules.MektonCore.StatsStuff.ScaledUnits.ScaledCostValue;
import Modules.MektonCore.StatsStuff.ScaledUnits.ScaledHitValue;

public class HumanLimb extends Servo
{
	private ServoType limbType;
	private int body;
	
	public HumanLimb(Scale scale, ServoType limbType, int body)
	{
		super(scale);
		this.limbType = limbType;
		this.body = body;
	}
	
	@Override
	public ScaledHitValue getMaxHealth()
	{
		double baseValue = 0;
		switch (body)
		{
		case 2: baseValue = 4; break;
		case 3: case 4: baseValue = 5; break;
		case 5: case 6: case 7: baseValue = 6; break;
		case 8: case 9: baseValue = 7; break;
		case 10: baseValue = 8; break;
		default: baseValue = 0;
		}
		
		switch (limbType)
		{
		case head: return new ScaledHitValue(Scale.human, baseValue);
		case torso: return new ScaledHitValue(Scale.human, 2 * baseValue);
		case arm: case leg: return new ScaledHitValue(Scale.human, Math.floor(baseValue / 2));
		default: return new ScaledHitValue(Scale.human, 0);
		}
	}

	@Override
	public double getWeight() {return 0;}

	@Override
	public ScaledCostValue getCost() {return new ScaledCostValue(scale, 0);}


	@Override
	public void populate(MenuSlate slate, Supplier<MenuSlate> supplier)
	{
		// TODO Auto-generated method stub
		
	}
}
