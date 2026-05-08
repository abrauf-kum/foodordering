package foodordering.view;

import foodordering.dao.MenuDAO;
import foodordering.dao.OrderDAO;
import foodordering.model.CartItem;
import foodordering.model.MenuItem;
import foodordering.model.Order;
import foodordering.session.Session;
import foodordering.util.BaseView;
import foodordering.util.Theme;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class MainView extends BaseView{

    private final MenuDAO menuDAO=new MenuDAO();
    private final OrderDAO orderDAO=new OrderDAO();

    private final List<CartItem> cart=new ArrayList<>();
    private List<MenuItem> menuRows=new ArrayList<>();

    private DefaultTableModel menuModel,cartModel,ordersModel;
    private JTable menuTable;
    private JComboBox<String> catBox;
    private JLabel totalLbl;
    private JTabbedPane tabs;

    public MainView(){
        setTitle("Food Ordering System  –  "+Session.getUser().getName());
        setSize(980,660);
        initUI();
        loadMenu(null);
    }

    @Override protected Color getBannerColor(){ return Theme.BLUE; }

    @Override protected String getBannerTitle(){ return "Hello, "+Session.getUser().getName(); }

    @Override
    protected JPanel buildContent(){
        JPanel wrapper=new JPanel(new BorderLayout());
        wrapper.setBackground(Theme.BG);

        tabs=new JTabbedPane();
        tabs.setFont(Theme.F_BTN);

        tabs.addTab("   Menu   ",buildMenuTab());
        tabs.addTab("   Cart   ",buildCartTab());
        tabs.addTab("   My Orders   ",buildOrdersTab());

        tabs.addChangeListener(e->{
            if(tabs.getSelectedIndex()==1) refreshCart();
            if(tabs.getSelectedIndex()==2) refreshOrders();
        });

        wrapper.add(tabs,BorderLayout.CENTER);
        return wrapper;
    }

    private JPanel buildMenuTab(){
        JPanel p=new JPanel(new BorderLayout());
        p.setBackground(Theme.BG);

        JPanel bar=new JPanel(new FlowLayout(FlowLayout.LEFT,14,10));
        bar.setBackground(Theme.WHITE);
        bar.setBorder(BorderFactory.createMatteBorder(0,0,1,0,Theme.BORDER));

        bar.add(Theme.lbl("Category:"));

        catBox=new JComboBox<>();
        catBox.setFont(Theme.F_BODY);

        menuDAO.getCategories().forEach(catBox::addItem);

        catBox.addActionListener(e->{
            String s=(String)catBox.getSelectedItem();
            loadMenu("All".equals(s)?null:s);
        });

        bar.add(catBox);
        bar.add(Box.createHorizontalStrut(28));

        JButton addBtn=Theme.blueBtn("+ Add to Cart");
        addBtn.setPreferredSize(new Dimension(135,32));
        addBtn.addActionListener(e->addToCart());
        bar.add(addBtn);

        String[] cols={"#","Restaurant","Item Name","Category","Price (Rs.)","Description"};

        menuModel=new DefaultTableModel(cols,0){
            public boolean isCellEditable(int r,int c){ return false; }
        };

        menuTable=Theme.styledTable(menuModel);
        menuTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        int[] w={40,140,170,100,95,290};
        for(int i=0;i<w.length;i++)
            menuTable.getColumnModel().getColumn(i).setPreferredWidth(w[i]);

        menuTable.addMouseListener(new MouseAdapter(){
            public void mouseClicked(MouseEvent e){
                if(e.getClickCount()==2) addToCart();
            }
        });

        JLabel tip=Theme.muted("  Tip: double-click a row, or select and click '+ Add to Cart'");
        tip.setBorder(Theme.pad(5,4));

        p.add(bar,BorderLayout.NORTH);
        p.add(new JScrollPane(menuTable),BorderLayout.CENTER);
        p.add(tip,BorderLayout.SOUTH);

        return p;
    }

    private void loadMenu(String category){
        menuRows=(category==null)?menuDAO.getAllItems():menuDAO.getByCategory(category);
        menuModel.setRowCount(0);

        for(MenuItem m:menuRows)
            menuModel.addRow(new Object[]{
                m.getId(),
                m.getRestaurantName(),
                m.getItemName(),
                m.getCategory(),
                String.format("%.2f",m.getPrice()),
                m.getDescription()
            });
    }

    private void addToCart(){
        int row=menuTable.getSelectedRow();
        if(row<0){ showInfo("Please select a menu item first.","No Selection"); return; }

        MenuItem item=menuRows.get(menuTable.convertRowIndexToModel(row));

        String input=JOptionPane.showInputDialog(this,
            "Quantity for:  "+item.getItemName(),
            "Add to Cart",
            JOptionPane.QUESTION_MESSAGE);

        if(input==null||input.trim().isEmpty()) return;

        int qty;
        try{ qty=Integer.parseInt(input.trim()); }
        catch(NumberFormatException ex){ qty=0; }

        if(qty<=0){ showWarning("Enter a valid quantity (1 or more)."); return; }

        for(CartItem ci:cart){
            if(ci.getItem().getId()==item.getId()){
                ci.setQuantity(ci.getQuantity()+qty);
                showInfo(item.getItemName()+" updated. New qty: "+ci.getQuantity(),"Cart Updated");
                return;
            }
        }

        cart.add(new CartItem(item,qty));
        showInfo(item.getItemName()+" x"+qty+"  added to cart!","Added");
    }

    private JPanel buildCartTab(){
        JPanel p=new JPanel(new BorderLayout());
        p.setBackground(Theme.BG);

        String[] cols={"Item Name","Unit Price (Rs.)","Qty","Subtotal (Rs.)"};

        cartModel=new DefaultTableModel(cols,0){
            public boolean isCellEditable(int r,int c){ return false; }
        };

        JTable ct=Theme.styledTable(cartModel);
        ct.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JPanel bot=new JPanel(new BorderLayout(10,0));
        bot.setBackground(Theme.WHITE);
        bot.setBorder(Theme.topLine());

        totalLbl=new JLabel("Total:  Rs. 0.00");
        totalLbl.setFont(new Font("Segoe UI",Font.BOLD,17));
        totalLbl.setForeground(Theme.BLUE_DARK);

        JButton removeBtn=Theme.redBtn("Remove Item");
        JButton clearBtn=Theme.redBtn("Clear Cart");
        JButton placeBtn=Theme.greenBtn("Place Order  >>");
        placeBtn.setPreferredSize(new Dimension(155,34));

        removeBtn.addActionListener(e->{
            int r=ct.getSelectedRow();
            if(r<0){ showWarning("Select an item to remove."); return; }
            cart.remove(r);
            refreshCart();
        });

        clearBtn.addActionListener(e->{
            if(!cart.isEmpty()&&confirm("Clear all items?","Confirm")){
                cart.clear();
                refreshCart();
            }
        });

        placeBtn.addActionListener(e->placeOrder());

        JPanel btns=new JPanel(new FlowLayout(FlowLayout.RIGHT,10,0));
        btns.setOpaque(false);

        btns.add(removeBtn);
        btns.add(clearBtn);
        btns.add(placeBtn);

        bot.add(totalLbl,BorderLayout.WEST);
        bot.add(btns,BorderLayout.EAST);

        p.add(new JScrollPane(ct),BorderLayout.CENTER);
        p.add(bot,BorderLayout.SOUTH);

        return p;
    }

    private void refreshCart(){
        cartModel.setRowCount(0);

        double total=0;

        for(CartItem ci:cart){
            double sub=ci.getSubtotal();
            total+=sub;

            cartModel.addRow(new Object[]{
                ci.getItem().getItemName(),
                String.format("%.2f",ci.getPrice()),
                ci.getQuantity(),
                String.format("%.2f",sub)
            });
        }

        totalLbl.setText(String.format("Total:  Rs. %.2f",total));
    }

    private void placeOrder(){
        if(cart.isEmpty()){ showInfo("Cart is empty.","Empty Cart"); return; }

        double total=cart.stream().mapToDouble(CartItem::getSubtotal).sum();

        if(!confirm(String.format("Place order for Rs. %.2f  (%d item(s))?",total,cart.size()),
            "Confirm Order")) return;

        int id=orderDAO.placeOrder(Session.getUser().getId(),cart,total);

        if(id>0){
            cart.clear();
            refreshCart();

            showInfo("Order #"+id+" placed!\nStatus: Pending  –  Thank you!","Order Placed");

            tabs.setSelectedIndex(2);
        }else showError("Order failed – check your database connection.");
    }

    private JPanel buildOrdersTab(){
        JPanel p=new JPanel(new BorderLayout());
        p.setBackground(Theme.BG);

        String[] cols={"Order ID","Date & Time","Total (Rs.)","Status"};

        ordersModel=new DefaultTableModel(cols,0){
            public boolean isCellEditable(int r,int c){ return false; }
        };

        JTable ot=Theme.styledTable(ordersModel);
        ot.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        int[] w={80,180,120,120};
        for(int i=0;i<w.length;i++)
            ot.getColumnModel().getColumn(i).setPreferredWidth(w[i]);

        ot.addMouseListener(new MouseAdapter(){
            public void mouseClicked(MouseEvent e){
                if(e.getClickCount()==2){
                    int r=ot.getSelectedRow();
                    if(r>=0)
                        showOrderDetail((int)ordersModel.getValueAt(r,0));
                }
            }
        });

        JPanel bot=new JPanel(new FlowLayout(FlowLayout.RIGHT,12,8));
        bot.setBackground(Theme.WHITE);
        bot.setBorder(Theme.topLine());

        bot.add(Theme.muted("Double-click a row to view order items"));

        JButton refresh=Theme.blueBtn("Refresh");
        refresh.addActionListener(e->refreshOrders());
        bot.add(refresh);

        p.add(new JScrollPane(ot),BorderLayout.CENTER);
        p.add(bot,BorderLayout.SOUTH);

        return p;
    }

    private void refreshOrders(){
        ordersModel.setRowCount(0);

        for(Order o:orderDAO.getByUser(Session.getUser().getId()))
            ordersModel.addRow(new Object[]{
                o.getId(),
                o.getOrderDate().toString().substring(0,16),
                String.format("%.2f",o.getTotalAmount()),
                o.getStatus()
            });
    }

    private void showOrderDetail(int orderId){
        List<String> lines=orderDAO.getOrderLines(orderId);

        if(lines.isEmpty()){ showWarning("No items found."); return; }

        StringBuilder sb=new StringBuilder("Order #"+orderId+"  –  Items\n");
        sb.append("─".repeat(50)).append("\n");

        lines.forEach(l->sb.append(l).append("\n"));

        JTextArea a=new JTextArea(sb.toString());
        a.setEditable(false);
        a.setFont(Theme.F_MONO);
        a.setBorder(BorderFactory.createEmptyBorder(8,10,8,10));

        JOptionPane.showMessageDialog(this,new JScrollPane(a),
            "Order Details",JOptionPane.INFORMATION_MESSAGE);
    }
}