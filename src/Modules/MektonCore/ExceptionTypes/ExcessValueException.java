// By Iacon1
// Created 11/23/2021
// Tried to give too much health

package Modules.MektonCore.ExceptionTypes;

import Modules.MektonCore.StatsStuff.ScaledUnits.ScaledValue;

@SuppressWarnings("serial")
public class ExcessValueException extends Exception
{
	public ExcessValueException(ScaledValue<?> attemptedValue, ScaledValue<?> maxValue, String valueName)
	{
		super("Tried to give " + attemptedValue.getValue() + " " + valueName + " to a servo with " + maxValue.getValue() + " maximum " + valueName + ".");
	}
}
