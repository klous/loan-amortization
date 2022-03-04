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


/*    private String formatNumber(double number){
        DecimalFormat df = new DecimalFormat("#,###.00");
        return df.format(number);
    }

    private double roundMyNum(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();
        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }*/

    private BigDecimal getInterestPayment(BigDecimal principalBalance) {
        BigDecimal monthlyInterest = principalBalance.multiply(interestRate.divide(new BigDecimal(NUMBER_OF_MONTHS_IN_YEAR), 15, RoundingMode.HALF_UP));
        return monthlyInterest.setScale(2, RoundingMode.HALF_UP);
    }

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
        paymentList = createLoanAmortizationPaymentList(extraPrincipalPayment);

    }

    public Loan(String loanAmount, String interestRate, int termInMonths, String extraPrincipalPayment){
        this.termInMonths = termInMonths;
        this.loanAmount = new BigDecimal(loanAmount);
        this.interestRate = getDecimalInterestRate(interestRate); // convert into decimal upon constructor taking the input
        this.extraPrincipalPayment = new BigDecimal(extraPrincipalPayment);


        paymentList = createLoanAmortizationPaymentList(new BigDecimal(extraPrincipalPayment));

        // create this baseline loan amortization to be able to compare them.
        noExtraPrincipalPaymentList = createLoanAmortizationPaymentList(BigDecimal.ZERO);
    }

    private BigDecimal getTotalInterestPaid(List<Payment> paymentList){
        BigDecimal totalInterestPaid = BigDecimal.ZERO;
        for(Payment payment : paymentList){
            totalInterestPaid = totalInterestPaid.add(payment.getInterestPayment());
        }
        return totalInterestPaid;
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

    private BigDecimal getDifferenceInInterestPaid(){
        BigDecimal baseLineInterest = getTotalInterestPaid(noExtraPrincipalPaymentList);

        BigDecimal totalInterestPaid = getTotalInterestPaid(paymentList);

        // return the difference between the baseline loan payment set total interest and actual total interest
        return baseLineInterest.subtract(totalInterestPaid);
    }

    // consider how to have one function that can create both tables

    private List <Payment> createLoanAmortizationPaymentList(BigDecimal extraPrincipalPaymentValue){

        List <Payment> paymentList = new ArrayList<>();
        BigDecimal principalBalance = loanAmount;
        BigDecimal loanPayment = getLoanPayment();
        int paymentNumber = 1;
        while(principalBalance.compareTo(BigDecimal.ZERO) > 0 && paymentNumber <= termInMonths){
            BigDecimal interestPayment = getInterestPayment(principalBalance);

            BigDecimal principalPayment = loanPayment.add(extraPrincipalPaymentValue).subtract(interestPayment);
            principalBalance = principalBalance.subtract(principalPayment);

            // On the last payment, the principal balance should zero out
            if(interestPayment.compareTo(principalBalance)==0 ){
                principalBalance = BigDecimal.ZERO;
            }

            Payment payment = new Payment(paymentNumber, loanPayment, principalPayment, interestPayment, principalBalance);
            paymentList.add(payment);
            paymentNumber ++;
        }

        return paymentList;
    }
    //todo fix for BigDecimal
    public String displayLoanAmortizationTableFromPaymentList(){
        BigDecimal loanPayment = getLoanPayment();

        // header
        String outputString = "\nLoan Amount: $" +loanAmount + " | Your payment is: $" + loanPayment + " Interest Rate: "+ interestRate.multiply(new BigDecimal(100)) + "%" + "\n\n";

        BigDecimal totalInterestPaid = BigDecimal.ZERO;

        outputString += "Payment No. \tPrincipal \t\t\tInterest \t\tEnding Balance";


        for(Payment payment : paymentList){
           int paymentNumber = payment.getPaymentNumber();
           BigDecimal principalPayment = payment.getPrincipalApplied();
           BigDecimal interestPayment = payment.getInterestPayment();
           BigDecimal principalBalance = payment.getEndingBalance();
           totalInterestPaid = getTotalInterestPaid(paymentList);
            outputString += "\n";

            outputString += "\t" + paymentNumber;

            outputString += "\t\t\t$" + principalPayment + "\t";


            // accounts for the size (in characters) of the principal payment in my spacing
            if(principalPayment.compareTo(new BigDecimal(1000))<0){
                outputString +="\t";
            }

            outputString += "\t\t$" + interestPayment + "\t";
            if(interestPayment.compareTo(new BigDecimal(1000))<0){
                outputString +="\t";
            }

            if(principalBalance.compareTo(BigDecimal.ONE)<0){
                outputString += "\t$0.00";
            }else {
                outputString += "\t$" + principalBalance;
            }

        }
        outputString += "\n\n TOTAL INTEREST PAID: $" + totalInterestPaid + "\n";
        if(paymentList.size()<termInMonths){
            outputString+= "Number of Payments You didn't have to make because of your extra payments: "+ (termInMonths - paymentList.size());
            outputString+= "\nTotal Interest saved: $"+getDifferenceInInterestPaid();
        }

        return outputString;
    }

}

