package foodordering.model;

import java.sql.Timestamp;

public class Order extends Model{
    private int userId;
    private String userName;
    private Timestamp orderDate;
    private double totalAmount;
    private String status;

    public Order(){

    }
    public Order(int id,int userId,String userName,Timestamp orderDate,
        double totalAmount,String status)
    {
        super(id);
        this.userId=userId;
        this.userName=userName;
        this.orderDate=orderDate;
        this.totalAmount=totalAmount;
        this.status=status;
    }
    public int getUserId(){ 
        return userId; 
    }
    public String getUserName(){ 
        return userName; 
    }
    public Timestamp getOrderDate(){ 
        return orderDate; 
    }
    public double getTotalAmount(){ 
        return totalAmount; 
    }
    public String getStatus(){ 
        return status; 
    }

    public void setStatus(String s){
        this.status=s;
    }

    @Override
    public String toString(){
        return "Order #" + getId() + " by " + userName +
               "  Rs." + String.format("%.2f", totalAmount) +
               "  [" + status + "]";
    }
}