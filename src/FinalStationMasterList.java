import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

import javax.swing.JOptionPane;
import java.time.*;

public class FinalStationMasterList implements Serializable{
	private File dir;
	private File thisFile;
	private File associatedWordDoc;
	private ArrayList<FinalStation> finalStations;
	private String fileName = "savedConfig";
	private String savedConfigsDirName = "savedConfigs";
	
	private LocalTime savedTime;
	private LocalDate savedDate;
	private MasterPlayerList playerList;
	transient private SavedSettings settings;
	private ArrayList<Player> players;// players that are not absent, taken from playerList
	private ArrayList<Player> availablePlayers;// players that are both not absent and available
	transient private ArrayList<FinalStationMasterList> oldConfigs;
	// (not being used for some station)

//	public static void main(String[] args) {
//		FinalStationMasterList FSML = new FinalStationMasterList(new File("text"), new MasterPlayerList(new File("players")));
//		System.out.println();
//	}
	public FinalStationMasterList(MasterPlayerList playerList) {
		this.dir = new File("savedConfigs");
		finalStations = new ArrayList<FinalStation>();
		players = playerList.getPresentPlayerList();
		availablePlayers = new ArrayList<Player>();
		this.playerList = playerList;
	}

	public FinalStationMasterList(File directory, MasterPlayerList playerList) {
		this.dir = directory;
		this.playerList = playerList;
		players = playerList.getPresentPlayerList();
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
	public int getGenerateStationsMaxAttempts() {
		return (int)(Math.pow(finalStations.size(), 2) * (int)Math.pow(settings.configsSaved,2) * settings.getTriesMultiplier());
	}
	public int getFillStationsToMinMaxAttempts(FinalStation station) {
		return (int)Math.pow(station.getStation().getMinPlayers(), 3) * settings.configsSaved;
	}
	public int getAddRandomCandidateMaxAttempts(ArrayList<Player> candidates) {
		return (int)Math.pow(candidates.size(), 3);
	}
	/*
	 * The master method to generate stations. Automatically fills every station in 
	 * finalStations with players according to their configurations.
	 * However, very random and unoptimized... Will not work if there's too many configs,
	 * and relies heavily on luck and tons of tries to get one config that fits all 
	 * required parameters.
	 * Logic:
	 * 1. Calculate maxAttempts according to finalStations size (larger = higher)
	 * 2. While attempts below MaxAttempts
	 * 	a.) Calculate priority for each station and sort
	 *  b.) Attempt to fill each station to their MINIMUM number of players required
	 *  	IF RETURNS TRUE (it succeeded), continue.
	 *  	IF RETURNS FALSE (it failed. Players don't fit required parameters), break.
	 *  c.) First, check if every station has minimum players
	 *  	IF NOT: (b returned false) break and add 1 to attempts
	 *  	IF YES: (b returned true), continue:
	 *  d.) are there are still players left to assign?
	 *  	IF YES: call useUpRemaining candidates to see if we can successfully add 
	 *  			all remaining players into stations until all players are gone
	 *  			Store whether it worked or not in successful. 
	 *  	IF NOT: break (SUCCESS!!!)
	 *  e.) Was using up remaining candidates successful?
	 *  	IF YES: break (SUCCESS!!!)
	 *  	IF NOT: continue and add 1 to attempts
	 *  
	 *  
	 *  f.) If by the end of this (attempts exceeded max attempts), there are still students
	 *  	Display error message.
	 * TODO: refine process for calculating maxAttempts so it calculates according to
	 * How many paramters, not stations.
	 */
	public void generateStations() {
		this.settings = this.deserializeSettings(new File(Frame3.settingsFileName));
		this.oldConfigs = this.getOldConfigs();
		int attempts = 0;
		int maxAttempts = this.getGenerateStationsMaxAttempts();
		while(attempts < maxAttempts) {
			for(FinalStation station : finalStations) {
				station.clearPlayers();
				station.calculatePriority();
			}
			reorderStationsByPriority();
			//printStationsAndPriority();
			for(int i = 0; i < finalStations.size(); i++) {
				if(!fillStationToMin(finalStations.get(i))) {
					break;
				}
			}
			if(allStationsAreAtMin()) {//false = failed to fill each station to minimum number of players
				if(this.getAvailablePlayers().size() > 0) {
					boolean successful = useUpRemainingCandidates();
					if(successful) {
						break;
					} else {
						attempts++;
						continue;
					}
				} else {
					break;
				}
			} else {
				attempts++;
				continue;
			}
		}
		
		if(this.getAvailablePlayers().size() > 0) {
			JOptionPane.showMessageDialog(null, "<HTML>There was an error likely due to too many parameters! <br>"
					+ "Please try again. If that doesn't work, <br>"
					+ "try decreasing the number of conditions and parameters!<br>"
					+ "Also ensure that parameters do not result in impossible situations <br>"
					+ "(ex. requiring gender differences when there are only males)<br>"
					+ "Alternatively, increase the # of tries multiplier in advanced settings.", "Error", 0);
		}
	}
	private boolean useUpRemainingCandidates() {//ERROR: candidateList size differs. Must account for each
		for(int i = 0; i < this.availablePlayers.size(); i++) {//try to fit one where it is preferred
			int listNumber = 0;
			boolean success = false;
			while(listNumber < finalStations.get(0).getCandidateLists().size()) {
				for(FinalStation station : finalStations) {
					if(!station.satisfiedPreferredNumberOfPlayers() && 
						listNumber < station.getCandidateLists().size() &&
						station.getCandidateLists().get(listNumber).contains(this.availablePlayers.get(i))) {//index out of bounds error
						station.addPlayer(this.availablePlayers.get(i));
						success = true;
						break;
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
						if(!station.isFull() && 
								station.getCandidateLists().get(listNumber).contains(this.availablePlayers.get(i))) {//index out of bounds error
							station.addPlayer(this.availablePlayers.get(i));
							success = true;
							break;
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
		if(this.getAvailablePlayers().size() > 0) {
			return false;
		}
		return true;
	}
	/**
	 * returns whether or not all stations in finalStations are at there preferred number of
	 * players
	 * @return boolean
	 */
	private boolean allStationsAreAtPref() {
		for(FinalStation station : finalStations) {
			if(!station.satisfiedPreferredNumberOfPlayers())
				return false;
		}
		return true;
	}
	/**
	 * returns whether or not all stations in finalStations are at there minimum number of
	 * players
	 * @return boolean
	 */
	private boolean allStationsAreAtMin() {
		for(FinalStation station : finalStations) {
			if(!station.satisfiedMinimum())
				return false;
		}
		return true;
	}
	/**
	 * clear all stations of their current players (BUT NOT MANUALLY ASSIGNED PLAYERS.)
	 */
	private void clearStations() {
		for(FinalStation station : finalStations) {
			station.clearPlayers();
		}
	}
	/**
	 * monster method that takes in a station and fills it up to its minimum number of 
	 * players with available players, according to its configs
	 * 
	 * Will try to use the list of players that is most preferable, and choose random
	 * players from that list.
	 * 
	 * 1.If station is full, return true
	 * 2.Loop while attempts is below maxAttempts
	 * 3.If the station is already satisfied (it's filled to min) , break.
	 * 3.declare list of players that will be used to get random players (targetList)
	 * 4.calculate lists, and the first list that isn't empty (most preferable list) is
	 * 	assigned to targetList by (kinda) reference
	 * 5.If targetList is empty (meaning every single one of the candidate lists (even
	 * were empty, which means there are NO players that fit all of the required parameters)
	 * continue, clear station, and add 1 to attempts.
	 * 6.If the station's minimum number of players is not satisfied, add a random candidate
	 * From targetList into station. ADD RANDOM CANDIDATE AUTOMATICALLY ACCOUNTS FOR PARTNER.
	 * If there's not enough slots for the partner, it will ignore it. If there are slots,
	 * AND they have a partner, AND the partner is available, they will automatically be
	 * added (doesn't care about configs...)
	 * 7.Is the station's minimum number of players satisfied?
	 * YES: break (SUCCESS!!)
	 * NO: check through every player and see, do they have a partner that should be added?
	 * (this is only useful for manually added players, as normal players would've already
	 * have had their partners added in) If so, AND the list is not full AND the player is
	 * available, add that partner. 
	 * continue (go back to the while loop and try to add a new player) no +attempts because there was no fail.
	 * 
	 * AT THE END (either some part of the while loop had a break or if attempts > maxAttempts)
	 * if station is satisfied, return true (success!)
	 * if not, return false (failed)
	 * @param station
	 * @return boolean indicating successfully filled station or not
	 */
	private boolean fillStationToMin(FinalStation station) {//returns successful or not
		if(station.isFull()) {
			return true;
		}
		int attempts = 0;
		int maxAttempts = this.getFillStationsToMinMaxAttempts(station);
		while(attempts <  maxAttempts) {
			if(station.satisfiedMinimum()) {
				break;
			}
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
				if(!station.satisfiedMinimum()) {
					addRandomCandidate(station, targetList);
				}
				if(station.satisfiedMinimum()) {
					break;
				} else {
					if(station.getStation().getCompType().equals(MasterStationList.competitiveType.doubles) && settings.getPartnerSystemOn()) {
						ArrayList<Player> existingPlayers = new ArrayList<Player>();
						for(Player player : station.getCurrentPlayers()) {
							existingPlayers.add(player);
						}
						for(Player player : existingPlayers) {
							if(player.hasPartner()) {
								if(!station.getCurrentPlayers().contains(player.getPartner())) {
									if(this.availablePlayers.contains(player.getPartner()) && station.getCurrentPlayers().size() <= 3) {
										station.addPlayer(player.getPartner());
										updateAvailablePlayers();
									}
								}
							}
						}
					}
				}
			}
			attempts++;
		}
		if(station.satisfiedMinimum()) {
			return true;
		}
		return false;
	}
	public String toString() {
		return "savedConfig" + savedDate.toString() + " " + savedTime.getHour() 
		+ ":" + savedTime.getMinute() + ":" + savedTime.getSecond();
	}
	private void printStationsAndPriority() {
		for(int i = 0; i < finalStations.size(); i++) {
			System.out.println(finalStations.get(i) + Integer.toString(finalStations.get(i).getPriority()));
		}
	}
	/**
	 * sorts stations by priority
	 * currently uses bubble sort
	 */
	public void reorderStationsByPriority() {
//		for(FinalStation station : finalStations) {
//			station.calculatePriority();
//		}
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
	/**
	 * takes a random candidate fromm the candidate list passed in
	 * (chooses randomly until a player that is in "available players" is chosen by chance
	 * Checks if the candidate chosen has a partner. If so:
	 * 1. Is there a slot for them?
	 * 2. are they available?
	 * if both are true, add
	 * if both are not true, don't add and don't use the target candidate.
	 * 
	 * Keep trying until the player is available and has a partner that can be added/has no partner.
	 * If tries exceed 50 (temp number), return false (it failed!)
	 * @param station
	 * @param candidates
	 * @return
	 */
	public boolean addRandomCandidate(FinalStation station, ArrayList<Player> candidates) {
		if(candidates.size() <= 0) {
			return false;
		}
		int index = 99999;
		int attempts = 0;
		int maxAttempts = this.getAddRandomCandidateMaxAttempts(candidates);
		while(attempts < maxAttempts) {
			index = (int) (candidates.size() * Math.random());
			if(!availablePlayers.contains(candidates.get(index))) {
				attempts++;
				continue;
			}
			if(!station.getStation().getCompType().equals(MasterStationList.competitiveType.doubles) || !settings.getPartnerSystemOn()) {
				break;
			}
			//is doubles
			if(candidates.get(index).hasPartner()) {
				if(station.getCurrentPlayers().size() >= 3) {
					attempts++;
					continue;
				}
				if(!this.availablePlayers.contains(candidates.get(index).getPartner())) {
					attempts++;
					continue;
				}
				station.addPlayer(candidates.get(index).getPartner());
				break;
			} else {
				break;
			}
		}
		if(attempts >= 50) {
			return false;
		}
		station.addPlayer(candidates.get(index));
		return true;
	}
	/**
	 * returns all the players that are not in this array using their unique playerID
	 * @param players
	 * @return
	 */
	public ArrayList<Player> invertPlayerList(ArrayList<Player> players) {
		ArrayList<Player> finalList = new ArrayList<Player>();
		for(int i = 0; i < availablePlayers.size(); i++) {
			boolean isInlist = false;
			for(int a = 0; a < players.size(); a++) {
				if(players.get(a).getPlayerID() == availablePlayers.get(i).getPlayerID()) {
					isInlist = true;
				}
			}
			if(!isInlist) {
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
	public ArrayList<FinalStationMasterList> getOldConfigs() {
		ArrayList<FinalStationMasterList> deserializedOldConfigs = new ArrayList<FinalStationMasterList>();
		File oldConfigDir = new File(Frame3.dirName);
		if(!oldConfigDir.exists()) {
			oldConfigDir.mkdir();
			System.out.println("NO OLD CONFIG DIR FOUND. MAKING A NEW ONE");
			return deserializedOldConfigs;
		}
		for(File file : oldConfigDir.listFiles()) {
			deserializedOldConfigs.add(this.deserializeFinalStationMasterList(file));
		}
		return deserializedOldConfigs;
	}
	/**
	 * returns the list of players in previous generations of the station (if they have the same tag)
	 * in order by time (earliest in the front (0))
	 * @param target
	 * @return list of player Lists (candidates)
	 */
	public ArrayList<ArrayList<Player>> getPreviousConfigLists(FinalStation target) {
		ArrayList<ArrayList<Player>> result = new ArrayList<ArrayList<Player>>();
		sortByTime(oldConfigs);
		File settingsFile = new File(Frame3.settingsFileName);
		if(!settingsFile.exists()) {
			return result;
		}
		int configsToSave = settings.getConfigsSaved();
		ArrayList<FinalStationMasterList> oldConfigsToUse = new ArrayList<FinalStationMasterList>();
		if(oldConfigs.size() <= configsToSave) {
			oldConfigsToUse = oldConfigs;
		} else {
			for(int i = configsToSave; i > 0; i--) {
				oldConfigsToUse.add(oldConfigs.get(oldConfigs.size() - i));
			}
		}
		for(FinalStationMasterList config : oldConfigsToUse) {
			for(FinalStation station : config.getFinalStations()) {
				if(station.getStation().getStationDesc().equals(target.getStation().getStationDesc())) {
					result.add(invertPlayerList(station.getCurrentPlayers()));
				}
			}
		}
		return result;
	}
	/**
	 * sorts masterLists from earliest(0) to latest (n - 1)
	 * @param masterStationLists
	 * @return
	 */
	private void sortByTime(ArrayList<FinalStationMasterList> stationLists) {
		for(int i = stationLists.size() - 1; i > 0; i--) {
			for(int a = 0; a < i; a++) {
				if(stationLists.get(a).getSavedDate().isAfter(stationLists.get(a+1).getSavedDate())) {
					FinalStationMasterList holder = stationLists.get(a);
					stationLists.set(a, stationLists.get(a+1));
					stationLists.set(a+1, holder);
				}
			}
		}
		for(int i = stationLists.size() - 1; i > 0; i--) {
			for(int a = 0; a < i; a++) {
				if(stationLists.get(a).getSavedDate().isEqual(stationLists.get(a+1).getSavedDate()) && stationLists.get(a).getSavedTime().isAfter(stationLists.get(a+1).getSavedTime())) {
					FinalStationMasterList holder = stationLists.get(a);
					stationLists.set(a, stationLists.get(a+1));
					stationLists.set(a+1, holder);
				}
			}
		}
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
	public void setTimeAndName() {
		savedTime = LocalTime.now();
		savedDate = LocalDate.now();
		fileName = "savedConfig" + savedTime.toString();
	}
	public void save() {
	    if (!dir.exists()){
	        dir.mkdir();
	    }
	    File oldFile = new File(dir.getPath() + "/" + fileName);
	    if(oldFile.exists()) {
	    	oldFile.delete();
	    }
	    try {
            FileOutputStream fileOut = new FileOutputStream(dir.getPath() + "/" + fileName);
            ObjectOutputStream objectOut = new ObjectOutputStream(fileOut);
            objectOut.writeObject(this);
            objectOut.close();
            System.out.println("The Object was succesfully written to a file");
 
        } catch (Exception ex) {
        	int option = JOptionPane.showConfirmDialog(null, "an error has occurred while saving "
        			+ "this config. "
        			+ "this shouldn't ever happen and if you see this message "
        			+ "that means Cyrus messed up. He assures you he feels really bad "
        			+ "about this. ");
        	if(option == 0) {
        		dir.delete();
        	}
            ex.printStackTrace();
        }
	}
	public void delete() {
		File thisFile = new File(dir.getPath() + "/" + fileName);
		if(thisFile.exists()) {
	    	thisFile.delete();
	    }
	}
	/**
	 * adds 1 to the int at the end of the name
	 * if the int is higher than maximum, the file is deleted.
	 * @param maximum
	 */
	public String getNewNameAddedOne(int maximum) {
		String lastChar = fileName.substring(fileName.length() - 1, fileName.length());
		int newInt = 1;
		try {//there's no number after savedConfig (it's just savedConfig)
			newInt = Integer.parseInt(lastChar);
		}
		catch(Exception e) {
			return fileName+1;
		}
		newInt++;//the number after savedConfig (e.g. savedConfig2 -> 2)
		if(newInt > maximum +1) {//if the number is too big after adding 1, return null
			//delete this station. 
			return null;
		}
		return fileName.substring(0,fileName.length() - 1) + newInt;
	}
	public void changeNameAndDeleteOldCopy(String newName) {
		delete();
		this.fileName = newName;
		save();
	}
	public void moveToNewDir(File newDir) {
		delete();
		this.dir = newDir;
		save();
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
	public SavedSettings deserializeSettings(File file) {
		if(!file.exists()) {
			return new SavedSettings();
		}
		try
        {    
            // Reading the object from a file 
            FileInputStream fileIn = new FileInputStream(file); 
            ObjectInputStream in = new ObjectInputStream(fileIn); 
              
            // Method for deserialization of object 
            SavedSettings settings = (SavedSettings)in.readObject(); 
              
            in.close(); 
            fileIn.close(); 
              
            System.out.println("Object has been deserialized "); 
            return settings;
        } 
          
        catch(IOException ex) 
        { 
            System.out.println("IOException is caught"); 
        	file.delete();
        	System.out.println("settings deleted");
        	System.out.println("retrying...");
            return deserializeSettings(file);
        } 
          
        catch(ClassNotFoundException ex) 
        { 
            System.out.println("ClassNotFoundException is caught"); 
            return null;
        } 
	}
	
	public FinalStationMasterList deserializeFinalStationMasterList(File file) {
		try
        {    
            // Reading the object from a file 
            FileInputStream fileInput = new FileInputStream(file); 
            ObjectInputStream in = new ObjectInputStream(fileInput); 
              
            // Method for deserialization of object 
            FinalStationMasterList stationList = (FinalStationMasterList)in.readObject(); 
              
            in.close(); 
            fileInput.close(); 
              
            System.out.println("Object has been deserialized "); 
            return stationList;
        } 
          
        catch(IOException ex) 
        { 
            System.out.println("IOException is caught"); 
            System.out.println("Deleting oldConfigs and recentlyDeletedConfigs and all word docs");
            this.deleteOldConfigs();
            this.deleteWordDocs();
            
            return null;
        } 
          
        catch(ClassNotFoundException ex) 
        { 
            System.out.println("ClassNotFoundException is caught"); 
            return null;
        } 
	}
	public void deleteOldConfigs() {
        File configDir = new File(Frame3.dirName);
        for(File config : configDir.listFiles()) {
        	config.delete();
        }
        File RDConfigDir = new File(Frame3.rDConfigDirName);
        for(File config : RDConfigDir.listFiles()) {
        	config.delete();
        }
	}
	public void deleteWordDocs() {
		File wordDocDir = new File(Frame3.docDirName);
		for(File doc : wordDocDir.listFiles()) {
			doc.delete();
		}
		File RDWordDocDir = new File(Frame3.rDWordDirName);
		for(File doc : RDWordDocDir.listFiles()) {
			doc.delete();
		}
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

	public LocalTime getSavedTime() {
		return savedTime;
	}

	public void setSavedTime(LocalTime savedTime) {
		this.savedTime = savedTime;
	}

	public File getAssociatedWordDoc() {
		return associatedWordDoc;
	}

	public void setAssociatedWordDoc(File associatedWordDoc) {
		this.associatedWordDoc = associatedWordDoc;
	}

	public File getThisFile() {
		thisFile = new File(dir.getPath() + "/" + fileName);
		return thisFile;
	}
	public String getFileName() {
		return fileName;
	}
	public File getDir() {
		return dir;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public LocalDate getSavedDate() {
		return savedDate;
	}

	public void setSavedDate(LocalDate savedDate) {
		this.savedDate = savedDate;
	}
}
