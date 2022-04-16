// By Iacon1
// Created 11/17/2021
// When a command parameter doesn't exist or is invalid

package Modules.GenUtils.Commands;

@SuppressWarnings("serial")
public class InvalidParameterException extends Exception
{
	public InvalidParameterException(String message)
	{
		super(message);
	}
	
	/** Constructor. Message is: [parameter] can't have value [value].
	 *  
	 *  @param parameter The parameter that is invalid.
	 *  @param value The invalid value it has.
	 */
	public InvalidParameterException(String parameter, String value)
	{
		super(parameter + " can't have value " + value + ".");
	}
}
