const MongoClient = require("mongodb");
const express = require("express");
const router = express.Router()
require('dotenv').config()
var url = process.env.MONGO_URL;
const DataHandlerModule = require('./DataHandler');
const getPlayerMasteries = DataHandlerModule.getPlayerMasteries;


// LIKE player
router.post('/like', async (req, response) => {
    let player = req.body.name;
    let googleId = req.body.googleid;
    let success = await sendLikeToDb(player, googleId)

    if (success) {
        response.status(200);
    } else {
        response.status(403);
    }
})

async function sendLikeToDb(player, googleId) {
    if (!player || !googleId) {
        return false;
    }

    let registered = await checkIfRegisteredUser(googleId);

    return new Promise((resolve) => {
        if (!registered) {
            return resolve(false);
        }
        
        if (!player) {
            return resolve(false);
        } else {
            MongoClient.MongoClient.connect(url, function(err, db) {
                if (err) throw err;
                var dbo = db.db("playerdb");
        
                dbo.collection("playerdb").updateOne({ _id: player },{ $push: {likes: googleId} }, {upsert: true}, function(err, res) {
                    if (err) throw err;
                    console.log("Liked player");
                    db.close();
                    return resolve(true);
                });
            });
        }
    })

}

// UNLIKE player
router.post('/unlike', async (req, response) => {
    let player = req.body.name;
    let googleId = req.body.googleid;

    let success = await sendUnlikeToDb(player, googleId);
    if (success) {
        response.status(200);
    } else {
        response.status(403);
    }
})

async function sendUnlikeToDb(player, googleId) {
    if (!player || !googleId) {
        return false;
    }

    let registered = await checkIfRegisteredUser(googleId);

    return new Promise((resolve) => {
        if (!registered) {
            return resolve(false);
        }
        
        if (!player) {
            return resolve(false);
        } else {
            MongoClient.MongoClient.connect(url, function(err, db) {
                if (err) throw err;
                var dbo = db.db("playerdb");
        
                dbo.collection("playerdb").updateOne({ _id: player },{ $pull: {likes: googleId} }, {upsert: true}, function(err, res) {
                    if (err) throw err;
                    console.log("Unliked player");
                    db.close();
                    return resolve(true);
                });
            });
        }
    })
}

// DISLIKE player
router.post('/dislike', async (req, response) => {
    let player = req.body.name;
    let googleId = req.body.googleid;

    let success = await sendDislikeToDb(player, googleId);
    if (success) {
        response.status(200);
    } else {
        response.status(403);
    }
})

async function sendDislikeToDb(player, googleId) {
    if (!player || !googleId) {
        return false;
    }

    let registered = await checkIfRegisteredUser(googleId);

    return new Promise((resolve) => {
        if (!registered) {
            return resolve(false);
        }
        
        if (!player) {
            return resolve(false);
        } else {
            MongoClient.MongoClient.connect(url, function(err, db) {
                if (err) throw err;
                var dbo = db.db("playerdb");
        
                dbo.collection("playerdb").updateOne({ _id: player },{ $push: {dislikes: googleId} }, {upsert: true}, function(err, res) {
                    if (err) throw err;
                    console.log("Disliked player");
                    db.close();
                    return resolve(true);
                });
            });
        }
    })
}

// UNDISLIKE player
router.post('/undislike', async (req, response) => {
    let player = req.body.name;
    let googleId = req.body.googleid;

    let success = await sendUndislikeToDb(player, googleId);
    if (success) {
        response.status(200);
    } else {
        response.status(403);
    }
})

async function sendUndislikeToDb(player, googleId) {
    if (!player || !googleId) {
        return false;
    }

    let registered = await checkIfRegisteredUser(googleId);

    return new Promise((resolve) => {
        if (!registered) {
            return resolve(false);
        }
        
        if (!player) {
            return resolve(false);
        } else {
            MongoClient.MongoClient.connect(url, function(err, db) {
                if (err) throw err;
                var dbo = db.db("playerdb");
        
                dbo.collection("playerdb").updateOne({ _id: player },{ $pull: {dislikes: googleId} }, {upsert: true}, function(err, res) {
                    if (err) throw err;
                    console.log("Undisliked player");
                    db.close();
                    return resolve(true);
                });
            });
        }
    })
}

// COMMENT on player
router.post('/comment', async (req, response) => {
    let player = req.body.name;
    let poster = req.body.poster;
    let googleId = req.body.googleid;
    let comment = req.body.comment;

    let success = await sendCommentToDb(player, poster, googleId, comment);
    if (success) {
        response.status(200);
    } else {
        response.status(403);
    }
})

