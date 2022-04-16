// By Iacon1
// Created 11/23/2021
// Damage from standard melee weapons, guns, and fists

package Modules.MektonCore.StatsStuff.DamageTypes;

import Modules.MektonCore.EntityTypes.Mek;
import Modules.MektonCore.StatsStuff.ScaledUnits.ScaledHitValue;
import Modules.MektonCore.StatsStuff.SystemTypes.AdditiveSystems.Servos.MekServo;

public class EnergyDamage extends SolidDamage
{
	public EnergyDamage(ScaledHitValue damage)
	{
		super(damage);
	}
	
	@Override
	public void apply(Mek recipient, MekServo servo)
	{
		ScaledHitValue delta = damage.multiply(1 - servo.getRAMReduction()).subtract(servo.getArmor()); // How much damage is left after armor
		ScaledHitValue deltaEnergy = damage.multiply(servo.getRAMReduction());
		
		if (servo.getDC().getValue() == 0) {applyDirect(servo, delta, damage);} // Ablative
		else if (servo.getDC().lessThan(damage)) {applyDirect(servo, delta, new ScaledHitValue(servo.getScale(), 1));} // Not ablative, armor chipped
		else applyDirect(servo, delta, new ScaledHitValue(servo.getScale(), 0)); // Not ablative, no armor damage
	}
}
