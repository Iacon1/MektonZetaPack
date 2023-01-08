// By Iacon1
// Created 09/17/2021
// For hit charts and servo lists

package Modules.MektonCore.StatsStuff;

public class HitLocation
{
	public enum ServoType // Servo type
	{
		torso,
		arm,
		leg,
		head,
		wing,
		tail,
		pod
	}
	
	public enum ServoSide // Which side it's on
	{
		left,
		right,
		middle
	}
	
	public enum Special // Special hit locations for mecha
	{
		weapon, // Pick a random weapon and damage it
		sensors,
		flightSystem,
		shieldMount,
		other,
		cockpit,
		powerplant
	}
	
	public enum Cinematic // Cinematic hit locations for mecha
	{
		hydraulic,
		blunt,
		sensorOverload,
		flightSystem,
		thrusterMalfunction,
		ammoExplosion,
		weaponMalfunction,
		controlJam,
		systemShutdown,
		powerplantOverload
	}

	public ServoType type;
	public ServoSide side;
	public Special special;
	public Cinematic cinematic;
	public int index; // In case multiple things occupy the same slot
	
	public HitLocation(ServoType type, ServoSide side, Special special, Cinematic cinematic, int number)
	{
		this.type = type;
		this.side = side;
		this.special = special;
		this.cinematic = cinematic;
		this.index = number;
	}
}
