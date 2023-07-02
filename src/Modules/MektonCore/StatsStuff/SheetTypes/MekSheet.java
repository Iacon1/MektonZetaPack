// By Iacon1
// Created 12/03/2021
//

package Modules.MektonCore.StatsStuff.SheetTypes;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Supplier;

import GameEngine.Editor.Editable;
import GameEngine.Editor.EditorPanel;
import GameEngine.EntityTypes.Alignable;
import GameEngine.MenuStuff.MenuSlate;
import GameEngine.MenuStuff.MenuSlatePopulator;
import GameEngine.MenuStuff.MenuSlate.ComponentHandle;
import GameEngine.MenuStuff.MenuSlate.DataFunction;
import GameEngine.MenuStuff.MenuSlate.TabHandle;
import Modules.MektonCore.Enums.ArmorType;
import Modules.MektonCore.Enums.LevelRAM;
import Modules.MektonCore.Enums.Scale;
import Modules.MektonCore.Enums.ServoClass;
import Modules.MektonCore.StatsStuff.HitLocation;
import Modules.MektonCore.StatsStuff.HitLocation.ServoType;
import Modules.MektonCore.StatsStuff.LocationList;
import Modules.MektonCore.StatsStuff.ScaledUnits.ScaledCostValue;
import Modules.MektonCore.StatsStuff.SystemTypes.AdditiveSystems.AdditiveSystem;
import Modules.MektonCore.StatsStuff.SystemTypes.AdditiveSystems.Servos.MekServo;
import Modules.MektonCore.StatsStuff.SystemTypes.MultiplierSystems.ACE;
import Modules.MektonCore.StatsStuff.SystemTypes.MultiplierSystems.Cloaking;
import Modules.MektonCore.StatsStuff.SystemTypes.MultiplierSystems.CockpitControls;
import Modules.MektonCore.StatsStuff.SystemTypes.MultiplierSystems.Environmentals;
import Modules.MektonCore.StatsStuff.SystemTypes.MultiplierSystems.Hydraulics;
import Modules.MektonCore.StatsStuff.SystemTypes.MultiplierSystems.InternalAutomation;
import Modules.MektonCore.StatsStuff.SystemTypes.MultiplierSystems.ManeuverVerniers;
import Modules.MektonCore.StatsStuff.SystemTypes.MultiplierSystems.MultiplierSystem;
import Modules.MektonCore.StatsStuff.SystemTypes.MultiplierSystems.Powerplant;
import Modules.MektonCore.StatsStuff.SystemTypes.MultiplierSystems.ShadowImager;
import Utils.MiscUtils;

public class MekSheet implements Editable, MenuSlatePopulator
{
	private LocationList<MekServo> servos;
	private LocationList<AdditiveSystem> additiveSystems;

	// Multiplier systems
	private List<MultiplierSystem> multiplierSystems; 
	private Powerplant powerplant;
	private CockpitControls cockpitControls;
	private Hydraulics hydraulics;
	private Environmentals environmentals; 
	private ManeuverVerniers maneuverVerniers;
	private ACE ace;
	private InternalAutomation internalAutomation;
	private Cloaking cloaking;
	private ShadowImager shadowImager;
	
	protected String name;
	protected double weightEfficiency; // Weight efficiency in tons.
	protected Scale scale;
	
	public MekSheet()
	{
		this.servos = new LocationList<MekServo>();
		this.additiveSystems =  new LocationList<AdditiveSystem>();
		this.multiplierSystems = new LinkedList<MultiplierSystem>();
		this.name = "";
		this.weightEfficiency = 0;
		this.scale = Scale.mekton;

		powerplant = new Powerplant(); multiplierSystems.add(powerplant);
		cockpitControls = new CockpitControls(); multiplierSystems.add(cockpitControls);
		hydraulics = new Hydraulics(); multiplierSystems.add(hydraulics);
		environmentals = new Environmentals(); multiplierSystems.add(environmentals);
		maneuverVerniers = new ManeuverVerniers(); multiplierSystems.add(maneuverVerniers);
		ace = new ACE(); multiplierSystems.add(ace);
		internalAutomation = new InternalAutomation(); multiplierSystems.add(internalAutomation);
		cloaking = new Cloaking(); multiplierSystems.add(cloaking);
		shadowImager = new ShadowImager(); multiplierSystems.add(shadowImager);
	}
	
	public MekServo getServo(HitLocation location) {return servos.getLocation(location);}
	
	private double getCostMultiplier()
	{
		double modifier = 1;
		for (MultiplierSystem multiplierSystem : multiplierSystems) modifier += multiplierSystem.getMultiplier();
		return modifier;
		
	}
	public ScaledCostValue getCost()
	{
		ScaledCostValue baseValue = new ScaledCostValue(Scale.mekton, 0);
		
		for (int i = 0; i < servos.size(); ++i)
			baseValue.addInPlace(servos.get(i).getCost());
		for (int i = 0; i < additiveSystems.size(); ++i)
			baseValue.addInPlace(additiveSystems.get(i).getCost());
		
		baseValue.addInPlace(new ScaledCostValue(scale, cockpitControls.getCostFlat()));
		
		ScaledCostValue multValue = baseValue.multiply(getCostMultiplier());
		
		multValue.addInPlace(new ScaledCostValue(scale, weightEfficiency * 2d));
		// TODO WE and stuff
		
		return multValue;
	}
	
	public double getWeight()
	{
		double weight = 0;
		
		// Weight of servos
		for (MekServo servo : servos) {weight += servo.getWeight();}
		// Weight of additive systems
		for (AdditiveSystem additiveSystem : additiveSystems) {weight += additiveSystem.getWeight();}
		
		// WE
		weight -= weightEfficiency;
		
		return weight;
	}
	
