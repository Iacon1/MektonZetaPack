// By Iacon1
// Created 09/13/2021
// Converts from one HexCoord type to another
// TODO moar modular; Maybe register converters?

package Modules.HexUtilities.HexStructures;

import Modules.HexUtilities.HexStructures.Axial.AxialHexCoord;
import Modules.HexUtilities.HexStructures.Axial.AxialHexCoord3D;

public final class HexCoordConverter
{
	/** Converts from one hex coord type to another
	 *  
	 *  @param coord       Coord to convert.
	 *  @param sourceClass Class to convert from.
	 *  @param targetClass Class to convert to.
	 */
	public static <S extends HexCoord, T extends HexCoord> T convert(HexCoord coord, Class<S> sourceClass, Class<T> targetClass)
	{
		// TODO keep up to date
		if (sourceClass == AxialHexCoord.class)
		{
			if (targetClass == AxialHexCoord.class) return (T) coord;
			else if (targetClass == AxialHexCoord3D.class)
				return (T) new AxialHexCoord3D(((AxialHexCoord) coord).q, ((AxialHexCoord) coord).r, 0);
			else return null;
		}
		else if (sourceClass == AxialHexCoord3D.class)
		{
			if (targetClass == AxialHexCoord.class) return (T) coord;
			else if (targetClass == AxialHexCoord3D.class)
				return (T) coord;
			else return null;
		}
		else return null;
	}
}
