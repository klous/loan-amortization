import java.util.Scanner;

public class MainProgram {
    public static void main(String[] args) {
        Scanner userInput = new Scanner(System.in);

        System.out.println("\nWelcome to the loan amortization calculator!");
        System.out.println("What is the loan amount?");
        String loanAmountInput = userInput.nextLine();
        double loanAmount = Double.parseDouble(loanAmountInput);

        System.out.println("What is the term / length of loan in months?");
        String termInMonthsInput = userInput.nextLine();
        int termInMonths = Integer.parseInt(termInMonthsInput);

        System.out.println("What is the interest rate? ( '5' for 5% or '3.25' for 3.25% )");
        String interestRateInput = userInput.nextLine();
        double interestRate = Double.parseDouble(interestRateInput);


        boolean validInput = false;
        boolean extraPayment = false;

        while(!validInput){
            System.out.println("Would you like to make an extra payment each month and see how it affects your loan amortization? (Y)es or (N)o");
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
            System.out.println("How much extra (on top of your required payment) would you like to pay each month?");
            String extraLoanPaymentInput = userInput.nextLine();
            double extraLoanPayment = Double.parseDouble(extraLoanPaymentInput);
            loan = new Loan(loanAmount, interestRate, termInMonths, extraLoanPayment);
        }else{
            loan = new Loan(loanAmount, interestRate, termInMonths);
        }

        System.out.println(loan.displayLoanAmortizationTable());

//        //For testing
//        Loan loan = new Loan(250000,10,120);



        //Loan l = new Loan( 40000, 5, 36);
        //System.out.println("Loan Payment is: " + l.calculatePayment());
//        System.out.println("Your monthly interest (Interest Only) is: " + l.getInterestOnlyPayment());
//        System.out.println("Your per diem interest is: " + l.getPerDiemInterest());

        // System.out.print(loan.displayLoanAmortizationTable());

    }
}
