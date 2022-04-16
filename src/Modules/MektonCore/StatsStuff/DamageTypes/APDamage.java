// By Iacon1
// Created 11/23/2021
// Damage from AP solid weapons

package Modules.MektonCore.StatsStuff.DamageTypes;

import Modules.MektonCore.EntityTypes.Mek;
import Modules.MektonCore.StatsStuff.HitLocation;
import Modules.MektonCore.StatsStuff.ScaledUnits.ScaledHitValue;
import Modules.MektonCore.StatsStuff.SystemTypes.AdditiveSystems.Servos.MekServo;

public class APDamage extends SolidDamage
{
	public APDamage(ScaledHitValue damage) {super(damage);}

	@Override
	public void apply(Mek recipient, MekServo servo)
	{
		ScaledHitValue delta = damage.subtract(servo.getArmor().divide(2)); // How much damage is left after armor
		
		if (servo.getDC().getValue() == 0) {applyDirect(servo, delta, damage);} // Ablative
		else if (servo.getDC().lessThan(damage)) {applyDirect(servo, delta, new ScaledHitValue(servo.getScale(), 1));} // Not ablative, armor chipped
		else applyDirect(servo, delta, new ScaledHitValue(servo.getScale(), 0)); // Not ablative, no armor damage
	}
}
