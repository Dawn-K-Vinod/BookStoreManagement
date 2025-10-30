-- -----------------------------------------------------------------
-- Book Store Management System - Complete Database Setup
-- -----------------------------------------------------------------
-- This script will:
-- 1. Drop existing tables (if they exist) to prevent errors.
-- 2. Create all 5 tables: Books, Users, Customers, Suppliers, Transactions.
-- 3. Insert sample data for Users, Customers, Suppliers, and Transactions.
-- 4. Insert 130 sample books with diverse genres and languages.
-- -----------------------------------------------------------------

-- Specify your database name (replace if different)
USE BookStoreManagementSystem;

-- -----------------------------------------------------------------
-- Section 1: Drop and Create Tables
-- -----------------------------------------------------------------

DROP TABLE IF EXISTS Transactions;
DROP TABLE IF EXISTS Suppliers;
DROP TABLE IF EXISTS Customers;
DROP TABLE IF EXISTS Users;
DROP TABLE IF EXISTS Books;

-- Create Table: Books
CREATE TABLE Books (
  bookId varchar(6) NOT NULL,
  ISBN char(13) DEFAULT NULL,
  title varchar(255) NOT NULL, -- Increased size for longer titles
  author varchar(255) DEFAULT NULL, -- Increased size
  publisher varchar(100) DEFAULT NULL, -- Increased size
  price decimal(10,2) NOT NULL CHECK (price > 0.00),
  quantity int(11) NOT NULL CHECK (quantity >= 0),
  genre varchar(50) DEFAULT NULL, -- Increased size
  language varchar(50) DEFAULT NULL, -- Increased size
  PRIMARY KEY (bookId),
  UNIQUE KEY ISBN (ISBN)
);

-- Create Table: Users
CREATE TABLE Users (
  username varchar(30) NOT NULL,
  password varchar(50) NOT NULL, -- Increased size for future hashing
  PRIMARY KEY (username)
);

-- Create Table: Customers
CREATE TABLE Customers (
  customerId varchar(10) NOT NULL,
  name varchar(100) NOT NULL, -- Increased size
  email varchar(100) DEFAULT NULL, -- Increased size
  phone varchar(15) DEFAULT NULL, -- Changed to VARCHAR
  address varchar(255) DEFAULT NULL, -- Increased size
  PRIMARY KEY (customerId)
);

-- Create Table: Suppliers
CREATE TABLE Suppliers (
  supplierId varchar(10) NOT NULL,
  name varchar(100) NOT NULL, -- Increased size
  email varchar(100) DEFAULT NULL, -- Increased size
  phone varchar(15) DEFAULT NULL, -- Changed to VARCHAR
  companyName varchar(100) DEFAULT NULL, -- Increased size
  address varchar(255) DEFAULT NULL, -- Increased size
  PRIMARY KEY (supplierId)
);

-- Create Table: Transactions
CREATE TABLE Transactions (
  transactionId varchar(40) NOT NULL, -- Increased size for UUID
  customerId varchar(10) DEFAULT NULL,
  bookId varchar(6) DEFAULT NULL,
  quantity int(11) DEFAULT NULL,
  totalAmount decimal(10,2) DEFAULT NULL,
  dateTime timestamp NULL DEFAULT current_timestamp(),
  PRIMARY KEY (transactionId),
  KEY fk_customer (customerId),
  KEY fk_book (bookId),
  CONSTRAINT fk_customer FOREIGN KEY (customerId) REFERENCES Customers (customerId),
  CONSTRAINT fk_book FOREIGN KEY (bookId) REFERENCES Books (bookId)
);

-- -----------------------------------------------------------------
-- Section 2: Insert Sample Data for Core Tables
-- -----------------------------------------------------------------

-- Sample Users
INSERT INTO Users (username, password) VALUES
('admin', 'admin123'),
('rahul_sharma', 'rahul@123'),
('priya_nair', 'priya@321'),
('arun_krishnan', 'arun@456'),
('meera_raj', 'meera#789'),
('dawn_vinod', 'dawn@001'),
('noel_shaji', 'noel@007'),
('harisankar_cr', 'hari@999'),
('amal_shaji', 'amal@555'),
('anjali_menon', 'anjali@321');

-- Sample Customers
INSERT INTO Customers (customerId, name, email, phone, address) VALUES
('C001', 'Dawn K Vinod', 'dawn.vinod@gmail.com', 9847612387, 'Kottayam'),
('C002', 'Noel Shaji', 'noel.shaji@yahoo.com', 9895612348, 'Kottayam'),
('C003', 'Harisankar C R', 'harisankar.cr@gmail.com', 9747112399, 'Kottayam'),
('C004', 'Amal Shaji', 'amal.shaji@gmail.com', 9823112398, 'Kottayam'),
('C005', 'Anjali Menon', 'anjali.menon@gmail.com', 9847123456, 'Panampilly Nagar, Kochi'),
('C006', 'Arun Krishnan', 'arun.krishnan@yahoo.com', 9745612345, 'Vellayambalam, Thiruvananthapuram'),
('C007', 'Neethu Nair', 'neethu.nair@gmail.com', 9567129876, 'Chembukkavu, Thrissur'),
('C008', 'Rahul Mathew', 'rahul.mathew@outlook.com', 9809123456, 'Kottayam Town, Kottayam'),
('C009', 'Meera Raj', 'meera.raj@gmail.com', 9895123467, 'Palarivattom, Ernakulam'),
('C010', 'Ajay Varghese', 'ajay.varghese@gmail.com', 9746321456, 'Kaloor, Kochi'),
('C011', 'Divya Pillai', 'divya.pillai@yahoo.com', 9897123412, 'Kowdiar, Thiruvananthapuram'),
('C012', 'Vivek Thomas', 'vivek.thomas@gmail.com', 9823123499, 'East Fort, Thrissur'),
('C013', 'Lakshmi Suresh', 'lakshmi.suresh@gmail.com', 9876012399, 'Thevara, Kochi'),
('C014', 'Jithin Joseph', 'jithin.joseph@gmail.com', 9747112388, 'Mannuthy, Thrissur');

