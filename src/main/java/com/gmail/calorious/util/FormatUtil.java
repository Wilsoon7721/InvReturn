package com.gmail.calorious.util;

import org.bukkit.ChatColor;

public class FormatUtil {

    public static String format(String msg) {
	return ChatColor.translateAlternateColorCodes('&', msg);
    }
}
