import org.junit.Assert;
import org.junit.Test;

public class LoanTest {

    @Test
    public void test_calculate_loan_payment_100k_loan_at_5_percent_30_years(){

        Loan loan = new Loan(100000, 5, 360);
        double expectedPayment = 536.82;
        double actualPayment = loan.calculatePayment();

        Assert.assertEquals("100,000 loan @ 5% for 30 years / 360 month term should have payment of 536.82", expectedPayment, actualPayment, 0);

    }
}
