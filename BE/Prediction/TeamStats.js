const PlayerProfile = require("../PlayerData/PlayerProfile");

// aggregate kills/deaths/etc per second
let akps = 0;
let aaps = 0;
let adps = 0;
let agps = 0;
let avps = 0;

function sumStats(player) {
    player.getOnlyStats().then(stats => {
        akps += stats.kps;
        aaps += stats.aps;
        adps += stats.dps;
        agps += stats.gps;
        avps += stats.vps;
    })
}

async function TeamStats(names, region) {

    // if (names.length < 5) {
    //     return;
    // }

    let player1 = new PlayerProfile(names[0], region);
    let player2 = new PlayerProfile(names[1], region);
    let player3 = new PlayerProfile(names[2], region);
    let player4 = new PlayerProfile(names[3], region);
    let player5 = new PlayerProfile(names[4], region);

    // player1.getOnlyStats().then(stats => {
    //     akps += stats.kps;
    //     aaps += stats.aps;
    //     adps += stats.dps;
    //     agps += stats.gps;
    //     avps += stats.vps;
    // })

    sumStats(player1);
    sumStats(player2);
    sumStats(player3);
    sumStats(player4);
    sumStats(player5);
    
    let teamData = {
        akps: akps,
        aaps: aaps,
        adps: adps,
        agps: agps,
        avps: avps,
    }

    // TODO: (AFTER MVP) include ML model that will take in the teamData
    // and return a prediction (value from 0-1). For now, as a placeholder
    // just use random number generator from 0 to 1
    let prediction = Math.random();

    return new Promise(resolve => {
        let summary = {
            prediction: prediction,
            player1: player1.getProfile(),
            player2: player2.getProfile(),
            player3: player3.getProfile(),
            player4: player4.getProfile(),
            player5: player5.getProfile()
        }
        resolve(summary)
    })
    
}

// let a = TeamStats(["2 4", "TheWanderersWay", "palukawhale", "Thick Rooster", "ct819"], "NA1");
// a.then(res => {
//     console.log(res);
//     res.player1.then(b => {
//         b.stats.then(c => {console.log(c)})
//     })
//     // res.player2.then(b => {
//     //     b.stats.then(c => {console.log(c)})
//     // })
// })