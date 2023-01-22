// By Iacon1
// Created 09/12/2021
//

package Modules.HexUtilities;

import GameEngine.Configurables.ConfigManager;

public class HexConfig
{
	public static int getHexRadius() // Hex radius in pixels
	{
		return Integer.valueOf(ConfigManager.getValue("hex_radius", "19"));
	}
	public static int getHexWidth() // Hex width in pixels
	{
		return getHexRadius() * 2;
	}
	public static int getHexHeight() // Hex height in pixels
	{
		return (int) (getHexRadius() * Math.sqrt(3));
	}
}
