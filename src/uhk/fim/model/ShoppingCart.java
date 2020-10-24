package uhk.fim.model;

import java.util.ArrayList;
import java.util.List;

public class ShoppingCart {
    private List<ShoppingCartItem> items;

    public ShoppingCart() {
        this.items = new ArrayList<>();
    }

    public List<ShoppingCartItem> getItems() {
        return items;
    }

    public void addItem(ShoppingCartItem item){
        // Check if item already exists
        String addName = item.getName();
        for (ShoppingCartItem i : items) {
            if(i.getName().equals(addName)){
                //check if pricePerPiece is the same
                if(i.getPricePerPiece() == item.getPricePerPiece()){
                    i.setPieces(i.getPieces()+item.getPieces());
                }else{
                    items.add(item);
                }
                // return early
                return;
            }
        }
        // if item does not yet exist, add it to the list
        items.add(item);
    }

    public double getTotalPrice(){
        double sum = 0;
        for (ShoppingCartItem item : items) {
            double price = item.getPieces() * item.getPricePerPiece();
            sum+=price;
        }
        return sum;
    }
}
