package tk.m47z.mineTag;

import org.bukkit.scheduler.BukkitRunnable;

public class GameProcessor extends BukkitRunnable
{
	private Game game = null;
	
	@Override
	public void run()
	{
		if ( game == null )
			return;
		
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
