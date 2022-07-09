const DataHandlerModule = require("./DataHandler");
const getMatchHistory = DataHandlerModule.getMatchHistory;
const userServiceModule = require("./UserService");
const getFromDB = userServiceModule.getFromDB;

class PlayerProfile {
    // Input is a single username and the region
    constructor(name, region) {
        this.name = name;
        this.region = region;
    }

    getOnlyStats() {
        return getMatchHistory(this.name, this.region);
    }

    async getProfile() {
        let reviews = await getFromDB(this.name);
        let stats = await getMatchHistory(this.name, this.region);

        return {
            name: this.name,
            region: this.region,
            stats: stats,
            reviews: reviews
        }
    }

}

module.exports = PlayerProfile;

// let player = new PlayerProfile("ct819", "NA1");
// let stats = player.getOnlyStats();
// stats.then(res => console.log(res));
// setTimeout(() => {
//     let stats2 = player.getProfile();
//     stats2.then(res => console.log(res))
// }, 2000)

// "2 4", "TheWanderersWay", "palukawhale", "Thick Rooster", "ct819"