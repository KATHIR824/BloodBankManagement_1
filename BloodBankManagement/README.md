# Blood Bank Management System (Java Swing + MySQL)

## Project Structure
```
BloodBankManagement/
├── sql/
│   └── schema.sql            # Database schema (tables + sample data)
├── src/
│   ├── Main.java             # Entry point
│   ├── db/
│   │   └── DBConnection.java # MySQL connection handler
│   ├── model/
│   │   ├── Donor.java
│   │   └── BloodRequest.java
│   ├── dao/
│   │   ├── DonorDAO.java
│   │   ├── BloodStockDAO.java
│   │   ├── BloodRequestDAO.java
│   │   └── LoginDAO.java
│   └── gui/
│       ├── LoginFrame.java
│       ├── DashboardFrame.java
│       ├── DonorFrame.java
│       ├── StockFrame.java
│       └── RequestFrame.java
└── lib/                      # put mysql-connector-j-x.x.x.jar here
```

## Step 1: Set up the database
1. Open MySQL Workbench (or the MySQL command line).
2. Run the script in `sql/schema.sql`. This creates the `bloodbank` database
   with 4 tables: `login`, `donor`, `blood_stock`, `blood_request`.
   - **If you already have your own blood bank database**, don't run this
     script. Instead, either rename your tables/columns to match the ones
     used here, or edit the DAO classes (`src/dao/*.java`) so the SQL
     queries use YOUR existing table/column names.
3. Note down your MySQL username and password — you'll need them next.

## Step 2: Download the MySQL JDBC driver
1. Download `mysql-connector-j` (the `.jar` file) from MySQL's official site:
   https://dev.mysql.com/downloads/connector/j/
2. Place the `.jar` file inside the `lib/` folder of this project.

## Step 3: Configure the connection
Open `src/db/DBConnection.java` and edit these three lines to match your setup:
```java
private static final String URL = "jdbc:mysql://localhost:3306/bloodbank?useSSL=false&serverTimezone=UTC";
private static final String USER = "root";
private static final String PASSWORD = "your_mysql_password";
```

## Step 4: Open the project in your IDE
**Using IntelliJ IDEA / Eclipse (recommended):**
1. Open the `BloodBankManagement` folder as a new project.
2. Add the JDBC jar to the classpath:
   - **IntelliJ**: File → Project Structure → Modules → Dependencies → "+" → JARs → select the mysql-connector jar in `lib/`.
   - **Eclipse**: Right-click project → Build Path → Add External JARs → select the jar in `lib/`.
3. Mark `src` as the Sources Root (IntelliJ does this automatically if you open `src` as the source folder).
4. Run `Main.java`.

**Using the command line instead:**
```bash
cd BloodBankManagement
javac -d out -cp "lib/mysql-connector-j-*.jar" $(find src -name "*.java")
java -cp "out:lib/mysql-connector-j-*.jar" Main
```
(On Windows, use `;` instead of `:` in the `-cp` classpath, and adjust the jar filename to match what you downloaded.)

## Step 5: Log in
Default login (from `schema.sql`):
- **Username:** `admin`
- **Password:** `admin123`

## What the app does
- **Login screen** — authenticates against the `login` table.
- **Dashboard** — navigation to Donors, Stock, and Requests.
- **Manage Donors** — add/update/delete donors. Adding a donor automatically
  adds +1 unit to that blood group's stock.
- **View Blood Stock** — shows units available per blood group.
- **Manage Blood Requests** — submit a request; "Approve & Issue Blood"
  deducts the requested units from stock (only if enough stock exists) and
  marks the request Approved; "Reject" marks it Rejected.

## Next steps / ideas to extend it
- Add donor donation history / eligibility check (e.g. 90 days since last donation).
- Add a hospital/recipient table with more details.
- Add password hashing instead of storing plain text passwords.
- Add reports (e.g. print donor list, low-stock alerts) using JasperReports or a simple table export.
- Add role-based access (admin vs staff).
