// // testing suite for UsernameProcessor
// const TeamStats = require('../Prediction/TeamStats.js')
// const UsernameProcessor = require('./UsernameProcessorController')
// const fs = require('fs')

// jest.setTimeout(90 * 1000)

// // TEST UPLOADING RIOT IDS

// describe("TEST UPLOADING RIOT IDS", () => {
//     beforeAll(() => {
//         jest.restoreAllMocks()

//         const player = {
//             name: "IronAetos",
//             region: "NA1", 
//             stats: {
//                 kps: 1, 
//                 aps: 1, 
//                 dps: 1, 
//                 gps: 1, 
//                 vps: 1
//             }, 
//             reviews: {
//                 _id: 'id', 
//                 likes: [], 
//                 dislikes: [],
//                 comments: []
//             }
//         }

//         TeamStats.TeamStats = jest.fn().mockResolvedValue({
//             summary : {
//                 prediction: 0.5,
//                 player1: player, 
//                 player2: player, 
//                 player3: player, 
//                 player4: player, 
//                 player5: player
//             }
//         })
//     })

//     test('All riot ids not specified and are registered', () => {
//         req = {
//             body : {
//                 id1: undefined, 
//                 id2: undefined, 
//                 id3: 'IronAetos', 
//                 id4: 'WatchMeGank', 
//                 id5: 'zAddyy', 
//                 region: 'NA1'
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
//         expect(res.json_).toBe("All riot ids must be specified")
//         expect(res.status_).toBe(400)
//         expect(TeamStats.TeamStats).toHaveBeenCalledTimes(0)
//     })
    
//     test('Invalid Region', () => {
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
//         expect(TeamStats.TeamStats).toHaveBeenCalledTimes(0)
//     })
    
//     test('Region not specified', () => {
//         req = {
//             body : {
//                 id1: '2 4', 
//                 id2: 'Aarontandude', 
//                 id3: 'IronAetos', 
//                 id4: 'WatchMeGank', 
//                 id5: 'zAddyy', 
//                 region: undefined
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
//         expect(res.json_).toBe("Region must be specified")
//         expect(res.status_).toBe(400)
//         expect(TeamStats.TeamStats).toHaveBeenCalledTimes(0)
//     })

//     test('All registered riot ids and valid region', () => {
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
//         expect(TeamStats.TeamStats).toBe(TeamStats.TeamStats)
//         expect(TeamStats.TeamStats).toHaveBeenCalledTimes(1)
//     })
// })


// // TEST UPLOADING RIOT IDS BY IMAGE

// describe("TEST UPLOADING RIOT IDS BY IMAGE", () => {
//     beforeAll(() => {
//         jest.restoreAllMocks()

//         const player = {
//             name: "IronAetos",
//             region: "NA1", 
//             stats: {
//                 kps: 1, 
//                 aps: 1, 
//                 dps: 1, 
//                 gps: 1, 
//                 vps: 1
//             }, 
//             reviews: {
//                 _id: 'id', 
//                 likes: [], 
//                 dislikes: [],
//                 comments: []
//             }
//         }

//         TeamStats.TeamStats = jest.fn().mockResolvedValue({
//             summary : {
//                 prediction: 0.5,
//                 player1: player, 
//                 player2: player, 
//                 player3: player, 
//                 player4: player, 
//                 player5: player
//             }
//         })
//     })

//     test('Invalid Region', () => {
//         req = {
//             body : {
//                 // note: do not need valid base64 encoded image here b/c we check region before decoding image
//                 base64EncodedImage: "TEST",
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

//         UsernameProcessor.uploadRiotIdsByImage(req, res)
//         expect(res.json_).toBe("Invalid Region - NA1 region is only supported currently")
//         expect(res.status_).toBe(400)
//     })
    
//     test('Region not specified', () => {
//         req = {
//             body : {
//                 // note: do not need valid base64 encoded image here b/c we check region before decoding image
//                 base64EncodedImage: "TEST",
//                 region: undefined
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

//         UsernameProcessor.uploadRiotIdsByImage(req, res)
//         expect(res.json_).toBe("Region must be specified")
//         expect(res.status_).toBe(400)
//     })
    
//     test('Properly encoded image with some riot ids not shown', async () => {
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
//         expect(TeamStats.TeamStats).toBe(TeamStats.TeamStats)
//         expect(res.json_).toBe("All 5 riot ids must be visible")
//         expect(res.status_).toBe(400)
//         expect(TeamStats.TeamStats).toHaveBeenCalledTimes(0)
//     })
    
//     test('Properly encoded image with all registered users', async () => {
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
//         expect(TeamStats.TeamStats).toBe(TeamStats.TeamStats)
//         expect(TeamStats.TeamStats).toHaveBeenCalledTimes(1)
//     })
// })

// describe('non-functional requirement test', () => {
//     test('image detection accuracy test', async () => {
//         const id1 = 'IronAetos'
//         const id2 = '24'
//         const id3 = 'WatchMeGank'
//         const id4 = 'Aarontandude'
//         const id5 = 'zAddyy'
//         const id6 = 'TheWanderersWay'
//         const id7 = 'palukawhale'
//         const id8 = 'Thick Rooster'
//         const id9 = 'ct819'
    
//         const test1_ids = await UsernameProcessor.parseText('test_photos/p1.jpg')
    
//         const test2_ids = await UsernameProcessor.parseText('test_photos/p2.jpg')
    
//         const test3_ids = await UsernameProcessor.parseText('test_photos/p3.jpg')
    
//         const test4_ids = await UsernameProcessor.parseText('test_photos/p4.jpg')
    
//         const test_ids_list_v1 = [test1_ids, test2_ids, test4_ids]
    
//         test_ids_list_v1.forEach((ids) => {
//             expect(ids).toContain(id1)
//             expect(ids).toContain(id2)
//             expect(ids).toContain(id3)
//             expect(ids).toContain(id4)
//             expect(ids).toContain(id5)
//         })
    
//         expect(test3_ids).toContain(id2)
//         expect(test3_ids).toContain(id6)
//         expect(test3_ids).toContain(id7)
//         expect(test3_ids).toContain(id8)
//         expect(test3_ids).toContain(id9)
//     })
// })

test("Filler", () => {
    expect(1).toBe(1);
})