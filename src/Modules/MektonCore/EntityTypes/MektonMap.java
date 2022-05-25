// By
// Iacon1
// Created 04/25/2021

/* Notes:
 * If you are at z = 1 then you are *above* on the tiles at z = 0, not *in* them
 * Level 0 should be a ground level, then; Comprised entirely of ground tiles that you cannot walk through
 *
 * Make a hex null to make it air/vacuum
 */
package Modules.MektonCore.EntityTypes;

import java.awt.Color;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Supplier;

import com.google.gson.reflect.TypeToken;

import GameEngine.ScreenCanvas;
import GameEngine.IntPoint2D;
import GameEngine.Configurables.ConfigManager;
import GameEngine.Configurables.ModuleManager;
import GameEngine.EntityTypes.GameEntity;
import GameEngine.EntityTypes.SpriteEntity;
import GameEngine.Managers.GraphicsManager;
import Modules.HexUtilities.HexConfig;
import Modules.HexUtilities.HexDirection;
import Modules.HexUtilities.HexEntity;
import Modules.HexUtilities.HexStructures.HexMap;
import Modules.HexUtilities.HexStructures.Axial.AxialHex3DMap;
import Modules.HexUtilities.HexStructures.Axial.AxialHexCoord;
import Modules.HexUtilities.HexStructures.Axial.AxialHexCoord3D;
import Modules.HexUtilities.HexStructures.Axial.AxialHexMapRectangle;
import Modules.MektonCore.MektonHex;
import Modules.MektonCore.Enums.EnvironmentType;
import Modules.Pathfinding.AStar;
import Modules.Pathfinding.PathfindingAdapter;
import Utils.JSONManager;
import Utils.Logging;
import Utils.MiscUtils;
import Utils.GSONConfig.TransSerializables.TransSerializable;

public class MektonMap<T extends MektonHex> extends GameEntity implements HexMap<AxialHexCoord3D, T>, TransSerializable
{	
	// Workaround for some genericing issues; This is the type our internal map uses.
	private static class InternalMap<T extends MektonHex> extends AxialHex3DMap<AxialHexMapRectangle<T>, T>
	{
		public InternalMap(Supplier<AxialHexMapRectangle<T>> supplier) {super(supplier);}
	}; 
	
	private String tileset; // Tileset
	private String zFog; // A translucent image the same size as the screen that renders between Z-levels
	
	private transient InternalMap<T> map;
	private String hexClass;	// The name of the class of hex we're using - that is, the name of T.
	private String serializedMap; // The serialized version of the map, to be serialized later.
	
	
	private EnvironmentType environmentType;
	
	private int hexCost(AxialHexCoord3D a, AxialHexCoord3D b) // Cost of coord
	{
		HexDirection dirA = a.getDirectionTo(b); // Direction from a to b
		HexDirection dirB = b.getDirectionTo(a); // Direction from b to a
		if (getHex(a).walls.get(dirA) || getHex(b).walls.get(dirB)) // Walls in the way
			return -1;
		return getHex(b).getCost();
	}
	private int hexDist(AxialHexCoord3D a, AxialHexCoord3D b) // Cost of coord
	{
		return a.distance(b);
	}
	private List<AxialHexCoord3D> neighbors(AxialHexCoord3D coord) // Cost of coord
	{
		List<AxialHexCoord3D> list = new ArrayList<AxialHexCoord3D>();
		list.add(coord.getNeighbor(HexDirection.north));
		list.add(coord.getNeighbor(HexDirection.northWest));
		list.add(coord.getNeighbor(HexDirection.northEast));

		list.add(coord.getNeighbor(HexDirection.south));
		list.add(coord.getNeighbor(HexDirection.southWest));
		list.add(coord.getNeighbor(HexDirection.southEast));
		
		return list;
	}
	private transient PathfindingAdapter<AxialHexCoord3D, AStar> pathfinder;
	
	public MektonMap(String tileset, String zFog, EnvironmentType environmentType)
	{
		super();
		Supplier<AxialHexMapRectangle<T>> supplier = () -> {return new AxialHexMapRectangle<T>();};
		map = new InternalMap<T>(supplier);
		
		this.tileset = tileset;
		this.zFog = zFog;
		this.environmentType = environmentType;
		
		pathfinder = new PathfindingAdapter<AxialHexCoord3D, AStar>(
				(AxialHexCoord3D a, AxialHexCoord3D b) -> hexCost(a, b),
				(AxialHexCoord3D a, AxialHexCoord3D b) -> hexDist(a, b),
				(AxialHexCoord3D coord) -> neighbors(coord),
				() -> new AStar());
	}
	public MektonMap()
	{
		super();
		Supplier<AxialHexMapRectangle<T>> supplier = () -> {return new AxialHexMapRectangle<T>();};
		map = new InternalMap<T>(supplier);
		
		tileset = null;
		zFog = null;
		hexClass = null;
		serializedMap = null;
		environmentType = null;
		
		pathfinder = new PathfindingAdapter<AxialHexCoord3D, AStar>(
				(AxialHexCoord3D a, AxialHexCoord3D b) -> hexCost(a, b),
				(AxialHexCoord3D a, AxialHexCoord3D b) -> hexDist(a, b),
				(AxialHexCoord3D coord) -> neighbors(coord),
				() -> new AStar());
	}
	
