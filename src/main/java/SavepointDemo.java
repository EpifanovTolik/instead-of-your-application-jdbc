import java.sql.*;

public class SavepointDemo {
    /**
     * JDBC Driver and database url
     */
    static final String JDBC_DRIVER = "org.postgresql.Driver";
    static final String DATABASE_URL = "jdbc:postgresql://localhost/proselyte_jdbc_db";

    /**
     * User and Password
     */

    static final String USER = "postgres";
    static final String PASSWORD = "admin";

    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        Connection connection = null;
        Statement statement = null;

        Class.forName("org.postgresql.Driver");

        connection = DriverManager.getConnection(DATABASE_URL, USER, PASSWORD);
        connection.setAutoCommit(false);

        statement = connection.createStatement();

        String SQL;
        SQL = "SELECT * FROM developers";

        ResultSet resultSet = statement.executeQuery(SQL);

        System.out.println("Retrieving data from database...");
        System.out.println("\nDevelopers:");
        while (resultSet.next()) {
            int id = resultSet.getInt("id");
            String name = resultSet.getString("name");
            String specialty = resultSet.getString("specialty");
            int salary = resultSet.getInt("salary");

            System.out.println("\n================\n");
            System.out.println("id: " + id);
            System.out.println("Name: " + name);
            System.out.println("Specialty: " + specialty);
            System.out.println("Salary: $" + salary);
        }

        System.out.println("\n===========================\n");
        System.out.println("Creating savepoint...");
        Savepoint savepointOne = connection.setSavepoint("SavepointOne");

        try {
            SQL = "INSERT INTO developer VALUES (6, 'John','C#', 2200)";
            statement.executeUpdate(SQL);

            SQL = "INSERT INTO developers VALUES (7, 'Ron', 'Ruby', 1900)";
            statement.executeUpdate(SQL);

            connection.commit();
        } catch (SQLException e) {
            System.out.println("SQLException. Executing rollback to savepoint...");
            connection.rollback(savepointOne);
        }
        SQL = "SELECT * FROM developers";
        resultSet = statement.executeQuery(SQL);
        System.out.println("Retrieving data from database...");
        System.out.println("\nDevelopers:");
        while (resultSet.next()) {
            int id = resultSet.getInt("id");
            String name = resultSet.getString("name");
            String specialty = resultSet.getString("specialty");
            int salary = resultSet.getInt("salary");

            System.out.println("id: " + id);
            System.out.println("Name: " + name);
            System.out.println("Specialty: " + specialty);
            System.out.println("Salary: $" + salary);
            System.out.println("\n================\n");
        }

        System.out.println("Closing connection and releasing resources...");
        resultSet.close();
        statement.close();
        connection.close();
    }
}
