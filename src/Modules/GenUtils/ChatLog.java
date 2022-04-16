// By Iacon1
// Created 11/22/2021
// Chat log for talking

package Modules.GenUtils;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ChatLog
{
	private static class ChatEntry
	{
		public Date date;
		public String channel;
		public String text;
	}
	private static List<ChatEntry> chatLog;

	public static void init()
	{
		chatLog = new ArrayList<ChatEntry>();
	}

	public static void chatTo(String channel, String text)
	{
		ChatEntry entry = new ChatEntry();
		entry.date = Date.from(Instant.now());
		entry.text = text;
		entry.channel = channel;
		
		chatLog.add(entry);
	}
}
