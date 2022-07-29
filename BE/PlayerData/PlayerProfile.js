const DataHandlerModule = require("./DataHandler");
const getMatchHistory = DataHandlerModule.getMatchHistory;
const userServiceModule = require("./UserService");
const getFromDB = userServiceModule.getFromDB;

async function getProfile(name, region) {
    let reviews = await getFromDB(name);
    let stats = await getMatchHistory(name, region);

    return { name, region, stats, reviews }
}
module.exports = { getProfile };
