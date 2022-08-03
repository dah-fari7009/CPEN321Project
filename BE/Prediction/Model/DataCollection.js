const DataHandler = require("../../PlayerData/DataHandler");
const ObjectsToCsv = require('objects-to-csv');

const PRIMER_ID = "s-4csharCh0b-lP3FtFHel9vAIilmE4j9My6qQtN_HhcnKCsbRitw_sxSOWKiqS5rFQJB-P_q5kAyg";

async function collectMatches(primer) {
    DataHandler.setServer("NA1");
    let gameIds = await DataHandler.getGameIdList(primer);

    let teammateNames = [];

    for (let i = 0; i < gameIds.data.length; i++) {
        let id = gameIds.data[i];
        console.log(id);
        let gameStats = await DataHandler.getGameStats(id);

        let duration = gameStats.data.info.gameDuration;
        if (duration == 0) {
            continue;
        }

        let team1 = {
            id: "w" + id,
            kps: 0,
            aps: 0,
            dps: 0,
            gps: 0,
            vps: 0,
            win: true
        }
        
        let team2 = {
            id: "l" + id,
            kps: 0,
            aps: 0,
            dps: 0,
            gps: 0,
            vps: 0,
            win: false
        }

        for (let j = 0; j < gameStats.data.info.participants.length; j++) {
            let participant = gameStats.data.info.participants[j];
            teammateNames.push(participant.puuid);

            if (participant.win) {
                team1.kps += participant.kills / duration;
                team1.dps += participant.deaths / duration;
                team1.aps += participant.assists / duration;
                team1.gps += participant.goldEarned / duration;
                team1.vps += participant.visionScore / duration;
            } else {
                team2.kps += participant.kills / duration;
                team2.dps += participant.deaths / duration;
                team2.aps += participant.assists / duration;
                team2.gps += participant.goldEarned / duration;
                team2.vps += participant.visionScore / duration;
            }
        }

        // get averages over 5 teammates
        team1.kps /= 5;
        team1.dps /= 5;
        team1.aps /= 5;
        team1.gps /= 5;
        team1.vps /= 5;

        team2.kps /= 5;
        team2.dps /= 5;
        team2.aps /= 5;
        team2.gps /= 5;
        team2.vps /= 5;
        
        const csv = new ObjectsToCsv([team1, team2]);
        await csv.toDisk('./gamedata.csv', {append: true});
    }

    return teammateNames;
}

async function startCollecting() {
    // checkedIds keeps track of all the IDs that have been visited
    // to prevent duplicate match histories collected
    // There will still be duplicate matches, due to two diff players
    // sharing 1+ games together, those will be removed after collection
    let checkedIds = new Set();
    let teamIds = await collectMatches(PRIMER_ID);
    checkedIds.add(PRIMER_ID);

    for (let i = 0; i < teamIds.length; i++) {
        let id = teamIds[i];
        console.log("ID: "+id);
        if (!checkedIds.has(id)) {
            console.log("Accepted ID: "+id);
            await sleep(60000);
            await collectMatches(id);
            checkedIds.add(id);
        }
    }
}

function sleep (milliseconds) {
    return new Promise((resolve) => setTimeout(resolve, milliseconds))
  }

startCollecting()