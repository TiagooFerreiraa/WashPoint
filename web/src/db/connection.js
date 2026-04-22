const mysql = require('mysql2');
require("dotenv").config();

const pool = mysql.createPool({
    host: process.env.DB_HOST,
    user: process.env.DB_UTILIZADOR,
    password: process.env.DB_PASSWORD,
    database: process.env.DB_NOME
});

module.exports = pool.promise();