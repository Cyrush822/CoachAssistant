
public class PartneredPlayer {

	Player player;
	Player partner;
	public PartneredPlayer(Player player) {
		this.player = player;
		this.partner = player.getPartner();
	}
	public Player getPlayer() {
		return player;
	}
	public void setPartner(Player player) {
		if(this.hasPartner()) {
			partner.deletePartner();
			partner = player;
			this.player.setPartner(player);
		}
		else {
			partner = player;
			this.player.setPartner(player);
		}
	}
	public void removePartner() {
		if(this.hasPartner()) {
			partner= null;
			player.deletePartner();
		}
	}
	public void updatePartnerStatus() {
		partner = player.getPartner();
	}
	public boolean hasPartner() {
		return partner != null;
	}
	public String toString() {
		if(player.getPartner() != null) {
			return player.getName() + " - " + player.getPartner().getName();
		}
		return player.getName();
	}
}
