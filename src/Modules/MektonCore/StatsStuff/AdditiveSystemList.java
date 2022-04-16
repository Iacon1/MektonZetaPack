// By Iacon1
// Created 09/16/2021
// A list of systems

package Modules.MektonCore.StatsStuff;

import GameEngine.Editor.Editable;
import GameEngine.Editor.EditorPanel;
import Modules.MektonCore.Enums.ArmorType;
import Modules.MektonCore.Enums.LevelRAM;
import Modules.MektonCore.Enums.Scale;
import Modules.MektonCore.Enums.ServoClass;
import Modules.MektonCore.StatsStuff.HitLocation.ServoSide;
import Modules.MektonCore.StatsStuff.HitLocation.ServoType;
import Modules.MektonCore.StatsStuff.SystemTypes.AdditiveSystems.AdditiveSystem;
import Modules.MektonCore.StatsStuff.SystemTypes.AdditiveSystems.Servos.MekServo;
import Modules.MektonCore.StatsStuff.SystemTypes.AdditiveSystems.Servos.Servo;
import Utils.IndexTable;
import Utils.IndexTypesException;
import Utils.Logging;

public class AdditiveSystemList implements Editable
{	
	protected IndexTable<AdditiveSystem> additiveSystems;

	public AdditiveSystemList()
	{
		additiveSystems = new IndexTable<AdditiveSystem>(ServoType.class, ServoSide.class);
	}
	
	/** If the servo is already in the list:
	 *    Adds the servo to the given type & side.
	 *  If the servo isn't already in the list:
	 *    Adds it to the system list then proceeds with above.
	 *  
	 *  @param type The type to list the servo as.
	 *  @param side The side to list the servo as.
	 *  @param servo The servo to add or to list.
	 */
	public void addServo(ServoType type, ServoSide side, Servo servo) {additiveSystems.add(servo, type, side, servo);}
	
	public Servo getServo(ServoType type, ServoSide side, int index)
	{
		try {return (Servo) additiveSystems.get(type, side).get(index);}
		catch (IndexTypesException e)
		{
			Logging.logException(e);
			return null;
		}
	}
	
	public Servo getServo(HitLocation location) {return getServo(location.type, location.side, location.index);}
	
	/** Returns the amount of servos of the given type.
	 * 
	 *  @param category The type of servo to count.
	 *  
	 *  @return The amount of servos of that type.
	 */
	public int servoCount(ServoType type)
	{
		try {return additiveSystems.get(type, null).size();}
		catch (IndexTypesException e)
		{
			Logging.logException(e);
			return 0;
		}
	}
	/** Returns the amount of servos in the given side.
	 * 
	 *  @param side The side to count.
	 *  
	 *  @return The amount of servos with that side.
	 */
	public int servoCount(ServoSide side)
	{
		try {return additiveSystems.get(null, side).size();}
		catch (IndexTypesException e)
		{
			Logging.logException(e);
			return 0;
		}
	}
	/** Returns the amount of servos in the given category & side.
	 * 
	 *  @param Type the type of servo to count.
	 *  @param side The side to count on.
	 *  
	 *  @return The amount of servos with that combination of category & side.
	 */
	public int servoCount(ServoType type, ServoSide side)
	{
		try {return additiveSystems.get(type, side).size();}
		catch (IndexTypesException e)
		{
			Logging.logException(e);
			return 0;
		}
	}

	/** Removes the given system from the table.
	 *  
	 *  @param system System to remove.
	 */
	public void removeSystem(AdditiveSystem additiveSystem) {additiveSystems.remove(additiveSystem);}
	public void addSystem(AdditiveSystem additiveSystem)
	{
		additiveSystems.add(additiveSystem);
	}

	@Override
	public String getName()
	{
		return "Additive System List";
	}
	@Override
	public EditorPanel editorPanel()
	{
		EditorPanel editorPanel = new EditorPanel();
		return editorPanel;
	}
}
