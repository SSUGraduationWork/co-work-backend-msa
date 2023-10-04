const workDao = {

    selectWorks: async (connection, teamId) => {
        const selectWorksQuery = `
        SELECT work_id, work_name, GROUP_CONCAT(user_id SEPARATOR ';') AS worker, end_date, importance, status
        FROM (SELECT Works.work_id, work_name, end_date, importance, status, user_id
              FROM Works
                    LEFT JOIN Workers
                        ON Workers.work_id = Works.work_id
              WHERE Works.team_id = ?) AS A
        GROUP BY work_id;
        `;
        const [workRows] = await connection.query(selectWorksQuery, teamId);
        return workRows;
    },

    selectWorkById: async (connection, workId) => {
        const selectWorkByIdQuery =`
            SELECT Works.work_id, work_name, GROUP_CONCAT(user_id SEPARATOR ';') AS worker, end_date, importance, status
            FROM Works
                LEFT JOIN Workers
                    ON Works.work_id = Workers.work_id
            WHERE Works.work_id = ?;
        `;

        const [workRows] = await connection.query(selectWorkByIdQuery, workId);
        return workRows;
    },

    selectBoardsAboutWork: async (connection, workId) => {
        const selectBoardsAboutWorkQuery = `
            SELECT board_id, user_id, title
            FROM Boards
                     LEFT JOIN Members M
                               ON Boards.user_id = M.id
            WHERE work_id = ?;
        `;

        const [boardRows] = await connection.query(
            selectBoardsAboutWorkQuery,
            workId
        );
        return {boards : boardRows};
    },

    insertWork: async (connection, teamId) => {

        const insertWorkQuery = `
            INSERT INTO Works(team_id)
            VALUES (${teamId});
        `;

        const insertWorkInfoRow = await connection.query(
            insertWorkQuery,
        );

        return insertWorkInfoRow;
    },

    insertWorkers : async(connection, workId, teamId, worker_arr) => {
        const insertWorkersQuery = `
            INSERT INTO Workers(work_id, user_id, team_id)
            VALUES (?,?,?);
        `;

        try{
            for (const userId of worker_arr) {
                await connection.query(insertWorkersQuery, [workId, userId, teamId]);
            }
            return true
        } catch(err) {
            console.log(err)
            return false;
        }
    },

    deleteWork: async (connection, workId) => {
        const deleteWorkQuery = `
            DELETE FROM Works WHERE work_id = ?;
        `

        const deleteWorkInfoRow = await connection.query(
            deleteWorkQuery,
            workId
        );

        return deleteWorkInfoRow;
    },

    updateWork: async (connection, workId, updateWorkParams) => {

        const updateWorkQuery = `
            UPDATE Works SET work_name = ?, end_date = ?, importance = ?, status = ?, worker_number = ?
            WHERE work_id = ${workId};   
        `;

        const updateWorkInfoRow = await connection.query(
            updateWorkQuery,
            updateWorkParams
        );

        return updateWorkInfoRow;
    },

    deleteWorkers: async (connection, workId) => {
        const deleteWorkersQuery = `
            DELETE FROM Workers WHERE work_id = ?
        `

        const deleteWorkInfoRow = await connection.query(
            deleteWorkersQuery,
            workId
        );

        return deleteWorkInfoRow;
    },

    patchWorkerNumber : async (connection, workId, worker_number) => {
        const patchWorkerQuery = `
            UPDATE Works SET worker_number = ${worker_number}
            WHERE work_id = ${workId};  
        `
        const updateWorkerNumber = await connection.query(patchWorkerQuery);

        return updateWorkerNumber;
    },
    patchWork : async (connection, workId, updateValue, val) => {
        const patchWorkQuery = `
            UPDATE Works 
            SET ${updateValue} = "${val}"
            WHERE work_id = ${workId};
        `
        const updateWork = await connection.query(patchWorkQuery);

        return updateWork;
    },

    selectWorkProgress : async(connection, teamId) => {
        const selectWorkProgressQuery = `
            SELECT count(*) AS totalWorks, 
                   count(CASE WHEN status = 1 THEN 1 END) AS notStarted, 
                   count(CASE WHEN status = 3 or status = 2 THEN 1 END) AS inProgress, 
                   count(CASE WHEN status = 4 THEN 1 END) AS done 
            FROM Works 
            WHERE team_id = ?;
        `
        const [workProgress] = await connection.query(selectWorkProgressQuery, teamId);
        return workProgress;
    }
}
module.exports = workDao;