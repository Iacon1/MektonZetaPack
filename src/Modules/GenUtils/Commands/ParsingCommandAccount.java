// By Iacon1
// Created 11/16/2021
//

package Modules.GenUtils.Commands;

import GameEngine.EntityTypes.CommandRunner;
import GameEngine.Server.Account;
import Utils.Logging;

import java.util.Map;

public abstract class ParsingCommandAccount extends Account
{
	private transient ParsingCommandBank commandBank;
	
	// Command functions
	protected void helpFunction(Object caller, Map<String, String> parameters, Map<String, Boolean> flags)
	{}
	protected void possessFunction(Object caller, Map<String, String> parameters, Map<String, Boolean> flags)
	{
		int newPossessee = Integer.valueOf(parameters.get("target"));

		possess(newPossessee);
	}
	protected void registerCommand(ParsingCommand command)
	{
		commandBank.registerCommand(command);
	}
	
	/** Registers the list of commands the account has available.
	 * 
	 */
	protected void registerCommands()
	{
		ParsingCommand helpCommand = new ParsingCommand(
				new String[] {"help", "Help"},
				"Lists all commands or gives info on one command.",
				new ParsingCommand.Parameter[] {
						new ParsingCommand.Parameter(new String[] {"command", "c"}, "The command to get info on.", true)},
				new ParsingCommand.Flag[] {},
				(Object caller, Map<String, String> parameters, Map<String, Boolean> flags) -> {helpFunction(caller, parameters, flags);}
				);
		registerCommand(helpCommand);
		
		ParsingCommand possessCommand = new ParsingCommand(
				new String[] {"possess", "Possess"},
				"Possesses a different entity.",
				new ParsingCommand.Parameter[] {
						new ParsingCommand.Parameter(new String[] {"target", "id"}, "The target to possess.", false)},
				new ParsingCommand.Flag[] {},
				(Object caller, Map<String, String> parameters, Map<String, Boolean> flags) -> {possessFunction(caller, parameters, flags);}
				);
		registerCommand(possessCommand);
	}
	
	public ParsingCommandAccount()
	{
		super();
		commandBank = new ParsingCommandBank();
		
		registerCommands();
	}

	
	
	@Override
	public boolean runCommand(String... words)
	{
		if (super.runCommand(words)) return true;
		
		if (commandBank.recognizes(words[0]))
		{
			try {commandBank.execute(this, words);}
			catch (Exception e) {Logging.logException(e);}
			return true;	
		}
		else if (CommandRunner.class.isAssignableFrom(getPossessee().getClass())) return ((CommandRunner) getPossessee()).runCommand(words);
		else return false;
	}
}
