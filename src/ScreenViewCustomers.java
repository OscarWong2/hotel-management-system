import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Map;
import javax.swing.*;

public class ScreenViewCustomers
{
    // CNAME = CardLayout Names
    public static final String CNAME_VIEW_CUSTOMERS = "viewCustomers";


    private final ScreenUtilList VIEW_CUSTOMERS;
    private final JPanel LIST_PANEL;
    private final JTextField SEARCH_BAR;

    private class listButtonListener implements ActionListener
    {
        private final int ID;

        public listButtonListener(int newId)
        {
            ID = newId;
        }
        @Override
        public void actionPerformed(ActionEvent event)
        {
            initCustomerInfo((int)ID);
        }
    }

    public ScreenViewCustomers()
    {
        VIEW_CUSTOMERS = new ScreenUtilList(ScreenMenu.CNAME_MENU,
                                            null,
                                            this::initCustomerList,
                                            true,
                                            new ArrayList<>());
        LIST_PANEL = VIEW_CUSTOMERS.getListPanel();
        SEARCH_BAR = VIEW_CUSTOMERS.getSearchBar();
    }

    public void initCustomerList()
    {
        // IF DB CONNECTION DNE, DON'T INITIALIZE CUSTOMER LIST
        ArrayList<Object> records = HotelSystem.filterRecords("customer", SEARCH_BAR.getText());
        if (records == null)
        {
            UiUtil.createDbErrorWindow();
            return;
        }

        LIST_PANEL.removeAll();
        for (Object o : records)
        {
            Customer record = (Customer)o;
            int customerId = record.getId();
            String name = record.getLastName() + ", " + record.getFirstName();
            String phone = UiUtil.formatPhone(record.getPhone());
            String email = record.getEmail();

            JButton button = new JButton("     "
                                            + customerId
                                            + "          "
                                            + name
                                            + "          "
                                            + phone
                                            + "          "
                                            + email);
            button.setMaximumSize(new Dimension(Integer.MAX_VALUE, button.getPreferredSize().height));
            button.setHorizontalAlignment(SwingConstants.LEFT);
            UiUtil.applyButtonLook(button);
            button.setFont(new Font("Tahoma", Font.PLAIN, 20));
            button.addActionListener(new listButtonListener(customerId));
            LIST_PANEL.add(button);
        }
        LIST_PANEL.revalidate();
        LIST_PANEL.repaint();
    }
    public void initCustomerInfo(int customerId)
    {
        // IF DB CONNECTION DNE, STAY ON CUSTOMER LIST SCREEN
        Object idResultFromDb = HotelSystem.getFromId("customer", customerId);
        if (idResultFromDb instanceof Integer errorCode && errorCode == -1)
        {
            UiUtil.createDbErrorWindow();
            return;
        }
        Customer record = (Customer)idResultFromDb;

        ArrayList<Map.Entry<String, String>> info = new ArrayList<>();
        info.add(Map.entry("Name", record.getFirstName() + " " + record.getLastName()));
        info.add(Map.entry("Phone Number", UiUtil.formatPhone(record.getPhone())));
        info.add(Map.entry("E-Mail", record.getEmail()));
        info.add(Map.entry("Points", Integer.toString(record.getPoints())));
        info.add(Map.entry("Customer ID", Integer.toString(record.getId())));

        @SuppressWarnings("unused")
        ScreenUtilInfo infoScreen = new ScreenUtilInfo(info,
                                                        "Customer Information",
                                                        CNAME_VIEW_CUSTOMERS,
                                                        new ArrayList<>());
    }
    public JPanel getScreen(int screen)
    {
        return VIEW_CUSTOMERS;
    }
}