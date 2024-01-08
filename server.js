const http = require("http");
const client = require('./eureka-helper');
const app = require('./config/express');
const db = require('./config/database');
const port = 5006;
const chat = require('./src/controller/chatController');

const handleListen = () => console.log(`Listening on port: ${port}`);

client.start( error => {
    console.log(error || "chat service registered");
});

const server = http.createServer(app);  //http server


server.listen(port, handleListen);