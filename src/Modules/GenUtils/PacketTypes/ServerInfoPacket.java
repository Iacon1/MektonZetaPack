// By Iacon1
// Created 04/24/2021
// Give server info when starting up

package Modules.GenUtils.PacketTypes;

import GameEngine.Net.Server.ServerInfo;

public class ServerInfoPacket extends Packet
{
	public String serverName; // Server name
	public String version; // Version of MtO that the server is running
	public String resourceFolder; // Folder that should hold resources for this game
	public enum Note
	{
		full,
		good;
	}
	public Note note; // Anything special?
	public String mix; // The server's diffie-hellman mix
	
	public ServerInfo getInfo()
	{
		ServerInfo info = new ServerInfo();
		info.serverName = serverName;
		info.version = version;
		info.resourceFolder = resourceFolder;
		
		return info;
	}
}
