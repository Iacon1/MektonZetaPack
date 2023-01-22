// By Iacon1
// Created 05/31/2021
// Module file

package Modules.TestModule;

import GameEngine.Graphics.Camera;
import GameEngine.Graphics.ScreenCanvas;
import GameEngine.Graphics.UtilCanvas;

import java.util.List;

import javax.swing.JPanel;

import GameEngine.GameInfo;
import GameEngine.IntPoint2D;
import GameEngine.Configurables.ConfigManager;
import GameEngine.Configurables.ModuleTypes.EditorPopulatingModule;
import GameEngine.Configurables.ModuleTypes.GraphicsHandlerModule;
import GameEngine.Configurables.ModuleTypes.Module;
import GameEngine.Configurables.ModuleTypes.PlayerHandlerModule;
import GameEngine.Configurables.ModuleTypes.ServerMakingModule;
import GameEngine.Configurables.ModuleTypes.WorldMakingModule;
import GameEngine.Editor.EditorPanel;
import GameEngine.Configurables.ModuleTypes.Module.ModuleConfig;
import GameEngine.EntityTypes.GameEntity;
import GameEngine.EntityTypes.Behaviors.CameraHolder;
import GameEngine.Server.Account;
import GameEngine.Server.BaseServer;
import GameEngine.Server.GameServer;
import Modules.HexUtilities.HexConfig;
import Modules.HexUtilities.HexEntity;
import Modules.HexUtilities.HexStructures.Axial.AxialHexCoord3D;
import Modules.MektonCore.MektonActorSprite;
import Modules.MektonCore.MektonHex;
import Modules.MektonCore.EntityTypes.MektonMap;
import Modules.MektonCore.Enums.ArmorType;
import Modules.MektonCore.Enums.EnvironmentType;
import Modules.MektonCore.Enums.LevelRAM;
import Modules.MektonCore.Enums.Scale;
import Modules.MektonCore.Enums.ServoClass;
import Modules.MektonCore.StatsStuff.HitLocation.ServoType;
import Modules.MektonCore.StatsStuff.SheetTypes.MekSheet;
import Modules.MektonCore.StatsStuff.SystemTypes.AdditiveSystems.Servos.MekServo;
import Utils.Logging;

public class TestModule implements Module, WorldMakingModule, ServerMakingModule, GraphicsHandlerModule, PlayerHandlerModule, EditorPopulatingModule
{
	@Override
	public ModuleConfig getModuleConfig()
	{
		ModuleConfig config = new ModuleConfig();
		config.moduleName = "Test Module";
		config.moduleVersion = "V0.X";
		config.moduleDescription = "A testing rig.";
		
		return config;
	}

	@Override
	public void initModule()
	{
	}

	@Override
	public GameServer makeServer()
	{
		BaseServer server = new BaseServer();
		return server;
	}
	
	@Override
	public void newWorld()
	{
		GameInfo.setWorld(new GameInfo.GameWorld());
		MektonMap<MektonHex> map = new MektonMap<MektonHex>("Tilesets/DummyTileset", "/Tilesets/ZFog", EnvironmentType.none);
		map.setDimensions(18, 9, 1, new MektonHex());
	}
	@Override
	public void loadWorld(String server)
	{
		return;
	}

	@Override
	public void drawWorld(GameEntity viewer, ScreenCanvas canvas)
	{
		try
		{
			if (GameInfo.getWorld() == null) return;
			Camera view = viewer.getBehavior(CameraHolder.class).getCamera();
			GameInfo.getWorld().getRootEntities().get(0).render(canvas, view);
		}
		catch (Exception e) {Logging.logException(e);}
	}

	@Override
	public GameEntity makePlayer(Account account)
	{
		account.setLogged(true);
		DummyPlayer player = new DummyPlayer((MektonMap) GameInfo.getWorld().getRootEntities().get(0)); // Adds a guy to the map

		account.possess(player);
		GameInfo.getWorld().getRootEntities().get(0).addChild(player);
		player.setHexPos(new AxialHexCoord3D(2, 2, 0));
		
		return player;
	}
	@Override
	public GameEntity wakePlayer(Account account)
	{
		account.setLogged(true);
		if (account.hasPossessee()) return null;
		else return makePlayer(account);
	}
	@Override
	public GameEntity sleepPlayer(Account account)
	{
		account.setLogged(false);
		GameInfo.getWorld().removeEntity(account.getPossessee(), true);
		return null;
	}
	@Override
	public GameEntity deletePlayer(Account account)
	{
		account.setLogged(false);
		GameInfo.getWorld().removeEntity(account.getPossessee(), true);
		return null;
	}

	@Override
	public void populateEntityTypes(List<Class<? extends GameEntity>> typeList) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void populateTabs(List<JPanel> panels)
	{
		EditorPanel panel = new MekSheet().editorPanel();
		panel.setSize(640, 480);
		panel.validate();
		panels.add(panel);
		MektonActorSprite s = new MektonActorSprite("set1");
		s.setBasicParams(0, 0, HexConfig.getHexWidth(), HexConfig.getHexHeight());
		EditorPanel newPanel = s.editorPanel();
		panels.add(newPanel);
	}

	@Override
	public Account newAccount()
	{
		return new TestAccount();
	}	
}
