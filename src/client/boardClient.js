const client = require('../../eureka-helper');
const axios = require('axios');
const serverName = 'board-service';

// const boardClient = {
    
//     getTeamMembers : async (teamId) => {
//         const boardInstance = client.getInstancesByAppId(serverName)[0];

//         if(dashboardInstance){
//             const url = `http://${boarddInstance.hostName}:${boardInstance.port.$}`;

//             try{
//                 const response = await axios.get(`${url}/${teamId}/team-members`);
//                 return response.data.teamMembers;
//             }catch(err){
//                 console.error(err);
//             }
//         }
//     }
// }

module.exports = boardClient;