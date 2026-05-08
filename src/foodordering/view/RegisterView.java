package foodordering.view;

import foodordering.dao.UserDAO;
import foodordering.model.User;
import foodordering.util.Theme;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class RegisterView extends JFrame{

    private final JTextField nameFld=Theme.field(22);
    private final JTextField emailFld=Theme.field(22);
    private final JTextField phoneFld=Theme.field(22);
    private final JPasswordField passFld=Theme.passField(22);
    private final JPasswordField confirmFld=Theme.passField(22);
    private final JTextArea addrArea=makeArea();
    private final UserDAO userDAO=new UserDAO();
    private final JFrame parent;

    public RegisterView(JFrame parent){
        this.parent=parent;

        setTitle("Create Account – Food Ordering System");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(460,590);
        setLocationRelativeTo(parent);
        setResizable(false);

        setContentPane(build());
    }

    private JPanel build(){
        JPanel root=new JPanel(new BorderLayout());
        root.setBackground(Theme.BG);

        JPanel banner=new JPanel();
        banner.setBackground(Theme.BLUE);
        banner.setBorder(Theme.pad(16,20));

        JLabel title=new JLabel("Create New Account");
        title.setFont(Theme.F_HEAD);
        title.setForeground(Color.WHITE);

        banner.add(title);

        JPanel form=new JPanel(new GridBagLayout());
        form.setBackground(Theme.WHITE);
        form.setBorder(BorderFactory.createEmptyBorder(20,36,20,36));

        GridBagConstraints g=new GridBagConstraints();
        g.fill=GridBagConstraints.HORIZONTAL;
        g.weightx=1;
        g.gridx=0;

        addRow(form,g,0,"Full Name *",nameFld,10);
        addRow(form,g,1,"Email Address *",emailFld,8);
        addRow(form,g,2,"Phone Number",phoneFld,8);
        addRow(form,g,3,"Password *",passFld,8);
        addRow(form,g,4,"Confirm Password *",confirmFld,8);

        g.gridy=10;
        g.insets=new Insets(8,0,3,0);
        form.add(Theme.lbl("Address"),g);

        g.gridy=11;
        g.insets=new Insets(0,0,0,0);
        form.add(new JScrollPane(addrArea),g);

        JButton createBtn=Theme.greenBtn("Create Account");
        createBtn.setPreferredSize(new Dimension(0,38));
        createBtn.addActionListener(this::doRegister);

        g.gridy=12;
        g.insets=new Insets(18,0,4,0);
        form.add(createBtn,g);

        JPanel foot=new JPanel();
        foot.setBackground(Theme.BG);

        JButton back=new JButton("<< Back to Login");
        back.setFont(Theme.F_SMALL);
        back.setForeground(Theme.BLUE);
        back.setBorderPainted(false);
        back.setContentAreaFilled(false);
        back.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        back.addActionListener(e->{
            parent.setVisible(true);
            dispose();
        });

        foot.add(back);

        root.add(banner,BorderLayout.NORTH);
        root.add(new JScrollPane(form),BorderLayout.CENTER);
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

    private void doRegister(ActionEvent e){
        String name=nameFld.getText().trim();
        String email=emailFld.getText().trim();
        String phone=phoneFld.getText().trim();
        String pass=new String(passFld.getPassword()).trim();
        String confirm=new String(confirmFld.getPassword()).trim();
        String addr=addrArea.getText().trim();

        if(name.isEmpty()||email.isEmpty()||pass.isEmpty()){
            warn("Name, email, and password are required.");
            return;
        }

        if(!email.contains("@")||!email.contains(".")){
            warn("Enter a valid email address.");
            return;
        }

        if(pass.length()<4){
            warn("Password must be at least 4 characters.");
            return;
        }

        if(!pass.equals(confirm)){
            warn("Passwords do not match.");
            confirmFld.setText("");
            return;
        }

        if(userDAO.emailExists(email)){
            warn("This email is already registered.");
            return;
        }

        User newUser=new User(0,name,email,pass,phone,addr,"user");

        if(userDAO.register(newUser)){
            JOptionPane.showMessageDialog(this,
                "Account created! You can now log in.",
                "Success",
                JOptionPane.INFORMATION_MESSAGE);

            parent.setVisible(true);
            dispose();
        }else{
            JOptionPane.showMessageDialog(this,
                "Registration failed. Please try again.",
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private void warn(String msg){
        JOptionPane.showMessageDialog(this,msg,"Validation",JOptionPane.WARNING_MESSAGE);
    }

    private static JTextArea makeArea(){
        JTextArea a=new JTextArea(3,22);
        a.setFont(Theme.F_BODY);
        a.setLineWrap(true);
        a.setWrapStyleWord(true);
        a.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Theme.BORDER),
            BorderFactory.createEmptyBorder(4,7,4,7)
        ));
        return a;
    }
}