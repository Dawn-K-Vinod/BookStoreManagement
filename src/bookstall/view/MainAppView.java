package bookstall.view;

import bookstall.model.Book;
import bookstall.model.Customer;
import bookstall.model.Supplier;
import bookstall.model.Transaction;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigDecimal;
// Removed unused imports: java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Main application View, containing all panels and GUI logic.
 * Exposes components and methods needed by the Controller to update the UI.
 */
public class MainAppView extends JFrame {

    private final JPanel mainPanel;
    private final CardLayout cardLayout;

    // --- Components to be accessed by the Controller ---

    // Home Panel Buttons
    private JButton searchBtn, checkoutBtn, updateBtn, customerBtn, supplierBtn, transactionBtn;

    // Search Panel Components
    private JComboBox<String> searchTypeCombo;
    private JTextField searchField;
    private JButton searchBookBtn, showAllBooksBtn, filterToggleBtn;
    private JPanel filterPanel; // Panel itself to toggle visibility
    private JTable bookTable;
    private DefaultTableModel bookTableModel;
    private JButton applyFiltersBtn, clearFiltersBtn;
    // Map to hold references to individual filter components (CheckBoxes, TextFields)
    private final Map<String, JComponent> filterComponents = new HashMap<>();

    // Checkout Panel Components
    private JTextField checkoutBookIdField, checkoutCustomerIdField, checkoutQuantityField;
    private JButton processCheckoutBtn;
    private JTextArea checkoutDetailsArea;

    // Update Book Panel Components
    private JTextField updateBookIdField, updateTitleField, updateAuthorField, updateIsbnField, updatePriceField, updateQuantityField, updateCategoryField; // Added more fields if needed
    // Consider adding fields for publisher, language if editable
    private JButton loadBookBtn, updateBookBtn, deleteBookBtn;

    // Info Panel Components
    private JTable customerTable, supplierTable, transactionTable;
    private DefaultTableModel customerTableModel, supplierTableModel, transactionTableModel;
    private JButton showAllCustomersBtn, showAllSuppliersBtn, showAllTransactionsBtn;

    // Back Buttons (Consider simplifying if all do the same thing)
    private JButton backToHomeSearch, backToHomeCheckout, backToHomeUpdate, backToHomeCustomer, backToHomeSupplier, backToHomeTransaction;


    public MainAppView() {
        // Window setup
        setTitle("Book Stall Management System (MVC)");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Center the window

        // Using CardLayout to switch between different panels
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        // --- Create all panels (Components are now assigned to fields within these methods) ---
        JPanel homePanel = createHomePanel();
        JPanel searchPanel = createSearchBookPanelWithFilters();
        JPanel checkoutPanel = createCheckoutBookPanel();
        JPanel updatePanel = createUpdateBookPanel();
        JPanel customerPanel = createCustomerInfoPanel();
        JPanel supplierPanel = createSupplierInfoPanel();
        JPanel transactionPanel = createTransactionHistoryPanel();

        // --- Remove ALL index-based component retrieval from constructor ---
        // The fields (searchBtn, searchField, checkoutBookIdField, etc.)
        // are now assigned directly within the create...Panel methods.

        // Add panels to the CardLayout
        mainPanel.add(homePanel, "HOME");
        mainPanel.add(searchPanel, "SEARCH");
        mainPanel.add(checkoutPanel, "CHECKOUT");
        mainPanel.add(updatePanel, "UPDATE");
        mainPanel.add(customerPanel, "CUSTOMER");
        mainPanel.add(supplierPanel, "SUPPLIER");
        mainPanel.add(transactionPanel, "TRANSACTION");

        // Add main panel to frame and show Home page initially
        add(mainPanel);
        cardLayout.show(mainPanel, "HOME"); // Show home panel by default
    }

    // ============================= NAVIGATION METHODS =============================
    public void showPanel(String name) {
        cardLayout.show(mainPanel, name);
    }

