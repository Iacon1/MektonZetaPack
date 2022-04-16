// By Iacon1
// Created 11/23/2021
// Damage from standard melee weapons, guns, and fists

package Modules.MektonCore.StatsStuff.DamageTypes;

import Modules.MektonCore.EntityTypes.Human;
import Modules.MektonCore.EntityTypes.Mek;
import Modules.MektonCore.ExceptionTypes.ExcessValueException;
import Modules.MektonCore.StatsStuff.HitLocation;
import Modules.MektonCore.StatsStuff.ScaledUnits.ScaledHitValue;
import Modules.MektonCore.StatsStuff.SystemTypes.AdditiveSystems.Servos.MekServo;
import Utils.Logging;

public class SolidDamage implements Damage
{
	protected ScaledHitValue damage;

	public SolidDamage(ScaledHitValue damage)
	{
		this.damage = damage;
	}
	/** Simply applies damage to the servo.
	 * 
	 *  @param healthDamage Damage to health at native scale.
	 *  @param armorDamage Damage to armor at native scale.
	 */
	protected void applyDirect(MekServo servo, ScaledHitValue healthDamage, ScaledHitValue armorDamage)
	{
		ScaledHitValue newHealth = servo.getHealth().subtract(healthDamage);
		ScaledHitValue newArmor = servo.getArmor().subtract(armorDamage);
		
		if (newHealth.getValue() < 0) newHealth = new ScaledHitValue(servo.getScale(), 0);
		if (newArmor.getValue() < 0) newArmor = new ScaledHitValue(servo.getScale(), 0);
		
		try {servo.setHealth(newHealth);}
		catch (ExcessValueException e) {Logging.logException(e);}
		
		try {servo.setArmor(newArmor);}
		catch (ExcessValueException e) {Logging.logException(e);}
	}

	protected void apply(Mek recipient, MekServo servo)
	{
		ScaledHitValue delta = damage.subtract(servo.getArmor()); // How much damage is left after armor
		
		if (servo.getDC().getValue() == 0) {applyDirect(servo, delta, damage);} // Ablative
		else if (servo.getDC().lessThan(damage)) {applyDirect(servo, delta, new ScaledHitValue(servo.getScale(), 1));} // Not ablative, armor chipped
		else applyDirect(servo, delta, new ScaledHitValue(servo.getScale(), 0)); // Not ablative, no armor damage
	}
	
	@Override
	public void apply(Mek recipient, HitLocation location)
	{
		if (location.type != null) apply(recipient, recipient.getServo(location));
		else if (location.special != null)
		{
			
		}
		
		
	}

	@Override
	public void apply(Human recipient, HitLocation location)
	{
		// TODO Auto-generated method stub

	}

}
