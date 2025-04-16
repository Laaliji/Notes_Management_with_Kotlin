const express = require('express');
const router = express.Router();
const db = require('../db');

router.get('/', (req, res) => {
  db.query('SELECT * FROM notes', (err, rows) => {
    if (err) return res.status(500).json(err);
    res.json(rows);
  });
});

router.post('/', (req, res) => {
  const { studentId, courseId, value, coefficient, date, synced } = req.body;
  db.query('INSERT INTO notes (studentId, courseId, value, coefficient, date, synced) VALUES (?, ?, ?, ?, ?, ?)',
    [studentId, courseId, value, coefficient || 1, date, synced || false], (err, result) => {
      if (err) return res.status(500).json(err);
      res.json({ id: result.insertId });
    });
});

router.put('/:id', (req, res) => {
  const { studentId, courseId, value, coefficient, date, synced } = req.body;
  db.query('UPDATE notes SET studentId=?, courseId=?, value=?, coefficient=?, date=?, synced=? WHERE id=?',
    [studentId, courseId, value, coefficient || 1, date, synced || false, req.params.id], (err) => {
      if (err) return res.status(500).json(err);
      res.json({ message: 'Note mise à jour' });
    });
});

router.delete('/:id', (req, res) => {
  db.query('DELETE FROM notes WHERE id=?', [req.params.id], (err) => {
    if (err) return res.status(500).json(err);
    res.json({ message: 'Note supprimée' });
  });
});

module.exports = router;
