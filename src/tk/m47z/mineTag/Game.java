package tk.m47z.mineTag;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class Game
{
	private static Game instance = null;
	
	public static Game getInstance()
	{
		return ( instance == null ? ( instance = new Game() ) : instance );
	}
	
	private Map<Player, ItemStack[]> all_player_items = new HashMap<Player, ItemStack[]>();
	private Map<Player, ItemStack[]> all_player_armor = new HashMap<Player, ItemStack[]>();
	
	public Map.Entry<ItemStack[], ItemStack[]> getPlayerInvetory(Player player)
	{
		return new AbstractMap.SimpleEntry<ItemStack[], ItemStack[]>(all_player_items.get(player), all_player_armor.get(player));
	}
	
	public boolean setPlayerIventory(Player player)
	{
		if ( all_player_items.containsKey(player) || all_player_armor.containsKey(player) )
			return false;
		
		all_player_items.put(player, player.getInventory().getContents());
		all_player_armor.put(player, player.getInventory().getContents());
		return true;
	}
}
