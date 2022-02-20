import java.math.BigDecimal;

public class MainProgram {
    public static void main(String[] args) {
        System.out.println("Main program output here");

        //Loan l = new Loan(360, new BigDecimal(100000), new BigDecimal(0.05));

        Loan l = new Loan(360, 100000, 0.05);

        System.out.println("Loan Payment is: " + l.getPayment());

    }
}
