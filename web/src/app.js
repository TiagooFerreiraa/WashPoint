const express = require('express');
const path = require('path');
const session = require('express-session');
require("dotenv");

const app = express();

// ENCRIPTAÇÃO
app.use(express.json());
app.use(express.urlencoded({ extended: true }));

// UTILIZAR PASTA 'VIEWS'
app.set("view engine", "ejs");
app.set("views", path.join(__dirname, "views"));

// SESSÃO
app.use(session({
    secret: "washpoint-secret-key",
    resave: false,
    saveUninitialized: false
}));

const routes = require('./routes');
app.use("/", routes);

const PORT = process.env.PORT || 3000;

app.listen(PORT, () => {
    console.log(`Server running on port ${PORT}`);
});