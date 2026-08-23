import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import javax.swing.*;

public class ScreenAddBooking
{
    // CNAME = CardLayout Names
    public static final String CNAME_ADD_BOOKING_1 = "addBooking1";
    public static final String CNAME_ADD_BOOKING_2 = "addBooking2";


    private Integer customerId; // Integer since ID can be null if new customer
    private String customerFirstName;
    private String customerLastName;
    private String customerPhone;
    private String customerEmail;
    private int customerPoints;

    private String startDate;
    private String endDate;
    private int partyCount;
    private float totalCharge;


    private final JPanel ADD_BOOKING_1;
    private final JPanel FILL_OUT_INFO_1;
    private final JPanel ADD_BOOKING_TOP_HEADER_1;
    private final JPanel ADD_BOOKING_BOTTOM_HEADER_1;

    private final JButton CHOOSE_START_DATE;
    private final JLabel CHOOSE_START_DATE_HEADER;
    private final JLabel INVALID_START_DATE;

    private final JButton CHOOSE_END_DATE;
    private final JLabel CHOOSE_END_DATE_HEADER;
    private final JLabel INVALID_END_DATE;

    private final JTextField ENTER_PARTY_COUNT;
    private final JLabel ENTER_PARTY_COUNT_HEADER;
    private final JLabel INVALID_PARTY_COUNT;


    private final JPanel ADD_BOOKING_2;
    private final JPanel FILL_OUT_INFO_2;
    private final JPanel ADD_BOOKING_TOP_HEADER_2;
    private final JPanel ADD_BOOKING_BOTTOM_HEADER_2;

    private final JTextField ENTER_FIRST_NAME;
    private final JLabel ENTER_FIRST_NAME_HEADER;
    private final JLabel INVALID_FIRST_NAME;

    private final JTextField ENTER_LAST_NAME;
    private final JLabel ENTER_LAST_NAME_HEADER;
    private final JLabel INVALID_LAST_NAME;

    private final JTextField ENTER_PHONE;
    private final JLabel ENTER_PHONE_HEADER;
    private final JLabel INVALID_PHONE;

    private final JTextField ENTER_EMAIL;
    private final JLabel ENTER_EMAIL_HEADER;
    private final JLabel INVALID_EMAIL;

