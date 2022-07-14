package misc.DB;

import misc.DB.Exc.DataStorageException;
import misc.DB.Exc.DuplicateException;
import misc.DB.Exc.NotFoundException;

import java.security.SecureRandom;

import static misc.DB.BankSystemDA.*;

public class BankSystemPD {

    private String firstName, surname;
    private String age, pin;
    private String cardNo;
    private double balance;

    public BankSystemPD() {

        firstName = "name";
        surname = "surname";
        age = "0";
        pin = "0000";
        cardNo = "0000000000000000";
        balance = 0;

    }

    public BankSystemPD(String firstName, String surname, String age, double balance) {
        this.firstName = firstName;
        this.surname = surname;
        this.age = age;
        this.balance = balance;
    }

    public BankSystemPD(String pin, String cardNo) {
        this.pin = pin;
        this.cardNo = cardNo;
    }

    public BankSystemPD(String firstName, String surname, String age, String pin, String cardNo, double balance) {
        this.firstName = firstName;
        this.surname = surname;
        this.age = age;
        this.pin = pin;
        this.cardNo = cardNo;
        this.balance = balance;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public String getCardNo() {
        return cardNo;
    }

    // Methods
    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public boolean checkGenerate(String cardNo) {
        int checksum = Character.digit(cardNo.charAt(15), Character.MAX_RADIX);
        int check_sum_gen;

        check_sum_gen = Integer.parseInt(generateCheckSum(cardNo));

        return check_sum_gen == checksum;

    }

    public String generateCheckSum(String cardNo) {
        int[] card = new int[16];
        int addition = 0;
        int check_sum_gen = 0;

        card[0] = 0;

        for (int i = 1; i <= 15; i++) {
            card[i] = (Character.digit(cardNo.charAt(i - 1), Character.MAX_RADIX));
        }

        for (int i = 1; i <= 15; i++) {
            if (i % 2 != 0) {
                card[i] = card[i] * 2;
            }

            if (card[i] > 9) {
                card[i] = card[i] - 9;
            }

            addition += card[i];

        }

        if (addition % 10 != 0) {
            for (int i = 1; i < 10; i++) {
                if ((addition + i) % 10 == 0) {
                    check_sum_gen = i;
                    break;
                }
            }
        }

        return String.valueOf(check_sum_gen);
    }

    public String generateCardNo() {
        StringBuilder cardNo = new StringBuilder();

        SecureRandom r1 = new SecureRandom();

        for (int i = 0; i < 9; i++) {
            cardNo.append(r1.nextInt(10));
        }

        return "400000" + cardNo;

    }

    public String generatePin() {
        StringBuilder pinNo = new StringBuilder();

        SecureRandom r1 = new SecureRandom();

        for (int i = 0; i < 4; i++) {
            pinNo.append(r1.nextInt(10));
        }

        return String.valueOf(pinNo);
    }

    public void logOut() {
        firstName = "name";
        surname = "surname";
        age = "0";
        pin = "0000";
        cardNo = "0000000000000000";
        balance = 0;
    }

    public static void init_() throws DataStorageException {
        initialise();
    }

    public static void terminate_() throws DataStorageException {
        terminate();
    }

    public void addCustomer_(BankSystemPD bankSystemPD) throws DuplicateException {
        addCustomer(bankSystemPD);
    }

    public static BankSystemPD getCustomer(String cardNo, String pin) throws NotFoundException {
        return getCustomerD(cardNo, pin);
    }

    public static void transDepMoney(String cardNo, double balance) throws NotFoundException {
        transferDepMoney(cardNo, balance);
    }

    public static void updateCustInfo(BankSystemPD bankSystemPD) throws NotFoundException {
        updateAccount(bankSystemPD);
    }

    public void removeAccount(String cardNo) throws NotFoundException {
        deleteAccount(cardNo);
    }
}
