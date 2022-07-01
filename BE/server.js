var express = require('express')
var app = express()

app.use(express.json())

// IMAGE RECOGNITION 
const usernameRouter = require('./ImageRecognition/UsernameProcessor')
app.use('/image', usernameRouter)

app.listen(8080, () => console.log('server running...'))