	/** Sets the width, length, and height of the map; Clears the map!
	 * 
	 * @param columns    Length in q-axis.
	 * @param rows       Length in r-axis.
	 * @param levels     Length in z-axis.
	 * @param defaultHex Default hex data.
	 */
	public void setDimensions(int columns, int rows, int levels, T defaultHex) // Sets new dimensions for map
	{
		map.setDimensions(columns, rows, levels);
		for (int k = 0; k < levels; ++k)
			for (int i = 0; i < columns; ++i)
				for (int j = map.firstRow(i); j <= map.lastRow(i); ++j)
					setHex(new AxialHexCoord3D(i, j, k), defaultHex);
	}
	/** Sets the width, length, and height of the map; Clears the map!
	 * 
	 * @param columns    Length in q-axis.
	 * @param rows       Length in r-axis.
	 * @param levels     Length in z-axis.
	 */
	public void setDimensions(int columns, int rows, int levels) // Sets new dimensions for map
	{
		setDimensions(columns, rows, levels, null);
	}
	
	/** Returns the optimal path from a to b
	 * 
	 *  @param a First coordinate.
	 *  @param b Second coordinate.
	 *  
	 *  @return Path.
	 */
	public LinkedList<AxialHexCoord3D> pathfind(AxialHexCoord3D a, AxialHexCoord3D b)
	{
		if (a.z != b.z) return null;
		
		List<AxialHexCoord3D> layer = new ArrayList<AxialHexCoord3D>();
		
		for (int i = 0; i < map.getColumns(); ++i) // Generate the list of hex coords
		{
			for (int j = map.firstRow(i); j <= map.lastRow(i); ++j)
			{
				layer.add(new AxialHexCoord3D(i, j, a.z));
			}
		}

		return pathfinder.findOptimalPath(layer, a, b);
	}
	
	public boolean inBounds(AxialHexCoord3D coord)
	{
		return map.inBounds(coord);
	}
	public void setHex(AxialHexCoord3D coord, T defaultHex)
	{
		map.setHex(coord, defaultHex);
	}
	public T getHex(AxialHexCoord3D coord)
	{
		return map.getHex(coord);
	}
	public EnvironmentType getEnvironmentType()
	{
		return environmentType;
	}
	/** Converts a hex coord to a *screen* coordinate, i. e. accounting for camera pos.
	 * 
	 * @param coord Coordinate to convert.
	 * @param camera Camera coordinates.
	 * 
	 * @return Corresponding screen coordinate.
	 */
	public IntPoint2D toPixel(AxialHexCoord3D coord, IntPoint2D camera)
	{
		return coord.toPixel().subtract(camera);
	}
	/** Converts a hex coord to a *screen* coordinate, i. e. accounting for last camera pos.
	 * 
	 * @param coord Coordinate to convert.
	 * 
	 * @return Corresponding screen coordinate.
	 */
	public IntPoint2D toPixel(AxialHexCoord3D coord)
	{
		return toPixel(coord, ScreenCanvas.getCamera());
	}
	/** Converts a screen coord to a hex coordinate, accounting for camera pos.
	 * 
	 * @param coord Coordinate to convert.
	 * @param camera Camera coordinates.
	 * 
	 * @return Corresponding hex coordinate.
	 */
	public AxialHexCoord3D fromPixel(IntPoint2D coord, IntPoint2D camera)
	{
		return new AxialHexCoord3D(0, 0, 0).fromPixel(coord.add(camera));
	}
	/** Converts a screen coord to a hex coordinate, accounting for last camera pos.
	 * 
	 * @param coord Coordinate to convert.
	 * @param camera Camera coordinates.
	 * 
	 * @return Corresponding hex coordinate.
	 */
	public AxialHexCoord3D fromPixel(IntPoint2D coord)
	{
		return fromPixel(coord, ScreenCanvas.getCamera());
	}
	
