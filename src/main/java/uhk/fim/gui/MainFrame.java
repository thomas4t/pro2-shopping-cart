package uhk.fim.gui;

import uhk.fim.actions.ShoppingCartFileManager;
import uhk.fim.model.ShoppingCart;
import uhk.fim.model.ShoppingCartItem;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class MainFrame extends JFrame implements ActionListener {
    MainFrame mainFrame;

    ShoppingCartFileManager fileManager;

    JButton btnInputAdd;
    JTextField txtInputName, txtInputPricePerPiece;
    JSpinner spInputPieces;

    ShoppingCart shoppingCart;
    ShoppingCartTableModel model;
    JLabel lblTotalPrice;

    public MainFrame(int width, int height) {
        super("PRO2 - Shopping cart");

        setSize(width, height);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        initObjects();
        initGUI();
        loadInitialData();

        updateGUI();
    }

    private void initObjects() {
        mainFrame = this;
        shoppingCart = new ShoppingCart();
        model = new ShoppingCartTableModel();
        model.setShoppingCart(shoppingCart);
        fileManager = new ShoppingCartFileManager(this, shoppingCart);
    }

    private void loadInitialData() {
        fileManager.loadFromStorage();
    }

    private void initGUI() {
        JPanel panelMain = new JPanel(new BorderLayout());

        createMenuBar();

        JPanel panelInputs = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JPanel panelTable = new JPanel();
        JPanel panelFooter = new JPanel();

        JLabel lblInputName = new JLabel("Nazev: ");
        txtInputName = new JTextField("", 15);
        JLabel lblPricePerPiece = new JLabel("Cena/kus ");
        txtInputPricePerPiece = new JTextField("", 5);
        JLabel lblInputPieces = new JLabel("Pocet kusu: ");
        spInputPieces = new JSpinner(new SpinnerNumberModel(1, 1, 999, 1));

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

        lblTotalPrice = new JLabel();
        panelFooter.add(lblTotalPrice, BorderLayout.WEST);

        panelMain.add(panelInputs, BorderLayout.NORTH);
        panelMain.add(panelTable, BorderLayout.CENTER);
        panelMain.add(panelFooter, BorderLayout.SOUTH);


        add(panelMain);
    }

    private void createMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        JMenu fileMenu = new JMenu("File");

        fileMenu.add(new AbstractAction("New cart") {
            @Override
            public void actionPerformed(ActionEvent e) {
                boolean didOkay = fileManager.clearCart();
                if (didOkay) {
                    updateGUI();
                }
            }
        });
        fileMenu.add(new AbstractAction("Load") {
            @Override
            public void actionPerformed(ActionEvent e) {
                fileManager.handleOpen();
                updateGUI();
            }
        });
        fileMenu.add(new AbstractAction("Save") {
            @Override
            public void actionPerformed(ActionEvent e) {
                fileManager.saveToStorage();
            }
        });
        fileMenu.add(new AbstractAction("Save as") {
            @Override
            public void actionPerformed(ActionEvent e) {
                fileManager.handleSaveAs();
            }
        });

        JMenu aboutMenu = new JMenu("About");
        aboutMenu.add(new AbstractAction("Author") {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(mainFrame, "Coded for PRO2@UHK by Tomas Trávníček",
                        "About author",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        });

        menuBar.add(fileMenu);
        menuBar.add(aboutMenu);

        setJMenuBar(menuBar);
    }

    private void updateGUI() {
        model.fireTableDataChanged();
        lblTotalPrice.setText("Celkova cena: " + shoppingCart.getTotalPrice());
    }


    private void runAddToCart(ShoppingCartItem item) {
        shoppingCart.addItem(item);
        updateGUI();
        JOptionPane.showMessageDialog(mainFrame, "Položka přidána.", "Úspěch", JOptionPane.INFORMATION_MESSAGE);
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        if (actionEvent.getSource() == btnInputAdd) {

            String name = txtInputName.getText();
            String pricePerPiece = txtInputPricePerPiece.getText();
            int pieces = (int) spInputPieces.getValue();

            boolean isItemValid = pricePerPiece.matches("^[+-]?(\\d*\\.)?\\d+$");

            if (isItemValid) {
                ShoppingCartItem item = new ShoppingCartItem(name, Double.parseDouble(pricePerPiece), pieces);
                runAddToCart(item);
            } else {
                JOptionPane.showMessageDialog(mainFrame, "Musite zadat cislo", "Error", JOptionPane.INFORMATION_MESSAGE);
            }


        }
    }
}
