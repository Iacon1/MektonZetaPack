// By Iacon1
// Created 1/7/2023
//


package Modules.MektonCore.StatsStuff.SystemTypes.MultiplierSystems;

import java.util.function.Supplier;

import GameEngine.MenuStuff.MenuSlate;
import GameEngine.MenuStuff.MenuSlate.DataFunction;
import Modules.MektonCore.MektonConfig;
import Modules.MektonCore.Enums.EnvironmentType;

public class Environmentals extends MultiplierSystem
{
	private boolean[] coveredTypes = new boolean[EnvironmentType.values().length];
	
	private void setProtection(EnvironmentType environmentType, boolean value)
	{
		coveredTypes[environmentType.ordinal()] = value;
	}
	public Environmentals()
	{
		setProtection(EnvironmentType.none, true);
		if (MektonConfig.isSpaceProtectionFree()) setProtection(EnvironmentType.space, true);
	}
	public boolean protectsAgainst(EnvironmentType environmentType)
	{
		return coveredTypes[environmentType.ordinal()];
	}
	
	@Override
	public double getMultiplier()
	{
		double mult = 0;
		for (EnvironmentType environmentType : EnvironmentType.values())
			if (protectsAgainst(environmentType)) mult += environmentType.getCostMult();
		return mult;
	}

	@Override
	public void populate(MenuSlate slate, Supplier<MenuSlate> supplier)
	{
		int numTypes = EnvironmentType.values().length;
		slate.setCells(20, Math.max(1, numTypes / 4) * 2 + 1);
		slate.addInfo(0, 0, "Environmentals", 6, 0, 1, () -> {return null;});
		int x = 0, y = 1;
		for (EnvironmentType environmentType : EnvironmentType.values())
		{
			if (environmentType == EnvironmentType.none) continue;
			else if (environmentType == EnvironmentType.space && MektonConfig.isSpaceProtectionFree()) continue;
			slate.addCheckbox(x, y, environmentType.name(), 3, 2, 2, new DataFunction<Boolean>()
			{
				@Override public Boolean getValue() {return protectsAgainst(environmentType);}
				@Override public void setValue(Boolean data) {setProtection(environmentType, data);}	
			});
			x += 5;
			if (x >= 20) {x = 0; y += 2;}
		}
	}

	
}
