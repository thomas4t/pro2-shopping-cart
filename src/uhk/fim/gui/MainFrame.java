package uhk.fim.gui;

import uhk.fim.model.ShoppingCart;
import uhk.fim.model.ShoppingCartItem;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class MainFrame extends JFrame implements ActionListener {
    MainFrame mainFrame;

    JButton btnInputAdd;
    JTextField txtInputName, txtInputPricePerPiece;
    JSpinner spInputPieces;

    ShoppingCart shoppingCart;
    ShoppingCartTableModel model;
    JLabel lblTotalPrice;

    public MainFrame(int width, int height) {
        super("PRO2 - Shopping cart");
        setSize(800, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        shoppingCart = new ShoppingCart();
        model = new ShoppingCartTableModel();
        model.setShoppingCart(shoppingCart);
        initGUI();
    }
    public void initGUI(){
        JPanel panelMain = new JPanel(new BorderLayout());

        JPanel panelInputs = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JPanel panelTable = new JPanel();
        JPanel panelFooter = new JPanel();

        JLabel lblInputName = new JLabel("Nazev: ");
        txtInputName = new JTextField("",15);
        JLabel lblPricePerPiece = new JLabel("Cena/kus ");
        txtInputPricePerPiece = new JTextField("",5);
        JLabel lblInputPieces = new JLabel("Pocet kusu: ");
        spInputPieces = new JSpinner(new SpinnerNumberModel(1,1,999,1));

        btnInputAdd = new JButton("Pridat ");
        btnInputAdd.addActionListener(this);


        panelInputs.add(lblInputName);
        panelInputs.add(txtInputName);
        panelInputs.add(lblPricePerPiece);
        panelInputs.add(txtInputPricePerPiece);
        panelInputs.add(lblInputPieces);
        panelInputs.add(spInputPieces);
        panelInputs.add(btnInputAdd);
        panelMain.add(panelInputs, BorderLayout.NORTH);
        panelMain.add(panelTable, BorderLayout.CENTER);
        panelMain.add(panelFooter, BorderLayout.SOUTH);

        JTable table = new JTable();
        table.setModel(model);
        panelTable.add(new JScrollPane(table), BorderLayout.CENTER);

        lblTotalPrice = new JLabel ("Celkova cena: " + shoppingCart.getTotalPrice());
        panelFooter.add(lblTotalPrice, BorderLayout.WEST);

        panelMain.add(panelInputs, BorderLayout.NORTH);
        panelMain.add(panelTable, BorderLayout.CENTER);
        panelMain.add(panelFooter, BorderLayout.SOUTH);


        add(panelMain);
    }

    private void updateUI() {
        model.fireTableDataChanged();
        lblTotalPrice.setText("Celkova cena: " + shoppingCart.getTotalPrice());
    }


    private void runAddToCart(ShoppingCartItem item){
        shoppingCart.addItem(item);
        updateUI();
        JOptionPane.showMessageDialog(mainFrame, "Položka přidána.", "Úspěch", JOptionPane.INFORMATION_MESSAGE);
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        if (actionEvent.getSource() == btnInputAdd) {

            String name = txtInputName.getText();
            String pricePerPiece = txtInputPricePerPiece.getText();
            int pieces = (int)spInputPieces.getValue();

            boolean isItemValid = pricePerPiece.matches("^[+-]?(\\d*\\.)?\\d+$");

            if(isItemValid) {
                ShoppingCartItem item = new ShoppingCartItem(name, Double.parseDouble(pricePerPiece), pieces);
                runAddToCart(item);
            } else {
                JOptionPane.showMessageDialog(mainFrame, "Musite zadat cislo", "Error", JOptionPane.INFORMATION_MESSAGE);
            }


        }
    }


}
