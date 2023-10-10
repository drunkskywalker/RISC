**************************

Concerning vision update: on the start of each turn the vision is updated adn will remain that way until next turn.
Spy move orders will take effect next turn in order not to intefere with hasVision().
If a player has vision on a territory, its info is retreived directly. 
If a player does not have vision current;y on a territory but previously had, its info is retreived in old_info with territory id as key.
(which means if a id has a corresponding value in the hashmap then it had old vision)
If a player has never seen a territory before, it only gets the id. 

Concerning gameplay: newly added methods gen*order*. notice that the gen*order*s are still wraped in try-catch blocks. to get details, let them throw.
Spy train, railway sabotage, scorched earth, cloak should resemble (somehow) unit upgrade. notice that railway sabotage is only valid on enemy territories.
railway construct should resemble unit move. spy move should resemble attack.


Add rulecheckers to the global params, and the end turn method calls to update vision and distances.

**************************
