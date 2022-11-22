package tk.m47z.mineTag;

import java.util.*;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import javax.management.openmbean.KeyAlreadyExistsException;

public class Game
{
	private int timer = 60;
	private UUID catcher_uuid = null;
	private boolean is_running = false;
	
	private final GameProcessor game_processor = new GameProcessor();
	
	private final Map<UUID, Map.Entry<ItemStack[], ItemStack[]>> players_inv = new HashMap<UUID, Map.Entry<ItemStack[], ItemStack[]>>();
	private final Map<UUID, Player> players_list = new HashMap<UUID, Player>();
	private final ItemStack item = new ItemStack(Material.BAKED_POTATO, 1);
	
	public Game()
	{
		// create item
		//
		item.addUnsafeEnchantment(Enchantment.FIRE_ASPECT, 4);
		item.addUnsafeEnchantment(Enchantment.KNOCKBACK, 4);
		
		// set processor game instance
		//
		game_processor.setGame(this);
	}
	
	private void savePlayerInventory(UUID uuid) throws NoSuchElementException
	{
		Map.Entry<ItemStack[], ItemStack[]> player_inv = players_inv.getOrDefault(uuid, null);
		if ( player_inv == null )
			throw new NoSuchElementException();
		
		// reset player inventory and armor
		//
		Player player = players_list.get(uuid);
		for ( int i = 0; i < 35; i++ )
			player.getInventory().setItem(i, null);
		player.getInventory().setArmorContents(null);
		
		player.getInventory().setContents(player_inv.getKey());
		player.getInventory().setArmorContents(player_inv.getValue());
	}
	
	private void loadPlayerInventory(UUID uuid) throws KeyAlreadyExistsException, NoSuchElementException
	{
		if ( players_inv.containsKey(uuid) )
			throw new KeyAlreadyExistsException();
		
		Player player = getPlayer(uuid);
		if ( player == null )
			throw new NoSuchElementException();
		
		players_inv.put(uuid, new AbstractMap.SimpleEntry<ItemStack[], ItemStack[]>(player.getInventory().getContents(), player.getInventory().getArmorContents()));
	}
	
	public Player getPlayer(UUID uuid)
	{
		return players_list.getOrDefault(uuid, null);
	}
	
	public void addPlayer(@NotNull Player player) throws KeyAlreadyExistsException
	{
		UUID uuid = player.getUniqueId();
		if ( players_list.containsKey(uuid) )
			throw new KeyAlreadyExistsException();
		
		players_list.put(uuid, player);
	}
	
	public void removePlayer(@NotNull UUID uuid) throws NoSuchElementException
	{
		if ( !players_list.containsKey(uuid) )
			throw new NoSuchElementException();
		
		savePlayerInventory(uuid);
		players_list.remove(uuid);
	}
	
	public boolean stop()
	{
		if ( !is_running )
			return false;
		
		if ( !game_processor.isCancelled() )
			game_processor.getTask().cancel();
		
		is_running = false;
		
		for ( Iterator<Map.Entry<UUID, Player>> iterator = players_list.entrySet().iterator(); iterator.hasNext(); )
		{
			Map.Entry<UUID, Player> entry = iterator.next();
			
			Player player = entry.getValue();
			
			// reset player inventory and armor
			//
			for ( int i = 0; i < 35; i++ )
				player.getInventory().setItem(i, null);
			player.getInventory().setArmorContents(null);
			
			if ( entry.getKey() == catcher_uuid )
				player.getInventory().setItemInMainHand(null);
			
			removePlayer(entry.getKey());
		}
		
		return true;
	}
	
	public boolean start(UUID catcher_uuid)
	{
		if ( is_running || players_list.get(catcher_uuid) == null )
			return false;
		
		is_running = true;
		this.catcher_uuid = catcher_uuid;
		players_list.forEach((UUID uuid, Player player) ->
		{
			loadPlayerInventory(uuid);
			
			// reset player inventory and armor
			//
			for ( int i = 0; i < 35; i++ )
				player.getInventory().setItem(i, null);
			player.getInventory().setArmorContents(null);
			
			if ( uuid == catcher_uuid )
				player.getInventory().setItemInMainHand(item);
		});
		
		game_processor.runTaskLater(Main.getInstance(), timer * 20L);
		
		return true;
	}
	
	public boolean isCatcher(UUID uuid)
	{
		return this.catcher_uuid == uuid;
	}
}
