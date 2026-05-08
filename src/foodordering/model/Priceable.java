package foodordering.model;

public interface Priceable{
    double getPrice();

    default String formattedPrice(){
        return String.format("Rs. %.2f", getPrice());
    }
}
