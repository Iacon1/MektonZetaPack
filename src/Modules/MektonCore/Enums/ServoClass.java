package Modules.MektonCore.Enums;

public enum ServoClass
{
	superLight(1),
	lightWeight(2),
	striker(3),
	mediumStriker(4),
	heavyStriker(5),
	mediumWeight(6),
	lightHeavy(7),
	mediumHeavy(8),
	armoredHeavy(9),
	superHeavy(10),
	megaHeavy(11);
	
	private int level; // Assuming a head
	private ServoClass(int level) {this.level = level;}
	public int level() {return level;}
}