	/** Calculates the MV of the mek.
 	 * 
	 * @return The MV of the mek.
	 */
	public int getMV()
	{
		int MV = 0;
		
		// MV penalty due to weight;
		MV = -1 * (int) Math.max(1, Math.floor(getWeight() / 10d));
		if (MV < -10) MV = -10;
		
		MV += maneuverVerniers.getLevel();

		if (MV > 0) MV = 0; // Put things that can't raise above 0 above, things that can below
		
		return MV;
	}
	@Override
	public EditorPanel editorPanel()
	{
		EditorPanel editorPanel = new EditorPanel(640, 480, 32, 32); // TODO 
		editorPanel.setName("Mek Editor");
		populate(editorPanel, () -> {return new EditorPanel();});
		return editorPanel;
	}

	@Override
	public String getName() {return name;}

	private void populateServosSlate(TabHandle tabHandle, Supplier<MenuSlate> slateSupplier)
	{
		MenuSlate servosSlate = slateSupplier.get();
		servosSlate.setCells(32,  23);
		
		servosSlate.addButton(0, 1, "Add servo", 5, 3, () -> 
		{
			MekServo servo = new MekServo(scale, ServoClass.mediumStriker, ServoClass.mediumStriker, ServoType.torso, ArmorType.standard, LevelRAM.none);
			servo.resetHealth();
			servo.resetArmor();
			servos.add(servo);
			populateServosSlate(tabHandle, slateSupplier);
		});
		
		ComponentHandle handle = null;
		
		for (MekServo servo : servos)
		{
			
			MenuSlate servoSlate = slateSupplier.get();
			servo.populate(servoSlate, slateSupplier);
			if (handle != null) handle = servosSlate.addSubSlate(handle, Alignable.AlignmentPoint.southWest, 0, 0, 28, 3, servoSlate);
			else handle = servosSlate.addSubSlate(0, 4, 28, 3, servoSlate);
			servosSlate.addButton(handle, Alignable.AlignmentPoint.northEast, 0, 0, "Remove", 5, 3, () ->
			{
				servos.remove(servo);
				populateServosSlate(tabHandle, slateSupplier);
			});
		}
		
		tabHandle.setTab("Servos", servosSlate);
	}
	private void populateAdditivesSlate(TabHandle tabHandle, Supplier<MenuSlate> slateSupplier)
	{
		MenuSlate addSlate = slateSupplier.get();
		addSlate.setCells(32,  23);
		tabHandle.setTab("Additive Systems", addSlate);
		addSlate.addInfo(0, 1, "Additive", 10, 0, 1, () -> {return null;});
	}
	private void populateMultipliersSlate(TabHandle tabHandle, Supplier<MenuSlate> slateSupplier)
	{
		MenuSlate multSlate = slateSupplier.get();
		MenuSlate multScrollSlate = slateSupplier.get();
		multSlate.setCells(32, 23);
		multScrollSlate.setCells(32, 23);
		multScrollSlate.addScrollSlate(0, 0, 32, 23, multSlate);
		tabHandle.setTab("Multiplier Systems", multScrollSlate);
		
		ComponentHandle handle = null;
		
		for (MultiplierSystem multiplierSystem : multiplierSystems)
		{
			MenuSlate multItem = slateSupplier.get();
			multiplierSystem.populate(multItem, slateSupplier);
			if (handle != null) handle = multSlate.addSubSlate(handle, Alignable.AlignmentPoint.southWest, 0, 0, multItem);
			else handle = multSlate.addSubSlate(0, 1, multItem);
		}
	}
	
	@Override
	public void populate(MenuSlate slate, Supplier<MenuSlate> slateSupplier)
	{
		slate.addTextbar(0, 0, "Name:", 5, 5, 20, 2, new DataFunction<String>()
		{
			@Override public String getValue() {return name;}
			@Override public void setValue(String data) {name = data;}	
		});
		slate.addInfo(0, 2, "Cost:", 5, 5, 1, () -> {return MiscUtils.doublePrecise(getCost().getValue(Scale.mekton), 2) + " CP";});
		slate.addInfo(11, 2, "Multiplier:", 5, 5, 1, () -> {return "x" + MiscUtils.doublePrecise(getCostMultiplier(), 2);});
		slate.addInfo(0, 3, "Weight:", 5, 5, 1, () -> {return MiscUtils.doublePrecise(getWeight(), 2) + " tons";});
		slate.addInfo(0, 4, "MV:", 5, 5, 1, () -> {return String.valueOf(getMV());});
		
		slate.addOptions(0, 5, "Scale:", 5, 6, 2, Scale.values(), new DataFunction<Scale>()
		{
			@Override public Scale getValue() {return scale;}
			@Override public void setValue(Scale data) {scale = data;}	
		});
		slate.addDoubleWheel(0, 7, "Weight Efficiency:", 5, 0, Double.MAX_VALUE, 1, 5, 2, new DataFunction<Double>()
		{
			@Override public Double getValue() {return Math.min(weightEfficiency, getWeight() + weightEfficiency);}
			@Override public void setValue(Double data) {if (data < getWeight() + weightEfficiency) weightEfficiency = data;}	
		});

		TabHandle tabHandle = slate.addTabbedSection(0, 9, 32, 23);

		populateServosSlate(tabHandle, slateSupplier);
		populateAdditivesSlate(tabHandle, slateSupplier);
		populateMultipliersSlate(tabHandle, slateSupplier);
	}


}
