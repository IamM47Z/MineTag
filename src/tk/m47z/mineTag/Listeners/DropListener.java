package tk.m47z.mineTag.Listeners;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerDropItemEvent;
import tk.m47z.mineTag.Game;
import tk.m47z.mineTag.Main;

public class DropListener implements Listener
{
	public DropListener()
	{
		Bukkit.getServer().getPluginManager().registerEvents(this, Main.getInstance());
	}
	
	@EventHandler
	public void onThrow(PlayerDropItemEvent e)
	{
		Main main = Main.getInstance();
		
		Game game = main.getGameByPlayer(e.getPlayer());
		e.setCancelled(game != null);
	}
}
