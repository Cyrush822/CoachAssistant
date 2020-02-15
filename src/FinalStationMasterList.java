import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.ArrayList;

import javax.swing.JOptionPane;

public class FinalStationMasterList {
	private File dir;
	private ArrayList<FinalStation> finalStations;
	private MasterPlayerList playerList;

	private ArrayList<Player> players;// players that are not absent, taken from playerList
	private ArrayList<Player> availablePlayers;// players that are both not absent and available
	// (not being used for some station)

//	public static void main(String[] args) {
//		FinalStationMasterList FSML = new FinalStationMasterList(new File("text"), new MasterPlayerList(new File("players")));
//		System.out.println();
//	}
	public FinalStationMasterList(MasterPlayerList playerList) {
		finalStations = new ArrayList<FinalStation>();
		players = playerList.getPlayerList();
		availablePlayers = new ArrayList<Player>();
		this.playerList = playerList;
	}

	public FinalStationMasterList(File directory, MasterPlayerList playerList) {
		this.dir = directory;
		this.playerList = playerList;
		players = playerList.getPlayerList();
		availablePlayers = new ArrayList<Player>();
		if (!dir.exists()) {
			dir.mkdir();
		}
		finalStations = new ArrayList<FinalStation>();
		for (File file : dir.listFiles()) {
			FinalStation fStation = deserializeStation(file);
			finalStations.add(fStation);
		}
	}

	public void updateAvailablePlayers() {
		ArrayList<Player> players = this.players;
		ArrayList<Player> usedPlayers = new ArrayList<Player>();
		for (int i = 0; i < finalStations.size(); i++) {
			ArrayList<Player> used = finalStations.get(i).getCurrentPlayers();
			for (Player player : used) {
				usedPlayers.add(player);
			}
		}

		ArrayList<Player> finalPlayers = new ArrayList<Player>();
		for (Player player : players) {
			if (!usedPlayers.contains(player)) {
				finalPlayers.add(player);
			}
		}
		availablePlayers = finalPlayers;
	}

