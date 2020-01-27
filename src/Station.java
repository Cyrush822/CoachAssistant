import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.awt.*;
public class Station implements Serializable { 
	private final String dirName = "stations";
	private String stationName;
	private String stationDescription;
	private int minPlayers;
	private int maxPlayers;
	private int preferredPlayers;
	private boolean mustBeEven;
	
	private boolean isRanked;
	private int maxRankDifference;
	private MasterStationList.genderPreference genderPref;
	private MasterStationList.rankPreference rankPref;
	private int rankMin;//use with rankPreference. e.g.: restrict rankMin (5) - rankMax(10) means
						//never use people from rank 5-10. 
	private int rankMax;
	private int numberOfTables;
	private MasterStationList.competitiveType compType;
	
	private ArrayList<Player> players;
	
	private boolean isDisabled;
	/**
	 * 
	 * @param name
	 * @param desc
	 * @param min
	 * @param max
	 * @param preferred
	 * @param even
	 * @param ranked
	 * @param maxRank
	 * @param genpref
	 * @param rankPref
	 * @param prefMin
	 * @param prefMax
	 * @param tables
	 */
	public Station(String name, String desc, int min, int max, int preferred, 
			boolean even, boolean ranked, int maxRank, 
			MasterStationList.genderPreference genpref, 
			MasterStationList.rankPreference rankPref, int prefMin,
			int prefMax, int tables, MasterStationList.competitiveType compType) {
		stationName = name;
		stationDescription = desc;
		minPlayers = min;
		maxPlayers = max;
		preferredPlayers = preferred;
		mustBeEven = even;
		isRanked = ranked;
		maxRankDifference = maxRank;
		this.genderPref = genpref;
		this.rankPref = rankPref;
		this.rankMin = prefMin;
		this.rankMax = prefMax;
		this.numberOfTables = tables;
		this.compType = compType;
		players = new ArrayList<Player>();
		saveTable();
	}
	
	public void saveTable() {
		File directory = new File(dirName);
	    if (! directory.exists()){
	        directory.mkdir();
	    }
	    File oldTable = new File(dirName + "/" + stationName);
	    if(oldTable.exists()) {
	    	oldTable.delete();
	    }
	    try {
            FileOutputStream fileOut = new FileOutputStream(directory + "/" + stationName);
            ObjectOutputStream objectOut = new ObjectOutputStream(fileOut);
            objectOut.writeObject(this);
            objectOut.close();
            System.out.println("The Object was succesfully written to a file");
 
        } catch (Exception ex) {
            ex.printStackTrace();
        }
	}
	
	public String toString() {
		String str = "";
		str += String.format("Name: %-20s | Desc: %-25s ", stationName, stationDescription);
		String compStr = "";
		if(compType == MasterStationList.competitiveType.notCompetitive) {
			compStr = String.format("| min: %-3d pref: "
					+ "%-3d max: %-3d ", minPlayers, preferredPlayers, maxPlayers);
		} else {
			if(compType == MasterStationList.competitiveType.singles) {
				compStr = String.format("| Singles ");
			} else {
				compStr = String.format("| Doubles ");
			}
		}
		str += String.format("%-20s | ", compStr);
		String rankText = "";
		if(isRanked) {
			rankText = String.format("Ranked. Max Rank Difference: %-2d", maxRankDifference); 
		}
		str += String.format("%-25s", rankText);
		if(isDisabled) {
			str += "- DISABLED";	
		}
		return str;
	}
	public static String adjustSpacing(int length, int tabs)
	{
		int tabsDeleted = length/7;
		String ans = "";
		for(int i = 0; i < tabs - tabsDeleted; i++)
		{
			ans += "\t";
		}
		return ans;
	}
	public void deleteTable() {
		File thisTable = new File(dirName + "/" + stationName);
		if(thisTable.exists()) {
	    	thisTable.delete();
	    }
	}
	public void addPlayer(Player player) {
		players.add(player);
	}
	public Player getPlayer(int index) {
		return players.get(index);
	}
	public void deletePlayer(int index) {
		players.remove(index);
	}
	public void addPlayer(Player player, int index) {
		players.add(index, player);
	}
	public void replacePlayer(Player old, Player newPlayer) {
		int index = players.indexOf(old);
		replacePlayer(index, newPlayer);
	}
	public void replacePlayer(int index, Player newPlayer) {
		players.remove(index);
		players.add(index, newPlayer);
	}
	public String getRankPreferenceStr() {
		if(rankPref == MasterStationList.rankPreference.allow) {
			return "Allow any";
		} else if(rankPref == MasterStationList.rankPreference.restrict) {
			return "Restrict ranks: " + this.rankMin + " - " + this.rankMax;
		} else if(rankPref == MasterStationList.rankPreference.stronglyavoid) {
			return "Strongly avoid; " + this.rankMin + " - " + this.rankMax;
		} else if(rankPref == MasterStationList.rankPreference.avoid) {
			return "Avoid " + this.rankMin + " - " + this.rankMax;
		} else if(rankPref == MasterStationList.rankPreference.prioritize) {
			return "Prioritize " + this.rankMin + " - " + this.rankMax;
		} else if(rankPref == MasterStationList.rankPreference.stronglyprioritize) {
			return "Strongly prioritize " + this.rankMin + " - " + this.rankMax;
		} else {
			return "Require " + this.rankMin + " - " + this.rankMax;
		}
	}
	public String getGenderPreferenceStr() {
		if(genderPref == MasterStationList.genderPreference.allow) {
			return "Allow";
		} else if(genderPref == MasterStationList.genderPreference.restrict) {
			return "Restrict";
		} else if(genderPref == MasterStationList.genderPreference.stronglyavoid) {
			return "Strongly avoid";
		} else if(genderPref == MasterStationList.genderPreference.avoid) {
			return "Avoid";
		} else if(genderPref == MasterStationList.genderPreference.prioritize) {
			return "Prioritize";
		} else if(genderPref == MasterStationList.genderPreference.stronglyprioritize) {
			return "Strongly prioritize";
		} else {
			return "Require";
		}
	}
	public String getPath() {
		return dirName + "/" + stationName;
	}
	
