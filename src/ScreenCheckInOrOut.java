import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.swing.*;

public class ScreenCheckInOrOut
{
    // CNAME = CardLayout Names
    public static final String CNAME_CHECK_IN_OR_OUT_VIEW_BOOKINGS = "checkInOrOutViewBookings";
    public static final String CNAME_USE_POINTS = "usePoints";
    public static final String CNAME_SELECT_ROOMS = "chooseRooms";

    private ScreenViewBookings viewBookingsRef;

    private Booking selectedBooking;
    private int selectedPartyCount;
    private float selectedCustomerPoints;
    private final ArrayList<Map.Entry<String, String>> ROOMS_SELECTED;

    private boolean checkInFromViewBookings;


    private final ScreenUtilList VIEW_BOOKINGS;
    private final JPanel VIEW_BOOKINGS_LIST_PANEL;
    private final JTextField VIEW_BOOKINGS_SEARCH_BAR;


    private final JPanel USE_POINTS;
    private final JPanel USE_POINTS_TOP_HEADER;
    private final JPanel USE_POINTS_PANEL;
    private final JPanel USE_POINTS_BOTTOM_HEADER;

    private final JTextField ENTER_DAYS_FOR_POINTS;
    private final JLabel ENTER_DAYS_FOR_POINTS_HEADER;
    private final JLabel INVALID_DAYS_ENTERED;


    private final ScreenUtilList SELECT_ROOMS;
    private final JPanel SELECT_ROOMS_LIST_PANEL;
    private final JButton PROCEED_TO_REVIEW_ROOMS_BUTTON;
    private final JLabel PARTY_COUNT_MSG_COUNTER;

    private class bookingListButtonListener implements ActionListener
    {
        private final Booking BUTTON_BOOKING;
        private final float CUSTOMER_POINTS;

        public bookingListButtonListener(Booking newBooking, float newCustomerPoints)
        {
            BUTTON_BOOKING = newBooking;
            CUSTOMER_POINTS = newCustomerPoints;
        }
        @Override
        public void actionPerformed(ActionEvent event)
        {
            checkInOrOut(BUTTON_BOOKING, CUSTOMER_POINTS, false);
        }
    }
    private class roomListButtonListener implements ActionListener
    {
        private final int ROOM_NUM;
        private final String ROOM_TYPE;
        private final int BED_COUNT;

        private final Color COLOR_SELECTED;

