package uhk.fim.gui;

import uhk.fim.model.ShoppingCart;
import uhk.fim.model.ShoppingCartItem;

import javax.swing.table.AbstractTableModel;

public class ShoppingCartTableModel extends AbstractTableModel {
    // V modelu potřebujeme referenci na data
    private ShoppingCart shoppingCart;

    @Override
    public int getRowCount() {
        return shoppingCart.getItems().size();
    }

    @Override
    public int getColumnCount() {
        return 4;
    }

    // Tato metoda se volá, když se tabulka dotazuje hodnotu v buňce. Tedy pro kažkou buňku.
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        // Řádek tabulky (rowIndex) odpovídá pozici položky v seznamu položek
        ShoppingCartItem item = shoppingCart.getItems().get(rowIndex);
        // Podle indexu sloupce vracíme atribut položky
        return switch (columnIndex) {
            case 0 -> item.getName();
            case 1 -> item.getPricePerPiece();
            case 2 -> item.getPieces();
            case 3 -> item.getTotalPrice();
            default -> null;
        };
    }

    // Tato metoda se volá, když se tabulka dotazuje na názvy sloupců
    @Override
    public String getColumnName(int columnIndex) {
        // Podle indexu sloupce vracíme název
        return switch (columnIndex) {
            case 0 -> "Název";
            case 1 -> "Cena/kus";
            case 2 -> "Počet kusů";
            case 3 -> "Celkova cena";
            default -> null;
        };
    }

    // Tato metoda se volá, když se tabulka dotazuje "typ" sloupce
    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return switch (columnIndex) {
            case 0 -> String.class;
            case 1 -> Double.class;
            case 2 -> Integer.class;
            case 3 -> Double.class;
            default -> null;
        };
    }

    public void setShoppingCart(ShoppingCart shoppingCart) {
        this.shoppingCart = shoppingCart;
    }
}
