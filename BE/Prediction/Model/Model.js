const tf = require('@tensorflow/tfjs-node');
const { parse } = require('csv-parse');
const fs = require('fs');

const SHUFFLE_SEED = 42;
const EPOCHS = 80;
const LEARNING_RATE = 0.001;

function oneHot(win) {
    return Array.from(tf.oneHot(win, 2).dataSync());
}

function cleanData(data, features, testSize, batchSize) {
    const X = data.map(r =>
        features.map(f => {
          return r[f];
        })
    )
    
    const y = data.map(r => {
        const win = r.win === '' ? 0 : r.win;
        return oneHot(win);
    })

    const dataset = tf.data
        .zip({ xs: tf.data.array(X), ys: tf.data.array(y) })
        .shuffle(data.length, SHUFFLE_SEED);

    const split = parseInt((1 - testSize) * data.length, 10)

    return [
        dataset.take(split).batch(batchSize),
        dataset.skip(split + 1).batch(batchSize),
        tf.tensor(X.slice(split)),
        tf.tensor(y.slice(split)),
    ]
}

async function train(featureCount, trainData, validationData) {
    const model = tf.sequential();
    model.add(
        tf.layers.dense({
            units: 2,
            activation: "softmax",
            inputShape: featureCount
        })
    );

    model.compile({
        optimizer: tf.train.adam(LEARNING_RATE),
        loss: "binaryCrossentropy",
        metrics: ["accuracy"] 
    });

    await model.fitDataset(trainData, {
        epochs: EPOCHS,
        validationData: validationData,
        callbacks: {
            onEpochEnd: async (epochs, logs) => {
                console.log(logs);
            }
        }
    })

    return model;
}

async function run() {
    let data;

    fs.readFile("./gamedata.csv", async function (err, fileData) {
      parse(fileData, {columns: true, trim: true, cast: true}, async function(err, rows) {
        data = rows;

        const features = ['kps', 'aps', 'dps', 'gps', 'vps'];
        const [trainSet, validationSet, xTest, yTest] = cleanData(data, features, 0.2, 16);

        const model = await train(features.length, trainSet, validationSet);
        await model.save('file:////Users/joshaheimann/pws/CPEN321Project/BE/Prediction/Model');

        const predictedLabels = model.predict(xTest).argMax(-1);
        const trueLabels = yTest.argMax(-1);
        const confMatrix = tf.math.confusionMatrix(trueLabels, predictedLabels, 2);
        confMatrix.print();   
    })
    })

}

run()