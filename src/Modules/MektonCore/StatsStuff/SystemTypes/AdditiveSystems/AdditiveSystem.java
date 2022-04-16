// By Iacon1
// Created 09/16/2021
// A system that takes up space and has health, weight, and has additive cost.

package Modules.MektonCore.StatsStuff.SystemTypes.AdditiveSystems;

import GameEngine.Editor.Editable;
import GameEngine.Editor.EditorPanel;
import Modules.MektonCore.Enums.Scale;
import Modules.MektonCore.ExceptionTypes.ExcessValueException;
import Modules.MektonCore.StatsStuff.ScaledUnits.ScaledCostValue;
import Modules.MektonCore.StatsStuff.ScaledUnits.ScaledHitValue;

public abstract class AdditiveSystem implements Editable
{
	private int systemID;
	private ScaledHitValue health;

	/** Copy constructor */
	public AdditiveSystem(AdditiveSystem system)
	{
		health = new ScaledHitValue(system.health);
	}
	public AdditiveSystem()
	{
		health = new ScaledHitValue();
	}
	
	public int getID()
	{
		return systemID;
	}
	
	// Space variables
	/** Returns how much space it occupies.
	 *  @return The volume.
	 */
	public abstract ScaledHitValue getVolume();
	
	// Health variables
	/** Gets max health.
	 * 
	 *  @return The maximum health.
	 */
	public abstract ScaledHitValue getMaxHealth();
	/** Sets current health.
	 * 
	 *  @param scale Scale of the incoming value.
	 *  @param value Value to set to.
	 *  
	 *  @throws ExcessValueException if you try to give it more health than its maximum.
	 */
	public void setHealth(ScaledHitValue value) throws ExcessValueException
	{
		if (value.greaterThan(getMaxHealth())) throw new ExcessValueException(value, getMaxHealth(), "health");
		health.setValue(value);
	}
	/** Returns the current health.
	 *  @return health.
	 */
	public ScaledHitValue getHealth()
	{
		return new ScaledHitValue(health);
	}
	
	// Weight variables
	/** Returns the weight in tons.
	 *  @return The weight in tons.
	 */
	public abstract double getWeight();
	
	//Cost variables
	/** Returns the cost.
	 *  @return The cost.
	 */
	public abstract ScaledCostValue getCost();

	@Override
	public EditorPanel editorPanel()
	{
		EditorPanel panel = new EditorPanel(640, 480, 8, 4);
		panel.addInfo(0, 0, "Cost:", 4, 4, () -> {return getCost() + " CP";});
		panel.addInfo(0, 1, "Weight:", 4, 4, () -> {return getWeight() + " tons";});
		return panel;
	}
}
