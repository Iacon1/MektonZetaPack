// By Iacon1
// Created 11/23/2021
//

package Modules.MektonCore.Enums;

public enum ArmorType
{
	ablative(0, 0.5),
	standard(1, 1.0),
	alpha(2, 1.25),
	beta(4, 1.5),
	gamma(8, 2.0);
	
	private double DC;
	private double costMult;
	
	private ArmorType(double DC, double costMult)
	{
		this.DC = DC;
		this.costMult = costMult;
	}
	
	public double DC() {return DC;}
	
	public double costMult() {return costMult;}
}
