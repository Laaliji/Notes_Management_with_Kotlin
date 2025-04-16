const express = require('express');
const router = express.Router();
const db = require('../db');

router.get('/', (req, res) => {
  db.query('SELECT * FROM students', (err, rows) => {
    if (err) return res.status(500).json(err);
    res.json(rows);
  });
});

router.post('/', (req, res) => {
  const { firstName, lastName, email, phone, synced } = req.body;
  db.query('INSERT INTO students (firstName, lastName, email, phone, synced) VALUES (?, ?, ?, ?, ?)',
    [firstName, lastName, email, phone, synced || false], (err, result) => {
      if (err) return res.status(500).json(err);
      res.json({ id: result.insertId });
    });
});

router.put('/:id', (req, res) => {
  const { firstName, lastName, email, phone, synced } = req.body;
  db.query('UPDATE students SET firstName=?, lastName=?, email=?, phone=?, synced=? WHERE id=?',
    [firstName, lastName, email, phone, synced || false, req.params.id], (err) => {
      if (err) return res.status(500).json(err);
      res.json({ message: 'Étudiant mis à jour' });
    });
});

router.delete('/:id', (req, res) => {
  db.query('DELETE FROM students WHERE id=?', [req.params.id], (err) => {
    if (err) return res.status(500).json(err);
    res.json({ message: 'Étudiant supprimé' });
  });
});

module.exports = router;