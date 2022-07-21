const MongoClient = require("mongodb");
const express = require("express");
const router = express.Router()
require('dotenv').config()
var url = process.env.MONGO_URL;
const DataHandlerModule = require('./DataHandler');
const getPlayerMasteries = DataHandlerModule.getPlayerMasteries;


// LIKE player
router.post('/like', (req, response) => {
    let player = req.body.name;

    if (!player) {
        response.status(400).json("Username not received");
    } else {
        MongoClient.MongoClient.connect(url, function(err, db) {
            if (err) throw err;
            var dbo = db.db("playerdb");
    
            dbo.collection("playerdb").updateOne({ _id: player },{ $inc: {likes: 1} }, {upsert: true}, function(err, res) {
                if (err) throw err;
                console.log("Liked player");
                response.json(res).status(200);
                db.close();
            });
        });
    }
})

// UNLIKE player
router.post('/unlike', (req, response) => {
    let player = req.body.name;

    if (!player) {
        response.status(400).json("Username not received");
    } else {
        MongoClient.MongoClient.connect(url, function(err, db) {
            if (err) throw err;
            var dbo = db.db("playerdb");
    
            dbo.collection("playerdb").updateOne({ _id: player },{ $inc: {likes: -1} }, {upsert: true}, function(err, res) {
                if (err) throw err;
                console.log("Unliked player");
                response.json(res).status(200);
                db.close();
            });
        });
    }
})

// DISLIKE player
router.post('/dislike', (req, response) => {
    let player = req.body.name;

    if (!player) {
        response.status(400).json("Username not received");
    } else {
        MongoClient.MongoClient.connect(url, function(err, db) {
            if (err) throw err;
            var dbo = db.db("playerdb");
    
            dbo.collection("playerdb").updateOne({ _id: player },{ $inc: {dislikes: 1} }, {upsert: true}, function(err, res) {
                if (err) throw err;
                console.log("Disliked player");
                response.json(res).status(200);
                db.close();
            });
        });
    }
})

// UNDISLIKE player
router.post('/undislike', (req, response) => {
    let player = req.body.name;

    if (!player) {
        response.status(400).json("Username not received");
    } else {
        MongoClient.MongoClient.connect(url, function(err, db) {
            if (err) throw err;
            var dbo = db.db("playerdb");
    
            dbo.collection("playerdb").updateOne({ _id: player },{ $inc: {dislikes: -1} }, {upsert: true}, function(err, res) {
                if (err) throw err;
                console.log("undisliked player");
                response.json(res).status(200);
                db.close();
            });
        });
    }
})

// COMMENT on player
router.post('/comment', (req, response) => {
    let player = req.body.name;
    let poster = req.body.poster;

    let today = new Date();
    let dd = String(today.getDate()).padStart(2, '0');
    let mm = String(today.getMonth() + 1).padStart(2, '0');
    let yyyy = today.getFullYear();

    today = mm + '/' + dd + '/' + yyyy;

    let comment = req.body.comment;

    if (!player || !comment) {
        response.status(400).json("Username and/or comment cannot be null");
    } else {
        MongoClient.MongoClient.connect(url, function(err, db) {
            if (err) throw err;
            var dbo = db.db("playerdb");
    
            dbo.collection("playerdb").updateOne({ _id: player },{ $push: { comments: {poster: poster, date: today, comment: comment} }}, {upsert: true}, function(err, res) {
                if (err) throw err;
                console.log("Added comment");
                response.json(res).status(200);
                db.close();
            });
        });
    }
})

// GET PLAYER
router.get('/getPlayer/:name', async (req, response) => {
    let player = req.params.name;
    player = player.replace("%20", /\s/g);

    if (!player) {
        response.send("Must specify player name").status(400);
    } else {
        MongoClient.MongoClient.connect(url, (err, db) => {
            if (err) throw err;
            var dbo = db.db("playerdb");
            dbo.collection("playerdb").findOne({_id: player}, function(err, res) {
                if (err) throw err;
                let cleanedRes = {
                    _id: res._id,
                    likes: res.likes ? res.likes : 0,
                    dislikes: res.dislikes ? res.dislikes : 0,
                    comments: res.comments ? res.comments : []
                }
                console.log(cleanedRes);
                response.json(cleanedRes).status(200);
                db.close();
            });
        });
    }
})

// POST Player Mastery
router.post('/getMastery', async (req, res) => {
    let player = req.body.name;
    let region = req.body.region;
    let champ = req.body.champ;

    getPlayerMasteries(player, region, champ).then(masteries => {
        res.json(masteries).status(200);
    })

})

// Separate function that can be called from player profile class
async function getFromDB(player) {

    return new Promise(resolve => {
        MongoClient.MongoClient.connect(url, (err, db) => {
            if (err) throw err;
            var dbo = db.db("playerdb");
            dbo.collection("playerdb").findOne({_id: player}, function(err, res) {
                if (err) throw err;
                if (!res) {
                    resolve({
                        _id: player,
                        likes: 0,
                        dislikes: 0,
                        comments: []
                    })
                } else {    
                    resolve({
                        _id: res._id,
                        likes: res.likes ? res.likes : 0,
                        dislikes: res.dislikes ? res.dislikes : 0,
                        comments: res.comments ? res.comments : []
                    });
                }
                db.close();
            });
        });
    
    })

}

module.exports = { router, getFromDB};