-- Sample Suppliers
INSERT INTO Suppliers (supplierId, name, email, phone, companyName, address) VALUES
('S001', 'Mohanlal', 'mohanlal@lalcreations.in', 9847012345, 'Lal Creations Pvt Ltd', 'Thevara, Kochi, Kerala'),
('S002', 'Mammootty', 'mammootty@megaent.in', 9746123456, 'Mega Star Enterprises', 'Kottayam Town, Kottayam, Kerala'),
('S003', 'Dulquer Salmaan', 'dulquer@wayfarerfilms.in', 9895123467, 'Wayfarer Films', 'Panampilly Nagar, Kochi, Kerala'),
('S004', 'Fahadh Faasil', 'fahadh@fahadhfilms.in', 9847512399, 'Fahadh Productions', 'Aluva, Ernakulam, Kerala'),
('S005', 'Manju Warrier', 'manju@manjucreations.in', 9747112356, 'Manju Creations', 'Thrissur Town, Thrissur, Kerala'),
('S006', 'Parvathy Thiruvothu', 'parvathy@parvathyfilms.in', 9823112378, 'Parvathy Films', 'Pattom, Thiruvananthapuram, Kerala'),
('S007', 'Tovino Thomas', 'tovino@tovinotalkies.in', 9867123490, 'Tovino Talkies', 'Irinjalakuda, Thrissur, Kerala'),
('S008', 'Aishwarya Lekshmi', 'aishwarya@lekshmiproductions.in', 9897012344, 'Lekshmi Productions', 'Ernakulam North, Kochi, Kerala'),
('S009', 'Prithviraj Sukumaran', 'prithvi@prithvirajfilms.in', 9746123890, 'Prithviraj Films', 'Vazhuthacaud, Thiruvananthapuram, Kerala'),
('S010', 'Nithya Menen', 'nithya@nithyastudios.in', 9823112398, 'Nithya Studios', 'Palarivattom, Kochi, Kerala'),
('S011', 'Asif Ali', 'asif@asifentertainment.in', 9747612345, 'Asif Entertainment', 'Kakkanad, Kochi, Kerala'),
('S012', 'Nazriya Nazim', 'nazriya@nazriyafilms.in', 9847012389, 'Nazriya Films', 'Fort Kochi, Ernakulam, Kerala'),
('S013', 'Kunchacko Boban', 'kunchacko@ubf.in', 9895612348, 'UBF Productions', 'Kottayam, Kerala'),
('S014', 'Rajisha Vijayan', 'rajisha@vijayanarts.in', 9747112399, 'Vijayan Arts', 'Kollam Town, Kollam, Kerala'),
('S015', 'Vineeth Sreenivasan', 'vineeth@habitfilms.in', 9823112395, 'Habit Films', 'Perumbavoor, Ernakulam, Kerala');
-- Sample Transactions (Note: These bookId/customerId must exist)
-- We will add these *after* inserting books to ensure foreign keys are valid.

-- -----------------------------------------------------------------
-- Section 3: Insert 130 Sample Books
-- -----------------------------------------------------------------
-- NEW SET of 130 sample books (B0224 to B0353)
-- 1 book for each of 13 categories and 10 languages.
-- Titles are transliterated for non-Latin languages as requested.
-- -----------------------------------------------------------------

-- --- Language: English --- (New Set: B0224 - B0236)

-- Category: Rom-Com
INSERT INTO Books (bookId, ISBN, title, author, publisher, price, quantity, genre, language) VALUES
('B0224', '9780349419184', 'The Flatshare', 'Beth O\'Leary', 'Flatiron Books', 310.00, 30, 'Rom-Com', 'English');
-- Category: Action-Adventure
INSERT INTO Books (bookId, ISBN, title, author, publisher, price, quantity, genre, language) VALUES
('B0225', '9780307475328', 'The Bourne Identity', 'Robert Ludlum', 'Bantam Books', 390.00, 25, 'Action-Adventure', 'English');
-- Category: Mystery
INSERT INTO Books (bookId, ISBN, title, author, publisher, price, quantity, genre, language) VALUES
('B0226', '9780316769488', 'The Catcher in the Rye', 'J.D. Salinger', 'Little, Brown', 290.00, 35, 'Mystery', 'English');
-- Category: Thriller
INSERT INTO Books (bookId, ISBN, title, author, publisher, price, quantity, genre, language) VALUES
('B0227', '9780307387899', 'Gone Girl', 'Gillian Flynn', 'Crown Publishing', 420.00, 30, 'Thriller', 'English');
-- Category: Science-Fiction
INSERT INTO Books (bookId, ISBN, title, author, publisher, price, quantity, genre, language) VALUES
('B0228', '9780345339737', 'Fahrenheit 451', 'Ray Bradbury', 'Del Rey Books', 270.00, 40, 'Science-Fiction', 'English');
-- Category: Fantasy
INSERT INTO Books (bookId, ISBN, title, author, publisher, price, quantity, genre, language) VALUES
('B0229', '9780765326355', 'The Way of Kings', 'Brandon Sanderson', 'Tor Books', 680.00, 20, 'Fantasy', 'English');
-- Category: Horror
INSERT INTO Books (bookId, ISBN, title, author, publisher, price, quantity, genre, language) VALUES
('B0230', '9780446329817', 'It', 'Stephen King', 'Warner Books', 590.00, 15, 'Horror', 'English');
-- Category: Biography
INSERT INTO Books (bookId, ISBN, title, author, publisher, price, quantity, genre, language) VALUES
('B0231', '9780307277671', 'A Moveable Feast', 'Ernest Hemingway', 'Scribner', 340.00, 22, 'Biography', 'English');
-- Category: Self-Help
INSERT INTO Books (bookId, ISBN, title, author, publisher, price, quantity, genre, language) VALUES
('B0232', '9780743269513', 'How to Win Friends and Influence People', 'Dale Carnegie', 'Simon & Schuster', 300.00, 50, 'Self-Help', 'English');
-- Category: History
INSERT INTO Books (bookId, ISBN, title, author, publisher, price, quantity, genre, language) VALUES
('B0233', '9780143118789', 'Guns, Germs, and Steel', 'Jared Diamond', 'Penguin Books', 480.00, 20, 'History', 'English');
-- Category: Poetry
INSERT INTO Books (bookId, ISBN, title, author, publisher, price, quantity, genre, language) VALUES
('B0234', '9780060935105', 'Leaves of Grass', 'Walt Whitman', 'HarperPerennial', 250.00, 28, 'Poetry', 'English');
-- Category: Drama
INSERT INTO Books (bookId, ISBN, title, author, publisher, price, quantity, genre, language) VALUES
('B0235', '9780141185084', 'A Streetcar Named Desire', 'Tennessee Williams', 'Penguin Plays', 230.00, 30, 'Drama', 'English');
-- Category: Children's Books
INSERT INTO Books (bookId, ISBN, title, author, publisher, price, quantity, genre, language) VALUES
('B0236', '9780064400558', 'Charlotte\'s Web', 'E.B. White', 'HarperCollins', 260.00, 45, 'Children\'s Books', 'English');

