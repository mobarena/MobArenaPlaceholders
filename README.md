# Installation
PlaceholderAPI expansion for MobArena.

1. Download the latest stable release on the right.
2. Upload the jar to plugins/PlaceholderAPI/expansions
3. Run /papi reload
4. And that's it! Have fun!

## Available Placeholders


## Global Placeholders
| Placeholder  | Output |
| ------------- | ------------- |
| %mapapi_total_enabled%  | Returns the amount of all enabled arenas on the server |
| %mapapi_total_playing%  | Returns the amount of all players playing on every arena  |

### Player-Arena Placeholders

These placeholders return the content for the arena the player is **currently** playing in. The player must be in a running arena for the placeholders to parse correctly. If they're not, then the placeholder will return "" instead of the actual value. I.E an empty string 

| Placeholder  | Output|
|------------- | -------------|
|%mapapi_player-arena_name%  | Returns the arena name|
|%mapapi_player-arena_wave%  | Returns the current arena wave number. Always starts at 1|
|%mapapi_player-arena_final-wave%  | Returns the final wave in the arena. If not found, returns "∞"|
|%mapapi_player-arena_remaining-mobs%  | Returns the amount of living mobs in the arena|
|%mapapi_player-arena_kills%  | Returns the amount of kills the player made in the arena, gets reset on leave.|
|%mapapi_player-arena_ready%  | Returns the amount of players ready players in the arena|
|%mapapi_player-arena_non-ready%  | Returns the amount of players who are _not_ ready yet|
|%mapapi_player-arena_players%  | Returns the amount of living players in the arena|
|%mapapi_player-arena_min-players%  | Returns the minimum amount of players needed for the arena to start|
|%mapapi_player-arena_max-players%  | Returns the max amount of players the arena can hold|
|%mapapi_player-arena_auto-start-timer%  | Returns the auto start timer in the arena (in seconds)|
|%mapapi_player-arena_isready%  | Returns if the player is ready or not. Returns `§aReady` if the player is ready, and returns `§7Not Ready` if they're not.|


### Per-arena placeholders

For the most part, the arena specific placeholders are the exact same as the player-arena ones. However, they always show their values regardless of if the player is in the arena or not. It also has a special 'status' placeholder, which returns the current status of the arena.

To use it, replace '{arena}' with the actual arena name! For example, `%mobarena_default_name%` will return the name of the arena 'default' 

| Placeholder  | Output|
|------------- | -------------|
|%mapapi_{arena}\_name%  | Returns the arena name|
|%mapapi_{arena}\_wave%  | Returns the current arena wave number. Always starts at 1|
|%mapapi_{arena}\_final-wave%  | Returns the final wave in the arena. If not found, returns "∞"|
|%mapapi_{arena}\_remaining-mobs%  | Returns the amount of living mobs in the arena|
|%mapapi_{arena}\_ready%  | Returns the amount of players ready players in the arena|
|%mapapi_{arena}\_non-ready%  | Returns the amount of players who are _not_ ready yet|
|%mapapi_{arena}\_players%  | Returns the amount of living players in the arena|
|%mapapi_{arena}\_min-players%  | Returns the minimum amount of players needed for the arena to start|
|%mapapi_{arena}\_max-players%  | Returns the max amount of players the arena can hold|
|%mapapi_{arena}\_auto-start-timer%  | Returns the auto start timer in the arena (in seconds)|
|%mapapi_{arena}\_status%  | Returns `EDITING` if the arena is in edit-mode, returns `RUNNING` if there are currently players in the arena, returns `ENABLED` if the arena is open, but no one is playing, returns `DISABLED` if the arena is disabled.
|%mapapi_{arena}\_status-colored%  | Returns the status of the arena with some colors and formatting, just to make it look better.|
|%mapapi_{arena}\_player-status%  | Returns the status of the player relative to the arena. Returns `§aPlaying` if the player is playing in the arena, returns `§cDead` if the player is dead (still didn't hit respawn), returns `§7Spectating` if the player is spectating the arena, and returns `§7Not playing` if the player isn't in the arena.|

## Stats 

This section requires you to install MobArenaStats. If it's not found on your server, all the placeholders below will return an empty string ("")! 

### Global Stats

| Placeholder  | Output |
| ------------- | ------------- |
| %mapapi_global_sessions%  | Returns combined amount of sessions played in all arenas |
| %mapapi_global_seconds%  | Returns amount of seconds played in all arenas |
| %mapapi_global_seconds-formatted%  | Same as above, but formats seconds into `HH:mm:ss` instead |
| %mapapi_global_kills%  | Returns amount of kills made in every arena |
| %mapapi_global_waves%  | Returns amount of waves played in every arena |

### Arena-Specific Stats
| Placeholder  | Output |
| ------------- | ------------- |
| %mapapi_{arena}\_total-sessions%  | Returns combined amount of sessions played in the sepcific arena|
| %mapapi_{arena}\_total-seconds%  | Returns amount of seconds played |
| %mapapi_{arena}\_total-seconds-formatted%  | Same as above, but formats seconds into `HH:mm:ss` instead |
| %mapapi_{arena}\_total-kills%  | Returns amount of kills made in said arena |
| %mapapi_{arena}\_total-waves%  | Returns amount of waves played in said arena |
| %mapapi_{arena}\_highest-wave%  | Returns the highest wave reached in an arena |
| %mapapi_{arena}\_highest-kills%  | Returns the highest amount of kills made in an arena|
| %mapapi_{arena}\_highest-seconds%  | Returns the highest duration survived in an arena|
| %mapapi_{arena}\_highest-seconds-formatted%  | Same as above, but formatted as `HH:mm:ss`|

| Placeholder  | Output |
| ------------- | ------------- |
| %mapapi_player_total-sessions%  | Returns combined amount of sessions played by the player |
| %mapapi_player_total-seconds%  | Returns amount of seconds played by the player |
| %mapapi_player_total-seconds-formatted%  | Same as above, but formats seconds into `HH:mm:ss` instead |
| %mapapi_player_total-kills%  | Returns amount of kills made in every arena by the player|
| %mapapi_player_total-waves%  | Returns amount of waves played in every arena by the player |
