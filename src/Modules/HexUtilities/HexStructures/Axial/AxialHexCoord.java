// By Iacon1
// Created 09/12/2021
// Axial coordinates as defined by https://www.redblobgames.com/grids/hexagons/#coordinates-axial

package Modules.HexUtilities.HexStructures.Axial;

import java.util.ArrayList;
import java.util.List;

import GameEngine.Point2D;
import Modules.HexUtilities.HexConfig;
import Modules.HexUtilities.HexDirection;
import Modules.HexUtilities.HexStructures.HexCoord;
import Modules.HexUtilities.HexStructures.HexCoordConverter;
import Utils.MiscUtils;

public class AxialHexCoord implements HexCoord
{
	public int q; // "Column"
	public int r; // "Row"
	public int s() // "Side"?
	{
		return -(q + r);
	}

	public AxialHexCoord(int q, int r)
	{
		this.q = q;
		this.r = r;
	}

	@Override
	public AxialHexCoord rAdd(HexCoord delta)
	{
		AxialHexCoord deltaAxial = HexCoordConverter.convert(delta, delta.getClass(), AxialHexCoord.class);
		return new AxialHexCoord(q + deltaAxial.q, r + deltaAxial.r);
	}
	@Override
	public AxialHexCoord rMultiply(int factor)
	{
		return new AxialHexCoord(this.q * factor, this.r * factor);
	}

	@Override
	public AxialHexCoord getUnitVector(HexDirection dir) // https://www.redblobgames.com/grids/hexagons/#neighbors-axial
	{
		switch (dir)
		{
		case north: return new AxialHexCoord(0, -1);
		case northWest: return new AxialHexCoord(-1, 0);
		case northEast: return new AxialHexCoord(1, -1);
		
		case south: return new AxialHexCoord(0, 1);
		case southWest: return new AxialHexCoord(-1, 1);
		case southEast: return new AxialHexCoord(1, 0);
		default: return null;
		}
	}
	
	@Override
	public HexDirection getDirectionTo(HexCoord target)
	{
		if (target.toPixel().y <= toPixel().y) // North
		{
			if (target.toPixel().x < toPixel().x) return HexDirection.northWest; // West
			else if (target.toPixel().x > toPixel().x) return HexDirection.northEast; // East
			else return HexDirection.north; // North
		}
		else // South
		{
			if (target.toPixel().x < toPixel().x) return HexDirection.southWest; // West
			else if (target.toPixel().x > toPixel().x) return HexDirection.southEast; // East
			else return HexDirection.south; // South
		}
	}
	
	@Override
	public AxialHexCoord getNeighbor(HexDirection dir) // https://www.redblobgames.com/grids/hexagons/#neighbors-axial
	{
		AxialHexCoord delta = getUnitVector(dir);
		return rAdd(delta);
	}
	
	@Override
	public int distance(HexCoord target) // https://www.redblobgames.com/grids/hexagons/#distances-cube
	{	
		AxialHexCoord targetAxial = HexCoordConverter.convert(target, target.getClass(), AxialHexCoord.class);
		int dQ = Math.abs(q - targetAxial.q);
		int dR = Math.abs(r - targetAxial.r);
		int dS = Math.abs(s() - targetAxial.s());
		return MiscUtils.multiMax(dQ, dR, dS);
	}
	
	@Override
	public List<AxialHexCoord> straightLine(HexCoord target) // https://www.redblobgames.com/grids/hexagons/#line-drawing
	{
		AxialHexCoord targetAxial = HexCoordConverter.convert(target, target.getClass(), AxialHexCoord.class);
		List<AxialHexCoord> line = new ArrayList<AxialHexCoord>();
		
		int dist = distance(targetAxial);
		
		float fAQ = (float) q;
		float fAR = (float) r;
		float fBQ = (float) targetAxial.q;
		float fBR = (float) targetAxial.r;
		for (int i = 0; i < dist; ++i)
		{
			float t = 1.0f / ((float) dist) * (float) i;
			int q = (int) MiscUtils.lerp(fAQ, fBQ, t);
			int r = (int) MiscUtils.lerp(fAR, fBR, t);
			
			line.add(new AxialHexCoord(q, r));
		}
		
		return line;
	}
	@Override
	public List<AxialHexCoord> withinDistance(int r) // https://www.redblobgames.com/grids/hexagons/#range
	{
		List<AxialHexCoord> within = new ArrayList<AxialHexCoord>();
		
		for (int i = -r; i <= r; ++i)
			for (int j = Math.max(-r, -(i + r)); j <= Math.min(r, r - i); ++j)
			{
				AxialHexCoord delta = new AxialHexCoord(i, j);
				within.add(rAdd(delta));
			}
		
		return within;
	}
	
	@Override
	public Point2D toPixel() // https://www.redblobgames.com/grids/hexagons/#hex-to-pixel
	{
		Point2D point = new Point2D(0, 0);
		point.x = ((3 * HexConfig.getHexWidth()) / 4 + 1) * q; // Extra q fixes a off-by-one spacing issue
		// x = (3 / 2 * q * size) = (3 * width) / 4 * q
		point.y = (HexConfig.getHexHeight() / 2) * q + HexConfig.getHexHeight() * r;
		// y = size * (sqrt3 / 2 * q + sqrt3 * r) = (height / 2 * q + height * r)

		// Using approximations somehow works better, yay
		return point;
	}

	@Override
	public AxialHexCoord fromPixel(Point2D point)
	{
		AxialHexCoord coord = new AxialHexCoord(0, 0);
		coord.q = Math.floorDiv(point.x, (3 * HexConfig.getHexWidth()) / 4 + 1);
		
		coord.r = Math.floorDiv(point.y - (HexConfig.getHexHeight() / 2) * coord.q, HexConfig.getHexHeight());
		if (point.y >= coord.toPixel().y) coord.r -= 1; // Hack
		
		return coord;
	}
}