# TiltCheck
Use the Riot Games API to see if a player is experiencing tilt

## About
Personal project using the [Riot Games API](https://developer.riotgames.com/) just for fun and learning. 

## Deploying

### Requirements
- Maven 3.0
- Java 8
- Riot Games API Key. If you don't have one, you can get one [here](https://developer.riotgames.com/)
- If you want to deploy to Heroku, you will need the [Heroku Command Line Tool](https://devcenter.heroku.com/articles/heroku-command-line)

### Build and run in Heroku
- Clone this repo
- Set up a new Heroku app with `heroku create`
- Set up your API key as an encironment variable: `heroku config:set SECRET="?api_key=YOUR_API_KEY_HERE"`
- Deploy with `mvn heroku:deploy`

### Build and run Locally
- Clone this repo
- Provide your API key as an argument when running the app in the form of ?api_key=YOUR_API_KEY_HERE
