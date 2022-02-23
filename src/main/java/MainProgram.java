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

        System.out.println("What is the interest rate? e.g. enter '5' for 5% or '3.25' for 3.25%");
        String interestRateInput = userInput.nextLine();
        double interestRate = Double.parseDouble(interestRateInput);

        Loan loan = new Loan(loanAmount, interestRate, termInMonths);



        //Loan l = new Loan( 40000, 5, 36);
        //System.out.println("Loan Payment is: " + l.calculatePayment());
//        System.out.println("Your monthly interest (Interest Only) is: " + l.getInterestOnlyPayment());
//        System.out.println("Your per diem interest is: " + l.getPerDiemInterest());

        System.out.print(loan.displayLoanAmortizationTable());

    }
}
