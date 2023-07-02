// By Iacon1
// Created 4/7/2023
//

package Modules.MektonCore.StatsStuff.SystemTypes.MultiplierSystems;

import java.util.function.Supplier;

import GameEngine.MenuStuff.MenuSlate;
import GameEngine.MenuStuff.MenuSlate.DataFunction;
import Modules.MektonCore.MektonConfig;
import Modules.MektonCore.Enums.EnvironmentType;

public class ShadowImager extends MultiplierSystem
{
	private int shadowCount;
	private boolean holographic;
	
	private void setCount(int shadowCount)
	{
		this.shadowCount = shadowCount;
	}
	
	private void setHolographic(boolean holographic)
	{
		this.holographic = holographic;
	}
	
	@Override
	public double getMultiplier()
	{
		return (0.05 * (double) shadowCount) * (holographic? 1. : .7);
	}

	@Override
	public void populate(MenuSlate slate, Supplier<MenuSlate> supplier)
	{
		slate.setCells(20, 4);
		slate.addInfo(0, 0, "Shadow Imager", 6, 0, 1, () -> {return null;});
		slate.addIntegerWheel(2, 2, "Shadow Count: ", 5, 0, 10, 2, 2, new DataFunction<Integer>()
		{
			@Override public Integer getValue() {return shadowCount;}
			@Override public void setValue(Integer data) {shadowCount = data;}
		});
		slate.addCheckbox(2, 2, "Holographic: ", 5, 1, 2, new DataFunction<Boolean>()
		{
			@Override public Boolean getValue() {return holographic;}
			@Override public void setValue(Boolean data) {holographic = data;}
		});
	}
}

