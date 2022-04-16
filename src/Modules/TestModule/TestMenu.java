// By Iacon1
// Created 09/17/2021
// A test

package Modules.TestModule;

import GameEngine.GameInfo;
import GameEngine.ImageSprite;
import GameEngine.EntityTypes.GUITypes.GUISpriteEntity;
import Modules.HexUtilities.HexConfig;

public class TestMenu extends GUISpriteEntity
{
	public TestMenu()
	{
		super();
		setSprite(new ImageSprite(GameInfo.getServerPackResource("DummyPlayer.PNG")));
		setSpriteParams(0, 0, HexConfig.getHexWidth(), 2 * HexConfig.getHexHeight());
		align(AlignmentPoint.northEast, null, AlignmentPoint.northEast);
	}
	
	@Override
	public void onStart()
	{
		// TODO Auto-generated method stub

	}
	@Override
	public void onStop()
	{
		// TODO Auto-generated method stub

	}
	@Override
	public void onAnimStop()
	{
		// TODO Auto-generated method stub

	}
	@Override
	public String getName()
	{
		// TODO Auto-generated method stub
		return null;
	}


}
