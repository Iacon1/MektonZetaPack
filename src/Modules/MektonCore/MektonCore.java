// By Iacon1
// Created 06/17/2021
//

package Modules.MektonCore;

import com.google.gson.GsonBuilder;

import GameEngine.Configurables.ModuleTypes.GSONModule;
import GameEngine.Configurables.ModuleTypes.Module;
import Modules.MektonCore.Adapters.MektonHexAdapter;
import Utils.GSONConfig.AbsFactory;

public class MektonCore implements Module, GSONModule
{
	@Override
	public ModuleConfig getModuleConfig()
	{
		ModuleConfig config = new ModuleConfig();
		config.moduleName = "Mekton Core";
		config.moduleVersion = "V0.X";
		config.moduleDescription = "Mekton's core rules and system, as best as they\ncan be replicated in this engine.";
		
		return config;
	}

	@Override
	public void initModule()
	{
	}

	@Override
	public void addToBuilder(GsonBuilder builder)
	{
		builder.registerTypeAdapter(MektonHex.class, new MektonHexAdapter());
		builder.registerTypeAdapterFactory(new AbsFactory<MektonHex>(MektonHex.class));
	}
}
