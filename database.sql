CREATE TABLE IF NOT EXISTS hospitals (
    id SERIAL PRIMARY KEY,
    name TEXT NOT NULL
);

CREATE TABLE IF NOT EXISTS staff (
    id SERIAL PRIMARY KEY,
    name TEXT NOT NULL,
    role TEXT NOT NULL,
    hospital_id INTEGER NOT NULL REFERENCES hospitals(id),
    username TEXT UNIQUE NOT NULL,
    password TEXT NOT NULL
);

CREATE TABLE IF NOT EXISTS patients (
    id SERIAL PRIMARY KEY,
    name TEXT NOT NULL,
    birth_date DATE,
    phone TEXT
);

CREATE TABLE IF NOT EXISTS appointments (
    id SERIAL PRIMARY KEY,
    patient_id INTEGER NOT NULL REFERENCES patients(id),
    staff_id INTEGER NOT NULL REFERENCES staff(id),
    date TIMESTAMP NOT NULL,
    reason TEXT
);

CREATE TABLE IF NOT EXISTS logs (
    id SERIAL PRIMARY KEY,
    user_id INTEGER REFERENCES staff(id),
    action TEXT,
    table_name TEXT,
    timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    details TEXT
);

INSERT INTO hospitals (name)
SELECT 'Главная больница'
WHERE NOT EXISTS (SELECT 1 FROM hospitals);

INSERT INTO staff (name, role, hospital_id, username, password)
SELECT 'Админ', 'admin', 1, 'admin', 'admin'
WHERE NOT EXISTS (SELECT 1 FROM staff);
