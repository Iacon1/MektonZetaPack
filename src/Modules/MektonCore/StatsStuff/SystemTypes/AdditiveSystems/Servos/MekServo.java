// By Iacon1
// Created 09/16/2021
// Servo for meks, roadstrikers, ships, etc.
// Remember that pods always have zero health
// TODO:
/* Kill sacrificing
 * Throw range
 * Melee+
 * 
 * Redo space occupation
 */

package Modules.MektonCore.StatsStuff.SystemTypes.AdditiveSystems.Servos;

import GameEngine.MenuSlate.DataFunction;
import GameEngine.Editor.EditorPanel;
import Modules.MektonCore.Enums.ArmorType;
import Modules.MektonCore.Enums.LevelRAM;
import Modules.MektonCore.Enums.Scale;
import Modules.MektonCore.Enums.ServoClass;
import Modules.MektonCore.ExceptionTypes.ExcessValueException;
import Modules.MektonCore.ExceptionTypes.InsufficientHealthException;
import Modules.MektonCore.StatsStuff.HitLocation.ServoType;
import Modules.MektonCore.StatsStuff.ScaledUnits.ScaledCostValue;
import Modules.MektonCore.StatsStuff.ScaledUnits.ScaledHitValue;
import Utils.Logging;

public class MekServo extends Servo
{
	private ServoClass servoClass;
	private ServoClass armorClass;
	private ServoType servoType;
	private ArmorType armorType;
	private LevelRAM levelRAM;
	
	private ScaledHitValue sacrificedHealth;
	private ScaledHitValue armor;

	/** Copy constructor */
	public MekServo(MekServo mekServo)
	{
		super(mekServo);
		this.servoClass = mekServo.servoClass;
		this.armorClass = mekServo.armorClass;
		this.servoType = mekServo.servoType;
		this.armorType = mekServo.armorType;
		this.levelRAM = mekServo.levelRAM;
		
		this.sacrificedHealth = new ScaledHitValue(scale, 0);
		this.armor = new ScaledHitValue(scale, 0);
	}
	public MekServo()
	{
		super();
		this.scale = Scale.mekton;
		this.servoClass = null;
		this.armorClass = null;
		this.servoType = null;
		this.armorType = null;
		this.levelRAM = null;
		
		this.sacrificedHealth = new ScaledHitValue(scale, 0);
		this.armor = new ScaledHitValue(scale, 0);
	}
	public MekServo(Scale scale, ServoClass servoClass, ServoClass armorClass, ServoType servoType, ArmorType armorType, LevelRAM levelRAM)
	{
		this.scale = scale;
		this.servoClass = servoClass;
		this.armorClass = armorClass;
		this.servoType = servoType;
		this.armorType = armorType;
		this.levelRAM = levelRAM;
		
		this.sacrificedHealth = new ScaledHitValue(scale, 0);
		this.armor = new ScaledHitValue(scale, 0);
	}
	
	public Scale getScale()
	{
		return scale;
	}
	
	// Space variables
	@Override
	public ScaledHitValue getVolume()
	{
		return new ScaledHitValue(scale, 0); // Nothing actually stores a mek servo in it so there's no rule for taking space
	}
	private ScaledHitValue getMaxSpacesBase() // The servo's max spaces, accounting for servo class and type but *not* sacrificed health
	{
		switch (servoType)
		{
		case torso: case pod: return new ScaledHitValue(scale, servoClass.level() * 2);
		case arm: case leg: return new ScaledHitValue(scale, servoClass.level() + 1);
		case head: case wing: case tail: return new ScaledHitValue(scale, servoClass.level());
		default: return new ScaledHitValue(scale, 0);
		}
	}
	/** Returns the maximum spaces of the servo. 
	 * 
	 *  @return The maximum spaces of the servo.
	 */
	public ScaledHitValue getMaxSpaces() // The servo's max spaces, accounting for servo class, type, and sacrificed health
	{
		return getMaxSpacesBase().add(sacrificedHealth.multiply(2)); 
	}

	// Armor variables
	/** Gets max armor.
	 *  @return The max armor.
	 */
	public ScaledHitValue getMaxArmor() {return new ScaledHitValue(scale, armorClass.level()).multiply(levelRAM.penalty());}
	/** Sets current armor.
	 * 
	 *  @param scale Scale of the incoming value.
	 *  @param value Value to set to.
	 *  
	 *  @throws ExcessArmorException if you try to give it more armor than its maximum.
	 */
	public void setArmor(ScaledHitValue value) throws ExcessValueException
	{
		if (value.greaterThan(getMaxArmor())) throw new ExcessValueException(value, getMaxArmor(), "armor");
		armor = new ScaledHitValue(value);
	}
	/** Gets current armor.
	 *  @return The current armor.
	 */
	public ScaledHitValue getArmor() {return new ScaledHitValue(armor);}
	/** Gets DC.
	 *  @return The DC.
	 */
	public ScaledHitValue getDC() {return new ScaledHitValue(scale, armorType.DC());}
	/** Gets the RAM reduction factor.
	 *  @return The RAM factor.
	 */
	public double getRAMReduction() {return levelRAM.reduction();}
	
