import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.*;

public class ScreenUtilList extends JPanel
{
    // CNAME = CardLayout Names
    public static final String CNAME_LIST = "list";


    private final JPanel HEADER;
    private final JScrollPane SCROLL_PANE;
    private final JPanel LIST;

    private JTextField searchBar;

    public ScreenUtilList(String backButtonTarget,
                            Runnable backButtonMethod,
                            Runnable initListMethod,
                            boolean includeSearchBar,
                            ArrayList<JComponent> bottomHeaderComp)
    {
        HEADER = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        LIST = new JPanel();
        SCROLL_PANE = new JScrollPane(LIST);

        // Initialize layout

        setLayout(new BorderLayout());

        // Search bar
        if (includeSearchBar)
        {
            class searchBarListener implements ActionListener
            {
                @Override
                public void actionPerformed(ActionEvent event)
                {
                    if (searchBar.getText().isEmpty())
                        clearResults();
                    else
                        initListMethod.run();
                }
            }

            JLabel searchMsg = new JLabel("Search to show results:");
            searchMsg.setFont(new Font("Tahoma", Font.PLAIN, 20));
            HEADER.add(searchMsg);

            searchBar = new JTextField(10);
            searchBar.setFont(new Font("Tahoma", Font.PLAIN, 20));
            searchBar.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
            searchBar.setHorizontalAlignment(JTextField.LEFT);
            searchBar.addActionListener(new searchBarListener());
            HEADER.add(searchBar);
        }

        // Bottom header
        if (!bottomHeaderComp.isEmpty())
        {
            JPanel bottomHeader = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            bottomHeader.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

            for (JComponent comp : bottomHeaderComp)
                bottomHeader.add(comp);

            add(bottomHeader, BorderLayout.SOUTH);
        }

        // HEADER
        HEADER.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        if (backButtonMethod == null)
            HEADER.add(UiUtil.addCornerButton(backButtonTarget, "Back", this::clearResults));
        else
            HEADER.add(UiUtil.addCornerButton(backButtonTarget, "Back", backButtonMethod));
        add(HEADER, BorderLayout.NORTH);
        // SCROLL_PANE
        SCROLL_PANE.getVerticalScrollBar().setUnitIncrement(75);
        // LIST
        LIST.setLayout(new BoxLayout(LIST, BoxLayout.Y_AXIS));
        add(SCROLL_PANE, BorderLayout.CENTER);

        addThisPanel();
        UiUtil.showPanel(CNAME_LIST);
    }

    private void addThisPanel()
    {
        UiUtil.addPanel(this, CNAME_LIST);
    }
    public void clearResults()
    {
        if (searchBar != null)
            searchBar.setText("");
        LIST.removeAll();
        LIST.revalidate();
        LIST.repaint();
    }
    public JPanel getListPanel()
    {
        return LIST;
    }
    public JTextField getSearchBar()
    {
        return searchBar;
    }
}