-- --- Language: Spanish --- (New Set: B0237 - B0249)

-- Category: Rom-Com
INSERT INTO Books (bookId, ISBN, title, author, publisher, price, quantity, genre, language) VALUES
('B0237', '9788408082267', 'Tres metros sobre el cielo', 'Federico Moccia', 'Planeta', 330.00, 25, 'Rom-Com', 'Spanish');
-- Category: Action-Adventure
INSERT INTO Books (bookId, ISBN, title, author, publisher, price, quantity, genre, language) VALUES
('B0238', '9788466330689', 'La Reina del Sur', 'Arturo Pérez-Reverte', 'Alfaguara', 410.00, 18, 'Action-Adventure', 'Spanish');
-- Category: Mystery
INSERT INTO Books (bookId, ISBN, title, author, publisher, price, quantity, genre, language) VALUES
('B0239', '9788423348197', 'El nombre de la rosa', 'Umberto Eco', 'Lumen', 520.00, 20, 'Mystery', 'Spanish');
-- Category: Thriller
INSERT INTO Books (bookId, ISBN, title, author, publisher, price, quantity, genre, language) VALUES
('B0240', '9788466355811', 'La Paciente Silenciosa', 'Alex Michaelides', 'Alfaguara', 470.00, 22, 'Thriller', 'Spanish');
-- Category: Science-Fiction
INSERT INTO Books (bookId, ISBN, title, author, publisher, price, quantity, genre, language) VALUES
('B0241', '9788498387896', 'Crónicas Marcianas', 'Ray Bradbury', 'Minotauro', 380.00, 15, 'Science-Fiction', 'Spanish');
-- Category: Fantasy
INSERT INTO Books (bookId, ISBN, title, author, publisher, price, quantity, genre, language) VALUES
('B0242', '9788498382556', 'El nombre del viento', 'Patrick Rothfuss', 'Plaza & Janés', 650.00, 25, 'Fantasy', 'Spanish');
-- Category: Horror
INSERT INTO Books (bookId, ISBN, title, author, publisher, price, quantity, genre, language) VALUES
('B0243', '9788497595834', 'Drácula', 'Bram Stoker', 'Debolsillo', 400.00, 17, 'Horror', 'Spanish');
-- Category: Biography
INSERT INTO Books (bookId, ISBN, title, author, publisher, price, quantity, genre, language) VALUES
('B0244', '9788499427281', 'Vivir para contarla', 'Gabriel García Márquez', 'Debolsillo', 490.00, 14, 'Biography', 'Spanish');
-- Category: Self-Help
INSERT INTO Books (bookId, ISBN, title, author, publisher, price, quantity, genre, language) VALUES
('B0245', '9788479533718', 'Los cuatro acuerdos', 'Don Miguel Ruiz', 'Urano', 290.00, 40, 'Self-Help', 'Spanish');
-- Category: History
INSERT INTO Books (bookId, ISBN, title, author, publisher, price, quantity, genre, language) VALUES
('B0246', '9788499928923', 'Homo Deus: Breve historia del mañana', 'Yuval Noah Harari', 'Debate', 660.00, 18, 'History', 'Spanish');
-- Category: Poetry
INSERT INTO Books (bookId, ISBN, title, author, publisher, price, quantity, genre, language) VALUES
('B0247', '9788437602568', 'Cántico', 'Jorge Guillén', 'Cátedra', 270.00, 20, 'Poetry', 'Spanish');
-- Category: Drama
INSERT INTO Books (bookId, ISBN, title, author, publisher, price, quantity, genre, language) VALUES
('B0248', '9788437604531', 'Don Quijote de la Mancha', 'Miguel de Cervantes', 'Cátedra', 700.00, 25, 'Drama', 'Spanish');
-- Category: Children's Books
INSERT INTO Books (bookId, ISBN, title, author, publisher, price, quantity, genre, language) VALUES
('B0249', '9788467584558', 'Donde viven los monstruos', 'Maurice Sendak', 'Kalandraka', 320.00, 30, 'Children\'s Books', 'Spanish');

-- --- Language: French --- (New Set: B0250 - B0262)

-- Category: Rom-Com
INSERT INTO Books (bookId, ISBN, title, author, publisher, price, quantity, genre, language) VALUES
('B0250', '9782253036490', 'L\'Amant', 'Marguerite Duras', 'Les Éditions de Minuit', 310.00, 22, 'Rom-Com', 'French');
-- Category: Action-Adventure
INSERT INTO Books (bookId, ISBN, title, author, publisher, price, quantity, genre, language) VALUES
('B0251', '9782070418386', 'Vingt mille lieues sous les mers', 'Jules Verne', 'Folio', 360.00, 25, 'Action-Adventure', 'French');
-- Category: Mystery
INSERT INTO Books (bookId, ISBN, title, author, publisher, price, quantity, genre, language) VALUES
('B0252', '9782253009388', 'Le Nom de la Rose', 'Umberto Eco', 'Grasset', 530.00, 15, 'Mystery', 'French');
-- Category: Thriller
INSERT INTO Books (bookId, ISBN, title, author, publisher, price, quantity, genre, language) VALUES
('B0253', '9782266281316', 'La Fille du train', 'Paula Hawkins', 'Pocket', 390.00, 28, 'Thriller', 'French');
-- Category: Science-Fiction
INSERT INTO Books (bookId, ISBN, title, author, publisher, price, quantity, genre, language) VALUES
('B0254', '9782070360050', 'Fahrenheit 451', 'Ray Bradbury', 'Folio SF', 300.00, 30, 'Science-Fiction', 'French');
-- Category: Fantasy
INSERT INTO Books (bookId, ISBN, title, author, publisher, price, quantity, genre, language) VALUES
('B0255', '9782070612662', 'Harry Potter à l\'école des sorciers', 'J.K. Rowling', 'Gallimard Jeunesse', 520.00, 35, 'Fantasy', 'French');
-- Category: Horror
INSERT INTO Books (bookId, ISBN, title, author, publisher, price, quantity, genre, language) VALUES
('B0256', '9782266207866', 'Shining', 'Stephen King', 'J\'ai lu', 410.00, 14, 'Horror', 'French');
-- Category: Biography
INSERT INTO Books (bookId, ISBN, title, author, publisher, price, quantity, genre, language) VALUES
('B0257', '9782070394192', 'L\'Enfance d\'un chef', 'Jean-Paul Sartre', 'Gallimard', 330.00, 18, 'Biography', 'French');
-- Category: Self-Help
INSERT INTO Books (bookId, ISBN, title, author, publisher, price, quantity, genre, language) VALUES
('B0258', '9782266271966', 'L\'art subtil de s\'en foutre', 'Mark Manson', 'Espaces Libres', 350.00, 30, 'Self-Help', 'French');
-- Category: History
INSERT INTO Books (bookId, ISBN, title, author, publisher, price, quantity, genre, language) VALUES
('B0259', '9782253242044', 'Sapiens: Une brève histoire de l\'humanité', 'Yuval Noah Harari', 'Albin Michel', 670.00, 16, 'History', 'French');
-- Category: Poetry
INSERT INTO Books (bookId, ISBN, title, author, publisher, price, quantity, genre, language) VALUES
('B0260', '9782070323673', 'Alcools', 'Guillaume Apollinaire', 'Folio Classique', 240.00, 26, 'Poetry', 'French');
-- Category: Drama
INSERT INTO Books (bookId, ISBN, title, author, publisher, price, quantity, genre, language) VALUES
('B0261', '9782081218055', 'Phèdre', 'Jean Racine', 'GF Flammarion', 190.00, 22, 'Drama', 'French');
-- Category: Children's Books
INSERT INTO Books (bookId, ISBN, title, author, publisher, price, quantity, genre, language) VALUES
('B0262', '9782211200021', 'Max et les Maximonstres', 'Maurice Sendak', 'L\'école des loisirs', 330.00, 32, 'Children\'s Books', 'French');

