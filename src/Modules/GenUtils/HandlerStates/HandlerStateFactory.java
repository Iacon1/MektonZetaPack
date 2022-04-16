// By Iacon1
// Created 06/16/2021
//

package Modules.GenUtils.HandlerStates;

import Utils.MiscUtils;
import GameEngine.Net.StateFactory;
import GameEngine.Net.ThreadState;
import Modules.GenUtils.ClientHandlerThread;

public class HandlerStateFactory implements StateFactory
{

	@Override
	public ThreadState<ClientHandlerThread> getState(int id)
	{
		switch (id)
		{
		case 0: return new CheckClient(this);
		case 2: return new Login(this);
		case 3: return new MainScreen(this);
		default: return new CheckClient(this);
		}
	}

	@Override
	public ThreadState<ClientHandlerThread> getState(String className)
	{
		if (className.equals(MiscUtils.ClassToString(CheckClient.class))) return new CheckClient(this);
		else if (className.equals(MiscUtils.ClassToString(Login.class))) return new Login(this);
		else if (className.equals(MiscUtils.ClassToString(MainScreen.class))) return new MainScreen(this);
		else return new CheckClient(this);
	}

}
