// By Iacon1
// Created 12/13/2021
// Makes a much shorter and therefore (hopefully) faster-to-write-and-read json string

package Modules.MektonCore.Adapters;

import java.io.IOException;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import Modules.HexUtilities.HexDirection;
import Modules.MektonCore.MektonHex;
import Modules.MektonCore.Enums.TerrainType;
import Utils.MiscUtils;

public class MektonHexAdapter extends TypeAdapter<MektonHex>
{
	private static int directionDigits() // Number of hexadecimal digits needed to store all the hex directions.
	{
		return HexDirection.values().length / 4; // Each direction is one bit, divide by 4 for 4 bits per digit.
	}
	
	@Override
	public MektonHex read(JsonReader in) throws IOException
	{
		MektonHex value = new MektonHex();
		
		String textValue = in.nextString();
		String[] textValues = textValue.split(";");
		
		int wallNumber = Integer.valueOf(textValues[0], 16); // Hexadecimal to number of walls.
		for (int i = 0; i < directionDigits(); ++i) value.walls.put(HexDirection.values()[i], (wallNumber & (1 << i)) != 0);
		// Cycles through each direction and sets the corresponding wall to the corresponding bit.
		
		value.texturePos.x = Integer.valueOf(textValues[1]);
		value.texturePos.y = Integer.valueOf(textValues[2]);
		value.type = TerrainType.values()[Integer.valueOf(textValues[3])];
		
		return value;
	}

	@Override
	public void write(JsonWriter out, MektonHex value) throws IOException
	{
		
		int wallNumber = 0;
		for (int i = 0; i < directionDigits(); ++i) if (value.walls.get(HexDirection.values()[i])) wallNumber |= 1 << i;
		// Cycles through each direction and sets the corresponding bit to the corresponding wall.
		
		String textValue = MiscUtils.asHex(wallNumber, directionDigits()) + ";" + value.texturePos.x + ";" + value.texturePos.y + ";" + value.type.ordinal();
		
		out.value(textValue);
	}

}