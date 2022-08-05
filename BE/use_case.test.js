const DataHandler = require("./PlayerData/DataHandler.js")
const UserService = require("./PlayerData/UserService.js")
const UsernameProcessor = require('./ImageRecognition/UsernameProcessorController')
const fs = require('fs')

beforeAll(() => jest.setTimeout(500))
jest.setTimeout(30000);

// tests without mocking

describe('Review Player', () => {
    test("postLike() - valid inputs", async () => {    
        await UserService.addRegisteredUser("test_googleId", "test_riot", "test_token");
        let registered = await UserService.checkIfRegisteredUser("test_googleId");
        expect(registered).toBe(true);
        let success = await UserService.sendLikeToDb("test_player", "test_googleId");
        expect(success).toBe(true);
    })

    test("postUnlike() - valid inputs", async () => {    
        await UserService.addRegisteredUser("test_googleId", "test_riot", "test_token");
        let registered = await UserService.checkIfRegisteredUser("test_googleId");
        expect(registered).toBe(true);
    
        // Like a player
        let success = await UserService.sendLikeToDb("test_player", "test_googleId");
        expect(success).toBe(true);
    
        // Then unlike them
        let unlikeSuccess = await UserService.sendUnlikeToDb("test_player", "test_googleId")
        expect(unlikeSuccess).toBe(true);
    })

    test("postDislike() - valid inputs", async () => {
        await UserService.addRegisteredUser("test_googleId", "test_riot", "test_token");
        let registered = await UserService.checkIfRegisteredUser("test_googleId");
        expect(registered).toBe(true);
        let success = await UserService.sendDislikeToDb("test_player", "test_googleId");
        expect(success).toBe(true);
    })

    test("postUndislike() - valid inputs", async () => {    
        await UserService.addRegisteredUser("test_googleId", "test_riot", "test_token");
        let registered = await UserService.checkIfRegisteredUser("test_googleId");
        expect(registered).toBe(true);
    
        // Dislike a player
        let success = await UserService.sendDislikeToDb("test_player", "test_googleId");
        expect(success).toBe(true);
    
        // Then undislike them
        let unlikeSuccess = await UserService.sendUndislikeToDb("test_player", "test_googleId")
        expect(unlikeSuccess).toBe(true);
    })

    test("getFromDB() - valid inputs, has never been reviewed", async () => {    
        let profile = await UserService.getFromDB("test_player");
        expect(profile._id).toBe("test_player");
        expect(profile.likes.length).toBe(0);
        expect(profile.dislikes.length).toBe(0);
        expect(profile.comments.length).toBe(0);
    })
    
    test("getFromDB() - valid inputs, has been reviewed", async () => {    
        await UserService.addRegisteredUser("test_googleId", "test_riot", "test_token");
        let registered = await UserService.checkIfRegisteredUser("test_googleId");
        expect(registered).toBe(true);
        // Send a like to player
        let liked = await UserService.sendLikeToDb("test_player", "test_googleId");
        expect(liked).toBe(true);
        
        // Send a dislike to player
        let disliked = await UserService.sendDislikeToDb("test_player", "test_googleId");
        expect(disliked).toBe(true);
    
        // Write comment on player
        let commented1 = await UserService.sendCommentToDb("test_player", "Josha", "test_googleId", "comment1");
        expect(commented1).toBe(true);
        let commented2 = await UserService.sendCommentToDb("test_player", "Josha", "test_googleId", "comment2");
        expect(commented2).toBe(true);
    
        let today = new Date();
        let dd = String(today.getDate()).padStart(2, '0');
        let mm = String(today.getMonth() + 1).padStart(2, '0');
        let yyyy = today.getFullYear();
    
        today = mm + '/' + dd + '/' + yyyy;
    
        let profile = await UserService.getFromDB("test_player");
        expect(profile._id).toBe("test_player");
        expect(profile.likes[0]).toBe("test_googleId");
        expect(profile.dislikes[0]).toBe("test_googleId");
        expect(profile.comments[0].comment).toBe("comment1");
        expect(profile.comments[1].comment).toBe("comment2");
    })

    test("postComment() - valid inputs, empty comment list", async () => {    
        await UserService.addRegisteredUser("test_googleId", "test_riot", "test_token");
        let registered = await UserService.checkIfRegisteredUser("test_googleId");
        expect(registered).toBe(true);
        let success = await UserService.sendCommentToDb("test_player", "Josha", "test_googleId", "comment");
        expect(success).toBe(true);
    })

    test("postComment() - valid inputs, existing comment list", async () => {    
        await UserService.addRegisteredUser("test_googleId", "test_riot", "test_token");
        let registered = await UserService.checkIfRegisteredUser("test_googleId");
        expect(registered).toBe(true);
        let success = await UserService.sendCommentToDb("test_player", "Josha", "test_googleId", "comment");
        expect(success).toBe(true);
        let success2 = await UserService.sendCommentToDb("test_player", "Josha", "test_googleId", "comment2");
        expect(success2).toBe(true);
    })

    afterEach(async () => {
        const UserService = jest.requireActual('./PlayerData/UserService.js');
        await UserService.cleanupTests();
        let exists = await UserService.checkIfRegisteredUser("test_googleId");
        expect(exists).toBe(false);
     });
})

