// By Iacon1
// Created 04/26/2021
//

package Modules.TestModule;

import Modules.HexUtilities.Hex3DCameraHolder;
import Modules.HexUtilities.HexConfig;
import Modules.HexUtilities.HexStructures.Axial.AxialHexCoord3D;
import Modules.MektonCore.EntityTypes.Human;
import Modules.MektonCore.EntityTypes.MektonActor;
import Modules.MektonCore.EntityTypes.MektonMap;
import Modules.MektonCore.StatsStuff.HitLocation;
import Modules.MektonCore.StatsStuff.DamageTypes.Damage;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import GameEngine.EntityToken;
import GameEngine.GameInfo;
import GameEngine.IntPoint2D;
import GameEngine.Graphics.SingleSprite;
import GameEngine.Graphics.Camera;
import GameEngine.Graphics.ScreenCanvas;
import GameEngine.EntityTypes.CommandRunner;
import GameEngine.EntityTypes.InputHandler;
import GameEngine.EntityTypes.Behaviors.CameraHolder;
import GameEngine.EntityTypes.GUITypes.ChatBox;
import Utils.MiscUtils;

public class DummyPlayer extends Human implements InputHandler, CommandRunner
{	
	EntityToken<ChatBox> chatBox;
	
	public DummyPlayer()
	{
		super();
		setSprite(new SingleSprite("DummyPlayer"));
		setSpriteParams(0, 0, HexConfig.getHexWidth(), 2 * HexConfig.getHexHeight());
		setBounds(HexConfig.getHexWidth(), HexConfig.getHexHeight(), 0, -HexConfig.getHexHeight());
		
		addBehavior(new Hex3DCameraHolder(CameraHolder.CameraType.centered));
	}
	public DummyPlayer(MektonMap map)
	{
		super(map);
		setSprite(new SingleSprite("DummyPlayer"));
		setSpriteParams(0, 0, HexConfig.getHexWidth(), 2 * HexConfig.getHexHeight());
		setBounds(HexConfig.getHexWidth(), HexConfig.getHexHeight(), 0, -HexConfig.getHexHeight());
		
		ChatBox chatBox = new ChatBox("MicrogrammaNormalFix", Color.red, 20);
		chatBox.setOwnerID(0); // TODO find value of possessor dynamically
		addChild(chatBox);
		this.chatBox = new EntityToken<ChatBox>(chatBox.getId());
		addChild(chatBox);
		
		addBehavior(new Hex3DCameraHolder(CameraHolder.CameraType.centered));
	}
	
	@Override
	public String getName()
	{
		return "Dummy Player";
	}
	
	@Override
	public void onAnimStop() {}

	@Override
	public void onStart()
	{
	}
	@Override
	public void onPause()
	{
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void render(ScreenCanvas canvas, Camera camera)
	{
		super.render(canvas, camera);
		
		if (true)//isPossessee())
		{
			String text =
					"Action points: " + MiscUtils.floatPrecise((float) remainingActions(), 2) + "\n" +
					statSummary();
			
			int fontSize = 20;
			IntPoint2D textSize = canvas.textSize(text, "MicrogrammaNormalFix", fontSize);
//			canvas.addRectangle(Color.black, new IntPoint2D(0, 0), textSize);
			canvas.addText(text, "MicrogrammaNormalFix", Color.red, new IntPoint2D(0, 0), fontSize);
		}
		//chatBox.get().render(canvas, camera);
	}
	@Override
	public void defend(MektonActor aggressor, HitLocation location) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void attack(MektonActor defender, HitLocation location) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void takeDamage(Damage damage) {
		// TODO Auto-generated method stub
		
	}
	
	
	
	@Override
	public void handleMouse(int userID, MouseEvent event)
	{
		if (event.getID() == MouseEvent.MOUSE_PRESSED)
		{
			AxialHexCoord3D point = mapToken.get().fromPixel(new IntPoint2D(event.getX(), event.getY()), getBehavior(CameraHolder.class).getCamera());
			if (event.getButton() == MouseEvent.BUTTON1) GameInfo.getServer().runCommand(userID, "move -q " + point.q + " -r " + point.r);
		}
	}
	@Override
	public void handleKeyboard(int userID, KeyEvent event)
	{
		if (chatBox.get().isSelected()) return;
		if (event.getID() != KeyEvent.KEY_RELEASED)
		{
			switch (event.getKeyCode())
			{
			case KeyEvent.VK_Q: GameInfo.getServer().runCommand(userID, "move -n -w"); break;
			case KeyEvent.VK_E: GameInfo.getServer().runCommand(userID, "move -n -e"); break;
			case KeyEvent.VK_W: GameInfo.getServer().runCommand(userID, "move -n"); break;
			case KeyEvent.VK_A: GameInfo.getServer().runCommand(userID, "move -s -w"); break;
			case KeyEvent.VK_D: GameInfo.getServer().runCommand(userID, "move -s -e"); break;
			case KeyEvent.VK_S: GameInfo.getServer().runCommand(userID, "move -s"); break;
			case KeyEvent.VK_SPACE: GameInfo.getServer().runCommand(userID, "move -u"); break;
			case KeyEvent.VK_SHIFT: GameInfo.getServer().runCommand(userID, "move -d"); break;
			}
		}
		
	}
}
