const { performance } = require('perf_hooks');
const TeamStats = require('./Prediction/TeamStats.js')

async function testBackendPerformance() {   
    let idSet1 = ["2 4", "jinxxxtentacion", "Xerzx", "clwm gktehrm", "Momoiskii"];
    let idSet2 = ["Liir", "2 4", "ogcurlyfry", "JJT248", "Cheri XD"];
    let idSet3 = ["2 4", "Ja LaCx", "Dr sk1nnypen1s", "BullZeye101", "Ridah408"];
    let idSet4 = ["JannaSlave", "An1meGameBwo1", "questionmark1", "weedguud", "honeybutttt"];
    
    let allIds = [idSet1, idSet2, idSet3, idSet4];
    
    let executionDurations = [];
    
    for (let i = 0; i < allIds.length; i++) {
        let startTime = performance.now();
        let region = "NA1";
        await TeamStats(allIds[i], region);
        let endTime = performance.now();
        executionDurations.push(endTime - startTime);
    }
    console.log(executionDurations);
}

async function imageTest() {
    const id1 = 'IronAetos'
    const id2 = '24'
    const id3 = 'WatchMeGank'
    const id4 = 'Aarontandude'
    const id5 = 'zAddyy'
    const id6 = 'TheWanderersWay'
    const id7 = 'palukawhale'
    const id8 = 'Thick Rooster'
    const id9 = 'ct819'

    const test1_ids = await UsernameProcessor.parseText('test_photos/p1.jpg')
    const test2_ids = await UsernameProcessor.parseText('test_photos/p2.jpg')
    const test3_ids = await UsernameProcessor.parseText('test_photos/p3.jpg')
    const test4_ids = await UsernameProcessor.parseText('test_photos/p4.jpg')
    const test_ids_list_v1 = [test1_ids, test2_ids, test4_ids]

    test_ids_list_v1.forEach((ids) => {
        const contains1 = ids.includes(id1)
        const contains2 = ids.includes(id2)
        const contains3 = ids.includes(id3)
        const contains4 = ids.includes(id4)
        const contains5 = ids.includes(id5)
        if (!contains1 || !contains2 || !contains3 || !contains4 || !contains5) {
            console.log("Failed: " + ids + " is missing an ID")
        } else {
            console.log("Correct")
        }
    })

    const contains6 = test3_ids.includes(id2)
    const contains7 = test3_ids.includes(id6)
    const contains8 = test3_ids.includes(id7)
    const contains9 = test3_ids.includes(id8)
    const contains10 = test3_ids.includes(id9)
    if (!contains6 || !contains7 || !contains8 || !contains9 || !contains10) {
        console.log("Failed: test ids 3 incorrect")
    } else {
        console.log("Correct")
    }
}

testBackendPerformance();
imageTest();