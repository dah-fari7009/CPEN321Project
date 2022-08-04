const PlayerProfile = require("./PlayerProfile.js")
const DataHandler = require("./DataHandler.js")
const UserService = require("./UserService");

test('Test getMatchHistory() interface - valid username/region', () => {
    const DataHandler = jest.requireActual('./DataHandler.js');

    DataHandler.getMatchHistory("2 4", "NA1").then(res => {
        // Can't test for specific values since they will change after
        // games are played, but greater than 0 is indicator it worked
        expect(res["kps"]).toBeGreaterThanOrEqual(0);
        expect(res["aps"]).toBeGreaterThanOrEqual(0)
        expect(res["dps"]).toBeGreaterThanOrEqual(0);
        expect(res["gps"]).toBeGreaterThanOrEqual(0);
        expect(res["vps"]).toBeGreaterThanOrEqual(0);
    })
})

test('Test getMatchHistory() interface - null region', async () => {
    const DataHandler = jest.requireActual('./DataHandler.js');

    try {
        await DataHandler.getMatchHistory("2 4", "")
    } catch(e) {
        expect(e).toMatch("Must include all fields")
    }
})

test('Test getMatchHistory() interface - both null', async () => {
    const DataHandler = jest.requireActual('./DataHandler.js');

    try {
        await DataHandler.getMatchHistory("", "")
    } catch(e) {
        expect(e).toMatch("Must include all fields")
    }
})

test('Test getMatchHistory() interface - username doesnt exist', async () => {
    const DataHandler = jest.requireActual('./DataHandler.js');

    try {
        await DataHandler.getMatchHistory("2093810jdl", "NA1")
    } catch(e) {
        expect(e).toMatch("Username cannot be found")
    }
})

test('Test getMatchHistory() interface - region isnt real', async () => {
    const DataHandler = jest.requireActual('./DataHandler.js');

    try {
        await DataHandler.getMatchHistory("2 4", "not real region")
    } catch(e) {
        expect(e).toMatch("Region name doesn't exist")
    }
})

test('Test getPlayerMasteries() interface - Valid champ, valid region', () => {
    const DataHandler = jest.requireActual('./DataHandler.js');

    DataHandler.getPlayerMasteries("2 4", "NA1", "Alistar").then(res => {
        expect(res["top1"]).toBe("Irelia")
        expect(res["top2"]).toBe("Riven")
        expect(res["top3"]).toBe("Leblanc")
        expect(res["playTime"]).toBe("low")
        expect(res["mastery"]).toBeGreaterThanOrEqual(0)
    })
})

// // NOTE: for tests with non existing values, there may be Axios errors that get logged in console
// // this is expected behaviour, note the tests still pass.
test('Test getPlayerMasteries() interface - non existent username', async () => {
    const DataHandler = jest.requireActual('./DataHandler.js');

    try {
        await DataHandler.getPlayerMasteries("31410sdfs", "NA1", "Aphelios")
    } catch(e) {
        expect(e).toMatch("Username cannot be found")
    }
})

test('Test getPlayerMasteries() interface - one or more null params', async () => {
    const DataHandler = jest.requireActual('./DataHandler.js');

    try {
        await DataHandler.getPlayerMasteries("", "NA1", "Aphelios")
    } catch(e) {
        expect(e).toMatch("Must include all fields")
    }

    try {
        await DataHandler.getPlayerMasteries("2 4", "", "Aphelios")
    } catch(e) {
        expect(e).toMatch("Must include all fields")
    }

    try {
        await DataHandler.getPlayerMasteries("2 4", "NA1", "")
    } catch(e) {
        expect(e).toMatch("Must include all fields")
    }
})

jest.mock("./DataHandler.js")
jest.mock("./UserService.js")
test("getProfile() interface - valid inputs", async () => {
    DataHandler.getMatchHistory.mockResolvedValue(Promise.resolve({
        kps: 0.1,
        aps: 0.1,
        dps: 0.1,
        gps: 0.1,
        vps: 0.1
    }))
    UserService.getFromDB.mockResolvedValue(Promise.resolve({
        _id: "2 4",
        likes: ["user123"],
        dislikes: ["user456"],
        comments: []
    }))
    resp = await PlayerProfile.getProfile("2 4", "NA1");

    expect(resp.name).toBe("2 4");
    expect(resp.region).toBe("NA1");

    // Tests to make sure the mock is being used
    expect(resp.stats["kps"]).toBe(0.1);
    expect(resp.stats["aps"]).toBe(0.1);
    expect(resp.stats["dps"]).toBe(0.1);
    expect(resp.stats["gps"]).toBe(0.1);
    expect(resp.stats["vps"]).toBe(0.1);
    expect(resp.reviews["likes"][0]).toBe("user123")
})

