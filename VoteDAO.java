package com.example.voting;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/** Contains the SQL operations used by the voting application. */
public class VoteDAO {

    public List<String> getSubjects() throws SQLException {
        List<String> subjects = new ArrayList<>();
        String sql = "SELECT subject_name FROM subjects ORDER BY subject_id";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                subjects.add(rs.getString("subject_name"));
            }
        }
        return subjects;
    }

    public List<SubjectResult> getResults() throws SQLException {
        List<SubjectResult> results = new ArrayList<>();
        int totalVotes = 0;
        String sql = "SELECT s.subject_name, COUNT(v.vote_id) AS vote_count "
                + "FROM subjects s LEFT JOIN votes v ON s.subject_id = v.subject_id "
                + "GROUP BY s.subject_id, s.subject_name ORDER BY s.subject_id";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                String name = rs.getString("subject_name");
                int votes = rs.getInt("vote_count");
                totalVotes += votes;
                results.add(new SubjectResult(name, votes, 0));
            }
        }

        List<SubjectResult> completedResults = new ArrayList<>();
        for (SubjectResult result : results) {
            int percentage = totalVotes == 0
                    ? 0
                    : (int) Math.round(result.getVotes() * 100.0 / totalVotes);
            completedResults.add(new SubjectResult(result.getName(), result.getVotes(), percentage));
        }
        return completedResults;
    }

    /**
     * Saves a vote by looking up the subject in the database.
     * The unique session_token column is an additional database-level guard
     * against saving more than one vote for the same session.
     */
    public boolean saveVote(String subjectName, String sessionToken) throws SQLException {
        String sql = "INSERT INTO votes (subject_id, session_token) "
                + "SELECT subject_id, ? FROM subjects WHERE subject_name = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, sessionToken);
            ps.setString(2, subjectName);
            return ps.executeUpdate() == 1;
        }
    }

    public String getSubjectForSession(String sessionToken) throws SQLException {
        String sql = "SELECT s.subject_name FROM votes v "
                + "JOIN subjects s ON v.subject_id = s.subject_id "
                + "WHERE v.session_token = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, sessionToken);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getString("subject_name") : null;
            }
        }
    }
}
