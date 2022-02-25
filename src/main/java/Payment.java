public class Payment {
    private int paymentNumber;
    private double loanPayment;
    private double endingBalance;
    private double principalApplied;
    private double interest;

    public int getPaymentNumber() {
        return paymentNumber;
    }

    public double getLoanPayment() {
        return loanPayment;
    }

    public double getEndingBalance() {
        return endingBalance;
    }

    public double getInterest() {
        return interest;
    }

    public double getPrincipalApplied() {
        return principalApplied;
    }

     public Payment(int paymentNumber, double loanPayment, double principalApplied, double interest, double endingBalance ){
        this.paymentNumber = paymentNumber;
        this.loanPayment = loanPayment;
        this.principalApplied = principalApplied;
        this.interest = interest;
        this.endingBalance = endingBalance;
     }



}