	// Health variables
	private ScaledHitValue getMaxHealthBase() // Without kill sacrificing
	{
		switch (servoType)
		{
		case pod: return new ScaledHitValue(scale, 0);
		default: return getMaxSpacesBase();
		}
	}
	@Override
	public ScaledHitValue getMaxHealth()
	{
		return getMaxHealthBase().subtract(sacrificedHealth);
	}
	/** Sets the sacrificed / reinforced (if negative) health.
	 *  @param value The value to set it to.
	 */
	public void setSacrificedHealth(ScaledHitValue value) throws InsufficientHealthException
	{
		if (!getMaxHealthBase().greaterThan(value)) throw new InsufficientHealthException(value, getMaxHealthBase());
		sacrificedHealth = new ScaledHitValue(value);
	}
	
	/** Gets the cost accounting for armor and the scale of the servo. */
	public ScaledCostValue getCost()
	{
		ScaledCostValue baseCost = new ScaledCostValue(scale, 0);
		switch (servoType)
		{
		case pod: baseCost = new ScaledCostValue(scale, servoClass.level()); break; // Pods are the only type that costs less than their max space
		default:
			if (sacrificedHealth.getValue() < 0) // Reinforced kills cost, sacrificed don't
				baseCost = new ScaledCostValue(scale, getMaxSpaces().getValue(scale)); // Cost scales differently than spaces, so we'll start with spaces at native scale
			else baseCost = new ScaledCostValue(scale, getMaxSpacesBase().getValue(scale));
			break;
		}
		return baseCost.add(new ScaledCostValue(scale, armorType.costMult() * levelRAM.costMult() * armorClass.level()));
	}
	/** Gets the weight in tons, accounting for armor and the scale of the servo. 
	 *  @return The weight of the servo in tons.
	 */
	@Override
	public double getWeight()
	{
		if (sacrificedHealth.getValue() < 0) // Reinforced kills take weight, sacrificed don't
			return getMaxHealth().getValue(Scale.mekton) / 2 + getMaxArmor().getValue(Scale.mekton) / 2;
		else return getMaxHealthBase().getValue(Scale.mekton) / 2 + getMaxArmor().getValue(Scale.mekton) / 2;
	}
	
	@Override
	public String getName()
	{
		return "Mek Servo";
	}
	@Override
	public EditorPanel editorPanel()
	{
		EditorPanel panel = super.editorPanel();
		panel.setCells(10, 20);
		EditorPanel subPanel1 = (EditorPanel) panel.addSubSlate(0, 2, 10, 3, null); // 3, 4, 5
		
		subPanel1.addInfo(0, 0, "Max health:", 4, 6,() -> {return getMaxHealth().getValue(Scale.mekton) + " Mek kills";});
		subPanel1.addInfo(0, 1, "Max spaces:", 4, 6, () -> {return getMaxSpaces().getValue(Scale.mekton) + " Mek spaces";});
		subPanel1.addInfo(0, 2, "Max armor:", 4, 6, () -> {return getMaxArmor().getValue(Scale.mekton) + " Mek SP";});
		
		EditorPanel subPanel2 = (EditorPanel) panel.addSubSlate(0, 7, 10, 6, null); // 3, 4, 5, 6, 7, 8
		
		subPanel2.addOptions(0, 0, "Scale:", 4, 6, Scale.values(), new DataFunction<Scale>()
		{
			@Override public Scale getValue() {return scale;}
			@Override public void setValue(Scale data) {scale = data;}	
		});
		subPanel2.addOptions(0, 1, "Servo class:", 4, 6, ServoClass.values(), new DataFunction<ServoClass>()
		{
			@Override public ServoClass getValue() {return servoClass;}
			@Override public void setValue(ServoClass data) {servoClass = data;}	
		});
		subPanel2.addOptions(0, 2, "Armor class:", 4, 6, ServoClass.values(), new DataFunction<ServoClass>()
		{
			@Override public ServoClass getValue() {return armorClass;}
			@Override public void setValue(ServoClass data) {armorClass = data;}	
		});
		subPanel2.addOptions(0, 3, "Servo type:", 4, 6, ServoType.values(), new DataFunction<ServoType>()
		{
			@Override public ServoType getValue() {return servoType;}
			@Override public void setValue(ServoType data) {servoType = data;}	
		});
		subPanel2.addOptions(0, 4, "Armor type:", 4, 6, ArmorType.values(), new DataFunction<ArmorType>()
		{
			@Override public ArmorType getValue() {return armorType;}
			@Override public void setValue(ArmorType data) {armorType = data;}	
		});
		subPanel2.addOptions(0, 5, "RAM level:", 4, 6, LevelRAM.values(), new DataFunction<LevelRAM>()
		{
			@Override public LevelRAM getValue() {return levelRAM;}
			@Override public void setValue(LevelRAM data) {levelRAM = data;}	
		});

		panel.addIntegerWheel(0, 12, "Sacrificed kills: ", 4, 0, (int) getMaxHealthBase().getValue(scale), 6, new DataFunction<Integer>()
		{

			@Override public Integer getValue() {return (int) sacrificedHealth.getValue(scale);}
			@Override
			public void setValue(Integer data)
			{
				try {setSacrificedHealth(new ScaledHitValue(scale, data));}
				catch (InsufficientHealthException e) {Logging.logException(e);}
			} 
		});
		
		return panel;
	}
	
}
