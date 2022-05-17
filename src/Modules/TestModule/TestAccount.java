// By Iacon1
// Created 06/17/2021
// Test account implementation

package Modules.TestModule;

import javax.swing.JPanel;

import GameEngine.IntPoint2D;
import GameEngine.Configurables.ConfigManager;
import Modules.GenUtils.Commands.ParsingCommandAccount;
import Modules.HexUtilities.HexEntity;

public class TestAccount extends ParsingCommandAccount
{
	public TestAccount()
	{
		super();
	}
	@Override
	public IntPoint2D getCamera()
	{
		HexEntity hexObject = (HexEntity) getPossessee();
//		return new Point2D(0, 0);
		return hexObject.getPos().subtract(new IntPoint2D(ConfigManager.getScreenWidth(), ConfigManager.getScreenHeight()).divide(2));
	}

	@Override
	public JPanel serverPanel()
	{
		String status = null;
		if (loggedIn == true) status = "Online";
		else status = "Offline";
		return new AccountPanel(username, status);
	}

}