-- --- Language: German --- (New Set: B0263 - B0275)

-- Category: Rom-Com
INSERT INTO Books (bookId, ISBN, title, author, publisher, price, quantity, genre, language) VALUES
('B0263', '9783423210291', 'Stolz und Vorurteil', 'Jane Austen', 'dtv', 320.00, 22, 'Rom-Com', 'German');
-- Category: Action-Adventure
INSERT INTO Books (bookId, ISBN, title, author, publisher, price, quantity, genre, language) VALUES
('B0264', '9783423113110', 'Die Schatzinsel', 'Robert Louis Stevenson', 'dtv', 280.00, 20, 'Action-Adventure', 'German');
-- Category: Mystery
INSERT INTO Books (bookId, ISBN, title, author, publisher, price, quantity, genre, language) VALUES
('B0265', '9783453436065', 'Verblendung', 'Stieg Larsson', 'Heyne', 470.00, 25, 'Mystery', 'German');
-- Category: Thriller
INSERT INTO Books (bookId, ISBN, title, author, publisher, price, quantity, genre, language) VALUES
('B0266', '9783404169008', 'Sakrileg', 'Dan Brown', 'Bastei Lübbe', 510.00, 30, 'Thriller', 'German');
-- Category: Science-Fiction
INSERT INTO Books (bookId, ISBN, title, author, publisher, price, quantity, genre, language) VALUES
('B0267', '9783453313376', 'Der Wüstenplanet', 'Frank Herbert', 'Heyne', 560.00, 18, 'Science-Fiction', 'German');
-- Category: Fantasy
INSERT INTO Books (bookId, ISBN, title, author, publisher, price, quantity, genre, language) VALUES
('B0268', '9783608939815', 'Der Hobbit', 'J.R.R. Tolkien', 'Klett-Cotta', 430.00, 28, 'Fantasy', 'German');
-- Category: Horror
INSERT INTO Books (bookId, ISBN, title, author, publisher, price, quantity, genre, language) VALUES
('B0269', '9783453435648', 'Frankenstein', 'Mary Shelley', 'Heyne', 350.00, 15, 'Horror', 'German');
-- Category: Biography
INSERT INTO Books (bookId, ISBN, title, author, publisher, price, quantity, genre, language) VALUES
('B0270', '9783442754589', 'Steve Jobs: Die autorisierte Biografie', 'Walter Isaacson', 'C. Bertelsmann', 760.00, 12, 'Biography', 'German');
-- Category: Self-Help
INSERT INTO Books (bookId, ISBN, title, author, publisher, price, quantity, genre, language) VALUES
('B0271', '9783868828775', 'Die subtile Kunst des Daraufscheißens', 'Mark Manson', 'mvg Verlag', 440.00, 38, 'Self-Help', 'German');
-- Category: History
INSERT INTO Books (bookId, ISBN, title, author, publisher, price, quantity, genre, language) VALUES
('B0272', '9783827011681', 'Der Hundertjährige, der aus dem Fenster stieg', 'Jonas Jonasson', 'Carl\'s Books', 500.00, 20, 'History', 'German');
-- Category: Poetry
INSERT INTO Books (bookId, ISBN, title, author, publisher, price, quantity, genre, language) VALUES
('B0273', '9783518222007', 'Duineser Elegien', 'Rainer Maria Rilke', 'Suhrkamp', 260.00, 24, 'Poetry', 'German');
-- Category: Drama
INSERT INTO Books (bookId, ISBN, title, author, publisher, price, quantity, genre, language) VALUES
('B0274', '9783150000034', 'Nathan der Weise', 'Gotthold Ephraim Lessing', 'Reclam', 160.00, 29, 'Drama', 'German');
-- Category: Children's Books
INSERT INTO Books (bookId, ISBN, title, author, publisher, price, quantity, genre, language) VALUES
('B0275', '9783551354013', 'Die kleine Raupe Nimmersatt', 'Eric Carle', 'Gerstenberg', 280.00, 42, 'Children\'s Books', 'German');

-- --- Language: Malayalam --- (New Set: B0276 - B0288)

