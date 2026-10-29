-- Useful SQL queries to explain in the Advanced Database Management System presentation.
USE online_voting;

-- 1. Display all available subjects.
SELECT subject_id, subject_name FROM subjects ORDER BY subject_id;

-- 2. Count votes for each subject (including subjects with zero votes).
SELECT s.subject_name, COUNT(v.vote_id) AS total_votes
FROM subjects s
LEFT JOIN votes v ON s.subject_id = v.subject_id
GROUP BY s.subject_id, s.subject_name
ORDER BY s.subject_id;

-- 3. Show the total number of votes.
SELECT COUNT(*) AS all_votes FROM votes;

-- 4. Show the most popular subject(s).
SELECT s.subject_name, COUNT(v.vote_id) AS total_votes
FROM subjects s
LEFT JOIN votes v ON s.subject_id = v.subject_id
GROUP BY s.subject_id, s.subject_name
ORDER BY total_votes DESC;

-- 5. Show when votes were submitted (do not display the session token).
SELECT vote_id, subject_id, voted_at FROM votes ORDER BY voted_at DESC;

-- 6. Optional: clear all votes to restart a demo poll. This does not delete subjects.
-- DELETE FROM votes;
