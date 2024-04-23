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

@WebServlet("/Servlet")
public class Servlet extends HttpServlet {

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
        double Amount = Double.parseDouble(AmountString); // Convert Amount to double for calculations

        // Add your addition and subtraction logic here
        double additionResult = Amount + 100; // Example addition
        double subtractionResult = Amount - 50; // Example subtraction

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch(ClassNotFoundException e) {
            e.printStackTrace();
        }

        try(Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/viveek", "root", "viveek");
            PreparedStatement ps = con.prepareStatement(INSERT_QUERY)) {

            ps.setString(1, TranscationID);
            ps.setString(2, CustomerName);
            ps.setString(3, Date);
            ps.setString(4, Description);
            ps.setString(5, AmountString);
            ps.setString(6, AccountType);

            int count = ps.executeUpdate();
            if(count == 0) {
                pw.println("<script>alert('Record Not Stored into Database');</script>");
            } else {
                pw.println("<script>alert('Record Stored into Database');</script>");
            }

            // Print addition and subtraction results
            //pw.println("<p>Addition result: " + additionResult + "</p>");
            //pw.println("<p>Subtraction result: " + subtractionResult + "</p>");

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