        public roomListButtonListener(int newRoomNum, String newRoomType)
        {
            ROOM_NUM = newRoomNum;
            ROOM_TYPE = newRoomType;
            switch (ROOM_TYPE)
            {
                case "single", "twin" -> BED_COUNT = 1;
                case "double" -> BED_COUNT = 2;
                default -> BED_COUNT = 4;
            }
            COLOR_SELECTED = new Color(155, 100, 100);
        }
        @Override
        public void actionPerformed(ActionEvent event)
        {
            JButton listButton = (JButton)event.getSource();
            if (listButton.getBackground() == COLOR_SELECTED)
            {
                selectedPartyCount += BED_COUNT;
                ROOMS_SELECTED.remove(ROOMS_SELECTED.indexOf(Map.entry(Integer.toString(ROOM_NUM), ROOM_TYPE)));
                UiUtil.applyButtonLook(listButton);
            }
            else
            {
                if (selectedPartyCount > 0)
                {
                    selectedPartyCount -= BED_COUNT;
                    ROOMS_SELECTED.add(Map.entry(Integer.toString(ROOM_NUM), ROOM_TYPE));
                    listButton.setBackground(COLOR_SELECTED);
                    listButton.revalidate();
                    listButton.repaint();
                }
            }

            if (selectedPartyCount <= 0)
            {
                PROCEED_TO_REVIEW_ROOMS_BUTTON.setVisible(true);
                PARTY_COUNT_MSG_COUNTER.setVisible(false);
            }
            else
            {
                PROCEED_TO_REVIEW_ROOMS_BUTTON.setVisible(false);
                PARTY_COUNT_MSG_COUNTER.setText(selectedPartyCount + " left to accommodate.");
                PARTY_COUNT_MSG_COUNTER.setVisible(true);
            }
        }
    }
    private class enterPointsListener implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent event)
        {
            String startDate = selectedBooking.getStartDate();
            String endDate = selectedBooking.getEndDate();
            String days = ENTER_DAYS_FOR_POINTS.getText();

            if (!days.chars().allMatch(Character::isDigit)
                || Integer.parseInt(days) < 0
                || Integer.parseInt(days) > selectedCustomerPoints/150
                || Integer.parseInt(days) > ChronoUnit.DAYS.between(
                                                LocalDate.parse(startDate),
                                                LocalDate.parse(endDate))
                                            )
            {
                long maxDays;
                if (selectedCustomerPoints/150 > ChronoUnit.DAYS.between(
                                                        LocalDate.parse(startDate),
                                                        LocalDate.parse(endDate)
                                                    ))
                    maxDays = ChronoUnit.DAYS.between(LocalDate.parse(startDate), LocalDate.parse(endDate));
                else
                    maxDays = (long)(selectedCustomerPoints/Booking.chargePerDay());
                if (!INVALID_DAYS_ENTERED.getText().equals("Enter a number between 0 and " + maxDays + "."))
                    INVALID_DAYS_ENTERED.setText("Enter a number between 0 and " + maxDays + ".");
            }
            else
                INVALID_DAYS_ENTERED.setText(" ");
        }
    }

    public ScreenCheckInOrOut()
    {
        selectedBooking = null;
        ROOMS_SELECTED = new ArrayList<>();

        checkInFromViewBookings = false;


        VIEW_BOOKINGS = new ScreenUtilList(ScreenMenu.CNAME_MENU,
                                            this::reset,
                                            this::initBookingList,
                                            true,
                                            new ArrayList<>());
        VIEW_BOOKINGS_LIST_PANEL = VIEW_BOOKINGS.getListPanel();
        VIEW_BOOKINGS_SEARCH_BAR = VIEW_BOOKINGS.getSearchBar();


        USE_POINTS = new JPanel(new BorderLayout());
        USE_POINTS_TOP_HEADER = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        USE_POINTS_PANEL = new JPanel(new GridBagLayout());
        USE_POINTS_BOTTOM_HEADER = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        ENTER_DAYS_FOR_POINTS = new JTextField(5);
        ENTER_DAYS_FOR_POINTS_HEADER = new JLabel("Nights");
        INVALID_DAYS_ENTERED = new JLabel(" ");


        PROCEED_TO_REVIEW_ROOMS_BUTTON = UiUtil.addCornerButton(null, "Review rooms", this::initRoomsInfo);
        PARTY_COUNT_MSG_COUNTER = new JLabel();
        SELECT_ROOMS = new ScreenUtilList(null,
                                            this::returnToStartingScreen,
                                            this::initRoomList,
                                            false,
                                            new ArrayList<>(List.of(PROCEED_TO_REVIEW_ROOMS_BUTTON, PARTY_COUNT_MSG_COUNTER)));
        SELECT_ROOMS_LIST_PANEL = SELECT_ROOMS.getListPanel();

        // Initialize layout

        GridBagConstraints layout = new GridBagConstraints();
        layout.insets = new Insets(15, 15, 15, 15);
        layout.fill = GridBagConstraints.HORIZONTAL;

        // USE_POINTS_TOP_HEADER
        USE_POINTS_TOP_HEADER.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        USE_POINTS_TOP_HEADER.add(UiUtil.addCornerButton(null, "Back", this::returnToStartingScreen));

        // ENTER_DAYS_FOR_POINTS
        layout.gridx = 1;
        layout.gridy = 0;
        ENTER_DAYS_FOR_POINTS.setFont(new Font("Tahoma", Font.PLAIN, 30));
        ENTER_DAYS_FOR_POINTS.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        ENTER_DAYS_FOR_POINTS.setHorizontalAlignment(SwingConstants.LEFT);
        ENTER_DAYS_FOR_POINTS.addActionListener(new enterPointsListener());
        USE_POINTS_PANEL.add(ENTER_DAYS_FOR_POINTS, layout);
        // ENTER_DAYS_FOR_POINTS_HEADER
        layout.gridx = 0;
        layout.gridy = 0;
        ENTER_DAYS_FOR_POINTS_HEADER.setFont(new Font("Tahoma", Font.PLAIN, 30));
        USE_POINTS_PANEL.add(ENTER_DAYS_FOR_POINTS_HEADER, layout);
        // INVALID_DAYS_ENTERED
        layout.gridx = 2;
        layout.gridy = 0;
        INVALID_DAYS_ENTERED.setFont(new Font("Tahoma", Font.PLAIN, 30));
        INVALID_DAYS_ENTERED.setForeground(new Color(255, 100, 100));
        USE_POINTS_PANEL.add(INVALID_DAYS_ENTERED, layout);

        // USE_POINTS_BOTTOM_HEADER
        USE_POINTS_BOTTOM_HEADER.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        USE_POINTS_BOTTOM_HEADER.add(UiUtil.addCornerButton(null, "Next", this::checkUsePointsInfo));

        // USE_POINTS
        USE_POINTS.add(USE_POINTS_TOP_HEADER, BorderLayout.NORTH);
        USE_POINTS.add(USE_POINTS_PANEL, BorderLayout.CENTER);
        USE_POINTS.add(USE_POINTS_BOTTOM_HEADER, BorderLayout.SOUTH);


        // PARTY_COUNT_MSG_COUNTER
        PARTY_COUNT_MSG_COUNTER.setFont(new Font("Tahoma", Font.PLAIN, 20));
        // PROCEED_TO_REVIEW_ROOMS_BUTTON
        UiUtil.applyButtonLook(PROCEED_TO_REVIEW_ROOMS_BUTTON);
    }

    public void checkUsePointsInfo()
    {
        String startDate = selectedBooking.getStartDate();
        String endDate = selectedBooking.getEndDate();
        String days = ENTER_DAYS_FOR_POINTS.getText();

        if (!days.chars().allMatch(Character::isDigit)
            || Integer.parseInt(days) < 0
            || Integer.parseInt(days) > selectedCustomerPoints/Booking.chargePerDay()
            || Integer.parseInt(days) > ChronoUnit.DAYS.between(
                                            LocalDate.parse(startDate),
                                            LocalDate.parse(endDate))
                                        )
        {
            long maxDays;
            if (selectedCustomerPoints/Booking.chargePerDay() > ChronoUnit.DAYS.between(
                                                                    LocalDate.parse(startDate),
                                                                    LocalDate.parse(endDate)
                                                                ))
                maxDays = ChronoUnit.DAYS.between(LocalDate.parse(startDate), LocalDate.parse(endDate));
            else
                maxDays = (long)(selectedCustomerPoints/Booking.chargePerDay());
            if (!INVALID_DAYS_ENTERED.getText().equals("Enter a number between 0 and " + maxDays + "."))
                INVALID_DAYS_ENTERED.setText("Enter a number between 0 and " + maxDays + ".");
            return;
        }
        else
            INVALID_DAYS_ENTERED.setText(" ");

        selectedBooking.setPointsUsed(Integer.parseInt(days) * Booking.chargePerDay());

        initPointsInfo();
    }
    public void checkEnoughPoints()
    {
        ENTER_DAYS_FOR_POINTS.setText("");
        INVALID_DAYS_ENTERED.setText(" ");

        if (selectedCustomerPoints < Booking.chargePerDay())
        {
            UiUtil.showPanel(CNAME_SELECT_ROOMS);
            return;
        }

        Runnable checkPoints = () -> UiUtil.showPanel(CNAME_USE_POINTS);

        @SuppressWarnings("unused")
        ScreenUtilConfirm confirmScreen = new ScreenUtilConfirm(checkPoints, CNAME_SELECT_ROOMS, "Use points?", false);
    }
    public void initPointsInfo()
    {
        float totalCharge = selectedBooking.getTotalCharge();
        float pointsUsed = selectedBooking.getPointsUsed();

        ArrayList<Map.Entry<String, String>> info = new ArrayList<>();
        info.add(Map.entry("Points to Use", "$" + String.format("%.2f", pointsUsed)));
        info.add(Map.entry("Total Charge After", "$" + String.format("%.2f", totalCharge - pointsUsed)));
        info.add(Map.entry("Customer Points", "$" + String.format("%.2f", selectedCustomerPoints - pointsUsed)));

        @SuppressWarnings("unused")
        ScreenUtilInfo infoScreen = new ScreenUtilInfo(info,
                                                        "Verify Points",
                                                        CNAME_USE_POINTS,
                                                        new ArrayList<>(List.of(UiUtil.addCornerButton(null, "Next", this::updatePointsInDb))));
    }
    public void initBookingList()
    {
        // IF DB CONNECTION DNE, DON'T INITIALIZE BOOKING LIST
        ArrayList<Booking> bookings = HotelSystem.getCheckInOrOutBookings(VIEW_BOOKINGS_SEARCH_BAR.getText());
        if (bookings == null)
        {
            UiUtil.createDbErrorWindow();
            return;
        }

        VIEW_BOOKINGS_LIST_PANEL.removeAll();
        for (Booking b : bookings)
        {
            // IF DB CONNECTION DNE, STOP INITIALIZING BOOKING LIST
            Object idResultFromDb = HotelSystem.getFromId("customer", b.getCustomerId());
            if (idResultFromDb instanceof Integer errorCode && errorCode == -1)
            {
                UiUtil.createDbErrorWindow();
                VIEW_BOOKINGS_LIST_PANEL.revalidate();
                VIEW_BOOKINGS_LIST_PANEL.repaint();
                return;
            }

            Customer bookingCustomer = (Customer)idResultFromDb;

            int bookingId = b.getId();
            String startDate = b.getStartDate();
            String endDate = b.getEndDate();
            String status = b.getStatus();

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
                                            + "          Status: "
                                            + UiUtil.formatString(status));
            button.setMaximumSize(new Dimension(Integer.MAX_VALUE, button.getPreferredSize().height));
            button.setHorizontalAlignment(SwingConstants.LEFT);
            UiUtil.applyButtonLook(button);
            button.setFont(new Font("Tahoma", Font.PLAIN, 20));
            button.addActionListener(new bookingListButtonListener(b, bookingCustomer.getPoints()));
            VIEW_BOOKINGS_LIST_PANEL.add(button);
        }
        VIEW_BOOKINGS_LIST_PANEL.revalidate();
        VIEW_BOOKINGS_LIST_PANEL.repaint();
    }
    public void initRoomList()
    {
        ROOMS_SELECTED.clear();
        PROCEED_TO_REVIEW_ROOMS_BUTTON.setVisible(false);
        PARTY_COUNT_MSG_COUNTER.setText(selectedPartyCount + " left to accommodate.");
        PARTY_COUNT_MSG_COUNTER.setVisible(true);

        // IF DB CONNECTION DNE, DON'T INITIALIZE ROOM LIST
        ArrayList<Room> rooms = HotelSystem.getAvailRooms();
        if (rooms == null)
        {
            UiUtil.createDbErrorWindow();
            return;
        }

        SELECT_ROOMS_LIST_PANEL.removeAll();
        for (Room r : rooms)
        {
            int roomNum = r.getRoomNum();
            String roomType = r.getRoomType();

            JButton button = new JButton("     "
                                            + roomNum
                                            + " - "
                                            + UiUtil.formatString(roomType));
            button.setMaximumSize(new Dimension(Integer.MAX_VALUE, button.getPreferredSize().height));
            button.setHorizontalAlignment(SwingConstants.LEFT);
            UiUtil.applyButtonLook(button);
            button.setFont(new Font("Tahoma", Font.PLAIN, 20));
            button.addActionListener(new roomListButtonListener(roomNum, roomType));
            SELECT_ROOMS_LIST_PANEL.add(button);
        }
        SELECT_ROOMS_LIST_PANEL.revalidate();
        SELECT_ROOMS_LIST_PANEL.repaint();
    }
    public void initRoomsInfo()
    {
        @SuppressWarnings("unused")
        ScreenUtilInfo infoScreen = new ScreenUtilInfo(ROOMS_SELECTED,
                                                        "Verify Rooms",
                                                        CNAME_SELECT_ROOMS,
                                                        new ArrayList<>(List.of(UiUtil.addCornerButton(null, "Check in", this::processDbCheckInOrOut))));
    }
    public void returnToStartingScreen()
    {
        if (checkInFromViewBookings)
        {
            checkInFromViewBookings = false;
            UiUtil.showPanel(ScreenViewBookings.CNAME_VIEW_BOOKINGS);
        }
        else
            UiUtil.showPanel(CNAME_CHECK_IN_OR_OUT_VIEW_BOOKINGS);
    }
    public void checkInOrOut(Booking bookingToCheck, float customerPoints, boolean calledFromViewBookings)
    {
        selectedBooking = bookingToCheck;
        selectedPartyCount = selectedBooking.getPartyCount();
        selectedCustomerPoints = customerPoints;

        checkInFromViewBookings = calledFromViewBookings;

        if (selectedBooking.getStatus().equals("booked"))
        {
            initRoomList();
            checkEnoughPoints();
        }
        else
            processDbCheckInOrOut();
    }
    public void processDbCheckInOrOut()
    {
        Runnable proceedCheckInOrOut = () ->
        {
            ArrayList<Integer> rooms = new ArrayList<>(ROOMS_SELECTED
                                                        .stream()
                                                        .map(room -> Integer.valueOf(room.getKey()))
                                                        .collect(Collectors.toList()));
            // IF DB CONNECTION DNE, GO BACK TO ROOM SELECTION SCREEN (CHECK IN) OR BOOKING LIST SCREEN (CHECK OUT)
            if (!HotelSystem.updateBookingStatus(selectedBooking.getId(), false, rooms))
                UiUtil.createDbErrorWindow();
            else
            {
                if (checkInFromViewBookings)
                {
                    checkInFromViewBookings = false;
                    viewBookingsRef.reset();
                }
                else
                    reset();
                UiUtil.showPanel(ScreenMenu.CNAME_MENU);
            }
        };

        String screenMsg;
        if (selectedBooking.getStatus().equals("booked"))
            screenMsg = "Confirm CHECK IN?";
        else
            screenMsg = "Confirm CHECK OUT?";
        @SuppressWarnings("unused")
        ScreenUtilConfirm confirmScreen;
        confirmScreen = new ScreenUtilConfirm(proceedCheckInOrOut, CNAME_SELECT_ROOMS, screenMsg, true);
    }
    public void updatePointsInDb()
    {
        Runnable proceedUpdatePointsInDb = () ->
        {
            int id = selectedBooking.getId();
            float pointsUsed = selectedBooking.getPointsUsed();

            // IF DB CONNECTION DNE, GO BACK TO 'USE_POINTS' SCREEN
            if (HotelSystem.updatePointsUsed(id, pointsUsed))
            {
                selectedCustomerPoints -= pointsUsed;
                selectedBooking.setTotalCharge(selectedBooking.getTotalCharge() - pointsUsed);
                UiUtil.showPanel(CNAME_SELECT_ROOMS);
            }
        };

        @SuppressWarnings("unused")
        ScreenUtilConfirm confirmScreen = new ScreenUtilConfirm(proceedUpdatePointsInDb,
                                                                CNAME_USE_POINTS,
                                                                "Confirm USE POINTS?",
                                                                true);
    }
    public void reset()
    {
        selectedBooking = null;
        ROOMS_SELECTED.clear();

        VIEW_BOOKINGS.clearResults();
        ENTER_DAYS_FOR_POINTS.setText("");
        INVALID_DAYS_ENTERED.setText(" ");
    }
    public void setViewBookingsRef(ScreenViewBookings screen)
    {
        viewBookingsRef = screen;
    }
    public JPanel getScreen(int screen)
    {
        if (screen == 1)
            return VIEW_BOOKINGS;
        if (screen == 2)
            return USE_POINTS;
        return SELECT_ROOMS;
    }
}