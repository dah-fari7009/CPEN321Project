const PlayerProfile = require('../PlayerData/PlayerProfile');
const getPlayer = PlayerProfile.getProfile;
const tf = require('@tensorflow/tfjs-node');

// aggregate kills/deaths/etc per second
let akps = 0;
let aaps = 0;
let adps = 0;
let agps = 0;
let avps = 0;

function sumStats(stats) {
    akps += stats.kps;
    aaps += stats.aps;
    adps += stats.dps;
    agps += stats.gps;
    avps += stats.vps;
}

async function TeamStats(names, region) {

    let player1 = await getPlayer(names[0], region);
    let player2 = await getPlayer(names[1], region);
    let player3 = await getPlayer(names[2], region);
    let player4 = await getPlayer(names[3], region);
    let player5 = await getPlayer(names[4], region);

    sumStats(player1.stats);
    sumStats(player2.stats);
    sumStats(player3.stats);
    sumStats(player4.stats);
    sumStats(player5.stats);

    let teamStats = [akps, aaps, adps, agps, avps];

    for (let i = 0; i < teamStats.length; i++) {
        teamStats[i] = teamStats[i] / 5;
    }
    
    let input = tf.tensor([teamStats]);
    let model = await tf.loadLayersModel('file://Prediction/Model/model.json');
    let prediction = await model.predict(input);
    prediction = prediction.arraySync()[0][0];

    return new Promise(resolve => {
        resolve({prediction, player1, player2, player3, player4, player5})
    })
    
}

module.exports = { TeamStats };