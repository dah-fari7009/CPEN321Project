const axios = require("axios");
require('dotenv').config()

const API_KEY = process.env.RIOT_API_KEY;
let URL_PREFIX;


function getRiotData(id, gameData) {
                
    for (let i = 0; i < gameData.data.info.participants.length; i++) {
        if (gameData.data.info.participants[i].puuid == id) {
            playerStats = gameData.data.info.participants[i];
        }
    }

    let killsPerSec = [];
    let deathsPerSec = [];
    let assistsPerSec = [];
    let goldPerSec = [];
    let visionPerSec = [];
    
    killsPerSec.push(playerStats.kills/gameData.data.info.gameDuration);
    deathsPerSec.push(playerStats.deaths/gameData.data.info.gameDuration);
    assistsPerSec.push(playerStats.assists/gameData.data.info.gameDuration);
    visionPerSec.push(playerStats.visionScore/gameData.data.info.gameDuration);
    goldPerSec.push(playerStats.goldEarned/gameData.data.info.gameDuration);

    return {
        kps: getAverage(killsPerSec),
        aps: getAverage(assistsPerSec),
        dps: getAverage(deathsPerSec),
        gps: getAverage(goldPerSec),
        vps: getAverage(visionPerSec)
    };
}

async function getGameIdList(id) {
    return await axios.get(`${URL_PREFIX}/lol/match/v5/matches/by-puuid/${id}/ids?type=ranked&start=0&count=20&api_key=${API_KEY}`);
}

async function getGameStats(matchId) {
    return await axios.get(`${URL_PREFIX}/lol/match/v5/matches/${matchId}?api_key=${API_KEY}`).catch(err => console.log(err));
}

function getAverage(stat) {
    let sum = 0;
    for (let val of stat) {
        sum += val;
    }
    return sum/stat.length;
}

async function getPlayerId(name, region) {
    name = name.replace(/\s/g, "%20");
    return axios.get(`https://${region}.api.riotgames.com/lol/summoner/v4/summoners/by-name/${name}?api_key=${API_KEY}`).catch(err => console.log(err));
}


async function getMatchHistory(name, region) {
    let server;
    if (region == "JP1" || region == "KR") {
        server = "asia";
    } else if (region == "BR1" || region == "LA1" || region == "LA2" || region == "NA1") {
        server = "americas";
    } else if (region == "EUN1" || region == "EUW1" || region == "RU1" || region == "TR1") {
        server = "europe";
    } else {
        server = "sea";
    }

    URL_PREFIX = `https://${server}.api.riotgames.com`;

    let player = await getPlayerId(name, region);
    let gameIds = await getGameIdList(player.data.puuid);

    let gameStats = await getGameStats(gameIds.data[0]);

    let formattedStats = getRiotData(player.data.puuid, gameStats);
    return formattedStats;
}

module.exports = getMatchHistory;

// getMatchHistory("ct819", "NA1").then(res => console.log(res));
// "2 4", "TheWanderersWay", "palukawhale", "Thick Rooster", "ct819"