// By Iacon1
// Created 06/17/2021
//

package Modules.MektonCore;

import com.google.gson.GsonBuilder;

import GameEngine.Configurables.ModuleTypes.DefaultModule;
import GameEngine.Configurables.ModuleTypes.GSONModule;
import Modules.MektonCore.Adapters.MektonHexAdapter;

public class MektonCore extends DefaultModule implements GSONModule
{
	@Override
	public void addToBuilder(GsonBuilder builder)
	{
		builder.registerTypeAdapter(MektonHex.class, new MektonHexAdapter());
//		builder.registerTypeAdapterFactory(new AbsFactory<MektonMap>(MektonMap.class));
	}
}