	public void setMinPlayers(int min) {
		minPlayers = min;
	}
	
	public int getMinPlayers() {
		return minPlayers;
	}

	public String getStationName() {
		return stationName;
	}

	public int getMaxPlayers() {
		return maxPlayers;
	}
	public String getStationDesc() {
		return stationDescription;
	}

	public int getPreferredPlayers() {
		return preferredPlayers;
	}

	public boolean isMustBeEven() {
		return mustBeEven;
	}

	public boolean isRanked() {
		return isRanked;
	}

	public int getMaxRankDifference() {
		return maxRankDifference;
	}

	public MasterStationList.genderPreference getGenderPref() {
		return genderPref;
	}

	public MasterStationList.rankPreference getRankPref() {
		return rankPref;
	}

	public int getRankMin() {
		return rankMin;
	}

	public int getRankMax() {
		return rankMax;
	}

	public int getNumberOfTables() {
		return numberOfTables;
	}

	public ArrayList getPlayers() {
		return players;
	}

	public void setStationName(String stationName) {
		this.stationName = stationName;
	}

	public void setMaxPlayers(int maxPlayers) {
		this.maxPlayers = maxPlayers;
	}

	public void setPreferredPlayers(int preferredPlayers) {
		this.preferredPlayers = preferredPlayers;
	}

	public void setMustBeEven(boolean mustBeEven) {
		this.mustBeEven = mustBeEven;
	}

	public void setRanked(boolean isRanked) {
		this.isRanked = isRanked;
	}

	public void setMaxRankDifference(int maxRankDifference) {
		this.maxRankDifference = maxRankDifference;
	}

	public void setGenderPref(MasterStationList.genderPreference genderPref) {
		this.genderPref = genderPref;
	}

	public void setRankPref(MasterStationList.rankPreference rankPref) {
		this.rankPref = rankPref;
	}

	public void setRankMin(int rankMin) {
		this.rankMin = rankMin;
	}

	public void setRankMax(int rankMax) {
		this.rankMax = rankMax;
	}

	public void setNumberOfTables(int numberOfTables) {
		this.numberOfTables = numberOfTables;
	}

	public void setPlayers(ArrayList<Player> players) {
		this.players = players;
	}


	public boolean isDisabled() {
		return isDisabled;
	}

	public void setStationDescription(String stationDescription) {
		this.stationDescription = stationDescription;
	}
	
	public void setDisabled(boolean isDisabled) {
		this.isDisabled = isDisabled;
		saveTable();
	}
	
	public MasterStationList.competitiveType getCompType() {
		return compType;
	}

	public void setCompType(MasterStationList.competitiveType compType) {
		this.compType = compType;
	}
}