describe('View Game Prediction', () => {
    test('uploadRiotIdsByImage() - invalid image', async () => {
        const UsernameProcessorController = jest.requireActual('./ImageRecognition/UsernameProcessorController.js')
        const TeamStats = jest.requireActual('./Prediction/TeamStats.js')
    
        UsernameProcessorController.parseText('./ImageRecognition/username_img.png').then(async (ids) => {
            if (ids.length != 5) {
                res.status(400)
                res.json("All 5 riot ids must be visible")
                return
            }
    
            try {
                await TeamStats.TeamStats(ids, "NA1")
            } catch(e) {
                expect(e).toMatch("Username cannot be found")
            }
        })
    })

    test('uploadRiotIdsByImage() - valid image', async () => {
        const UsernameProcessorController = jest.requireActual('./ImageRecognition/UsernameProcessorController.js')
        const TeamStats = jest.requireActual('./Prediction/TeamStats.js')
    
        UsernameProcessorController.parseText('./ImageRecognition/username_img.png').then(async (ids) => {
            if (ids.length != 5) {
                res.status(400)
                res.json("All 5 riot ids must be visible")
                return
            }
    
            const teamStats = await TeamStats.TeamStats(ids, "NA1")
            expect(teamStats.prediction).toBeGreaterThanOrEqual(0)
        })
    })

    test('Invalid Region', async () => {
        req = {
            body : {
                id1: '2 4', 
                id2: 'Aarontandude', 
                id3: 'IronAetos', 
                id4: 'WatchMeGank', 
                id5: 'zAddyy', 
                region: "INVALID REGION"
            }
        }

        res = {
            json_: undefined, 
            status_: undefined, 
            json: function(teamStats) {
                res.json_ = teamStats
            }, 
            status: function(status) {
                res.status_ = status
            }
        }
        
        UsernameProcessor.uploadRiotIds(req, res)
        expect(res.json_).toBe("Invalid Region - NA1 region is only supported currently")
        expect(res.status_).toBe(400)
    })

    test('player not found', async () => {
        const TeamStats = jest.requireActual('./Prediction/TeamStats.js')
        const names = ['wlkejrwlkefjlwkef', 'ItsKaia', 'Ksnyde', 'yasuways', 'BHboy']
        
        try {
            await TeamStats.TeamStats(names, "NA1")
        } catch(e) {
            expect(e).toMatch("Username cannot be found")
        }
    })

    test('all valid ids and region', async () => {
        const TeamStats = jest.requireActual('./Prediction/TeamStats.js')

        const names = ['wela34', 'ItsKaia', 'Ksnyde', 'yasuways', 'BHboy']

        const teamStats = await TeamStats.TeamStats(names, "NA1")
        expect(teamStats.prediction).toBeGreaterThanOrEqual(0)
    })
})

describe('View Player Profile', () => {
    test('Test getPlayerMasteries() interface - Valid champ, valid region', async () => {    
        const DataHandler = jest.requireActual('./PlayerData/DataHandler.js');

        DataHandler.getPlayerMasteries("2 4", "NA1", "Alistar").then(res => {
            expect(res["top1"]).toBe("Irelia")
            expect(res["top2"]).toBe("Riven")
            expect(res["top3"]).toBe("Leblanc")
            expect(res["playTime"]).toBe("low")
            expect(res["mastery"]).toBeGreaterThanOrEqual(0)
        })
    })

    test('Test getPlayerMasteries() interface - non existent username', async () => {
        const DataHandler = jest.requireActual('./PlayerData/DataHandler.js');

        try {
            await DataHandler.getPlayerMasteries("31410sdfs", "NA1", "Aphelios")
        } catch(e) {
            expect(e).toMatch("Username cannot be found")
        }
    })
})
test("Filler", () => {
    expect(1).toBe(1)
})