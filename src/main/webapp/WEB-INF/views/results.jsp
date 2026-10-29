<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Poll Results | Campus Poll</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<header class="topbar">
    <a class="brand" href="${pageContext.request.contextPath}/" aria-label="Campus Poll home">
        <span class="brand-mark">✓</span>
        <span>campus<span class="brand-light">poll</span></span>
    </a>
    <a class="nav-link" href="${pageContext.request.contextPath}/">Back to poll <span aria-hidden="true">↗</span></a>
</header>

<main class="page-wrap results-wrap">
    <section class="hero results-hero">
        <div class="eyebrow"><span class="live-dot"></span> POLL OVERVIEW</div>
        <h1>Here’s what<br><span>everyone thinks.</span></h1>
        <p class="hero-copy">A quick look at the favourite subjects selected by students.</p>
    </section>

    <section class="poll-card results-card" aria-labelledby="results-title">
        <div class="results-topline">
            <div>
                <p class="section-label">CURRENT STANDINGS</p>
                <h2 id="results-title">Favourite subject</h2>
            </div>
            <div class="total-badge"><strong><c:out value="${totalVotes}"/></strong><span>total votes</span></div>
        </div>

        <c:if test="${dbError}">
            <div class="notice notice-error" role="alert"><span class="notice-icon">!</span><div><strong>Could not load results from MySQL.</strong><br>Check that MySQL is running, execute <code>database/online_voting.sql</code>, and verify <code>DBConnection.java</code>.</div></div>
        </c:if>
        <c:if test="${hasVoted}">
            <div class="notice notice-success compact-notice" role="status">Your vote for <strong><c:out value="${selectedSubject}"/></strong> has been included.</div>
        </c:if>

        <c:if test="${not dbError and totalVotes eq 0}">
            <div class="empty-state"><div class="empty-icon">▤</div><h3>No votes yet</h3><p>Be the first to choose your favourite subject.</p></div>
        </c:if>

        <c:if test="${not dbError}">
        <div class="results-list">
            <c:forEach items="${results}" var="result" varStatus="loop">
                <div class="result-row">
                    <div class="result-label-line">
                        <div class="result-name"><span class="result-number">0${loop.count}</span><span><c:out value="${result.name}"/></span></div>
                        <div class="result-stat"><strong><c:out value="${result.percentage}"/>%</strong><span><c:out value="${result.votes}"/> vote<c:if test="${result.votes ne 1}">s</c:if></span></div>
                    </div>
                    <div class="progress-track" role="progressbar" aria-label="${result.name} votes" aria-valuemin="0" aria-valuemax="100" aria-valuenow="${result.percentage}">
                        <div class="progress-fill fill-${loop.count}" style="width: ${result.percentage}%"></div>
                    </div>
                </div>
            </c:forEach>
        </div>
        </c:if>

        <a class="button button-primary button-full results-back-button" href="${pageContext.request.contextPath}/">${hasVoted ? 'Back to my vote' : 'Back to the poll'} <span aria-hidden="true">→</span></a>
        <p class="form-note results-note">Results are read from MySQL and remain available after the server restarts.</p>
    </section>
    <p class="page-footnote">A small campus poll built to make every voice count.</p>
</main>
</body>
</html>
