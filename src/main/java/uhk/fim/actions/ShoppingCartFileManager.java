package uhk.fim.actions;

import com.google.gson.Gson;
import uhk.fim.model.ShoppingCart;
import uhk.fim.model.ShoppingCartItem;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.*;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.stream.Collectors;


public class ShoppingCartFileManager implements FileManager {
    private final JFrame frame;
    private final ShoppingCart cart;
    private final String storagePath = System.getProperty("user.dir") + "\\storage.csv";

    /**
     * Constructor which accepts frame and a cart to manage
     *
     * @param frame JFrame where certain messages and popups will be displayed
     * @param cart  shopping cart model which gets saved or loaded
     */
    public ShoppingCartFileManager(JFrame frame, ShoppingCart cart) {
        this.frame = frame;
        this.cart = cart;
    }

    /**
     * Helper which displays a "save as" window with supported formats.
     */
    public void handleSaveAs() {
        //return early if cart is empty
        if (cart.getItems().size() == 0) {
            JOptionPane.showMessageDialog(frame, "Your cart is empty!", "Error", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        JFileChooser fc = new JFileChooser();
        fc.setAcceptAllFileFilterUsed(false);

        // Set usable formats
        for (CartFormat format : CartFormat.values()) {
            String formatString = format.toString();
            fc.setFileFilter(new FileNameExtensionFilter(formatString, formatString));
        }

        int result = fc.showSaveDialog(frame);

        if (result == JFileChooser.APPROVE_OPTION) {
            String filePath = fc.getSelectedFile().getAbsolutePath();

            //Get current filter's description - eg: json
            String selectedFormat = fc.getFileFilter().getDescription();
            CartFormat format = getCartFormat(selectedFormat);

            //Handle correct formatting
            if (!filePath.endsWith("." + selectedFormat)) {
                filePath += "." + selectedFormat;
            }
            switch (format) {
                case CSV -> saveCsv(filePath);
                case JSON -> saveJson(filePath);
                //TODO more formats
            }
        }
    }

    /**
     * Saves current shopping cart as .csv
     *
     * @param fullPath determines where the file wil be saved
     *                 make sure format is included
     */
    @Override
    public void saveCsv(String fullPath) {
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(fullPath));
            for (ShoppingCartItem item : cart.getItems()) {
                bw.write(item.getName() + ";" + item.getPricePerPiece() + ";" + item.getPieces());
                bw.newLine();
            }
            bw.close();

        } catch (IOException e) {
            JOptionPane.showMessageDialog(frame, "Saving csv failed", "Error", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    /**
     * Saves current shopping cart as .json
     *
     * @param path determines where the file wil be saved
     *             make sure format is included
     */
    @Override
    public void saveJson(String path) {
        Gson gson = new Gson();
        try {
            FileWriter writer = new FileWriter(path);
            gson.toJson(cart, writer);
            writer.close();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(frame, "Saving json failed.", "Error", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    /**
     * Saves cart to predefined location so it can be retrieved on next load.
     */
    @Override
    public void saveToStorage() {
        saveCsv(storagePath);
    }

    /**
     * Loads cart from stored resource.
     */
    @Override
    public void loadFromStorage() {
        if (new File(storagePath).exists()) {
            loadCsv(storagePath);
        }
    }

    /**
     * Just a helper which determines what method gets called upon trying to load supported formats
     */
    public void handleOpen() {
        JFileChooser fc = new JFileChooser();

        fc.setAcceptAllFileFilterUsed(false);

        //Get app's supported formats
        String[] usableFormats = Arrays.stream(CartFormat.values())
                .map(CartFormat::toString)
                .toArray(String[]::new);

        //Get description of supported formats
        String description = EnumSet.allOf(CartFormat.class).
                stream().
                map(Enum::toString).
                collect(Collectors.joining(", "));

        fc.setFileFilter(new FileNameExtensionFilter(description, usableFormats));

        int userAction = fc.showOpenDialog(frame);
        if (userAction == JFileChooser.APPROVE_OPTION) {
            String filePath = fc.getSelectedFile().getAbsolutePath();

            //Get selected format
            String stringFormat = getFormatFromPath(filePath);

            //@review - format has been double checked above - is this still bad?
            CartFormat format = getCartFormat(stringFormat);

            //Clear cart
            cart.clear();

            //Call appropriate method
            switch (format) {
                case JSON -> loadJson(filePath);
                case CSV -> loadCsv(filePath);
                //TODO more formats
            }
        }
    }

    /**
     * Clears cart and loads specified .csv file
     *
     * @param fullPath has to be a complete path including a format: eg: C:\Example\file.csv
     */
    public void loadCsv(String fullPath) {
        String splitBy = ";";
        try {
            //Clear cart
            cart.clear();

            BufferedReader br = new BufferedReader(new FileReader(fullPath));
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(splitBy);
                cart.addItem(new ShoppingCartItem(
                        data[0],
                        Double.parseDouble(data[1]),
                        Integer.parseInt(data[2])
                ));
            }
        } catch (IOException e) {
            System.out.println("Resource was not loaded - incorrect file path.");
            e.printStackTrace();
        }
    }

    /**
     * Clears cart and loads specified .json file
     *
     * @param fullPath has to be a complete path including a format: eg: C:\Example\file.json
     */
    @Override
    public void loadJson(String fullPath) {
        try {
            Gson gson = new Gson();
            //Clear cart
            cart.clear();
            ShoppingCart newCart = gson.fromJson(new FileReader(fullPath), ShoppingCart.class);
            cart.setItems(newCart.getItems());
        } catch (FileNotFoundException e) {
            System.out.println("Specified path is invalid.");
            e.printStackTrace();
        }
    }

    /**
     * Displays a warning before continuing, then clears cart
     *
     * @return signifies whether user proceeded after the warning
     */
    public boolean clearCart() {
        int userChoice = JOptionPane.showConfirmDialog(frame, "Current cart will be deleted, are you sure?");
        if (userChoice == JOptionPane.OK_OPTION) {
            cart.clear();
        }
        return userChoice == JOptionPane.OK_OPTION;
    }

    /**
     * Helper which returns a format from filePath
     *
     * @param path path to the file
     * @return string such as "json" ... etc.
     */
    private String getFormatFromPath(String path) {
        String stringFormat = "";
        int i = path.lastIndexOf('.');
        if (i > 0) {
            stringFormat = path.substring(i + 1);
        }
        return stringFormat;
    }

    /**
     * Helper which returns a CartFormat from String
     *
     * @return CartFormat enum type
     * @apiNote If incompatible string is passed, default CSV will have been returned.
     */
    private CartFormat getCartFormat(String s) {
        try {
            return CartFormat.valueOf(s.toUpperCase());
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
        //Default
        return CartFormat.CSV;
    }


}