	public ArrayList<Player> getRankDifferenceList(FinalStation target) {
		// takes the all player included and returns suitable candidates
		// of similar rank to every player, the merges them to find similarities.
		// if there are none, gradually decreases the number of players compared.
		updateAvailablePlayers();
		if (target.getCurrentPlayers().isEmpty()) {
			return availablePlayers;
		}
		ArrayList<ArrayList<Player>> playerLists = new ArrayList<ArrayList<Player>>();
		for (Player player : target.getCurrentPlayers()) {
			playerLists.add(getPlayersOfSimilarRanks(player, target.getStation().getMaxRankDifference()));
		}
		ArrayList<Player> finalList = mergePlayerLists(playerLists);
		if(finalList != null || playerLists != null) {
			while (finalList.isEmpty() && (playerLists.size() > 1)) {
				playerLists.remove(playerLists.size() - 1);
				finalList = mergePlayerLists(playerLists);
			}
		} else {
			finalList = new ArrayList<Player>();
		}
		return finalList;
	}
	public void generateStations() {
		int attempts = 0;
		
		while(attempts < Math.pow(finalStations.size(), 3)) {
			for(FinalStation station : finalStations) {
				station.clearPlayers();
				station.calculatePriority();
			}
			reorderStationsByPriority();
			printStationsAndPriority();
			for(int i = 0; i <finalStations.size(); i++) {
				if(!fillStationToMin(finalStations.get(i))) {
					break;
				}
			}
//			if(allStationsAreAtMin()) {
//				if(this.getAvailablePlayers().size() > 0) {
//					if(!useUpRemainingCandidates()) {
//						attempts++;
//						continue;
//					} else {
//						break;
//					}
//				} 
//			} else {
//				attempts++;
//			}
			if(allStationsAreAtMin()) {
				break;
			}
			attempts++;
		}
		
//		if(this.getAvailablePlayers().size() > 0) {
//			JOptionPane.showMessageDialog(null, "There was an error likely due to too many parameters! "
//					+ "Please try again. If that doesn't work, "
//					+ "try decreasing the number of conditions and parameters!", "error", 0);
//		}
	}
	private boolean useUpRemainingCandidates() {
		for(int i = 0; i < this.availablePlayers.size(); i++) {//try to fit one where it is preferred
			int listNumber = 0;
			boolean success = false;
			while(listNumber < finalStations.get(0).getCandidateLists().size()) {
				for(FinalStation station : finalStations) {
					if(!station.satisfiedPreferredNumberOfPlayers() && station.getCandidateLists().get(listNumber).contains(this.availablePlayers.get(i))) {
						station.addPlayer(this.availablePlayers.get(i));
						success = true;
					}
				}
				if(!success) {
					listNumber++;
					continue;
				} else {
					break;
				}
			}
			if(!success) {//try to fit a station that has not been maxed yet. 
				listNumber = 0;
				success = false;
				while(listNumber < finalStations.get(0).getCandidateLists().size()) {
					for(FinalStation station : finalStations) {
						if(!station.isFull() && station.getCandidateLists().get(listNumber).contains(this.getAvailablePlayers())) {
							station.addPlayer(this.availablePlayers.get(i));
							success = true;
						}
					}
					if(!success) {
						listNumber++;
						continue;
					} else {
						break;
					}
				}
			}
			if(!success) {
				return false;
			}
		}
		return true;
	}
	private boolean allStationsAreAtPref() {
		for(FinalStation station : finalStations) {
			if(!station.satisfiedPreferredNumberOfPlayers())
				return false;
		}
		return true;
	}
	private boolean allStationsAreAtMin() {
		for(FinalStation station : finalStations) {
			if(!station.satisfiedMinimum())
				return false;
		}
		return true;
	}
	private void clearStations() {
		for(FinalStation station : finalStations) {
			station.clearPlayers();
			station.calculatePriority();
		}
	}
	private boolean fillStationToMin(FinalStation station) {//returns successful or not
		if(station.isFull()) {
			return true;
		}
		int attempts = 0;
		while(attempts < Math.pow(station.getStation().getMinPlayers(), 2) ) {
			ArrayList<Player> targetList = new ArrayList<Player>();
			station.calculateLists();
			for(int i = 0; i < station.getCandidateLists().size(); i++) {
				if(station.getCandidateLists().get(i).size() > 0) {
					targetList = station.getCandidateLists().get(i);
					break;
				}
			}
			if(targetList.isEmpty()) {
				attempts++;
				station.clearPlayers();
				continue;
			} else {
				addRandomCandidate(station, targetList);
				if(station.satisfiedMinimum()) {
					break;
				} else {
					continue;
				}
			}
		}
		if(station.satisfiedMinimum()) {
			return true;
		}
		return false;
	}
	private void printStationsAndPriority() {
		for(int i = 0; i < finalStations.size(); i++) {
			System.out.println(finalStations.get(i) + Integer.toString(finalStations.get(i).getPriority()));
		}
	}
	public void reorderStationsByPriority() {
		for(FinalStation station : finalStations) {
			station.calculatePriority();
		}
		for(int i = finalStations.size(); i > 0; i--) {
			for(int a = 0; a < i - 1; a++) {
				if(finalStations.get(a).getPriority() < finalStations.get(a + 1).getPriority()) {
					FinalStation holder = finalStations.get(a+1);
					finalStations.set(a + 1, finalStations.get(a));
					finalStations.set(a, holder);
				}
			}
		}
	}
	public boolean addCandidates(FinalStation station, int listNumber, int numberOfCandidates) {
		for(int i = 0; i < numberOfCandidates; i++) {
			if(!addRandomCandidate(station, station.getCandidateLists().get(listNumber))) {
				return false;
			} else {
				station.calculateLists();
			}
		}
		return true;
	}
	public boolean addRandomCandidate(FinalStation station, ArrayList<Player> candidates) {
		if(candidates.size() <= 0) {
			return false;
		}
		int index = (int) (candidates.size() * Math.random());
		station.addPlayer(candidates.get(index));
		return true;
	}
	/**
	 * returns all the players that are not in this array
	 * @param players
	 * @return
	 */
	public ArrayList<Player> invertPlayerList(ArrayList<Player> players) {
		ArrayList<Player> finalList = new ArrayList<Player>();
		for(int i = 0; i < availablePlayers.size(); i++) {
			if(!(players.contains(availablePlayers.get(i)))) {
				finalList.add(availablePlayers.get(i));
			}
		}
		return finalList;
	}