    private class addBookingListener implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent event)
        {
            if (event.getSource() == CHOOSE_START_DATE)
            {
                @SuppressWarnings("unused")
                ScreenUtilCalendar calendar = new ScreenUtilCalendar(LocalDate.now(),
                                                                        null,
                                                                        CHOOSE_START_DATE,
                                                                        CNAME_ADD_BOOKING_1);
            }
            else if (event.getSource() == CHOOSE_END_DATE)
            {
                LocalDate date;
                if (!CHOOSE_START_DATE.getText().equals("Choose date"))
                    date = UiUtil.parseFormattedDate(CHOOSE_START_DATE.getText()).plusDays(1);
                else
                    date = LocalDate.now().plusDays(1);
                @SuppressWarnings("unused")
                ScreenUtilCalendar calendar = new ScreenUtilCalendar(date,
                                                                        1,
                                                                        CHOOSE_END_DATE,
                                                                        CNAME_ADD_BOOKING_1);
            }
            else if (event.getSource() == ENTER_PARTY_COUNT)
            {
                if (ENTER_PARTY_COUNT.getText().isEmpty()
                    || !Booking.checkPartyCount(Integer.parseInt(ENTER_PARTY_COUNT.getText())))
                    INVALID_PARTY_COUNT.setVisible(true);
                else
                    INVALID_PARTY_COUNT.setVisible(false);
            }
            else if (event.getSource() == ENTER_FIRST_NAME)
            {
                if (!Customer.checkName(ENTER_FIRST_NAME.getText()))
                    INVALID_FIRST_NAME.setVisible(true);
                else
                    INVALID_FIRST_NAME.setVisible(false);
            }
            else if (event.getSource() == ENTER_LAST_NAME)
            {
                if (!Customer.checkName(ENTER_LAST_NAME.getText()))
                    INVALID_LAST_NAME.setVisible(true);
                else
                    INVALID_LAST_NAME.setVisible(false);
            }
            else if (event.getSource() == ENTER_PHONE)
            {
                if (!Customer.checkPhone(ENTER_PHONE.getText()))
                    INVALID_PHONE.setVisible(true);
                else
                    INVALID_PHONE.setVisible(false);
            }
            else if (event.getSource() == ENTER_EMAIL)
            {
                if (!Customer.checkEmail(ENTER_EMAIL.getText()))
                    INVALID_EMAIL.setVisible(true);
                else
                    INVALID_EMAIL.setVisible(false);
            }
        }
    }

    public ScreenAddBooking()
    {
        customerId = null;
        customerFirstName = "";
        customerLastName = "";
        customerPhone = "";
        customerEmail = "";

        startDate = "";
        endDate = "";
        partyCount = 0;
        totalCharge = 0;


        ADD_BOOKING_1 = new JPanel(new BorderLayout());
        FILL_OUT_INFO_1 = new JPanel(new GridBagLayout());
        ADD_BOOKING_TOP_HEADER_1 = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        ADD_BOOKING_BOTTOM_HEADER_1 = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        CHOOSE_START_DATE = new JButton("Choose date");
        CHOOSE_START_DATE_HEADER = new JLabel("Check in date");
        INVALID_START_DATE = new JLabel("Please select a valid date.");

        CHOOSE_END_DATE = new JButton("Choose date");
        CHOOSE_END_DATE_HEADER = new JLabel("Check out date");
        INVALID_END_DATE = new JLabel("Please select a valid date.");

        ENTER_PARTY_COUNT = new JTextField(5);
        ENTER_PARTY_COUNT_HEADER = new JLabel("Party count");
        INVALID_PARTY_COUNT = new JLabel("Invalid number.");


        ADD_BOOKING_2 = new JPanel(new BorderLayout());
        FILL_OUT_INFO_2 = new JPanel(new GridBagLayout());
        ADD_BOOKING_TOP_HEADER_2 = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        ADD_BOOKING_BOTTOM_HEADER_2 = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        ENTER_FIRST_NAME = new JTextField(15);
        ENTER_FIRST_NAME_HEADER = new JLabel("First name");
        INVALID_FIRST_NAME = new JLabel("Invalid name.");

        ENTER_LAST_NAME = new JTextField(15);
        ENTER_LAST_NAME_HEADER = new JLabel("Last name");
        INVALID_LAST_NAME = new JLabel("Invalid name.");

        ENTER_PHONE = new JTextField(15);
        ENTER_PHONE_HEADER = new JLabel("Phone number");
        INVALID_PHONE = new JLabel("Invalid phone number.");

        ENTER_EMAIL = new JTextField(15);
        ENTER_EMAIL_HEADER = new JLabel("E-Mail");
        INVALID_EMAIL = new JLabel("Invalid e-mail.");
        
        // Initialize layout

        GridBagConstraints layout = new GridBagConstraints();
        layout.insets = new Insets(15, 15, 15, 15);
        layout.fill = GridBagConstraints.HORIZONTAL;

        // ADD_BOOKING_TOP_HEADER_1
        ADD_BOOKING_TOP_HEADER_1.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        ADD_BOOKING_TOP_HEADER_1.add(UiUtil.addCornerButton(ScreenMenu.CNAME_MENU, "Back", this::reset));

        // CHOOSE_START_DATE
        layout.gridx = 1;
        layout.gridy = 0;
        UiUtil.applyButtonLook(CHOOSE_START_DATE);
        CHOOSE_START_DATE.setFont(new Font("Tahoma", Font.PLAIN, 30));
        CHOOSE_START_DATE.setHorizontalAlignment(SwingConstants.CENTER);
        CHOOSE_START_DATE.addActionListener(new addBookingListener());
        FILL_OUT_INFO_1.add(CHOOSE_START_DATE, layout);
        // CHOOSE_START_DATE_HEADER
        layout.gridx = 0;
        layout.gridy = 0;
        CHOOSE_START_DATE_HEADER.setFont(new Font("Tahoma", Font.PLAIN, 30));
        FILL_OUT_INFO_1.add(CHOOSE_START_DATE_HEADER, layout);
        // INVALID_START_DATE
        layout.gridx = 2;
        layout.gridy = 0;
        INVALID_START_DATE.setFont(new Font("Tahoma", Font.PLAIN, 30));
        INVALID_START_DATE.setForeground(new Color(255, 100, 100));
        INVALID_START_DATE.setVisible(false);
        FILL_OUT_INFO_1.add(INVALID_START_DATE, layout);

        // CHOOSE_END_DATE
        layout.gridx = 1;
        layout.gridy = 1;
        UiUtil.applyButtonLook(CHOOSE_END_DATE);
        CHOOSE_END_DATE.setFont(new Font("Tahoma", Font.PLAIN, 30));
        CHOOSE_END_DATE.setHorizontalAlignment(SwingConstants.CENTER);
        CHOOSE_END_DATE.addActionListener(new addBookingListener());
        FILL_OUT_INFO_1.add(CHOOSE_END_DATE, layout);
        // CHOOSE_END_DATE_HEADER
        layout.gridx = 0;
        layout.gridy = 1;
        CHOOSE_END_DATE_HEADER.setFont(new Font("Tahoma", Font.PLAIN, 30));
        FILL_OUT_INFO_1.add(CHOOSE_END_DATE_HEADER, layout);
        // INVALID_END_DATE
        layout.gridx = 2;
        layout.gridy = 1;
        INVALID_END_DATE.setFont(new Font("Tahoma", Font.PLAIN, 30));
        INVALID_END_DATE.setForeground(new Color(255, 100, 100));
        INVALID_END_DATE.setVisible(false);
        FILL_OUT_INFO_1.add(INVALID_END_DATE, layout);

        // ENTER_PARTY_COUNT
        layout.gridx = 1;
        layout.gridy = 2;
        ENTER_PARTY_COUNT.setFont(new Font("Tahoma", Font.PLAIN, 30));
        ENTER_PARTY_COUNT.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        ENTER_PARTY_COUNT.setHorizontalAlignment(SwingConstants.LEFT);
        ENTER_PARTY_COUNT.addActionListener(new addBookingListener());
        FILL_OUT_INFO_1.add(ENTER_PARTY_COUNT, layout);
        // ENTER_PARTY_COUNT_HEADER
        layout.gridx = 0;
        layout.gridy = 2;
        ENTER_PARTY_COUNT_HEADER.setFont(new Font("Tahoma", Font.PLAIN, 30));
        FILL_OUT_INFO_1.add(ENTER_PARTY_COUNT_HEADER, layout);
        // INVALID_PARTY_COUNT
        layout.gridx = 2;
        layout.gridy = 2;
        INVALID_PARTY_COUNT.setFont(new Font("Tahoma", Font.PLAIN, 30));
        INVALID_PARTY_COUNT.setForeground(new Color(255, 100, 100));
        INVALID_PARTY_COUNT.setVisible(false);
        FILL_OUT_INFO_1.add(INVALID_PARTY_COUNT, layout);

        // ADD_BOOKING_BOTTOM_HEADER_1
        ADD_BOOKING_BOTTOM_HEADER_1.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        ADD_BOOKING_BOTTOM_HEADER_1.add(UiUtil.addCornerButton(null, "Next", this::checkAddBooking1Info));

        // ADD_BOOKING_1
        ADD_BOOKING_1.add(ADD_BOOKING_TOP_HEADER_1, BorderLayout.NORTH);
        ADD_BOOKING_1.add(ADD_BOOKING_BOTTOM_HEADER_1, BorderLayout.SOUTH);
        ADD_BOOKING_1.add(FILL_OUT_INFO_1, BorderLayout.CENTER);


        // ADD_BOOKING_TOP_HEADER_2
        ADD_BOOKING_TOP_HEADER_2.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        ADD_BOOKING_TOP_HEADER_2.add(UiUtil.addCornerButton(CNAME_ADD_BOOKING_1, "Back", null));

        // ENTER_FIRST_NAME
        layout.gridx = 1;
        layout.gridy = 0;
        ENTER_FIRST_NAME.setFont(new Font("Tahoma", Font.PLAIN, 30));
        ENTER_FIRST_NAME.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        ENTER_FIRST_NAME.setHorizontalAlignment(SwingConstants.LEFT);
        ENTER_FIRST_NAME.addActionListener(new addBookingListener());
        FILL_OUT_INFO_2.add(ENTER_FIRST_NAME, layout);
        // ENTER_FIRST_NAME_HEADER
        layout.gridx = 0;
        layout.gridy = 0;
        ENTER_FIRST_NAME_HEADER.setFont(new Font("Tahoma", Font.PLAIN, 30));
        FILL_OUT_INFO_2.add(ENTER_FIRST_NAME_HEADER, layout);
        // INVALID_FIRST_NAME
        layout.gridx = 2;
        layout.gridy = 0;
        INVALID_FIRST_NAME.setFont(new Font("Tahoma", Font.PLAIN, 30));
        INVALID_FIRST_NAME.setForeground(new Color(255, 100, 100));
        INVALID_FIRST_NAME.setVisible(false);
        FILL_OUT_INFO_2.add(INVALID_FIRST_NAME, layout);

        // ENTER_LAST_NAME
        layout.gridx = 1;
        layout.gridy = 1;
        ENTER_LAST_NAME.setFont(new Font("Tahoma", Font.PLAIN, 30));
        ENTER_LAST_NAME.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        ENTER_LAST_NAME.setHorizontalAlignment(SwingConstants.LEFT);
        ENTER_LAST_NAME.addActionListener(new addBookingListener());
        FILL_OUT_INFO_2.add(ENTER_LAST_NAME, layout);
        // ENTER_LAST_NAME_HEADER
        layout.gridx = 0;
        layout.gridy = 1;
        ENTER_LAST_NAME_HEADER.setFont(new Font("Tahoma", Font.PLAIN, 30));
        FILL_OUT_INFO_2.add(ENTER_LAST_NAME_HEADER, layout);
        // INVALID_LAST_NAME
        layout.gridx = 2;
        layout.gridy = 1;
        INVALID_LAST_NAME.setFont(new Font("Tahoma", Font.PLAIN, 30));
        INVALID_LAST_NAME.setForeground(new Color(255, 100, 100));
        INVALID_LAST_NAME.setVisible(false);
        FILL_OUT_INFO_2.add(INVALID_LAST_NAME, layout);

        // ENTER_PHONE
        layout.gridx = 1;
        layout.gridy = 2;
        ENTER_PHONE.setFont(new Font("Tahoma", Font.PLAIN, 30));
        ENTER_PHONE.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        ENTER_PHONE.setHorizontalAlignment(SwingConstants.LEFT);
        ENTER_PHONE.addActionListener(new addBookingListener());
        FILL_OUT_INFO_2.add(ENTER_PHONE, layout);
        // ENTER_PHONE_HEADER
        layout.gridx = 0;
        layout.gridy = 2;
        ENTER_PHONE_HEADER.setFont(new Font("Tahoma", Font.PLAIN, 30));
        FILL_OUT_INFO_2.add(ENTER_PHONE_HEADER, layout);
        // INVALID_PHONE
        layout.gridx = 2;
        layout.gridy = 2;
        INVALID_PHONE.setFont(new Font("Tahoma", Font.PLAIN, 30));
        INVALID_PHONE.setForeground(new Color(255, 100, 100));
        INVALID_PHONE.setVisible(false);
        FILL_OUT_INFO_2.add(INVALID_PHONE, layout);

        // ENTER_EMAIL
        layout.gridx = 1;
        layout.gridy = 3;
        ENTER_EMAIL.setFont(new Font("Tahoma", Font.PLAIN, 30));
        ENTER_EMAIL.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        ENTER_EMAIL.setHorizontalAlignment(SwingConstants.LEFT);
        ENTER_EMAIL.addActionListener(new addBookingListener());
        FILL_OUT_INFO_2.add(ENTER_EMAIL, layout);
        // ENTER_EMAIL_HEADER
        layout.gridx = 0;
        layout.gridy = 3;
        ENTER_EMAIL_HEADER.setFont(new Font("Tahoma", Font.PLAIN, 30));
        FILL_OUT_INFO_2.add(ENTER_EMAIL_HEADER, layout);
        // INVALID_EMAIL
        layout.gridx = 2;
        layout.gridy = 3;
        INVALID_EMAIL.setFont(new Font("Tahoma", Font.PLAIN, 30));
        INVALID_EMAIL.setForeground(new Color(255, 100, 100));
        INVALID_EMAIL.setVisible(false);
        FILL_OUT_INFO_2.add(INVALID_EMAIL, layout);

        // ADD_BOOKING_BOTTOM_HEADER_2
        ADD_BOOKING_BOTTOM_HEADER_2.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        ADD_BOOKING_BOTTOM_HEADER_2.add(UiUtil.addCornerButton(null, "Next", this::checkAddBooking2Info));

        // ADD_BOOKING_2
        ADD_BOOKING_2.add(ADD_BOOKING_TOP_HEADER_2, BorderLayout.NORTH);
        ADD_BOOKING_2.add(ADD_BOOKING_BOTTOM_HEADER_2, BorderLayout.SOUTH);
        ADD_BOOKING_2.add(FILL_OUT_INFO_2, BorderLayout.CENTER);
    }

    public void checkAddBooking1Info()
    {
        LocalDate chosenStartDate = UiUtil.parseFormattedDate(CHOOSE_START_DATE.getText());
        LocalDate chosenEndDate = UiUtil.parseFormattedDate(CHOOSE_END_DATE.getText());
        if (CHOOSE_START_DATE.getText().equals("Choose date")
            || chosenStartDate.isAfter(chosenEndDate)
            || chosenStartDate.isEqual(chosenEndDate))
            INVALID_START_DATE.setVisible(true);
        else
            INVALID_START_DATE.setVisible(false);
        if (CHOOSE_END_DATE.getText().equals("Choose date")
            || chosenEndDate.isBefore(chosenStartDate)
            || chosenEndDate.isEqual(chosenStartDate))
            INVALID_END_DATE.setVisible(true);
        else
            INVALID_END_DATE.setVisible(false);
        if (ENTER_PARTY_COUNT.getText().isEmpty()
            || !Booking.checkPartyCount(Integer.parseInt(ENTER_PARTY_COUNT.getText())))
            INVALID_PARTY_COUNT.setVisible(true);
        else
            INVALID_PARTY_COUNT.setVisible(false);

        if (INVALID_START_DATE.isVisible()
            || INVALID_END_DATE.isVisible()
            || INVALID_PARTY_COUNT.isVisible())
            return;

        // Set startDate, endDate, partyCount
        startDate = UiUtil.unformatDate(CHOOSE_START_DATE.getText());
        endDate = UiUtil.unformatDate(CHOOSE_END_DATE.getText());
        partyCount = Integer.parseInt(ENTER_PARTY_COUNT.getText());

        UiUtil.showPanel(CNAME_ADD_BOOKING_2);
    }
    public void checkAddBooking2Info()
    {
        if (!Customer.checkName(ENTER_FIRST_NAME.getText()))
            INVALID_FIRST_NAME.setVisible(true);
        else
            INVALID_FIRST_NAME.setVisible(false);
        if (!Customer.checkName(ENTER_LAST_NAME.getText()))
            INVALID_LAST_NAME.setVisible(true);
        else
            INVALID_LAST_NAME.setVisible(false);
        if (!Customer.checkPhone(ENTER_PHONE.getText()))
            INVALID_PHONE.setVisible(true);
        else
            INVALID_PHONE.setVisible(false);
        if (!Customer.checkEmail(ENTER_EMAIL.getText()))
            INVALID_EMAIL.setVisible(true);
        else
            INVALID_EMAIL.setVisible(false);

        if (INVALID_FIRST_NAME.isVisible()
            || INVALID_LAST_NAME.isVisible()
            || INVALID_PHONE.isVisible()
            || INVALID_EMAIL.isVisible())
            return;

        // Set customerFirstName, customerLastName, customerPhone, customerEmail
        customerFirstName = ENTER_FIRST_NAME.getText().toUpperCase();
        customerLastName = ENTER_LAST_NAME.getText().toUpperCase();
        customerPhone = ENTER_PHONE.getText();
        customerEmail = ENTER_EMAIL.getText();

        // IF DB CONNECTION DNE, STAY ON 'ADD_BOOKING_2' SCREEN
        ArrayList<Object> customerFromDb = HotelSystem.filterRecords("customer", customerPhone);
        if (customerFromDb == null)
        {
            UiUtil.createDbErrorWindow();
            return;
        }

        if (!customerFromDb.isEmpty())
        {
            // Set customerId, customerPoints if existing customer
            Customer existingCustomer = (Customer)customerFromDb.getFirst();
            customerId = existingCustomer.getId();
            customerPoints = existingCustomer.getPoints();

            initCustomerInfo();
        }
        else
            initReviewInfo();
    }
    public void initCustomerInfo()
    {
        ArrayList<Map.Entry<String, String>> info = new ArrayList<>();
        info.add(Map.entry("Name", customerFirstName + " " + customerLastName));
        info.add(Map.entry("Phone Number", UiUtil.formatPhone(customerPhone)));
        info.add(Map.entry("E-Mail", customerEmail));
        info.add(Map.entry("Points", Integer.toString(customerPoints)));
        info.add(Map.entry("Customer ID", Integer.toString(customerId)));

        @SuppressWarnings("unused")
        ScreenUtilInfo infoScreen = new ScreenUtilInfo(info,
                                                        "Verify Existing Customer",
                                                        CNAME_ADD_BOOKING_2,
                                                        new ArrayList<>(List.of(UiUtil.addCornerButton(null, "Next", this::initReviewInfo))));
    }
    public void initReviewInfo()
    {
        // Set totalCharge
        totalCharge = ChronoUnit.DAYS.between(LocalDate.parse(startDate), LocalDate.parse(endDate)) * Booking.chargePerDay();

        ArrayList<Map.Entry<String, String>> info = new ArrayList<>();
        info.add(Map.entry("Check in Date", UiUtil.formatDate(startDate)));
        info.add(Map.entry("Check Out Date", UiUtil.formatDate(endDate)));
        info.add(Map.entry("Party Count", Integer.toString(partyCount)));
        info.add(Map.entry("Name", customerFirstName + " " + customerLastName));
        info.add(Map.entry("Phone Number", UiUtil.formatPhone(customerPhone)));
        info.add(Map.entry("E-Mail", customerEmail));
        info.add(Map.entry("Base Charge", "$" + String.format("%.2f", totalCharge)));

        @SuppressWarnings("unused")
        ScreenUtilInfo infoScreen = new ScreenUtilInfo(info,
                                                        "Verify Booking",
                                                        CNAME_ADD_BOOKING_2,
                                                        new ArrayList<>(List.of(UiUtil.addCornerButton(null, "Add Booking", this::addNewDbBooking))));
    }
    public void addNewDbBooking()
    {
        Runnable proceedAddNewDbBooking = () ->
        {
            if (customerId == null)
            {
                Customer newCustomer = new Customer(
                    -1,
                    customerFirstName,
                    customerLastName,
                    customerPhone,
                    customerEmail,
                    0
                );

                // IF DB CONNECTION DNE, DON'T ADD BOOKING AND GO BACK TO 'ADD_BOOKING_2' SCREEN
                if (!HotelSystem.addCustomer(newCustomer))
                {
                    UiUtil.createDbErrorWindow();
                    return;
                }
                else
                {
                    // IF DB CONNECTION DNE, DON'T ADD BOOKING AND GO BACK TO 'ADD_BOOKING_2' SCREEN
                    ArrayList<Object> customerFromDb = HotelSystem.filterRecords("customer", customerPhone);
                    if (customerFromDb == null)
                    {
                        UiUtil.createDbErrorWindow();
                        return;
                    }
                    // Set customerId, customerPoints after creating record in database if new customer
                    customerId = ((Customer)customerFromDb.getFirst()).getId();
                    customerPoints = 0;
                    UiUtil.showPanel(ScreenMenu.CNAME_MENU);
                }
            }

            Booking newBooking = new Booking(
                -1,
                customerId,
                startDate,
                endDate,
                partyCount,
                UiUtil.getToday(),
                totalCharge,
                0,
                "booked",
                HotelSystem.getEmployeeIdLoggedIn()
            );

            // IF DB CONNECTION DNE, DON'T ADD BOOKING AND GO BACK TO 'ADD_BOOKING_2' SCREEN
            if (!HotelSystem.addBooking(newBooking))
                UiUtil.createDbErrorWindow();
            else
            {
                reset();
                UiUtil.showPanel(ScreenMenu.CNAME_MENU);
            }
        };

        @SuppressWarnings("unused")
        ScreenUtilConfirm confirmScreen = new ScreenUtilConfirm(proceedAddNewDbBooking,
                                                                CNAME_ADD_BOOKING_2,
                                                                "Confirm ADD BOOKING?",
                                                                true);
    }
    public void reset()
    {
        customerId = null;
        customerFirstName = "";
        customerLastName = "";
        customerPhone = "";
        customerEmail = "";

        startDate = "";
        endDate = "";
        partyCount = 0;
        totalCharge = 0;

        CHOOSE_START_DATE.setText("Choose date");
        CHOOSE_END_DATE.setText("Choose date");
        ENTER_PARTY_COUNT.setText("");
        ENTER_FIRST_NAME.setText("");
        ENTER_LAST_NAME.setText("");
        ENTER_PHONE.setText("");
        ENTER_EMAIL.setText("");

        INVALID_START_DATE.setVisible(false);
        INVALID_END_DATE.setVisible(false);
        INVALID_PARTY_COUNT.setVisible(false);
        INVALID_FIRST_NAME.setVisible(false);
        INVALID_LAST_NAME.setVisible(false);
        INVALID_PHONE.setVisible(false);
        INVALID_EMAIL.setVisible(false);
    }
    public JPanel getScreen(int screen)
    {
        if (screen == 1)
            return ADD_BOOKING_1;
        return ADD_BOOKING_2;
    }
}