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

    let duration = gameData.data.info.gameDuration > 0 ? gameData.data.info.gameDuration : 1;
    
    killsPerSec.push(playerStats.kills/duration);
    deathsPerSec.push(playerStats.deaths/duration);
    assistsPerSec.push(playerStats.assists/duration);
    visionPerSec.push(playerStats.visionScore/duration);
    goldPerSec.push(playerStats.goldEarned/duration);

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

async function getChamps(id, region) {
    return await axios.get(`https://${region}.api.riotgames.com/lol/champion-mastery/v4/champion-masteries/by-summoner/${id}?api_key=${API_KEY}`).catch(err => console.log(err));
}


function getChampToNameMap() {
    return axios.get('http://ddragon.leagueoflegends.com/cdn/12.12.1/data/en_US/champion.json');
}

async function getChampName(id) {
    let map = await getChampToNameMap();

    for (let i in map.data.data) {
        if (id == map.data.data[i].key) {
            return map.data.data[i].id;
        }
    }
}

async function getChampId(name) {
    let map = await getChampToNameMap();
    return map.data.data[name].key;
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

function setServer(region) {
    let server;
    if (region == "JP1" || region == "KR") {
        server = "asia";
    } else if (region == "BR1" || region == "LA1" || region == "LA2" || region == "NA1") {
        server = "americas";
    } else if (region == "EUN1" || region == "EUW1" || region == "RU1" || region == "TR1") {
        server = "europe";
    } else if (region == "OC1") {
        server = "sea";
    } else {
        throw "Region name doesn't exist";
    }

    URL_PREFIX = `https://${server}.api.riotgames.com`;
}


async function getMatchHistory(name, region) {
    if (!name || !region) {
        throw "Must include all fields";
    }

    setServer(region);

    let player = await getPlayerId(name, region);
    if (!player) {
        throw "Username cannot be found";
    }

    let gameIds = await getGameIdList(player.data.puuid);

    let gameStats = await getGameStats(gameIds.data[0]);

    let formattedStats = getRiotData(player.data.puuid, gameStats);
    return formattedStats;
}

async function getPlayerMasteries(name, region, champ) {
    if (!name || !region || !champ) {
        throw "Must include all fields";
    }

    setServer(region);
    let id = await getPlayerId(name, region);
    if (!id) {
        throw "Username cannot be found";
    }

    let list = await getChamps(id.data.id, region);
    
    let top1 = await getChampName(list.data[0].championId);
    let top2 = await getChampName(list.data[1].championId);
    let top3 = await getChampName(list.data[2].championId);

    let currChampId = await getChampId(champ);
    let playTime;
    let mastery;

    for (let i in list.data) {
        if(currChampId == list.data[i].championId) {
            if (i === 0) {
                playTime = "main";
            } else if (i <= 10) {
                playTime = "high";
            } else if (i <= 20) {
                playTime = "moderate";
            } else {
                playTime = "low";
            }
            mastery = list.data[i].championPoints;
        }
    }

    return {
        top1,
        top2,
        top3,
        playTime,
        mastery
    }
}

module.exports = {getMatchHistory, getPlayerMasteries};

// getMatchHistory("ct819", "NA1").then(res => console.log(res));
// "2 4", "TheWanderersWay", "palukawhale", "Thick Rooster", "ct819"

// async function dothing()
// {let id = await getPlayerMasteries("2 4", "NA1", "Irelia");
// console.log(id)}

// dothing()
