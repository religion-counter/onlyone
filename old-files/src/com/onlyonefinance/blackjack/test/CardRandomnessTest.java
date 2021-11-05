package com.onlyonefinance.blackjack.test;

import com.onlyonefinance.blackjack.Card;
import com.onlyonefinance.blackjack.GameService;
import com.onlyonefinance.blackjack.GameState;
import com.onlyonefinance.blackjack.RandomGenerator;

import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;

public class CardRandomnessTest {

    public static void main(String[] args) {

        int numberOfTests = 100_000;
        int numberOfCardsToDrawEachTest = 200;

        Map<Card.Type, Integer> statistics = new HashMap<>();
        for (Card.Type type : Card.Type.values()) {
            statistics.put(type, 0);
        }

        for (int test = 0; test < numberOfTests; ++test) {
            int numberOfPlayers = RandomGenerator.secureRandom.nextInt(GameService.MAX_NUMBER_OF_PLAYERS) + 1;
            double[] bets = new double[numberOfPlayers];
            for (int i = 0; i < numberOfPlayers; ++i) {
                bets[i] = RandomGenerator.secureRandom.nextDouble() * 100;
            }
            GameState gameState = new GameState(numberOfPlayers, bets, RandomGenerator.secureRandom);
            for (int c = 0; c < numberOfCardsToDrawEachTest; ++c) {
                Card card = gameState.getRandomCard();
                statistics.put(card.type, statistics.get(card.type) + 1);
            }
        }

        int totalNumberOfCardsDrawn = numberOfTests * numberOfCardsToDrawEachTest;
        for (Card.Type type : Card.Type.values()) {
            int numberOfDrawsForCard = statistics.get(type);
            System.out.println("Card " + type + " was drawn " + numberOfDrawsForCard + " times which is " +
                    ((numberOfDrawsForCard / (double) totalNumberOfCardsDrawn)*100.0) + "%" );
        }
    }
}
