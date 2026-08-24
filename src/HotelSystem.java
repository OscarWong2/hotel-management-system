import java.sql.*;
import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.time.Duration;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

public class HotelSystem
{
    private static final String DB = "hotel_database";
    private static final String HOST = "";
    private static final String PORT = "";
    private static final String USERNAME = "";
    private static final String PASSWORD = "";
    private static final String URL = "jdbc:mysql://" + HOST + ":" + PORT + "/";

    private static int employeeIdLoggedIn;

    // Load driver
    private static boolean loadDriver()
    {
        try
        {
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("Driver loaded.");
            return true;
        }
        catch (ClassNotFoundException error)
        {
            System.out.println(error);
            return false;
        }
    }

    // Update database methods
    public static boolean addBooking(Booking newBooking)
    {
        try (Connection connection = DriverManager.getConnection(URL + DB, USERNAME, PASSWORD))
        {
            if (connection == null)
            {
                System.out.println("Connection to database failed.");
                return false;
            }

            // Add booking
            PreparedStatement query = connection.prepareStatement(
                "INSERT INTO booking(customer_id," +
                    " start_date," +
                    " end_date," +
                    " party_count," +
                    " book_date," +
                    " total_charge," +
                    " points_used," +
                    " status," +
                    " booked_by_employee)" +
                " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?);"
            );
            query.setInt(1, newBooking.getCustomerId());
            query.setString(2, newBooking.getStartDate());
            query.setString(3, newBooking.getEndDate());
            query.setInt(4, newBooking.getPartyCount());
            query.setString(5, newBooking.getBookDate());
            query.setFloat(6, newBooking.getTotalCharge());
            query.setFloat(7, newBooking.getPointsUsed());
            query.setString(8, newBooking.getStatus());
            if (newBooking.getBookedByEmployee() == null)
                query.setNull(9, Types.INTEGER);
            else
                query.setInt(9, (int)newBooking.getBookedByEmployee());
            query.executeUpdate();

            return true;
        }
        catch (SQLException error)
        {
            System.out.println(error);
            return false;
        }
    }
    public static boolean addCustomer(Customer newCustomer)
    {
        try (Connection connection = DriverManager.getConnection(URL + DB, USERNAME, PASSWORD))
        {
            if (connection == null)
            {
                System.out.println("Connection to database failed.");
                return false;
            }

            PreparedStatement query = connection.prepareStatement(
                "INSERT INTO customer(first_name, last_name, phone, email)" +
                " VALUES (?, ?, ?, ?);"
            );
            query.setString(1, newCustomer.getFirstName());
            query.setString(2, newCustomer.getLastName());
            query.setString(3, newCustomer.getPhone());
            query.setString(4, newCustomer.getEmail());
            query.executeUpdate();

            return true;
        }
        catch (SQLException error)
        {
            System.out.println(error);
            return false;
        }
    }
    public static boolean updateBookingStatus(int id, boolean cancel, ArrayList<Integer> rooms)
    {
        try (Connection connection = DriverManager.getConnection(URL + DB, USERNAME, PASSWORD))
        {
            if (connection == null)
            {
                System.out.println("Connection to database failed.");
                return false;
            }

            if (cancel)
            {
                PreparedStatement query = connection.prepareStatement(
                    "UPDATE booking" +
                    " SET status = 'cancelled'" +
                    " WHERE booking_id = ? AND status = 'booked' AND CURDATE() < start_date;"
                );
                query.setInt(1, id);
                query.executeUpdate();
                return true;
            }

            PreparedStatement query = connection.prepareStatement(
                "UPDATE booking" +
                " SET status = CASE" +
                    " WHEN status = 'booked' THEN 'checked_in'" +
                    " WHEN status = 'checked_in' OR status = 'overstayed' THEN 'checked_out'" +
                    " ELSE status" +
                " END" +
                " WHERE booking_id = ?;"
            );
            query.setInt(1, id);
            query.executeUpdate();

            if (!rooms.isEmpty())
            {
                String queryArray = "(";
                for (int i = 1; i <= rooms.size(); i++)
                    queryArray += "?, ";
                queryArray = queryArray.substring(0, queryArray.length() - 2) + ");";

                query = connection.prepareStatement(
                    "UPDATE room" +
                    " SET occupied_by = ?" +
                    " WHERE room_num IN " +
                    queryArray
                );
                query.setInt(1, id);
                for (int i = 1; i <= rooms.size(); i++)
                    query.setInt(i + 1, rooms.get(i - 1));
                query.executeUpdate();
            }

            return true;
        }
        catch (SQLException error)
        {
            System.out.println(error);
            return false;
        }
    }
    public static boolean updateBookingDates(int id,
                                                String startDate,
                                                String endDate,
                                                float totalCharge)
    {
        try (Connection connection = DriverManager.getConnection(URL + DB, USERNAME, PASSWORD))
        {
            if (connection == null)
            {
                System.out.println("Connection to database failed.");
                return false;
            }

            PreparedStatement query = connection.prepareStatement(
                "UPDATE booking" +
                " SET start_date = ?," +
                    " end_date = ?," +
                    " base_charge = ?," +
                    " total_charge = base_charge" +
                " WHERE booking_id = ?;"
            );
            query.setString(1, startDate);
            query.setString(2, endDate);
            query.setFloat(3, totalCharge);
            query.setInt(4, id);
            query.executeUpdate();

            return true;
        }
        catch (SQLException error)
        {
            System.out.println(error);
            return false;
        }
    }
    public static boolean updatePointsUsed(int id, float pointsUsed)
    {
        try (Connection connection = DriverManager.getConnection(URL + DB, USERNAME, PASSWORD))
        {
            if (connection == null)
            {
                System.out.println("Connection to database failed.");
                return false;
            }

            PreparedStatement query = connection.prepareStatement(
                "UPDATE booking, customer" +
                " SET points_used = ?," +
                    " points = points - points_used," +
                    " total_charge = total_charge - points_used" +
                " WHERE booking.customer_id = customer.customer_id" +
                    " AND booking_id = ?" +
                    " AND status = 'booked';"
            );
            query.setFloat(1, pointsUsed);
            query.setInt(2, id);
            query.executeUpdate();

            return true;
        }
        catch (SQLException error)
        {
            System.out.println(error);
            return false;
        }
    }

