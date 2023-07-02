// By Iacon1
// Created 4/7/2023
//

package Modules.MektonCore.StatsStuff.SystemTypes.MultiplierSystems;

import java.util.function.Supplier;

import GameEngine.MenuStuff.MenuSlate;
import GameEngine.MenuStuff.MenuSlate.DataFunction;
import Modules.MektonCore.MektonConfig;
import Modules.MektonCore.Enums.EnvironmentType;

public class Cloaking extends MultiplierSystem
{
	private enum CloakingType
	{
		basic(0.15),
		active(0.3),
		pulse(0.1),
		magnetic(0.1),
		beam(0.1),
		fire(0.3),
		combat(0.2);
		
		private double costMult;
		private CloakingType(double costMult)
		{
			this.costMult = costMult;
		}
		public double getCostMult() {return costMult;}
	}
	private boolean[] coveredTypes = new boolean[CloakingType.values().length];
	
	private void setCloaking(CloakingType cloakingType, boolean value)
	{
		coveredTypes[cloakingType.ordinal()] = value;
	}
	public Cloaking() {}
	
	public boolean hasCloaking(CloakingType cloakingType)
	{
		return coveredTypes[cloakingType.ordinal()];
	}
	
	@Override
	public double getMultiplier()
	{
		double mult = 0;
		for (CloakingType cloakingType : CloakingType.values())
			if (hasCloaking(cloakingType)) mult += cloakingType.getCostMult();
		return mult;
	}

	@Override
	public void populate(MenuSlate slate, Supplier<MenuSlate> supplier)
	{
		int numTypes = EnvironmentType.values().length;
		slate.setCells(20, Math.max(1, numTypes / 4) * 2 + 1);
		slate.addInfo(0, 0, "Environmentals", 6, 0, 1, () -> {return null;});
		int x = 0, y = 1;
		for (CloakingType cloakingType : CloakingType.values())
		{
			slate.addCheckbox(x, y, cloakingType.name(), 3, 2, 2, new DataFunction<Boolean>()
			{
				@Override public Boolean getValue() {return hasCloaking(cloakingType);}
				@Override public void setValue(Boolean data) {setCloaking(cloakingType, data);}	
			});
			x += 5;
			if (x >= 20) {x = 0; y += 2;}
		}
	}

	
}
