import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

public class ScreenUtilConfirm extends JPanel
{
    // CNAME = CardLayout Names
    public static final String CNAME_CONFIRM = "confirm";
    public static final String CNAME_CONFIRM_ENTER_ID = "confirmEnterId";


    private final Runnable YES_RUNNABLE;
    private final String NO_CNAME;

    private final JLabel CONFIRM_MSG;
    private final JButton YES_BUTTON;
    private final JButton NO_BUTTON;

    private JPanel enterId;

    private class buttonListener implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent event)
        {
            if (event.getSource() == YES_BUTTON)
            {
                if (enterId != null)
                    UiUtil.showPanel(CNAME_CONFIRM_ENTER_ID);
                else
                {
                    YES_RUNNABLE.run();
                    removeThisPanel();
                }
            }
            else
            {
                UiUtil.showPanel(NO_CNAME);
                removeThisPanel();
            }
        }
    }

    public ScreenUtilConfirm(Runnable yesRunnable, String noCName, String msg, boolean requireId)
    {
        YES_RUNNABLE = yesRunnable;
        NO_CNAME = noCName;

        CONFIRM_MSG = new JLabel(msg);
        YES_BUTTON = new JButton("Yes");
        NO_BUTTON = new JButton("No");

        // Initialize layout

        setLayout(new GridBagLayout());

        GridBagConstraints layout = new GridBagConstraints();
        layout.insets = new Insets(10, 0, 10, 0);

        if (requireId)
        {
            enterId = new JPanel(new BorderLayout());
            JPanel HEADER = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            JPanel MAIN_PANEL = new JPanel(new GridBagLayout());
            JLabel ENTER_ID_MSG = new JLabel("Enter Your Employee ID");
            JTextField ENTER_ID_FIELD = new JPasswordField(10);
            JButton ENTER_BUTTON = new JButton("Enter");
            JLabel INVALID_ID_MSG = new JLabel(" ");

            class enterIdListener implements ActionListener
            {
                @Override
                public void actionPerformed(ActionEvent event)
                {
                    if (!ENTER_ID_FIELD.getText().chars().allMatch(Character::isDigit)
                        || ENTER_ID_FIELD.getText().isEmpty()
                        || !ENTER_ID_FIELD.getText().equals(Integer.toString(HotelSystem.getEmployeeIdLoggedIn())))
                        INVALID_ID_MSG.setText("Incorrect employee ID.");
                    else
                    {
                        YES_RUNNABLE.run();
                        removeThisPanel();
                    }
                }
            }

            HEADER.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
            HEADER.add(UiUtil.addCornerButton(noCName, "Cancel", this::removeThisPanel));
            enterId.add(HEADER, BorderLayout.NORTH);

            layout.gridx = 0;
            layout.gridy = 0;
            layout.anchor = GridBagConstraints.CENTER;
            ENTER_ID_MSG.setFont(new Font("Tahoma", Font.PLAIN, 40));
            MAIN_PANEL.add(ENTER_ID_MSG, layout);

            layout.gridx = 0;
            layout.gridy = 1;
            ENTER_ID_FIELD.setFont(new Font("Tahoma", Font.PLAIN, 25));
            ENTER_ID_FIELD.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
            ENTER_ID_FIELD.setHorizontalAlignment(JTextField.CENTER);
            ENTER_ID_FIELD.addActionListener(new enterIdListener());
            MAIN_PANEL.add(ENTER_ID_FIELD, layout);

            layout.gridx = 0;
            layout.gridy = 2;
            UiUtil.applyButtonLook(ENTER_BUTTON);
            ENTER_BUTTON.setFont(new Font("Tahoma", Font.PLAIN, 25));
            ENTER_BUTTON.setHorizontalAlignment(JButton.CENTER);
            ENTER_BUTTON.addActionListener(new enterIdListener());
            MAIN_PANEL.add(ENTER_BUTTON, layout);

            layout.gridx = 0;
            layout.gridy = 3;
            INVALID_ID_MSG.setFont(new Font("Tahoma", Font.PLAIN, 20));
            INVALID_ID_MSG.setForeground(new Color(255, 100, 100));
            MAIN_PANEL.add(INVALID_ID_MSG, layout);

            enterId.add(MAIN_PANEL, BorderLayout.CENTER);
        }

        // CONFIRM_MSG
        layout.gridx = 0;
        layout.gridy = 0;
        CONFIRM_MSG.setFont(new Font("Tahoma", Font.PLAIN, 35));
        add(CONFIRM_MSG, layout);
        // YES_BUTTON
        layout.gridx = 0;
        layout.gridy = 1;
        UiUtil.applyButtonLook(YES_BUTTON);
        YES_BUTTON.setFont(new Font("Tahoma", Font.PLAIN, 35));
        YES_BUTTON.setHorizontalAlignment(JButton.CENTER);
        YES_BUTTON.addActionListener(new buttonListener());
        add(YES_BUTTON, layout);
        // NO_BUTTON
        layout.gridx = 0;
        layout.gridy = 2;
        UiUtil.applyButtonLook(NO_BUTTON);
        NO_BUTTON.setFont(new Font("Tahoma", Font.PLAIN, 35));
        NO_BUTTON.setHorizontalAlignment(JButton.CENTER);
        NO_BUTTON.addActionListener(new buttonListener());
        add(NO_BUTTON, layout);

        addThisPanel();
        UiUtil.showPanel(CNAME_CONFIRM);
    }

    private void addThisPanel()
    {
        UiUtil.addPanel(this, CNAME_CONFIRM);
        if (enterId != null)
            UiUtil.addPanel(enterId, CNAME_CONFIRM_ENTER_ID);
    }
    private void removeThisPanel()
    {
        UiUtil.removePanel(this);
        if (enterId != null)
            UiUtil.removePanel(enterId);
    }
}