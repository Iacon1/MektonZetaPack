// By Iacon1
// Created 12/13/2021
//

package Modules.MektonCore.Adapters;

import java.io.IOException;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import Modules.MektonCore.MektonHex;
import Modules.MektonCore.Enums.TerrainType;

public class MektonHexAdapter extends TypeAdapter<MektonHex>
{

	@Override
	public MektonHex read(JsonReader in) throws IOException
	{
		MektonHex value = new MektonHex();
		
		String textValue = in.nextString();
		String[] textValues = textValue.split(";");
		
		value.texturePos.x = Integer.valueOf(textValues[0]);
		value.texturePos.y = Integer.valueOf(textValues[1]);
		value.type = TerrainType.values()[Integer.valueOf(textValues[2])];
		
		return value;
	}

	@Override
	public void write(JsonWriter out, MektonHex value) throws IOException
	{
		String textValue = value.texturePos.x + ";" + value.texturePos.y + ";" + value.type.ordinal();
		
		out.value(textValue);
	}

}