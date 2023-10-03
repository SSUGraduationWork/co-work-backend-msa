const Eureka = require('eureka-js-client').Eureka;
const eurekaHost = 'localhost' ;
const eurekaPort = 7777;
const hostName = 'localhost';
const ipAddr = '127.0.0.1';
const appName = 'work-service';
const PORT = 3000;


const client = new Eureka({
  // application instance information
  instance: {
    app: appName,
    hostName: hostName,
    ipAddr: ipAddr,
    port: {
      '$' : PORT,
      '@enabled' : 'true',
    },
    vipAddress: appName,
    dataCenterInfo: {
      '@class': 'com.netflix.appinfo.InstanceInfo$DefaultDataCenterInfo',
      name: 'MyOwn',
    },
  },
  eureka: {
    // eureka server host / port
    host: eurekaHost,
    port: eurekaPort,
    servicePath: '/eureka/apps',
    maxRetries: 10,
    requestRetryDelay: 2000,
  },
});

function exitHandler(options, exitCode) {
  if (options.cleanup) {
  }
  if (exitCode || exitCode === 0) console.log(exitCode);
  if (options.exit) {
      client.stop();
  }
}

client.on('deregistered', () => {
  console.log('after deregistered');
  process.exit();
})

client.on('started', () => {
  console.log("eureka host  " + eurekaHost);
})

process.on('SIGINT', exitHandler.bind(null, {exit:true}));

module.exports = client;