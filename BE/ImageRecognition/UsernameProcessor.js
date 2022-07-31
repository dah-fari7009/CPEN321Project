const express = require('express')
const router = express.Router()
const controller = require('./UsernameProcessorController')

// Upload riot ids
router.post('/usernames', controller.uploadRiotIds)

// Upload image to parse riot ids
router.post('/', controller.uploadRiotIdsByImage)

module.exports = router
