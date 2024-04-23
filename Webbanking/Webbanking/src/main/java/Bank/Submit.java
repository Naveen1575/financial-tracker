package Bank;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/Submit")
public class Submit extends HttpServlet {

    private static final String INSERT_QUERY ="INSERT INTO BANK(TRANSCATIONID, CUSTOMERNAME, DATE, DESCRIPTION, AMOUNT, ACCOUNTTYPE) VALUES(?, ?, ?, ?, ?, ?)";

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter pw = response.getWriter();
        response.setContentType("text/html");

        String TranscationID = request.getParameter("TranscationID");
        String CustomerName = request.getParameter("CustomerName");
        String Date = request.getParameter("Date");
        String Description = request.getParameter("Description");
        String AmountString = request.getParameter("Amount");
        String AccountType = request.getParameter("bank");
        
        // Ensure AmountString is not null before parsing
        if (AmountString == null || AmountString.isEmpty()) {
            pw.println("<script>alert('Amount cannot be null or empty');</script>");
            return; // Exit the method if amount is null or empty
        }
        
        // Parse amount string into double
        double Amount = Double.parseDouble(AmountString);

        // Check if AccountType is not null before processing
        if (AccountType == null) {
            pw.println("<script>alert('Account type cannot be null');</script>");
            return; // Exit the method if account type is null
        }

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch(ClassNotFoundException e) {
            e.printStackTrace();
        }

        try(Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/viveek", "root", "viveek");
            PreparedStatement ps = con.prepareStatement(INSERT_QUERY)) {

            // Determine whether to add or subtract based on the selected account type
            if(AccountType.equals("Credit")) {
                Amount += 100; // Example addition for credit
            } else if(AccountType.equals("Debit")) {
                Amount -= 50; // Example subtraction for debit
            }

            // Set the modified amount to the prepared statement
            ps.setString(1, TranscationID);
            ps.setString(2, CustomerName);
            ps.setString(3, Date);
            ps.setString(4, Description);
            ps.setDouble(5, Amount); // Use setDouble for numeric values
            ps.setString(6, AccountType);

            int count = ps.executeUpdate();
            if(count == 0) {
                pw.println("<script>alert('Record Not Stored into Database');</script>");
            } else {
                pw.println("<script>alert('Record Stored into Database');</script>");
            }

        } catch (SQLException se) {
            pw.println(se.getMessage());
            se.printStackTrace();
        } catch (Exception e) {
            pw.println(e.getMessage());
            e.printStackTrace();
        } finally {
            pw.close();
        }
    }
}