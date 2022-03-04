
import java.math.BigDecimal;
import java.util.Scanner;

public class MainProgram {
    public static void main(String[] args) {
        Scanner userInput = new Scanner(System.in);

        userPrompting(userInput);


//        Loan loan = new Loan("100000", "5", 360);
//        BigDecimal loanPayment = loan.getLoanPayment();
    }

    private static void userPrompting(Scanner userInput) {
        System.out.println("\nWelcome to the loan amortization calculator!");
        System.out.print("\nWhat is the loan amount? ");
        String loanAmountInput = userInput.nextLine();

        System.out.print("What is the term / length of loan in months? ");
        String termInMonthsInput = userInput.nextLine();
        int termInMonths = Integer.parseInt(termInMonthsInput);

        System.out.print("What is the interest rate? ( '5' for 5% or '3.25' for 3.25% ) >> ");
        String interestRateInput = userInput.nextLine();

        boolean validInput = false;
        boolean extraPayment = false;

        while(!validInput){
            System.out.print("Would you like to make an extra payment each month and see how it affects your loan amortization? (Y)es or (N)o >> ");
            String extraPaymentYN = userInput.nextLine();
            if(extraPaymentYN.equalsIgnoreCase("Y")){
                extraPayment = true;
                validInput = true;
            }else if(extraPaymentYN.equalsIgnoreCase("N")){
                validInput = true;
            }else{
                System.out.println("Please enter valid input ('y' or 'n')");
            }
        }
        Loan loan = null;
        if(extraPayment){
            System.out.print("How much extra (on top of your required payment) would you like to pay each month? ");
            String extraLoanPaymentInput = userInput.nextLine();
            loan = new Loan(loanAmountInput, interestRateInput, termInMonths, extraLoanPaymentInput);
        }else{
            loan = new Loan(loanAmountInput, interestRateInput, termInMonths);
        }

        System.out.print(loan.displayLoanAmortizationTableFromPaymentList());
    }

}
