// By Iacon1
// Created 12/03/2021
//

package Modules.MektonCore.StatsStuff.SheetTypes;

import java.util.List;

import GameEngine.Editor.Editable;
import Modules.MektonCore.Enums.Scale;
import Modules.MektonCore.StatsStuff.AdditiveSystemList;
import Modules.MektonCore.StatsStuff.ScaledUnits.ScaledCostValue;
import Modules.MektonCore.StatsStuff.SystemTypes.MultiplierSystem;

public class MekSheet extends AdditiveSystemList implements Editable
{
	private List<MultiplierSystem> multiplierSystems;
	
	public ScaledCostValue getCost()
	{
		ScaledCostValue baseValue = new ScaledCostValue(Scale.mekton, 0);
		
		for (int i = 0; i < additiveSystems.size(); ++i)
			baseValue.addInPlace(additiveSystems.get(i).getCost());
		
		double modifier = 1;
		for (int i = 0; i < multiplierSystems.size(); ++i) modifier += multiplierSystems.get(i).getMultiplier();
		ScaledCostValue multValue = baseValue.multiply(modifier);
		
		// TODO WE and stuff
		
		return multValue;
	}
}
