import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;

public class Booking
{
    private int id;
    private int customerId;
    private String startDate;
    private String endDate;
    private int partyCount;
    private String bookDate;
    private float totalCharge;
    private float pointsUsed;
    private String status;
    private Integer bookedByEmployee;

    public Booking(int newId,
                    int newCustomerId,
                    String newStartDate,
                    String newEndDate,
                    int newPartyCount,
                    String newBookDate,
                    float newTotalCharge,
                    float newPointsUsed,
                    String newStatus,
                    Integer newBookedByEmployee)
    {
        id = newId;
        customerId = newCustomerId;
        startDate = newStartDate;
        endDate = newEndDate;
        partyCount = newPartyCount;
        bookDate = newBookDate;
        totalCharge = newTotalCharge;
        pointsUsed = newPointsUsed;
        status = newStatus;
        bookedByEmployee = newBookedByEmployee;
    }

    // Accessor methods
    public int getId()
    {
        return id;
    }
    public int getCustomerId()
    {
        return customerId;
    }
    public String getStartDate()
    {
        return startDate;
    }
    public String getEndDate()
    {
        return endDate;
    }
    public int getPartyCount()
    {
        return partyCount;
    }
    public String getBookDate()
    {
        return bookDate;
    }
    public float getTotalCharge()
    {
        return totalCharge;
    }
    public float getPointsUsed()
    {
        return pointsUsed;
    }
    public String getStatus()
    {
        return status;
    }
    public Object getBookedByEmployee()
    {
        return bookedByEmployee;
    }
    // Mutator methods
    public void setId(int newId)
    {
        id = newId;
    }
    public void setCustomerId(int newCustomerId)
    {
        customerId = newCustomerId;
    }
    public void setStartDate(String newStartDate)
    {
        startDate = newStartDate;
    }
    public void setEndDate(String newEndDate)
    {
        endDate = newEndDate;
    }
    public void setPartyCount(int newPartyCount)
    {
        partyCount = newPartyCount;
    }
    public void setBookDate(String newBookDate)
    {
        bookDate = newBookDate;
    }
    public void setTotalCharge(float newTotalCharge)
    {
        totalCharge = newTotalCharge;
    }
    public void setPointsUsed(float newPointsUsed)
    {
        pointsUsed = newPointsUsed;
    }
    public void setStatus(String newStatus)
    {
        status = newStatus;
    }
    public void setBookedByEmployee(Integer newBookedByEmployee)
    {
        bookedByEmployee = newBookedByEmployee;
    }
    // Methods to check if fields are formatted correctly and valid
    public static boolean checkDateFormat(String dateCheck)
    {
        // Check if date is valid otherwise throw DateTimeParseException
        try
        {
            LocalDate.parse(
                dateCheck,
                DateTimeFormatter.ofPattern("yyyy-MM-dd").withResolverStyle(ResolverStyle.STRICT)
            );
            return true;
        }
        catch (DateTimeParseException error)
        {
            return false;
        }
    }
    public static boolean checkStartDate(String startDate, String endDate)
    {
        if (!checkDateFormat(startDate) || !checkDateFormat(endDate))
            return false;
        LocalDate start = LocalDate.parse(startDate);
        LocalDate end = LocalDate.parse(endDate);
        return (start.isBefore(end));
    }
    public static boolean checkBookDate(String bookDate, String startDate)
    {
        if (!checkDateFormat(bookDate) || !checkDateFormat(startDate))
            return false;
        LocalDate book = LocalDate.parse(bookDate);
        LocalDate start = LocalDate.parse(startDate);
        return (book.isBefore(start) || book.isEqual(start));
    }
    public static boolean checkPartyCount(int partyCountCheck)
    {
        return (partyCountCheck >= 1);
    }
    public static boolean checkTotalCharge(float chargeCheck)
    {
        return (chargeCheck >= 0);
    }
    public static boolean checkPointsUsed(float pointsUsedCheck)
    {
        return (pointsUsedCheck >= 0);
    }
    public static boolean checkStatus(String statusCheck)
    {
        return (statusCheck.equals("cancelled")
                || statusCheck.equals("booked")
                || statusCheck.equals("checked_in")
                || statusCheck.equals("checked_out")
                || statusCheck.equals("overstayed"));
    }
    // Charge per day
    public static float chargePerDay()
    {
        return 150;
    }
    // Charge per hour if overstaying
    public static float chargePerOverstayHour()
    {
        return (float)12.5;
    }
    // Charge per hour if checking in late
    public static float chargeLateCheckIn()
    {
        return 50;
    }
}