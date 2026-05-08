package foodordering.model;

public class CartItem implements Priceable{
    private final MenuItem item;
    private int quantity;

    public CartItem(MenuItem item,int quantity){
        this.item=item;
        this.quantity=quantity;
    }
    public MenuItem getItem(){ 
        return item; 
    }
    public int getQuantity(){ 
        return quantity;
    }
    public void setQuantity(int q){
        this.quantity=q;
    }

    @Override
    public double getPrice(){
        return item.getPrice();
    }

    public double getSubtotal(){
        return item.getPrice() * quantity;
    }

    @Override
    public String toString(){
        return item.getItemName() + " x" + quantity +
               "  =  Rs." + String.format("%.2f", getSubtotal());
    }
}