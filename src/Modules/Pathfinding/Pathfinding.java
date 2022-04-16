// By Iacon1
// Created 09/21/2021
// The module for pathfinding algorithms

package Modules.Pathfinding;

import GameEngine.Configurables.ModuleTypes.Module;

public class Pathfinding implements Module
{

	@Override
	public ModuleConfig getModuleConfig()
	{
		ModuleConfig config = new ModuleConfig();
		config.moduleName = "Pathfinding";
		config.moduleVersion = "V0.X";
		config.moduleDescription = "An A* pathfinding engine.";
		
		return config;
	}

	@Override
	public void initModule()
	{
	}

}
