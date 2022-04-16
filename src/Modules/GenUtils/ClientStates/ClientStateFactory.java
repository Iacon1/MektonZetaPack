// By Iacon1
// Created 06/16/2021
//

package Modules.GenUtils.ClientStates;

import Utils.MiscUtils;
import Client.GameClientThread;
import GameEngine.Net.StateFactory;
import GameEngine.Net.ThreadState;

public class ClientStateFactory implements StateFactory
{

	@Override
	public ThreadState<GameClientThread> getState(int id)
	{
		switch (id)
		{
		case 0: return new CheckServer(this);
		case 1: return new BadServer(this);
		case 2: return new Login(this);
		case 3: return new MainScreen(this);
		default: return new CheckServer(this);
		}
	}

	@Override
	public ThreadState<GameClientThread> getState(String className)
	{
		if (className.equals(MiscUtils.ClassToString(CheckServer.class))) return new CheckServer(this);
		else if (className.equals(MiscUtils.ClassToString(BadServer.class))) return new BadServer(this);
		else if (className.equals(MiscUtils.ClassToString(Login.class))) return new Login(this);
		else if (className.equals(MiscUtils.ClassToString(MainScreen.class))) return new MainScreen(this);
		else return new CheckServer(this);
	}

}
