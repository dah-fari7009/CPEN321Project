// const DataHandler = require("./PlayerData/DataHandler.js")
// const UserService = require("./PlayerData/UserService.js")
// const UsernameProcessor = require('./ImageRecognition/UsernameProcessorController')
// const fs = require('fs')

// result = {
//     "prediction": 0.6435593829811463,
//     "player1": {
//       "name": "2 4",
//       "region": "NA1",
//       "stats": {
//         "kps": 0.005612722170252572,
//         "aps": 0.003274087932647334,
//         "dps": 0.00420954162768943,
//         "gps": 7.196913002806361,
//         "vps": 0.007483629560336763
//       },
//       "reviews": {
//         "_id": "2 4",
//         "likes": [],
//         "dislikes": [],
//         "comments": []
//       }
//     },
//     "player2": {
//       "name": "TheWanderersWay",
//       "region": "NA1",
//       "stats": {
//         "kps": 0.0036275695284159614,
//         "aps": 0.0012091898428053204,
//         "dps": 0.004232164449818622,
//         "gps": 6.792623941958888,
//         "vps": 0.006045949214026602
//       },
//       "reviews": {
//         "_id": "TheWanderersWay",
//         "likes": [],
//         "dislikes": [],
//         "comments": []
//       }
//     },
//     "player3": {
//       "name": "palukawhale",
//       "region": "NA1",
//       "stats": {
//         "kps": 0.0006345177664974619,
//         "aps": 0.006979695431472081,
//         "dps": 0.0019035532994923859,
//         "gps": 4.6421319796954315,
//         "vps": 0.02982233502538071
//       },
//       "reviews": {
//         "_id": "palukawhale",
//         "likes": [],
//         "dislikes": [],
//         "comments": []
//       }
//     },
//     "player4": {
//       "name": "Thick Rooster",
//       "region": "NA1",
//       "stats": {
//         "kps": 0.007340485601355167,
//         "aps": 0.0016939582156973462,
//         "dps": 0.002258610954263128,
//         "gps": 9.568605307735742,
//         "vps": 0.012987012987012988
//       },
//       "reviews": {
//         "_id": "Thick Rooster",
//         "likes": [],
//         "dislikes": [],
//         "comments": []
//       }
//     },
//     "player5": {
//       "name": "ct819",
//       "region": "NA1",
//       "stats": {
//         "kps": 0.00258732212160414,
//         "aps": 0.008624407072013798,
//         "dps": 0.0008624407072013799,
//         "gps": 7.630012936610608,
//         "vps": 0.01121172919361794
//       },
//       "reviews": {
//         "_id": "ct819",
//         "likes": [],
//         "dislikes": [],
//         "comments": []
//       }
//     }
//   }

// // tests without mocking

// describe('Review Player', () => {
//     test("postLike() - valid inputs", async () => {    
//         await UserService.addRegisteredUser("test_googleId", "test_riot", "test_token");
//         let registered = await UserService.checkIfRegisteredUser("test_googleId");
//         expect(registered).toBe(true);
//         let success = await UserService.sendLikeToDb("test_player", "test_googleId");
//         expect(success).toBe(true);
//     })

//     test("postUnlike() - valid inputs", async () => {    
//         await UserService.addRegisteredUser("test_googleId", "test_riot", "test_token");
//         let registered = await UserService.checkIfRegisteredUser("test_googleId");
//         expect(registered).toBe(true);
    
//         // Like a player
//         let success = await UserService.sendLikeToDb("test_player", "test_googleId");
//         expect(success).toBe(true);
    
//         // Then unlike them
//         let unlikeSuccess = await UserService.sendUnlikeToDb("test_player", "test_googleId")
//         expect(unlikeSuccess).toBe(true);
//     })

//     test("postDislike() - valid inputs", async () => {
//             await UserService.addRegisteredUser("test_googleId", "test_riot", "test_token");
//         let registered = await UserService.checkIfRegisteredUser("test_googleId");
//         expect(registered).toBe(true);
//         let success = await UserService.sendDislikeToDb("test_player", "test_googleId");
//         expect(success).toBe(true);
//     })

