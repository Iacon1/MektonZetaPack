// By Iacon1
// Created 11/16/2022
// Like CameraHolder but takes the entity's hex position into account for Z-axis.

package Modules.HexUtilities;

import GameEngine.EntityTypes.Behaviors.CameraHolder;
import Modules.HexUtilities.HexStructures.Axial.AxialHexCoord3D;

public class Hex3DCameraHolder extends CameraHolder
{

	public Hex3DCameraHolder(CameraType cameraType) {super(cameraType);}
	
	@Override
	public void update()
	{
		super.update();
		
		if (!HexEntity.class.isAssignableFrom(getParent().getClass())) return; // Needs a hex entity
		HexEntity<AxialHexCoord3D> parent = (HexEntity<AxialHexCoord3D>) getParent();
		camera.z = parent.hexPos.z;
	}

}
