// By Iacon1
// Created 11/22/2021
// Chat bar. TODO Add log display features

package Modules.GenUtils;

import java.awt.Color;

import GameEngine.GameInfo;
import GameEngine.EntityTypes.GUITypes.Textbar;

public class ChatBox extends Textbar
{
	public ChatBox(String font, Color color, int heightPixels)
	{
		super("ChatBox", font, color, true, heightPixels);
		sprite.setBasicParams(0, 0, 64, 64);
		setBounds(0, 0, 0, 0);
	}
	
	@Override
	public void onSubmit()
	{
		GameInfo.getServer().runCommand(this.getOwner(), buffer);
		clear();
	}

	@Override
	public void onStart() {}
	@Override
	public void onStop() {}
	@Override
	public void onAnimStop() {}

	@Override
	public String getName()
	{
		return "Chat Box";
	}

	@Override
	public String onMousePress(int mX, int mY, int button) {return null;}
	@Override
	public String onMouseRelease(int mX, int mY, int button) {return null;}
	@Override
	public String onKeyRelease(int code) {return null;}


}
