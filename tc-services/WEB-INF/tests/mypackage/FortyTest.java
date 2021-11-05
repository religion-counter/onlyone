package mypackage;

import java.math.BigDecimal;
import java.security.SecureRandom;

class FortyTest {

    SecureRandom random = new SecureRandom();

    void testThousandGamesOneToTwenty() {
        BigDecimal casinoBalance = new BigDecimal("1.5"); // BNB
        BigDecimal playerBalance = new BigDecimal("2000"); // BNB

        BigDecimal bet = new BigDecimal("0.01");

        int numberOfGames = 1000000;

        for (int i = 1; i <= numberOfGames; ++i) {
            int chosenNumber = random.nextInt(41);
            if (chosenNumber >= 1 && chosenNumber <= 20) {
                casinoBalance = casinoBalance.subtract(bet);
                playerBalance = playerBalance.add(bet);
            } else { // 0 or > 20
                casinoBalance = casinoBalance.add(bet);
                playerBalance = playerBalance.subtract(bet);
            }

            if (casinoBalance.compareTo(BigDecimal.ZERO) <= 0) {
                System.err.println("Bankrupt at " + i + "th game. Casino Balance: " +
                        casinoBalance + ", Player Balance: " + playerBalance);
                break;
            }
        }

        System.out.println("ONE_TO_TWENTY Casino Balance: " + casinoBalance + ", Player Balance: " + playerBalance);
    }

    void testThousandGamesOdds() {
        BigDecimal casinoBalance = new BigDecimal("1.5"); // BNB
        BigDecimal playerBalance = new BigDecimal("2000"); // BNB

        BigDecimal bet = new BigDecimal("0.01");

        int numberOfGames = 1000000;

        for (int i = 1; i <= numberOfGames; ++i) {
            int chosenNumber = random.nextInt(41);
            if (chosenNumber % 2 == 1) {
                casinoBalance = casinoBalance.subtract(bet);
                playerBalance = playerBalance.add(bet);
            } else { // even
                casinoBalance = casinoBalance.add(bet);
                playerBalance = playerBalance.subtract(bet);
            }

            if (casinoBalance.compareTo(BigDecimal.ZERO) <= 0) {
                System.err.println("Bankrupt at " + i + "th game. Casino Balance: " +
                        casinoBalance + ", Player Balance: " + playerBalance);
                break;
            }
        }

        System.out.println("ODDS: Casino Balance: " + casinoBalance + ", Player Balance: " + playerBalance);
    }

    void testThousandGamesOneNumber(int betNumber) {
        BigDecimal casinoBalance = new BigDecimal("3"); // BNB
        BigDecimal playerBalance = new BigDecimal("2000"); // BNB

        BigDecimal bet = new BigDecimal("0.0005");

        int numberOfGames = 1000000;

        int bankruptGame = -1;

        for (int i = 1; i <= numberOfGames; ++i) {
            int chosenNumber = random.nextInt(41);
            BigDecimal amountWon;
            playerBalance = playerBalance.subtract(bet);
            casinoBalance = casinoBalance.add(bet);
            if (chosenNumber == betNumber) {
                amountWon = bet.multiply(new BigDecimal(40));
                casinoBalance = casinoBalance.subtract(amountWon);
                playerBalance = playerBalance.add(amountWon);
            }

            if (casinoBalance.compareTo(BigDecimal.ZERO) <= 0) {
                if (bankruptGame == -1) {
                    System.err.println("Bankrupt at " + i + "th game. Casino Balance: " +
                            casinoBalance + ", Player Balance: " + playerBalance +
                            " with bet number: " + betNumber);
                    bankruptGame = i;
                }
            }
        }

        System.out.println("ONE_NUMBER Casino Balance: " + casinoBalance + ", Player Balance: " + playerBalance +
                " with bet number: " + betNumber);
    }

    public static void main(String[] args) {
        FortyTest test = new FortyTest();
        test.testThousandGamesOneToTwenty();
        test.testThousandGamesOdds();
        int[] numbers = new int[40];
        for (int i = 0; i < numbers.length; ++i) {
            numbers[i] = i;
        }
        test.shuffle(numbers);
        for (int i : numbers) {
            test.testThousandGamesOneNumber(i);
        }

    }

    void shuffle(int[] ar)
    {
        for (int i = ar.length - 1; i > 0; i--)
        {
            int index = random.nextInt(i + 1);
            // Simple swap
            int a = ar[index];
            ar[index] = ar[i];
            ar[i] = a;
        }
    }
}
