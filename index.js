const app = require('./config/express');
const port = 3000;
const client = require('./eureka-helper');
//const SpringCloudConfig = require('spring-cloud-config');

app.listen(port, () => {
    console.log(`${port}에서 서버 가동`);
});

client.start( error => {
    console.log(error || "work service registered");
});

// SpringCloudConfig.load({
//     endpoint: 'http://localhost:8888',
//     name: 'application',
//     label: 'main',
// })
// .then(config => {
//     console.log(config);
//     console.log(`hello : ${spring.datasource.url}`);
// })
// .catch(error => {
//     console.error('Error fetching config:', error);
// })

