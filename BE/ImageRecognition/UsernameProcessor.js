const express = require('express')
const router = express.Router()
const fs = require('fs')
const vision = require('@google-cloud/vision');
const client = new vision.ImageAnnotatorClient({
    keyFilename: 'vision-key.json'
});
const stats = require('../Prediction/TeamStats')

const JOINED_THE_LOBBY = " joined the lobby"

// Upload riot ids
router.post('/usernames', (req, res) => {
    const ids = []
    ids[0] = req.body.id1
    ids[1] = req.body.id2
    ids[2] = req.body.id3
    ids[3] = req.body.id4
    ids[4] = req.body.id5

    const region = req.body.region

    if (!region) {
        res.status(400).json("Region must be specified")
    }

    if (!ids[0] || !ids[1] || !ids[2] || !ids[3] || !ids[4]) {
        res.status(400).json("All riot ids must be specified")
    } else {
        // TODO: send riot ids to team stats
        res.status(200).json(ids)
    }
})

// Upload image to parse riot ids
router.get('/', async (req, res) => {
    const base64EncodedImage = req.base64EncodedImage
    const data = Buffer.from(base64EncodedImage, 'base64')
    const region = req.body.region

    if (!region) {
        res.status(400).json("Region must be specified")
        return
    }
    
    fs.writeFile('usernames.png', data, (err) => {
        if (err) {
            res.status(500).json(err)
            return
        } else {
            console.log('successfully wrote file')
        }
    })

    await parseText('usernames.png').then((ids) => {
        // TODO: send riot ids to team stats and return response
        console.log(ids)
    })

    fs.unlink('usernames.png', (err) => {
        if (err) {
            res.status(500).json(err)
        } else {
            console.log('successfully deleted file')
        }
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

    return ids
}

module.exports = router