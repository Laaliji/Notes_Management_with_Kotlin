const express = require('express');
const cors = require('cors');
const bodyParser = require('body-parser');

const studentRoutes = require('./routes/students');
const courseRoutes = require('./routes/courses');
const absenceRoutes = require('./routes/absences');
const noteRoutes = require('./routes/notes');

const app = express();
app.use(cors());
app.use(bodyParser.json());

app.use('/api/students', studentRoutes);
app.use('/api/courses', courseRoutes);
app.use('/api/absences', absenceRoutes);
app.use('/api/notes', noteRoutes);

const PORT = 3000;
app.listen(PORT, () => console.log(` Serveur lancé sur http://localhost:${PORT}`));
