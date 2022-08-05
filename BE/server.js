var express = require('express')
var app = express()

const bodyParser = require('body-parser');

app.use(bodyParser.json({ limit: "50mb" }))

// IMAGE RECOGNITION 
const usernameRouter = require('./ImageRecognition/UsernameProcessor')
app.use('/image', usernameRouter)

const utilRouter = require('./util')
app.use('/token', utilRouter)

const userServiceModule = require('./PlayerData/UserService')
const userServiceRouter = userServiceModule.router
app.use('/playerdb', userServiceRouter)

app.listen(8080, () => console.log('server running...'))

