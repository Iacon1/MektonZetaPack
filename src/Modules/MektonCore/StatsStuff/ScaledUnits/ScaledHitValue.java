// By Iacon1
// Created 12/02/2021
//

package Modules.MektonCore.StatsStuff.ScaledUnits;

import Modules.MektonCore.Enums.Scale;

public class ScaledHitValue extends ScaledValue<ScaledHitValue>
{

	/** Copy constructor. */
	public ScaledHitValue(ScaledHitValue value) {super(value);}
	public ScaledHitValue(Scale scale, double value) {super(scale, value);}
	public ScaledHitValue(Scale scale) {super(scale);}
	public ScaledHitValue(double value) {super(value);}
	public ScaledHitValue() {super();}

	@Override
	protected double getRatio(Scale a, Scale b) {return a.getDamageScale() / b.getDamageScale();}
	@Override
	protected ScaledHitValue copy() {return new ScaledHitValue(this);}
}
