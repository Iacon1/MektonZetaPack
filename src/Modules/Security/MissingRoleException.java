// By Iacon1
// Created 11/17/2021
//

package Modules.Security;

import java.util.ArrayList;
import java.util.List;

import Utils.MiscUtils;

@SuppressWarnings("serial")
public class MissingRoleException extends Exception
{	
	private static String roleSetsToString(String commandName, String[][] roleSets)
	{
		List<String> roleSetStrings = new ArrayList<String>();
		for (int i = 0; i < roleSets.length; ++i) roleSetStrings.add(MiscUtils.arrayToString(roleSets[i], ", "));
		
		return "Needs one of the following rolesets to execute " + commandName + ": ("  + MiscUtils.arrayToString(roleSetStrings, "), (") + ").";
	}
	
	public MissingRoleException(String message)
	{
		super(message);
	}
	
	public MissingRoleException(String commandName, String[][] roleSets)
	{
		super(roleSetsToString(commandName, roleSets));
	}

	
}
