package foodordering.util;

import foodordering.session.Session;
import foodordering.view.LoginView;
import javax.swing.*;
import java.awt.*;

public abstract class BaseView extends JFrame{

    protected abstract Color getBannerColor();

    protected abstract String getBannerTitle();

    protected abstract JPanel buildContent();

    protected void initUI(){
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel root=new JPanel(new BorderLayout());
        root.setBackground(Theme.BG);

        root.add(buildBanner(),BorderLayout.NORTH);
        root.add(buildContent(),BorderLayout.CENTER);

        setContentPane(root);
    }

    private JPanel buildBanner(){
        JPanel bar=new JPanel(new BorderLayout());

        bar.setBackground(getBannerColor());
        bar.setBorder(Theme.pad(10,18));

        JLabel title=new JLabel("  " + getBannerTitle());
        title.setFont(Theme.F_HEAD);
        title.setForeground(Color.WHITE);

        JButton logoutBtn=Theme.redBtn("Logout");

        logoutBtn.addActionListener(e -> {
            Session.logout();
            new LoginView().setVisible(true);
            dispose();
        });

        bar.add(title,BorderLayout.WEST);
        bar.add(logoutBtn,BorderLayout.EAST);

        return bar;
    }

    protected void showInfo(String msg,String title){
        JOptionPane.showMessageDialog(this,msg,title,JOptionPane.INFORMATION_MESSAGE);
    }

    protected void showError(String msg){
        JOptionPane.showMessageDialog(this,msg,"Error",JOptionPane.ERROR_MESSAGE);
    }

    protected void showWarning(String msg){
        JOptionPane.showMessageDialog(this,msg,"Validation",JOptionPane.WARNING_MESSAGE);
    }

    protected boolean confirm(String msg,String title){
        return JOptionPane.showConfirmDialog(this,msg,title,
            JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION;
    }
}