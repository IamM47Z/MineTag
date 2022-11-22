package tk.m47z.mineTag.Commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import tk.m47z.mineTag.Game;
import tk.m47z.mineTag.Main;

import javax.management.openmbean.KeyAlreadyExistsException;
import java.util.NoSuchElementException;

public class Start implements CommandExecutor
{
	public Start() throws NoSuchElementException
	{
		PluginCommand command = Main.getInstance().getCommand("mt-start");
		if ( command == null )
			throw new NoSuchElementException();
		
		command.setExecutor(this);
	}
	
	@Override
	public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, String[] strings)
	{
		if ( !( commandSender instanceof Player ) )
		{
			Bukkit.getLogger().info("[MineTag] Invalid sender");
			return false;
		}
		
		Main main = Main.getInstance();
		Player sender = Bukkit.getPlayer(commandSender.getName());
		if ( sender == null )
		{
			Bukkit.getLogger().info("[MineTag] Error obtaining sender Player object");
			return false;
		}
		
		if ( main.getGameByPlayer(sender) != null )
		{
			Bukkit.getLogger().info("[MineTag] Player is not in a game");
			return false;
		}
		
		Game game = new Game();
		
		// add all world players
		//
		for ( Player player : sender.getWorld().getPlayers() )
		{
			try
			{
				game.addPlayer(player);
			}
			catch ( KeyAlreadyExistsException e )
			{
				Bukkit.getLogger().info("[MineTag] Non-Unique UUID found: " + player.getUniqueId());
			}
		}
		
		if ( !game.start(sender.getUniqueId()) )
		{
			Bukkit.getLogger().info("[MineTag] Error beginning Tag Game");
			return false;
		}
		
		main.addGame(game);
		Bukkit.getLogger().info("[MineTag] Tag Game began");
		return true;
	}
}