    // Select from database methods
    public static Object getFromId(String table, int id)
    {
        try (Connection connection = DriverManager.getConnection(URL + DB, USERNAME, PASSWORD))
        {
            if (connection == null || table.equals("room"))
            {
                System.out.println("Connection to database failed.");
                return -1;
            }

            PreparedStatement query;
            switch (table)
            {
                case "customer" ->
                    query = connection.prepareStatement(
                        "SELECT *" +
                        " FROM customer" +
                        " WHERE customer_id = ?;"
                    );
                case "employee" ->
                    query = connection.prepareStatement(
                        "SELECT *" +
                        " FROM employee" +
                        " WHERE employee_id = ?;"
                    );
                default ->
                    query = connection.prepareStatement(
                        "SELECT *" +
                        " FROM booking" +
                        " WHERE booking_id = ?;"
                    );
            }
            query.setInt(1, id);

            ResultSet result = query.executeQuery();
            if (!result.next())
                return null;

            switch (table)
            {
                case "customer" ->
                {
                    return new Customer(
                        result.getInt("customer_id"),
                        result.getString("first_name"),
                        result.getString("last_name"),
                        result.getString("phone"),
                        result.getString("email"),
                        result.getInt("points")
                    );
                }
                case "employee" ->
                {
                    return new Employee(
                        result.getInt("employee_id"),
                        result.getString("first_name"),
                        result.getString("last_name"),
                        result.getString("phone"),
                        result.getString("email"),
                        result.getBoolean("is_manager")
                    );
                }
                default ->
                {
                    return new Booking(
                        result.getInt("booking_id"),
                        result.getInt("customer_id"),
                        result.getString("start_date"),
                        result.getString("end_date"),
                        result.getInt("party_count"),
                        result.getString("book_date"),
                        result.getFloat("total_charge"),
                        result.getFloat("points_used"),
                        result.getString("status"),
                        (Integer)result.getObject("booked_by_employee")
                    );
                }
            }
        }
        catch (SQLException error)
        {
            System.out.println(error);
            return -1;
        }
    }
    public static ArrayList<Object> filterRecords(String table, String filter)
    {
        try (Connection connection = DriverManager.getConnection(URL + DB, USERNAME, PASSWORD))
        {
            if (connection == null)
            {
                System.out.println("Connection to database failed.");
                return null;
            }

            PreparedStatement query;
            if (!filter.isEmpty())
            {
                switch (table)
                {
                    case "customer" ->
                    {
                        query = connection.prepareStatement(
                            "SELECT *" +
                            " FROM customer" +
                            " WHERE customer_id = ?" +
                                " OR first_name LIKE ?" +
                                " OR last_name LIKE ?" +
                                " OR phone LIKE ?" +
                                " OR email LIKE ?" +
                            " ORDER BY last_name ASC;"
                        );
                        query.setString(1, filter);
                        for (int i = 1; i <= 5; i++)
                            query.setString(i, "%" + filter + "%");
                    }
                    case "employee" ->
                    {
                        query = connection.prepareStatement(
                            "SELECT *" +
                            " FROM employee" +
                            " WHERE employee_id = ?" +
                                " OR first_name LIKE ?" +
                                " OR last_name LIKE ?" +
                            " ORDER BY last_name ASC;"
                        );
                        query.setString(1, filter);
                        for (int i = 2; i <= 3; i++)
                            query.setString(i, "%" + filter + "%");
                    }
                    default ->
                    {
                        query = connection.prepareStatement(
                            "SELECT *" +
                            " FROM booking, customer" +
                            " WHERE booking.customer_id = customer.customer_id AND (" +
                                " booking_id = ?" +
                                " OR first_name LIKE ?" +
                                " OR last_name LIKE ?" +
                                " OR phone LIKE ?" +
                                " OR start_date LIKE ?" +
                                " OR end_date LIKE ?" +
                                " OR book_date LIKE ?" +
                                " OR booked_by_employee = ?)" +
                            " ORDER BY book_date ASC;"
                        );
                        query.setString(1, filter);
                        for (int i = 2; i <= 7; i++)
                            query.setString(i, "%" + filter + "%");
                        query.setString(8, filter);
                    }
                }
            }
            else
            {
                switch (table)
                {
                    case "customer" ->
                        query = connection.prepareStatement(
                            "SELECT *" +
                            " FROM customer" +
                            " ORDER BY last_name ASC;"
                        );
                    case "employee" ->
                        query = connection.prepareStatement(
                            "SELECT *" +
                            " FROM employee" +
                            " ORDER BY last_name ASC;"
                        );
                    default ->
                        query = connection.prepareStatement(
                            "SELECT *" +
                            " FROM booking" +
                            " ORDER BY book_date ASC;"
                        );
                }
            }

            ResultSet result = query.executeQuery();
            ArrayList<Object> records = new ArrayList<>();
            while (result.next())
            {
                switch (table)
                {
                    case "customer" ->
                        records.add(new Customer(
                            result.getInt("customer_id"),
                            result.getString("first_name"),
                            result.getString("last_name"),
                            result.getString("phone"),
                            result.getString("email"),
                            result.getInt("points")
                        ));
                    case "employee" ->
                        records.add(new Employee(
                            result.getInt("employee_id"),
                            result.getString("first_name"),
                            result.getString("last_name"),
                            result.getString("phone"),
                            result.getString("email"),
                            result.getBoolean("is_manager")
                        ));
                    default ->
                        records.add(new Booking(
                            result.getInt("booking_id"),
                            result.getInt("customer_id"),
                            result.getString("start_date"),
                            result.getString("end_date"),
                            result.getInt("party_count"),
                            result.getString("book_date"),
                            result.getFloat("total_charge"),
                            result.getFloat("points_used"),
                            result.getString("status"),
                            (Integer)result.getObject("booked_by_employee")
                        ));
                }
            }
            return records;
        }
        catch (SQLException error)
        {
            System.out.println(error);
            return null;
        }
    }
    public static ArrayList<Room> getAvailRooms()
    {
        try (Connection connection = DriverManager.getConnection(URL + DB, USERNAME, PASSWORD))
        {
            if (connection == null)
            {
                System.out.println("Connection to database failed.");
                return null;
            }

            PreparedStatement query = connection.prepareStatement(
                "SELECT *" +
                " FROM room" +
                " WHERE occupied_by IS NULL" +
                " ORDER BY room_num, room_type;"
            );

            ResultSet result = query.executeQuery();
            ArrayList<Room> rooms = new ArrayList<>();
            while (result.next())
            {
                rooms.add(new Room(result.getInt("room_num"),
                                    (Integer)result.getObject("occupied_by"),
                                    result.getString("room_type")));
            }
            return rooms;
        }
        catch (SQLException error)
        {
            System.out.println(error);
            return null;
        }
    }
    public static ArrayList<Booking> getCheckInOrOutBookings(String filter)
    {
        try (Connection connection = DriverManager.getConnection(URL + DB, USERNAME, PASSWORD))
        {
            if (connection == null)
            {
                System.out.println("Connection to database failed.");
                return null;
            }

            PreparedStatement query = connection.prepareStatement(
                "SELECT booking.*" +
                " FROM booking, customer" +
                " WHERE booking.customer_id = customer.customer_id" +
                    " AND NOT status IN ('cancelled', 'checked_out')" +
                    " AND start_date <= CURDATE()" +
                    " AND (booking_id = ?" +
                        " OR first_name LIKE ?" +
                        " OR last_name LIKE ?" +
                        " OR phone LIKE ?" +
                        " OR start_date LIKE ?" +
                        " OR end_date LIKE ?" +
                        " OR book_date LIKE ?)" +
                " ORDER BY start_date, last_name ASC;"
            );
            query.setString(1, filter);
            for (int i = 2; i <= 7; i++)
                query.setString(i, "%" + filter + "%");

            ResultSet result = query.executeQuery();
            ArrayList<Booking> bookings = new ArrayList<>();
            while (result.next())
            {
                bookings.add(new Booking(
                    result.getInt("booking_id"),
                    result.getInt("customer_id"),
                    result.getString("start_date"),
                    result.getString("end_date"),
                    result.getInt("party_count"),
                    result.getString("book_date"),
                    result.getFloat("total_charge"),
                    result.getFloat("points_used"),
                    result.getString("status"),
                    (Integer)result.getObject("booked_by_employee")
                ));
            }
            return bookings;
        }
        catch (SQLException error)
        {
            System.out.println(error);
            return null;
        }
    }

