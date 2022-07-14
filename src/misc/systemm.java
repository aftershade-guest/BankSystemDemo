package misc;

import misc.DB.BankSystemPD;
import misc.DB.Exc.DataStorageException;
import misc.DB.Exc.DuplicateException;
import misc.DB.Exc.NotFoundException;

import java.util.Scanner;

import static misc.DB.BankSystemPD.*;

public class systemm {

    private static BankSystemPD systemPD;
    private static String cardNo, pin;

    public static void main(String[] args) {

        try {
            init_();

            Scanner scanner = new Scanner(System.in);
            int input;

            while (true){

                System.out.println("Please choose from the menu below:");
                System.out.println("1. Create Account \n2.Log in \n3.Exit");

                input = scanner.nextInt();

                if (input == 1) {
                    createAccount();
                } else if (input == 2) {
                    logIn();
                } else if (input == 3) {
                    System.out.println("Good bye!");
                    try {
                        scanner.close();
                        terminate_();
                    } catch (DataStorageException e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                } else {
                    System.out.println("Please enter a valid menu number.");
                }
            }

        } catch (DataStorageException e) {
            System.out.println(e.getMessage());
        }


    }

    public static void createAccount() {
        try {
            Scanner scanner = new Scanner(System.in);

            System.out.println("Please enter your firs name:");
            String firstName = scanner.nextLine();
            System.out.println("Please enter your surname:");
            String surname = scanner.nextLine();
            System.out.println("Please enter your age:");
            String age = scanner.nextLine();
            String card;

            double balance = 0;

            systemPD = new BankSystemPD(firstName, surname, age, balance);

            //Initialises connection to database

            String tempCard = systemPD.generateCardNo();
            String generateCheck = systemPD.generateCheckSum(tempCard);
            String pin = systemPD.generatePin();

            card = tempCard + generateCheck;

            //Inserts a new record to DB
            systemPD.addCustomer_(new BankSystemPD(firstName, surname, age, pin, card, balance));

            System.out.println("Card number: " + card);
            System.out.println("Pin number: " + pin);
            System.out.println("Name: " + firstName + " " + surname);
            System.out.println("Age: " + age);

        } catch (DuplicateException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public static void logIn() {

        Scanner scanner = new Scanner(System.in);

        BankSystemPD systemPD = new BankSystemPD();

        System.out.println("Please enter your card number:");
        cardNo = scanner.nextLine();
        System.out.println("Please enter your pin number:");
        pin = scanner.nextLine();

        try {

            if (systemPD.checkGenerate(cardNo)) {
                systemPD = getCustomer(cardNo, pin);

                System.out.println("Welcome " + systemPD.getFirstName());

                logInInput(systemPD);

            } else {
                System.out.println("It seems that the card no entered is incorrect");
            }


        } catch (NotFoundException ex) {
            System.out.println(ex.getMessage());
        }


    }

    private static void logInInput(BankSystemPD systemPD) {

        Scanner scanner = new Scanner(System.in);
        int inp;

        while (true) {
            System.out.println("1. Balance \n2. Transfer \n3. Deposit \n4. Log out \n5. Exit");
            inp = scanner.nextInt();

            if (inp == 1) {
                System.out.println(systemPD.getBalance());
            } else if(inp == 2) {

                System.out.println("Please enter a card number:");
                String cardNo = scanner.next();

                if(systemPD.checkGenerate(cardNo)) {

                    System.out.println("Please enter the transfer amount");
                    double bal = scanner.nextDouble();

                    if (systemPD.getBalance() < bal) {
                        System.out.println("Insufficient funds");
                    } else {
                        try {
                            transDepMoney(cardNo, bal);
                            systemPD.setBalance(systemPD.getBalance() - bal);
                            updateCustInfo(systemPD);
                        } catch (NotFoundException e) {
                            System.out.println(e.getMessage());
                        }
                    }



                } else {
                    System.out.println("Card number entered is incorrect.");
                }

            } else if (inp == 3) {

                System.out.println("Please enter the deposit amount amount");
                double bal = scanner.nextDouble();

                systemPD.setBalance(systemPD.getBalance() + bal);

                try {
                    updateCustInfo(systemPD);
                } catch (NotFoundException e) {
                    System.out.println(e.getMessage());
                }

            } else if(inp == 4) {
                systemPD.logOut();
                System.out.println("Good bye");
                break;
            } else if (inp == 5) {
                try {
                    systemPD.logOut();
                    terminate_();
                    System.exit(0);
                } catch (DataStorageException e) {
                    System.out.println(e.getMessage());
                }
            } else {
                System.out.println("Please choose from option below: \n");
            }

        }



    }

    public static void updateAccount_() {
        Scanner scanner = new Scanner(System.in);

        String cardNo = scanner.nextLine();

    }
}

