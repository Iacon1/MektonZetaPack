// By Iacon1
// Created 09/12/2021
// An actor is an entity with standard Mekton actions like shooting and moving and stuff

package Modules.MektonCore.EntityTypes;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import Utils.Logging;
import Utils.MiscUtils;
import Utils.SimpleTimer;

import GameEngine.Animation;
import GameEngine.EntityTypes.CommandRunner;
import Modules.GenUtils.Commands.InvalidParameterException;
import Modules.GenUtils.Commands.ParsingCommand;
import Modules.GenUtils.Commands.ParsingCommandBank;
import Modules.HexUtilities.HexDirection;
import Modules.HexUtilities.HexStructures.Axial.AxialHexCoord3D;
import Modules.MektonCore.MektonUtil.Rolls;
import Modules.MektonCore.StatsStuff.AdditiveSystemList;
import Modules.MektonCore.StatsStuff.HitLocation;
import Modules.MektonCore.StatsStuff.DamageTypes.Damage;
import Modules.Security.RoleAccount;
import Modules.Security.RoleHolder;

public abstract class MektonActor extends MapEntity implements CommandRunner, RoleHolder
{
	
	
	private double actionPoints;
	private transient SimpleTimer actionTimer;
	private transient ParsingCommandBank commandBank;
	
	protected enum ActorAnim // Animations
	{
		idle,
		
		walk,
		fly,
		
		attackH2H,
		attackSword,
		attackEMW,
		attackMissile,
		attackGun,
		attackBeam,
		
		blockStandard,
		blockActive,
		blockReactive;
		
		public Animation animation;
	}
	// Commands
	private void moveFunction(Object caller, Map<String, String> parameters, Map<String, Boolean> flags)
	{
		if (caller != this) return; // The only thing that should run *our* moveFunction is *us*
			
		if (parameters.get("q") != null && parameters.get("r") != null)
		{
			AxialHexCoord3D target = new AxialHexCoord3D(Integer.valueOf(parameters.get("q")), Integer.valueOf(parameters.get("r")), hexPos.z);
			movePath(mapToken.get().pathfind(hexPos, target), 2);
		}
		else
		{
			if (flags.get("north") && flags.get("west")) moveDirectionalAct(HexDirection.northWest, 1, 2);
			else if (flags.get("north") && flags.get("east")) moveDirectionalAct(HexDirection.northEast, 1, 2);
			else if (flags.get("north")) moveDirectionalAct(HexDirection.north, 1, 2);

			else if (flags.get("south") && flags.get("west")) moveDirectionalAct(HexDirection.southWest, 1, 2);
			else if (flags.get("south") && flags.get("east")) moveDirectionalAct(HexDirection.southEast, 1, 2);
			else if (flags.get("south")) moveDirectionalAct(HexDirection.south, 1, 2);
			
			if (flags.get("up")) moveDirectionalAct(HexDirection.up, 1, 2);
			if (flags.get("down")) moveDirectionalAct(HexDirection.down, 1, 2);
		}
	}
	private void attackFunction(Object caller, Map<String, String> parameters, Map<String, Boolean> flags) throws InvalidParameterException
	{
		if (caller != this) return; // The only thing that should run *our* moveFunction is *us*
		
		int targetID = Integer.valueOf(parameters.get("target"));
		int weaponID = Integer.valueOf(parameters.get("weapon"));
		String locationName = parameters.get("location");
		
		MektonActor opponent = null;
		AdditiveSystemList opponentServos = null;
		HitLocation location = new HitLocation(null, null, null, null, 0);
		
		if (locationName != null)
		{
			try // Is a servo?
			{
				location.type = HitLocation.ServoType.valueOf(locationName);
				
				if (flags.get("left")) location.side = HitLocation.ServoSide.left;
				else if (flags.get("middle")) location.side = HitLocation.ServoSide.middle;
				else if (flags.get("right")) location.side = HitLocation.ServoSide.right;
				else location.side = Rolls.rollSide(opponentServos, location.type);
				
				if (parameters.get("index") != null) location.index = Integer.valueOf(parameters.get("index")) - 1; // -1 is because humans start counting at one
				else location.index = Rolls.rollXDY(1, opponentServos.servoCount(location.type, location.side)) - 1;
				
				if (opponentServos.getServo(location) == null) // Has the player not picked a valid location?
					location = Rolls.mechaHitChart(opponentServos, true); // TODO error message?
			}
			catch (Exception e1) // Is a special?
			{
				try {location.special = HitLocation.Special.valueOf(locationName);}
				catch (Exception e2) // Is a cinematic?
				{
					try {location.cinematic = HitLocation.Cinematic.valueOf(locationName);}
					catch (Exception e3) {throw new InvalidParameterException("location", locationName);}
				}
			}
		}
		else location = Rolls.mechaHitChart(opponentServos, true);
		
		attack(opponent, location);
	}

	protected void registerCommand(ParsingCommand command)
	{
		commandBank.registerCommand(command);
	}
	
