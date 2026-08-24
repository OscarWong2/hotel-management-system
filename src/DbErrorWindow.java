import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.*;

public class DbErrorWindow extends JFrame
{
    private final JLabel MSG;
    private final JButton CLOSE;

    private class closeWindowListener implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent event)
        {
            dispose();
        }
    }

    public DbErrorWindow()
    {
        MSG = new JLabel("An error occurred connecting to the database, please try again.");
        CLOSE = new JButton("OK");

        setTitle("Error");
        setIconImage(new ImageIcon(new File("Programs/hotel-management-system/images/Icon.png").getAbsolutePath()).getImage());
        setSize(960, 270);
        setLocationRelativeTo(null);
        setLayout(new GridBagLayout());

        GridBagConstraints layout = new GridBagConstraints();
        layout.insets = new Insets(10, 0, 10, 0);

        // MSG
        layout.gridx = 0;
        layout.gridy = 0;
        MSG.setFont(new Font("Tahoma", Font.PLAIN, 20));
        add(MSG, layout);
        // CLOSE
        layout.gridx = 0;
        layout.gridy = 1;
        UiUtil.applyButtonLook(CLOSE);
        CLOSE.setFont(new Font("Tahoma", Font.PLAIN, 20));
        CLOSE.addActionListener(new closeWindowListener());
        add(CLOSE, layout);

        setVisible(true);
    }
}