import java.math.BigDecimal;
import java.math.RoundingMode;

public class Loan {
    private int termInMonths;

    public int getTermInMonths() {return termInMonths;}

    private BigDecimal loanAmount;
    public BigDecimal getLoanAmount() {return loanAmount;}


    private BigDecimal interestRate;
    public BigDecimal getInterestRate() {return interestRate;}



    public BigDecimal getPayment(){
        BigDecimal payment = BigDecimal.ZERO;
        payment.setScale(2, RoundingMode.HALF_UP);
        //if loan amount was 100000, interest rate 7.5%, term in months = 360
        // payment = 100000*(0.05/12)*(1+0.05/12)^360   /    (1+0.075/12)^360 - 1)
        BigDecimal monthlyRate = interestRate.divide(BigDecimal.valueOf(12), 2, RoundingMode.HALF_UP);

        BigDecimal intermediateStep = BigDecimal.ZERO;
        intermediateStep.setScale(2, RoundingMode.HALF_UP);

        intermediateStep = (loanAmount.multiply(monthlyRate.multiply(monthlyRate.add(BigDecimal.ONE)).pow(termInMonths)));
        // 100,000 with 5% interest rate shoudl be 22,338.72 at this point

        payment = (loanAmount.multiply(monthlyRate.multiply(monthlyRate.add(BigDecimal.ONE)).pow(termInMonths))).divide
                ((monthlyRate.add(BigDecimal.ONE)).pow(termInMonths-1));

        return payment;
    }

    public Loan(int termInMonths, BigDecimal loanAmount, BigDecimal interestRate){
        this.termInMonths = termInMonths;
        this.loanAmount = loanAmount;
        this.interestRate = interestRate;
    }


}
