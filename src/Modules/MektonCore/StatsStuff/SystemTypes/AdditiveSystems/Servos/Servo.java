// By Iacon1
// Created 11/23/2021
// A limb or such thing; Has scale but takes no volume

package Modules.MektonCore.StatsStuff.SystemTypes.AdditiveSystems.Servos;

import Modules.MektonCore.Enums.Scale;
import Modules.MektonCore.StatsStuff.ScaledUnits.ScaledHitValue;
import Modules.MektonCore.StatsStuff.SystemTypes.AdditiveSystems.AdditiveSystem;

public abstract class Servo extends AdditiveSystem
{
	protected Scale scale;
	
	/** Copy constructor. */
	public Servo(Servo servo)
	{
		super(servo);
		scale = servo.scale;
	}
	public Servo() {super();}
	public Servo(Scale scale)
	{
		super();
		this.scale = scale;
	}
	@Override
	public ScaledHitValue getVolume()
	{
		return new ScaledHitValue(scale, 0);
	}
}
