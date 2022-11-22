package tk.m47z.mineTag.Commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import org.bukkit.entity.Player;
import tk.m47z.mineTag.Game;
import tk.m47z.mineTag.Utils;

import java.rmi.UnexpectedException;

public class Start implements CommandExecutor
{
	@Override
	public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings)
	{
		// iterating all players
		//
		for ( Player player : Bukkit.getOnlinePlayers() )
		{
			// saving players inventory
			//
			Game game = Game.getInstance();
			if ( !game.setPlayerIventory(player) )
			{
				Bukkit.getLogger().info("[MineTag] Error storing player inventory");
				return false;
			}
		}
		
		return false;
	}
}
