// By Iacon1
// Created 12/03/2021
//

package Modules.MektonCore.StatsStuff.SheetTypes;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import GameEngine.MenuSlate;
import GameEngine.Editor.Editable;
import GameEngine.Editor.EditorPanel;
import GameEngine.MenuSlate.DataFunction;
import GameEngine.MenuSlate.TabHandle;
import GameEngine.MenuSlatePopulator;
import GameEngine.Configurables.ModuleManager;
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
import Modules.MektonCore.StatsStuff.SystemTypes.MultiplierSystems.MultiplierSystem;
import Modules.MektonCore.StatsStuff.SystemTypes.MultiplierSystems.Powerplant;

public class MekSheet implements Editable, MenuSlatePopulator
{
	private LocationList<MekServo> servos;
	private LocationList<AdditiveSystem> additiveSystems;
	private Map<String, MultiplierSystem> multiplierSystems;
	
	protected String name;
	protected double weightEfficiency; // Weight efficiency in tons.
	protected Scale scale;
	
	public MekSheet()
	{
		this.servos = new LocationList<MekServo>();
		this.additiveSystems =  new LocationList<AdditiveSystem>();
		this.multiplierSystems = new HashMap<String, MultiplierSystem>();

		this.name = "";
		this.weightEfficiency = 0;
		this.scale = Scale.mekton;
		
		servos.add(new MekServo(new MekServo(Scale.mekton, ServoClass.mediumStriker, ServoClass.mediumStriker, ServoType.torso, ArmorType.standard, LevelRAM.none)));
		multiplierSystems.put("powerplant", new Powerplant());
	}
	
	public MekServo getServo(HitLocation location) {return servos.getLocation(location);}
	
	public ScaledCostValue getCost()
	{
		ScaledCostValue baseValue = new ScaledCostValue(Scale.mekton, 0);
		
		for (int i = 0; i < servos.size(); ++i)
			baseValue.addInPlace(servos.get(i).getCost());
		for (int i = 0; i < additiveSystems.size(); ++i)
			baseValue.addInPlace(additiveSystems.get(i).getCost());
		
		double modifier = 1;
		for (MultiplierSystem multiplierSystem : multiplierSystems.values()) modifier += multiplierSystem.getMultiplier();
		ScaledCostValue multValue = baseValue.multiply(modifier);
		
		multValue.addInPlace(new ScaledCostValue(Scale.mekton, weightEfficiency * 2d));
		// TODO WE and stuff
		
		return multValue;
	}
	
	public double getWeight()
	{
		double weight = 110; // Test
		
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
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void populate(MenuSlate slate, Supplier<MenuSlate> slateSupplier)
	{
		slate.addTextbar(0, 0, "Name:", 5, 5, 20, 2, new DataFunction<String>()
		{
			@Override public String getValue() {return name;}
			@Override public void setValue(String data) {name = data;}	
		});
		slate.addInfo(0, 2, "Cost:", 5, 5, 1, () -> {return getCost().getValue(Scale.mekton) + " CP";});
		slate.addInfo(0, 3, "Weight:", 5, 5, 1, () -> {return getWeight() + " tons";});
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

		TabHandle tabHandle = slate.addTabbedSection(0, 9, 32, 16);
		
		MenuSlate servoSlate = slateSupplier.get();
		tabHandle.addTab("Additive Systems", servoSlate);
		servoSlate.addInfo(0, 1, "Servos", 10, 0, 1, () -> {return null;});
		
		MenuSlate addSlate = slateSupplier.get();
		tabHandle.addTab("Additive Systems", addSlate);
		addSlate.addInfo(0, 1, "Additive", 10, 0, 1, () -> {return null;});
		
		MenuSlate multSlate = slateSupplier.get();
		multSlate.setCells(32,  16);
		tabHandle.addTab("Multiplier Systems", multSlate);
		int i = 0;
		for (MultiplierSystem multiplierSystem : multiplierSystems.values())
		{
			MenuSlate multItem = slateSupplier.get();
			multiplierSystem.populate(multItem, slateSupplier);
			multSlate.addSubSlate(0, 4*i + 1, 20, 2, multItem);
		}
	}
}
