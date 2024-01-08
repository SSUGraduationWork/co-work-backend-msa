const Chat = require("../../models/Chat");

const chatController = {
    getChats: async(req, res) => {
        try{
            const {teamId} = req.params;
            const chats = await Chat.find({teamId: teamId});
            return res.status(200).send({chats});

        }catch(err){
            console.log(err);
            return res.status(500).end();
        }
    },
    
}

module.exports = chatController;