// By Iacon1
// Created 05/10/2021
// Contains password & username

package Modules.GenUtils.PacketTypes;

public class LoginPacket extends Packet // TODO encrypt these (maybe encrypt *all* packets?)
{
	public String username;
	public String myPassword; // TODO this is likely the best way to do this, at least once it's encrypted
	
	public boolean newUser; // A new user?
}