async function sendCommentToDb(player, poster, googleId, comment) {
    if (!player || !poster || !googleId || !comment || comment.length > 250) {
        return false;
    }

    let today = new Date();
    let dd = String(today.getDate()).padStart(2, '0');
    let mm = String(today.getMonth() + 1).padStart(2, '0');
    let yyyy = today.getFullYear();

    today = mm + '/' + dd + '/' + yyyy;

    let registered = await checkIfRegisteredUser(googleId);

    return new Promise((resolve) => {
        if (!registered) {
            return resolve(false);
        }
        
        if (!player) {
            return resolve(false);
        } else {
            MongoClient.MongoClient.connect(url, function(err, db) {
                if (err) throw err;
                var dbo = db.db("playerdb");
        
                dbo.collection("playerdb").updateOne({ _id: player },{ $push: { comments: {poster: poster, date: today, comment: comment} }}, {upsert: true}, function(err, res) {
                    if (err) throw err;
                    console.log("Added comment");
                    db.close();
                    return resolve(true)
                });
            });
        }
    })
}

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
                if (res) {
                    let cleanedRes = {
                        _id: res._id,
                        likes: res.likes ? res.likes : [],
                        dislikes: res.dislikes ? res.dislikes : [],
                        comments: res.comments ? res.comments : []
                    }
                    console.log(cleanedRes);
                    response.json(cleanedRes).status(200);
                    db.close();
                } else {
                    response.status(404);
                }
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
        if (!player) {
            throw "Must include name parameter"
        }
        MongoClient.MongoClient.connect(url, (err, db) => {
            if (err) throw err;
            var dbo = db.db("playerdb");
            dbo.collection("playerdb").findOne({_id: player}, function(err, res) {
                if (err) throw err;
                if (!res) {
                    resolve({
                        _id: player,
                        likes: [],
                        dislikes: [],
                        comments: []
                    })
                } else {    
                    resolve({
                        _id: res._id,
                        likes: res.likes ? res.likes : [],
                        dislikes: res.dislikes ? res.dislikes : [],
                        comments: res.comments ? res.comments : []
                    });
                }
                db.close();
            });
        });
    
    })

}

// Returns null if user doesn't exist
async function checkIfRegisteredUser(googleId) {
    return new Promise((resolve) => {
        MongoClient.MongoClient.connect(url, function(err, db) {
            if (err) throw err;
            var dbo = db.db("playerdb");
    
            dbo.collection("userdb").findOne({ _id: googleId }, async function(err, res) {
                if (err) throw err;
                db.close();
                if (res) {
                    return resolve(true);
                } else {
                    return resolve(false);
                }
            });
        });
    })
}

async function addRegisteredUser(googleId, riotName, token) {
    return new Promise((resolve) => {
        MongoClient.MongoClient.connect(url, function(err, db) {
            if (err) throw err;
            var dbo = db.db("playerdb");
    
            dbo.collection("userdb").updateOne({ _id: googleId },{$set: { username: riotName, firebaseToken: token}}, {upsert: true}, function(err, res) {
                if (err) throw err;
                resolve(res);
                db.close();
            });
        });
    })
}

async function removeRegisteredUser(googleId) {
    return new Promise((resolve) => {
        MongoClient.MongoClient.connect(url, function(err, db) {
            if (err) throw err;
            var dbo = db.db("playerdb");
    
            dbo.collection("userdb").deleteOne({ _id: googleId }, function(err, res) {
                if (err) throw err;
                resolve(res);
                db.close();
            });
        });
    })
}

async function cleanupTests() {
    return new Promise((resolve) => {
        MongoClient.MongoClient.connect(url, function(err, db) {
            if (err) throw err;
            var dbo = db.db("playerdb");
    
            dbo.collection("playerdb").deleteOne({ _id: "test_player" }, function(err, res) {
                if (err) throw err;

                dbo.collection("userdb").deleteOne({ _id: "test_googleId" }, function(err, res) {
                    if (err) throw err;
                    db.close();
                    resolve(true);
                });
            });
            

        });
    })
}

// addRegisteredUser("Bamal", "abc12", "yomama")
// checkIfRegisteredUser("Jamal")
// removeRegisteredUser("Jamal")
// curl http://localhost:8080/playerdb/getPlayer/Jim
// curl -d '{"name":"josha", "googleid":"abdd"}' -H "Content-Type: application/json" -X POST http://localhost:8080/playerdb/like

module.exports = { 
    router,
    getFromDB,
    addRegisteredUser,
    checkIfRegisteredUser,
    removeRegisteredUser,
    sendLikeToDb,
    sendUnlikeToDb,
    sendDislikeToDb,
    sendUndislikeToDb,
    sendCommentToDb,
    cleanupTests
};
