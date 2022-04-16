// By Iacon1
// Created 12/01/2021
//

package Modules.Security;

import GameEngine.Configurables.ModuleTypes.Module;

public class Security implements Module
{

	@Override
	public ModuleConfig getModuleConfig()
	{
		ModuleConfig config = new ModuleConfig();
		config.moduleName = "Security";
		config.moduleVersion = "V0.X";
		config.moduleDescription = "A basic roles system.";
		
		return config;
	}

	@Override
	public void initModule()
	{
		// TODO Auto-generated method stub

	}

}
