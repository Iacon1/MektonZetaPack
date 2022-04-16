// By Iacon1
// Created 11/23/2021
// Signifies that a space-taking entity couldn't fit in a servo's spaces

package Modules.MektonCore.ExceptionTypes;

@SuppressWarnings("serial")
public class DoesntFitException extends Exception
{
	public DoesntFitException(int neededSpace, int emptySpace, int maxSpace)
	{
		super("Cannot fit a " + neededSpace + "-space object into the " + emptySpace + " empty spaces of the " + maxSpace + "-space servo.");
	}
}
