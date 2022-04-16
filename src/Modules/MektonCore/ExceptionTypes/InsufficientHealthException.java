// By Iacon1
// Created 11/23/2021
// Tried to give too much health

package Modules.MektonCore.ExceptionTypes;

import Modules.MektonCore.StatsStuff.ScaledUnits.ScaledHitValue;

@SuppressWarnings("serial")
public class InsufficientHealthException extends Exception
{
	public InsufficientHealthException(ScaledHitValue attemptedValue, ScaledHitValue maxValue)
	{
		super("Tried to sacrifice " + attemptedValue + " health in a servo with " + maxValue + " base maximum health.");
	}
}
