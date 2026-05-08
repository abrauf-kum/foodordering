package foodordering.view;

import foodordering.dao.OrderDAO;
import foodordering.model.Order;
import foodordering.util.BaseView;
import foodordering.util.Theme;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class AdminView extends BaseView{

    private final OrderDAO orderDAO=new OrderDAO();
    private DefaultTableModel model;
    private JLabel infoLbl;

    private static final String[] STATUSES={"Pending","Processing","Delivered","Cancelled"};

    public AdminView(){
        setTitle("Admin Panel  –  Food Ordering System");
        setSize(960,620);
        initUI();
        loadOrders();
    }

    @Override protected Color getBannerColor(){ return Theme.BLUE_DARK; }

    @Override protected String getBannerTitle(){ return "Admin Panel  –  Order Management"; }

    @Override
    protected JPanel buildContent(){
        JPanel root=new JPanel(new BorderLayout());
        root.setBackground(Theme.BG);

        String[] cols={"Order ID","Customer","Date & Time","Total (Rs.)","Status"};

        model=new DefaultTableModel(cols,0){
            public boolean isCellEditable(int r,int c){ return false; }
        };

        JTable table=Theme.styledTable(model);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        int[] w={80,165,165,115,115};
        for(int i=0;i<w.length;i++)
            table.getColumnModel().getColumn(i).setPreferredWidth(w[i]);

        JPanel action=new JPanel(new BorderLayout(10,0));
        action.setBackground(Theme.WHITE);
        action.setBorder(Theme.topLine());

        infoLbl=new JLabel("Select an order row to manage it");
        infoLbl.setFont(Theme.F_BODY);
        infoLbl.setForeground(Theme.MUTED);

        JComboBox<String> statusBox=new JComboBox<>(STATUSES);
        statusBox.setFont(Theme.F_BODY);
        statusBox.setPreferredSize(new Dimension(145,32));

        JButton updateBtn=Theme.blueBtn("Update Status");
        JButton viewBtn=Theme.blueBtn("View Items");
        JButton refreshBtn=Theme.greenBtn("Refresh");

        updateBtn.addActionListener(e->{
            int row=table.getSelectedRow();
            if(row<0){ showWarning("Select an order first."); return; }

            int oid=(int)model.getValueAt(row,0);
            String status=(String)statusBox.getSelectedItem();

            if(orderDAO.updateStatus(oid,status)){
                model.setValueAt(status,row,4);
                infoLbl.setText("Order #"+oid+"  →  "+status);
                infoLbl.setForeground(Theme.GREEN);
            }else showError("Status update failed.");
        });

        viewBtn.addActionListener(e->{
            int row=table.getSelectedRow();
            if(row<0){ showWarning("Select an order first."); return; }
            showOrderDetail((int)model.getValueAt(row,0));
        });

        refreshBtn.addActionListener(e->{
            loadOrders();
            infoLbl.setText("Refreshed.");
            infoLbl.setForeground(Theme.MUTED);
        });

        JPanel btns=new JPanel(new FlowLayout(FlowLayout.RIGHT,10,0));
        btns.setOpaque(false);

        btns.add(Theme.lbl("Set Status:"));
        btns.add(statusBox);
        btns.add(updateBtn);
        btns.add(viewBtn);
        btns.add(refreshBtn);

        action.add(infoLbl,BorderLayout.WEST);
        action.add(btns,BorderLayout.EAST);

        root.add(new JScrollPane(table),BorderLayout.CENTER);
        root.add(action,BorderLayout.SOUTH);

        return root;
    }

    private void loadOrders(){
        model.setRowCount(0);

        for(Order o:orderDAO.getAll())
            model.addRow(new Object[]{
                o.getId(),
                o.getUserName(),
                o.getOrderDate().toString().substring(0,16),
                String.format("%.2f",o.getTotalAmount()),
                o.getStatus()
            });
    }

    private void showOrderDetail(int orderId){
        List<String> lines=orderDAO.getOrderLines(orderId);

        if(lines.isEmpty()){ showWarning("No items found."); return; }

        StringBuilder sb=new StringBuilder("Order #"+orderId+"  –  Item Breakdown\n");
        sb.append("─".repeat(52)).append("\n");

        lines.forEach(l->sb.append(l).append("\n"));

        JTextArea a=new JTextArea(sb.toString());
        a.setEditable(false);
        a.setFont(Theme.F_MONO);
        a.setBorder(BorderFactory.createEmptyBorder(8,10,8,10));

        JOptionPane.showMessageDialog(this,new JScrollPane(a),
            "Order Items",JOptionPane.INFORMATION_MESSAGE);
    }
}