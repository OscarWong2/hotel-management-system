import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Map;
import javax.swing.*;

public class ScreenViewEmployees
{
    // CNAME = CardLayout Names
    public static final String CNAME_VIEW_EMPLOYEES = "viewEmployees";


    private final ScreenUtilList VIEW_EMPLOYEES;
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
            initEmployeeInfo((int)ID);
        }
    }

    public ScreenViewEmployees()
    {
        VIEW_EMPLOYEES = new ScreenUtilList(ScreenMenu.CNAME_MENU,
                                            null,
                                            this::initEmployeeList,
                                            true,
                                            new ArrayList<>());
        LIST_PANEL = VIEW_EMPLOYEES.getListPanel();
        SEARCH_BAR = VIEW_EMPLOYEES.getSearchBar();
    }

    public void initEmployeeList()
    {
        // IF DB CONNECTION DNE, DON'T INITIALIZE EMPLOYEE LIST
        ArrayList<Object> records = HotelSystem.filterRecords("employee", SEARCH_BAR.getText());
        if (records == null)
        {
            UiUtil.createDbErrorWindow();
            return;
        }

        LIST_PANEL.removeAll();
        for (Object o : records)
        {
            Employee record = (Employee)o;
            int employeeId = record.getId();
            String name = record.getLastName() + ", " + record.getFirstName();

            JButton button = new JButton("     " + name);
            button.setMaximumSize(new Dimension(Integer.MAX_VALUE, button.getPreferredSize().height));
            button.setHorizontalAlignment(SwingConstants.LEFT);
            UiUtil.applyButtonLook(button);
            button.setFont(new Font("Tahoma", Font.PLAIN, 20));
            button.addActionListener(new listButtonListener(employeeId));
            LIST_PANEL.add(button);
        }
        LIST_PANEL.revalidate();
        LIST_PANEL.repaint();
    }
    public void initEmployeeInfo(int employeeId)
    {
        // IF DB CONNECTION DNE, STAY ON EMPLOYEE LIST SCREEN
        Object idResultFromDb = HotelSystem.getFromId("employee", employeeId);
        if (idResultFromDb instanceof Integer errorCode && errorCode == -1)
        {
            UiUtil.createDbErrorWindow();
            return;
        }
        Employee record = (Employee)idResultFromDb;

        ArrayList<Map.Entry<String, String>> info = new ArrayList<>();
        info.add(Map.entry("Name", record.getFirstName() + " " + record.getLastName()));
        info.add(Map.entry("Phone Number", UiUtil.formatPhone(record.getPhone())));
        info.add(Map.entry("E-Mail", record.getEmail()));

        @SuppressWarnings("unused")
        ScreenUtilInfo infoScreen = new ScreenUtilInfo(info,
                                                        "Employee Information",
                                                        CNAME_VIEW_EMPLOYEES,
                                                        new ArrayList<>());
    }
    public JPanel getScreen(int screen)
    {
        return VIEW_EMPLOYEES;
    }
}