module.exports = (app) => {
    const work = require('../controller/workController');

    // 1. 전체 작업 조회 API
    app.get('/works/:teamId', work.getWorks);

    // 2. 작업 등록 API
    app.post('/works/:teamId',work.postWork);

    // 3. 특정 작업 조회 API
    app.get('/works-detail/:workId', work.getWorkById);

    //4. 특정 작업 삭제 API
    app.delete('/works/:workId', work.deleteWork);

    //5. 특정 작업 전체 수정 API
    app.put('/works/:workId', work.putWork);

    //6. 특정 작업 하나 수정 API
    app.patch('/works/:teamId/:workId/:updateValue', work.patchWork);

    //7. 팀 작업 현황 조회 API
    app.get('/work-progress/:teamId', work.getWorkProgress);

    //board-service에 필요
    //8. 팀에서 특정 유저가 맡은 모든 작업 반환 API
    app.get('/works/:teamId/:userId', work.getWorksByUser);

    //9. 특정 작업에서 특정 담당자의 게시글 작성 유무(writeYN) 반환 API
    app.get('/works/write-status/:userId/:workId', work.getWriteStatus);

    //10. workId로 work 반환 API
    app.get('/work/:workId', work.findWorkById);

    //11. 작업 담당자의 게시글 작성 상태 true로 변경
    app.post('/works/write-status/:userId/:workId', work.setWriteStatusTrue);

    //12. 작업 상태 변경
    app.post('/works/status/:workId/:status', work.setWorkStatus);

    //13. 팀의 모든 작업 반환
    app.get('/works-list/:teamId', work.findWorksByTeamId);

    //14. guyujung workId로 worker 반환 API
    app.get('/worker/:workId', work.findWorkerById);
};