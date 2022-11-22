package tk.m47z.mineTag;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin
{
	@Override
	public void onEnable()
	{
		Bukkit.getLogger().info("MineTag has been Enabled");
	}
	
	@Override
	public void onDisable()
	{
		Bukkit.getLogger().info("MineTag has been Disabled");
	}
}