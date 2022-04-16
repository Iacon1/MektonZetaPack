// By Iacon1
// Created 04/26/2021
//

package Modules.GenUtils;

import GameEngine.Server.GameServer;

public class BaseServer extends GameServer<ClientHandlerThread>
{	
	public BaseServer()
	{	
		super(() -> new ClientHandlerThread());
		ClientHandlerThread thread = new ClientHandlerThread();
		thread.setParent(this);
	}
	
	
	public String getName() // Get server name
	{
		return null;
	}

	public int currentPlayers()
	{
		return 0;
	}
	public int maxPlayers()
	{
		return 10;
	}
}
