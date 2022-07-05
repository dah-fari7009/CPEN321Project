const axios = require("axios");

const API_KEY = "RGAPI-c1ce139d-1bef-4175-9ce8-1158c11ceb65";
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

    let stats = {
        kps: getAverage(killsPerSec),
        aps: getAverage(assistsPerSec),
        dps: getAverage(deathsPerSec),
        gps: getAverage(goldPerSec),
        vps: getAverage(visionPerSec)
    };

    return new Promise(resolve => {
        resolve(stats)
    });
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

function getPlayerId(name, region) {
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
    
    return new Promise((resolve, reject) => {
                
        getPlayerId(name, region).then(player => {
            getGameIdList(player.data.puuid).then(gameIds => {
                for (let i = 0; i < 1; i++) {
                    getGameStats(gameIds.data[i]).then(gameStats => {
                        resolve(getRiotData(player.data.puuid, gameStats));
                    })
                }
            })
        })

    })
}

module.exports = getMatchHistory;

// getMatchHistory("ct819", "NA1").then(res => console.log(res));
// "2 4", "TheWanderersWay", "palukawhale", "Thick Rooster", "ct819"