    // ============================= HOME PAGE =============================
    private JPanel createHomePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);

        JLabel titleLabel = new JLabel("Welcome to Book Stall Management System", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 34));
        titleLabel.setForeground(Color.BLACK);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(30, 0, 30, 0));
        panel.add(titleLabel, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel(new GridLayout(3, 2, 20, 20));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(50, 100, 50, 100));
        buttonPanel.setBackground(new Color(243, 250, 255));

        // Create buttons and assign directly to fields
        searchBtn = createStyledButton("Search Book");
        checkoutBtn = createStyledButton("Checkout Book");
        updateBtn = createStyledButton("Update Book");
        customerBtn = createStyledButton("Customer Info");
        supplierBtn = createStyledButton("Supplier Info");
        transactionBtn = createStyledButton("Transaction History");

        buttonPanel.add(searchBtn);
        buttonPanel.add(checkoutBtn);
        buttonPanel.add(updateBtn);
        buttonPanel.add(customerBtn);
        buttonPanel.add(supplierBtn);
        buttonPanel.add(transactionBtn);

        panel.add(buttonPanel, BorderLayout.CENTER);
        return panel;
    }

    // ============================= SEARCH BOOK PAGE =============================
    private JPanel createSearchBookPanelWithFilters() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // --- Top Panel ---
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        topPanel.add(new JLabel("Search by:"));

        String[] searchOptions = {"Book ID", "Title", "Author", "ISBN", "Publisher", /* "Release year" */}; // Add year if column exists
        searchTypeCombo = new JComboBox<>(searchOptions); // Assign to field
        topPanel.add(searchTypeCombo);

        searchField = new JTextField(20); // Assign to field
        topPanel.add(searchField);

        searchBookBtn = new JButton("Search"); // Assign to field
        showAllBooksBtn = new JButton("Show All Books"); // Assign to field
        filterToggleBtn = new JButton("Open Filters"); // Assign to field
        backToHomeSearch = new JButton("Back to Home"); // Assign to field

        topPanel.add(searchBookBtn);
        topPanel.add(showAllBooksBtn);
        topPanel.add(filterToggleBtn);
        topPanel.add(backToHomeSearch);
        panel.add(topPanel, BorderLayout.NORTH);

        // --- Filter Panel (created separately, assigned to field) ---
        filterPanel = createFilterPanel(); // Assign to field
        filterPanel.setVisible(false); // Initially hidden
        panel.add(filterPanel, BorderLayout.WEST);

        // --- Center Table ---
        String[] columns = {"Book ID", "Title", "Author", "ISBN", "Price",
                "Quantity", "Category", "Language", "Publisher", /* "Release Year" */};
        bookTableModel = new DefaultTableModel(columns, 0); // Assign to field
        bookTable = new JTable(bookTableModel); // Assign to field
        panel.add(new JScrollPane(bookTable), BorderLayout.CENTER);

        // --- Bottom Panel ---
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        applyFiltersBtn = new JButton("Apply Filters"); // Assign to field
        clearFiltersBtn = new JButton("Clear Filters"); // Assign to field
        bottomPanel.add(applyFiltersBtn);
        bottomPanel.add(clearFiltersBtn);
        panel.add(bottomPanel, BorderLayout.SOUTH);

        return panel;
    }


    // ============================= FILTER PANEL =============================
    // (This method now correctly assigns components to the filterComponents map)
     private JPanel createFilterPanel() {
        JPanel filterP = new JPanel();
        filterP.setLayout(new BoxLayout(filterP, BoxLayout.Y_AXIS));
        filterP.setBorder(BorderFactory.createTitledBorder("Filters"));
        filterP.setPreferredSize(new Dimension(250, 600)); // Adjusted height

        JPanel filterContent = new JPanel();
        filterContent.setLayout(new BoxLayout(filterContent, BoxLayout.Y_AXIS));

        // Filter categories - Create sections first
        JPanel languagePanel = createFilterSection("Language",
                new String[]{"English", "Spanish", "French", "German", "Malayalam",
                        "Hindi", "Tamil", "Telugu", "Chinese", "Japanese"});
        filterContent.add(languagePanel); // Add to content panel

        JPanel categoryPanel = createFilterSection("Category",
                new String[]{"Rom-Com", "Action & Adventure", "Mystery", "Thriller",
                        "Science-Fiction", "Fantasy", "Horror", "Biography",
                        "Self-Help", "History", "Poetry", "Drama", "Children's Book"});
        filterContent.add(categoryPanel); // Add to content panel

        // Price filter fields
        JPanel pricePanel = new JPanel();
        pricePanel.setLayout(new BoxLayout(pricePanel, BoxLayout.Y_AXIS));
        pricePanel.setBorder(BorderFactory.createTitledBorder("Price Range"));
        JPanel priceInputPanel = new JPanel(new GridLayout(2, 2, 5, 5));
        priceInputPanel.add(new JLabel("Min:"));
        JTextField minPriceField = new JTextField(8);
        minPriceField.setName("minPrice"); // Unique name
        filterComponents.put("minPrice", minPriceField); // Add to map
        priceInputPanel.add(minPriceField);
        priceInputPanel.add(new JLabel("Max:"));
        JTextField maxPriceField = new JTextField(8);
        maxPriceField.setName("maxPrice"); // Unique name
        filterComponents.put("maxPrice", maxPriceField); // Add to map
        priceInputPanel.add(maxPriceField);
        pricePanel.add(priceInputPanel);
        filterContent.add(pricePanel); // Add to content panel

        // Availability filter (checkbox)
        JPanel availabilityPanel = new JPanel();
        availabilityPanel.setLayout(new BoxLayout(availabilityPanel, BoxLayout.Y_AXIS));
        availabilityPanel.setBorder(BorderFactory.createTitledBorder("Availability"));
        JCheckBox inStockCheckBox = new JCheckBox("In Stock Only");
        inStockCheckBox.setName("inStock"); // Unique name
        filterComponents.put("inStock", inStockCheckBox); // Add to map
        availabilityPanel.add(inStockCheckBox);
        filterContent.add(availabilityPanel); // Add to content panel

        // Year range filter (Assuming you might add this later)
        // JPanel yearPanel = ... create year filter ...
        // filterComponents.put("fromYear", fromYearField);
        // filterComponents.put("toYear", toYearField);
        // filterContent.add(yearPanel); // Add to content panel

        // Add checkboxes from sections to the map after creating them
        collectFilterCheckBoxes(languagePanel); // Collects checkboxes from languagePanel
        collectFilterCheckBoxes(categoryPanel); // Collects checkboxes from categoryPanel

        // Put the content panel inside a scroll pane
        JScrollPane scrollPane = new JScrollPane(filterContent);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        filterP.add(scrollPane); // Add scroll pane to the main filter panel

        return filterP;
    }

    // Creates each section inside filter panel (like language, category)
    private JPanel createFilterSection(String title, String[] options) {
        JPanel section = new JPanel();
        section.setLayout(new BoxLayout(section, BoxLayout.Y_AXIS));
        section.setBorder(BorderFactory.createTitledBorder(title));
        section.setAlignmentX(Component.LEFT_ALIGNMENT); // Align checkboxes left

        for (String option : options) {
            JCheckBox checkBox = new JCheckBox(option);
            // Use a unique name for easier retrieval in the map
            checkBox.setName(title.toLowerCase() + "-" + option.replaceAll("[^A-Za-z0-9]", "")); // Sanitize name
            section.add(checkBox);
        }
        return section;
    }

     // Helper function to add check boxes from a section to the filterComponents map
    private void collectFilterCheckBoxes(Container container) {
        // Collects only the direct children JCheckBoxes
        for (Component comp : container.getComponents()) {
            if (comp instanceof JCheckBox) {
                JCheckBox checkBox = (JCheckBox) comp;
                // Use the name set in createFilterSection as the key
                filterComponents.put(checkBox.getName(), checkBox);
            }
        }
    }

    // ============================= CHECKOUT PAGE =============================
    private JPanel createCheckoutBookPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;

        // Book ID
        gbc.gridx = 0; gbc.gridy = 0; formPanel.add(new JLabel("Book ID:"), gbc);
        checkoutBookIdField = new JTextField(15); // Assign to field
        gbc.gridx = 1; gbc.gridy = 0; formPanel.add(checkoutBookIdField, gbc);

        // Customer ID
        gbc.gridx = 0; gbc.gridy = 1; formPanel.add(new JLabel("Customer ID:"), gbc);
        checkoutCustomerIdField = new JTextField(15); // Assign to field
        gbc.gridx = 1; gbc.gridy = 1; formPanel.add(checkoutCustomerIdField, gbc);

        // Quantity
        gbc.gridx = 0; gbc.gridy = 2; formPanel.add(new JLabel("Quantity:"), gbc);
        checkoutQuantityField = new JTextField(15); // Assign to field
        gbc.gridx = 1; gbc.gridy = 2; formPanel.add(checkoutQuantityField, gbc);

        // Buttons
        processCheckoutBtn = new JButton("Process Checkout"); // Assign to field
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        formPanel.add(processCheckoutBtn, gbc);

        backToHomeCheckout = new JButton("Back to Home"); // Assign to field
        gbc.gridy = 4;
        formPanel.add(backToHomeCheckout, gbc);

        panel.add(formPanel, BorderLayout.NORTH);

        // Output area
        checkoutDetailsArea = new JTextArea(15, 50); // Assign to field
        checkoutDetailsArea.setEditable(false);
        panel.add(new JScrollPane(checkoutDetailsArea), BorderLayout.CENTER);

        return panel;
    }


    // ============================= UPDATE BOOK PAGE =============================
    private JPanel createUpdateBookPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        // Labels and Fields (Assign fields directly)
        String[] labels = {"Book ID:", "Title:", "Author:", "ISBN:", "Price:", "Quantity:", "Category:"};
        JTextField[] fields = new JTextField[labels.length];

        int yPos = 0;
        gbc.gridx = 0; gbc.gridy = yPos; formPanel.add(new JLabel(labels[yPos]), gbc);
        updateBookIdField = new JTextField(20); fields[yPos] = updateBookIdField;
        gbc.gridx = 1; formPanel.add(fields[yPos], gbc);
        yPos++;

        gbc.gridx = 0; gbc.gridy = yPos; formPanel.add(new JLabel(labels[yPos]), gbc);
        updateTitleField = new JTextField(20); fields[yPos] = updateTitleField;
        gbc.gridx = 1; formPanel.add(fields[yPos], gbc);
        yPos++;

        gbc.gridx = 0; gbc.gridy = yPos; formPanel.add(new JLabel(labels[yPos]), gbc);
        updateAuthorField = new JTextField(20); fields[yPos] = updateAuthorField;
        gbc.gridx = 1; formPanel.add(fields[yPos], gbc);
        yPos++;

        gbc.gridx = 0; gbc.gridy = yPos; formPanel.add(new JLabel(labels[yPos]), gbc);
        updateIsbnField = new JTextField(20); fields[yPos] = updateIsbnField;
        gbc.gridx = 1; formPanel.add(fields[yPos], gbc);
        yPos++;

        gbc.gridx = 0; gbc.gridy = yPos; formPanel.add(new JLabel(labels[yPos]), gbc);
        updatePriceField = new JTextField(20); fields[yPos] = updatePriceField;
        gbc.gridx = 1; formPanel.add(fields[yPos], gbc);
        yPos++;

        gbc.gridx = 0; gbc.gridy = yPos; formPanel.add(new JLabel(labels[yPos]), gbc);
        updateQuantityField = new JTextField(20); fields[yPos] = updateQuantityField;
        gbc.gridx = 1; formPanel.add(fields[yPos], gbc);
        yPos++;

        gbc.gridx = 0; gbc.gridy = yPos; formPanel.add(new JLabel(labels[yPos]), gbc);
        updateCategoryField = new JTextField(20); fields[yPos] = updateCategoryField;
        gbc.gridx = 1; formPanel.add(fields[yPos], gbc);
        yPos++;
        // Add more fields (publisher, language) here if needed, assigning to fields


        // Buttons Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        loadBookBtn = new JButton("Load Book"); buttonPanel.add(loadBookBtn);
        updateBookBtn = new JButton("Update Book"); buttonPanel.add(updateBookBtn);
        //addNewBookBtn = new JButton("Add New Book"); buttonPanel.add(addNewBookBtn);
        deleteBookBtn = new JButton("Delete Book"); buttonPanel.add(deleteBookBtn);

        gbc.gridx = 0; gbc.gridy = yPos; // Place button panel below fields
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        formPanel.add(buttonPanel, gbc);
        yPos++;

        backToHomeUpdate = new JButton("Back to Home"); // Assign to field
        gbc.gridx = 0; gbc.gridy = yPos; // Below button panel
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        formPanel.add(backToHomeUpdate, gbc);

        panel.add(formPanel, BorderLayout.CENTER); // Add formPanel to center
        return panel;
    }


    // ============================= CUSTOMER INFO PAGE =============================
    private JPanel createCustomerInfoPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        showAllCustomersBtn = new JButton("Show All Customers"); // Assign to field
        backToHomeCustomer = new JButton("Back to Home"); // Assign to field
        topPanel.add(showAllCustomersBtn);
        topPanel.add(backToHomeCustomer);
        panel.add(topPanel, BorderLayout.NORTH);

        String[] columns = {"Customer ID", "Name", "Email", "Phone", "Address"};
        customerTableModel = new DefaultTableModel(columns, 0); // Assign to field
        customerTable = new JTable(customerTableModel); // Assign to field
        panel.add(new JScrollPane(customerTable), BorderLayout.CENTER);

        return panel;
    }


    // ============================= SUPPLIER INFO PAGE =============================
    private JPanel createSupplierInfoPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        showAllSuppliersBtn = new JButton("Show All Suppliers"); // Assign to field
        backToHomeSupplier = new JButton("Back to Home"); // Assign to field
        topPanel.add(showAllSuppliersBtn);
        topPanel.add(backToHomeSupplier);
        panel.add(topPanel, BorderLayout.NORTH);

        String[] columns = {"Supplier ID", "Name", "Email", "Phone", "Company Name", "Address"}; // Columns matching DB
        supplierTableModel = new DefaultTableModel(columns, 0); // Assign to field
        supplierTable = new JTable(supplierTableModel); // Assign to field
        panel.add(new JScrollPane(supplierTable), BorderLayout.CENTER);

        return panel;
    }


    // ============================= TRANSACTION HISTORY PAGE =============================
    private JPanel createTransactionHistoryPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        showAllTransactionsBtn = new JButton("Show All Transactions"); // Assign to field
        backToHomeTransaction = new JButton("Back to Home"); // Assign to field
        topPanel.add(showAllTransactionsBtn);
        topPanel.add(backToHomeTransaction);
        panel.add(topPanel, BorderLayout.NORTH);

        String[] columns = {"Transaction ID", "Customer ID", "Book ID", "Quantity", "Total Amount", "Date/Time"}; // Columns matching DB
        transactionTableModel = new DefaultTableModel(columns, 0); // Assign to field
        transactionTable = new JTable(transactionTableModel); // Assign to field
        panel.add(new JScrollPane(transactionTable), BorderLayout.CENTER);

        return panel;
    }


    // ============================= UTILITY METHODS =============================

    private JButton createStyledButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Arial", Font.BOLD, 16));
        btn.setBackground(new Color(70, 130, 180)); // Steel Blue
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(46, 86, 120), 1), // Darker border
                BorderFactory.createEmptyBorder(15, 20, 15, 20)) // Padding
        );
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }


    private void clearTable(DefaultTableModel model) {
        model.setRowCount(0);
    }

    // --- Data Update Methods (Called by Controller) ---

    public void updateBookTable(List<Book> books) {
        clearTable(bookTableModel);
        if (books == null) books = new ArrayList<>(); // Prevent NPE
        for (Book book : books) {
            bookTableModel.addRow(new Object[]{
                    book.getBookId(), book.getTitle(), book.getAuthor(), book.getIsbn(),
                    String.format("%.2f", book.getPrice()), book.getQuantity(),
                    book.getGenre(), book.getLanguage(), book.getPublisher(),
                    /* "N/A" // Release Year */
            });
        }
    }


    public void updateCustomerTable(List<Customer> customers) {
        clearTable(customerTableModel);
        if (customers == null) customers = new ArrayList<>(); // Prevent NPE
        for (Customer customer : customers) {
            customerTableModel.addRow(new Object[]{
                    customer.getCustomerId(), customer.getName(), customer.getEmail(),
                    customer.getPhone(), customer.getAddress()
            });
        }
    }

    public void updateSupplierTable(List<Supplier> suppliers) {
        clearTable(supplierTableModel);
        if (suppliers == null) suppliers = new ArrayList<>(); // Prevent NPE
        for (Supplier supplier : suppliers) {
            supplierTableModel.addRow(new Object[]{
                    supplier.getSupplierId(), supplier.getName(), supplier.getEmail(),
                    supplier.getPhone(), supplier.getCompanyName(), supplier.getAddress()
            });
        }
    }

    public void updateTransactionTable(List<Transaction> transactions) {
        clearTable(transactionTableModel);
        if (transactions == null) transactions = new ArrayList<>(); // Prevent NPE
        for (Transaction t : transactions) {
            transactionTableModel.addRow(new Object[]{
                    t.getTransactionId(), t.getCustomerId(), t.getBookId(), t.getQuantity(),
                    String.format("%.2f", t.getTotalAmount()), t.getDateTime()
            });
        }
    }


    public void populateUpdateFields(Book book) {
        if (book == null) {
            clearUpdateFields();
            return;
        }
        updateBookIdField.setText(book.getBookId() != null ? book.getBookId() : "");
        updateTitleField.setText(book.getTitle() != null ? book.getTitle() : "");
        updateAuthorField.setText(book.getAuthor() != null ? book.getAuthor() : "");
        updateIsbnField.setText(book.getIsbn() != null ? book.getIsbn() : "");
        updatePriceField.setText(String.format("%.2f", book.getPrice()));
        updateQuantityField.setText(String.valueOf(book.getQuantity()));
        updateCategoryField.setText(book.getGenre() != null ? book.getGenre() : "");
        // Update publisher, language fields if added
    }


    public void clearUpdateFields() {
        updateBookIdField.setText("");
        updateTitleField.setText("");
        updateAuthorField.setText("");
        updateIsbnField.setText("");
        updatePriceField.setText("");
        updateQuantityField.setText("");
        updateCategoryField.setText("");
        // Clear publisher, language fields if added
    }

    // --- Getters for Controller to read input/state ---

    public JButton getSearchBtn() { return searchBtn; }
    public JButton getCheckoutBtn() { return checkoutBtn; }
    public JButton getUpdateBtn() { return updateBtn; }
    public JButton getCustomerBtn() { return customerBtn; }
    public JButton getSupplierBtn() { return supplierBtn; }
    public JButton getTransactionBtn() { return transactionBtn; }

    public JButton getBackToHomeSearch() { return backToHomeSearch; }
    public JButton getBackToHomeCheckout() { return backToHomeCheckout; }
    public JButton getBackToHomeUpdate() { return backToHomeUpdate; }
    public JButton getBackToHomeCustomer() { return backToHomeCustomer; }
    public JButton getBackToHomeSupplier() { return backToHomeSupplier; }
    public JButton getBackToHomeTransaction() { return backToHomeTransaction; }

    public JComboBox<String> getSearchTypeCombo() { return searchTypeCombo; }
    public JTextField getSearchField() { return searchField; }
    public JButton getSearchBookBtn() { return searchBookBtn; }
    public JButton getShowAllBooksBtn() { return showAllBooksBtn; }
    public JButton getFilterToggleBtn() { return filterToggleBtn; }
    public JPanel getFilterPanel() { return filterPanel; } // The panel itself
    public Map<String, JComponent> getFilterComponents() { return filterComponents; } // Individual filter controls
    public JButton getApplyFiltersBtn() { return applyFiltersBtn; }
    public JButton getClearFiltersBtn() { return clearFiltersBtn; }


    public JTextField getCheckoutBookIdField() { return checkoutBookIdField; }
    public JTextField getCheckoutCustomerIdField() { return checkoutCustomerIdField; }
    public JTextField getCheckoutQuantityField() { return checkoutQuantityField; }
    public JButton getProcessCheckoutBtn() { return processCheckoutBtn; }
    public JTextArea getCheckoutDetailsArea() { return checkoutDetailsArea; }

    public JTextField getUpdateBookIdField() { return updateBookIdField; }
    public JTextField getUpdateTitleField() { return updateTitleField; }
    public JTextField getUpdateAuthorField() { return updateAuthorField; }
    public JTextField getUpdateIsbnField() { return updateIsbnField; }
    public JTextField getUpdatePriceField() { return updatePriceField; }
    public JTextField getUpdateQuantityField() { return updateQuantityField; }
    public JTextField getUpdateCategoryField() { return updateCategoryField; }
    public JButton getLoadBookBtn() { return loadBookBtn; }
    public JButton getUpdateBookBtn() { return updateBookBtn; }
   // public JButton getAddNewBookBtn() { return addNewBookBtn; }
    public JButton getDeleteBookBtn() { return deleteBookBtn; }

    public JButton getShowAllCustomersBtn() { return showAllCustomersBtn; }
    public JButton getShowAllSuppliersBtn() { return showAllSuppliersBtn; }
    public JButton getShowAllTransactionsBtn() { return showAllTransactionsBtn; }

    // Retrieves data from update panel fields, includes basic error handling for numbers
    public Book getBookFromUpdateFields() {
        Book book = new Book();
        book.setBookId(updateBookIdField.getText().trim());
        book.setTitle(updateTitleField.getText().trim());
        book.setAuthor(updateAuthorField.getText().trim());
        book.setIsbn(updateIsbnField.getText().trim());
        try {
            String priceStr = updatePriceField.getText().trim().replace(',', '.');
            if(priceStr.isEmpty()) {
                 showMessage("Price cannot be empty.", "Input Error", JOptionPane.ERROR_MESSAGE);
                 return null;
            }
            book.setPrice(new BigDecimal(priceStr).doubleValue());
        } catch (NumberFormatException e) {
             showMessage("Invalid price format.", "Input Error", JOptionPane.ERROR_MESSAGE);
             return null; // Indicate failure
        }
        try {
             String quantityStr = updateQuantityField.getText().trim();
             if(quantityStr.isEmpty()) {
                  showMessage("Quantity cannot be empty.", "Input Error", JOptionPane.ERROR_MESSAGE);
                  return null;
             }
            book.setQuantity(Integer.parseInt(quantityStr));
        } catch (NumberFormatException e) {
             showMessage("Invalid quantity format.", "Input Error", JOptionPane.ERROR_MESSAGE);
             return null; // Indicate failure
        }
        book.setGenre(updateCategoryField.getText().trim());
        // Set publisher, language if fields exist and are assigned
        return book;
    }


    // --- Generic UI Helpers ---
    public void showMessage(String message, String title, int type) {
        JOptionPane.showMessageDialog(this, message, title, type);
    }
}
