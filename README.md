This mod allows server owners to define the wardrobes of players. Players can choose to wear the cosmetics in their wardrobes. There are two types of cosmetics: hats and bodies. Both types can be worn at once. A body changes the entire appearance of your character, a hat appears as a hat above the head.

This is what the wardrobe screen looks like:
![Wardrobe Screen](https://cdn.modrinth.com/data/cached_images/7bdfdc6f67fc5de7b317b16990261ead15ffc70c_0.webp)

## For Server Owners

This mod uses an HTTP server which is not included in this mod to manage each of your player's wardrobes. 
To configure this HTTP server on your minecraft server, a `cosmetic-backend-config.properties` config file is generated with the following content: 
```properties
apiKey=abc
apiUrl=https://example.com/api/cosmetics/
```
The `apiKey` is sent with every request as a Bearer-Authorization header. 
The `apiUrl` is the base url of your http server.
Your http server needs to implement these routes:

### GET /cosmetic/{id}: Returns the model data of the model with the given id
Response body:
```json
{
  "model": "String: the blockbench model json data",
  "script": "String: Lua script for animations etc.",
  "type": "String: Type of the cosmetic. May be either HAT or BODY. This defines how the cosmetics appears. HAT cosmetics appear above the head as a hat and BODY ones change the entire appearance of the player's avatar"
}
```

### POST /player/{player-uuid}/equip: Called when the player with the given UUID clicks the equip button on a cosmetic. 
Request body:
```json
{
  "cosmeticId": "Long: The ID of the cosmetic to equip"
}
```

Response body:
```json
{
  "equippedCosmetics": "Long-Array: The IDs of every cosmetic equipped by this player after this operation"
}
```


### POST /player/{player-uuid}/unequip: Called when the player with the given UUID clicks the unequip button on a cosmetic. 
Request body:
```json
{
  "cosmeticId": "Long: The ID of the cosmetic to unequip"
}
```

Response body:
```json
{
  "equippedCosmetics": "Long-Array: The IDs of every cosmetic equipped by this player after this operation"
}
```

### GET /player/{player-uuid}/wardrobe: Returns all the items in this player's wardrobe.
Response body:
```json
[
  {
    "id": "Long: This cosmetic's id",
    "name": "String: This cosmetic's name",
    "type": "String: This cosmetic's type, may be either HAT or BODY",
    "equipped": "Boolean: Whether this player has this cosmetic equipped",
    "previewImage": "String: Base64 encoded PNG to show in the avatar list. Is rendered with a resolution of 40 x 60 px"
  },
]
```

### GET /equipped: Returns the ID's of the cosmetics each player requested in the request body has equipped
Request body:
```json
{
  "equippedCosmetics": "UUID-Array: The UUIDs of the players to request"
}
```

Response body:
```json
{
  "UUID: the player's UUID": "Long-Array: The IDs of the cosmetics this player has equipped",
  ...
}
```

This mod is only supposed to be used in the Kryeit Minecraft server for now. 