	/** Finds the highest hex at a position below a certain threshold, if one exists, and
	 *  returns its z-value (or -1 if none found).
	 * 
	 *  @param coord Coordinate to look at.
	 *  @param zMax  Maximum altitude to check.
	 */
	public int findHighestHex(AxialHexCoord coord, int zMax) // O(z); TODO memoize
	{
		if (zMax >= map.getLevels()) zMax = map.getLevels() - 1;
		if (!map.inBounds(new AxialHexCoord3D(coord.q, coord.r, zMax))) return -1;
		
		for (int k = zMax; k >= 0; --k)
		{
			AxialHexCoord3D coord3D = new AxialHexCoord3D(coord.q, coord.r, k);
			if (map.getHex(coord3D) != null) return k;
		}
		
		return -1;
	}
	
	public SpriteEntity findEntity(AxialHexCoord3D coord) // returns a game instance at that position if available
	{
		for (int i = 0; i < childrenIds.size(); ++i)
		{
			HexEntity obj = (HexEntity) getChild(i); // Please only put physical objects in here
			if (obj.getHexPos().equals(coord)) return obj;
		}
		
		return null;
	}
	
	@Override
	public String getName()
	{
		return "Map"; // TODO
	}
	@Override
	public void update() {}
	
	private void drawZFog(ScreenCanvas canvas)
	{
		canvas.drawImage(zFog, new IntPoint2D(0, 0), new IntPoint2D(0, 0), new IntPoint2D(ConfigManager.getScreenWidth(), ConfigManager.getScreenHeight()));
	}
	private void drawHexes(ScreenCanvas canvas, IntPoint2D camera, int k, int cameraZ)
	{
		if (k >= map.getLevels()) return; // Cannot draw hexes above this
		// TODO optimization using BakingCanvas
		camera = camera.add(new IntPoint2D(0, (k - cameraZ) * HexConfig.getHexHeight()));
		for (int i = 0; i < map.getColumns(); ++i) // columns
			for (int j = map.firstRow(i); j <= map.lastRow(i); ++j)
			{
				if (k != findHighestHex(new AxialHexCoord(i, j), cameraZ)) continue;
				
				AxialHexCoord3D hexCoord = new AxialHexCoord3D(i, j, k);
				MektonHex hex = getHex(hexCoord);
				canvas.drawImage(tileset, toPixel(hexCoord, camera), new IntPoint2D(
						hex.texturePos.x * HexConfig.getHexWidth(), 
						hex.texturePos.y * HexConfig.getHexHeight()),
						new IntPoint2D(HexConfig.getHexWidth(), HexConfig.getHexHeight()));
				canvas.drawText(hexCoord.q + ", " + hexCoord.r, GraphicsManager.getFont("MicrogrammaNormalFix"), Color.white, toPixel(hexCoord, camera), 16);
			}
	}
	private void drawChildren(ScreenCanvas canvas, IntPoint2D camera, int k, int cameraZ)
	{
		camera = camera.add(new IntPoint2D(0, (k - cameraZ) * HexConfig.getHexHeight()));
		for (int t = 0; t < getChildren().size(); ++t) // O(w)
		{
			HexEntity<AxialHexCoord3D> entity = (HexEntity<AxialHexCoord3D>) getChildren().get(t);
			AxialHexCoord3D pos3D = (AxialHexCoord3D) entity.getHexPos();
			if (pos3D.z == k) entity.render(canvas, camera);
		}
	}
	public void render(ScreenCanvas canvas, IntPoint2D camera, int z)
	{
		if (map == null || map.getColumns() == 0 || map.getRows() == 0 || map.getLevels() == 0) return;
		
		for (int k = 0; k <= z; ++k) // O(1 + n * n^2 + n * w) = O(n^3)
		{
			if (k < z - 1) drawZFog(canvas); // O(1)
			
			drawHexes(canvas, camera, k, z); // O(n^2)
			
			drawChildren(canvas, camera, k, z); // O(w)
		}
	}
	@Override
	public void render(ScreenCanvas canvas, IntPoint2D camera) {render(canvas, camera, 0);}
	@Override
	public void preSerialize()
	{
		serializedMap = JSONManager.serializeJSON(map);
		hexClass = MiscUtils.ClassToString(map.getHex(new AxialHexCoord3D(0, 0, 0)).getClass());
	}
	@Override
	public void postDeserialize()
	{
		Class<T> hexClass = null;
		try {hexClass = (Class<T>) ModuleManager.getLoader().loadClass(this.hexClass);} // Convert string back into class.
		catch (Exception e) {Logging.logException(e); return;}
		
		map = (InternalMap<T>) JSONManager.deserializeCollectionJSONList(serializedMap, InternalMap.class, hexClass);
		
		serializedMap = null;
		this.hexClass = null;
	}
}
