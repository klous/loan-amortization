public class MainProgram {
    public static void main(String[] args) {
        System.out.println("Main program output here");

        //Loan l = new Loan(360, new BigDecimal(100000), new BigDecimal(0.05));

        Loan l = new Loan( 40000, 5, 36);

        System.out.println("Loan Payment is: " + l.calculatePayment());
        System.out.println("Your monthly interest (Interest Only) is: " + l.getInterestOnlyPayment());
        System.out.println("Your per diem interest is: " + l.getPerDiemInterest());

        System.out.println(l.displayLoanAmortizationTable());

    }
}
