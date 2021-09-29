package controller;


import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import library.Books;


public class MainController implements Initializable { /*ALLOWS AUTOMATIC INJECTION OF LOCATION AND RESOURCES
    PROPERTIES INTO THE CONTROLLER.  FXMLLoader will now automatically call any suitably annotated
    no-arg initialize() method defined by the controller. */

    /* TEXT FIELDS, TABLE VIEW WITH TABLE COLUMNS, AND BUTTONS INTEGRATED FROM SCENE BUILDER
    WITH THEIR FX:IDS AND FUNCTIONS */
    @FXML
    private TextField tfId;
    @FXML
    private TextField tfTitle;
    @FXML
    private TextField tfAuthor;
    @FXML
    private TextField tfYear;
    @FXML
    private TextField tfPages;
    @FXML
    private TableView<Books> tvBooks;
    @FXML
    private TableColumn<Books, Integer> colId;
    @FXML
    private TableColumn<Books, String> colTitle;
    @FXML
    private TableColumn<Books, String> colAuthor;
    @FXML
    private TableColumn<Books, Integer> colYear;
    @FXML
    private TableColumn<Books, Integer> colPages;
    @FXML
    private Button btnInsert;
    @FXML
    private Button btnUpdate;
    @FXML
    private Button btnDelete;

    @FXML
    private void handleButtonAction(ActionEvent event) { // ASSIGNS BEHAVIOR TO BUTTONS ON THE UI.

        if(event.getSource() == btnInsert){
            insertRecord();
        }else if (event.getSource() == btnUpdate){
            updateRecord();
        }else if(event.getSource() == btnDelete){
            deleteRecord();
        }

    }


    @Override
    // INITIALIZE INTERFACE ALLOWS TO INTERACT WITH VARIABLES INJECTED WITH @FXML.
    public void initialize(URL url, ResourceBundle rb) {
        showBooks(); /*EVERYTIME THE PROGRAM RUNS, IT WILL SHOW THE LIST OF BOOKS AS RETURNED BY
        showBooks METHOD*/
    }

    // CREATING METHOD getConnection TO CONNECT AND ACCESS DATABASE EVERYTIME.
    public Connection getConnection(){
        Connection conn;
        try{
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/yajibbb", "root","SiGMsDCr7");
            return conn;
        }catch(Exception ex){
            System.out.println("Error: " + ex.getMessage());
            return null;
        }
    }

    /* CREATING METHOD getBooksList WITH ObservableList TO FETCH THE RECORDS FROM THE DATABASE.
    ObservableList ALLOWS listener TO TRACK CHANGES.*/
    public ObservableList<Books> getBooksList(){
        // RETURNS booklist WITH ALL FETCHED RECORDS.
        ObservableList<Books> bookList = FXCollections.observableArrayList();
        Connection conn = getConnection();
        String query = "SELECT * FROM books";
        Statement statement; // statement NOW PROVIDES INTERFACE AND FEW METHODS THROUGH WHICH WE CAN SUBMIT SQL QUERIES.
        ResultSet resultSet; // resultSet NOW REPRESENTS A TABLE OF DATA FETCHED FROM OUR DATABASE AS EXECUTED THROUGH QUERY.

        try{
            statement = conn.createStatement(); // IMPLEMENTING STATEMENT statement.
            resultSet = statement.executeQuery(query); // EXECUTING query STATEMENT WE SPECIFIED ABOVE and FETCHING DATA IN resultSet.
            Books books;

            // ITERATING THE ResultSet resultSet or WHILE THE ResultSet resultSet HAS ELEMENTS OR DATA.
            while(resultSet.next()){

                // CREATING NEW INSTANCE OF books THROUGH BOOKS CONSTRUCTOR.
                books = new Books(resultSet.getInt("id"), resultSet.getString("title"),
                        resultSet.getString("author"), resultSet.getInt("year"),
                        resultSet.getInt("pages")); // id,title,author,year AND pages HERE PRETENDS TO BE COLUMNS IN OUR DATABASE.

                bookList.add(books); //ADDING FETCHED DATA INTO bookList
            }

        }catch(Exception ex){
            ex.printStackTrace();
        }
        return bookList; // RETURNS THE LIST OF BOOKS FETCHED FROM DATABASE BY THE getBooksList METHOD.
    }