	public ArrayList<Player> getGenderDifferenceList(FinalStation target) {
		updateAvailablePlayers();
		if (target.getCurrentPlayers().isEmpty()) {
			return availablePlayers;

		}
		if (target.getCurrentPlayers().size() >= 2) {
			boolean maleExists = false;
			boolean femaleExists = false;
			for (int i = 0; i < target.getCurrentPlayers().size(); i++) {
				if (target.getCurrentPlayers().get(i).getIsMale()) {
					maleExists = true;
				} else {
					femaleExists = true;
				}
			}
			if (maleExists && femaleExists) {
				return availablePlayers;
			}
		}
		boolean genderIsMale = target.getCurrentPlayers().get(0).getIsMale();
		ArrayList<Player> candidates = new ArrayList<Player>();
		for (int i = 0; i < availablePlayers.size(); i++) {
			if (availablePlayers.get(i).getIsMale() == genderIsMale) {
				candidates.add(availablePlayers.get(i));
			}
		}
		if (target.getStation().getGenderPref().equals(MasterStationList.genderPreference.prioritize)
				|| target.getStation().getGenderPref().equals(MasterStationList.genderPreference.stronglyprioritize)
				|| target.getStation().getGenderPref().equals(MasterStationList.genderPreference.require)) {
			candidates = invertPlayerList(candidates);
		}
		return candidates;
	}

	public ArrayList<Player> getRankPreferenceList(FinalStation target) {
		updateAvailablePlayers();
		if(target.getStation().getRankPref().equals(MasterStationList.rankPreference.allow)) {
			return availablePlayers;
		}
		int max = target.getStation().getRankMax();
		int min = target.getStation().getRankMin();
		ArrayList<Player> candidates = new ArrayList<Player>();
		for (int i = 0; i < availablePlayers.size(); i++) {
			if (availablePlayers.get(i).getRank() >= min && availablePlayers.get(i).getRank() <= max) {
				candidates.add(availablePlayers.get(i));
			}
		}
		if(target.getStation().getRankPref().equals(MasterStationList.rankPreference.avoid) ||
				target.getStation().getRankPref().equals(MasterStationList.rankPreference.stronglyavoid) ||
						target.getStation().getRankPref().equals(MasterStationList.rankPreference.restrict)) {
					candidates = invertPlayerList(candidates);
				}
		return candidates;
	}

	public ArrayList<Player> getPlayersOfSimilarRanks(Player player, int range) {
		ArrayList<Player> suitablePlayers = new ArrayList<Player>();
		for (Player targetPlayer : availablePlayers) {
			if (Math.abs(targetPlayer.getRank() - player.getRank()) <= range) {
				suitablePlayers.add(targetPlayer);
			}
		}
		return suitablePlayers;
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
			return new ArrayList<Player>();
		} else if (lists.size() == 1) {
			return lists.get(0);
		}
		ArrayList<Player> finalPlayers = lists.get(0);
		for (int i = 1; i < lists.size(); i++) {
			finalPlayers = mergePlayerLists(finalPlayers, lists.get(i));
		}
		return finalPlayers;
	}

	public void addStation(FinalStation fStation) {
		finalStations.add(fStation);
	}

	public void getStation(int index) {
		finalStations.get(index);
	}

	public FinalStation deserializeStation(File filename) {
		try {
			// Reading the object from a file
			FileInputStream file = new FileInputStream(filename);
			ObjectInputStream in = new ObjectInputStream(file);

			// Method for deserialization of object
			FinalStation station = (FinalStation) in.readObject();

			in.close();
			file.close();

			System.out.println("Object has been deserialized ");
			return station;
		}

		catch (IOException ex) {
			System.out.println("IOException is caught");
			return null;
		}

		catch (ClassNotFoundException ex) {
			System.out.println("ClassNotFoundException is caught");
			return null;
		}
	}

	public File getDir() {
		return dir;
	}

	public void setDir(File dir) {
		this.dir = dir;
	}

	public ArrayList<FinalStation> getFinalStations() {
		this.reorderStationsByPriority();
		return finalStations;
	}

	public void setFinalStations(ArrayList<FinalStation> finalStations) {
		this.finalStations = finalStations;
	}

	public MasterPlayerList getPlayerList() {
		return playerList;
	}

	public void setPlayerList(MasterPlayerList playerList) {
		this.playerList = playerList;
	}

	public ArrayList<Player> getPlayers() {
		return players;
	}

	public void setPlayers(ArrayList<Player> players) {
		this.players = players;
	}

	public ArrayList<Player> getAvailablePlayers() {
		updateAvailablePlayers();
		sortPlayersByRank(availablePlayers);
		return availablePlayers;
	}
	public void sortPlayersByRank(ArrayList<Player> players) {
		for(int i = players.size(); i > 0; i--) {
			for(int a = 0; a < i - 1; a++) {
				if(players.get(a).getRank() > players.get(a+1).getRank()) {
					Player holder = players.get(a);
					players.set(a, players.get(a+1));
					players.set(a+1, holder);
				}
			}
		}
	}
	public void setAvailablePlayers(ArrayList<Player> availablePlayers) {
		this.availablePlayers = availablePlayers;
	}
}
