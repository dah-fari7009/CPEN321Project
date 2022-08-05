const fs = require('fs')
const vision = require('@google-cloud/vision');
const client = new vision.ImageAnnotatorClient({
    keyFilename: 'vision-key.json'
});
const TeamStats = require('../Prediction/TeamStats')

const JOINED_THE_LOBBY = " joined the lobby"

const uploadRiotIds = (req, res) => {
    const ids = []
    ids[0] = req.body.id1
    ids[1] = req.body.id2
    ids[2] = req.body.id3
    ids[3] = req.body.id4
    ids[4] = req.body.id5

    const region = req.body.region

    if (!region) {
        res.status(400)
        res.json("Region must be specified")
        return
    }

    if (region != 'NA1') {
        res.status(400)
        res.json("Invalid Region - NA1 region is only supported currently")
        return
    }

    if (!ids[0] || !ids[1] || !ids[2] || !ids[3] || !ids[4]) {
        res.status(400)
        res.json("All riot ids must be specified")
    } else {
        TeamStats.TeamStats(ids, region).then(teamStats => {
            res.status(200);
            res.json(teamStats)
        })
    }
}

const uploadRiotIdsByImage = async (req, res) => {
    const base64EncodedImage = req.body.base64EncodedImage
    const region = req.body.region

    if (!region) {
        res.status(400)
        res.json("Region must be specified")
        return
    }

    if (region != 'NA1') {
        res.status(400)
        res.json("Invalid Region - NA1 region is only supported currently")
        return
    }

    const data = Buffer.from(base64EncodedImage, 'base64')
    
    fs.writeFile('./ImageRecognition/usernames.png', data, () => {
        return
    })

    await parseText('./ImageRecognition/usernames.png').then((ids) => {
        if (ids.length != 5) {
            res.status(400)
            res.json("All 5 riot ids must be visible")
            return
        }

        TeamStats.TeamStats(ids, region).then(teamStats => {
            res.json(teamStats)
            res.status(200);
        })
    })

    fs.unlink('./ImageRecognition/usernames.png', () => { return })
}

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

    return ids
}

module.exports = {uploadRiotIds, uploadRiotIdsByImage, parseText}