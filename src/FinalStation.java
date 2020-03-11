/**
 * wrapper class for stations, to use for randomly generating players inside.
 * @author Cyrus2021262
 *
 */
import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.awt.*; 
public class FinalStation {
	
	private Station station;
	private FinalStationMasterList pastList;
	private int priority;
	private ArrayList<ArrayList<Player>> candidateLists;
	private ArrayList<Player> manuallyAddedPlayers;
	private ArrayList<Player> currentPlayers;
	private MasterPlayerList playerList;
	private FinalStationMasterList finalStationList;
	public FinalStation(Station station, MasterPlayerList playerList, FinalStationMasterList finalStationList) {//without previous save
		this.station = station;
		this.playerList = playerList;
		this.finalStationList = finalStationList;
		this.manuallyAddedPlayers = new ArrayList<Player>();
		this.currentPlayers = new ArrayList<Player>();
	}
	
//	public FinalStation(Station station, MasterPlayerList playerList, FinalStationMasterList pastList) {//with previous save
//		this.station = station;
//		this.playerList = playerList;
//		this.pastList = pastList;
//	}

	public void manualAddPlayer(Player player) {
		manuallyAddedPlayers.add(player);
		currentPlayers.add(player);
	}
	public void manualRemovePlayer(Player player) {
		manuallyAddedPlayers.remove(player);
		currentPlayers.remove(player);
	}
	public void addPlayer(Player player) {
		currentPlayers.add(player);
	}
	public void removePlayer(Player player) {
		currentPlayers.remove(player);
	}
	public void clearPlayers() {
		currentPlayers = new ArrayList<Player>();
		for(Player player : manuallyAddedPlayers) {
			currentPlayers.add(player);
		}
	}
	public void trueClearPlayers() {
		manuallyAddedPlayers = new ArrayList<Player>();
		clearPlayers();
	}
	public void calculatePriority() {
		int priority = 0;
		priority += station.getMinPlayers();
		if(station.isMustBeEven()) {
			priority += 1;
		}
		if(station.isRanked()) {
			int increase = MasterPlayerList.playerNumber/2 - station.getMaxRankDifference();
			if(increase > 0) {
				priority += increase;
			}
		}
		if(station.getGenderPref().equals(MasterStationList.genderPreference.avoid) || (station.getGenderPref().equals(MasterStationList.genderPreference.prioritize))) {
			priority += 5;
		} else if (station.getGenderPref().equals(MasterStationList.genderPreference.stronglyavoid) || (station.getGenderPref().equals(MasterStationList.genderPreference.stronglyprioritize))){
			priority += 10;
		} else if (station.getGenderPref().equals(MasterStationList.genderPreference.require) || (station.getGenderPref().equals(MasterStationList.genderPreference.restrict))) {
			priority += 20;
		}
		int rankRange = station.getRankMax() - station.getRankMin() + 1 ;//inclusive
		if(station.getRankPref().equals(MasterStationList.rankPreference.avoid)) {
			priority += rankRange/2;
		} else if(station.getRankPref().equals(MasterStationList.rankPreference.stronglyavoid)) {
			priority += rankRange;
		} else if(station.getRankPref().equals(MasterStationList.rankPreference.restrict)) {
			priority += Math.pow(rankRange/2, 2);
		}
		if(station.getRankPref().equals(MasterStationList.rankPreference.prioritize)) {
			priority += (MasterPlayerList.playerNumber - rankRange)/2;
		} else if(station.getRankPref().equals(MasterStationList.rankPreference.stronglyprioritize)) {
			priority += (MasterPlayerList.playerNumber - rankRange);
		} else if(station.getRankPref().equals(MasterStationList.rankPreference.stronglyprioritize)) {
			priority += Math.pow(MasterPlayerList.playerNumber - rankRange/2, 2);
		}
		if(station.getCompType().equals(MasterStationList.competitiveType.doubles)) {
			priority += 20;
		} else if(station.getCompType().equals(MasterStationList.competitiveType.singles)) {
			priority += 10;
		}
		this.priority = priority;
	}
	public void printLists() {
		System.out.println(this.station.getStationName()); 
		for(int i = 0; i < candidateLists.size(); i++) {
			System.out.println(this.candidateLists.get(i));
		}
	}
	public void calculateLists() {//0 is most preferable (least # of candidates) and 4 is least preferable
		candidateLists = new ArrayList<ArrayList<Player>>();
		ArrayList<Player> candidatesForRankDifference = finalStationList.getRankDifferenceList(this);
		System.out.println(this.station.getStationName() + "candrank: " + candidatesForRankDifference);
		ArrayList<Player> candidatesForGenderDifference = finalStationList.getGenderDifferenceList(this);
		System.out.println(this.station.getStationName() + "candGender: " + candidatesForGenderDifference);
		ArrayList<Player> candidatesForRankPreference = finalStationList.getRankPreferenceList(this);
		System.out.println(this.station.getStationName() + "candrankpref: " + candidatesForRankPreference);
		for(int i = 0; i < 6;i++) {
			ArrayList<ArrayList<Player>> playerListsToMerge = new ArrayList<ArrayList<Player>>();
			playerListsToMerge.add(finalStationList.getAvailablePlayers());
			switch(i)  {
			case 0:
				if(this.station.getGenderPrefImportance() == 1) {
					playerListsToMerge.add(candidatesForGenderDifference);
				}
			case 1:
				if(this.station.getRankPreferenceImportance() == 1) {
					playerListsToMerge.add(candidatesForRankPreference);
				}
			case 2:
				if(this.station.getGenderPrefImportance() == 2) {
					playerListsToMerge.add(candidatesForGenderDifference);
				}
			case 3:
				if(this.station.getRankPreferenceImportance() == 2) {
					playerListsToMerge.add(candidatesForRankPreference);
				}
			case 5:
				//add previous save here
			case 4: {
				if(this.station.getCompType() != MasterStationList.competitiveType.notCompetitive) {
					playerListsToMerge.add(candidatesForRankDifference);
				}
				if(this.station.getRankPreferenceImportance() == 3) {
					playerListsToMerge.add(candidatesForRankPreference);
				}
				if(this.station.getGenderPrefImportance() == 3) {
					playerListsToMerge.add(candidatesForGenderDifference);
				}
				break;
			}
			default: {
				break;
			}
			}
			candidateLists.add(i, mergePlayerLists(playerListsToMerge));
		}
	}
	public Player getRandomPlayerFromList(ArrayList<Player> playerList) {
		int num = (int) (Math.random() * playerList.size());
		return playerList.get(num);
	}
	public ArrayList<Player> mergePlayerLists(ArrayList<Player> first, ArrayList<Player> second) {
		// combines two playerLists so that only the players in both are returned.
		ArrayList<Player> finalPlayers = new ArrayList<Player>();
		for (Player player : first) {
			if (second.contains(player)) {
				finalPlayers.add(player);
			}
		}
		return finalPlayers;
	}

