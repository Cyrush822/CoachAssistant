import java.util.ArrayList;

public class testing {

	public static ArrayList<Player> players;
	public static void main(String[] args) {
		players = new ArrayList<Player>();
		players.add(new Player("6", 6, false, false, 0));
		players.add(new Player("1", 1, false, false, 0));
		players.add(new Player("3", 3, false, false, 0));
		players.add(new Player("5", 5, false, false, 0));
		players.add(new Player("2", 2, false, false, 0));
		players.add(new Player("4", 4, false, false, 0));
		System.out.println(players);
		sort();
		System.out.println(players);
	}
	public static void sort() {
		for(int i = players.size(); i > 0; i--) {
			for(int a = 0; a < i - 1; a++) {
				int rank1 = players.get(a).getRank();
				int rank2 = players.get(a + 1).getRank();
				if(rank1 > rank2) {
					swapInList(a, a+1);
				}
			}
		}
	}
	private static void swapInList(int p1, int p2) {
		//precondition: must be within bounds
		Player holder = players.get(p1);
		players.set(p1, players.get(p2));
		players.set(p2,  holder);
	}
	
	
}