    // CREATING METHOD TO DISPLAY ALL DATA IN TABLEVIEW.
    public void showBooks(){
        ObservableList<Books> list = getBooksList();

        // ASSIGNS DATA STORED UNDER id IN DATABASE RETURNED BY getBooksList() IN COLUMN WHOSE fx:id IS SET "colId"
        colId.setCellValueFactory(new PropertyValueFactory<Books, Integer>("id"));

        // ASSIGNS DATA STORED UNDER title IN DATABASE RETURNED BY getBooksList() IN COLUMN WHOSE fx:id IS SET "colTitle"
        colTitle.setCellValueFactory(new PropertyValueFactory<Books, String>("title"));

        // ASSIGNS DATA STORED UNDER author IN DATABASE RETURNED BY getBooksList() IN COLUMN WHOSE fx:id IS SET "colAuthor"
        colAuthor.setCellValueFactory(new PropertyValueFactory<Books, String>("author"));

        // ASSIGNS DATA STORED UNDER year IN DATABASE RETURNED BY getBooksList() IN COLUMN WHOSE fx:id IS SET "colYear"
        colYear.setCellValueFactory(new PropertyValueFactory<Books, Integer>("year"));

        // ASSIGNS DATA STORED UNDER pages IN DATABASE RETURNED BY getBooksList() IN COLUMN WHOSE fx:id IS SET "colPages"
        colPages.setCellValueFactory(new PropertyValueFactory<Books, Integer>("pages"));

        // SHOWS ALL THE ABOVE DATA IN TABLEVIEW
        tvBooks.setItems(list);
    }

    // CREATING METHOD TO INSERT DATA
    private void insertRecord(){
        Connection connection = getConnection();
        PreparedStatement stmt;
        try{
            stmt = connection.prepareStatement("INSERT INTO books VALUES(?,?,?,?,?)");
            stmt.setInt(1, Integer.parseInt(tfId.getText()));
            stmt.setString(2, tfTitle.getText());
            stmt.setString(3, tfAuthor.getText());
            stmt.setInt(4, Integer.parseInt(tfYear.getText()));
            stmt.setInt(5, Integer.parseInt(tfPages.getText()));
            int i = stmt.executeUpdate();
            System.out.println(i);
            connection.close();

        }catch (Exception e){
            System.out.println();
        }
        showBooks();
    }

    // CREATING METHOD TO DELETE DATA
    private void deleteRecord() {
        Connection connection = getConnection();
        PreparedStatement stmt;

        try {
            stmt=connection.prepareStatement("delete from books where id=?");
            stmt.setInt(1,Integer.parseInt(tfId.getText()));

            int i=stmt.executeUpdate();
            System.out.println(i+" records deleted");
        }catch (Exception e){
            System.out.println();
        }
        showBooks();
    }

    // CREATING METHOD TO UPDATE DATA
    private void updateRecord() {
        Connection connection = getConnection();
        PreparedStatement stmt;

        try {
            stmt = connection.prepareStatement("UPDATE books SET title=?, author=?, year=?, pages=? WHERE id=?");
            ((PreparedStatement) stmt).setInt(5, Integer.parseInt(tfId.getText()));
            stmt.setString(1, tfTitle.getText());
            stmt.setString(2, tfAuthor.getText());
            stmt.setInt(3, Integer.parseInt(tfYear.getText()));
            stmt.setInt(4, Integer.parseInt(tfPages.getText()));
            int i = stmt.executeUpdate();

            System.out.println(i);
            connection.close();

        }catch (Exception e){
            System.out.println();
        }
        showBooks();
    }


}
