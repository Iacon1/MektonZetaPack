// By Iacon1
// Created 09/12/2021
//

package Modules.MektonCore;

import GameEngine.Configurables.ConfigManager;

public class MektonConfig
{
	public static boolean areDiceSenki()
	{
		return Boolean.valueOf(ConfigManager.getValue("Mekton_senkiDice", "false"));
	}
	
	public static boolean isSpaceProtectionFree()
	{
		return Boolean.valueOf(ConfigManager.getValue("Mekton_freeSpaceEnv", "false"));
	}
}
