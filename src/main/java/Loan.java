import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.*;

public class Loan {
    private int termInMonths;
    public int getTermInMonths() {return termInMonths;}

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

    public double calculatePayment(){
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
    }

    public Loan(double loanAmount, double interestRate, int termInMonths, double extraPrincipalPayment){
        this.termInMonths = termInMonths;
        this.loanAmount = loanAmount;
        this.interestRate = interestRate / 100; // convert into decimal upon constructor taking the input
        this.extraPrincipalPayment = extraPrincipalPayment;
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

    //todo put the values into an list of arrays? each array contains data per loan period -- LinkedHashMap ? or SortedMap
    // Map<String, Object> <-- Object can be whatever... 

    //todo make this private

    //todo another class for that data model - to have that object

    public List<Map<String, Object>> createLoanAmortization(){

        List<Map<String, Object>> paymentList = new ArrayList<>();
        double principalBalance = loanAmount;
        double loanPayment = calculatePayment();
        int paymentNumber = 1;
        while(principalBalance>0){

            Map<String, Object> payPeriod = new LinkedHashMap<>();
            if (principalBalance<loanPayment){ // at the end, the principal balance can drop below the required loan payment, especially when extra payments are made
                loanPayment = principalBalance;
            }
            double interestPayment = getInterestPayment(principalBalance);

            double principalPayment = roundMyNum(loanPayment - interestPayment, 2);
            principalBalance = roundMyNum(principalBalance-principalPayment-extraPrincipalPayment, 2);

            payPeriod.put("Payment Number", paymentNumber);
            payPeriod.put("Principal Payment", principalPayment);
            payPeriod.put("Interest Payment", interestPayment);
            payPeriod.put("Ending Balance", principalBalance);

            paymentList.add(payPeriod);

            paymentNumber ++;
        }

        return paymentList;
    }






    public String displayLoanAmortizationTable(){
        double principalBalance = loanAmount;
        double loanPayment = calculatePayment();

        String outputString = "\nLoan Amount: $" + formatNumber(loanAmount) + " | Your payment is: $" + formatNumber(loanPayment) + " Interest Rate: "+ formatNumber(interestRate*100) + "%" + "\n\n";

        double totalInterestPaid = 0;

        outputString += "Payment No. \tPrincipal \t\t\tInterest \t\tEnding Balance";



        //todo update this method so that it creates an array of payments, each item in the array is a key-value pair containing the payment, interst payment, principal payment, and new balance

        //todo then this array of payments can be compared with a loan without separate payments or with a different interest rate and be able to compare them

        int paymentNumber = 1;
        while(principalBalance>0 && paymentNumber<= termInMonths){
            if (principalBalance<loanPayment){ // at the end, the principal balance can drop below the required loan payment, especially when extra payments are made
                loanPayment = principalBalance;
            }
            double interestPayment = getInterestPayment(principalBalance);
            totalInterestPaid += interestPayment;
            double principalPayment = roundMyNum(loanPayment - interestPayment, 2);
            principalBalance = roundMyNum(principalBalance-principalPayment-extraPrincipalPayment, 2);
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
            //outputString += "\t$" + formatNumber(principalBalance);

            if(principalBalance<=1){
               outputString += "\t$0.00";
            }else {
                outputString += "\t$" + formatNumber(principalBalance);
            }
        paymentNumber ++;
        }
        outputString += "\n\n TOTAL INTEREST PAID: $" + formatNumber(totalInterestPaid) + "\n";

        return outputString;
    }

}

