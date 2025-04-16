const express = require('express');
const router = express.Router();
const db = require('../db');

router.get('/', (req, res) => {
  db.query('SELECT * FROM absences', (err, rows) => {
    if (err) return res.status(500).json(err);
    res.json(rows);
  });
});

router.post('/', (req, res) => {
  const { studentId, courseId, date, justified, justification, synced } = req.body;
  db.query('INSERT INTO absences (studentId, courseId, date, justified, justification, synced) VALUES (?, ?, ?, ?, ?, ?)',
    [studentId, courseId, date, justified || false, justification, synced || false], (err, result) => {
      if (err) return res.status(500).json(err);
      res.json({ id: result.insertId });
    });
});

router.put('/:id', (req, res) => {
  const { studentId, courseId, date, justified, justification, synced } = req.body;
  db.query('UPDATE absences SET studentId=?, courseId=?, date=?, justified=?, justification=?, synced=? WHERE id=?',
    [studentId, courseId, date, justified || false, justification, synced || false, req.params.id], (err) => {
      if (err) return res.status(500).json(err);
      res.json({ message: 'Absence mise à jour' });
    });
});

router.delete('/:id', (req, res) => {
  db.query('DELETE FROM absences WHERE id=?', [req.params.id], (err) => {
    if (err) return res.status(500).json(err);
    res.json({ message: 'Absence supprimée' });
  });
});

module.exports = router;

