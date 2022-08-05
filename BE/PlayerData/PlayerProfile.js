const DataHandlerModule = require("./DataHandler");
const getMatchHistory = DataHandlerModule.getMatchHistory;
const userServiceModule = require("./UserService");
const getFromDB = userServiceModule.getFromDB;

async function getProfile(name, region) {
    if (!name || !region) {
        throw "Must include all fields";
    }

    let reviews = await getFromDB(name);
    let stats = await getMatchHistory(name, region);
    if (!stats) {
        throw `This player couldnt be found for the specified region. Check the spelling of the region.`
    }

    let profile = { name, region, stats, reviews };
    return profile;
}
module.exports = { getProfile };