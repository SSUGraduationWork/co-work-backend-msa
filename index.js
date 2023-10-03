const app = require('./config/express');
const port = 3000;
const client = require('./eureka-helper');

app.listen(port, () => {
    console.log(`${port}에서 서버 가동`);
});

client.start( error => {
    console.log(error || "work service registered");
});
