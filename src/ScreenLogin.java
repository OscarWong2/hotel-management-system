import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

public class ScreenLogin
{
    // CNAME = CardLayout Names
    public static final String CNAME_LOGIN = "login";


    private final ScreenMenu MENU_REF;

    private final JPanel LOGIN;
    private final JTextField EMPLOYEE_ID_FIELD;
    private final JButton LOGIN_BUTTON;
    private final JLabel LOGIN_HEADER;
    private final JLabel INVALID_LOGIN_MSG;

    private class loginListener implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent event)
        {
            if (!EMPLOYEE_ID_FIELD.getText().chars().allMatch(Character::isDigit)
                || EMPLOYEE_ID_FIELD.getText().isEmpty())
            {
                INVALID_LOGIN_MSG.setText("Invalid login. Please enter a valid employee ID.");
                return;
            }

            // IF DB CONNECTION DNE, DON'T LOGIN
            Object idResultFromDb = HotelSystem.getFromId("employee", Integer.parseInt(EMPLOYEE_ID_FIELD.getText()));
            if (idResultFromDb instanceof Integer errorCode && errorCode == -1)
            {
                UiUtil.createDbErrorWindow();
                return;
            }

            if (idResultFromDb == null)
                INVALID_LOGIN_MSG.setText("Invalid login. Please enter a valid employee ID.");
            else
            {
                Employee loginEmployee = (Employee)idResultFromDb;
                HotelSystem.setEmployeeIdLoggedIn(loginEmployee.getId());
                MENU_REF.setLoginMsg(loginEmployee.getFirstName() + " " + loginEmployee.getLastName(),
                                        loginEmployee.getIsManager());
                UiUtil.showPanel(ScreenMenu.CNAME_MENU);

                INVALID_LOGIN_MSG.setText(" ");
                EMPLOYEE_ID_FIELD.setText("");
            }
        }
    }

    public ScreenLogin(ScreenMenu menuRef)
    {
        MENU_REF = menuRef;

        LOGIN = new JPanel(new GridBagLayout());
        EMPLOYEE_ID_FIELD = new JPasswordField(10);
        LOGIN_BUTTON = new JButton("Log In");
        LOGIN_HEADER = new JLabel("Enter Your Employee ID");
        INVALID_LOGIN_MSG = new JLabel(" ");

        // Initialize layout

        GridBagConstraints layout = new GridBagConstraints();
        layout.insets = new Insets(10, 0, 10, 0);

        // LOGIN_HEADER
        layout.gridx = 0;
        layout.gridy = 0;
        layout.anchor = GridBagConstraints.CENTER;
        LOGIN_HEADER.setForeground(Color.WHITE);
        LOGIN_HEADER.setFont(new Font("Tahoma", Font.PLAIN, 40));
        LOGIN.add(LOGIN_HEADER, layout);
        // EMPLOYEE_ID_FIELD
        layout.gridx = 0;
        layout.gridy = 1;
        EMPLOYEE_ID_FIELD.setFont(new Font("Tahoma", Font.PLAIN, 25));
        EMPLOYEE_ID_FIELD.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        EMPLOYEE_ID_FIELD.setHorizontalAlignment(JTextField.CENTER);
        EMPLOYEE_ID_FIELD.addActionListener(new loginListener());
        LOGIN.add(EMPLOYEE_ID_FIELD, layout);
        // LOGIN_BUTTON
        layout.gridx = 0;
        layout.gridy = 2;
        UiUtil.applyButtonLook(LOGIN_BUTTON);
        LOGIN_BUTTON.setFont(new Font("Tahoma", Font.PLAIN, 25));
        LOGIN_BUTTON.setHorizontalAlignment(JButton.CENTER);
        LOGIN_BUTTON.addActionListener(new loginListener());
        LOGIN.add(LOGIN_BUTTON, layout);
        // INVALID_LOGIN_MSG
        layout.gridx = 0;
        layout.gridy = 3;
        INVALID_LOGIN_MSG.setFont(new Font("Tahoma", Font.PLAIN, 20));
        INVALID_LOGIN_MSG.setForeground(new Color(255, 100, 100));
        LOGIN.add(INVALID_LOGIN_MSG, layout);
        // LOGIN
        LOGIN.setOpaque(false);
    }

    public JPanel getScreen()
    {
        return LOGIN;
    }
}