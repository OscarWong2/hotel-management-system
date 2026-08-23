import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import javax.swing.*;

public class UiUtil
{
    private static JPanel mainPanelRef;
    private static CardLayout cardLayoutRef;

    // Method to create a database connection error window
    public static void createDbErrorWindow()
    {
        @SuppressWarnings("unused")
        DbErrorWindow dbErrorWindow = new DbErrorWindow();
    }
    // Method to create a button in the upper right corner
    public static JButton addCornerButton(String targetPanel,
                                                String displayText,
                                                Runnable optionalMethod)
    {
        JButton cornerButton = new JButton(displayText);
        applyButtonLook(cornerButton);
        cornerButton.setFont(new Font("Tahoma", Font.PLAIN, 15));
        cornerButton.addActionListener(event -> {
            if (optionalMethod != null)
                optionalMethod.run();
            if (targetPanel != null)
                cardLayoutRef.show(mainPanelRef, targetPanel);
        });

        return cornerButton;
    }
    // Method to apply a more flat look to JButtons
    public static void applyButtonLook(JButton targetButton)
    {
        targetButton.setBackground(new Color(100, 100, 155));
        targetButton.setForeground(Color.WHITE);
        targetButton.setContentAreaFilled(true);
        targetButton.setFocusPainted(false);
    }

    // Method to return today's date as string
    public static String getToday()
    {
        return LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }
    // Method to create a LocalDate from month, day, and year integers
    public static LocalDate createLocalDate(int year, int month, int day)
    {
        String monthFormatted, dayFormatted;
        if (month < 10)
            monthFormatted = "0" + month;
        else
            monthFormatted = Integer.toString(month);
        if (day < 10)
            dayFormatted = "0" + day;
        else
            dayFormatted = Integer.toString(day);

        return LocalDate.parse(year + "-" + monthFormatted + "-" + dayFormatted);
    }
    // Method to create a string date from month, day, and year integers
    public static String createStringDate(int year, int month, int day)
    {
        String monthFormatted, dayFormatted;
        if (month < 10)
            monthFormatted = "0" + month;
        else
            monthFormatted = Integer.toString(month);
        if (day < 10)
            dayFormatted = "0" + day;
        else
            dayFormatted = Integer.toString(day);

        return (year + "-" + monthFormatted + "-" + dayFormatted);
    }
    // Method to convert string date from "yyyy-mm-dd" to "mm/dd/yyyy" format
    public static String formatDate(String date)
    {
        return (LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd"))).format(DateTimeFormatter.ofPattern("M/d/uuuu"));
    }
    // Method to convert string date from "mm/dd/yyyy" to "yyyy-mm-dd" format
    public static String unformatDate(String date)
    {
        return (LocalDate.parse(date, DateTimeFormatter.ofPattern("M/d/uuuu"))).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }
    // Method to create a LocalDate from a string date in "mm/dd/yyyy" format
    public static LocalDate parseFormattedDate(String date)
    {
        return LocalDate.parse(date, DateTimeFormatter.ofPattern("M/d/uuuu"));
    }
    // Method to convert string phone to "(xxx) xxx - xxxx" format
    public static String formatPhone(String phone)
    {
        return "(" + phone.substring(0, 3) + ") " + phone.substring(3, 6) + " - " + phone.substring(6, 10);
    }
    // Method to format string to first letter upper-case
    public static String formatString(String status)
    {
        return (status.toUpperCase().charAt(0) + status.substring(1, status.length())).replace('_', ' ');
    }

    // Method to set the main panel and main CardLayout
    public static void setCardLayout(JPanel mainPanel, CardLayout cardLayout)
    {
        mainPanelRef = mainPanel;
        cardLayoutRef = cardLayout;
    }
    // Method to add screen to main CardLayout
    public static void addPanel(JPanel panel, String panelCName)
    {
        mainPanelRef.add(panel, panelCName);
    }
    // Method to remove screen from main CardLayout
    public static void removePanel(JPanel panel)
    {
        mainPanelRef.remove(panel);
    }
    // Method to switch screens
    public static void showPanel(String panel)
    {
        cardLayoutRef.show(mainPanelRef, panel);
    }
}