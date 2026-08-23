import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

public class ScreenMenu
{
    // CNAME = CardLayout Names
    public static final String CNAME_MENU = "menu";


    private final JPanel MENU;
    private final JPanel OPTIONS;
    private final JPanel MENU_HEADER;
    private final JButton CHECK_IN_OR_OUT_BUTTON;
    private final JButton ADD_BOOKING_BUTTON;
    private final JButton VIEW_BOOKINGS_BUTTON;
    private final JButton VIEW_CUSTOMER_BUTTON;
    private final JButton VIEW_EMPLOYEE_BUTTON;
    private final JLabel WELCOME;

    private class menuListener implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent event)
        {
            if (event.getSource() == CHECK_IN_OR_OUT_BUTTON)
                UiUtil.showPanel(ScreenCheckInOrOut.CNAME_CHECK_IN_OR_OUT_VIEW_BOOKINGS);
            else if (event.getSource() == ADD_BOOKING_BUTTON)
                UiUtil.showPanel(ScreenAddBooking.CNAME_ADD_BOOKING_1);
            else if (event.getSource() == VIEW_BOOKINGS_BUTTON)
                UiUtil.showPanel(ScreenViewBookings.CNAME_VIEW_BOOKINGS);
            else if (event.getSource() == VIEW_CUSTOMER_BUTTON)
                UiUtil.showPanel(ScreenViewCustomers.CNAME_VIEW_CUSTOMERS);
            else
                UiUtil.showPanel(ScreenViewEmployees.CNAME_VIEW_EMPLOYEES);
        }
    }

    public ScreenMenu()
    {
        MENU = new JPanel(new BorderLayout());
        OPTIONS = new JPanel(new GridBagLayout());
        MENU_HEADER = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        CHECK_IN_OR_OUT_BUTTON = new JButton("Check In/Out");
        ADD_BOOKING_BUTTON = new JButton("Add Booking");
        VIEW_BOOKINGS_BUTTON = new JButton("Booking Lookup");
        VIEW_CUSTOMER_BUTTON = new JButton("Customer Lookup");
        VIEW_EMPLOYEE_BUTTON = new JButton("Employee Lookup");
        WELCOME = new JLabel(); // Welcome text is blank as user's name is displayed after logging in

        // Initialize layout

        GridBagConstraints layout = new GridBagConstraints();
        layout.insets = new Insets(10, 0, 10, 0);

        // WELCOME
        layout.gridx = 0;
        layout.gridy = 0;
        WELCOME.setForeground(Color.WHITE);
        WELCOME.setFont(new Font("Tahoma", Font.PLAIN, 30));
        OPTIONS.add(WELCOME, layout);
        // CHECK_IN_OR_OUT_BUTTON
        layout.gridx = 0;
        layout.gridy = 1;
        UiUtil.applyButtonLook(CHECK_IN_OR_OUT_BUTTON);
        CHECK_IN_OR_OUT_BUTTON.setFont(new Font("Tahoma", Font.PLAIN, 25));
        CHECK_IN_OR_OUT_BUTTON.addActionListener(new menuListener());
        OPTIONS.add(CHECK_IN_OR_OUT_BUTTON, layout);
        // ADD_BOOKING_BUTTON
        layout.gridx = 0;
        layout.gridy = 2;
        UiUtil.applyButtonLook(ADD_BOOKING_BUTTON);
        ADD_BOOKING_BUTTON.setFont(new Font("Tahoma", Font.PLAIN, 25));
        ADD_BOOKING_BUTTON.addActionListener(new menuListener());
        OPTIONS.add(ADD_BOOKING_BUTTON, layout);
        // VIEW_BOOKINGS_BUTTON
        layout.gridx = 0;
        layout.gridy = 3;
        UiUtil.applyButtonLook(VIEW_BOOKINGS_BUTTON);
        VIEW_BOOKINGS_BUTTON.setFont(new Font("Tahoma", Font.PLAIN, 25));
        VIEW_BOOKINGS_BUTTON.addActionListener(new menuListener());
        OPTIONS.add(VIEW_BOOKINGS_BUTTON, layout);
        // VIEW_CUSTOMER_BUTTON
        layout.gridx = 0;
        layout.gridy = 4;
        UiUtil.applyButtonLook(VIEW_CUSTOMER_BUTTON);
        VIEW_CUSTOMER_BUTTON.setFont(new Font("Tahoma", Font.PLAIN, 25));
        VIEW_CUSTOMER_BUTTON.addActionListener(new menuListener());
        OPTIONS.add(VIEW_CUSTOMER_BUTTON, layout);
        // VIEW_EMPLOYEE_BUTTON
        layout.gridx = 0;
        layout.gridy = 5;
        UiUtil.applyButtonLook(VIEW_EMPLOYEE_BUTTON);
        VIEW_EMPLOYEE_BUTTON.setFont(new Font("Tahoma", Font.PLAIN, 25));
        VIEW_EMPLOYEE_BUTTON.setVisible(false);
        VIEW_EMPLOYEE_BUTTON.addActionListener(new menuListener());
        OPTIONS.add(VIEW_EMPLOYEE_BUTTON, layout);
        // OPTIONS
        OPTIONS.setOpaque(false);
        // MENU_HEADER
        MENU_HEADER.setOpaque(false);
        MENU_HEADER.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        MENU_HEADER.add(UiUtil.addCornerButton(ScreenLogin.CNAME_LOGIN, "Log Out", null));
        // MENU
        MENU.setOpaque(false);
        MENU.add(MENU_HEADER, BorderLayout.NORTH);
        MENU.add(OPTIONS, BorderLayout.CENTER);
    }

    public void setLoginMsg(String employeeName, boolean isManager)
    {
        WELCOME.setText("Welcome back "
                        + employeeName
                        + "!");
        if (isManager)
            VIEW_EMPLOYEE_BUTTON.setVisible(true);
        else
            VIEW_EMPLOYEE_BUTTON.setVisible(false);
    }
    public JPanel getScreen()
    {
        return MENU;
    }
}