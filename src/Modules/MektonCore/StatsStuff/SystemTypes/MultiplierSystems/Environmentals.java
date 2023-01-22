// By Iacon1
// Created 1/7/2023
//


package Modules.MektonCore.StatsStuff.SystemTypes.MultiplierSystems;

import java.util.function.Supplier;

import GameEngine.MenuSlate;
import GameEngine.MenuSlate.DataFunction;
import Modules.MektonCore.Enums.EnvironmentType;

public class Environmentals extends MultiplierSystem
{
	private EnvironmentType environmentType;
	
	@Override
	public double getMultiplier()
	{
		return environmentType.getCostMult();
	}

	@Override
	public void populate(MenuSlate slate, Supplier<MenuSlate> supplier)
	{
		slate.setCells(20, 1);
		slate.addInfo(0, 0, "Hydraulics", 4, 0, 1, () -> {return null;});
		slate.addOptions(4, 0, "", 0, 5, 1, EnvironmentType.values(), new DataFunction<EnvironmentType>()
		{
			@Override public EnvironmentType getValue() {return environmentType;}
			@Override public void setValue(EnvironmentType data) {environmentType = data;}
		});
	}
}
