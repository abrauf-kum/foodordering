package foodordering.util;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class Theme{

    public static final Color BLUE=new Color(41,128,185);
    public static final Color BLUE_DARK=new Color(26,82,118);
    public static final Color GREEN=new Color(39,174,96);
    public static final Color RED=new Color(192,57,43);
    public static final Color BG=new Color(245,247,250);
    public static final Color WHITE=Color.WHITE;
    public static final Color TEXT=new Color(44,62,80);
    public static final Color MUTED=new Color(127,140,141);
    public static final Color BORDER=new Color(218,220,224);
    public static final Color ROW_SEL=new Color(210,230,255);

    public static final Font F_TITLE=new Font("Segoe UI",Font.BOLD,22);
    public static final Font F_HEAD=new Font("Segoe UI",Font.BOLD,15);
    public static final Font F_BODY=new Font("Segoe UI",Font.PLAIN,13);
    public static final Font F_SMALL=new Font("Segoe UI",Font.PLAIN,11);
    public static final Font F_BTN=new Font("Segoe UI",Font.BOLD,13);
    public static final Font F_MONO=new Font("Monospaced",Font.PLAIN,13);

    private Theme(){}

    public static Border pad(int v,int h){
        return BorderFactory.createEmptyBorder(v,h,v,h);
    }

    public static Border inputBorder(){
        return BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER,1,true),
            BorderFactory.createEmptyBorder(4,7,4,7)
        );
    }

    public static Border topLine(){
        return BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(1,0,0,0,BORDER),
            BorderFactory.createEmptyBorder(12,16,12,16)
        );
    }

    public static JButton btn(String text,Color bg){
        JButton b=new JButton(text);
        b.setFont(F_BTN);
        b.setBackground(bg);
        b.setForeground(WHITE);
        b.setFocusPainted(false);
        b.setBorderPainted(false);
        b.setOpaque(true);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return b;
    }

    public static JButton blueBtn(String t){ return btn(t,BLUE); }

    public static JButton greenBtn(String t){ return btn(t,GREEN); }

    public static JButton redBtn(String t){ return btn(t,RED); }

    public static JTextField field(int cols){
        JTextField f=new JTextField(cols);
        f.setFont(F_BODY);
        f.setBorder(inputBorder());
        return f;
    }

    public static JPasswordField passField(int cols){
        JPasswordField f=new JPasswordField(cols);
        f.setFont(F_BODY);
        f.setBorder(inputBorder());
        return f;
    }

    public static JLabel lbl(String text){
        JLabel l=new JLabel(text);
        l.setFont(F_BODY);
        l.setForeground(TEXT);
        return l;
    }

    public static JLabel muted(String text){
        JLabel l=new JLabel(text);
        l.setFont(F_SMALL);
        l.setForeground(MUTED);
        return l;
    }

    public static JTable styledTable(DefaultTableModel model){
        JTable t=new JTable(model);
        t.setFont(F_BODY);
        t.setRowHeight(28);
        t.setGridColor(BORDER);
        t.setSelectionBackground(ROW_SEL);
        t.setSelectionForeground(TEXT);
        t.setShowHorizontalLines(true);
        t.setShowVerticalLines(true);
        t.getTableHeader().setFont(F_HEAD);
        t.getTableHeader().setBackground(new Color(236,240,245));
        t.getTableHeader().setForeground(TEXT);
        t.getTableHeader().setReorderingAllowed(false);
        return t;
    }

    public static void apply(){
        try{
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        }catch(Exception ignored){}

        UIManager.put("Panel.background",BG);
        UIManager.put("OptionPane.background",BG);
        UIManager.put("Table.gridColor",BORDER);
        UIManager.put("Table.font",F_BODY);
        UIManager.put("TableHeader.font",F_HEAD);
        UIManager.put("Label.font",F_BODY);
        UIManager.put("Button.font",F_BTN);
        UIManager.put("TabbedPane.font",F_BTN);
    }
}