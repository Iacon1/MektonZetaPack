// By Iacon1
// Created 04/26/2021
// For handling the connection client-side

package Modules.GenUtils;

import GameEngine.Configurables.ModuleManager;
import GameEngine.Configurables.ModuleTypes.PlayerHandlerModule;
import GameEngine.Configurables.ModuleTypes.StateGiverModule;
import GameEngine.EntityTypes.GameEntity;
import GameEngine.Net.StateFactory;
import GameEngine.Net.StatefulConnectionPairThread;
import GameEngine.Server.Account;
import Utils.Logging;

public class ClientHandlerThread extends StatefulConnectionPairThread
{
	private StateFactory stateFactory_; // Where we get our states
	
	protected volatile String username_ = null; // Client's account name
	
	public Account getAccount()
	{
		return parent_.getAccount(username_);
	}
	protected GameEntity getUserEntity()
	{
		return parent_.getAccount(username_).getPossessee();
	}
	
	protected static volatile BaseServer parent_;
	
	public ClientHandlerThread()
	{
		super();
		stateFactory_ = ModuleManager.getHighestOfType(StateGiverModule.class).handlerFactory();
		initState(stateFactory_.getState(0));
	}
	public void setParent(BaseServer parent)
	{
		parent_ = parent;
	}
	
	@Override
	public void runFunc()
	{
		parent_.update();
		super.runFunc();
	}
	
	@Override
	public void onClose()
	{
		Logging.logNotice("Client " + socket.getInetAddress() + " has disconnected.");
		ModuleManager.getHighestOfType(PlayerHandlerModule.class).sleepPlayer(getAccount());
	}
	
	public BaseServer getParent()
	{
		return parent_;
	}

	public void setUsername(String username)
	{
		username_ = username;
	}
	public String getUsername()
	{
		return username_;
	}
}
