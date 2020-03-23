
public class playerAvgRankWrapper {
	Player player;
	public playerAvgRankWrapper(Player player) {
		this.player = player;
	}
	public Player getPlayer() {
		return player;
	}
	public String toString() {
		return player.getName() + " - " + player.getAvgRank();
	}
}
