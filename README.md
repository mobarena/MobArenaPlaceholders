# Installation
PlaceholderAPI expansion for MobArena.

1. Download the latest stable release on the right.
2. Upload the jar to plugins/PlaceholderAPI/expansions
3. Run /papi reload
4. And that's it! Have fun!

## Available Placeholders


### Player-Arena Placeholders

These placeholders return the content for the arena the player is **currently** playing in. The player must be in a running arena for the placeholders to parse correctly. If they're not, then the placeholder will return "" instead of the actual value. I.E an empty string 

| Placeholder  | Output|
|------------- | -------------|
|%mobarena_player-arena_name%  | Returns the arena name|
|%mobarena_player-arena_wave%  | Returns the current arena wave number. Always starts at 1|
|%mobarena_player-arena_final-wave%  | Returns the final wave in the arena. If not found, returns "∞"|
|%mobarena_player-arena_remaining-mobs%  | Returns the amount of living mobs in the arena|
|%mobarena_player-arena_kills%  | Returns the amount of kills the player made in the arena, gets reset on leave.|
|%mobarena_player-arena_ready%  | Returns the amount of players ready players in the arena|
|%mobarena_player-arena_non-ready%  | Returns the amount of players who are _not_ ready yet|
|%mobarena_player-arena_players%  | Returns the amount of living players in the arena|
|%mobarena_player-arena_min-players%  | Returns the minimum amount of players needed for the arena to start|
|%mobarena_player-arena_max-players%  | Returns the max amount of players the arena can hold|
|%mobarena_player-arena_auto-start-timer%  | Returns the auto start timer in the arena (in seconds)|
|%mobarena_player-arena_isready%  | Returns if the player is ready or not. Returns `§aReady` if the player is ready, and returns `§7Not Ready` if they're not.|


### Per-arena placeholders

For the most part, the arena specific placeholders are the exact same as the player-arena ones. However, they always show their values regardless of if the player is in the arena or not. It also has a special 'status' placeholder, which returns the current status of the arena.

To use it, replace '{arena}' with the actual arena name! For example, `%mobarena_default_name%` will return the name of the arena 'default' 

| Placeholder  | Output|
|------------- | -------------|
|%mobarena_{arena}\_name%  | Returns the arena name|
|%mobarena_{arena}\_wave%  | Returns the current arena wave number. Always starts at 1|
|%mobarena_{arena}\_final-wave%  | Returns the final wave in the arena. If not found, returns "∞"|
|%mobarena_{arena}\_remaining-mobs%  | Returns the amount of living mobs in the arena|
|%mobarena_{arena}\_ready%  | Returns the amount of players ready players in the arena|
|%mobarena_{arena}\_non-ready%  | Returns the amount of players who are _not_ ready yet|
|%mobarena_{arena}\_players%  | Returns the amount of living players in the arena|
|%mobarena_{arena}\_min-players%  | Returns the minimum amount of players needed for the arena to start|
|%mobarena_{arena}\_max-players%  | Returns the max amount of players the arena can hold|
|%mobarena_{arena}\_auto-start-timer%  | Returns the auto start timer in the arena (in seconds)|
|%mobarena_{arena}\_status%  | Returns `EDITING` if the arena is in edit-mode, returns `RUNNING` if there are currently players in the arena, returns `ENABLED` if the arena is open, but no one is playing, returns `DISABLED` if the arena is disabled.
|%mobarena_{arena}\_status-colored%  | Returns the status of the arena with some colors and formatting, just to make it look better.|
|%mobarena_{arena}\_player-status%  | Returns the status of the player relative to the arena. Returns `§aPlaying` if the player is playing in the arena, returns `§cDead` if the player is dead (still didn't hit respawn), returns `§7Spectating` if the player is spectating the arena, and returns `§7Not playing` if the player isn't in the arena.|

