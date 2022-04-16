// By Iacon1
// Created 06/16/2021
// Checking client state
// Sends a packet to the client, then waits for one back

package Modules.GenUtils.HandlerStates;

import GameEngine.Net.StateFactory;
import GameEngine.Net.ThreadState;
import Modules.GenUtils.ClientHandlerThread;
import Modules.GenUtils.DiffieHellman;
import Modules.GenUtils.PacketTypes.ClientInfoPacket;
import Modules.GenUtils.PacketTypes.ServerInfoPacket;
import Utils.JSONManager;
import Utils.Logging;
import Utils.MiscUtils;

public class CheckClient implements ThreadState<ClientHandlerThread>
{
	boolean sent;
	private StateFactory factory;
	private DiffieHellman diffieHellman;
	
	public CheckClient(StateFactory factory)
	{
		this.factory = factory;
		diffieHellman = new DiffieHellman();
		diffieHellman.start();
		sent = false;
	}
	
	@Override
	public void onEnter(ClientHandlerThread parentThread) {parentThread.setEncrypt(false);}

	@Override
	public void processInput(String input, ClientHandlerThread parentThread)
	{
		if (sent)
		{
			ClientInfoPacket packet = JSONManager.deserializeJSON(input, ClientInfoPacket.class);
			if (packet == null || !packet.version.equals(MiscUtils.getVersion())) // We don't actually care about this client anymore
			{
				if (packet == null) Logging.logError("Client " + parentThread.getSocket().getInetAddress() + " tried to connect<br> but failed to send a info packet.");
				else Logging.logError("Client " + parentThread.getSocket().getInetAddress() + " tried to connect<br> but sent a bad packet.");
				parentThread.close();
			}
			else
			{
				Logging.logNotice("Client " + parentThread.getSocket().getInetAddress() + " has connected.");
				diffieHellman.end(packet.mix, parentThread);
				parentThread.queueStateChange(getFactory().getState(MiscUtils.ClassToString(Login.class))); // They're good, let's login	
			}
		}
	}
	@Override
	public String processOutput(ClientHandlerThread parentThread)
	{
		if (!sent)
		{
			ServerInfoPacket packet = new ServerInfoPacket();
			packet.serverName = parentThread.getParent().getName();
			packet.version = MiscUtils.getVersion();
			packet.resourceFolder = "Default"; // TODO Set this
			packet.mix = diffieHellman.getPublicMix();
			
			if (parentThread.getParent().currentPlayers() >= parentThread.getParent().maxPlayers()) // We're full
			{
				packet.note = ServerInfoPacket.Note.full;
				parentThread.close(); // We don't need this connection any more
			}
			else packet.note = ServerInfoPacket.Note.good;
			sent = true;
			
			Logging.logNotice("Sent info to " + parentThread.getSocket().getInetAddress());
			return JSONManager.serializeJSON(packet);
		}
		else return null; // We have nothing to say to them
	}

	@Override
	public StateFactory getFactory()
	{
		return factory;
	}
}
