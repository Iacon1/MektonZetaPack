// By Iacon1
// Created 11/07/2021
//

package Modules.GenUtils.Commands;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import Utils.MiscUtils;

public class ParsingCommand
{
	public static interface CommandFunction
	{
		public void execute(Object caller, Map<String, String> parameters, Map<String, Boolean> flags) throws InvalidParameterException;
	}
	
	public static class Parameter
	{
		public String[] aliases;
		public String name()
		{
			return aliases[0];
		}
		public String usage;
		public boolean optional;
		
		public Parameter(String[] aliases, String usage, boolean optional)
		{
			this.aliases = aliases;
			this.usage = usage;
			this.optional = optional;
		}
		
		@Override
		public String toString()
		{
			String documentation = MiscUtils.arrayToString(aliases, ", ");
			if (optional) documentation += " (Optional)";
			documentation += ": " + usage;
			
			return documentation;
		}
	}
	
	public static class Flag
	{
		public String[] aliases;
		public String name()
		{
			return aliases[0];
		}
		public String usage;
		// All flags are optional
		
		public Flag(String[] aliases, String usage)
		{
			this.aliases = aliases;
			this.usage = usage;
		}
		
		@Override
		public String toString()
		{
			String documentation = MiscUtils.arrayToString(aliases, ", ");
			documentation += ": " + usage;
			
			return documentation;
		}
	}
	private String[] aliases;
	private String usage;
	private Parameter[] parameters;
	private Flag[] flags;
	private static char tagChar = '-'; // Denotes a tag, i. e. a parameter or flag
	
	private CommandFunction commandFunction;
	
	/** Constructor.
	 * 
	 *  @param names The list of names this command can be called by.
	 *  @param usage The description of this command.
	 *  @param parameters The list of parameter names for this command, each with their own set of aliases.
	 *  @param flags The set of flag names for this command, listed with aliases like the parameters.
	 *  @param commandFunction the function that runs when this command is executed.
	 */
	public ParsingCommand(String[] aliases, String usage, Parameter[] parameters, Flag[] flags, CommandFunction commandFunction)
	{
		this.aliases = aliases;
		this.usage = usage;
		this.parameters = parameters;
		this.flags = flags;
		this.commandFunction = commandFunction;
	}

	public String getName()
	{
		return aliases[0];
	}
	public boolean hasAlias(String alias)
	{
		return Arrays.asList(aliases).contains(alias);
	}
	public String getDocumentation()
	{
		String documentation = 
				getName() + ": \n" +
				"  Aliases: " + MiscUtils.arrayToString(aliases, ", ") + "\n" +
				"  Parameters: \n    " + MiscUtils.arrayToString(parameters, "\n    ") + "\n" +
				"  Flags: \n    " + MiscUtils.arrayToString(flags, "\n    ") + "\n" + 
				"  Usage: " + usage + "\n";
		// TODO string manager
		return documentation;
	}
	
	private Map<String, String> parseParameters(String[] words) throws InvalidParameterException // Parses out all parameters
	{
		Map<String, String> parameterValues = new HashMap<String, String>();
		for (int i = 0; i < parameters.length; ++i)
		{
			parameterValues.put(parameters[i].name(), null);
			for (int j = 0; j < parameters[i].aliases.length; ++j) // Search to see if any of the parameter's aliases are there
			{
				int index = Arrays.asList(words).indexOf(tagChar + parameters[i].aliases[j]);
				
				if (index != -1) // Found an alias
				{
					if (index + 1 >= words.length)
						throw new InvalidParameterException(parameters[i].aliases[j], "Null");
					else if (words[index + 1].charAt(0) == tagChar)
						throw new InvalidParameterException(parameters[i].aliases[j], words[index + 1]);		
					else
					{
						parameterValues.put(parameters[i].name(), words[index + 1]);
						break;
					}
				}
				else continue;
			}
			if (parameterValues.get(parameters[i].name()) == null && !parameters[i].optional)
				throw new InvalidParameterException(parameters[i].name() + " must have a value.");
		}
		
		return parameterValues;
	}
	private Map<String, Boolean> parseFlags(String[] words) // Parses out all flags
	{
		Map<String, Boolean> flagValues = new HashMap<String, Boolean>();
		
		for (int i = 0; i < flags.length; ++i)
		{
			flagValues.put(flags[i].name(), false);
			for (int j = 0; j < flags[i].aliases.length; ++j) // Search to see if any of the flag's aliases are there
			{
				int index = Arrays.asList(words).indexOf(tagChar + this.flags[i].aliases[j]);
				
				if (index != -1) // Found an alias
				{
					flagValues.put(this.flags[i].name(), true);
					break;
				}
				else continue;
			}
		}
		
		return flagValues;
	}
	
	/** Parses and executes the command.
	 *
	 *  @param caller The object calling this command.
	 *  @param words The command, parameters, and flags that were called - In no particular order.
	 */
	public void execute(Object caller, String[] words) throws Exception
	{	
		Map<String, String> parameters = parseParameters(words);
		Map<String, Boolean> flags = parseFlags(words);
		
		commandFunction.execute(caller, parameters, flags);
	}
}
