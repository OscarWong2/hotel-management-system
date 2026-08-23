import java.awt.*;
import java.io.File;
import javax.swing.*;

public class Ui extends JFrame
{
    private final CardLayout CARD_LAYOUT;
    private final JPanel MAIN_PANEL;
    private final JLabel BACKGROUND;

    // Log in screen
    private final ScreenLogin LOGIN;
    // Main menu screen
    private final ScreenMenu MENU;
    // Check in or out screen
    private final ScreenCheckInOrOut CHECK_IN_OR_OUT;
    // Add booking screen
    private final ScreenAddBooking ADD_BOOKING;
    // View bookings screen
    private final ScreenViewBookings VIEW_BOOKINGS;
    // View customers screen
    private final ScreenViewCustomers VIEW_CUSTOMERS;
    // View employees screen
    private final ScreenViewEmployees VIEW_EMPLOYEES;

    public Ui()
    {
        CARD_LAYOUT = new CardLayout();
        MAIN_PANEL = new JPanel(CARD_LAYOUT);
        BACKGROUND = new JLabel(new ImageIcon(new File("Programs/hotel-management-system/images/Background.png").getAbsolutePath()));

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setTitle("Hotel Management");
        setIconImage(new ImageIcon(new File("Programs/hotel-management-system/images/Icon.png").getAbsolutePath()).getImage());
        setSize(1920, 1080);
        setContentPane(BACKGROUND);
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        UiUtil.setCardLayout(MAIN_PANEL, CARD_LAYOUT);


        MAIN_PANEL.setOpaque(false);
        // Add in reverse order in terms of layout
        VIEW_EMPLOYEES = new ScreenViewEmployees();
        MAIN_PANEL.add(VIEW_EMPLOYEES.getScreen(1), ScreenViewEmployees.CNAME_VIEW_EMPLOYEES);

        VIEW_CUSTOMERS = new ScreenViewCustomers();
        MAIN_PANEL.add(VIEW_CUSTOMERS.getScreen(1), ScreenViewCustomers.CNAME_VIEW_CUSTOMERS);
    
        ADD_BOOKING = new ScreenAddBooking();
        MAIN_PANEL.add(ADD_BOOKING.getScreen(1), ScreenAddBooking.CNAME_ADD_BOOKING_1);
        MAIN_PANEL.add(ADD_BOOKING.getScreen(2), ScreenAddBooking.CNAME_ADD_BOOKING_2);

        VIEW_BOOKINGS = new ScreenViewBookings();
        MAIN_PANEL.add(VIEW_BOOKINGS.getScreen(1), ScreenViewBookings.CNAME_VIEW_BOOKINGS);
        MAIN_PANEL.add(VIEW_BOOKINGS.getScreen(2), ScreenViewBookings.CNAME_RESCHEDULE);

        CHECK_IN_OR_OUT = new ScreenCheckInOrOut();
        MAIN_PANEL.add(CHECK_IN_OR_OUT.getScreen(1), ScreenCheckInOrOut.CNAME_CHECK_IN_OR_OUT_VIEW_BOOKINGS);
        MAIN_PANEL.add(CHECK_IN_OR_OUT.getScreen(2), ScreenCheckInOrOut.CNAME_USE_POINTS);
        MAIN_PANEL.add(CHECK_IN_OR_OUT.getScreen(3), ScreenCheckInOrOut.CNAME_SELECT_ROOMS);

        VIEW_BOOKINGS.setCheckInOrOutRef(CHECK_IN_OR_OUT);
        CHECK_IN_OR_OUT.setViewBookingsRef(VIEW_BOOKINGS);

        MENU = new ScreenMenu();
        MAIN_PANEL.add(MENU.getScreen(), ScreenMenu.CNAME_MENU);
    
        LOGIN = new ScreenLogin(MENU);
        MAIN_PANEL.add(LOGIN.getScreen(), ScreenLogin.CNAME_LOGIN);


        UiUtil.showPanel(ScreenLogin.CNAME_LOGIN);

        BACKGROUND.setLayout(new BorderLayout());
        BACKGROUND.add(MAIN_PANEL, BorderLayout.CENTER);

        setVisible(true);
    }
}