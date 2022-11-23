package tk.m47z.mineTag;

import java.util.*;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import javax.management.openmbean.KeyAlreadyExistsException;

public class Game
{
	private int duration = 60;
	private UUID catcher_uuid = null;
	private UUID last_catcher_uuid = null;
	private boolean is_running = false;
	private final GameTimer game_timer = new GameTimer();
	private final GameProcessor game_processor = new GameProcessor();
	private final Map<UUID, Map.Entry<ItemStack[], ItemStack[]>> players_inv = new HashMap<UUID, Map.Entry<ItemStack[], ItemStack[]>>();
	private final Map<UUID, Player> players_list = new HashMap<UUID, Player>();
	private final ItemStack item = new ItemStack(Material.BAKED_POTATO, 1);
	
	public Game(UUID catcher_uuid)
	{
		this.catcher_uuid = catcher_uuid;
		
		// create item
		//
		item.addUnsafeEnchantment(Enchantment.FIRE_ASPECT, 4);
		item.addUnsafeEnchantment(Enchantment.KNOCKBACK, 4);
		
		// set game instance in runnables
		//
		game_timer.setGame(this);
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
	
	public void sendTitle(String string1, String string2, float time)
	{
		players_list.forEach((uuid, player) ->
		{
			player.sendTitle(Utils.chat(string1), Utils.chat(string2), 1, ( int ) ( time * 20 ), 10);
		});
	}
	
	public void removePlayer(@NotNull UUID uuid) throws NoSuchElementException
	{
		if ( !players_list.containsKey(uuid) )
			throw new NoSuchElementException();
		
		players_list.remove(uuid);
	}
	
	public boolean stop()
	{
		if ( !is_running )
			return false;
		
		if ( !game_processor.isCancelled() )
			game_processor.getTask().cancel();
		
		is_running = false;
		
		for ( Map.Entry<UUID, Player> entry : players_list.entrySet() )
		{
			UUID uuid = entry.getKey();
			Player player = entry.getValue();
			
			if ( entry.getKey() == catcher_uuid )
			{
				player.getInventory().setItemInMainHand(null);
				player.removePotionEffect(PotionEffectType.SPEED);
			}
			
			// reset player inventory and armor
			//
			for ( int i = 0; i < 35; i++ )
				player.getInventory().setItem(i, null);
			player.getInventory().setArmorContents(null);
			
			player.setCanPickupItems(true);
			player.setFoodLevel(20);
			player.setHealth(20);
			
			savePlayerInventory(uuid);
		}
		
		players_list.clear();
		
		return true;
	}
	
	public boolean start()
	{
		if ( is_running || players_list.get(catcher_uuid) == null )
			return false;
		
		is_running = true;
		players_list.forEach((UUID uuid, Player player) ->
		{
			loadPlayerInventory(uuid);
			
			// reset player inventory and armor
			//
			for ( int i = 0; i < 35; i++ )
				player.getInventory().setItem(i, null);
			player.getInventory().setArmorContents(null);
			
			player.setFoodLevel(20);
			player.setHealth(20);
			
			// add saturation
			//
			PotionEffect effect1 = null;
			PotionEffect effect = new PotionEffect(PotionEffectType.SATURATION, duration * 20, 999);
			if ( uuid == catcher_uuid )
			{
				player.getInventory().setItemInMainHand(item);
				
				// add speed
				//
				effect1 = new PotionEffect(PotionEffectType.SPEED, duration * 20, 4);
			}
			
			final PotionEffect finalEffect = effect1;
			new BukkitRunnable()
			{
				@Override
				public void run()
				{
					player.addPotionEffect(effect);
					if ( finalEffect != null )
						player.addPotionEffect(finalEffect);
				}
			}.runTask(Main.getInstance());
		});
		
		game_processor.runTaskLaterAsynchronously(Main.getInstance(), ( duration - 5 ) * 20L);
		
		return true;
	}
	
	public boolean free()
	{
		if ( is_running )
			return stop();
		
		if ( !game_timer.isCancelled() )
			game_timer.getTask().cancel();
		
		if ( !game_processor.isCancelled() )
			game_processor.getTask().cancel();
		
		sendTitle("&6Game Ended", "&2Game &4Finished&f", 1);
		return true;
	}
	
	public void schedule()
	{
		game_timer.runTaskAsynchronously(Main.getInstance());
	}
	
	public boolean isRunning()
	{
		return this.is_running;
	}
	
	public boolean isCatcher(UUID uuid)
	{
		return this.catcher_uuid == uuid;
	}
	
	public void changeCatcher(UUID new_uuid)
	{
		last_catcher_uuid = catcher_uuid;
		catcher_uuid = new_uuid;
		
		Player old_player = getPlayer(last_catcher_uuid);
		Player player = getPlayer(catcher_uuid);
		
		old_player.getInventory().setItemInMainHand(null);
		old_player.removePotionEffect(PotionEffectType.SPEED);
		
		// reset player inventory and armor
		//
		for ( int i = 0; i < 35; i++ )
			old_player.getInventory().setItem(i, null);
		old_player.getInventory().setArmorContents(null);
		
		player.getInventory().setItemInMainHand(item);
		
		// add speed
		//
		PotionEffect effect = new PotionEffect(PotionEffectType.SPEED, duration * 20, 4);
		PotionEffect old_effect = new PotionEffect(PotionEffectType.SPEED, 5 * 20, 8);
		new BukkitRunnable()
		{
			@Override
			public void run()
			{
				player.addPotionEffect(effect);
				old_player.addPotionEffect(old_effect);
			}
		}.runTask(Main.getInstance());
		
		sendTitle("&6New Catcher", "&1" + player.getDisplayName() + "&f is the new Catcher!", 2);
	}
}
