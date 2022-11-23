package tk.m47z.mineTag;

import org.bukkit.scheduler.BukkitRunnable;

public class GameTimer extends BukkitRunnable
{
	private Game game = null;
	
	@Override
	public void run()
	{
		if ( game == null || game.isRunning() )
			return;
		
		try
		{
			game.sendTitle("&6Game Starting", "Game starts in &230&f scs", 2);
			Thread.sleep(1000 * 15);
			game.sendTitle("&6Game Starting", "&215&f scs left", 1.5f);
			Thread.sleep(1000 * 5);
			
			for ( int i = 10; i > 0; i-- )
			{
				game.sendTitle("&6Game Starting", "&2" + i + "&f scs left", 0.5f);
				Thread.sleep(1000);
			}
		}
		catch ( InterruptedException e )
		{
			game.sendTitle("&6Game Starting", "&4Error doing countdown", 1);
		}
		
		if ( !game.start() )
			game.sendTitle("&6Game Starting", "&4Error starting the game", 1);
		else
			game.sendTitle("&6Game Started", "&2GO!", 1);
	}
	
	public BukkitRunnable getTask()
	{
		return this;
	}
	
	public void setGame(Game game)
	{
		this.game = game;
	}
}