-- Category: Rom-Com
INSERT INTO Books (bookId, ISBN, title, author, publisher, price, quantity, genre, language) VALUES
('B0276', '9788171300992', 'Mathilukal', 'Vaikom Muhammad Basheer', 'DC Books', 110.00, 32, 'Rom-Com', 'Malayalam');
-- Category: Action-Adventure
INSERT INTO Books (bookId, ISBN, title, author, publisher, price, quantity, genre, language) VALUES
('B0277', '9788171307375', 'Dharmaraja', 'C. V. Raman Pillai', 'DC Books', 340.00, 18, 'Action-Adventure', 'Malayalam');
-- Category: Mystery
INSERT INTO Books (bookId, ISBN, title, author, publisher, price, quantity, genre, language) VALUES
('B0278', '9789389351513', 'Alchemist (New Malayalam Translation)', 'Paulo Coelho', 'Green Books', 205.00, 38, 'Mystery', 'Malayalam');
-- Category: Thriller
INSERT INTO Books (bookId, ISBN, title, author, publisher, price, quantity, genre, language) VALUES
('B0279', '9788126419302', 'Oru Police Surgeonte Ormakurippukal (Vol 2)', 'Dr. B. Umadathan', 'DC Books', 235.00, 20, 'Thriller', 'Malayalam');
-- Category: Science-Fiction
INSERT INTO Books (bookId, ISBN, title, author, publisher, price, quantity, genre, language) VALUES
('B0280', '9788171307879', 'Professor Shonku (New Edition)', 'Satyajit Ray', 'DC Books', 195.00, 16, 'Science-Fiction', 'Malayalam');
-- Category: Fantasy
INSERT INTO Books (bookId, ISBN, title, author, publisher, price, quantity, genre, language) VALUES
('B0281', '9789388856756', 'Francis Itty Cora (New Edition)', 'T. D. Ramakrishnan', 'DC Books', 455.00, 14, 'Fantasy', 'Malayalam');
-- Category: Horror
INSERT INTO Books (bookId, ISBN, title, author, publisher, price, quantity, genre, language) VALUES
('B0282', '9788182663953', 'Neermathalam Poothakalam', 'Kamala Surayya', 'Mathrubhumi Books', 180.00, 24, 'Horror', 'Malayalam');
-- Category: Biography
INSERT INTO Books (bookId, ISBN, title, author, publisher, price, quantity, genre, language) VALUES
('B0283', '9788171300825', 'Oru Desathinte Katha (Hardcover)', 'S. K. Pottekkatt', 'DC Books', 425.00, 19, 'Biography', 'Malayalam');
-- Category: Self-Help
INSERT INTO Books (bookId, ISBN, title, author, publisher, price, quantity, genre, language) VALUES
('B0284', '9788182647618', 'Ormakalude Bhramanapadham', 'Gopinath Muthukad', 'Mathrubhumi Books', 185.00, 22, 'Self-Help', 'Malayalam');
-- Category: History
INSERT INTO Books (bookId, ISBN, title, author, publisher, price, quantity, genre, language) VALUES
('B0285', '9788126418343', 'Keralam: Oru Charithram (Reprint)', 'M. G. S. Narayanan', 'Current Books', 285.00, 14, 'History', 'Malayalam');
-- Category: Poetry
INSERT INTO Books (bookId, ISBN, title, author, publisher, price, quantity, genre, language) VALUES
('B0286', '9788171301662', 'Karutha Pakshiyude Pattu', 'O. N. V. Kurup', 'DC Books', 155.00, 23, 'Poetry', 'Malayalam');
-- Category: Drama
INSERT INTO Books (bookId, ISBN, title, author, publisher, price, quantity, genre, language) VALUES
('B0287', '9788171301136', 'Indulekha (New Edition)', 'O. Chandu Menon', 'DC Books', 185.00, 30, 'Drama', 'Malayalam');
-- Category: Children's Books
INSERT INTO Books (bookId, ISBN, title, author, publisher, price, quantity, genre, language) VALUES
('B0288', '9788182650021', 'Kuttan Padhathil (Reprint)', 'Sumangala', 'Mathrubhumi Books', 95.00, 33, 'Children\'s Books', 'Malayalam');

-- --- Language: Hindi --- (New Set: B0289 - B0301)

-- Category: Rom-Com
INSERT INTO Books (bookId, ISBN, title, author, publisher, price, quantity, genre, language) VALUES
('B0289', '9789353491763', 'Gunahon Ka Devta', 'Dharamvir Bharati', 'Bharati Sahitya', 170.00, 28, 'Rom-Com', 'Hindi');
-- Category: Action-Adventure
INSERT INTO Books (bookId, ISBN, title, author, publisher, price, quantity, genre, language) VALUES
('B0290', '9789389143223', 'The Immortals of Meluha (Hindi)', 'Amish Tripathi', 'Westland', 355.00, 35, 'Action-Adventure', 'Hindi');
-- Category: Mystery
INSERT INTO Books (bookId, ISBN, title, author, publisher, price, quantity, genre, language) VALUES
('B0291', '9788183222046', 'Byomkesh Bakshi Ki Kahaniyan', 'Saradindu Bandyopadhyay', 'Prabhat Prakashan', 260.00, 20, 'Mystery', 'Hindi');
-- Category: Thriller
INSERT INTO Books (bookId, ISBN, title, author, publisher, price, quantity, genre, language) VALUES
('B0292', '9789389977054', 'Angarey', 'Various', 'Rajpal & Sons', 230.00, 17, 'Thriller', 'Hindi');
-- Category: Science-Fiction
INSERT INTO Books (bookId, ISBN, title, author, publisher, price, quantity, genre, language) VALUES
('B0293', '9789350094945', 'Dune (Hindi)', 'Frank Herbert', 'Manjul Publishing', 490.00, 14, 'Science-Fiction', 'Hindi');
-- Category: Fantasy
INSERT INTO Books (bookId, ISBN, title, author, publisher, price, quantity, genre, language) VALUES
('B0294', '9789387578859', 'Nagaon ka Rahasya (Shiva Trilogy 2)', 'Amish Tripathi', 'Westland', 360.00, 38, 'Fantasy', 'Hindi');
-- Category: Horror
INSERT INTO Books (bookId, ISBN, title, author, publisher, price, quantity, genre, language) VALUES
('B0295', '9789387779087', 'Bhootnath', 'Devaki Nandan Khatri', 'Rajkamal Prakashan', 290.00, 16, 'Horror', 'Hindi');
-- Category: Biography
INSERT INTO Books (bookId, ISBN, title, author, publisher, price, quantity, genre, language) VALUES
('B0296', '9788170289076', 'My Experiments with Truth (Satya Ke Prayog)', 'Mahatma Gandhi', 'Navajivan Trust', 180.00, 40, 'Biography', 'Hindi');
-- Category: Self-Help
INSERT INTO Books (bookId, ISBN, title, author, publisher, price, quantity, genre, language) VALUES
('B0297', '9788183220301', 'Jeet Aapki (You Can Win)', 'Shiv Khera', 'Full Circle', 310.00, 50, 'Self-Help', 'Hindi');
-- Category: History
INSERT INTO Books (bookId, ISBN, title, author, publisher, price, quantity, genre, language) VALUES
('B0298', '9789381668263', 'Raag Darbari', 'Shrilal Shukla', 'Rajkamal Prakashan', 330.00, 22, 'History', 'Hindi');
-- Category: Poetry
INSERT INTO Books (bookId, ISBN, title, author, publisher, price, quantity, genre, language) VALUES
('B0299', '9788181438258', 'Gitanjali', 'Rabindranath Tagore', 'Maple Press', 160.00, 30, 'Poetry', 'Hindi');
-- Category: Drama
INSERT INTO Books (bookId, ISBN, title, author, publisher, price, quantity, genre, language) VALUES
('B0300', '9788126700819', 'Nirmala', 'Munshi Premchand', 'Rajkamal Prakashan', 140.00, 27, 'Drama', 'Hindi');
-- Category: Children's Books
INSERT INTO Books (bookId, ISBN, title, author, publisher, price, quantity, genre, language) VALUES
('B0301', '9788170282916', 'Malgudi Days (Hindi)', 'R.K. Narayan', 'Penguin India', 200.00, 35, 'Children\'s Books', 'Hindi');

