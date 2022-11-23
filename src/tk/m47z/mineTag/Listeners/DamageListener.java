package tk.m47z.mineTag.Listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import tk.m47z.mineTag.Game;
import tk.m47z.mineTag.Main;

import static org.bukkit.event.entity.EntityDamageEvent.DamageCause.ENTITY_ATTACK;

public class DamageListener implements Listener
{
	public DamageListener()
	{
		Bukkit.getServer().getPluginManager().registerEvents(this, Main.getInstance());
	}
	
	@EventHandler
	public void onPlayerDamage(EntityDamageEvent e)
	{
		if ( !( e.getEntity() instanceof Player ) || e.getCause() != ENTITY_ATTACK )
		{
			e.setCancelled(false);
			return;
		}
		
		Main main = Main.getInstance();
		Player player_damaged = ( Player ) e.getEntity();
		Player player_damager = ( Player ) ( ( EntityDamageByEntityEvent ) e ).getDamager();
		
		Game game = main.getGameByPlayer(player_damaged);
		if ( game == null || game != main.getGameByPlayer(player_damager) || !game.isRunning() || !game.isCatcher(player_damager.getUniqueId()) )
		{
			e.setCancelled(false);
			return;
		}
		
		game.changeCatcher(player_damaged.getUniqueId());
		e.setCancelled(true);
	}
}
