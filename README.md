# TableTennisProject
A simple app that keep tracks of player data for sports coaches, and automatically creates practicing "stations" for every practice to group players together!

1. Add players and adjust their ranks
2. Creates stations and fill out information on which kinds of players should be prioritized
3. Click "generate" for the algorithm to automatically add players into stations according to your configurations. You can also choose to manually add or remove players from a station.
 a. The algorithm will also avoid putting players in the same configuration every time.
4. Press Done to save the configuration and automatically convert the assignments into a word document!

Features:
- Simple interface to add, edit, and delete players to keep track of their rank and average ranks.
- Easily mark players absent, assign them partners, and change their ranks as necessary
- Create and edit stations, customizing them for any purpose. Customizable options include:
- Minimum, preferred, and maximum players allowed in the station.
	- Require that players in a station have similar ranks to ensure fairness
	- Prioritize or avoid players of a set range of ranks. Or allow everyone!
	- Prioritize or avoid gender mixing within the station. Or always allow it!
- Smart algorithm that generates stations filled with players according to your configurations
	- Reshuffle button if the assignments are not satisfactory
	- Allows manually adding or removing players from stations before or after the algorithm adds them in
	- Algorithm remembers previous assignments, and works to prevent putting players in the same station consecutively. You can also adjust how many stations it remembers.
	- Marking a station with "doubles" will always result in a player's partner to be added, if they have one.
- Automatically converts configurations into word documents for easy viewing. 
