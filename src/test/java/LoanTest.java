import org.junit.Assert;
import org.junit.Test;

public class LoanTest {

    @Test
    public void test_calculate_loan_payment_100k_loan_at_5_percent_30_years(){
        Loan loan = new Loan(100000, 5, 360);
        double expectedPayment = 536.82;
        double actualPayment = loan.getLoanPayment();

        Assert.assertEquals("100,000 loan @ 5% for 30 years / 360 month term should have payment of 536.82", expectedPayment, actualPayment, 0);
    }

    @Test
    public void test_calculate_interest_only_payment_160k_loan_at_7_percent(){
        Loan loan = new Loan(160000, 7, 360);
        double expectedPayment = 933.33;
        double actualPayment = loan.getInterestOnlyPayment();

        Assert.assertEquals("160,000 loan @ 7% should have interest only payment of 933.33", expectedPayment, actualPayment, 0);
    }

    @Test
    public void test_calculate_per_diem_interest_135k_at_7_point_5_percent(){
        Loan loan = new Loan(135000, 7.5, 360);
        double expectedPerDiem = 27.74;
        double actualPerDiem = loan.getPerDiemInterest();

        Assert.assertEquals("135,000 loan @ 7.5% should have per diem of 27.74", expectedPerDiem, actualPerDiem, 0);
    }


}
