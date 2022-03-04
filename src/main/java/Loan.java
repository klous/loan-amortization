import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.*;

public class Loan {
    private int termInMonths;
    public int getTermInMonths() {return termInMonths;}

    private List<Payment> paymentList = new ArrayList<>();
    private List<Payment> noExtraPrincipalPaymentList = new ArrayList<>();

    private final int NUMBER_OF_MONTHS_IN_YEAR = 12;
    private final int NUMBER_OF_DAYS_IN_A_YEAR = 365;


    private BigDecimal loanAmount;
    public BigDecimal getLoanAmount(){return loanAmount;}

    private BigDecimal extraPrincipalPayment = BigDecimal.ZERO;

    public BigDecimal getExtraPrincipalPayment() {return extraPrincipalPayment;}

    public void setExtraPrincipalPayment(BigDecimal extraPrincipalPayment) {this.extraPrincipalPayment = extraPrincipalPayment;}

    /**
     * Use numbers like 5.75 would be equivalent to 5.75%
     */
    private BigDecimal interestRate;
    public BigDecimal getInterestRate() {return interestRate;}


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

    private BigDecimal getInterestPayment(BigDecimal principalBalance) {
        BigDecimal monthlyInterest = principalBalance.multiply(interestRate.divide(new BigDecimal(NUMBER_OF_MONTHS_IN_YEAR), 15, RoundingMode.HALF_UP));
        return monthlyInterest.setScale(2, RoundingMode.HALF_UP);
    }

    //todo fix this for BD
    public BigDecimal getPerDiemInterest(){
        BigDecimal perDiemInterest = loanAmount.multiply(interestRate.divide(BigDecimal.valueOf(NUMBER_OF_DAYS_IN_A_YEAR), 15, RoundingMode.HALF_UP));
        return perDiemInterest.setScale(2, RoundingMode.HALF_UP);
    }

    public BigDecimal getInterestOnlyPayment(){
        return getInterestPayment(loanAmount);
    }

    public BigDecimal getLoanPayment(){
        // payment = 100000*(0.05/12)*(1+0.05/12)^360   /    (1+0.075/12)^360 - 1)
        // should be 536.82
        BigDecimal monthlyInterestRate = interestRate.divide(new BigDecimal("12"),15, RoundingMode.HALF_UP );

        BigDecimal loanPaymentBD = (monthlyInterestRate.multiply(loanAmount)).divide
                ((BigDecimal.ONE.subtract((BigDecimal.ONE.add(monthlyInterestRate).pow(-termInMonths, MathContext.DECIMAL32)))),2, RoundingMode.HALF_UP);
        //double loanPayment = (monthlyInterestRate * loanAmount) / (1-Math.pow(1+monthlyInterestRate, -termInMonths));
        return loanPaymentBD;
    }


    /**
     *
     * @param termInMonths loan term in months
     * @param loanAmount represent the initial loan amount / principal amount
     * @param interestRate use numbers like 5.75 = 5.75%
     */
    public Loan(String loanAmount, String interestRate, int termInMonths){
        this.termInMonths = termInMonths;
        this.loanAmount = new BigDecimal(loanAmount);
        this.interestRate = getDecimalInterestRate(interestRate); // convert into decimal upon constructor taking the input

        //todo uncomment this once paymentList fixed
        //paymentList = createLoanAmortizationPaymentList(extraPrincipalPayment);

    }

    public Loan(String loanAmount, String interestRate, int termInMonths, String extraPrincipalPayment){
        this.termInMonths = termInMonths;
        this.loanAmount = new BigDecimal(loanAmount);
        this.interestRate = getDecimalInterestRate(interestRate); // convert into decimal upon constructor taking the input
        this.extraPrincipalPayment = new BigDecimal(extraPrincipalPayment);

        //todo uncomment this once paymentList fixed
        //paymentList = createLoanAmortizationPaymentList(extraPrincipalPayment);

        // create this baseline loan amortization to be able to compare them.
        //todo uncomment this once paymentList fixed
        //noExtraPrincipalPaymentList = createLoanAmortizationPaymentList(0);
    }

