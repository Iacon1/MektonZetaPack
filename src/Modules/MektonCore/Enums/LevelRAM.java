// By Iacon1
// Created 11/23/2021
// Levels of RAM (Radiation-Absorbing Material)

package Modules.MektonCore.Enums;

public enum LevelRAM
{
	none(0.0, 1.0, 1),
	oneFifth(0.2, 1.5, 1),
	oneFourth(0.25, 1.8, 0.8),
	oneThird(0.33, 2.2, 0.75),
	oneHalf(0.5, 2.5, 0.66);
	
	private double reduction;
	private double costMult;
	private double penalty;
	
	private LevelRAM(double reduction, double costMult, double penalty)
	{
		this.reduction = reduction;
		this.costMult = costMult;
		this.penalty = penalty;
	}
	
	public double reduction() {return reduction;}
	public double costMult() {return costMult;}
	public double penalty() {return penalty;}
}