-- --- Language: Tamil --- (New Set: B0302 - B0314)

-- Category: Rom-Com
INSERT INTO Books (bookId, ISBN, title, author, publisher, price, quantity, genre, language) VALUES
('B0302', '9789389977061', 'Vellai Yaanai', 'S. Ramakrishnan', 'Uyirmmai Pathippagam', 340.00, 18, 'Rom-Com', 'Tamil');
-- Category: Action-Adventure
INSERT INTO Books (bookId, ISBN, title, author, publisher, price, quantity, genre, language) VALUES
('B0303', '9788184760012', 'Parthiban Kanavu (New Edition)', 'Kalki Krishnamurthy', 'Vanathi Pathippagam', 355.00, 20, 'Action-Adventure', 'Tamil');
-- Category: Mystery
INSERT INTO Books (bookId, ISBN, title, author, publisher, price, quantity, genre, language) VALUES
('B0304', '9788184930106', 'En Iniya Iyanthira (Reprint)', 'Sujatha', 'Viking', 255.00, 17, 'Mystery', 'Tamil');
-- Category: Thriller
INSERT INTO Books (bookId, ISBN, title, author, publisher, price, quantity, genre, language) VALUES
('B0305', '9789381668219', 'Kolaiyuthir Kalam', 'Sujatha', 'Viking', 285.00, 20, 'Thriller', 'Tamil');
-- Category: Science-Fiction
INSERT INTO Books (bookId, ISBN, title, author, publisher, price, quantity, genre, language) VALUES
('B0306', '9788184930212', 'Aah...!', 'Sujatha', 'Viking', 265.00, 16, 'Science-Fiction', 'Tamil');
-- Category: Fantasy
INSERT INTO Books (bookId, ISBN, title, author, publisher, price, quantity, genre, language) VALUES
('B0307', '9789386862595', 'Ragasiyamai Oru Ragasiyam', 'Indra Soundar Rajan', 'Kizhakku', 225.00, 14, 'Fantasy', 'Tamil');
-- Category: Horror
INSERT INTO Books (bookId, ISBN, title, author, publisher, price, quantity, genre, language) VALUES
('B0308', '9788184490339', 'Marma Novelgal', 'Sujatha', 'Vikatan Prasuram', 200.00, 15, 'Horror', 'Tamil');
-- Category: Biography
INSERT INTO Books (bookId, ISBN, title, author, publisher, price, quantity, genre, language) VALUES
('B0309', '9789380026772', 'Karuvachi Kaaviyam', 'Vairamuthu', 'Surya Literature', 300.00, 19, 'Biography', 'Tamil');
-- Category: Self-Help
INSERT INTO Books (bookId, ISBN, title, author, publisher, price, quantity, genre, language) VALUES
('B0310', '9788184764836', 'Indhiya 2020', 'A.P.J. Abdul Kalam', 'Kannan Puthakasalai', 210.00, 32, 'Self-Help', 'Tamil');
-- Category: History
INSERT INTO Books (bookId, ISBN, title, author, publisher, price, quantity, genre, language) VALUES
('B0311', '9788172540027', 'Sivagamiyin Sapatham (New Edition)', 'Kalki Krishnamurthy', 'Vanathi Pathippagam', 655.00, 18, 'History', 'Tamil');
-- Category: Poetry
INSERT INTO Books (bookId, ISBN, title, author, publisher, price, quantity, genre, language) VALUES
('B0312', '9789380026000', 'Kavithaigal', 'Bharathiyar', 'Surya Literature', 170.00, 35, 'Poetry', 'Tamil');
-- Category: Drama
INSERT INTO Books (bookId, ISBN, title, author, publisher, price, quantity, genre, language) VALUES
('B0313', '9788184492418', 'Moonram Ulagapor', 'Vairamuthu', 'Surya Literature', 275.00, 22, 'Drama', 'Tamil');
-- Category: Children's Books
INSERT INTO Books (bookId, ISBN, title, author, publisher, price, quantity, genre, language) VALUES
('B0314', '9788184490056', 'Panchathanthira Kathaigal', 'Various', 'Vikatan Prasuram', 135.00, 33, 'Children\'s Books', 'Tamil');

-- --- Language: Telugu --- (New Set: B0315 - B0327)