test("getProfile() interface - null inputs", async () => {
    DataHandler.getMatchHistory.mockResolvedValue(Promise.resolve({
        kps: 0.1,
        aps: 0.1,
        dps: 0.1,
        gps: 0.1,
        vps: 0.1
    }))
    UserService.getFromDB.mockResolvedValue(Promise.resolve({
        _id: "2 4",
        likes: ["user123"],
        dislikes: ["user456"],
        comments: []
    }))

    // Test all combinations of null inputs    
    try {
        await PlayerProfile.getProfile("", "NA1");
    } catch(e) {
        expect(e).toMatch("Must include all fields")
    }
    try {
        await PlayerProfile.getProfile("2 4", "");
    } catch(e) {
        expect(e).toMatch("Must include all fields")
    }
    try {
        await PlayerProfile.getProfile("", "");
    } catch(e) {
        expect(e).toMatch("Must include all fields")
    }
})

test("getProfile() interface - non null, but invalid inputs", async () => {
    // Clears mocked return values from previous tests
    DataHandler.getMatchHistory.mockReset()
    UserService.getFromDB.mockReset()

    DataHandler.getMatchHistory.mockResolvedValue(Promise.resolve(null))
    UserService.getFromDB.mockResolvedValue(Promise.resolve(null))

    try {
        await PlayerProfile.getProfile("hqoqhrfolqwn", "NA1");
    } catch(e) {
        expect(e).toMatch("This player couldnt be found for the specified region. Check the spelling of the region.")
    }
    try {
        await PlayerProfile.getProfile("2 4", "sd,mnflsnf");
    } catch(e) {
        expect(e).toMatch("This player couldnt be found for the specified region. Check the spelling of the region.")
    }
})

test("postLike() - valid inputs", async () => {
    const UserService = jest.requireActual('./UserService.js');

    await UserService.addRegisteredUser("test_googleId", "test_riot", "test_token");
    let registered = await UserService.checkIfRegisteredUser("test_googleId");
    expect(registered).toBe(true);
    let success = await UserService.sendLikeToDb("test_player", "test_googleId");
    expect(success).toBe(true);
})

test("postLike() - no valid google ID", async () => {
    const UserService = jest.requireActual('./UserService.js');

    let success = await UserService.sendLikeToDb("test_player", "hacker_dude");
    expect(success).toBe(false);
})

test("postLike() - either input is null", async () => {
    const UserService = jest.requireActual('./UserService.js');

    let fail1 = await UserService.sendLikeToDb("", "test_googleId");
    expect(fail1).toBe(false);
    let fail2 = await UserService.sendLikeToDb("test_player", "");
    expect(fail2).toBe(false);
})

