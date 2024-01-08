const client = require('../../eureka-helper');
const axios = require('axios');
const serverName = 'board-service';

const boardClient = {
    
    getPostsByWorkId : async (workId) => {
        const boardInstance = client.getInstancesByAppId(serverName)[0];

        if(boardInstance){
            const url = `http://${boardInstance.hostName}:${boardInstance.port.$}`;

            try{
                const response = await axios.get(`${url}/board/posts/${workId}`);
                return response.data
            }catch(err){
                console.error(err);
            }
        }
    }
}

module.exports = boardClient;