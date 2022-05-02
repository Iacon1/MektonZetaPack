// By Iacon1
// Created 12/03/2021
//

package Modules.MektonCore.StatsStuff.SheetTypes;

import java.util.List;

import GameEngine.Editor.Editable;
import GameEngine.Editor.EditorPanel;
import Modules.MektonCore.Enums.Scale;
import Modules.MektonCore.StatsStuff.AdditiveSystemList;
import Modules.MektonCore.StatsStuff.ScaledUnits.ScaledCostValue;
import Modules.MektonCore.StatsStuff.SystemTypes.MultiplierSystem;
import Modules.MektonCore.StatsStuff.SystemTypes.AdditiveSystems.AdditiveSystem;

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
	
	public double getWeight()
	{
		double weight = 0;
		
		for (AdditiveSystem additiveSystem : additiveSystems) {weight += additiveSystem.getWeight();}
		
		return weight;
	}
	@Override
	public EditorPanel editorPanel()
	{
		EditorPanel editorPanel = new EditorPanel(640, 480, 64, 64); // TODO 
		editorPanel.addInfo(0, 0, "Name:", 5, 5, () -> {return "Mek";}); // TODO
		editorPanel.addInfo(0, 1, "Cost:", 5, 5, () -> {return getCost().getValue(Scale.mekton) + " CP";});
		editorPanel.addInfo(0, 2, "Weight:", 5, 5, () -> {return getWeight() + " tons";});
		editorPanel.addOptions(0, 3, "Servos", 5, 10, 20, null, null);
		
		return editorPanel;
	}
}