test("postUnlike() - valid inputs", async () => {
    const UserService = jest.requireActual('./UserService.js');

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

test("postUnlike() - non valid google ID", async () => {
    const UserService = jest.requireActual('./UserService.js');

    let success = await UserService.sendUnlikeToDb("test_player", "hacker_dude");
    expect(success).toBe(false);
})

test("postUnlike() - either input is null", async () => {
    const UserService = jest.requireActual('./UserService.js');

    let fail1 = await UserService.sendUnlikeToDb("", "test_googleId");
    expect(fail1).toBe(false);
    let fail2 = await UserService.sendUnlikeToDb("test_player", "");
    expect(fail2).toBe(false);
})

test("postDislike() - valid inputs", async () => {
    const UserService = jest.requireActual('./UserService.js');

    await UserService.addRegisteredUser("test_googleId", "test_riot", "test_token");
    let registered = await UserService.checkIfRegisteredUser("test_googleId");
    expect(registered).toBe(true);
    let success = await UserService.sendDislikeToDb("test_player", "test_googleId");
    expect(success).toBe(true);
})

test("postDislike() - no valid google ID", async () => {
    const UserService = jest.requireActual('./UserService.js');

    let success = await UserService.sendDislikeToDb("test_player", "hacker_dude");
    expect(success).toBe(false);
})

test("postDislike() - either input is null", async () => {
    const UserService = jest.requireActual('./UserService.js');

    let fail1 = await UserService.sendDislikeToDb("", "test_googleId");
    expect(fail1).toBe(false);
    let fail2 = await UserService.sendDislikeToDb("test_player", "");
    expect(fail2).toBe(false);
})

test("postUndislike() - valid inputs", async () => {
    const UserService = jest.requireActual('./UserService.js');

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

test("postUndislike() - non valid google ID", async () => {
    const UserService = jest.requireActual('./UserService.js');

    let success = await UserService.sendUndislikeToDb("test_player", "hacker_dude");
    expect(success).toBe(false);
})

test("postUndislike() - either input is null", async () => {
    const UserService = jest.requireActual('./UserService.js');

    let fail1 = await UserService.sendUndislikeToDb("", "test_googleId");
    expect(fail1).toBe(false);
    let fail2 = await UserService.sendUndislikeToDb("test_player", "");
    expect(fail2).toBe(false);
})

test("postComment() - valid inputs", async () => {
    const UserService = jest.requireActual('./UserService.js');

    await UserService.addRegisteredUser("test_googleId", "test_riot", "test_token");
    let registered = await UserService.checkIfRegisteredUser("test_googleId");
    expect(registered).toBe(true);
    let success = await UserService.sendCommentToDb("test_player", "Josha", "test_googleId", "comment");
    expect(success).toBe(true);
})

test("postComment() - no valid google ID", async () => {
    const UserService = jest.requireActual('./UserService.js');

    let success = await UserService.sendCommentToDb("test_player", "Josha", "hacker_dude", "comment");
    expect(success).toBe(false);
})

test("postComment() - any input is null", async () => {
    const UserService = jest.requireActual('./UserService.js');

    let fail1 = await UserService.sendCommentToDb("", "Josha", "test_googleId", "comment");
    expect(fail1).toBe(false);
    let fail2 = await UserService.sendCommentToDb("test_player", "", "test_googleId", "comment");
    expect(fail2).toBe(false);
    let fail3 = await UserService.sendCommentToDb("test_player", "Josha", "", "comment");
    expect(fail3).toBe(false);
    let fail4 = await UserService.sendCommentToDb("test_player", "Josha", "test_googleId", "");
    expect(fail4).toBe(false);
})

test("postComment() - comment exceeds character limit", async () => {
    const UserService = jest.requireActual('./UserService.js');
    let longComment = "aaaaaaaaflskkjfjgjlkjfslndlsnflkwejflkwjfelkjew \
    elkfjlkewjfljwelkfjlwkesdmnv,dnv,smdnvlkjglwjelkfjwelfjlewkjkflkwjfl \
    kjwelkjfljwelkfjwlkjflkwjflkjwlkfjlwjlkkwjlekjlwkejlkfjwlkeflkweejef \
    lkjwlkefjwlkekjflkwejlnvd,msdnv,snv,sndv,msddnv,sdnv,msnv,mdns,mnv, \
    msndv,smdnv"

    await UserService.addRegisteredUser("test_googleId", "test_riot", "test_token");
    let registered = await UserService.checkIfRegisteredUser("test_googleId");
    expect(registered).toBe(true);

    let success = await UserService.sendCommentToDb("test_player", "Josha", "test_googleId", longComment);
    expect(success).toBe(false);
})

test("getFromDB() - valid inputs, has been reviewed", async () => {
    const UserService = jest.requireActual('./UserService.js');

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

test("getFromDB() - valid inputs, has never been reviewed", async () => {
    const UserService = jest.requireActual('./UserService.js');

    let profile = await UserService.getFromDB("test_player");
    expect(profile._id).toBe("test_player");
    expect(profile.likes.length).toBe(0);
    expect(profile.dislikes.length).toBe(0);
    expect(profile.comments.length).toBe(0);
})

test("getFromDB() - null name", async () => {
    const UserService = jest.requireActual('./UserService.js');

    try {
        await UserService.getFromDB("");
    } catch(e) {
        expect(e).toMatch("Must include name parameter")
    }
})

// Gets called after each test to remove test entries from DB
afterEach(async () => {
    const UserService = jest.requireActual('./UserService.js');
    await UserService.cleanupTests();
    let exists = await UserService.checkIfRegisteredUser("test_googleId");
    expect(exists).toBe(false);
 });