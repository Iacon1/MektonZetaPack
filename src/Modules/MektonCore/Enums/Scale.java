// By Iacon1
// Created 11/18/2021
// Scales of entities

package Modules.MektonCore.Enums;

public enum Scale
{
	human(0.04, 0.02, 0.08), // Should be 0.1, 0.1, 0.2 according to MZ+ but I need 1 kill = 1 hit & 1 hex = 1 meter
	roadstriker(0.2, 0.2, 0.33),
	mekton(1, 1, 1), // 1 kill = 25 hits, 1 hex = 50 m
	corvette(10, 10, 25),
	starship(100, 100, 500);
	
	private double damageScale;
	private double distanceScale;
	private double costScale;
	
	private Scale(double damageScale, double distanceScale, double costScale)
	{
		this.damageScale = damageScale;
		this.distanceScale = distanceScale;
		this.costScale = costScale;
	}
	
	public double getDamageScale() {return damageScale;}
	public double getDistanceScale() {return distanceScale;}
	public double getCostScale() {return costScale;}
}
