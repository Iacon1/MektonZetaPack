// By Iacon1
// Created 09/11/2021
// Hex utilities

package Modules.HexUtilities;

import GameEngine.Configurables.ModuleTypes.Module;

public class HexUtilities implements Module
{

	@Override
	public ModuleConfig getModuleConfig()
	{
		ModuleConfig config = new ModuleConfig();
		config.moduleName = "Hex Utilities";
		config.moduleVersion = "V0.X";
		config.moduleDescription = "Systems for handling a Hex grid.";
		
		return config;
	}

	@Override
	public void initModule() {}

}
