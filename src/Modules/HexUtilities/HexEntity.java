// By Iacon1
// Created 09/10/2021
//

package Modules.HexUtilities;

import java.util.LinkedList;

import GameEngine.EntityTypes.SpriteEntity;
import Modules.HexUtilities.HexStructures.HexCoord;

public abstract class HexEntity<T extends HexCoord> extends SpriteEntity // T is coordinate type
{
	protected T hexPos;
	private T targetHexPos;
	private HexDirection facing;
	
	private boolean paused = false;
	protected LinkedList<T> path; // Important that it is a LinkedList
	protected int baseSpeed;
	
	public HexEntity()
	{
		super();
		path = new LinkedList<T>();
	}

	private void alignCoords()
	{
		this.setPos(hexPos.toPixel());
	}
	
	public T getHexPos()
	{
		return hexPos;
	}
	public void setHexPos(T pos)
	{
		hexPos = pos;
		alignCoords();
	}
	
	/**
	* Move to a (2D) hex coord at a set speed.
	* <p>
	*
	* @param  target Target position. (in hexes)
	* @param  speed  Speed to move at. (in pixels!)
	*/
	public void moveTargetHex(T target, int speed)
	{
		targetHexPos = target;
		
		setDirection(this.hexPos.getDirectionTo(targetHexPos));
		
		float speedFactor = 1f;
		switch (facing)
		{
		case northWest: case northEast: case southWest: case southEast:
			speedFactor = 5f / 3f; break; // No math basis, just a rough adjustment to fix a mysterious speed difference between diagonal and straight
		default: break;
		}
		baseSpeed = speed;
		moveTargetSpeed(target.toPixel(), (int) (speed * speedFactor));
	}
	/**
	* Move in 2D hexes at a set speed.
	* <p>
	*
	* @param  hX    How far right (or left, if negative) to move.
	* @param  hY    How far down (or up, if negative) to move
	* @param  speed Speed to move at. (in pixels!)
	*/
	public void moveDeltaHex(T delta, int speed)
	{
		moveTargetHex(hexPos.rAdd(delta), speed);
	}

	public void moveDirectional(HexDirection dir, int distance, int speed)
	{
		T delta = hexPos.getUnitVector(dir).rMultiply(distance);
		setDirection(dir);
		
		moveDeltaHex(delta, speed);
	}
	
	public boolean isPresentAt(T pos)
	{
		return hexPos == pos;
	}

	public void setDirection(HexDirection dir)
	{
		int mult = 0;
		switch (dir)
		{
		case north: mult = 3; break;
		case northWest: mult = 4; break;
		case northEast: mult = 2; break;
	
		case south: mult = 0; break;
		case southWest: mult = 5; break;
		case southEast: mult = 1; break;
		default: return;
		}
		
		setSpriteParams(sprite.getSize().x * mult, null, null, null);
		facing = dir;
	}
	
	/** Called when motion paused.*/
	public abstract void onPause();
	/** Called when motion unpaused.*/
	public abstract void onResume();
	
	public void pause()
	{
		paused = true;
		onPause();
	}
	
	public void resume()
	{
		paused = false;
		onResume();
	}
	
	
	public void movePath(LinkedList<T> path, int speed)
	{
		this.path = path;
		moveTargetHex(path.getFirst(), speed);
	}
	/** Sets course to the next coord in the path. */
	public void updatePath()
	{
		if (this.path != null && this.hexPos == this.path.getFirst()) // Ready for next step of path
		{
			this.path.remove();
			if (this.path.isEmpty()) this.path = null;
			else moveTargetHex(path.getFirst(), baseSpeed);
		}
		else if (this.path != null)
		{
			moveTargetHex(path.getFirst(), baseSpeed);
		}
	}
	@Override
	public void onStop()
	{
		hexPos = targetHexPos;
		if (!paused) updatePath();
	}
}
