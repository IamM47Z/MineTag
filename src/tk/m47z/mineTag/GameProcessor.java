package tk.m47z.mineTag;

import org.bukkit.scheduler.BukkitRunnable;

public class GameProcessor extends BukkitRunnable
{
	private Game game = null;
	
	@Override
	public void run()
	{
		if ( game == null || !game.isRunning() )
			return;
		
		try
		{
			game.sendTitle("&6Game Ending", "Game ends in &25&f scs", 0.5f);
			Thread.sleep(1000);
			
			for ( int i = 4; i > 0; i-- )
			{
				game.sendTitle("&6Game Ending", "&2" + i + "&f scs left", 0.5f);
				Thread.sleep(1000);
			}
		}
		catch ( InterruptedException e )
		{
			game.sendTitle("&6Game Ending", "&4Error doing countdown", 1);
		}
		
		game.stop();
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
