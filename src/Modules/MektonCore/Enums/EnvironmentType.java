// By Iacon1
// Created 11/17/2021
// Environment types

package Modules.MektonCore.Enums;

public enum EnvironmentType
{
	none(0),
	arctic(.05),
	desert(.05),
	underwater(.05),
	highPressure(.05),
	space(.05),
	EM(.1),
	reentry(.1);
	
	private double costMult;
	
	private EnvironmentType(double costMult)
	{
		this.costMult = costMult;
	}
	
	public double getCostMult() {return costMult;}
}
