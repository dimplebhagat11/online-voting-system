-- Online Voting System database
-- Run this entire script in MySQL Workbench or the MySQL command line.

CREATE DATABASE IF NOT EXISTS online_voting;
USE online_voting;

CREATE TABLE IF NOT EXISTS subjects (
    subject_id INT AUTO_INCREMENT PRIMARY KEY,
    subject_name VARCHAR(100) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS votes (
    vote_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    subject_id INT NOT NULL,
    session_token CHAR(36) NOT NULL UNIQUE,
    voted_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_votes_subject
        FOREIGN KEY (subject_id) REFERENCES subjects(subject_id)
);

-- Add the default poll options. INSERT IGNORE avoids duplicates if re-run.
INSERT IGNORE INTO subjects (subject_name) VALUES
    ('Computer Science'),
    ('Mathematics'),
    ('Data Science'),
    ('English');

-- Quick check: list the available options.
SELECT * FROM subjects ORDER BY subject_id;
