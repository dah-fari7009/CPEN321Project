
const PlayerProfile = require("./PlayerProfile.js")
const DataHandler = require("./DataHandler.js")

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

// NOTE: for tests with non existing values, there may be Axios errors that get logged in console
// this is expected behaviour, note the tests still pass.
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
test("getProfile()", async () => {
    DataHandler.getMatchHistory.mockResolvedValueOnce(false)
    // jest.spyOn(DataHandler, 'getMatchHistory').mockImplementation(() => false)
    resp = await PlayerProfile.getProfile("2 4", "NA1");

    expect(resp.stats).toBe(false);
    // expect(1).toBe(1)
})