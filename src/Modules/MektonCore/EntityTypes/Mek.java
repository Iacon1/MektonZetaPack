// By Iacon1
// Created 11/23/2021
// A mek.

package Modules.MektonCore.EntityTypes;

import Modules.MektonCore.StatsStuff.HitLocation;
import Modules.MektonCore.StatsStuff.DamageTypes.Damage;
import Modules.MektonCore.StatsStuff.SystemTypes.AdditiveSystems.Servos.MekServo;

public class Mek extends MektonActor
{
	
	public MekServo getServo(HitLocation location)
	{
		return null; // TODO
	}
	
	@Override
	protected int getMA()
	{
		// TODO Auto-generated method stub
		return 0;
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
