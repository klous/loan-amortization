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
     * Use numbers like 5.75 is equivalent to 5.75%
     */
    private double interestRate;
    public double getInterestRate() {return interestRate;}

    //todo add private method to format the numbers to return them ALWAYS with 2 decimals

    private String formatNumber(double number){
        DecimalFormat df = new DecimalFormat("#.00");

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


    /**
     *
     * @param termInMonths
     * @param loanAmount represent the initial loan amount / principal amount
     * @param interestRate use numbers like 5.75 = 5.75%
     */

    public Loan(double loanAmount, double interestRate, int termInMonths){
        this.termInMonths = termInMonths;
        this.loanAmount = loanAmount;
        this.interestRate = interestRate / 100; // convert into decimal upon constructor taking the input
    }


}

