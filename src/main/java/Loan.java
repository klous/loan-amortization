import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.*;

public class Loan {
    private int termInMonths;
    public int getTermInMonths() {return termInMonths;}

    private List<Payment> paymentList = new ArrayList<>();


    private final int NUMBER_OF_MONTHS_IN_YEAR = 12;
    private final int NUMBER_OF_DAYS_IN_A_YEAR = 365;

    private double loanAmount;
    public double getLoanAmount(){return loanAmount;}

    private double extraPrincipalPayment;

    public double getExtraPrincipalPayment() {return extraPrincipalPayment;}

    public void setExtraPrincipalPayment(double extraPrincipalPayment) {this.extraPrincipalPayment = extraPrincipalPayment;}

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

    public double getLoanPayment(){
        // payment = 100000*(0.05/12)*(1+0.05/12)^360   /    (1+0.075/12)^360 - 1)
        // should be 536.82
        double monthlyInterestRate = interestRate / 12;
        double loanPayment = (monthlyInterestRate * loanAmount) / (1-Math.pow(1+monthlyInterestRate, -termInMonths));
        return roundMyNum(loanPayment, 2);
    }


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
        extraPrincipalPayment = 0;
        paymentList = createActualLoanAmortizationPaymentList();
    }

    public Loan(double loanAmount, double interestRate, int termInMonths, double extraPrincipalPayment){
        this.termInMonths = termInMonths;
        this.loanAmount = loanAmount;
        this.interestRate = interestRate / 100; // convert into decimal upon constructor taking the input
        this.extraPrincipalPayment = extraPrincipalPayment;
        paymentList = createActualLoanAmortizationPaymentList();
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


    private List <Payment> createActualLoanAmortizationPaymentList(){

        List <Payment> paymentList = new ArrayList<>();
        double principalBalance = loanAmount;
        double loanPayment = getLoanPayment();
        int paymentNumber = 1;
        while(principalBalance>0 && paymentNumber <= termInMonths){


            if (principalBalance<loanPayment){ // at the end, the principal balance can drop below the required loan payment, especially when extra payments are made
                loanPayment = principalBalance;
            }
            double interestPayment = getInterestPayment(principalBalance);

            double principalPayment = roundMyNum(loanPayment + extraPrincipalPayment - interestPayment, 2);
            principalBalance = roundMyNum(principalBalance-principalPayment, 2);
            Payment payment = new Payment(paymentNumber, loanPayment, principalPayment, interestPayment, principalBalance);

            paymentList.add(payment);

            paymentNumber ++;
        }

        return paymentList;
    }

    private List <Payment> createBothAmortizationTables(double extraPrincipalPaymentAmount){

        List <Payment> paymentList = new ArrayList<>();
        double principalBalance = loanAmount;
        double loanPayment = getLoanPayment();
        int paymentNumber = 1;
        while(principalBalance>0 && paymentNumber <= termInMonths){


            if (principalBalance<loanPayment){ // at the end, the principal balance can drop below the required loan payment, especially when extra payments are made
                loanPayment = principalBalance;
            }
            double interestPayment = getInterestPayment(principalBalance);

            double principalPayment = roundMyNum(loanPayment - interestPayment, 2);
            principalBalance = roundMyNum(principalBalance-principalPayment-extraPrincipalPayment, 2);
            Payment payment = new Payment(paymentNumber, loanPayment, principalPayment, interestPayment, principalBalance);

            paymentList.add(payment);

            paymentNumber ++;
        }

        return paymentList;
    }


    public String displayLoanAmortizationTableFromPaymentList(){
        double loanPayment = getLoanPayment();

        // header
        String outputString = "\nLoan Amount: $" + formatNumber(loanAmount) + " | Your payment is: $" + formatNumber(loanPayment) + " Interest Rate: "+ formatNumber(interestRate*100) + "%" + "\n\n";

        double totalInterestPaid = 0;

        outputString += "Payment No. \tPrincipal \t\t\tInterest \t\tEnding Balance";


        for(Payment payment : paymentList){
           int paymentNumber = payment.getPaymentNumber();
           double principalPayment = payment.getPrincipalApplied(); //todo shouldn't this add on the extra principal portion?
           double interestPayment = payment.getInterest();
           double principalBalance = payment.getEndingBalance();
           totalInterestPaid += interestPayment;
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
        }

        return outputString;
    }


}

