// By Iacon1
// Created 11/16/2021
// List of commands that can be executed

package Modules.GenUtils.Commands;

import java.util.HashMap;
import java.util.Map;

public class ParsingCommandBank
{
	private Map<String, ParsingCommand> commands;
	
	public ParsingCommandBank()
	{
		commands = new HashMap<String, ParsingCommand>();
	}
	
	/** Registers a command.
	 *  If the command's primary name (i. e. The first of its aliases) is already registered,
	 *  this will override the previously registered command.
	 *  
	 *  @param command The command to register.
	 */
	public void registerCommand(ParsingCommand command)
	{
		commands.put(command.getName(), command);
	}
	
	/** Returns whether the command is recognized or not.
	 *  
	 *  @param text The command to check.
	 *  @return Whether a command with the same name is in this bank.
	 */
	public boolean recognizes(String command)
	{
		String[] words = command.split(" ");
		for (String name : commands.keySet())
			if (commands.get(name).hasAlias(words[0])) return true;
		
		return false;
	}
	
	/** Executes the command, if it exists.
	 * 
	 *  @param caller The object calling this.
	 *  @param words The command, parameters, values, and flags to run.
	 */
	public void execute(Object caller, String[] words) throws Exception
	{
		for (String name : commands.keySet())
		{
			if (commands.get(name).hasAlias(words[0]))
			{
				commands.get(name).execute(caller, words);
				return;
			}
		}
	}
	
	/** Executes the command, if it exists.
	 * 
	 *  @param caller The object calling this.
	 *  @param command The command to run.
	 */
	public void execute(Object caller, String command) throws Exception
	{
		String[] words = command.split(" ");
		
		execute(caller, words);
	}
}
