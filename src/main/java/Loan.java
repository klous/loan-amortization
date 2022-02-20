import java.math.BigDecimal;
import java.math.RoundingMode;

public class Loan {
    private int termInMonths;
    public int getTermInMonths() {return termInMonths;}


    private double loanAmount;
    public double getLoanAmount(){
        return loanAmount;
    }

    /**
     * Use numbers like 5.75 is equivalent to 5.75%
     */
    private double interestRate;
    public double getInterestRate() {return interestRate;}

    private double getInterestPayment(double principalBalance){
        double monthlyInterest = principalBalance * ((interestRate / 100) / 12);
        return round(monthlyInterest, 2);
    }

    public double getPerDiemInterest(){
        double perDiemInterest = loanAmount * (interestRate/100/365);
        return round(perDiemInterest, 2);
    }

    public double getInterestOnlyPayment(){
        return round(getInterestPayment(loanAmount), 2);
    }


    // method that rounds using BigDecimal
    private double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }


    public double calculatePayment(){
        double interestRateDecimal = interestRate / 100;
        // payment = 100000*(0.05/12)*(1+0.05/12)^360   /    (1+0.075/12)^360 - 1)
        // should be 536.82
        double monthlyInterestRate = interestRateDecimal / 12;
        double loanPayment = (monthlyInterestRate * loanAmount) / (1-Math.pow(1+monthlyInterestRate, -termInMonths));
        return round(loanPayment, 2);
    }



//    public BigDecimal getPayment(){
//        BigDecimal payment = BigDecimal.ZERO;
//        payment.setScale(2, RoundingMode.HALF_UP);
//        //if loan amount was 100000, interest rate 7.5%, term in months = 360
//        // payment = 100000*(0.05/12)*(1+0.05/12)^360   /    (1+0.075/12)^360 - 1)
//        BigDecimal monthlyRate = interestRate.divide(BigDecimal.valueOf(12), 2, RoundingMode.HALF_UP);
//
//        BigDecimal intermediateStep = BigDecimal.ZERO;
//        intermediateStep.setScale(2, RoundingMode.HALF_UP);
//
//        intermediateStep = (loanAmount.multiply(monthlyRate.multiply(monthlyRate.add(BigDecimal.ONE)).pow(termInMonths)));
//        // 100,000 with 5% interest rate shoudl be 22,338.72 at this point
//
//        payment = (loanAmount.multiply(monthlyRate.multiply(monthlyRate.add(BigDecimal.ONE)).pow(termInMonths))).divide
//                ((monthlyRate.add(BigDecimal.ONE)).pow(termInMonths-1));
//
//        return payment;
//    }

    /**
     *
     * @param termInMonths
     * @param loanAmount represent the initial loan amount / principal amount
     * @param interestRate use numbers like 5.75 = 5.75%
     */

    public Loan(double loanAmount, double interestRate, int termInMonths){
        this.termInMonths = termInMonths;
        this.loanAmount = loanAmount;
        this.interestRate = interestRate;
    }


}
