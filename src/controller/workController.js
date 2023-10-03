const workProvider = require('../service/workProvider');
const workService = require('../service/workService');
const baseResponse = require("../../config/baseResponseStatus");
const {response, errResponse} = require("../../config/response");

/**
 * API No. 1
 * API Name : 전체 작업 조회 API
 * [GET] /works/:teamId
 */
exports.getWorks = async (req, res) => {

    try{
        const teamId = req.params.teamId;

        const workList = await workProvider.retrieveWorkList(teamId);
        return res.send(response(baseResponse.SUCCESS, workList));
    } catch(err){
        console.log(err);
        return res.send(errResponse(baseResponse.SERVER_ERROR));
    }
};

/**
 * API No. 2
 * API Name : 작업 등록 API
 * [POST] /works/:teamId
 */
exports.postWork = async (req, res) => {

    const teamId = req.params.teamId;

    const postWorkResult = await workService.createWork(teamId);

    return res.send(postWorkResult);

};

/**
 * API No. 3
 * API Name : 특정 작업 조회 API
 * [GET] /works-detail/:workId
 */
exports.getWorkById = async (req, res) => {
    try {
        const workId = req.params.workId;
        const workById = await workProvider.retrieveWork(workId);
        return res.send(response(baseResponse.SUCCESS, workById));
    } catch(err){
        console.log(err);
        return res.send(errResponse(baseResponse.SERVER_ERROR));
    }
};

/**
 * API No. 4
 * API Name : 특정 작업 삭제 API
 * [DELETE] /works/:workId
 */
exports.deleteWork = async function (req, res) {

    const workId = req.params.workId;

    const deleteWorkResult = await workService.deleteWorkById(workId);

    return res.send(deleteWorkResult);
};


/**
 * API No. 5
 * API Name : 특정 작업 수정 API
 * [PUT] /works/:teamId/:workId
 * body : work_name, worker,end_date, importance, status
 */

exports.putWork = async function (req, res) {


    const {workId, teamId} = req.params;
    console.log(req.params);
    const {work_name, worker, end_date, importance, status} = req.body;

    let worker_arr = JSON.parse(worker);
    const worker_number = worker_arr.length;

    const updateWorkParams = [
        work_name, end_date, importance, status, worker_number
    ];
    const editWorkResult = await workService.editWork(workId, updateWorkParams, worker_arr);
    res.send(editWorkResult);

};


/**
 * API No. 6
 * API Name : 작업명, 담당자, 마감일, 중요도, 상태 중 하나 수정 API
 * [PUT] /works/:workId/:updateValue
 * body : work_namre or end_date or importance or status or worker
 * 수정하려는 값: updateValue, 수정 내용 : val
 */

exports.patchWork = async(req, res) => {
    const {workId, updateValue} = req.params
    
    const val = req.body[updateValue];  
    console.log(`update "${updateValue}" to ${val}`);
    if (val == undefined) return res.send(errResponse(baseResponse.NOT_MATCH));
    if (updateValue == "worker"){
        const worker_number = val.length;
        const patchWorkerResult = await workService.patchWorker(workId, worker_number, val);
        return res.send(patchWorkerResult);
        
    } else{
        const patchWorkResult = await workService.patchWork(workId, updateValue, val);
        return res.send(patchWorkResult);
    }
}

/**
 * API No. 7
 * API Name : 팀 작업 현황 조회
 * [GET] /work-progress/:teamId
 */
exports.getWorkProgress = async (req, res) => {

    try{
        const teamId = req.params.teamId;
     
        const progress = await workProvider.retrieveWorkProgress(teamId);
        const result = progress;
        return res.status(200).send(result);
    } catch(err){
        console.log(err);
        return res.send(errResponse(baseResponse.SERVER_ERROR));
    }
};