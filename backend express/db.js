const mysql = require('mysql2');

const db = mysql.createConnection({
    host: 'localhost',
    user: 'root',
    password: '', // par défaut avec XAMPP
    database: 'gestion_etudiants'
});

db.connect((err) => {
    if (err) {
        console.error('Erreur de connexion :', err);
        return;
    }
    console.log('Connexion MySQL réussie ');
});

module.exports = db;
