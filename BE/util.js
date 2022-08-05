const express = require('express')
const router = express.Router()
const { google } = require('googleapis');
const SCOPES = ['https://www.googleapis.com/auth/firebase.messaging'];


router.get('/', async(req, res) => {
    getAccessToken().then(token => {
        res.json(token)
        res.status(200)
    })
})

async function getAccessToken() {
    return new Promise ((resolve, reject) => {
      const key = require('./firebase-push-service-account.json');
      const jwtClient = new google.auth.JWT(
        key.client_email,
        null,
        key.private_key,
        SCOPES,
        null
      );
      jwtClient.authorize((err, tokens) => {
        if (err) {
          reject(err);
          return;
        }
        resolve(tokens.access_token);
      });
    });
  }

module.exports = router