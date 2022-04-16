// By Iacon1
// Created 09/26/2021
// Mekton has many kinds of damage, so this is very broad

package Modules.MektonCore.StatsStuff.DamageTypes;

import Modules.MektonCore.EntityTypes.Human;
import Modules.MektonCore.EntityTypes.Mek;
import Modules.MektonCore.StatsStuff.HitLocation;

public interface Damage
{
	/** Applies the damage to a mek.
	 * 
	 *  @param recipient The servo to damage.
	 */
	public void apply(Mek recipient, HitLocation location);

	/** Applies the damage to a human.
	 * 
	 *  @param recipient The human recipient.
	 */
	public void apply(Human recipient, HitLocation location);
}
