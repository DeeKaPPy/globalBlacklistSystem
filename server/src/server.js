const express = require("express")
const app = express()
const connect = require("../configs/connect.json")
const config = require("../configs/config.json")
const MongoClient = require('mongodb').MongoClient;
const parser = require('body-parser')

app.use(parser.json())


let dbo = null;

MongoClient.connect(connect.mongodb.uri).then(client =>{
    const db = client.db(connect.mongodb.database)
    dbo = db
});

// has access
const hasAccess = (req, res, next) => {
    const ip = req.headers['cf-connecting-ip'] || req.headers['x-forwarded-for'] || req.connection.remoteAddress;
    if(!config.whitelisted.includes(ip)) return res.send({status: 500,message:"Contact server administrator"});
    return next();
}

// has currect secret
const checkSecret = (req, res, next) => {
    const secret = req.body.secret;
    if(config.secret === secret) return next();
    return res.send({status: 500,message:"Contact server administrator"})
}

// Get secret
app.get("/api/blacklists/secret", hasAccess, (req,res) => {
    const ip = req.headers['cf-connecting-ip'] || req.headers['x-forwarded-for'] || req.connection.remoteAddress;
    if(!config.secretWhitelist.includes(ip)) return res.send({status: 500,message:"Contact server administrator"});
    return res.send({secret: config.secret})
})

// Get all blacklists
app.get("/api/blacklists", hasAccess, async(req,res) => {
    const data = await dbo.collection("blacklists").find().toArray();
    return res.send(data);
});

// Get blacklist details
app.get('/api/blacklists/:name', hasAccess, async(req,res) => {
    const data = await dbo.collection("blacklists").find({username: req.params.name}).toArray();
    return res.send(data[0]);
});

// Get blacklist details
app.get('/api/blacklists/server/:server', hasAccess, async(req,res) => {
    const data = await dbo.collection("blacklists").find({server: req.params.server}).toArray();
    return res.send(data);
});


// Add blacklist
app.post('/api/blacklists/add', hasAccess, checkSecret, async(req,res) => {
    dbo.collection("blacklists").insertOne({
        username: req.body.username,
        ipAddress: req.body.ipAddress,
        uuid: req.body.uuid,
        admin: req.body.admin,
        server: req.body.server
    },(err,response) => {
        if(err) return res.send({status: 500,message:"Contact server administrator"});
        res.send({status: 200,message:"Blacklist added"});
    });
});

// Remove blacklist
app.post('/api/blacklists/remove', hasAccess, checkSecret, async(req,res) => {
    dbo.collection("blacklists").deleteOne({username: req.body.username}, (err,response) => {
        if(err) return res.send({status: 500,message:"Contact server administrator"});
        res.send({status: 200,message:"Blacklist removed"});
    });
});


// Listen on port from config
app.listen(connect.port);