	public ArrayList<Player> mergePlayerLists(ArrayList<ArrayList<Player>> lists) {
		if (lists.isEmpty()) {
			return null;
		} else if (lists.size() == 1) {
			return lists.get(0);
		}
		ArrayList<Player> finalPlayers = lists.get(0);
		for (int i = 1; i < lists.size(); i++) {
			finalPlayers = mergePlayerLists(finalPlayers, lists.get(i));
		}
		return finalPlayers;
	}
	public boolean satisfiedMinimum() {
		if(currentPlayers.size() >= station.getMinPlayers()) {
			return true;
		}
		return false;
	}
	public boolean satisfiedPreferredNumberOfPlayers() {
		if(currentPlayers.size() >= station.getPreferredPlayers()) {
			return true;
		}
		return false;
	}
	public boolean isFull() {
		if(currentPlayers.size() >= station.getMaxPlayers()) {
			return true;
		}
		return false;
	}
	public int getPriority() {
		return priority;
	}
	public String toString() {
		String playerNames = "";
		for(int i = 0; i < currentPlayers.size(); i++) {
			if(i == currentPlayers.size() - 1) {
				playerNames += currentPlayers.get(i).getName();
			} else {
				playerNames += currentPlayers.get(i).getName() + ", ";
			}
		}
		return String.format("%s - %s", this.station.getStationName(), playerNames);
	}

	public Station getStation() {
		return station;
	}

	public void setStation(Station station) {
		this.station = station;
	}

	public FinalStationMasterList getPastList() {
		return pastList;
	}

	public void setPastList(FinalStationMasterList pastList) {
		this.pastList = pastList;
	}

	public ArrayList<ArrayList<Player>> getCandidateLists() {
		return candidateLists;
	}

	public void setCandidateLists(ArrayList<ArrayList<Player>> candidateLists) {
		this.candidateLists = candidateLists;
	}

	public ArrayList<Player> getCurrentPlayers() {
		return currentPlayers;
	}

	public void setCurrentPlayers(ArrayList<Player> currentPlayers) {
		this.currentPlayers = currentPlayers;
	}

	public MasterPlayerList getPlayerList() {
		return playerList;
	}

	public void setPlayerList(MasterPlayerList playerList) {
		this.playerList = playerList;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	public ArrayList<Player> getManuallyAddedPlayers() {
		return manuallyAddedPlayers;
	}

	public void setManuallyAddedPlayers(ArrayList<Player> manuallyAddedPlayers) {
		this.manuallyAddedPlayers = manuallyAddedPlayers;
	}

	public FinalStationMasterList getFinalStationList() {
		return finalStationList;
	}

	public void setFinalStationList(FinalStationMasterList finalStationList) {
		this.finalStationList = finalStationList;
	}
	
}
