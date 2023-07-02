// By Iacon1
// Created 01/21/2023
//

package Modules.MektonCore;

import java.awt.Color;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import GameEngine.GameInfo;
import GameEngine.Editor.Editable;
import GameEngine.Editor.EditorPanel;
import GameEngine.Graphics.LayeredSprite;
import GameEngine.Managers.GraphicsManager;
import GameEngine.MenuStuff.MenuSlate;
import GameEngine.MenuStuff.MenuSlatePopulator;
import GameEngine.MenuStuff.MenuSlate.DataFunction;

public class MektonActorSprite extends LayeredSprite implements Editable, MenuSlatePopulator
{
	final private static String dir = "MASSets";
	private String setName;
	private Map<String, Color[]> basePalettes;
	private Map<String, Float[]> transforms;
	
	private void applyTransformation(String layerName)
	{
		if (transforms.get(layerName) == null) transforms.put(layerName, new Float[]{0f, 0f, 0f});
		Float[] transform = transforms.get(layerName);
		
		Color[] palette = basePalettes.get(layerName).clone();
		for (int i = 0; i < palette.length; ++i)
		{
			float[] HSB = new float[3];
			int alpha = palette[i].getAlpha();
			Color.RGBtoHSB(palette[i].getRed(), palette[i].getGreen(), palette[i].getBlue(), HSB);
			HSB[0] = GraphicsManager.rotateHue(HSB[0], transform[0]);
			HSB[1] = Math.max(0, Math.min(1, HSB[1] + transform[1]));
			HSB[2] = Math.max(0, Math.min(1, HSB[2] + transform[2]));
			palette[i] = Color.getHSBColor(HSB[0], HSB[1], HSB[2]);
			palette[i] = new Color(palette[i].getRed(), palette[i].getGreen(), palette[i].getBlue(), alpha);
		}
		setPalette(layerName, palette);
	}
	private float getRotation(String layerName)
	{
		if (transforms.get(layerName) == null) transforms.put(layerName, new Float[]{0f, 0f, 0f});
		return transforms.get(layerName)[0];
	}
	private void setRotation(String layerName, float rot)
	{
		if (transforms.get(layerName) == null) transforms.put(layerName, new Float[]{0f, 0f, 0f});
		transforms.get(layerName)[0] = rot;
		applyTransformation(layerName);
	}
	private float getSaturation(String layerName)
	{
		if (transforms.get(layerName) == null) transforms.put(layerName, new Float[]{0f, 0f, 0f});
		return transforms.get(layerName)[1];
	}
	private void setSaturation(String layerName, float sat)
	{
		if (transforms.get(layerName) == null) transforms.put(layerName, new Float[]{0f, 0f, 0f});
		transforms.get(layerName)[1] = sat;
		applyTransformation(layerName);
	}
	private float getBrightness(String layerName)
	{
		if (transforms.get(layerName) == null) transforms.put(layerName, new Float[]{0f, 0f, 0f});
		return transforms.get(layerName)[2];
	}
	private void setBrightness(String layerName, float val)
	{
		if (transforms.get(layerName) == null) transforms.put(layerName, new Float[]{0f, 0f, 0f});
		transforms.get(layerName)[2] = val;
		applyTransformation(layerName);
	}
	
	private static String[] removeFileExt(String[] strings)
	{
		String[] newStrings = new String[strings.length];
		for (int i = 0; i < strings.length; ++i)
		{
			newStrings[i] = strings[i].split(".png")[0];
		}
		
		return newStrings;
	}
	private static String[] getSets()
	{
		File setFolder = new File(GameInfo.inServerPack("Graphics/" + dir + "/" ));
		return removeFileExt(setFolder.list());
	}
	private String[] getLayers()
	{
		File setFile = new File(GameInfo.inServerPack("Graphics/" + dir + "/" + setName));
		return removeFileExt(setFile.list());
	}
	private String[] getLayerOptions(String layerName)
	{
		File layerFile = new File(GameInfo.inServerPack("Graphics/" + dir + "/" + setName + "/" + layerName));
		return removeFileExt(layerFile.list());
	}
	private void updateSet()
	{	
		for (String layerName : getLayers())
		{
			File layerFile = new File(GameInfo.inServerPack("Graphics/" + dir + "/" + setName + "/" + layerName));
			if (layerFile.isDirectory() && layerFile.list().length != 0)
			{
				String filePath = dir + "/" + setName + "/" + layerName + "/" + layerFile.list()[0].split(".png")[0];		
				setLayer(layerName, filePath);
				basePalettes.put(layerName, getPalette(layerName));
			}
		}
	}
	
	public MektonActorSprite()
	{
		super();
		this.basePalettes = new HashMap<String, Color[]>();
		this.transforms = new HashMap<String, Float[]>();
	}
	public MektonActorSprite(String setName)
	{
		super();
		this.setName = setName;
		this.basePalettes = new HashMap<String, Color[]>();
		this.transforms = new HashMap<String, Float[]>();
		updateSet();
	}
	
	@Override
	public void populate(MenuSlate slate, Supplier<MenuSlate> supplier)
	{
		slate.clear();
		slate.setCells(32, 32);
		slate.addOptions(0, 0, "Sprite Set:", 5, 5, 2, getSets(), getSets(), new DataFunction<String>()
		{
			@Override public String getValue() {return setName;}
			@Override public void setValue(String data) {setName = data; updateSet(); populate(slate, supplier);}
		});
		
		int i = 1;
		for (String layerName : getLayers())
		{
			int y = 4 * i - 2;
			MenuSlate layerSlate = supplier.get();
			layerSlate.setCells(15, 4);
			slate.addSubSlate(0, y, 15, 4, layerSlate);
			layerSlate.addOptions(0, 0, layerName, 2, 5, 2, getLayerOptions(layerName), getLayerOptions(layerName), new DataFunction<String>()
			{
				@Override public String getValue() {return getTexture(layerName).split("/")[3];}
				@Override public void setValue(String data)
				{
					setLayer(layerName, dir + "/" + setName + "/" + layerName + "/" + data);
					basePalettes.put(layerName, getPalette(layerName));
					applyTransformation(layerName);
				}
			});
			layerSlate.addDoubleWheel(9, 0, "Hue Shift", 3, -180, 0, 180, -1, 3, 2, new DataFunction<Double>()
			{
				@Override public Double getValue() {return Double.valueOf(getRotation(layerName));}
				@Override public void setValue(Double data) {setRotation(layerName, data.floatValue());}
			});
			layerSlate.addDoubleWheel(0, 2, "Saturation Shift", 4, -1, 0, 1, 2, 3, 2, new DataFunction<Double>()
			{
				@Override public Double getValue() {return Double.valueOf(getSaturation(layerName));}
				@Override public void setValue(Double data) {setSaturation(layerName, data.floatValue());}
			});
			layerSlate.addDoubleWheel(8, 2, "Brightness Shift", 4, -1, 0, 1, 2, 3, 2, new DataFunction<Double>()
			{
				@Override public Double getValue() {return Double.valueOf(getBrightness(layerName));}
				@Override public void setValue(Double data) {setBrightness(layerName, data.floatValue());}
			});
			++i;
		}
	
		slate.addSprite(15, 0, 20, 20, () ->
		{
			return this;
		});
	}

	@Override public String getName() {return "Sprite Set";}

	@Override
	public EditorPanel editorPanel()
	{
		EditorPanel panel = new EditorPanel(640, 480, 32, 32);
		populate(panel, () -> {return new EditorPanel();});
		return panel;
		
	}

}
