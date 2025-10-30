package bookstall.controller;

import bookstall.model.Book;
import bookstall.model.DatabaseManager;
import bookstall.view.MainAppView;

import javax.swing.*;

import com.mysql.cj.xdevapi.Statement;

import java.awt.*;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Controller for the Main Application View. Manages navigation,
 * handles button clicks, and coordinates between View and DatabaseManager (Model).
 */
public class MainAppController {

    private final MainAppView view;
    private final DatabaseManager model;

    public MainAppController(MainAppView view) {
        this.view = view;
        this.model = new DatabaseManager();

        // 1. Initial Data Load for Search View
        view.updateBookTable(model.searchBooks("", ""));

        // 2. Add Listeners for Navigation
        addNavigationListeners();

        // 3. Add Listeners for Search/Filter Logic
        addSearchListeners();

        // 4. Add Listeners for Checkout Logic
        addCheckoutListeners();

        // 5. Add Listeners for Update Book Logic
        addUpdateListeners();

        // 6. Add Listeners for Info Panels
        addInfoPanelListeners();
    }

    // --- 2. Navigation Listeners ---
    private void addNavigationListeners() {
        // Home buttons
        view.getSearchBtn().addActionListener(e -> view.showPanel("SEARCH"));
        view.getCheckoutBtn().addActionListener(e -> view.showPanel("CHECKOUT"));
        view.getUpdateBtn().addActionListener(e -> view.showPanel("UPDATE"));
        view.getCustomerBtn().addActionListener(e -> view.showPanel("CUSTOMER"));
        view.getSupplierBtn().addActionListener(e -> view.showPanel("SUPPLIER"));
        view.getTransactionBtn().addActionListener(e -> view.showPanel("TRANSACTION"));

        // Back buttons
        ActionListener backListener = e -> view.showPanel("HOME");
        view.getBackToHomeCheckout().addActionListener(backListener);
        view.getBackToHomeCustomer().addActionListener(backListener);
        view.getBackToHomeSearch().addActionListener(backListener);
        view.getBackToHomeSupplier().addActionListener(backListener);
        view.getBackToHomeTransaction().addActionListener(backListener);
        view.getBackToHomeUpdate().addActionListener(backListener);
    }

    // --- 3. Search/Filter Listeners ---
    private void addSearchListeners() {
        // Search Button
        view.getSearchBookBtn().addActionListener(e -> {
            String type = (String) view.getSearchTypeCombo().getSelectedItem();
            String query = view.getSearchField().getText();
            List<Book> results = model.searchBooks(type, query);
            view.updateBookTable(results);
        });

        // Show All Button
        view.getShowAllBooksBtn().addActionListener(e -> {
            view.getSearchField().setText(""); // Clear search field on Show All
            view.updateBookTable(model.searchBooks("", ""));
        });

        // Filter Toggle Button (strictly re-implemented original logic)
        view.getFilterToggleBtn().addActionListener(e -> {
            boolean isVisible = view.getFilterPanel().isVisible();
            view.getFilterPanel().setVisible(!isVisible);
            view.getFilterToggleBtn().setText(isVisible ? "Open Filters" : "Close Filters");
        });

        // Apply Filters Button
        view.getApplyFiltersBtn().addActionListener(e -> {
            Map<String, List<String>> selectedFilters = getSelectedFilters();
            List<Book> filteredBooks = model.applyFilters(selectedFilters); // Logic is mocked in Model
            view.updateBookTable(filteredBooks);
        });

        // Clear Filters Button
        view.getClearFiltersBtn().addActionListener(e -> {
            clearAllFilters(view.getFilterPanel());
            view.showMessage("All filters cleared!", "Filter Status", JOptionPane.INFORMATION_MESSAGE);
        });
    }

