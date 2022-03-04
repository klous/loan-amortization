import java.math.BigDecimal;

public class Payment {
    private int paymentNumber;
    private BigDecimal loanPayment;
    private BigDecimal endingBalance;
    private BigDecimal principalApplied;
    private BigDecimal interestPayment;

    public int getPaymentNumber() {
        return paymentNumber;
    }

    public BigDecimal getLoanPayment() {
        return loanPayment;
    }

    public BigDecimal getEndingBalance() {
        return endingBalance;
    }

    public BigDecimal getInterestPayment() {
        return interestPayment;
    }

    public BigDecimal getPrincipalApplied() {
        return principalApplied;
    }

     public Payment(int paymentNumber, BigDecimal loanPayment, BigDecimal principalApplied, BigDecimal interestPayment, BigDecimal endingBalance ){
        this.paymentNumber = paymentNumber;
        this.loanPayment = loanPayment;
        this.principalApplied = principalApplied;
        this.interestPayment = interestPayment;
        this.endingBalance = endingBalance;
     }



}
