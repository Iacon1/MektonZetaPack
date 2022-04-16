// By Iacon1
// Created 04/26/2021
// For clients to send to servers

package Modules.GenUtils.PacketTypes;

public class ClientInfoPacket extends Packet
{
	public String version; // Version of MtO that the server is running
	public String mix; // The client's Diffie-Hellman mix
}
