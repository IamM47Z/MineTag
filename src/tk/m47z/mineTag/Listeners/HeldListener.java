package tk.m47z.mineTag.Listeners;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;

import tk.m47z.mineTag.Game;
import tk.m47z.mineTag.Main;

public class HeldListener implements Listener
{
	public HeldListener()
	{
		Bukkit.getServer().getPluginManager().registerEvents(this, Main.getInstance());
	}
	
	@EventHandler
	public void onSwapHandItems(PlayerSwapHandItemsEvent e)
	{
		Main main = Main.getInstance();
		e.getPlayer().sendTitle("Swap", "Swaping Items is not Allowed!", 5, 20, 10);
		
		Game game = main.getGameByPlayer(e.getPlayer());
		e.setCancelled(game != null && game.isRunning());
	}
}