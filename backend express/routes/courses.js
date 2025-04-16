const express = require('express');
const router = express.Router();
const db = require('../db');

router.get('/', (req, res) => {
  db.query('SELECT * FROM courses', (err, rows) => {
    if (err) return res.status(500).json(err);
    res.json(rows);
  });
});

router.post('/', (req, res) => {
  const { name, description, synced } = req.body;
  db.query('INSERT INTO courses (name, description, synced) VALUES (?, ?, ?)',
    [name, description, synced || false], (err, result) => {
      if (err) return res.status(500).json(err);
      res.json({ id: result.insertId });
    });
});

router.put('/:id', (req, res) => {
  const { name, description, synced } = req.body;
  db.query('UPDATE courses SET name=?, description=?, synced=? WHERE id=?',
    [name, description, synced || false, req.params.id], (err) => {
      if (err) return res.status(500).json(err);
      res.json({ message: 'Cours mis à jour' });
    });
});

router.delete('/:id', (req, res) => {
  db.query('DELETE FROM courses WHERE id=?', [req.params.id], (err) => {
    if (err) return res.status(500).json(err);
    res.json({ message: 'Cours supprimé' });
  });
});

module.exports = router;
