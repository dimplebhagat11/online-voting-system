## Online Voting System
A simple voting web application built with Jakarta Servlets, JSP, JDBC, and MySQL.
![Voting System](Voting%20System.png)
## Features
- Displays available subjects to vote for.
- Lets a visitor submit one vote per browser session.
- Shows the vote counts and percentages.
- Stores votes in a MySQL database.
- Uses a simple, student-friendly web interface.
## Technologies Used
- Java
- Jakarta Servlets and JSP
- HTML and CSS
- JDBC
- MySQL
- Apache Tomcat 10.1
- Maven
## Database Setup
1. Start your local MySQL server and open MySQL Workbench.
2. Open the SQL script in the project's database folder.
3. Execute the script to create the database and tables.
4. In NetBeans, open src/main/java/com/example/voting/DBConnection.java.
## Run the Project
1. Open the OnlineVotingSystem project folder in NetBeans (the folder containing pom.xml).
2. Make sure Apache Tomcat 10.1 is configured in NetBeans.
3. Right-click the project and choose Clean and Build.
4. Click Run and open the local URL shown by NetBeans.
## Concepts Demonstrated
- HTML form handling
- Jakarta Servlet request processing
- Session tracking with HttpSession
- JDBC database connectivity
- SQL tables, primary keys, foreign keys, and queries
## Limitations
This project is for educational use. The one-vote restriction is based on a browser session, not a verified person's identity. Votes are stored in MySQL, but the session-based restriction is not suitable for a real election.
## Screenshots
### Voting Page
![Voting Page](Voting%20Page.png)

### Vote Confirmation
![Vote Confirmation](Vote%20Confirmation.png)

### Results Page
![Results Page](Results%20Page.png)
