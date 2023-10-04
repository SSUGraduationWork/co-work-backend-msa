const client = require('../../eureka-helper');
const axios = require('axios');
const serverName = 'dashboard-service';

const teamMemberClient = {
    
    getTeamMembers : async (teamId) => {
        const dashboardInstance = client.getInstancesByAppId(serverName)[0];

        if(dashboardInstance){
            const url = `http://${dashboardInstance.hostName}:${dashboardInstance.port.$}`;

            try{
                const response = await axios.get(`${url}/${teamId}/team-members`);
                return response.data.teamMembers;
            }catch(err){
                console.error(err);
            }
        }
    }
}

module.exports = teamMemberClient;