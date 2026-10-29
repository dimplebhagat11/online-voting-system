package com.example.voting;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/** Displays the poll and processes votes using MySQL and HTTP sessions. */
@WebServlet(urlPatterns = {"/home", "/vote"})
public class VotingServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final String HAS_VOTED = "hasVoted";
    private static final String SELECTED_SUBJECT = "selectedSubject";
    private static final String SESSION_TOKEN = "votingSessionToken";

    private final VoteDAO voteDAO = new VoteDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        HttpSession session = request.getSession();
        List<String> subjects = new ArrayList<>();
        List<SubjectResult> results = new ArrayList<>();
        int totalVotes = 0;
        boolean dbError = false;

        try {
            subjects = voteDAO.getSubjects();
            results = voteDAO.getResults();
            for (SubjectResult result : results) {
                totalVotes += result.getVotes();
            }
        } catch (SQLException e) {
            getServletContext().log("Could not load voting information from MySQL.", e);
            dbError = true;
        }

        request.setAttribute("subjects", subjects);
        request.setAttribute("results", results);
        request.setAttribute("totalVotes", totalVotes);
        request.setAttribute("dbError", dbError);
        request.setAttribute("hasVoted", Boolean.TRUE.equals(session.getAttribute(HAS_VOTED)));
        request.setAttribute("selectedSubject", session.getAttribute(SELECTED_SUBJECT));

        if ("results".equals(request.getParameter("view"))) {
            request.getRequestDispatcher("/WEB-INF/views/results.jsp").forward(request, response);
        } else {
            request.getRequestDispatcher("/WEB-INF/views/index.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        HttpSession session = request.getSession();
        String selected = request.getParameter("subject");
        String status;

        // Synchronizing on the session prevents two quick submissions from the same
        // browser session being processed together.
        synchronized (session) {
            if (Boolean.TRUE.equals(session.getAttribute(HAS_VOTED))) {
                status = "already";
            } else if (selected == null || selected.trim().isEmpty()) {
                status = "invalid";
            } else {
                String sessionToken = (String) session.getAttribute(SESSION_TOKEN);
                if (sessionToken == null) {
                    sessionToken = UUID.randomUUID().toString();
                    session.setAttribute(SESSION_TOKEN, sessionToken);
                }

                try {
                    String previousVote = voteDAO.getSubjectForSession(sessionToken);
                    if (previousVote != null) {
                        session.setAttribute(HAS_VOTED, Boolean.TRUE);
                        session.setAttribute(SELECTED_SUBJECT, previousVote);
                        status = "already";
                    } else if (voteDAO.saveVote(selected, sessionToken)) {
                        session.setAttribute(HAS_VOTED, Boolean.TRUE);
                        session.setAttribute(SELECTED_SUBJECT, selected);
                        status = "voted";
                    } else {
                        status = "invalid";
                    }
                } catch (SQLException e) {
                    getServletContext().log("Could not save vote to MySQL.", e);
                    status = "db-error";
                }
            }
        }

        response.sendRedirect(request.getContextPath() + "/home?status=" + status);
    }
}
