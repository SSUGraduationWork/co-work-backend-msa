const express = require('express');
const dotenv = require('dotenv');
const compression = require('compression');
const methodOverride = require('method-override');
const cors = require('cors');

dotenv.config();

const app = express();

app.use(compression());

app.use(express.json());
app.use(express.urlencoded({extended:true}));

app.use(methodOverride());
app.use(cors());

require('../src/route/chatRoute')(app);

module.exports = app;