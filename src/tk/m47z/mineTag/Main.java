package tk.m47z.mineTag;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import tk.m47z.mineTag.Commands.Start;
import tk.m47z.mineTag.Commands.Stop;

import java.util.ArrayList;
import java.util.List;

public class Main extends JavaPlugin
{
	private static Main instance = null;
	private List<Game> games = new ArrayList<Game>();
	
	public static Main getInstance()
	{
		return instance;
	}
	
	public void addGame(Game game)
	{
		games.add(game);
	}
	
	public Game getGameByPlayer(Player player)
	{
		for ( Game game : games )
			if ( game.getPlayer(player.getUniqueId()) != null )
				return game;
		
		return null;
	}
	
	@Override
	public void onEnable()
	{
		instance = this;
		
		new Stop();
		new Start();
		
		Bukkit.getLogger().info("MineTag has been Enabled");
	}
	
	@Override
	public void onDisable()
	{
		for ( Game game : games )
			game.stop();
		
		Bukkit.getLogger().info("MineTag has been Disabled");
	}
}