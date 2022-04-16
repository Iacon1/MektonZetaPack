// By Iacon1
// Created 12/02/2021
// Cost value

package Modules.MektonCore.StatsStuff.ScaledUnits;

import Modules.MektonCore.Enums.Scale;

public class ScaledDistanceValue extends ScaledValue<ScaledDistanceValue>
{

	/** Copy constructor. */
	public ScaledDistanceValue(ScaledDistanceValue value) {super(value);}
	public ScaledDistanceValue(Scale scale, double value) {super(scale, value);}
	public ScaledDistanceValue(Scale scale) {super(scale);}
	public ScaledDistanceValue(double value) {super(value);}
	public ScaledDistanceValue() {super();}

	@Override
	protected double getRatio(Scale a, Scale b) {return a.getDistanceScale() / b.getDistanceScale();}
	@Override
	protected ScaledDistanceValue copy() {return new ScaledDistanceValue(this);}
}
