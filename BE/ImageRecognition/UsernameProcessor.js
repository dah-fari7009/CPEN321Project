const express = require('express')
const router = express.Router()
const vision = require('@google-cloud/vision');
const client = new vision.ImageAnnotatorClient({
    keyFilename: '<--- GOOGLE VISION API KEY JSON FILE --->'
});
const TeamStats = require("../Prediction/TeamStats");

const JOINED_THE_LOBBY = " joined the lobby"

// Upload riot ids
router.post('/usernames', (req, res) => {
    var ids = {
        id1 : req.body.id1,
        id2 : req.body.id2,
        id3 : req.body.id3,
        id4 : req.body.id4,
        id5 : req.body.id5,
    }

    if (!ids.id1 || !ids.id2 || !ids.id3 || !ids.id4 || !ids.id5) {
        res.status(400).json("All riot ids must be specified")
    } else {
        // TODO: send riot ids to team stats class 
        res.status(200)
    }
})

// Upload image for further processing
router.get('/', async (req, res) => {
    // TODO: get image from frontend
    parseText('<--- FILE NAME --->').then((names) => {
        TeamStats(names).then(teamStats => {
            res.json(teamStats).status(200);
        })
    })
})

async function parseText(fileName) {
    const [result] = await client.textDetection(fileName)
    const detections = result.textAnnotations
    const text = detections[Object.keys(detections)[0]].description
    const textAsArray = text.split('\n')

    const ids = []
    
    textAsArray.forEach(element => {
        if (element.endsWith(JOINED_THE_LOBBY)) {
            const id = element.replace(JOINED_THE_LOBBY, '')
            ids.push(id)
        }
    });

    console.log(ids)
}

module.exports = router