    private Map<String, List<String>> getSelectedFilters() {
        Map<String, List<String>> filters = new HashMap<>();
        filters.put("languages", new ArrayList<>());
        filters.put("categories", new ArrayList<>());
        filters.put("priceRange", new ArrayList<>()); // [min, max]
        filters.put("yearRange", new ArrayList<>());   // [from, to]
        filters.put("availability", new ArrayList<>());

        Map<String, JComponent> components = view.getFilterComponents();

        // Checkboxes and TextFields
        for (Map.Entry<String, JComponent> entry : components.entrySet()) {
            String name = entry.getKey();
            JComponent comp = entry.getValue();

            if (comp instanceof JCheckBox) {
                JCheckBox cb = (JCheckBox) comp;
                if (cb.isSelected()) {
                    if (name.startsWith("language")) filters.get("languages").add(cb.getText());
                    else if (name.startsWith("category")) filters.get("categories").add(cb.getText());
                    else if (name.equals("inStock")) filters.get("availability").add("In Stock Only");
                }
            } else if (comp instanceof JTextField) {
                JTextField tf = (JTextField) comp;
                String text = tf.getText().trim();
                if (!text.isEmpty()) {
                    if (name.equals("minPrice") || name.equals("maxPrice")) filters.get("priceRange").add(text);
                    else if (name.equals("fromYear") || name.equals("toYear")) filters.get("yearRange").add(text);
                }
            }
        }
        return filters;
    }

    private void clearAllFilters(Container container) {
        for (JComponent comp : view.getFilterComponents().values()) {
            if (comp instanceof JCheckBox) ((JCheckBox) comp).setSelected(false);
            else if (comp instanceof JTextField) ((JTextField) comp).setText("");
        }
    }

    // --- 4. Checkout Listeners ---
    private void addCheckoutListeners() {
        view.getProcessCheckoutBtn().addActionListener(e -> {
            String bookId = view.getCheckoutBookIdField().getText().trim();
            String customerId = view.getCheckoutCustomerIdField().getText().trim();
            String quantityStr = view.getCheckoutQuantityField().getText().trim();
            int quantity;

            try {
                quantity = Integer.parseInt(quantityStr);
                if (quantity <= 0) throw new NumberFormatException();
            } catch (NumberFormatException ex) {
                view.showMessage("Please enter a valid quantity.", "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            model.processCheckout(bookId, customerId, quantity, view.getCheckoutDetailsArea());
        });
    }

    // --- 5. Update Book Listeners ---
    private void addUpdateListeners() {
        // Load Book
        view.getLoadBookBtn().addActionListener(e -> {
            String bookId = view.getUpdateBookIdField().getText().trim();
            if (bookId.isEmpty()) {
                view.showMessage("Please enter a Book ID to load.", "Input Required", JOptionPane.WARNING_MESSAGE);
                return;
            }
            Book book = model.loadBook(bookId); // Mocked
            if (book != null) {
                view.populateUpdateFields(book);
            } else {
                view.showMessage("Book ID not found (Mocked).", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Add New Book
//        view.getAddNewBookBtn().addActionListener(e -> {
//            view.clearUpdateFields();
//            view.showMessage("Ready to add new book. Enter details and press Update.", "New Entry Mode", JOptionPane.INFORMATION_MESSAGE);
//        });

        // Update Book (used for both Update and Add)
        view.getUpdateBookBtn().addActionListener(e -> {
            Book book = view.getBookFromUpdateFields();
            try {
				Connection con = DatabaseManager.getConnection();
				java.sql.PreparedStatement ps = con.prepareStatement("Select * from books where bookId = ?");
				ps.setString(1, book.getBookId());
				ResultSet rs = ps.executeQuery();
				
				if (!rs.next()) {
	                // Assume new book since ID is empty
	                model.addBook(book);
	            } else {
	                model.updateBook(book);
	            }
				
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}            
            
    
        });

        // Delete Book
        view.getDeleteBookBtn().addActionListener(e -> {
            String bookId = view.getUpdateBookIdField().getText().trim();
            if (bookId.isEmpty()) {
                view.showMessage("Please enter a Book ID to delete.", "Input Required", JOptionPane.WARNING_MESSAGE);
                return;
            }
            model.deleteBook(bookId);
            view.clearUpdateFields();
        });
    }

    // --- 6. Info Panel Listeners ---
    private void addInfoPanelListeners() {
        view.getShowAllCustomersBtn().addActionListener(e -> {
            view.updateCustomerTable(model.getAllCustomers());
        });

        view.getShowAllSuppliersBtn().addActionListener(e -> {
            view.updateSupplierTable(model.getAllSuppliers());
        });

        view.getShowAllTransactionsBtn().addActionListener(e -> {
            view.updateTransactionTable(model.getAllTransactions());
        });
    }
}
