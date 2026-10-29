# Online Voting System (Jakarta Servlet + MySQL)

A beginner-friendly web application for a favourite-subject poll. The project demonstrates **Advanced Internet Programming** concepts with Jakarta Servlets, JSP, HTML forms, CSS and HTTP sessions, and **Advanced Database Management System** concepts with MySQL, JDBC, relationships, constraints, joins and aggregate queries.

## Features

* Displays poll options stored in MySQL.
* Submits a selected option through an HTML form and a Jakarta Servlet `doPost()` method.
* Uses `HttpSession` to remember that a visitor has voted.
* Stores every accepted vote in MySQL, so results remain after Tomcat restarts.
* Uses a unique database constraint on `session\_token` as an extra guard against duplicate votes from the same session.
* Displays vote counts and percentages using data read from MySQL.
* Uses a simple, responsive layout suitable for a classroom demonstration.

## Requirements

* JDK 11 or newer
* Apache Tomcat **10.1** (Jakarta Servlet 6)
* MySQL Server 8.0 or compatible
* NetBeans with Maven support
* MySQL Workbench is recommended for running the SQL script

> This is a Jakarta project. Use Tomcat 10.1, not Tomcat 9, because the code imports `jakarta.servlet` classes.

## Step 1: Create the database

1. Start the MySQL server.
2. Open MySQL Workbench and connect to the local MySQL instance.
3. Open `database/online\_voting.sql` from this project.
4. Run the complete script (the lightning-bolt execute button).
5. Refresh the Schemas panel. We should see the `online\_voting` database and its `subjects` and `votes` tables.

The script adds four starter options: Computer Science, Mathematics, Data Science and English. To change the poll options, edit the `INSERT IGNORE` section of the SQL script and run the appropriate `INSERT` statements. The options shown by the web app come from the `subjects` table.

## Step 2: Set the MySQL username and password

Open this file in NetBeans:

`src/main/java/com/example/voting/DBConnection.java`

Find these lines and change them to match your local MySQL installation:

```java
private static final String URL =
        "jdbc:mysql://localhost:3306/online\_voting?useSSL=false\&serverTimezone=UTC";
private static final String USER = "root";
private static final String PASSWORD = "CHANGE\_ME";
```

* Keep the URL as shown if MySQL runs on your computer at port `3306` and you used the supplied database name.
* `USER` is the MySQL username (often `root` for a local student setup).
* Replace `CHANGE\_ME` with the actual MySQL password.
* Do not put the password inside quotation marks twice as the Java string itself already uses quotes.

The MySQL JDBC driver is declared in `pom.xml`. Maven downloads it when the project is built. An internet connection may be needed the first time Maven resolves dependencies.

## Step 3: Open and run in NetBeans

1. Extract `OnlineVotingSystem.zip`.
2. In NetBeans, choose **File → Open Project**.
3. Select the extracted `OnlineVotingSystem` folder (the one containing `pom.xml`).
4. Wait for Maven to download dependencies.
5. Select a JDK 11+ platform and Apache Tomcat 10.1 in project/server settings.
6. Right-click the project and choose **Run**.
7. If the browser does not open automatically, visit `http://localhost:8080/OnlineVotingSystem/` (adjust the port if your Tomcat uses another port).

## Step 4: Test the project

1. Select a subject and click **Submit my vote**.
2. The website should confirm that the vote was counted and show that your session has already voted.
3. Click **View results** to see vote totals and percentages loaded from MySQL.
4. Open the app in a private/incognito window or another browser to submit a second demonstration vote.
5. Try submitting again in the first session. The application should reject the second vote.
6. Restart Tomcat and visit the results page. Database votes should remain.

## How the code works

* `VotingServlet.java`: handles page requests, validates form submission, checks the session and forwards to the JSP views.
* `DBConnection.java`: creates a JDBC connection to MySQL. Edit this file with your local login credentials.
* `VoteDAO.java`: contains SQL statements for loading subjects, saving votes and counting results.
* `SubjectResult.java`: holds a subject's name, vote count and percentage for the results page.
* `index.jsp`: displays the voting form or an already-voted/database-error message.
* `results.jsp`: shows vote totals and percentage bars.
* `css/style.css`: controls colours, spacing, cards and responsive layout.
* `database/online\_voting.sql`: creates and seeds the database tables.
* `database/queries\_for\_presentation.sql`: contains extra SQL queries for DBMS explanation.
* `pom.xml`: lists the Jakarta Servlet, JSTL and MySQL JDBC dependencies.

## Database design 

* `subject\_id`: primary key; automatically generated.
* `subject\_name`: required and unique, so the same option is not added twice.

### `votes` table

* `vote\_id`: primary key; automatically generated.
* `subject\_id`: foreign key referencing `subjects.subject\_id`.
* `session\_token`: unique session identifier generated by the application; prevents two rows for one session.
* `voted\_at`: timestamp automatically set when a vote is stored.

Relationship: **one subject can have many votes**, while each vote belongs to one subject. The results query uses a `LEFT JOIN`, `COUNT()` and `GROUP BY` so that subjects with no votes still appear with a count of zero.

## Coursework concepts covered

**Advanced Internet Programming:** Jakarta Servlet, `doGet()`, `doPost()`, HTTP request parameters, HTML form handling, JSP, session tracking, redirects, and JDBC.

**Advanced Database Management System:** database/table creation, primary keys, foreign keys, `UNIQUE` and `NOT NULL` constraints, one-to-many relationship, `INSERT`, `SELECT`, `LEFT JOIN`, `COUNT()`, `GROUP BY`, and `ORDER BY`.

## Important limitations

* The one-vote check is per HTTP session, not per verified student. Clearing browser cookies or using another browser creates a different session.
* This is a classroom demonstration, not a secure real-world election system.
* Vote totals are stored persistently in MySQL. They do **not** reset when Tomcat restarts. To clear demo votes, run `DELETE FROM votes;` in MySQL Workbench. Then open a new private/incognito session (or let the current session expire), because the current browser session will still remember that it has voted. 

