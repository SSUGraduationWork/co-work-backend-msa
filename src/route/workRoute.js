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
    app.patch('/works/:workId/:updateValue', work.patchWork);

    //7. 팀 작업 현황 조회 API
    app.get('/work-progress/:teamId', work.getWorkProgress);
};