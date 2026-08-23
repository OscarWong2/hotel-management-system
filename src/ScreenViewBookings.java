import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import javax.swing.*;

public class ScreenViewBookings
{
    // CNAME = CardLayout Names
    public static final String CNAME_VIEW_BOOKINGS = "viewBookings";
    public static final String CNAME_RESCHEDULE = "reschedule";

    private ScreenCheckInOrOut checkInOrOutRef;

    private Booking selectedBooking;
    private String selectedStartDate;
    private String selectedEndDate;


    private final ScreenUtilList VIEW_BOOKINGS;
    private final JPanel VIEW_BOOKINGS_LIST_PANEL;
    private final JTextField VIEW_BOOKINGS_SEARCH_BAR;


    private final JButton CHECK_IN_OR_OUT_BUTTON;
    private final JButton RESCHEDULE_BUTTON;
    private final JButton CANCEL_BUTTON;


    private final JPanel RESCHEDULE;
    private final JPanel RESCHEDULE_PANEL;
    private final JPanel RESCHEDULE_TOP_HEADER;
    private final JPanel RESCHEDULE_BOTTOM_HEADER;

    private final JButton CHOOSE_START_DATE;
    private final JLabel CHOOSE_START_DATE_HEADER;
    private final JLabel INVALID_START_DATE;

    private final JButton CHOOSE_END_DATE;
    private final JLabel CHOOSE_END_DATE_HEADER;
    private final JLabel INVALID_END_DATE;

    private class bookingListButtonListener implements ActionListener
    {
        private final Booking BUTTON_BOOKING;

