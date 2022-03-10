# Installation
PlaceholderAPI expansion for MobArena.

1. Download the latest *dev build* of MobArena from [here](https://github.com/garbagemule/MobArena/actions)
2. Download the latest release of MobArenaStats from [here](https://github.com/mobarena/MobArenaStats)
3. Download the latest release of MobArenaPlaceholders on the right.
4. Upload the MobArenaPlaceholders jar to `plugins/PlaceholderAPI/expansions`
5. Run `/papi reload`
6. And that's it! Have fun!

## Available Placeholders

### Arena placeholders

Arena placeholders are used to retrieve info from a specific arena at any given time!

To use it, replace '{arena}' with the actual arena name! For example, `%mobarena_arena_default_state%` will return the state of the arena 'default'. You can also use `$current` as your arena name to get info for the current arena that the player is in! If the player isn't in said arena, it'll return an empty string.

| Placeholder  | Output |
|------------- | -------------|
|%mobarena_arena_{arena}\_current-wave%  | Returns the current arena wave number. Always starts at 1|
|%mobarena_arena_{arena}\_final-wave%  | Returns the final wave in the arena. If not found, returns "∞"|
|%mobarena_arena_{arena}\_live-mobs%  | Returns the amount of living mobs in the arena|
|%mobarena_arena_{arena}\_ready-players%  | Returns the amount of players ready players in the arena|
|%mobarena_arena_{arena}\_live-players%  | Returns the amount of living players in the arena|
|%mobarena_arena_{arena}\_dead-players%  | Returns the amount of dead players in the arena|
|%mobarena_arena_{arena}\_initial-players%  | Returns the initial amount of players that joined the arena|
|%mobarena_arena_{arena}\_lobby-players%  | Returns the amount of players in the lobby|
|%mobarena_arena_{arena}\_min-players%  | Returns the minimum amount of players needed for the arena to start|
|%mobarena_arena_{arena}\_max-players%  | Returns the max amount of players the arena can hold|
|%mobarena_arena_{arena}\_auto-start-timer%  | Returns the auto start timer in the arena (in seconds)|
|%mobarena_arena_{arena}\_name%  | Returns the arena slug (name)|
|%mobarena_arena_{arena}\_state%  | Returns `Editing` if the arena is in edit-mode, returns `Running` if there are currently players in the arena, returns `Enabled` if the arena is open, but no one is playing, returns `Disabled` if the arena is disabled. **All Strings are configurable!** You can just take a look at placeholderapi's config file to find the configurations!

## Player placeholders

These placeholders are used to get info from a player's current arena stats. So the kills they've made in the current session, swings, hits, and so on. If the player is not in an arena, the placeholders output an empty string (""). Once a player dies, all these stats are cleared. If you're looking for permanent stats, scroll further!

| Placeholder  | Output|
|------------- | -------------|
|%mobarena_player_class%  | Returns the class of the player.|
|%mobarena_player_kills%  | Returns the amount of kills the player has made in the current session.|
|%mobarena_player_swings%  | Returns the amount of swings a player has made.|
|%mobarena_player_damage-taken%  | Returns the damage a player has taken.|
|%mobarena_player_damage-done%  | Returns the damage a player has done to mobs|
|%mobarena_player_swings%  | Returns the amount of swings a player made, regardless of if it hit a mob or not|
|%mobarena_player_hits%  | Returns the amount of hits a player landed on a mob|
|%mobarena_player_last-wave%  | Returns the last wave a player survived to.|



## Stats 

This section requires you to install MobArenaStats. If it's not found on your server, all the placeholders below will not work! 

### Global Stats
Returns the stats for all arenas

| Placeholder  | Output |
| ------------- | ------------- |
| %mobarena_stats_global_total-sessions%  | Returns combined amount of sessions played in all arenas |
| %mobarena_stats_global_total-seconds%  | Returns amount of seconds played in all arenas |
| %mobarena_stats_global_total-kills%  | Returns amount of kills made in every arena |
| %mobarena_stats_global_total-waves%  | Returns amount of waves played in every arena |

### Arena-Specific Stats
Returns the stats for a specific arena. 

| Placeholder  | Output |
| ------------- | ------------- |
| %mobarena_stats_arena_{arena}\_total-sessions%  | Returns combined amount of sessions played in the sepcific arena|
| %mobarena_stats_arena_{arena}\_total-seconds%  | Returns amount of seconds played |
| %mobarena_stats_arena_{arena}\_total-kills%  | Returns amount of kills made in said arena |
| %mobarena_stats_arena_{arena}\_total-waves%  | Returns amount of waves played in said arena |
| %mobarena_stats_arena_{arena}\_highest-wave%  | Returns the highest wave reached in an arena |
| %mobarena_stats_arena_{arena}\_highest-kills%  | Returns the highest amount of kills made in an arena|
| %mobarena_stats_arena_{arena}\_highest-seconds%  | Returns the highest duration survived in an arena|

## Player Stats
Returns the total stats for a player 
| Placeholder  | Output |
| ------------- | ------------- |
| %mobarena_stats_player_total-sessions%  | Returns combined amount of sessions played by the player |
| %mobarena_stats_player_total-seconds%  | Returns amount of seconds played by the player |
| %mobarena_stats_player_total-kills%  | Returns amount of kills made in every arena by the player|
| %mobarena_stats_player_total-waves%  | Returns amount of waves played in every arena by the player |
