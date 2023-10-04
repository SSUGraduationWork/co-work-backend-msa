const {pool} = require("../../config/database");
const workDao = require("../dao/workDao");
const teamMemberClient = require("../client/teamMemberClient");

// Provider: Read 비즈니스 로직 처리

const workProvider = {
    retrieveWorkList : async (teamId) => {
        const connection = await pool.getConnection(async (conn) => conn);
        const workResult = await workDao.selectWorks(connection, teamId);
        connection.release();

        const teamMembers = await teamMemberClient.getTeamMembers(teamId);

        const result = {teamMembers : teamMembers, works : workResult,};
        return result;
    },

    retrieveWork : async (workId) => {
        const connection = await pool.getConnection(async (conn) => conn);
        const workResult = await workDao.selectWorkById(connection, workId);
        const boardsResult = await workDao.selectBoardsAboutWork(connection, workId);
        const result = {...workResult[0], ...boardsResult}
        connection.release();
        return result;
    },

    retrieveWorkProgress : async (teamId) => {
        const connection = await pool.getConnection(async(conn) => conn);
        const [workProgress] = await workDao.selectWorkProgress(connection, teamId);
        connection.release();
        return workProgress;
    },

    retrieveWorksByUserId : async(teamId, userId) => {
        const connection = await pool.getConnection(async(conn) => conn);
        const worksList = await workDao.selectWorksByUser(connection, teamId, userId);
        connection.release();
        return worksList;
    },

    getWriteStatus : async(userId, workId) => {
        const connection = await pool.getConnection(async(conn) => conn);
        const [writeYn] = await workDao.selectWriteStatus(connection, userId, workId);
        connection.release();
        return writeYn;
    },

    getWork : async(workId) => {
        const connection = await pool.getConnection(async(conn) => conn);
        const [work] = await workDao.selectWork(connection, workId);
        connection.release();
        return work;
    },

    getTeamWorks : async(teamId) => {
        const connection = await pool.getConnection(async(conn) => conn);
        const result = await workDao.selectTeamWorks(connection, teamId);
        connection.release();
        return result;
    }
}

module.exports = workProvider;