    private double getTotalInterestPaid(List<Payment> paymentList){
        double totalInterestPaid = 0;
        for(Payment payment : paymentList){
            totalInterestPaid += payment.getInterest();
        }
        return roundMyNum(totalInterestPaid, 2);
    }

    /**
     *
  /*   * @param loanAmount loan amount
     * @param interestRate interest rate in number like 5 = 5% or 7.5 = 7.5%
     *//*
    public Loan(String loanAmount, String interestRate, int termInMonths){
        this.loanAmount = new BigDecimal(loanAmount);
        this.interestRate = getDecimalInterestRate(interestRate);
        this.termInMonths = termInMonths;
    }*/

    private BigDecimal getDecimalInterestRate(String interestRate) {
        BigDecimal interestRateBD = new BigDecimal(interestRate);
        interestRateBD = interestRateBD.divide(BigDecimal.valueOf(100));
        // set the scale to cover 5 decimal places - e.g. 3.875 --> 0.03875
        return  interestRateBD.setScale(5, RoundingMode.HALF_UP);
    }

    private double getDifferenceInInterestPaid(){
        double baseLineInterest = getTotalInterestPaid(noExtraPrincipalPaymentList);

        double totalInterestPaid = getTotalInterestPaid(paymentList);

        // return the difference between the baseline loan payment set total interest and actual total interest
        return roundMyNum(baseLineInterest - totalInterestPaid, 2);
    }

    // consider how to have one function that can create both tables
    //todo fix this for BigDecimal
   /* private List <Payment> createLoanAmortizationPaymentList(double extraPrincipalPaymentValue){

        List <Payment> paymentList = new ArrayList<>();
        double principalBalance = loanAmount;
        double loanPayment = getLoanPayment();
        int paymentNumber = 1;
        while(principalBalance>0 && paymentNumber <= termInMonths){
            double interestPayment = getInterestPayment(principalBalance);

            double principalPayment = roundMyNum(loanPayment + extraPrincipalPaymentValue - interestPayment, 2);
            principalBalance = roundMyNum(principalBalance-principalPayment, 2);

            // On the last payment, the principal balance should zero out
            if(interestPayment == principalBalance){
                principalBalance = 0;
            }

            Payment payment = new Payment(paymentNumber, loanPayment, principalPayment, interestPayment, principalBalance);
            paymentList.add(payment);
            paymentNumber ++;
        }

        return paymentList;
    }*/
    //todo fix for BigDecimal
    /*public String displayLoanAmortizationTableFromPaymentList(){
        BigDecimal loanPayment = getLoanPayment();

        // header
        String outputString = "\nLoan Amount: $" + formatNumber(loanAmount) + " | Your payment is: $" + formatNumber(loanPayment) + " Interest Rate: "+ formatNumber(interestRate*100) + "%" + "\n\n";

        double totalInterestPaid = 0;

        outputString += "Payment No. \tPrincipal \t\t\tInterest \t\tEnding Balance";


        for(Payment payment : paymentList){
           int paymentNumber = payment.getPaymentNumber();
           double principalPayment = payment.getPrincipalApplied(); //todo shouldn't this add on the extra principal portion?
           double interestPayment = payment.getInterest();
           double principalBalance = payment.getEndingBalance();
           totalInterestPaid = getTotalInterestPaid(paymentList);
            outputString += "\n";

            outputString += "\t" + paymentNumber;

            outputString += "\t\t\t$" + formatNumber(principalPayment) + "\t";


            // accounts for the size (in characters) of the principal payment in my spacing
            if(principalPayment<1000){
                outputString +="\t";
            }

            outputString += "\t\t$" + formatNumber(interestPayment) + "\t";
            if(interestPayment<1000){
                outputString +="\t";
            }

            if(principalBalance<=1){
                outputString += "\t$0.00";
            }else {
                outputString += "\t$" + formatNumber(principalBalance);
            }

        }
        outputString += "\n\n TOTAL INTEREST PAID: $" + formatNumber(totalInterestPaid) + "\n";
        if(paymentList.size()<termInMonths){
            outputString+= "Number of Payments You didn't have to make because of your extra payments: "+ (termInMonths - paymentList.size());
            outputString+= "\nTotal Interest saved: $"+formatNumber(getDifferenceInInterestPaid());
        }

        return outputString;
    }*/


}

