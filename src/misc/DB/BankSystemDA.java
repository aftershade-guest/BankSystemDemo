package misc.DB;

import jdk.jfr.Experimental;
import misc.DB.Exc.DataStorageException;
import misc.DB.Exc.DuplicateException;
import misc.DB.Exc.NotFoundException;

import java.sql.*;
import java.util.ArrayList;

public class BankSystemDA {

    private static ResultSet rs;
    private static PreparedStatement ps;
    private static Connection con;
    public static ArrayList<BankSystemPD> arBankSystemPDS = new ArrayList<>();

    public static void initialise() throws DataStorageException {

        final String USERNAME = "root";
        final String PASSWORD = "";
        final String URL = "jdbc:mysql://localhost:3306/banksystem";
        final String DRIVER = "com.mysql.jdbc.Driver";

        try {
            Class.forName(DRIVER);

            con = DriverManager.getConnection(URL, USERNAME, PASSWORD);

        } catch (ClassNotFoundException ex) {
            throw new DataStorageException("Database driver is missing\n" + ex.getMessage());
        } catch (SQLException ex) {
            throw new DataStorageException("Connection failed.\n" + ex.getMessage());
        }

    }

    public static void terminate() throws DataStorageException {

        try {
            if (con != null) {
                con.close();
            }
        } catch (SQLException ex) {
            throw new DataStorageException(ex.getMessage());
        }

    }

    public static void addCustomer(BankSystemPD bankSystemPD) throws DuplicateException {
        String qry = "INSERT INTO tblbanksytem values(?, ?, ?, ?, ?, ?)";

        try {
            ps = con.prepareStatement(qry);
            ps.setString(1, bankSystemPD.getCardNo());
            ps.setString(2, bankSystemPD.getPin());
            ps.setString(3, bankSystemPD.getFirstName());
            ps.setString(4, bankSystemPD.getSurname());
            ps.setString(5, bankSystemPD.getAge());
            ps.setDouble(6, 0);

            int i = ps.executeUpdate();

            if (i > 0) {
                System.out.println("Saved successfully");
            }

        } catch (SQLException ex) {
            throw new DuplicateException("Customer already exists." + ex.getMessage());
        }
    }

    public static ArrayList<BankSystemPD> getAllCustomer() throws NotFoundException {
        String qry = "SELECT * FROM tblbanksytem";


        try {
            Statement st = con.createStatement();
            rs = st.executeQuery(qry);

            while (rs.next()) {
                arBankSystemPDS.add(new BankSystemPD(rs.getString(3),
                        rs.getString(4), rs.getString(5),
                        rs.getString(2), rs.getString(1), rs.getDouble(6)));
            }

            return arBankSystemPDS;

        } catch (SQLException e) {
            throw new NotFoundException("Table empty or error occured." + e.getMessage());
        }

    }

    public static BankSystemPD getCustomerD(String cardNo, String pin) throws NotFoundException {
        String qry = "SELECT firstname, surname, age, balance FROM tblbanksytem WHERE cardno = ? AND pin = ?";

        BankSystemPD systemPD = null;

        try {

            ps = con.prepareStatement(qry);
            ps.setString(1, cardNo);
            ps.setString(2, pin);
            rs = ps.executeQuery();

            while (rs.next()) {
                systemPD = new BankSystemPD(rs.getString(1),
                        rs.getString(2), rs.getString(3),
                        pin, cardNo, rs.getDouble(4));
            }

            return systemPD;

        } catch (SQLException ex) {
            throw new NotFoundException("Not found." + ex.getMessage());
        }
    }

    public static void updateAccount(BankSystemPD bankSystemPD) throws NotFoundException {

        String qry = "UPDATE tblbanksytem SET firstname = ?, surname = ?, age = ?, balance = ? " +
                "WHERE cardno = ?";

        try {

            ps = con.prepareStatement(qry);
            ps.setString(1, bankSystemPD.getFirstName());
            ps.setString(2, bankSystemPD.getSurname());
            ps.setString(3, bankSystemPD.getAge());
            ps.setDouble(4, bankSystemPD.getBalance());
            ps.setString(5, bankSystemPD.getCardNo());


            int i = ps.executeUpdate();

            if (i > 0) {
                System.out.println("Updated Successfully");
            }

        } catch (SQLException ex) {
            throw new NotFoundException("Not found.\n" + ex.getMessage());
        }

    }

    public static void transferDepMoney(String cardno, double balance) throws NotFoundException {
        String qry = "UPDATE tblbanksytem SET balance =+ ? WHERE cardno = ?";

        try {
            ps = con.prepareStatement(qry);
            ps.setDouble(1, balance);
            ps.setString(2, cardno);

            int i = ps.executeUpdate();

            if (i > 0) {
                System.out.println("Transaction successful.");
            }
        } catch (SQLException ex) {
            throw new NotFoundException(ex.getMessage());
        }
    }

    public static void deleteAccount(String cardNo) throws NotFoundException {

        String qry = "DELETE FROM tblbanksytem WHERE cardno = ?";

        try {

            ps = con.prepareStatement(qry);
            ps.setString(1, cardNo);

            int i = ps.executeUpdate();

            if (i > 0) {
                System.out.println("Account deleted successfully.");
            }

        } catch (SQLException ex) {
            throw new NotFoundException("Not found.\n" + ex.getMessage());
        }

    }

}
