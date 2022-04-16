package Modules.GenUtils.ClientStates;

import Client.GameClientThread;
import GameEngine.Net.StateFactory;
import GameEngine.Net.ThreadState;
import Modules.GenUtils.DiffieHellman;
import Modules.GenUtils.PacketTypes.ClientInfoPacket;
import Modules.GenUtils.PacketTypes.ServerInfoPacket;
import Utils.JSONManager;
import Utils.MiscUtils;

public class CheckServer implements ThreadState<GameClientThread>
{
	private enum Result
	{
		waiting,
		login,
		bad,
		done
	}
	private StateFactory factory;
	private volatile Result result; // 0 - Waiting for result; 1 - Login; 2 - Bad Server; 3 - Done
	private DiffieHellman diffieHellman;
	
	public CheckServer(StateFactory factory)
	{
		this.factory = factory;
		diffieHellman = new DiffieHellman();
		diffieHellman.start();
		
		result = Result.waiting;
	}
	
	@Override
	public void onEnter(GameClientThread parentThread) {parentThread.setEncrypt(false);}
	
	@Override
	public void processInput(String input, GameClientThread parentThread)
	{
		ServerInfoPacket packet = JSONManager.deserializeJSON(input, ServerInfoPacket.class);
		
		if (packet == null)
		{
			result = Result.bad;
			parentThread.queueStateChange(getFactory().getState(MiscUtils.ClassToString(BadServer.class)));
		}
		else
		{
			parentThread.setInfo(packet.getInfo());
			
			if (parentThread.getSocket() == null || !packet.version.equals(MiscUtils.getVersion()))
			{
				result = Result.bad;
				parentThread.queueStateChange(getFactory().getState(MiscUtils.ClassToString(BadServer.class)));
			}
			else
			{
				result = Result.login;
				diffieHellman.start();
				diffieHellman.end(packet.mix, parentThread);
				
				parentThread.queueStateChange(getFactory().getState(MiscUtils.ClassToString(Login.class)));
			}
		}
	}
	@Override
	public String processOutput(GameClientThread parentThread)
	{
		if (result == Result.waiting) return null;
		
		if (result == Result.login) // We decided to go forward!
		{
			result = Result.done;
			ClientInfoPacket packet = new ClientInfoPacket();
			packet.version = MiscUtils.getVersion();
			packet.mix = diffieHellman.getPublicMix();
			
			return JSONManager.serializeJSON(packet);
		}
		else if (result == Result.bad)
		{
			result = Result.done;
			return null; // We have nothing to say to them
		}
		else if (result == Result.done) return null;
		else return null; // ???
	}
	
	@Override
	public StateFactory getFactory()
	{
		return factory;
	}
}