	protected void registerCommands()
	{
		ParsingCommand moveCommand = new ParsingCommand(
				new String[]{"move", "Move"},
				"Moves the actor.",
				new ParsingCommand.Parameter[]{
						new ParsingCommand.Parameter(new String[] {"q"}, "Q position to move to.", true),
						new ParsingCommand.Parameter(new String[] {"r"}, "R position to move to.", true)},
				new ParsingCommand.Flag[]{
						new ParsingCommand.Flag(new String[] {"north", "n"}, "Denotes moving north."),
						new ParsingCommand.Flag(new String[] {"west", "w"}, "Denotes moving west."),
						new ParsingCommand.Flag(new String[] {"east", "e"}, "Denotes moving east."),
						new ParsingCommand.Flag(new String[] {"south", "s"}, "Denotes moving south."),
						new ParsingCommand.Flag(new String[] {"up", "u"}, "Denotes moving up."),
						new ParsingCommand.Flag(new String[] {"down", "d"}, "Denotes moving down."),
				},
				(caller, parameters, flags) -> {moveFunction(caller, parameters, flags);});
		registerCommand(moveCommand);
		
		ParsingCommand attackCommand = new ParsingCommand(
				new String[]{"attack", "Attack"},
				"Attacks the target with the weapon.",
				new ParsingCommand.Parameter[]{
						new ParsingCommand.Parameter(new String[] {"target", "t"}, "Target entity to attack.", false),
						new ParsingCommand.Parameter(new String[] {"weapon", "w"}, "ID of weapon to use.", false),
						new ParsingCommand.Parameter(new String[] {"location", "l"}, "Location to aim for.", true),
						new ParsingCommand.Parameter(new String[] {"index", "i"}, "Index of the part in the specified location to aim for.", true)},
				new ParsingCommand.Flag[] {
						new ParsingCommand.Flag(new String[] {"left", "l"}, "Target the left side of the opponent."),
						new ParsingCommand.Flag(new String[] {"middle", "m"}, "Target the middle of the opponent."),
						new ParsingCommand.Flag(new String[] {"right", "r"}, "Target the right side of the opponent."),},
				(caller, parameters, flags) -> {moveFunction(caller, parameters, flags);});
		registerCommand(moveCommand);
	}
		
	// Protected abstracts
		
	protected abstract int getMA(); // Speed in human hexes	
		
	// Constructor
	
	public MektonActor()
	{
		super();
		actionPoints = 0f;
		actionTimer = new SimpleTimer();
		commandBank = new ParsingCommandBank();
		
		registerCommands();
	}
	public MektonActor(MektonMap map)
	{
		super(map);
		actionPoints = 0f;
		actionTimer = new SimpleTimer();
		commandBank = new ParsingCommandBank();
		
		resetActionPoints();
		
		registerCommands();
	}

	// Actions system
	
	public void resetActionPoints()
	{
		actionPoints = 2d;
		actionTimer.start();
		resume();
	}
	public double remainingActions()
	{
		return actionPoints;
	}
	public boolean takeAction(double cost)
	{
		if (actionPoints >= cost)
		{
			actionPoints = Double.valueOf(MiscUtils.floatPrecise((float) actionPoints - (float) cost, 4));
			return true;
		}
		else return false;
	}

	// Public abstracts
	
	public abstract void takeDamage(Damage damage);
	public abstract void defend(MektonActor aggressor, HitLocation location);
	public abstract void attack(MektonActor defender, HitLocation location);
	
	// Conscious movementw
	
	public void moveTargetHexAct(AxialHexCoord3D target, int speed)
	{
		double moveCost = Math.abs((float) hexPos.distance(target)) / (float) getMA(); // TODO assumes walking
		if (takeAction(moveCost)) super.moveTargetHex(target, speed);
		else pause();
	}
	public void moveDeltaHexAct(AxialHexCoord3D delta, int speed)
	{
		moveTargetHexAct(hexPos.rAdd(delta), speed);
	}
	public void moveDirectionalAct(HexDirection dir, int distance, int speed)
	{
		AxialHexCoord3D delta = hexPos.getUnitVector(dir).rMultiply(distance);
		setDirection(dir);
		
		moveDeltaHexAct(delta, speed);
	}
	
	// Overridden functions
	
	@Override
	public void movePath(LinkedList<AxialHexCoord3D> path, int speed)
	{
		this.path = path;
		moveTargetHexAct(path.getFirst(), speed);
	}
	@Override
	public void updatePath()
	{
		if (this.path != null && this.path.size() != 0 && this.hexPos == this.path.getFirst()) // Ready for next step of path
		{
			this.path.remove();
			if (this.path.isEmpty()) this.path = null;
			else moveTargetHexAct(path.getFirst(), baseSpeed);
		}
		else if (this.path != null && this.path.size() != 0)
		{
			moveTargetHexAct(path.getFirst(), baseSpeed);
		}
	}
	
	@Override
	public void onResume()
	{
		updatePath();
	}
	
	@Override
	public void update()
	{
		super.update();
		if (actionTimer.checkTime(10000))
		{
			resetActionPoints();
		}
	}
	
	@Override
	public void addRole(String role) {} // Might be security loophole to set through here
	
	@Override
	public boolean hasRole(String role)
	{
		if (RoleAccount.class.isAssignableFrom(getPossessor().getClass()))
			return ((RoleAccount) getPossessor()).hasRole(role);
		else return false;
	}
	
	@Override
	public List<String> getRoles()
	{
		if (RoleAccount.class.isAssignableFrom(getPossessor().getClass()))
			return ((RoleAccount) getPossessor()).getRoles();
		else return null;
	}
	
	@Override
	public boolean runCommand(String... words)
	{
		if (commandBank.recognizes(words[0]))
		{
			try {commandBank.execute(this, words);}
			catch (Exception e) {Logging.logException(e);}
			return true;
			
		}
		else return false;
	}
}
