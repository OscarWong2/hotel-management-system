import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.YearMonth;
import javax.swing.*;

public class ScreenUtilCalendar extends JPanel
{
    // CNAME = CardLayout Names
    public static final String CNAME_CALENDAR = "calendar";


    private final LocalDate START_DATE;
    private final Integer MONTH_LIMIT; // Integer since can be null
    private final JButton DATE_BUTTON_REF;
    private final String RETURN_PANEL;

    private final JPanel HEADER;
    private final JPanel DAYS;

    public ScreenUtilCalendar(LocalDate newStartDate,
                                Integer newMonthLimit,
                                JButton newDateButtonRef,
                                String newReturnPanel)
    {
        START_DATE = newStartDate;
        if (newMonthLimit == null)
            MONTH_LIMIT = 12;
        else
            MONTH_LIMIT = newMonthLimit;
        DATE_BUTTON_REF = newDateButtonRef;
        RETURN_PANEL = newReturnPanel;

        HEADER = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        DAYS = new JPanel(new GridBagLayout());

        // Initialize layout

        setLayout(new BorderLayout());

        // HEADER
        HEADER.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        HEADER.add(UiUtil.addCornerButton(RETURN_PANEL, "Back", null));
        add(HEADER, BorderLayout.NORTH);
        // DAYS
        add(DAYS, BorderLayout.CENTER);

        addThisPanel();
        UiUtil.showPanel(CNAME_CALENDAR);

        initMonthDays(START_DATE);
    }

    private void initMonthDays(LocalDate date)
    {
        int currentMonth = date.getMonthValue();
        int startDay = date.getDayOfMonth();
        int currentYear = date.getYear();

        JLabel monthLabel = new JLabel(date.getMonth().name());
        JButton prevMonth = new JButton("←");
        JButton nextMonth = new JButton("→");
        JLabel sundayLabel = new JLabel("Su");
        JLabel mondayLabel = new JLabel("M");
        JLabel tuesdayLabel = new JLabel("Tu");
        JLabel wednesdayLabel = new JLabel("W");
        JLabel thursdayLabel = new JLabel("Th");
        JLabel fridayLabel = new JLabel("F");
        JLabel saturdayLabel = new JLabel("Sa");

        GridBagConstraints layout = new GridBagConstraints();

        class changeMonthListener implements ActionListener
        {
            @Override
            public void actionPerformed(ActionEvent event)
            {
                LocalDate newMonth = date.withDayOfMonth(1);
                if (event.getSource() == nextMonth)
                    newMonth = newMonth.plusMonths(1);
                else
                {
                    newMonth = newMonth.minusMonths(1);
                    if (newMonth.isBefore(START_DATE))
                        newMonth = START_DATE;
                }
                initMonthDays(newMonth);
            }
        }
        class daySelectedListener implements ActionListener
        {
            private final int DAY;

            public daySelectedListener(int newDay)
            {
                DAY = newDay;
            }
            @Override
            public void actionPerformed(ActionEvent event)
            {
                DATE_BUTTON_REF.setText(UiUtil.formatDate(UiUtil.createStringDate(currentYear, currentMonth, DAY)));
                UiUtil.showPanel(RETURN_PANEL);
                removeThisPanel();
            }
        }

        DAYS.removeAll();

        layout.gridx = date.getDayOfWeek().getValue() + 1;
        // Make Sunday the first day
        if (layout.gridx == 8)
            layout.gridx = 1;
        layout.gridy = 2;

        for (int i = startDay; i <= YearMonth.of(currentYear, currentMonth).lengthOfMonth(); i++)
        {
            if (layout.gridx == 8)
            {
                layout.gridx = 1;
                layout.gridy++;
            }

            JButton day = new JButton();
            if (i < 10)
                day.setText(" " + Integer.toString(i));
            else
                day.setText(Integer.toString(i));
            UiUtil.applyButtonLook(day);
            day.setFont(new Font("Monospaced", Font.PLAIN, 50));
            day.addActionListener(new daySelectedListener(i));
            DAYS.add(day, layout);

            layout.gridx++;
        }

        // MONTH_LABEL
        layout.gridx = 4;
        layout.gridy = 0;
        monthLabel.setFont(new Font("Tahoma", Font.PLAIN, 50));
        monthLabel.setText(date.getMonth().name() + " " + currentYear);
        DAYS.add(monthLabel, layout);
        // PREV_MONTH
        layout.gridx = 0;
        layout.gridy = 1;
        UiUtil.applyButtonLook(prevMonth);
        prevMonth.setFont(new Font("Tahoma", Font.PLAIN, 50));
        prevMonth.addActionListener(new changeMonthListener());
        DAYS.add(prevMonth, layout);
        if (date.minusMonths(1).withDayOfMonth(1).isBefore(START_DATE.withDayOfMonth(1)))
            prevMonth.setVisible(false);
        else
            prevMonth.setVisible(true);
        // NEXT_MONTH
        layout.gridx = 8;
        layout.gridy = 1;
        UiUtil.applyButtonLook(nextMonth);
        nextMonth.setFont(new Font("Tahoma", Font.PLAIN, 50));
        nextMonth.addActionListener(new changeMonthListener());
        DAYS.add(nextMonth, layout);
        if (date.plusMonths(1).withDayOfMonth(1).isAfter(START_DATE.plusMonths(MONTH_LIMIT).withDayOfMonth(1)))
            nextMonth.setVisible(false);
        else
            nextMonth.setVisible(true);
        // Day labels
        layout.gridx = 1;
        layout.gridy = 1;
        sundayLabel.setFont(new Font("Monospaced", Font.PLAIN, 50));
        DAYS.add(sundayLabel, layout);
        layout.gridx = 2;
        layout.gridy = 1;
        mondayLabel.setFont(new Font("Monospaced", Font.PLAIN, 50));
        DAYS.add(mondayLabel, layout);
        layout.gridx = 3;
        layout.gridy = 1;
        tuesdayLabel.setFont(new Font("Monospaced", Font.PLAIN, 50));
        DAYS.add(tuesdayLabel, layout);
        layout.gridx = 4;
        layout.gridy = 1;
        wednesdayLabel.setFont(new Font("Monospaced", Font.PLAIN, 50));
        DAYS.add(wednesdayLabel, layout);
        layout.gridx = 5;
        layout.gridy = 1;
        thursdayLabel.setFont(new Font("Monospaced", Font.PLAIN, 50));
        DAYS.add(thursdayLabel, layout);
        layout.gridx = 6;
        layout.gridy = 1;
        fridayLabel.setFont(new Font("Monospaced", Font.PLAIN, 50));
        DAYS.add(fridayLabel, layout);
        layout.gridx = 7;
        layout.gridy = 1;
        saturdayLabel.setFont(new Font("Monospaced", Font.PLAIN, 50));
        DAYS.add(saturdayLabel, layout);

        DAYS.revalidate();
        DAYS.repaint();
    }
    private void addThisPanel()
    {
        UiUtil.addPanel(this, CNAME_CALENDAR);
    }
    private void removeThisPanel()
    {
        UiUtil.removePanel(this);
    }
}