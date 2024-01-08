const http = require("http");

const app = require('./config/express');
const db = require('./config/database');
const port = 3001;
const chat = require('./src/controller/chatController');

const handleListen = () => console.log(`Listening on http://localhost:${port}`);

//같은 서버에서 http, websocket 둘 다 작동시키는 방법
const server = http.createServer(app);  //http server

//http 서버 위에 webSocket 서버를 만듦. 동일한 포트에서 http, ws request 두 개를 다 처리할 수 있다.
//const wss = new WebSocket.Server({server});   //websocket server. server를 전달해주지 않아도 되지만 이렇게하면 http서버와 webSocket 서버를 둘 다 사용할 수 있다.


const io = require("socket.io")(server, {
    cors: {
      origin: "http://localhost:8080",
      methods: ["GET", "POST"],
      allowedHeaders: ["extra-custrom-headeres"],
      credentials: true
    },
    allowEIO3: true 
});


io.on("connection", (socket) => {

    socket.on("chat", async (teamId, message) => {
        console.log(socket["userId"]);
        io.to(teamId).emit("chat_message", socket["userId"], message);
        await chat.saveChat(teamId, socket["userId"], message);
    })
    socket.on("enter_room", (teamId, userId, done) => {
        socket.join(teamId);
        socket["userId"] = userId;
        done();
    })
})


server.listen(port, handleListen);