// By Iacon1
// Created 1/7/2023
//


package Modules.MektonCore.StatsStuff.SystemTypes.MultiplierSystems;

import java.util.function.Supplier;

import GameEngine.MenuStuff.MenuSlate;
import GameEngine.MenuStuff.MenuSlate.DataFunction;

public class InternalAutomation extends MultiplierSystem
{
	private enum Portfolio
	{
		level1 (0.4, 1),
		level2 (0.6, 2),
		level3 (0.7, 3),
		level4 (0.9, 4),
		level5 (1.0, 5),
		level7 (1.2, 7),
		level10(1.5, 10),
		level15(1.7, 15),
		level20(1.9, 20),
		level25(2.0, 25);
		
		private double costMult;
		private int skillCount;

		private Portfolio(double costMult,  int skillCount)
		{
			this.costMult = costMult;
			this.skillCount = skillCount;
		}

		public double getCostMult() {return costMult;}
		public int getSkillCount() {return skillCount;}
	}
	private int level = 0;
	private Portfolio portfolio = Portfolio.level1;
	
	@Override
	public double getMultiplier()
	{
		return .1 * (double) level * portfolio.getCostMult();
	}
	
	@Override
	public void populate(MenuSlate slate, Supplier<MenuSlate> supplier)
	{
		slate.setCells(20, 2);
		slate.addInfo(0, 0, "Internal Automation", 2, 0, 2, () -> {return null;});
		slate.addIntegerWheel(2, 0, "Level: ", 2, 0, 10, 2, 2, new DataFunction<Integer>()
		{
			@Override public Integer getValue() {return level;}
			@Override public void setValue(Integer data) {level = data;}
		});
		slate.addOptions(7, 0, "Portfolio: ", 2, 4, 2, Portfolio.values(), new DataFunction<Portfolio>()
		{
			@Override public Portfolio getValue() {return portfolio;}
			@Override public void setValue(Portfolio data) {portfolio = data;}
		});
	}
}
