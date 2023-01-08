// By Iacon1
// Created 09/16/2021
// All sorts of rolls

package Modules.MektonCore.MektonUtil;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import Modules.MektonCore.StatsStuff.LocationList;
import Modules.MektonCore.StatsStuff.SystemTypes.AdditiveSystems.Servos.Servo;
import Modules.MektonCore.StatsStuff.HitLocation;
import Modules.MektonCore.StatsStuff.HitLocation.Cinematic;
import Modules.MektonCore.StatsStuff.HitLocation.ServoSide;
import Modules.MektonCore.StatsStuff.HitLocation.ServoType;
import Modules.MektonCore.StatsStuff.HitLocation.Special;

public class Rolls
{
	private static int maxRolls = 256; // So explosions don't go on forever
	
	public static int rollXDY(int X, int Y)
	{
		Random random = new Random();
		int roll = 0;
		for (int i = 0; i < X; ++i) roll += random.nextInt(Y) + 1;
		
		return roll;
	}
	public static <T> T select(T... items)
	{
		int index = rollXDY(1, items.length) - 1;
		
		return items[index];
	}
	
	private static int rollD10Counting(int count)
	{
		Random random = new Random();
		int roll = random.nextInt(10) + 1;
		if (roll == 10 && count < maxRolls) return roll + rollD10Counting(count + 1);
		else return roll;
	}
	/** Rolls a 1D10.
	 * 
	 *  @param explode Does the die explode?
	 */
	public static int rollD10(boolean explode)
	{
		Random random = new Random();
		int roll = random.nextInt(10) + 1;
		if (roll == 10 && explode) return roll + rollD10Counting(1);
		else return roll;
	}

	public static ServoSide rollSide(LocationList<Servo> servos, ServoType type)
	{
		if (servos.getTypeCount(type) == 0)
			return null;
		else if (servos.getTypeAndSideCount(type, ServoSide.left) == 0 && servos.getTypeAndSideCount(type, ServoSide.middle) == 0)
			return ServoSide.right;
		else if (servos.getTypeAndSideCount(type, ServoSide.left) == 0 && servos.getTypeAndSideCount(type, ServoSide.right) == 0)
			return ServoSide.middle;
		else if (servos.getTypeAndSideCount(type, ServoSide.middle) == 0 && servos.getTypeAndSideCount(type, ServoSide.right) == 0)
			return ServoSide.left;
		else if (servos.getTypeAndSideCount(type, ServoSide.left) == 0)
			return select(ServoSide.middle, ServoSide.right);
		else if (servos.getTypeAndSideCount(type, ServoSide.middle) == 0)
			return select(ServoSide.left, ServoSide.right);
		else if (servos.getTypeAndSideCount(type, ServoSide.right) == 0)
			return select(ServoSide.left, ServoSide.middle);
		else return select(ServoSide.left, ServoSide.middle, ServoSide.right);
	}
	
	public static HitLocation cinematicHitChart()
	{
		int roll = rollD10(false);
		
		switch (roll)
		{
		case 1: return new HitLocation(null, null, null, Cinematic.hydraulic, 0);
		case 2: return new HitLocation(null, null, null, Cinematic.blunt, 0);
		case 3: return new HitLocation(null, null, null, Cinematic.sensorOverload, 0);
		case 4: return new HitLocation(null, null, null, Cinematic.flightSystem, 0);
		case 5: return new HitLocation(null, null, null, Cinematic.thrusterMalfunction, 0);
		case 6: return new HitLocation(null, null, null, Cinematic.ammoExplosion, 0);
		case 7: return new HitLocation(null, null, null, Cinematic.weaponMalfunction, 0);
		case 8: return new HitLocation(null, null, null, Cinematic.controlJam, 0);
		case 9: return new HitLocation(null, null, null, Cinematic.systemShutdown, 0);
		case 10: return new HitLocation(null, null, null, Cinematic.powerplantOverload, 0);
		default: return null;
		}
	}
	public static HitLocation specialHitChart(boolean doCinematic)
	{
		int roll = 0;
		if (doCinematic) roll = rollD10(false);
		else roll = select(1, 2, 3, 4, 5, 6, 7, 10);
		
		switch (roll)
		{
		case 1:
		case 2: return new HitLocation(null, null, Special.weapon, null, 0);
		case 3: return new HitLocation(null, null, Special.sensors, null, 0);
		case 4: return new HitLocation(null, null, Special.flightSystem, null, 0);
		case 5: return new HitLocation(null, null, Special.shieldMount, null, 0);
		case 6: return new HitLocation(null, null, Special.other, null, 0);
		case 7: return new HitLocation(null, null, Special.cockpit, null, 0);
		case 8:
		case 9: return cinematicHitChart();
		case 10: return new HitLocation(null, null, Special.powerplant, null, 0);
		default: return null;
		}
	}
	public static HitLocation mechaHitChart(LocationList<Servo> servos, boolean doSpecial)
	{
		int roll = 0;
		
		Set<Integer> availableRolls = new HashSet<Integer>();
		if (servos.getTypeCount(ServoType.head) != 0) availableRolls.add(1);
		if (servos.getTypeCount(ServoType.torso) != 0) {availableRolls.add(2); availableRolls.add(3); availableRolls.add(4);}
		availableRolls.add(4); // Pod never requires a re-roll, instead defaults to torso
		if (servos.getTypeCount(ServoType.arm) != 0) {availableRolls.add(5); availableRolls.add(6);}
		if (servos.getTypeCount(ServoType.leg) != 0) {availableRolls.add(7); availableRolls.add(8);}
		if (servos.getTypeCount(ServoType.wing) != 0) availableRolls.add(9);
		if (servos.getTypeCount(ServoType.tail) != 0) availableRolls.add(9);
		if (doSpecial) availableRolls.add(10);
		
		int index;
		ServoType type;
		ServoSide side;
		
		roll = (Integer) select(availableRolls.toArray());
		
		switch (roll)
		{
		case 1: type = ServoType.head; break;
		case 2:
		case 3: type = ServoType.torso; break;
		case 4: // Pod
			if (servos.getTypeCount(ServoType.pod) != 0) type = ServoType.pod;
			else type = ServoType.torso;
			break;
		case 5:
		case 6: type = ServoType.arm; break;
		case 7:
		case 8: type = ServoType.leg; break;
		case 9: // Other
			if (servos.getTypeCount(ServoType.wing) == 0) type = ServoType.tail;
			else if (servos.getTypeCount(ServoType.tail) == 0) type = ServoType.wing;
			else type = select(ServoType.wing, ServoType.tail);
			break;
		case 10: return specialHitChart(true);
		default:
			return null;
		}
		
		side = rollSide(servos, type);
		index = rollXDY(1, servos.getTypeAndSideCount(type, side)) - 1;
		return new HitLocation(type, side, null, null, index);
	}
}