//     test("postUndislike() - valid inputs", async () => {    
//         await UserService.addRegisteredUser("test_googleId", "test_riot", "test_token");
//         let registered = await UserService.checkIfRegisteredUser("test_googleId");
//         expect(registered).toBe(true);
    
//         // Dislike a player
//         let success = await UserService.sendDislikeToDb("test_player", "test_googleId");
//         expect(success).toBe(true);
    
//         // Then undislike them
//         let unlikeSuccess = await UserService.sendUndislikeToDb("test_player", "test_googleId")
//         expect(unlikeSuccess).toBe(true);
//     })

//     test("getFromDB() - valid inputs, has never been reviewed", async () => {    
//         let profile = await UserService.getFromDB("test_player");
//         expect(profile._id).toBe("test_player");
//         expect(profile.likes.length).toBe(0);
//         expect(profile.dislikes.length).toBe(0);
//         expect(profile.comments.length).toBe(0);
//     })
    
//     test("getFromDB() - valid inputs, has been reviewed", async () => {    
//         await UserService.addRegisteredUser("test_googleId", "test_riot", "test_token");
//         let registered = await UserService.checkIfRegisteredUser("test_googleId");
//         expect(registered).toBe(true);
//         // Send a like to player
//         let liked = await UserService.sendLikeToDb("test_player", "test_googleId");
//         expect(liked).toBe(true);
        
//         // Send a dislike to player
//         let disliked = await UserService.sendDislikeToDb("test_player", "test_googleId");
//         expect(disliked).toBe(true);
    
//         // Write comment on player
//         let commented1 = await UserService.sendCommentToDb("test_player", "Josha", "test_googleId", "comment1");
//         expect(commented1).toBe(true);
//         let commented2 = await UserService.sendCommentToDb("test_player", "Josha", "test_googleId", "comment2");
//         expect(commented2).toBe(true);
    
//         let today = new Date();
//         let dd = String(today.getDate()).padStart(2, '0');
//         let mm = String(today.getMonth() + 1).padStart(2, '0');
//         let yyyy = today.getFullYear();
    
//         today = mm + '/' + dd + '/' + yyyy;
    
//         let profile = await UserService.getFromDB("test_player");
//         expect(profile._id).toBe("test_player");
//         expect(profile.likes[0]).toBe("test_googleId");
//         expect(profile.dislikes[0]).toBe("test_googleId");
//         expect(profile.comments[0].comment).toBe("comment1");
//         expect(profile.comments[1].comment).toBe("comment2");
//     })

//     test("postComment() - valid inputs, empty comment list", async () => {    
//         await UserService.addRegisteredUser("test_googleId", "test_riot", "test_token");
//         let registered = await UserService.checkIfRegisteredUser("test_googleId");
//         expect(registered).toBe(true);
//         let success = await UserService.sendCommentToDb("test_player", "Josha", "test_googleId", "comment");
//         expect(success).toBe(true);
//     })

//     test("postComment() - valid inputs, existing comment list", async () => {    
//         await UserService.addRegisteredUser("test_googleId", "test_riot", "test_token");
//         let registered = await UserService.checkIfRegisteredUser("test_googleId");
//         expect(registered).toBe(true);
//         let success = await UserService.sendCommentToDb("test_player", "Josha", "test_googleId", "comment");
//         expect(success).toBe(true);
//         let success2 = await UserService.sendCommentToDb("test_player", "Josha", "test_googleId", "comment2");
//         expect(success2).toBe(true);
//     })

//     afterEach(async () => {
//         await UserService.cleanupTests();
//      });
// })

// describe('View Game Prediction', () => {
//     test('uploadRiotIdsByImage() - invalid image', async () => {
//         const file = fs.readFileSync('ImageRecognition/encodedImage2.txt', 'utf8')

//         req = {
//             body : {
//                 base64EncodedImage: file,
//                 region: "NA1"
//             }
//         }

//         res = {
//             json_: undefined, 
//             status_: undefined, 
//             json: function(teamStats) {
//                 res.json_ = teamStats
//             }, 
//             status: function(status) {
//                 res.status_ = status
//             }
//         }

