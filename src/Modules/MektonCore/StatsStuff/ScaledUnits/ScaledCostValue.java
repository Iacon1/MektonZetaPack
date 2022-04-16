// By Iacon1
// Created 12/02/2021
// Cost value

package Modules.MektonCore.StatsStuff.ScaledUnits;

import Modules.MektonCore.Enums.Scale;

public class ScaledCostValue extends ScaledValue<ScaledCostValue>
{

	/** Copy constructor. */
	public ScaledCostValue(ScaledCostValue value) {super(value);}
	public ScaledCostValue(Scale scale, double value) {super(scale, value);}
	public ScaledCostValue(Scale scale) {super(scale);}
	public ScaledCostValue(double value) {super(value);}
	public ScaledCostValue() {super();}

	@Override
	protected double getRatio(Scale a, Scale b) {return a.getCostScale() / b.getCostScale();}
	@Override
	protected ScaledCostValue copy() {return new ScaledCostValue(this);}
}
