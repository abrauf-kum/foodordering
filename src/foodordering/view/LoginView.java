package foodordering.view;

import foodordering.dao.UserDAO;
import foodordering.model.User;
import foodordering.session.Session;
import foodordering.util.Theme;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class LoginView extends JFrame{

    private final JTextField emailFld=Theme.field(22);
    private final JPasswordField passFld=Theme.passField(22);
    private final UserDAO userDAO=new UserDAO();

    public LoginView(){
        setTitle("Food Ordering System – Login");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(430,500);
        setLocationRelativeTo(null);
        setResizable(false);

        setContentPane(build());
        passFld.addActionListener(this::doLogin);
    }

    private JPanel build(){
        JPanel root=new JPanel(new BorderLayout());
        root.setBackground(Theme.BG);

        JPanel banner=new JPanel(new GridLayout(2,1,0,5));
        banner.setBackground(Theme.BLUE);
        banner.setBorder(Theme.pad(22,30));

        JLabel h1=new JLabel("Food Ordering System",SwingConstants.CENTER);
        h1.setFont(Theme.F_TITLE);
        h1.setForeground(Color.WHITE);

        JLabel h2=new JLabel("Sign in to your account",SwingConstants.CENTER);
        h2.setFont(Theme.F_SMALL);
        h2.setForeground(new Color(200,220,255));

        banner.add(h1);
        banner.add(h2);

        JPanel card=new JPanel(new GridBagLayout());
        card.setBackground(Theme.WHITE);
        card.setBorder(BorderFactory.createEmptyBorder(32,46,32,46));

        GridBagConstraints g=new GridBagConstraints();
        g.fill=GridBagConstraints.HORIZONTAL;
        g.weightx=1;
        g.gridx=0;

        JButton loginBtn=Theme.blueBtn("Login");
        loginBtn.setPreferredSize(new Dimension(0,38));
        loginBtn.addActionListener(this::doLogin);

        JButton regBtn=Theme.greenBtn("Create Account");
        regBtn.setPreferredSize(new Dimension(0,38));
        regBtn.addActionListener(e->{
            new RegisterView(this).setVisible(true);
            setVisible(false);
        });

        addRow(card,g,0,"Email Address",emailFld,8);
        addRow(card,g,1,"Password",passFld,10);

        g.gridy=4;
        g.insets=new Insets(24,0,6,0);
        card.add(loginBtn,g);

        g.gridy=5;
        g.insets=new Insets(8,0,0,0);
        card.add(regBtn,g);

        JPanel foot=new JPanel();
        foot.setBackground(Theme.BG);
        foot.add(Theme.muted("admin@food.com / admin123    |    ali@example.com / user123"));

        root.add(banner,BorderLayout.NORTH);
        root.add(card,BorderLayout.CENTER);
        root.add(foot,BorderLayout.SOUTH);

        return root;
    }

    private void addRow(JPanel p,GridBagConstraints g,int idx,
                        String label,JComponent field,int top){

        g.gridy=idx*2;
        g.insets=new Insets(top,0,3,0);
        p.add(Theme.lbl(label),g);

        g.gridy=idx*2+1;
        g.insets=new Insets(0,0,0,0);
        p.add(field,g);
    }

    private void doLogin(ActionEvent e){
        String email=emailFld.getText().trim();
        String pass=new String(passFld.getPassword()).trim();

        if(email.isEmpty()||pass.isEmpty()){
            JOptionPane.showMessageDialog(this,
                "Please enter email and password.",
                "Required",
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        User u=userDAO.login(email,pass);

        if(u==null){
            JOptionPane.showMessageDialog(this,
                "Incorrect email or password.",
                "Login Failed",
                JOptionPane.ERROR_MESSAGE);

            passFld.setText("");
            return;
        }

        Session.login(u);
        dispose();

        if(Session.isAdmin()) new AdminView().setVisible(true);
        else new MainView().setVisible(true);
    }
}