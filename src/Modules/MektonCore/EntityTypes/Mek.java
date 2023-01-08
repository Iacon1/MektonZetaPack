// By Iacon1
// Created 11/23/2021
// A mek.

package Modules.MektonCore.EntityTypes;

import Modules.MektonCore.StatsStuff.HitLocation;
import Modules.MektonCore.StatsStuff.DamageTypes.Damage;
import Modules.MektonCore.StatsStuff.SheetTypes.MekSheet;
import Modules.MektonCore.StatsStuff.SystemTypes.AdditiveSystems.Servos.MekServo;

public abstract class Mek extends MektonActor
{
	protected MekSheet sheet;
	
	@Override
	protected int getMA()
	{
		// TODO Auto-generated method stub
		return 0;
	}

	public MekSheet getSheet()
	{
		return sheet;
	}
	
	@Override
	public void takeDamage(Damage damage)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void defend(MektonActor aggressor, HitLocation location)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void attack(MektonActor defender, HitLocation location)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void onPause()
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void onStart()
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void onAnimStop()
	{
		// TODO Auto-generated method stub

	}

	@Override
	public String getName()
	{
		// TODO Auto-generated method stub
		return null;
	}
}
