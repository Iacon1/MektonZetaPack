// By Iacon1
// Created 10/27/2021
//

package Modules.Security;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import GameEngine.Commands.ParsingCommand;
import GameEngine.Commands.ParsingCommandAccount;

public abstract class RoleAccount extends ParsingCommandAccount implements RoleHolder
{
	private List<String> roles;

	@Override
	protected void registerCommands()
	{
		super.registerCommands();
		
		ParsingCommand possessCommand = new RoleCommand(
				new String[] {"possess", "Possess"},
				"Possesses a different entity.",
				new ParsingCommand.Parameter[] {
						new ParsingCommand.Parameter(new String[] {"target", "id"}, "The target to possess.", false)},
				new ParsingCommand.Flag[] {},
				(Object caller, Map<String, String> parameters, Map<String, Boolean> flags) -> {possessFunction(caller, parameters, flags);},
				new String[][] {
					new String[]{"admin"},
					new String[]{"moderator"}}); // TODO IDK lol
		registerCommand(possessCommand);
	}
	
	public RoleAccount()
	{
		super();
		roles = new ArrayList<String>();
	}
	
	@Override
	public void addRole(String role)
	{
		if (!hasRole(role)) roles.add(role);
	}
	@Override
	public boolean hasRole(String role)
	{
		if (!this.roles.contains(role)) return false;
		else return true;
	}
	@Override
	public List<String> getRoles()
	{
		return roles;
	}
	
}
