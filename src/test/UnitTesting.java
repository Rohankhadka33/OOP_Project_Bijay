package test;

import controller.MainController;
import javafx.collections.ObservableList;
import library.Books;
import org.junit.Before;
import org.junit.Test;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ResourceBundle;

import static org.junit.Assert.assertTrue;

public class UnitTesting {

    public MainController mainControllerUnderTest;

    @Before
    public void setUp() {
        mainControllerUnderTest = new MainController();
    }
    // Connection Conn = mainControllerUnderTest.getConnection();

    @Test
    public void testGetConnection() {
        // Setup

        // Run the test
        final Connection result = mainControllerUnderTest.getConnection();

        // Verify the results
    }

    // Insert Test
    @Test
    public void insert() throws SQLException {
        Connection Conn = mainControllerUnderTest.getConnection();
        boolean initial = false;
        PreparedStatement insert = Conn.prepareStatement("Insert into books(id,title,author,year,pages) values (?,?,?,?,?)");
        insert.setString(1,"32");
        insert.setString(2,"Notebook");
        insert.setString(3,"Ramesh");
        insert.setString(4,"2020");
        insert.setString(5,"256");
        int last = insert.executeUpdate();
        if(last!= 0){
            initial= true;
        }
        assertTrue(initial);

    }

    //Update Test
    @Test
    public void update() throws SQLException {
        Connection Conn = mainControllerUnderTest.getConnection();
        boolean initial = false;
        PreparedStatement insert = Conn.prepareStatement("Update books set id =?,title=?,author=?,year=?,pages=? WHERE id =32");
        insert.setString(1,"36");
        insert.setString(2,"Rich Dad");
        insert.setString(3,"Gokul");
        insert.setString(4,"2019");
        insert.setString(5,"306");
        int last = insert.executeUpdate();
        if(last!= 0){
            initial= true;
        }
        assertTrue(initial);

    }
    // Delete
    @Test
    public void delete() throws SQLException {
        Connection Conn = mainControllerUnderTest.getConnection();
        boolean initial = false;
        PreparedStatement insert = Conn.prepareStatement("Delete from  books WHERE id =?");
        insert.setString(1,"1");
        int last = insert.executeUpdate();
        if(last!= 0){
            initial= true;
        }
        assertTrue(initial);

    }

    @Test
    public void testGetBooksList() {
        // Setup

        // Run the test
        final ObservableList<Books> result = mainControllerUnderTest.getBooksList();

        // Verify the results
    }

}
