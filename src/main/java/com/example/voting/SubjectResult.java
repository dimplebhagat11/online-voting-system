package com.example.voting;

/**
 * Holds the vote count and percentage for one subject.
 * This small class makes it easy for the JSP page to display results.
 */
public class SubjectResult {

    private final String name;
    private final int votes;
    private final int percentage;

    public SubjectResult(String name, int votes, int percentage) {
        this.name = name;
        this.votes = votes;
        this.percentage = percentage;
    }

    public String getName() {
        return name;
    }

    public int getVotes() {
        return votes;
    }

    public int getPercentage() {
        return percentage;
    }
}
