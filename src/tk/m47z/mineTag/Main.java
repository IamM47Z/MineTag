package tk.m47z.mineTag;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import tk.m47z.mineTag.Commands.Start;
import tk.m47z.mineTag.Commands.Stop;
import tk.m47z.mineTag.Listeners.DamageListener;
import tk.m47z.mineTag.Listeners.DropListener;
import tk.m47z.mineTag.Listeners.HeldListener;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

public class Main extends JavaPlugin
{
	private static Main instance = null;
	private final List<Game> games = new ArrayList<Game>();
	
	public static Main getInstance()
	{
		return instance;
	}
	
	public void addGame(Game game)
	{
		games.add(game);
	}
	
	public void removeGame(Game game) throws NoSuchElementException
	{
		if ( !games.contains(game) )
			throw new NoSuchElementException();
		
		games.remove(game);
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
		
		// register commands
		//
		new Stop();
		new Start();
		
		// register listeners
		//
		new DropListener();
		new HeldListener();
		new DamageListener();
		
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