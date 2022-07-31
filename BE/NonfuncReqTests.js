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
        console.log("i: " + i);
        let stats = await TeamStats(allIds[i], region);
        console.log(stats);
        let endTime = performance.now();
        console.log(endTime - startTime);
        executionDurations.push(endTime - startTime);
    }

    console.log("All execution durations: "+executionDurations);
    // Get average
    let sum = 0;
    for (let j = 0; j < executionDurations.length; j++) {
        sum += executionDurations[j];
    }
    console.log("Average execution duration: " + sum / executionDurations.length);
}

testBackendPerformance();