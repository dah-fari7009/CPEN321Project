var express = require('express')
var app = express()

app.use(express.json())

// IMAGE RECOGNITION 
const usernameRouter = require('./ImageRecognition/UsernameProcessor')
app.use('/image', usernameRouter)

const userServiceModule = require('./PlayerData/UserService')
const userServiceRouter = userServiceModule.router
app.use('/playerdb', userServiceRouter)

app.listen(8080, () => console.log('server running...'))