    public static boolean checkBookingsOnStartup()
    {
        try (Connection connection = DriverManager.getConnection(URL + DB, USERNAME, PASSWORD))
        {
            if (connection == null)
            {
                System.out.println("Connection to database failed.");
                return false;
            }

            PreparedStatement query = connection.prepareStatement(
                "UPDATE booking" +
                " SET status = CASE" +
                    " WHEN status = 'checked_in'" +
                        " AND (CURDATE() = end_date" +
                        " AND CURTIME() > '12:30:00')" +
                        " THEN 'overstayed'" +
                    " WHEN status = 'booked' AND CURDATE() = end_date THEN 'cancelled'" +
                    " ELSE status" +
                " END," +
                " total_charge = CASE" +
                    " WHEN status = 'overstayed'" +
                        " THEN base_charge + (TIMESTAMPDIFF(HOUR, CONCAT(start_date, ' 12:00:00'), NOW()) * ?)" +
                    " WHEN status = 'booked'" +
                        " AND (CURDATE() = start_date AND CURTIME() > '12:00:00')" +
                        " THEN total_charge + ?" +
                    " ELSE total_charge" +
                " END;"
            );
            query.setFloat(1, Booking.chargePerOverstayHour());
            query.setFloat(2, Booking.chargeLateCheckIn());
            query.executeUpdate();

            System.out.println("Bookings have been updated; updated on hourly basis.");

            return true;
        }
        catch (SQLException error)
        {
            System.out.println(error);
            return false;
        }
    }

    public static int getEmployeeIdLoggedIn()
    {
        return employeeIdLoggedIn;
    }
    public static void setEmployeeIdLoggedIn(int employeeId)
    {
        employeeIdLoggedIn = employeeId;
    }

    public static void main(String args[])
    {
        if (!loadDriver())
        {
            UiUtil.createDbErrorWindow();
            return;
        }

        // Use scheduler to update bookings on hourly basis
        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.scheduleAtFixedRate(HotelSystem::checkBookingsOnStartup,
                                        Duration.between(
                                            LocalTime.now(),
                                            LocalTime.now().plusHours(1).truncatedTo(ChronoUnit.HOURS)
                                        ).toSeconds(),
                                        3600,
                                        TimeUnit.SECONDS);
        checkBookingsOnStartup();

        @SuppressWarnings("unused")
        Ui window = new Ui();
    }
}