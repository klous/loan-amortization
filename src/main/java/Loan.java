import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;

public class Loan {
    private int termInMonths;
    public int getTermInMonths() {return termInMonths;}

    private final int NUMBER_OF_MONTHS_IN_YEAR = 12;
    private final int NUMBER_OF_DAYS_IN_A_YEAR = 365;

    private double loanAmount;
    public double getLoanAmount(){
        return loanAmount;
    }

    /**
     * Use numbers like 5.75 would be equivalent to 5.75%
     */
    private double interestRate;
    public double getInterestRate() {return interestRate;}


    private String formatNumber(double number){
        DecimalFormat df = new DecimalFormat("#,###.00");
        return df.format(number);
    }

    private double roundMyNum(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();
        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    private double getInterestPayment(double principalBalance) {
        double monthlyInterest = principalBalance * (interestRate / NUMBER_OF_MONTHS_IN_YEAR);
        return roundMyNum(monthlyInterest, 2);
    }


    public double getPerDiemInterest(){
        double perDiemInterest = loanAmount * (interestRate/NUMBER_OF_DAYS_IN_A_YEAR);
        return roundMyNum(perDiemInterest, 2);
    }

    public double getInterestOnlyPayment(){
        return roundMyNum(getInterestPayment(loanAmount), 2);
    }

    public double calculatePayment(){
        // payment = 100000*(0.05/12)*(1+0.05/12)^360   /    (1+0.075/12)^360 - 1)
        // should be 536.82
        double monthlyInterestRate = interestRate / 12;
        double loanPayment = (monthlyInterestRate * loanAmount) / (1-Math.pow(1+monthlyInterestRate, -termInMonths));
        return roundMyNum(loanPayment, 2);
    }

    //todo fix what happens at the end of the loan amortization table where the payment can change for the last one when the remaining principal is less than the payment

    /**
     *
     * @param termInMonths loan term in months
     * @param loanAmount represent the initial loan amount / principal amount
     * @param interestRate use numbers like 5.75 = 5.75%
     */
    public Loan(double loanAmount, double interestRate, int termInMonths){
        this.termInMonths = termInMonths;
        this.loanAmount = loanAmount;
        this.interestRate = interestRate / 100; // convert into decimal upon constructor taking the input
    }

    /**
     *
     * @param loanAmount loan amount
     * @param interestRate interest rate in number like 5 = 5% or 7.5 = 7.5%
     */
    public Loan(double loanAmount, double interestRate){
        this.loanAmount = loanAmount;
        this.interestRate = interestRate / 100; // convert into decimal upon constructor taking the input
    }

    public String displayLoanAmortizationTable(){
        double principalBalance = loanAmount;
        double loanPayment = calculatePayment();

        String outputString = "\nLoan Amount: $" + formatNumber(loanAmount) + " | Your payment is: $" + formatNumber(loanPayment) + " Interest Rate: "+ formatNumber(interestRate*100) + "%" + "\n\n";

        double totalInterestPaid = 0;

        outputString += "Payment No. \tPrincipal \t\t\tInterest \t Ending Balance";

        for(int i = 1; i <= termInMonths; i++){
            double interestPayment = getInterestPayment(principalBalance);
            totalInterestPaid += interestPayment;
            double principalPayment = roundMyNum(loanPayment - interestPayment, 2);
            principalBalance = roundMyNum(principalBalance-principalPayment, 2);
            outputString += "\n";
//            outputString += "Payment Number: ";
            outputString += "\t" + i;

            //outputString += "Principal: \t$" + formatNumber(principalPayment) + "\t";
            outputString += "\t\t\t$" + formatNumber(principalPayment) + "\t";


            // accounts for the size (in characters) of the principal payment in my spacing
            if(principalPayment<1000){
                outputString +="\t";
            }
            //outputString += "Interest: \t$" + formatNumber(interestPayment) + "\t\t";
            outputString += "\t\t$" + formatNumber(interestPayment) + "\t";
            if(interestPayment<1000){
                outputString +="\t";
            }
            //outputString += "\t$" + formatNumber(principalBalance);
            if(principalBalance<=1){
               outputString += "\t$0.00";
            }else {
                outputString += "\t$" + formatNumber(principalBalance);
            }

        }
        outputString += "\n\n TOTAL INTEREST PAID: $" + formatNumber(totalInterestPaid);

        return outputString;
    }

}

