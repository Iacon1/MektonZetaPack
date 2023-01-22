// By Iacon1
// Created 09/12/2021
//

package Modules.MektonCore;

import GameEngine.Configurables.ConfigManager;

public class MektonConfig
{
	public static boolean isSenkiDice()
	{
		return Boolean.valueOf(ConfigManager.getValue("mekton_senkiDice", "false"));
	}
}
