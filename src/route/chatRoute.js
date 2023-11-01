module.exports = (app) => {
    const chat = require('../controller/chatController.js');

    //1. 채팅 조회
    app.get('/chats/:teamId', chat.getChats);
}