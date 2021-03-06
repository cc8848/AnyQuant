package ui.myui;

/**
 * Created by TOSHIBA on 2015/12/23.
 */

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateChooserJButton extends JLabel {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private DateChooser dateChooser = null;
    private String preLabel = "";
    public DateChooserJButton() {
        this(getNowDate());
    }
    public DateChooserJButton(Date date) {
        this("", date);
    }
    public DateChooserJButton(String preLabel, Date date) {

        if (preLabel != null)
            this.preLabel = preLabel;
        setDate(date);
        setBorder(null);
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        super.addMouseListener(new MouseListener() {
            public void mouseClicked(MouseEvent e) {
                // 处理鼠标点击
             if (dateChooser == null)
            dateChooser = new DateChooser();
            Point p = getLocationOnScreen();
            p.y = p.y + 30;
            dateChooser.showDateChooser(p);

            }
            public void mouseEntered(MouseEvent e) {
                // 处理鼠标移入
            }
            public void mouseExited(MouseEvent e) {
                // 处理鼠标离开
            }
            public void mousePressed(MouseEvent e) {
                // 处理鼠标按下
            }
            public void mouseReleased(MouseEvent e) {
                // 处理鼠标释放
            }
        });
    }
    private static Date getNowDate() {
        return Calendar.getInstance().getTime();
    }
    private static SimpleDateFormat getDefaultDateFormat() {
        return new SimpleDateFormat("yyyy-MM-dd"); //按钮显示的日期格式
    }
    //覆盖父类的方法
    public void setText(String s) {
        Date date;
        try {
            date = getDefaultDateFormat().parse(s);
        } catch (ParseException e) {
            date = getNowDate();
        }
        setDate(date);
    }

    public void setDate(Date date) {
        super.setText(preLabel + getDefaultDateFormat().format(date));
    }
    public Date getDate() {
        String dateString = getText().substring(preLabel.length());
        try {
            return getDefaultDateFormat().parse(dateString);
        } catch (ParseException e) {
            return getNowDate();
        }
    }

    private class DateChooser extends JPanel implements ActionListener,
            ChangeListener {
        /**
         *
         */
        private static final long serialVersionUID = 1L;
        int startYear = 2013; //默认【最小】显示年份
        int lastYear = 2020; //默认【最大】显示年份
        int width = 220; //界面宽度
        int height = 190; //界面高度
        Color backGroundColor = Color.gray; //底色
        //月历表格配色----------------//
        Color palletTableColor = Color.white; //日历表底色
        Color todayBackColor = Color.orange; //今天背景色
        Color weekFontColor = Color.black; //星期文字色
        Color dateFontColor = Color.black; //日期文字色
        Color weekendFontColor = Color.black; //周末文字色
        //控制条配色------------------//
        Color controlLineColor = Color.pink; //控制条底色
        Color controlTextColor = Color.white; //控制条标签文字色
        JDialog dialog;
        JSpinner yearSpin;
        JSpinner monthSpin;
        JSpinner hourSpin;
        JButton[][] daysButton = new JButton[6][7];
        DateChooser() {
            setLayout(new BorderLayout());
            setBorder(new LineBorder(backGroundColor, 2));
            setBackground(backGroundColor);
            JPanel topYearAndMonth = createYearAndMonthPanal();
            add(topYearAndMonth, BorderLayout.NORTH);
            JPanel centerWeekAndDay = createWeekAndDayPanal();
            add(centerWeekAndDay, BorderLayout.CENTER);
        }
        private JPanel createYearAndMonthPanal() {
            Calendar c = getCalendar();
            int currentYear = c.get(Calendar.YEAR);
            int currentMonth = c.get(Calendar.MONTH) + 1;

            JPanel result = new JPanel();
            result.setLayout(new FlowLayout());
            result.setBackground(controlLineColor);
            yearSpin = new JSpinner(new SpinnerNumberModel(currentYear,
                    startYear, lastYear, 1));
            yearSpin.setPreferredSize(new Dimension(48, 20));
            yearSpin.setName("Year");
            yearSpin.setEditor(new JSpinner.NumberEditor(yearSpin, "####"));
            yearSpin.addChangeListener(this);
            result.add(yearSpin);
            JLabel yearLabel = new JLabel("年");
            yearLabel.setForeground(controlTextColor);
            result.add(yearLabel);
            monthSpin = new JSpinner(new SpinnerNumberModel(currentMonth, 1,
                    12, 1));
            monthSpin.setPreferredSize(new Dimension(35, 20));
            monthSpin.setName("Month");
            monthSpin.addChangeListener(this);
            result.add(monthSpin);
            JLabel monthLabel = new JLabel("月");
            monthLabel.setForeground(controlTextColor);
            result.add(monthLabel);

            return result;
        }
        private JPanel createWeekAndDayPanal() {
            String colname[] = { "日", "一", "二", "三", "四", "五", "六" };
            JPanel result = new JPanel();
            //设置固定字体，以免调用环境改变影响界面美观
            result.setFont(new Font("雅黑", Font.PLAIN, 12));
            result.setLayout(new GridLayout(7, 7));
            result.setBackground(Color.white);
            JLabel cell;
            for (int i = 0; i < 7; i++) {
                cell = new JLabel(colname[i]);
                cell.setHorizontalAlignment(JLabel.CENTER);
                if (i == 0 || i == 6)
                    cell.setForeground(weekendFontColor);
                else
                    cell.setForeground(weekFontColor);
                result.add(cell);
            }
            int actionCommandId = 0;
            for (int i = 0; i < 6; i++)
                for (int j = 0; j < 7; j++) {
                    JButton numberButton = new JButton();
                    numberButton.setBorder(null);
                    numberButton.setHorizontalAlignment(SwingConstants.CENTER);
                    numberButton.setActionCommand(String
                            .valueOf(actionCommandId));
                    numberButton.addActionListener(this);
                    numberButton.setBackground(palletTableColor);
                    numberButton.setForeground(dateFontColor);
                    if (j == 0 || j == 6)
                        numberButton.setForeground(weekendFontColor);
                    else
                        numberButton.setForeground(dateFontColor);
                    daysButton[i][j] = numberButton;
                    result.add(numberButton);
                    actionCommandId++;
                }
            return result;
        }
        private JDialog createDialog(Frame owner) {
            JDialog result = new JDialog(owner, "", true);
            result.setUndecorated(true);
            result.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            result.getContentPane().add(this, BorderLayout.CENTER);
            result.pack();
            result.setSize(width, height);
            return result;
        }
        @SuppressWarnings("deprecation")
        void showDateChooser(Point position) {
            Frame owner = (Frame) SwingUtilities
                    .getWindowAncestor(DateChooserJButton.this);
            if (dialog == null || dialog.getOwner() != owner)
                dialog = createDialog(owner);
            dialog.setLocation(getAppropriateLocation(owner, position));
            flushWeekAndDay();
            dialog.show();
        }
        Point getAppropriateLocation(Frame owner, Point position) {
            Point result = new Point(position);
            Point p = owner.getLocation();
            int offsetX = (position.x + width) - (p.x + owner.getWidth());
            int offsetY = (position.y + height) - (p.y + owner.getHeight());
            if (offsetX > 0) {
                result.x -= offsetX;
            }
            if (offsetY > 0) {
                result.y -= offsetY;
            }
            return result;
        }
        private Calendar getCalendar() {
            Calendar result = Calendar.getInstance();
            result.setTime(getDate());
            return result;
        }
        private int getSelectedYear() {
            return ((Integer) yearSpin.getValue()).intValue();
        }
        private int getSelectedMonth() {
            return ((Integer) monthSpin.getValue()).intValue();
        }
        private int getSelectedHour() {
            return ((Integer) hourSpin.getValue()).intValue();
        }
        private void dayColorUpdate(boolean isOldDay) {
            Calendar c = getCalendar();
            int day = c.get(Calendar.DAY_OF_MONTH);
            c.set(Calendar.DAY_OF_MONTH, 1);
            int actionCommandId = day - 2 + c.get(Calendar.DAY_OF_WEEK);
            int i = actionCommandId / 7;
            int j = actionCommandId % 7;
            if (isOldDay)
                daysButton[i][j].setForeground(dateFontColor);
            else
                daysButton[i][j].setForeground(todayBackColor);
        }
        private void flushWeekAndDay() {
            Calendar c = getCalendar();
            c.set(Calendar.DAY_OF_MONTH, 1);
            int maxDayNo = c.getActualMaximum(Calendar.DAY_OF_MONTH);
            int dayNo = 2 - c.get(Calendar.DAY_OF_WEEK);
            for (int i = 0; i < 6; i++) {
                for (int j = 0; j < 7; j++) {
                    String s = "";
                    if (dayNo >= 1 && dayNo <= maxDayNo)
                        s = String.valueOf(dayNo);
                    daysButton[i][j].setText(s);
                    dayNo++;
                }
            }
            dayColorUpdate(false);
        }
        public void stateChanged(ChangeEvent e) {
            JSpinner source = (JSpinner) e.getSource();
            Calendar c = getCalendar();
            if (source.getName().equals("Hour")) {
                c.set(Calendar.HOUR_OF_DAY, getSelectedHour());
                setDate(c.getTime());
                return;
            }
            dayColorUpdate(true);
            if (source.getName().equals("Year"))
                c.set(Calendar.YEAR, getSelectedYear());
            else
                // (source.getName().equals("Month"))
                c.set(Calendar.MONTH, getSelectedMonth() - 1);
            setDate(c.getTime());
            flushWeekAndDay();
        }
        public void actionPerformed(ActionEvent e) {
            JButton source = (JButton) e.getSource();
            if (source.getText().length() == 0)
                return;
            dayColorUpdate(true);
            source.setForeground(todayBackColor);
            int newDay = Integer.parseInt(source.getText());
            Calendar c = getCalendar();
            c.set(Calendar.DAY_OF_MONTH, newDay);
            setDate(c.getTime());
            dialog.dispose();

        }
    }

}