-- Category: Rom-Com
INSERT INTO Books (bookId, ISBN, title, author, publisher, price, quantity, genre, language) VALUES
('B0315', '9788172240184', 'Jeevana Tarangalu', 'Yaddanapudi Sulochana Rani', 'Emsco Books', 185.00, 28, 'Rom-Com', 'Telugu');
-- Category: Action-Adventure
INSERT INTO Books (bookId, ISBN, title, author, publisher, price, quantity, genre, language) VALUES
('B0316', '9789380619166', 'Chatrapathi Sivaji', 'Various', 'Westland', 325.00, 22, 'Action-Adventure', 'Telugu');
-- Category: Mystery
INSERT INTO Books (bookId, ISBN, title, author, publisher, price, quantity, genre, language) VALUES
('B0317', '9788184950661', 'Shadow', 'Malladi Venkata Krishna Murthy', 'Navodaya Book House', 245.00, 20, 'Mystery', 'Telugu');
-- Category: Thriller
INSERT INTO Books (bookId, ISBN, title, author, publisher, price, quantity, genre, language) VALUES
('B0318', '9789352642233', 'One Indian Girl (Telugu)', 'Chetan Bhagat', 'Rupa Publications', 205.00, 24, 'Thriller', 'Telugu');
-- Category: Science-Fiction
INSERT INTO Books (bookId, ISBN, title, author, publisher, price, quantity, genre, language) VALUES
('B0319', '9788172240412', 'Yugasandhi', 'Yandamuri Veerendranath', 'Emsco Books', 215.00, 16, 'Science-Fiction', 'Telugu');
-- Category: Fantasy
INSERT INTO Books (bookId, ISBN, title, author, publisher, price, quantity, genre, language) VALUES
('B0320', '9788184950159', 'Chandamama Kathalu (Part 2)', 'Various', 'Navodaya Book House', 305.00, 32, 'Fantasy', 'Telugu');
-- Category: Horror
INSERT INTO Books (bookId, ISBN, title, author, publisher, price, quantity, genre, language) VALUES
('B0321', '9788172240115', 'Raktha Pipasi', 'Yandamuri Veerendranath', 'Emsco Books', 195.00, 14, 'Horror', 'Telugu');
-- Category: Biography
INSERT INTO Books (bookId, ISBN, title, author, publisher, price, quantity, genre, language) VALUES
('B0322', '9788172240046', 'Malgudi Kathalu (Malgudi Days)', 'R.K. Narayan', 'Emsco Books', 190.00, 30, 'Biography', 'Telugu');
-- Category: Self-Help
INSERT INTO Books (bookId, ISBN, title, author, publisher, price, quantity, genre, language) VALUES
('B0323', '9788184950012', 'Vijayaniki Aidu Metlu', 'Yandamuri Veerendranath', 'Navodaya Book House', 175.00, 38, 'Self-Help', 'Telugu');
-- Category: History
INSERT INTO Books (bookId, ISBN, title, author, publisher, price, quantity, genre, language) VALUES
('B0324', '9788172240214', 'Veyipadagalu', 'Viswanatha Satyanarayana', 'Emsco Books', 700.00, 12, 'History', 'Telugu');
-- Category: Poetry
INSERT INTO Books (bookId, ISBN, title, author, publisher, price, quantity, genre, language) VALUES
('B0325', '9788126710406', 'Amrutham Kurisina Rathri', 'Devulapalli Krishna Sastri', 'Visalaandhra', 125.00, 32, 'Poetry', 'Telugu');
-- Category: Drama
INSERT INTO Books (bookId, ISBN, title, author, publisher, price, quantity, genre, language) VALUES
('B0326', '9788172240030', 'Maa Bhoomi', 'Sunkara Satyanarayana', 'Emsco Books', 165.00, 28, 'Drama', 'Telugu');
-- Category: Children's Books
INSERT INTO Books (bookId, ISBN, title, author, publisher, price, quantity, genre, language) VALUES
('B0327', '9788184950029', 'Akbar Birbal Kathalu', 'Various', 'Navodaya Book House', 115.00, 42, 'Children\'s Books', 'Telugu');

-- --- Language: Chinese --- (New Set: B0328 - B0340)

-- Category: Rom-Com
INSERT INTO Books (bookId, ISBN, title, author, publisher, price, quantity, genre, language) VALUES
('B0328', '9787530219431', 'Ban Sheng Yuan', 'Eileen Chang', 'Beijing October Art', 325.00, 18, 'Rom-Com', 'Chinese');
-- Category: Action-Adventure
INSERT INTO Books (bookId, ISBN, title, author, publisher, price, quantity, genre, language) VALUES
('B0329', '9787508678369', 'Tian Long Ba Bu (Demi-Gods and Semi-Devils)', 'Jin Yong', 'CITIC Press', 555.00, 22, 'Action-Adventure', 'Chinese');
-- Category: Mystery
INSERT INTO Books (bookId, ISBN, title, author, publisher, price, quantity, genre, language) VALUES
('B0330', '9787530215006', 'Xian Yi Ren X De Xian Shen', 'Keigo Higashino', 'Nanhai Publishing', 415.00, 20, 'Mystery', 'Chinese');
-- Category: Thriller
INSERT INTO Books (bookId, ISBN, title, author, publisher, price, quantity, genre, language) VALUES
('B0331', '9787544258608', 'Sha Po Lang', 'Priest', 'Nanhai Publishing', 405.00, 24, 'Thriller', 'Chinese');
-- Category: Science-Fiction
INSERT INTO Books (bookId, ISBN, title, author, publisher, price, quantity, genre, language) VALUES
('B0332', '9787536692931', 'Hei An Sen Lin (The Dark Forest)', 'Cixin Liu', 'Chongqing Publishing', 485.00, 32, 'Science-Fiction', 'Chinese');
-- Category: Fantasy
INSERT INTO Books (bookId, ISBN, title, author, publisher, price, quantity, genre, language) VALUES
('B0333', '9787020024755', 'Feng Shen Yan Yi (Investiture of the Gods)', 'Xu Zhonglin', 'People\'s Literature', 385.00, 27, 'Fantasy', 'Chinese');
-- Category: Horror
INSERT INTO Books (bookId, ISBN, title, author, publisher, price, quantity, genre, language) VALUES
('B0334', '9787539989915', 'Liao Zhai Zhi Yi', 'Pu Songling', 'Qunyan Press', 425.00, 17, 'Horror', 'Chinese');
-- Category: Biography
INSERT INTO Books (bookId, ISBN, title, author, publisher, price, quantity, genre, language) VALUES
('B0335', '9787506365438', 'Chen Zhong Ji', 'Yang Jiang', 'SDX Joint Publishing', 305.00, 22, 'Biography', 'Chinese');
-- Category: Self-Help
INSERT INTO Books (bookId, ISBN, title, author, publisher, price, quantity, genre, language) VALUES
('B0336', '9787550263933', 'Fu Ba Ba Qiong Ba Ba (Rich Dad Poor Dad)', 'Robert Kiyosaki', 'Peking University Press', 515.00, 20, 'Self-Help', 'Chinese');
-- Category: History
INSERT INTO Books (bookId, ISBN, title, author, publisher, price, quantity, genre, language) VALUES
('B0337', '9787101003049', 'San Guo Zhi (Records of the Three Kingdoms)', 'Chen Shou', 'Zhonghua Book Company', 805.00, 12, 'History', 'Chinese');
-- Category: Poetry
INSERT INTO Books (bookId, ISBN, title, author, publisher, price, quantity, genre, language) VALUES
('B0338', '9787532767074', 'Xin Yue Ji (The Crescent Moon)', 'Rabindranath Tagore', 'Shanghai Translation', 225.00, 28, 'Poetry', 'Chinese');
-- Category: Drama
INSERT INTO Books (bookId, ISBN, title, author, publisher, price, quantity, genre, language) VALUES
('B0339', '9787020040120', 'Lei Yu (Thunderstorm)', 'Cao Yu', 'People\'s Literature', 195.00, 30, 'Drama', 'Chinese');
-- Category: Children's Books
INSERT INTO Books (bookId, ISBN, title, author, publisher, price, quantity, genre, language) VALUES
('B0340', '9787533257775', 'Cai Mi Tu Hua Shu (Spot\'s First Walk)', 'Eric Hill', 'Tomorrow Publishing', 155.00, 42, 'Children\'s Books', 'Chinese');

