package bookstall.model;

    import javax.swing.*;
    import java.sql.*;
    import java.util.ArrayList;
    import java.util.List;
    import java.util.Map;
    import java.math.BigDecimal;
    import java.util.UUID;
import java.sql.*
;
    /**
     * Model layer component handling all CRUD and data access logic using JDBC.
     */
    public class DatabaseManager {

        // --- Database Connection Details (Updated with User Provided Info) ---
        private static final String DB_URL = "jdbc:mysql://localhost:3306/BookStoreManagementSystem?useSSL=false&allowPublicKeyRetrieval=true"; // DB name updated
        private static final String DB_USER = "admin"; // Username updated
        private static final String DB_PASSWORD = "123"; // Password updated

        // --- Load the JDBC driver ---
        static {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
            } catch (ClassNotFoundException e) {
                JOptionPane.showMessageDialog(null,
                        "MySQL JDBC Driver not found!\nMake sure the JAR is in the classpath.\n" + e.getMessage(),
                        "Database Driver Error",
                        JOptionPane.ERROR_MESSAGE);
                System.exit(1);
            }
        }

        // --- Helper method to establish connection ---
        // Made public static so AuthModel can potentially use it, or create its own.
        // Consider dependency injection for better design in larger apps.
        public static Connection getConnection() throws SQLException {
            // Adding serverTimezone might be necessary depending on MySQL config
            // String urlWithTimezone = DB_URL + "&serverTimezone=UTC"; // Example
            return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
        }

        // --- searchBooks Method ---
        public List<Book> searchBooks(String searchType, String query) {
            List<Book> books = new ArrayList<>();
            StringBuilder sqlBuilder = new StringBuilder("SELECT bookId, ISBN, title, author, publisher, price, quantity, genre, language FROM Books");
            List<Object> params = new ArrayList<>();
            boolean hasQuery = query != null && !query.trim().isEmpty();

            if (hasQuery) {
                String field = "";
                boolean useLike = false;
                switch (searchType) {
                    case "Book ID": field = "bookId"; break;
                    case "Title": field = "title"; useLike = true; break;
                    case "Author": field = "author"; useLike = true; break;
                    case "ISBN": field = "ISBN"; break;
                    case "Publisher": field = "publisher"; useLike = true; break;
                    default:
                        field = "title";
                        useLike = true;
                        break;
                }
                 sqlBuilder.append(" WHERE ").append(field);
                 if (useLike) {
                     sqlBuilder.append(" LIKE ?");
                     params.add("%" + query + "%");
                 } else {
                     sqlBuilder.append(" = ?");
                     params.add(query);
                 }
            }
            sqlBuilder.append(" ORDER BY title"); // Added default ordering

            String sql = sqlBuilder.toString();

            try (Connection conn = getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {
                for (int i = 0; i < params.size(); i++) {
                    pstmt.setObject(i + 1, params.get(i));
                }
                try (ResultSet rs = pstmt.executeQuery()) {
                    while (rs.next()) {
                        Book book = mapResultSetToBook(rs);
                        books.add(book);
                    }
                }
            } catch (SQLException e) {
                handleSqlError(e, "Search Error");
            }
            return books;
        }

        // --- loadBook Method ---
         public Book loadBook(String bookId) {
             String sql = "SELECT bookId, ISBN, title, author, publisher, price, quantity, genre, language FROM Books WHERE bookId = ?";
             Book book = null;
             try (Connection conn = getConnection();
                  PreparedStatement pstmt = conn.prepareStatement(sql)) {
                 pstmt.setString(1, bookId);
                 try (ResultSet rs = pstmt.executeQuery()) {
                     if (rs.next()) {
                         book = mapResultSetToBook(rs);
                     } else {
                          JOptionPane.showMessageDialog(null, "Book ID '" + bookId + "' not found.", "Load Error", JOptionPane.WARNING_MESSAGE);
                     }
                 }
             } catch (SQLException e) {
                 handleSqlError(e, "Load Error");
             }
             return book;
         }

        // --- addBook Method ---
        public boolean addBook(Book book) {
            if (!validateBook(book)) {
            	
            	JOptionPane.showMessageDialog(null, "Validation failed! The book data is invalid.", "Validation Error", JOptionPane.WARNING_MESSAGE);
            	return false;
            }
            String sql = "INSERT INTO Books (bookId, ISBN, title, author, publisher, price, quantity, genre, language) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
            boolean success = false;
            try (Connection conn = getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {
                setBookStatementParams(pstmt, book);
                int affectedRows = pstmt.executeUpdate();
                success = affectedRows > 0;
                if (success) {
                    JOptionPane.showMessageDialog(null, "Book added successfully.");
                } else {
                     JOptionPane.showMessageDialog(null, "Failed to add book (0 rows affected).", "Database Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (SQLIntegrityConstraintViolationException e) {
                 handleConstraintViolation(e, book);
            } catch (SQLException e) {
                handleSqlError(e, "Add Error");
            }
            return success;
        }

        // --- updateBook Method ---
        public boolean updateBook(Book book) {
             if (book.getBookId() == null || book.getBookId().trim().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Cannot update: Book ID is missing.", "Update Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }
            if (!validateBook(book)) return false;
            String sql = "UPDATE Books SET ISBN = ?, title = ?, author = ?, publisher = ?, price = ?, quantity = ?, genre = ?, language = ? WHERE bookId = ?";
            boolean success = false;
            try (Connection conn = getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {
                setBookStatementParams(pstmt, book, false); // false = don't set bookId first
                pstmt.setString(9, book.getBookId()); // Set bookId for WHERE clause
                int affectedRows = pstmt.executeUpdate();
                success = affectedRows > 0;
                if (success) {
                    JOptionPane.showMessageDialog(null, "Book updated successfully.");
                } else {
                     JOptionPane.showMessageDialog(null, "Failed to update book. Book ID may not exist or data hasn't changed.", "Update Warning", JOptionPane.WARNING_MESSAGE);
                }
            } catch (SQLIntegrityConstraintViolationException e) {
                handleConstraintViolation(e, book);
            } catch (SQLException e) {
                handleSqlError(e, "Update Error");
            }
             return success;
        }

        // --- deleteBook Method ---
        public boolean deleteBook(String bookId) {
            if (bookId == null || bookId.trim().isEmpty()) {
                  JOptionPane.showMessageDialog(null, "Cannot delete: Book ID is missing.", "Delete Error", JOptionPane.ERROR_MESSAGE);
                  return false;
             }
            String sql = "DELETE FROM Books WHERE bookId = ?";
            boolean success = false;
            try (Connection conn = getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, bookId);
                int affectedRows = pstmt.executeUpdate();
                success = affectedRows > 0;
                if (success) {
                    JOptionPane.showMessageDialog(null, "Book deleted successfully.");
                } else {
                     JOptionPane.showMessageDialog(null, "Failed to delete book. Book ID may not exist.", "Delete Warning", JOptionPane.WARNING_MESSAGE);
                }
            } catch (SQLIntegrityConstraintViolationException e) {
                 JOptionPane.showMessageDialog(null, "Cannot delete book: It might be referenced in transactions.\n" + e.getMessage(), "Delete Error", JOptionPane.ERROR_MESSAGE);
                 e.printStackTrace();
            } catch (SQLException e) {
                handleSqlError(e, "Delete Error");
            }
            return success;
        }

        // --- processCheckout Method ---
         public boolean processCheckout(String bookId, String customerId, int quantity, JTextArea detailsArea) {
             if (bookId == null || bookId.trim().isEmpty() || customerId == null || customerId.trim().isEmpty() || quantity <= 0) {
                 detailsArea.setText("Error: Invalid input provided for checkout.");
                 JOptionPane.showMessageDialog(null, "Invalid input. Book ID, Customer ID, and positive Quantity are required.", "Checkout Error", JOptionPane.ERROR_MESSAGE);
                 return false;
             }

             Connection conn = null;
             boolean success = false;
             BigDecimal bookPrice = BigDecimal.ZERO;
             int currentQuantity = -1;
             String transactionId = UUID.randomUUID().toString().substring(0, 10);

             try {
                 conn = getConnection();
                 conn.setAutoCommit(false); // Start transaction

                 // 1. Check book existence, get price, and lock the row
                 String checkSql = "SELECT price, quantity FROM Books WHERE bookId = ? FOR UPDATE";
                 try (PreparedStatement checkPstmt = conn.prepareStatement(checkSql)) {
                     checkPstmt.setString(1, bookId);
                     try (ResultSet rs = checkPstmt.executeQuery()) {
                         if (rs.next()) {
                             bookPrice = rs.getBigDecimal("price");
                             currentQuantity = rs.getInt("quantity");
                         } else {
                             throw new SQLException("Book with ID '" + bookId + "' not found.");
                         }
                     }
                 }

                 // 2. Check stock and update quantity
                 if (currentQuantity < quantity) {
                     throw new SQLException("Insufficient stock for Book ID '" + bookId + "'. Available: " + currentQuantity);
                 }
                 String updateSql = "UPDATE Books SET quantity = quantity - ? WHERE bookId = ?";
                 try (PreparedStatement updatePstmt = conn.prepareStatement(updateSql)) {
                     updatePstmt.setInt(1, quantity);
                     updatePstmt.setString(2, bookId);
                     int affectedRows = updatePstmt.executeUpdate();
                     if (affectedRows == 0) {
                         throw new SQLException("Failed to update book quantity (concurrent modification?).");
                     }
                 }

                 // 3. Calculate total amount
                 BigDecimal totalAmount = bookPrice.multiply(BigDecimal.valueOf(quantity));

                 // 4. Insert transaction record
                 String insertSql = "INSERT INTO Transactions (transactionId, customerId, bookId, quantity, totalAmount, dateTime) VALUES (?, ?, ?, ?, ?, NOW())";
                 try (PreparedStatement insertPstmt = conn.prepareStatement(insertSql)) {
                     insertPstmt.setString(1, transactionId);
                     insertPstmt.setString(2, customerId);
                     insertPstmt.setString(3, bookId);
                     insertPstmt.setInt(4, quantity);
                     insertPstmt.setBigDecimal(5, totalAmount);
                     insertPstmt.executeUpdate();
                 }

                 conn.commit(); // Commit transaction
                 success = true;

                 detailsArea.setText("Checkout Successful!\nTransaction ID: " + transactionId
                         + "\nBook ID: " + bookId
                         + "\nCustomer ID: " + customerId
                         + "\nQuantity: " + quantity
                         + "\nPrice per book: $" + bookPrice.toPlainString()
                         + "\nTotal Amount: $" + totalAmount.toPlainString());
                 JOptionPane.showMessageDialog(null, "Checkout processed successfully.");

             } catch (SQLException e) {
                 if (conn != null) { try { conn.rollback(); } catch (SQLException ex) { ex.printStackTrace(); } }
                 detailsArea.setText("Checkout Failed:\n" + e.getMessage());
                 handleSqlError(e, "Checkout Error");
             } finally {
                 if (conn != null) { try { conn.setAutoCommit(true); conn.close(); } catch (SQLException ex) { ex.printStackTrace(); } }
             }
             return success;
         }

        // --- getAllCustomers Method ---
        public List<Customer> getAllCustomers() {
            List<Customer> customers = new ArrayList<>();
            String sql = "SELECT customerId, name, email, phone, address FROM Customers ORDER BY name";
            try (Connection conn = getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(sql);
                 ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Customer customer = new Customer(
                            rs.getString("customerId"),
                            rs.getString("name"),
                            rs.getString("email"),
                            rs.getString("phone"),
                            rs.getString("address")
                    );
                    customers.add(customer);
                }
            } catch (SQLException e) {
                handleSqlError(e, "Error Fetching Customers");
            }
            return customers;
        }

        // --- getAllSuppliers Method ---
        public List<Supplier> getAllSuppliers() {
            List<Supplier> suppliers = new ArrayList<>();
            String sql = "SELECT supplierId, name, email, phone, companyName, address FROM Suppliers ORDER BY companyName";
            try (Connection conn = getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(sql);
                 ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Supplier supplier = new Supplier(
                            rs.getString("supplierId"),
                            rs.getString("name"),
                            rs.getString("email"),
                            rs.getString("phone"),
                            rs.getString("companyName"),
                            rs.getString("address")
                    );
                    suppliers.add(supplier);
                }
            } catch (SQLException e) {
                handleSqlError(e, "Error Fetching Suppliers");
            }
            return suppliers;
        }

        // --- getAllTransactions Method ---
        public List<Transaction> getAllTransactions() {
            List<Transaction> transactions = new ArrayList<>();
            String sql = "SELECT transactionId, customerId, bookId, quantity, totalAmount, dateTime FROM Transactions ORDER BY dateTime DESC";
            try (Connection conn = getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(sql);
                 ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Transaction transaction = new Transaction(
                            rs.getString("transactionId"),
                            rs.getString("customerId"),
                            rs.getString("bookId"),
                            rs.getInt("quantity"),
                            rs.getDouble("totalAmount"),
                            rs.getTimestamp("dateTime")
                    );
                    transactions.add(transaction);
                }
            } catch (SQLException e) {
                handleSqlError(e, "Error Fetching Transactions");
            }
            return transactions;
        }

        // --- applyFilters Method ---
         public List<Book> applyFilters(Map<String, List<String>> filters) {
             List<Book> books = new ArrayList<>();
             StringBuilder sqlBuilder = new StringBuilder("SELECT bookId, ISBN, title, author, publisher, price, quantity, genre, language FROM Books WHERE 1=1");
             List<Object> params = new ArrayList<>();

             // Price Range Filter
             List<String> priceRange = filters.getOrDefault("priceRange", new ArrayList<>());
             try {
                 // Check if minPrice exists and is not empty
                 if (priceRange.size() >= 1 && priceRange.get(0) != null && !priceRange.get(0).isEmpty()) {
                     sqlBuilder.append(" AND price >= ?");
                     params.add(new BigDecimal(priceRange.get(0).replace(',', '.'))); // Sanitize input
                 }
                  // Check if maxPrice exists and is not empty
                 if (priceRange.size() >= 2 && priceRange.get(1) != null && !priceRange.get(1).isEmpty()) {
                     sqlBuilder.append(" AND price <= ?");
                     params.add(new BigDecimal(priceRange.get(1).replace(',', '.'))); // Sanitize input
                 }
             } catch (NumberFormatException e) {
                 JOptionPane.showMessageDialog(null, "Invalid price format entered.", "Filter Error", JOptionPane.ERROR_MESSAGE);
                 // Optionally ignore price filter or return empty list
             } catch (IndexOutOfBoundsException e) {
                  // Handle case where map might contain only one price value unexpectedly
                  System.err.println("Price range filter error: Expected two values, found fewer.");
             }


             // Availability Filter ("inStock")
             List<String> availability = filters.getOrDefault("availability", new ArrayList<>());
             if (!availability.isEmpty() && "In Stock Only".equals(availability.get(0))) {
                 sqlBuilder.append(" AND quantity > 0");
             }

             // Handle multi-select filters (Language, Category) using IN clause
              addInClause(sqlBuilder, params, filters.getOrDefault("languages", new ArrayList<>()), "language");
              addInClause(sqlBuilder, params, filters.getOrDefault("categories", new ArrayList<>()), "genre"); // Assuming category maps to 'genre' column

             // TODO: Add Year Range filter if needed

             sqlBuilder.append(" ORDER BY title");

             String sql = sqlBuilder.toString();
             System.out.println("Executing Filter SQL: " + sql); // Debugging
             System.out.println("Parameters: " + params); // Debugging

             try (Connection conn = getConnection();
                  PreparedStatement pstmt = conn.prepareStatement(sql)) {
                 for (int i = 0; i < params.size(); i++) {
                     pstmt.setObject(i + 1, params.get(i));
                 }
                 try (ResultSet rs = pstmt.executeQuery()) {
                     while (rs.next()) {
                         Book book = mapResultSetToBook(rs);
                         books.add(book);
                     }
                 }
             } catch (SQLException e) {
                 handleSqlError(e, "Filter Error");
             }
             return books;
         }

         // Helper for adding IN clauses dynamically
         private void addInClause(StringBuilder sqlBuilder, List<Object> params, List<String> values, String columnName) {
             if (values != null && !values.isEmpty()) {
                 sqlBuilder.append(" AND ").append(columnName).append(" IN (");
                 for (int i = 0; i < values.size(); i++) {
                     sqlBuilder.append(i == 0 ? "?" : ", ?");
                 }
                 sqlBuilder.append(")");
                 params.addAll(values);
             }
         }

        // --- Helper Methods ---

        private Book mapResultSetToBook(ResultSet rs) throws SQLException {
            return new Book(
                    rs.getString("bookId"), rs.getString("ISBN"), rs.getString("title"),
                    rs.getString("author"), rs.getString("publisher"), rs.getDouble("price"),
                    rs.getInt("quantity"), rs.getString("genre"), rs.getString("language")
            );
        }

         private void setBookStatementParams(PreparedStatement pstmt, Book book, boolean includeBookId) throws SQLException {
             int paramIndex = 1;
             if (includeBookId) {
                 pstmt.setString(paramIndex++, book.getBookId());
             }
             pstmt.setObject(paramIndex++, book.getIsbn()); // Use setObject for potential nulls
             pstmt.setString(paramIndex++, book.getTitle());
             pstmt.setObject(paramIndex++, book.getAuthor());
             pstmt.setObject(paramIndex++, book.getPublisher());
             pstmt.setBigDecimal(paramIndex++, BigDecimal.valueOf(book.getPrice())); // Use BigDecimal
             pstmt.setInt(paramIndex++, book.getQuantity());
             pstmt.setObject(paramIndex++, book.getGenre());
             pstmt.setObject(paramIndex++, book.getLanguage());
         }

          private void setBookStatementParams(PreparedStatement pstmt, Book book) throws SQLException {
              setBookStatementParams(pstmt, book, true);
          }


         private boolean validateBook(Book book) {
             if (book == null) { JOptionPane.showMessageDialog(null, "Book data is null.", "Input Error", JOptionPane.ERROR_MESSAGE); return false; }
             if (book.getBookId() == null || book.getBookId().trim().isEmpty()) { JOptionPane.showMessageDialog(null, "Book ID is required.", "Input Error", JOptionPane.ERROR_MESSAGE); return false; }
             if (book.getTitle() == null || book.getTitle().trim().isEmpty()) { JOptionPane.showMessageDialog(null, "Book Title is required.", "Input Error", JOptionPane.ERROR_MESSAGE); return false; }
             if (book.getPrice() <= 0) { JOptionPane.showMessageDialog(null, "Price must be greater than 0.", "Input Error", JOptionPane.ERROR_MESSAGE); return false; }
             if (book.getQuantity() < 0) { JOptionPane.showMessageDialog(null, "Quantity cannot be negative.", "Input Error", JOptionPane.ERROR_MESSAGE); return false; }
             // Optional: Add length constraints if needed
             if (book.getBookId().length() > 6) { JOptionPane.showMessageDialog(null, "Book ID cannot exceed 6 characters.", "Input Error", JOptionPane.ERROR_MESSAGE); return false; }
             if (book.getIsbn() != null && book.getIsbn().length() > 13) { JOptionPane.showMessageDialog(null, "ISBN cannot exceed 13 characters.", "Input Error", JOptionPane.ERROR_MESSAGE); return false; }
             // ... add more length checks based on your table definition ...
             return true;
         }

         private void handleConstraintViolation(SQLIntegrityConstraintViolationException e, Book book) {
              String message = e.getMessage().toLowerCase();
              String userMessage = "Database Constraint Error: ";
              if (message.contains("duplicate entry")) {
                  if (message.contains("'books.primary'")) { userMessage = "Error: Book ID '" + book.getBookId() + "' already exists."; }
                  else if (message.contains("'books.isbn'")) { userMessage = "Error: ISBN '" + (book.getIsbn() == null ? "" : book.getIsbn()) + "' already exists."; } // Handle null ISBN
                  else { userMessage = "Error: Duplicate entry. A unique value is required."; }
              } else if (message.contains("foreign key constraint fails")) { userMessage = "Error: Cannot perform operation due to related records."; }
              else if (message.contains("cannot be null")) { userMessage = "Error: A required field was left empty."; }
              else if (message.contains("check constraint")) { userMessage = "Error: Data violates check constraint (e.g., Price > 0 or Quantity >= 0)."; }
              else { userMessage += e.getMessage(); }
              JOptionPane.showMessageDialog(null, userMessage, "Database Constraint Error", JOptionPane.ERROR_MESSAGE);
              e.printStackTrace();
         }

         private void handleSqlError(SQLException e, String context) {
             JOptionPane.showMessageDialog(null, "Database Error (" + context + "): " + e.getMessage(), context, JOptionPane.ERROR_MESSAGE);
             e.printStackTrace();
         }
    }
