// By Iacon1
// Created 09/12/2021
// Axial coordinates plus 3D

package Modules.HexUtilities.HexStructures.Axial;

import GameEngine.Point2D;
import Modules.HexUtilities.HexDirection;
import Modules.HexUtilities.HexStructures.HexCoord;
import Modules.HexUtilities.HexStructures.HexCoordConverter;

public class AxialHexCoord3D extends AxialHexCoord
	{
		private AxialHexCoord3D convertAxial(AxialHexCoord coord, int z)
		{
			AxialHexCoord3D coord3D = HexCoordConverter.convert(coord, AxialHexCoord.class, AxialHexCoord3D.class);
			coord3D.z = z;
			return coord3D;
		}
		public int z;
		public AxialHexCoord3D(int q, int r, int z)
		{
			super(q, r);
			this.z = z;
		}

		@Override
		public AxialHexCoord3D rAdd(HexCoord delta)
		{
			AxialHexCoord3D delta3D = HexCoordConverter.convert(delta, delta.getClass(), AxialHexCoord3D.class);
			return convertAxial(super.rAdd(delta), z + delta3D.z);
		}

		@Override
		public AxialHexCoord3D rMultiply(int factor)
		{
			return convertAxial(super.rMultiply(factor), z * factor);
		}

		@Override
		public AxialHexCoord3D getUnitVector(HexDirection dir)
		{
			switch (dir)
			{
			case up: return new AxialHexCoord3D(0, 0, 1);
			case down: return new AxialHexCoord3D(0, 0, -1);
				
			default: return convertAxial(super.getUnitVector(dir), 0);
			}
		}
		
		@Override
		public HexDirection getDirectionTo(HexCoord target)
		{
			AxialHexCoord3D target3D = HexCoordConverter.convert(target, target.getClass(), AxialHexCoord3D.class);
			if (target3D.z < z) return HexDirection.down;
			else if (target3D.z > z) return HexDirection.up;
			else return super.getDirectionTo(target);
		}
		
		@Override
		public AxialHexCoord3D getNeighbor(HexDirection dir)
		{
			switch (dir)
			{
			case up: return new AxialHexCoord3D(q, r, z + 1);
			case down: return new AxialHexCoord3D(q, r, z - 1);
				
			default: return convertAxial(super.getNeighbor(dir), z);
			}	
		}

		@Override
		public int distance(HexCoord target)
		{
			AxialHexCoord3D target3D = HexCoordConverter.convert(target, target.getClass(), AxialHexCoord3D.class);
			return super.distance(target) + z - target3D.z;
		}

		/*@Override
		public ArrayList<AxialHexCoord3D> straightLine(HexCoord target)
		{
			// TODO Auto-generated method stub
			return null;
		}*/

		/*@Override
		public ArrayList<AxialHexCoord3D> withinDistance(int r)
		{
			// TODO Auto-generated method stub
			return null;
		}*/
		
		@Override
		public AxialHexCoord3D fromPixel(Point2D point)
		{
			return convertAxial(super.fromPixel(point), 0);
		}
		
		@Override
		public boolean equals(Object obj)
		{
			if (obj.getClass() != AxialHexCoord3D.class) return false;
			else
			{
				AxialHexCoord3D coord = (AxialHexCoord3D) obj;
				return q == coord.q && r == coord.r && z == coord.z;
			}
		}
		@Override
		public int hashCode()
		{
			return new String(q + " " + r + " " + z).hashCode();
		}
	}