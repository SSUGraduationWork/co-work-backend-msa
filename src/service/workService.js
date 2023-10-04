const {pool} = require("../../config/database");
const workProvider = require("./workProvider");
const workDao = require("../dao/workDao");
const baseResponse = require("../../config/baseResponseStatus");
const {response} = require("../../config/response");
const {errResponse} = require("../../config/response");

// Service: Create, Update, Delete 비즈니스 로직 처리

const workService = {
    createWork : async (teamId) => {
        try{
            const connection = await pool.getConnection(async(conn) => conn);

            const createdWork = await workDao.insertWork(connection, teamId);
            const workId = createdWork[0].insertId;
            console.log(`추가된 작업: ${workId}`);

            connection.release();

            return response(baseResponse.SUCCESS, {'work_id' : workId});

        } catch(err) {
            console.log(err);
            return errResponse(baseResponse.DB_ERROR);
        }
    },

    deleteWorkById : async (workId) => {
        try{
            const connection = await pool.getConnection(async(conn) => conn);

            const deletedWork = await workDao.deleteWork(connection, workId);
            console.log(`삭제된 작업 수 : ${deletedWork[0].affectedRows}, 삭제 시도한 작업 : ${workId}`);
            connection.release();
            return response(baseResponse.SUCCESS);
        } catch(err) {
            console.log(err);
            return errResponse(baseResponse.DB_ERROR);
        }
    },

    editWork : async (workId, updateWorkParams, worker_arr) => {
        try{
            const connection = await pool.getConnection(async(conn) => (conn));
            const updatedWork = await workDao.updateWork(connection, workId, updateWorkParams, worker_arr);
            console.log(`수정된 작업 : ${updatedWork[0].info}`);

            const deletedWorkers = await workDao.deleteWorkers(connection, workId);
            console.log(`삭제된 작업 담당자 수 : ${deletedWorkers[0].affectedRows}, 작업 아이디 : ${workId}`);

            const updatedWorkers = await workDao.insertWorkers(connection, workId, worker_arr);
            connection.release();
            if (updatedWorkers) return response(baseResponse.SUCCESS, {'work_id' : workId});
            else return errResponse(baseResponse.DB_ERROR);

        } catch(err) {
            console.log(err);
            return errResponse(baseResponse.DB_ERROR);
        }
    },

    patchWorker : async (workId, teamId, worker_number, worker_arr) => {
        try{
            const connection = await pool.getConnection(async(conn) => (conn));
            
            //유저 삭제 후 새로 등록, worker_number update
            const deletedWorkers = await workDao. deleteWorkers(connection, workId);
            const patchWorkers = await workDao.insertWorkers(connection, workId, teamId, worker_arr);
            const patchWorkerNumber = await workDao.patchWorkerNumber(connection, workId, worker_number);
            
            connection.release();
            return response(baseResponse.SUCCESS);
            
        } catch(err) {
            console.log(err);
            return errResponse(baseResponse.DB_ERROR);
        }
    },
    patchWork : async (workId, updateValue, val) => {
        try{
            const connection = await pool.getConnection(async(conn) => (conn));
            const patchWork = await workDao.patchWork(connection, workId, updateValue, val);

            connection.release();
            return response(baseResponse.SUCCESS);

        } catch(err){
            console.log(err);
            return errResponse(baseResponse.DB_ERROR);
        }
    }
}

module.exports = workService;