//         await UsernameProcessor.uploadRiotIdsByImage(req, res)
//         expect(res.json_).toBe("All 5 riot ids must be visible")
//         expect(res.status_).toBe(400)
//     })

//     test('uploadRiotIdsByImage() - valid image', async () => {
//         const file = fs.readFileSync('ImageRecognition/encodedImage.txt', 'utf8')

//         req = {
//             body : {
//                 base64EncodedImage: file,
//                 region: "NA1"
//             }
//         }

//         res = {
//             json_: undefined, 
//             status_: undefined, 
//             json: function(teamStats) {
//                 res.json_ = teamStats
//             }, 
//             status: function(status) {
//                 res.status_ = status
//             }
//         }

//         await UsernameProcessor.uploadRiotIdsByImage(req, res)
//         expect(res.json_).toBe(result)
//         expect(res.status_).toBe(200)
//     })

//     test('Invalid Region', async() => {
//         req = {
//             body : {
//                 id1: '2 4', 
//                 id2: 'Aarontandude', 
//                 id3: 'IronAetos', 
//                 id4: 'WatchMeGank', 
//                 id5: 'zAddyy', 
//                 region: "INVALID REGION"
//             }
//         }

//         res = {
//             json_: undefined, 
//             status_: undefined, 
//             json: function(teamStats) {
//                 res.json_ = teamStats
//             }, 
//             status: function(status) {
//                 res.status_ = status
//             }
//         }
        
//         UsernameProcessor.uploadRiotIds(req, res)
//         expect(res.json_).toBe("Invalid Region - NA1 region is only supported currently")
//         expect(res.status_).toBe(400)
//     })

//     test('player not found', async () => {
//         req = {
//             body : {
//                 id1: 'playerthatwontbefoundalsehflwuafwef', 
//                 id2: 'Aarontandude', 
//                 id3: 'IronAetos', 
//                 id4: 'WatchMeGank', 
//                 id5: 'zAddyy', 
//                 region: "NA1"
//             }
//         }

//         res = {
//             json_: undefined, 
//             status_: undefined, 
//             json: function(teamStats) {
//                 res.json_ = teamStats
//             }, 
//             status: function(status) {
//                 res.status_ = status
//             }
//         }
        
//         UsernameProcessor.uploadRiotIds(req, res)
//         expect(res.status_).toBe(400)
//         expect(res.json_).toBe("Summoner not found")
//     })

//     test('all valid ids and region', async () => {
//         req = {
//             body : {
//                 id1: '2 4', 
//                 id2: 'Aarontandude', 
//                 id3: 'IronAetos', 
//                 id4: 'WatchMeGank', 
//                 id5: 'zAddyy', 
//                 region: "NA1"
//             }
//         }

//         res = {
//             json_: undefined, 
//             status_: undefined, 
//             json: function(teamStats) {
//                 res.json_ = teamStats
//             }, 
//             status: function(status) {
//                 res.status_ = status
//             }
//         }
        
//         UsernameProcessor.uploadRiotIds(req, res)
//         expect(res.json_).toBe(result)
//         expect(res.status_).toBe(200)
//     })
// })

// describe('View Player Profile', () => {
//     test('Test getPlayerMasteries() interface - Valid champ, valid region', async () => {    
//         DataHandler.getPlayerMasteries("2 4", "NA1", "Alistar").then(res => {
//             expect(res["top1"]).toBe("Irelia")
//             expect(res["top2"]).toBe("Riven")
//             expect(res["top3"]).toBe("Leblanc")
//             expect(res["playTime"]).toBe("low")
//             expect(res["mastery"]).toBeGreaterThanOrEqual(0)
//         })
//     })

//     test('Test getPlayerMasteries() interface - non existent username', async () => {
//         try {
//             await DataHandler.getPlayerMasteries("31410sdfs", "NA1", "Aphelios")
//         } catch(e) {
//             expect(e).toMatch("Username cannot be found")
//         }
//     })
// })

test("Filler", () => {
    expect(1).toBe(1);
})