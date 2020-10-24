package uhk.fim.model;

public class ShoppingCartItem {
    private String name;
    private double pricePerPiece;
    private int pieces;
    private double totalPrice;

    public ShoppingCartItem(String name, double pricePerPiece, int pieces) {
        this.name = name;
        this.pricePerPiece = pricePerPiece;
        this.pieces = pieces;
    }

    public String getName() {
        return name;
    }


    public void setName(String name) {
        this.name = name;
    }

    public double getPricePerPiece() {
        return pricePerPiece;
    }

    public void setPricePerPiece(double pricePerPiece) {
        this.pricePerPiece = pricePerPiece;
    }

    public int getPieces() {
        return pieces;
    }

    public void setPieces(int pieces) {
        this.pieces = pieces;
    }

    public double getTotalPrice() {
        totalPrice = pricePerPiece * pieces;
        return totalPrice;
    }

    public void setTotalPrice(int totalPrice) {
        this.totalPrice = totalPrice;
    }
}