-- --- Language: Japanese --- (New Set: B0341 - B0353)

-- Category: Rom-Com
INSERT INTO Books (bookId, ISBN, title, author, publisher, price, quantity, genre, language) VALUES
('B0341', '9784062752049', 'Sekai no Chuushin de, Ai o Sakebu', 'Kyoichi Katayama', 'Kodansha', 455.00, 28, 'Rom-Com', 'Japanese');
-- Category: Action-Adventure
INSERT INTO Books (bookId, ISBN, title, author, publisher, price, quantity, genre, language) VALUES
('B0342', '9784088707001', 'Naruto (Vol. 1)', 'Masashi Kishimoto', 'Shueisha', 305.00, 42, 'Action-Adventure', 'Japanese');
-- Category: Mystery
INSERT INTO Books (bookId, ISBN, title, author, publisher, price, quantity, genre, language) VALUES
('B0343', '9784167181051', 'Hakuba Sanso Satsujin Jiken', 'Keigo Higashino', 'Bungeishunju', 425.00, 32, 'Mystery', 'Japanese');
-- Category: Thriller
INSERT INTO Books (bookId, ISBN, title, author, publisher, price, quantity, genre, language) VALUES
('B0344', '9784088708848', 'Shingeki no Kyojin (Attack on Titan Vol. 1)', 'Hajime Isayama', 'Kodansha', 315.00, 30, 'Thriller', 'Japanese');
-- Category: Science-Fiction
INSERT INTO Books (bookId, ISBN, title, author, publisher, price, quantity, genre, language) VALUES
('B0345', '9784150115609', 'Akira (Vol. 1)', 'Katsuhiro Otomo', 'Kodansha', 555.00, 18, 'Science-Fiction', 'Japanese');
-- Category: Fantasy
INSERT INTO Books (bookId, ISBN, title, author, publisher, price, quantity, genre, language) VALUES
('B0346', '9784041026132', 'Tonari no Totoro (My Neighbor Totoro)', 'Hayao Miyazaki', 'Tokuma Shoten', 385.00, 24, 'Fantasy', 'Japanese');
-- Category: Horror
INSERT INTO Books (bookId, ISBN, title, author, publisher, price, quantity, genre, language) VALUES
('B0347', '9784041031785', 'Uzumaki (Vol. 1)', 'Junji Ito', 'Shogakukan', 405.00, 20, 'Horror', 'Japanese');
-- Category: Biography
INSERT INTO Books (bookId, ISBN, title, author, publisher, price, quantity, genre, language) VALUES
('B0348', '9784167900151', 'Ningen Shikkaku (No Longer Human)', 'Osamu Dazai', 'Shinchosha', 495.00, 18, 'Biography', 'Japanese');
-- Category: Self-Help
INSERT INTO Books (bookId, ISBN, title, author, publisher, price, quantity, genre, language) VALUES
('B0349', '9784837927680', 'Jinsei ga Tokimeku Katazuke no Mahou', 'Marie Kondo', 'Sunmark Publishing', 465.00, 33, 'Self-Help', 'Japanese');
-- Category: History
INSERT INTO Books (bookId, ISBN, title, author, publisher, price, quantity, genre, language) VALUES
('B0350', '9784101152072', 'Saka no Ue no Kumo', 'Ryotaro Shiba', 'Bungeishunju', 525.00, 16, 'History', 'Japanese');
-- Category: Poetry
INSERT INTO Books (bookId, ISBN, title, author, publisher, price, quantity, genre, language) VALUES
('B0351', '9784003100314', 'Man\'yoshu', 'Various', 'Iwanami Shoten', 255.00, 27, 'Poetry', 'Japanese');
-- Category: Drama
INSERT INTO Books (bookId, ISBN, title, author, publisher, price, quantity, genre, language) VALUES
('B0352', '9784101001019', 'Botchan', 'Natsume Soseki', 'Shinchosha', 285.00, 30, 'Drama', 'Japanese');
-- Category: Children's Books
INSERT INTO Books (bookId, ISBN, title, author, publisher, price, quantity, genre, language) VALUES
('B0353', '9784834000826', 'Sora Iro no Tane', 'Rieko Nakagawa', 'Fukuinkan Shoten', 305.00, 38, 'Children\'s Books', 'Japanese');

-- -----------------------------------------------------------------
-- Section 4: Insert Sample Transactions (using new Book IDs)
-- -----------------------------------------------------------------

INSERT INTO Transactions (transactionId, customerId, bookId, quantity, totalAmount, dateTime) VALUES
('T001', 'customer001', 'B0002', 1, 650.00, '2025-10-15 10:15:00'),
('T002', 'customer004', 'B0008', 2, 1598.00, '2025-10-15 11:30:00'),
('T003', 'customer007', 'B0003', 1, 899.00, '2025-10-16 09:20:00'),
('T004', 'customer002', 'B0005', 1, 950.00, '2025-10-16 14:40:00'),
('T005', 'customer010', 'B0001', 2, 998.00, '2025-10-17 10:10:00'),
('T006', 'customer008', 'B0009', 1, 599.00, '2025-10-17 16:45:00'),
('T007', 'customer006', 'B0004', 1, 799.00, '2025-10-18 13:25:00'),
('T008', 'customer005', 'B0007', 3, 1050.00, '2025-10-18 18:00:00'),
('T009', 'customer012', 'B0010', 1, 699.00, '2025-10-19 09:50:00'),
('T010', 'customer014', 'B0006', 2, 2100.00, '2025-10-19 17:20:00');
