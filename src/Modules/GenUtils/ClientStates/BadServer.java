// By Iacon1
// Created 06/16/2021
// Bad server

package Modules.GenUtils.ClientStates;

import Client.ConnectFailDialog;
import Client.GameClientThread;
import GameEngine.Net.StateFactory;
import GameEngine.Net.ThreadState;
import Utils.MiscUtils;

public class BadServer implements ThreadState<GameClientThread>
{
	private StateFactory factory;
	
	public BadServer(StateFactory factory)
	{
		this.factory = factory;
	}
	
	@Override
	public void onEnter(GameClientThread parentThread)
	{
		if (parentThread.getSocket() == null)
			ConnectFailDialog.main("Server wasn't found.");
		else if (parentThread.getInfo() == null)
			ConnectFailDialog.main("Server wasn't recognized.");
		else if (!parentThread.getInfo().version.equals(MiscUtils.getVersion()))
			ConnectFailDialog.main("Server was version " + parentThread.getInfo().version + ";<br> Should be " + MiscUtils.getVersion());
		parentThread.close();
	}

	@Override
	public void processInput(String input, GameClientThread parentThread) {}
	@Override
	public String processOutput(GameClientThread parentThread) {return null;}
	
	@Override
	public StateFactory getFactory()
	{
		return factory;
	}

}