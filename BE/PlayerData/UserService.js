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
        response.status(200).end();
    } else {
        response.status(403).end();
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
        response.status(200).end();
    } else {
        response.status(403).end();
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
        response.status(200).end();
    } else {
        response.status(403).end();
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
        response.status(200).end();
    } else {
        response.status(403).end();
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
        response.status(200).end();
    } else {
        response.status(403).end();
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
        
                dbo.collection("playerdb").updateOne({ _id: player },{ $push: { comments: {poster, date: today, comment} }}, {upsert: true}, function(err, res) {
                    if (err) throw err;
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
                    response.json(cleanedRes).status(200);
                    db.close();
                } else {
                    response.status(404).end();
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

router.get('/get_token/:riotId', async(req, res) => {
    let riotId = req.params.riotId

    getUserToken(riotId).then((result) => {
        res.json(result[0].firebaseToken)
        res.status(200)
    })
})

router.post('/add_user', async(req, res) => {
    let googleId = req.body.googleId
    let riotName = req.body.riotId
    let token = req.body.token

    addRegisteredUser(googleId, riotName, token).then(() => {
        res.json('added user')
        res.status(200)
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

async function getUserToken(riotId) {
    return new Promise((resolve) => {
        MongoClient.MongoClient.connect(url, function(err, db) {
            if (err) throw err;
            var dbo = db.db("playerdb");
    
            dbo.collection("userdb").find({ username: riotId }).toArray(function(err, res) {
                if (err) throw err
                db.close()
                return resolve(res)
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
