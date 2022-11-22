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

import java.util.NoSuchElementException;

public class Stop implements CommandExecutor
{
	public Stop() throws NoSuchElementException
	{
		PluginCommand command = Main.getInstance().getCommand("mt-stop");
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
		
		Game game = main.getGameByPlayer(sender);
		if ( game == null )
		{
			Bukkit.getLogger().info("[MineTag] Error obtaining Player Game object");
			return false;
		}
		
		if ( !game.stop() )
		{
			Bukkit.getLogger().info("[MineTag] Error finishing Tag Game");
			return false;
		}
		
		Bukkit.getLogger().info("[MineTag] Tag Game finished");
		return true;
	}
}