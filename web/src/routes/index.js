const express = require('express');
const router = express.Router();
const bcrypt = require("bcrypt");

const db = require("../db/connection");

router.get("/", async (req, res) => {
    const utilizador = req.session.utilizador;

    if (!utilizador) {
        return res.redirect("/login");
    }

    const [servicos] = await db.query("SELECT * FROM servicos");

    res.render("home", { 
        utilizador,
        servicos
    });
});

router.post("/book", async (req, res) => {
    const { id_servico, data, hora } = req.body;
    const utilizadorID = req.session.utilizador.id;

    try {
        await db.query("INSERT INTO bookings (id_utilizador, id_servico, data, hora) VALUES (?, ?, ?, ?)",
            [utilizadorID, id_servico, data, hora]
        );

        res.redirect("/");
    } catch (err) {
        console.log(err);
        res.send("Erro ao criar reserva");
    }
});

// LOGIN
router.get("/login", (req, res) => {
    res.render("login");
});
router.post("/login", async (req, res) => {
    const { email, palavra_passe } = req.body;

    const [rows] = await db.query("SELECT * FROM utilizadores WHERE email = ?", [email]);

    if (rows.length === 0) {
        return res.send("Credenciais inválidas");
    }

    const utilizador = rows[0];
    console.log(palavra_passe);
    console.log(utilizador.palavra_passe);

    const match = await bcrypt.compare(palavra_passe, utilizador.palavra_passe);

    if (match) {
        req.session.utilizador = {
            id: utilizador.id,
            email: utilizador.email
        }

        res.redirect("/");
    } else {
        res.send("Credenciais inválidas");
    }

    res.send("Login recebido");
});

// REGISTRO
router.get("/register", async (req, res) => {
    res.render("register");
})
router.post("/register", async (req, res) => {
    const { email, nome_utilizador, palavra_passe } = req.body;

    try {
        const hashedPalavra_Passe = await bcrypt.hash(palavra_passe, 10);

        await db.query("INSERT INTO utilizadores (email, nome_utilizador, palavra_passe) VALUES (?, ?, ?)", 
            [email, nome_utilizador, hashedPalavra_Passe]
        );

        res.send("Utilizador criado");
    } catch (err) {
        res.send("Erro ao criar utilizador");
    }
});

// LOGOUT
router.get("/logout", async (req, res) => {
    req.session.destroy(() => {
        res.redirect("/login");
    });
});

// TESTE DB
router.get("/test-db", async (req, res) => {
    try {
        const [rows] = await db.query("SELECT NOW() AS time");
        res.json(rows[0]);
    } catch (err) {
        res.status(500).json({ error: err.message });
    }
});

module.exports = router;