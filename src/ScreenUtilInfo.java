import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Map;
import javax.swing.*;

public class ScreenUtilInfo extends JPanel
{
    // CNAME = CardLayout Names
    public static final String CNAME_INFO = "info";


    private final JPanel TOP_HEADER;
    private final JPanel INFO_PANEL;
    private final JPanel BOTTOM_HEADER;

    private class bottomButtonListener implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent event)
        {
            removeThisPanel();
        }
    }

    public ScreenUtilInfo(ArrayList<Map.Entry<String, String>> newInfo,
                            String newTitle,
                            String backButtonTarget,
                            ArrayList<JButton> bottomHeaderButtons)
    {
        TOP_HEADER = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        INFO_PANEL = new JPanel(new GridBagLayout());
        BOTTOM_HEADER = new JPanel(new FlowLayout(FlowLayout.RIGHT));


        TOP_HEADER.add(UiUtil.addCornerButton(backButtonTarget, "Back", this::removeThisPanel));
        for (JButton button : bottomHeaderButtons)
        {
            button.addActionListener(new bottomButtonListener());
            BOTTOM_HEADER.add(button);
        }


        // Initialize layout

        setLayout(new BorderLayout());

        GridBagConstraints layout = new GridBagConstraints();
        layout.insets = new Insets(10, 0, 10, 0);
        layout.fill = GridBagConstraints.HORIZONTAL;

        // TOP_HEADER
        TOP_HEADER.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        add(TOP_HEADER, BorderLayout.NORTH);
        // BOTTOM_HEADER
        BOTTOM_HEADER.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        add(BOTTOM_HEADER, BorderLayout.SOUTH);
        // INFO_PANEL
        layout.gridx = 0;
        layout.gridy = 0;
        JLabel titleLabel = new JLabel(newTitle);
        titleLabel.setFont(new Font("Tahoma", Font.BOLD, 40));
        INFO_PANEL.add(titleLabel, layout);

        int y = 1;
        for (Map.Entry<String, String> e : newInfo)
        {
            layout.gridx = 0;
            layout.gridy = y;
            JLabel keyLabel = new JLabel(e.getKey());
            keyLabel.setFont(new Font("Tahoma", Font.ITALIC, 30));
            INFO_PANEL.add(keyLabel, layout);

            layout.gridx = 1;
            layout.gridy = y;
            JLabel valueLabel = new JLabel(e.getValue());
            valueLabel.setFont(new Font("Tahoma", Font.PLAIN, 30));
            INFO_PANEL.add(valueLabel, layout);

            y++;
        }

        add(INFO_PANEL, BorderLayout.CENTER);

        addThisPanel();
        UiUtil.showPanel(CNAME_INFO);
    }

    private void addThisPanel()
    {
        UiUtil.addPanel(this, CNAME_INFO);
    }
    private void removeThisPanel()
    {
        UiUtil.removePanel(this);
    }
}