// By Iacon1
// Created 06/16/2021
// Login

package Modules.GenUtils.ClientStates;

import Client.GameClientThread;
import GameEngine.Net.StateFactory;
import GameEngine.Net.ThreadState;
import Modules.GenUtils.ClientFrames.LoginDialog;
import Modules.GenUtils.PacketTypes.LoginFeedbackPacket;
import Modules.GenUtils.PacketTypes.LoginPacket;
import Utils.JSONManager;
import Utils.Logging;
import Utils.MiscUtils;

public class Login implements ThreadState<GameClientThread>
{
	private StateFactory factory;
	private boolean successful;
	
	public Login(StateFactory factory)
	{
		this.factory = factory;
	}
	
	@Override
	public void onEnter(GameClientThread parentThread)
	{
		LoginDialog.main(parentThread);
		parentThread.setEncrypt(true);
	}
	
	@Override
	public void processInput(String input, GameClientThread parentThread)
	{
		LoginFeedbackPacket packet = JSONManager.deserializeJSON(input, LoginFeedbackPacket.class);
		
		successful = packet.successful;
		if (successful)
		{
			LoginDialog dialog = (LoginDialog) parentThread.getContainer("login");
			dialog.setVisible(false);
			dialog.dispose();
			parentThread.queueStateChange(getFactory().getState(MiscUtils.ClassToString(MainScreen.class)));
		}
		else
		{
			LoginDialog dialog = (LoginDialog) parentThread.getContainer("login");
			dialog.onFail("Login failed.");
		}
	}
	@Override
	public String processOutput(GameClientThread parentThread)
	{
		LoginDialog dialog = (LoginDialog) parentThread.getContainer("login");
		if (dialog.isVisible() == false && !successful)
			parentThread.close();
		LoginPacket packet = dialog.getPacket();
		if (packet != null)
			return JSONManager.serializeJSON(packet);
		else return null;
	}

	@Override
	public StateFactory getFactory()
	{
		return factory;
	}

}