        public bookingListButtonListener(Booking newBooking)
        {
            BUTTON_BOOKING = newBooking;
        }
        @Override
        public void actionPerformed(ActionEvent event)
        {
            selectedBooking = BUTTON_BOOKING;
            selectedStartDate = selectedBooking.getStartDate();
            selectedEndDate = selectedBooking.getEndDate();
            String status = selectedBooking.getStatus();

            if (!status.equals("cancelled")
                && !status.equals("checked_out"))
            {
                if (LocalDate.parse(selectedStartDate).isAfter(LocalDate.now()))
                {
                    CHECK_IN_OR_OUT_BUTTON.setVisible(false);
                    RESCHEDULE_BUTTON.setVisible(true);
                    CANCEL_BUTTON.setVisible(true);
                }
                else
                {
                    CHECK_IN_OR_OUT_BUTTON.setVisible(true);
                    RESCHEDULE_BUTTON.setVisible(false);
                    CANCEL_BUTTON.setVisible(false);
                }
            }
            else
            {
                CHECK_IN_OR_OUT_BUTTON.setVisible(false);
                RESCHEDULE_BUTTON.setVisible(false);
                CANCEL_BUTTON.setVisible(false);
            }

            initBookingInfo();
        }
    }
    private class rescheduleListener implements ActionListener
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
                                                                        CNAME_RESCHEDULE);
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
                                                                        CNAME_RESCHEDULE);
            }
        }
    }

    public ScreenViewBookings()
    {
        selectedBooking = null;
        selectedStartDate = "";
        selectedEndDate = "";


        CHECK_IN_OR_OUT_BUTTON = UiUtil.addCornerButton(null, "Check In/Out", this::checkInOrOut);
        RESCHEDULE_BUTTON = UiUtil.addCornerButton(null, "Reschedule", this::reschedule);
        CANCEL_BUTTON = UiUtil.addCornerButton(null, "Cancel", this::cancel);


        VIEW_BOOKINGS = new ScreenUtilList(ScreenMenu.CNAME_MENU,
                                            this::reset,
                                            this::initBookingList,
                                            true,
                                            new ArrayList<>());
        VIEW_BOOKINGS_LIST_PANEL = VIEW_BOOKINGS.getListPanel();
        VIEW_BOOKINGS_SEARCH_BAR = VIEW_BOOKINGS.getSearchBar();


        RESCHEDULE = new JPanel(new BorderLayout());
        RESCHEDULE_PANEL = new JPanel(new GridBagLayout());
        RESCHEDULE_TOP_HEADER = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        RESCHEDULE_BOTTOM_HEADER = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        CHOOSE_START_DATE = new JButton();
        CHOOSE_START_DATE_HEADER = new JLabel("Check in date");
        INVALID_START_DATE = new JLabel("Please select a valid date.");

        CHOOSE_END_DATE = new JButton();
        CHOOSE_END_DATE_HEADER = new JLabel("Check out date");
        INVALID_END_DATE = new JLabel("Please select a valid date.");

        // Initialize layout

        GridBagConstraints layout = new GridBagConstraints();
        layout.insets = new Insets(10, 0, 10, 0);

        // CHECK_IN_OR_OUT_BUTTON
        CHECK_IN_OR_OUT_BUTTON.setVisible(false);
        // RESCHEDULE_BUTTON
        RESCHEDULE_BUTTON.setVisible(false);
        // CANCEL_BUTTON
        CANCEL_BUTTON.setVisible(false);


        layout.insets = new Insets(15, 15, 15, 15);
        layout.fill = GridBagConstraints.HORIZONTAL;


        // RESCHEDULE_TOP_HEADER
        RESCHEDULE_TOP_HEADER.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        RESCHEDULE_TOP_HEADER.add(UiUtil.addCornerButton(CNAME_VIEW_BOOKINGS, "Back", null));

        // CHOOSE_START_DATE
        layout.gridx = 1;
        layout.gridy = 0;
        UiUtil.applyButtonLook(CHOOSE_START_DATE);
        CHOOSE_START_DATE.setFont(new Font("Tahoma", Font.PLAIN, 30));
        CHOOSE_START_DATE.setHorizontalAlignment(SwingConstants.CENTER);
        CHOOSE_START_DATE.addActionListener(new rescheduleListener());
        RESCHEDULE_PANEL.add(CHOOSE_START_DATE, layout);
        // CHOOSE_START_DATE_HEADER
        layout.gridx = 0;
        layout.gridy = 0;
        CHOOSE_START_DATE_HEADER.setFont(new Font("Tahoma", Font.PLAIN, 30));
        RESCHEDULE_PANEL.add(CHOOSE_START_DATE_HEADER, layout);
        // INVALID_START_DATE
        layout.gridx = 2;
        layout.gridy = 0;
        INVALID_START_DATE.setFont(new Font("Tahoma", Font.PLAIN, 30));
        INVALID_START_DATE.setForeground(new Color(255, 100, 100));
        INVALID_START_DATE.setVisible(false);
        RESCHEDULE_PANEL.add(INVALID_START_DATE, layout);

        // CHOOSE_END_DATE
        layout.gridx = 1;
        layout.gridy = 1;
        UiUtil.applyButtonLook(CHOOSE_END_DATE);
        CHOOSE_END_DATE.setFont(new Font("Tahoma", Font.PLAIN, 30));
        CHOOSE_END_DATE.setHorizontalAlignment(SwingConstants.CENTER);
        CHOOSE_END_DATE.addActionListener(new rescheduleListener());
        RESCHEDULE_PANEL.add(CHOOSE_END_DATE, layout);
        // CHOOSE_END_DATE_HEADER
        layout.gridx = 0;
        layout.gridy = 1;
        CHOOSE_END_DATE_HEADER.setFont(new Font("Tahoma", Font.PLAIN, 30));
        RESCHEDULE_PANEL.add(CHOOSE_END_DATE_HEADER, layout);
        // INVALID_END_DATE
        layout.gridx = 2;
        layout.gridy = 1;
        INVALID_END_DATE.setFont(new Font("Tahoma", Font.PLAIN, 30));
        INVALID_END_DATE.setForeground(new Color(255, 100, 100));
        INVALID_END_DATE.setVisible(false);
        RESCHEDULE_PANEL.add(INVALID_END_DATE, layout);

        // RESCHEDULE_BOTTOM_HEADER
        RESCHEDULE_BOTTOM_HEADER.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        RESCHEDULE_BOTTOM_HEADER.add(UiUtil.addCornerButton(null, "Next", this::checkRescheduleInfo));

        // RESCHEDULE
        RESCHEDULE.add(RESCHEDULE_TOP_HEADER, BorderLayout.NORTH);
        RESCHEDULE.add(RESCHEDULE_BOTTOM_HEADER, BorderLayout.SOUTH);
        RESCHEDULE.add(RESCHEDULE_PANEL, BorderLayout.CENTER);
    }

    public void checkInOrOut()
    {
        // IF DB CONNECTION DNE, DON'T PROCEED TO CHECK IN/OUT AND STAY ON BOOKING LIST SCREEN
        Booking bookingFromDb = (Booking)HotelSystem.getFromId("booking", selectedBooking.getId());
        if (bookingFromDb == null)
            return;
        Customer customerFromDb = (Customer)HotelSystem.getFromId("customer", bookingFromDb.getCustomerId());
        if (customerFromDb == null)
            return;
        checkInOrOutRef.checkInOrOut(bookingFromDb, customerFromDb.getPoints(), true);
    }
    public void reschedule()
    {
        CHOOSE_START_DATE.setText(UiUtil.formatDate(selectedStartDate));
        CHOOSE_END_DATE.setText(UiUtil.formatDate(selectedEndDate));
        UiUtil.showPanel(CNAME_RESCHEDULE);
    }
    public void cancel()
    {
        Runnable proceedCancel = () ->
        {
            // IF DB CONNECTION DNE, DON'T CANCEL BOOKING AND GO BACK TO BOOKING LIST SCREEN
            if (HotelSystem.updateBookingStatus(selectedBooking.getId(), true, null))
            {
                initBookingInfo();
                UiUtil.showPanel(CNAME_VIEW_BOOKINGS);
            }
        };

        @SuppressWarnings("unused")
        ScreenUtilConfirm confirmScreen = new ScreenUtilConfirm(proceedCancel,
                                                                CNAME_VIEW_BOOKINGS,
                                                                "Confirm CANCEL BOOKING?",
                                                                true);
    }
    public void checkRescheduleInfo()
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

        if (INVALID_START_DATE.isVisible()
            || INVALID_END_DATE.isVisible())
            return;

        selectedStartDate = UiUtil.unformatDate(CHOOSE_START_DATE.getText());
        selectedEndDate = UiUtil.unformatDate(CHOOSE_END_DATE.getText());
        initRescheduleInfo(selectedStartDate, selectedEndDate);
    }
    public void confirmReschedule()
    {
        Runnable proceedReschedule = () ->
        {
            int id = selectedBooking.getId();
            float newTotalCharge = ChronoUnit.DAYS.between(
                                        LocalDate.parse(selectedStartDate),
                                        LocalDate.parse(selectedEndDate)
                                    ) * Booking.chargePerDay();
            // IF DB CONNECTION DNE, DON'T RESCHEDULE AND GO BACK TO 'RESCHEDULE' SCREEN
            if (!HotelSystem.updateBookingDates(id,
                                                selectedStartDate,
                                                selectedEndDate,
                                                newTotalCharge))
            {
                UiUtil.createDbErrorWindow();
                return;
            }
            UiUtil.showPanel(CNAME_VIEW_BOOKINGS);
        };

        @SuppressWarnings("unused")
        ScreenUtilConfirm confirmScreen = new ScreenUtilConfirm(proceedReschedule,
                                                                CNAME_RESCHEDULE,
                                                                "Confirm RESCHEDULE BOOKING?",
                                                                true);
    }
    public void initBookingList()
    {
        // IF DB CONNECTION DNE, DON'T INITIALIZE BOOKING LIST
        ArrayList<Object> records = HotelSystem.filterRecords("booking", VIEW_BOOKINGS_SEARCH_BAR.getText());
        if (records == null)
        {
            UiUtil.createDbErrorWindow();
            return;
        }

        VIEW_BOOKINGS_LIST_PANEL.removeAll();
        for (Object o : records)
        {
            // IF DB CONNECTION DNE, STOP INITIALIZING BOOKING LIST
            Booking record = (Booking)o;
            Object idResultFromDb = HotelSystem.getFromId("customer", record.getCustomerId());
            if (idResultFromDb instanceof Integer errorCode && errorCode == -1)
            {
                UiUtil.createDbErrorWindow();
                VIEW_BOOKINGS_LIST_PANEL.revalidate();
                VIEW_BOOKINGS_LIST_PANEL.repaint();
                return;
            }
            Customer bookingCustomer = (Customer)idResultFromDb;

            int bookingId = record.getId();
            String startDate = record.getStartDate();
            String endDate = record.getEndDate();
            int partyCount = record.getPartyCount();
            String bookDate = record.getBookDate();
            String bookedBy;
            if (record.getBookedByEmployee() == null)
                bookedBy = "Booked via online";
            else
                bookedBy = "Booked by employee " + record.getBookedByEmployee();
            String status = record.getStatus();

            String customerName = bookingCustomer.getFirstName() + " " + bookingCustomer.getLastName();
            String customerPhone = UiUtil.formatPhone(bookingCustomer.getPhone());

            JButton button = new JButton("     "
                                            + bookingId
                                            + "          "
                                            + customerName
                                            + "          "
                                            + customerPhone
                                            + "          "
                                            + UiUtil.formatDate(startDate)
                                            + " - "
                                            + UiUtil.formatDate(endDate)
                                            + "          Party count: "
                                            + partyCount
                                            + "          Status: "
                                            + UiUtil.formatString(status)
                                            + "          "
                                            + bookedBy
                                            + " on "
                                            + UiUtil.formatDate(bookDate));
            button.setMaximumSize(new Dimension(Integer.MAX_VALUE, button.getPreferredSize().height));
            button.setHorizontalAlignment(SwingConstants.LEFT);
            UiUtil.applyButtonLook(button);
            button.setFont(new Font("Tahoma", Font.PLAIN, 20));
            button.addActionListener(new bookingListButtonListener(record));
            VIEW_BOOKINGS_LIST_PANEL.add(button);
        }
        VIEW_BOOKINGS_LIST_PANEL.revalidate();
        VIEW_BOOKINGS_LIST_PANEL.repaint();
    }
    public void initBookingInfo()
    {
        // IF DB CONNECTION DNE, DON'T INITIALIZE BOOKING INFO AND GO BACK TO BOOKING LIST SCREEN
        Object idResultFromDb1 = HotelSystem.getFromId("booking", selectedBooking.getId());
        if (idResultFromDb1 instanceof Integer errorCode && errorCode == -1)
        {
            UiUtil.createDbErrorWindow();
            return;
        }
        Booking record = (Booking)idResultFromDb1;

        Object idResultFromDb2 = HotelSystem.getFromId("customer", record.getCustomerId());
        if (idResultFromDb2 instanceof Integer errorCode && errorCode == -1)
        {
            UiUtil.createDbErrorWindow();
            return;
        }
        Customer bookingCustomer = (Customer)idResultFromDb2;

        ArrayList<Map.Entry<String, String>> info = new ArrayList<>();
        info.add(Map.entry("Booking Status", UiUtil.formatString(record.getStatus())));
        info.add(Map.entry("Check in Date", UiUtil.formatDate(record.getStartDate())));
        info.add(Map.entry("Check Out Date", UiUtil.formatDate(record.getEndDate())));
        info.add(Map.entry("Booked On", UiUtil.formatDate(record.getBookDate())));
        info.add(Map.entry("Name", bookingCustomer.getFirstName() + " " + bookingCustomer.getLastName()));
        info.add(Map.entry("Phone Number", UiUtil.formatPhone(bookingCustomer.getPhone())));
        info.add(Map.entry("E-Mail", bookingCustomer.getEmail()));
        info.add(Map.entry("Party Count", Integer.toString(record.getPartyCount())));
        info.add(Map.entry("Total Charge", "$" + String.format("%.2f", record.getTotalCharge())));
        info.add(Map.entry("Points Used", String.format("%.2f", record.getPointsUsed())));

        @SuppressWarnings("unused")
        ScreenUtilInfo infoScreen = new ScreenUtilInfo(info,
                                                        "Booking Information",
                                                        CNAME_VIEW_BOOKINGS,
                                                        new ArrayList<>(List.of(CANCEL_BUTTON, RESCHEDULE_BUTTON, CHECK_IN_OR_OUT_BUTTON)));
    }
    public void initRescheduleInfo(String startDate, String endDate)
    {
        String newTotalCharge = "$" + String.format("%.2f", ChronoUnit.DAYS.between(
                                                                LocalDate.parse(startDate),
                                                                LocalDate.parse(endDate)
                                                            ) * Booking.chargePerDay());

        ArrayList<Map.Entry<String, String>> info = new ArrayList<>();
        info.add(Map.entry("New Check in Date", UiUtil.formatDate(startDate)));
        info.add(Map.entry("New Check Out Date", UiUtil.formatDate(endDate)));
        info.add(Map.entry("New Base Charge", newTotalCharge));

        @SuppressWarnings("unused")
        ScreenUtilInfo infoScreen = new ScreenUtilInfo(info,
                                                        "Verify New Dates",
                                                        CNAME_RESCHEDULE,
                                                        new ArrayList<>(List.of(UiUtil.addCornerButton(null, "Next", this::confirmReschedule))));
    }
    public void reset()
    {
        selectedBooking = null;
        selectedStartDate = "";
        selectedEndDate = "";

        VIEW_BOOKINGS.clearResults();
        INVALID_START_DATE.setVisible(false);
        INVALID_END_DATE.setVisible(false);
    }
    public void setCheckInOrOutRef(ScreenCheckInOrOut screen)
    {
        checkInOrOutRef = screen;
    }
    public JPanel getScreen(int screen)
    {
        if (screen == 1)
            return VIEW_BOOKINGS;
        return RESCHEDULE;
    }
}