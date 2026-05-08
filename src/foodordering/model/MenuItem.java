package foodordering.model;

public class MenuItem extends Model implements Priceable{
    private int restaurantId;
    private String restaurantName;
    private String itemName;
    private double price;
    private String category;
    private String description;

    public MenuItem(){

    }
    public MenuItem(int id,int restaurantId,String restaurantName,String itemName,
        double price,String category,String description)
    {
        super(id);
        this.restaurantId=restaurantId;
        this.restaurantName=restaurantName;
        this.itemName=itemName;
        this.price=price;
        this.category=category;
        this.description=description;
    }
    public int getRestaurantId(){ 
        return restaurantId; 
    }
    public String getRestaurantName(){ 
        return restaurantName; 
    }
    public String getItemName(){ 
        return itemName; 
    }
    public String getCategory(){ 
        return category; 
    }
    public String getDescription(){ 
        return description; 
    }
    @Override
    public double getPrice(){
        return price;
    }
    @Override
    public String toString(){
        return itemName + "  " + formattedPrice();
    }
}