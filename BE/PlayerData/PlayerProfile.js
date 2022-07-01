import { getMatchHistory } from "./DataHandler.js";

class PlayerProfile {
    // Input is a single username and the region
    constructor(name, region) {
        this.name = name;
        this.region = region;
        this.stats = getMatchHistory(name, region);

        // TODO: add in the likes/dislikes/comments
        // after the user service and DB are up
    }

    getStats() {
        return this.stats;
    }

}

let player = new PlayerProfile("2 4", "NA1");
console.log(await player.getStats());