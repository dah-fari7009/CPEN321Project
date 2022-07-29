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
        throw `This player ${name} couldnt be found for the specified region. Check the spelling of the region.`
    }

    return { name, region, stats, reviews }
}
module.exports = { getProfile };
