// By Iacon1
// Created 01/21/2023
//

package Modules.MektonCore;

import java.awt.Color;
import java.io.File;
import java.util.Arrays;
import java.util.function.Supplier;

import GameEngine.GameInfo;
import GameEngine.MenuSlate;
import GameEngine.MenuSlatePopulator;
import GameEngine.Editor.Editable;
import GameEngine.Editor.EditorPanel;
import GameEngine.Graphics.LayeredSprite;
import GameEngine.MenuSlate.DataFunction;

public class MektonActorSprite extends LayeredSprite implements Editable, MenuSlatePopulator
{
	final private static String dir = "MASSets";
	private String setName;
	
	private Color getColor(String layerName)
	{
		Color[] palette = getPalette(layerName).clone();
		
		Arrays.sort(palette, (Color o1, Color o2) ->
		{
			float[] f1 = new float[3], f2 = new float[3];
			Color.RGBtoHSB(o1.getRed(), o1.getGreen(), o1.getBlue(), f1);
			Color.RGBtoHSB(o2.getRed(), o2.getGreen(), o2.getBlue(), f2);
			if (f1[2] < f2[2]) return -1;
			else if (f1[2] == f2[2]) return 0;
			else return 1;
		});
		
		return palette[palette.length / 2];
	}
	private float getHue(String layerName)
	{
		float[] f = new float[3];
		Color c = getColor(layerName);
		Color.RGBtoHSB(c.getRed(), c.getGreen(), c.getBlue(), f);
		return f[0];
	}
	private void setHue(String layerName, float hue)
	{
		float oldHue = getHue(layerName);
		
		Color[] palette = getPalette(layerName).clone();
		for (int i = 0; i < palette.length; ++i)
		{
			float[] HSB = new float[3];
			int alpha = palette[i].getAlpha();
			Color.RGBtoHSB(palette[i].getRed(), palette[i].getGreen(), palette[i].getBlue(), HSB);
			if (HSB[0] == 0 && oldHue == 0) HSB[0] = hue;
			else HSB[0] *= hue / oldHue;
			palette[i] = Color.getHSBColor(HSB[0], HSB[1], HSB[2]);
			palette[i] = new Color(palette[i].getRed(), palette[i].getGreen(), palette[i].getBlue(), alpha);
		}
		setPalette(layerName, palette);
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
		File setFolder = new File(GameInfo.getServerPackResource("Graphics/" + dir + "/" ));
		return removeFileExt(setFolder.list());
	}
	private String[] getLayers()
	{
		File setFile = new File(GameInfo.getServerPackResource("Graphics/" + dir + "/" + setName));
		return removeFileExt(setFile.list());
	}
	private String[] getLayerOptions(String layerName)
	{
		File layerFile = new File(GameInfo.getServerPackResource("Graphics/" + dir + "/" + setName + "/" + layerName));
		return removeFileExt(layerFile.list());
	}
	private void updateSet()
	{	
		for (String layerName : getLayers())
		{
			File layerFile = new File(GameInfo.getServerPackResource("Graphics/" + dir + "/" + setName + "/" + layerName));
			if (layerFile.isDirectory() && layerFile.list().length != 0)
			{
				String filePath = dir + "/" + setName + "/" + layerName + "/" + layerFile.list()[0].split(".png")[0];		
				addLayer(layerName, filePath);
			}
		}
	}
	
	public MektonActorSprite() {super();}
	public MektonActorSprite(String setName)
	{
		super();
		this.setName = setName;
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
			slate.addOptions(0, 2 * i, layerName, 2, 5, 2, getLayerOptions(layerName), getLayerOptions(layerName), new DataFunction<String>()
			{
				@Override public String getValue() {return getTexture(layerName).split("/")[3];}
				@Override public void setValue(String data) {setTexture(dir + "/" + setName + "/" + layerName, data);}
			});
			slate.addDoubleWheel(8, 2 * i, "Hue:", 2, 0, 1, 2, 3, 2, new DataFunction<Double>()
			{
				@Override public Double getValue() {return Double.valueOf(getHue(layerName));}
				@Override public void setValue(Double data) {setHue(layerName, data.floatValue());}
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
