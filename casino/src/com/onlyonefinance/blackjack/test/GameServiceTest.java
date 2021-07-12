package com.onlyonefinance.blackjack.test;

import com.onlyonefinance.blackjack.GameService;
import com.onlyonefinance.blackjack.RandomGenerator;

import java.util.Scanner;

public class GameServiceTest {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        final int numberOfPlayers = 3;

        while (true) {
            System.out.println("New Game");
            GameService gameService = new GameService();

            GameService.UserVisibleState state = gameService.startGame(numberOfPlayers, new double[] {1,2,3}, RandomGenerator.secureRandom);

            while (state.playerIndex < state.playersCards.length) {
                System.out.print(state + "(h) - Hit, (s) - Stay, (d) - Double, (sp) - Split: ");

                String choose = sc.next();
                switch (choose) {
                    case "h":
                        state = gameService.playerHit(state, state.playerIndex);
                        break;
                    case "s":
                        state = gameService.playerStay(state, state.playerIndex);
                        break;
                    case "d":
                        state = gameService.playerDouble(state, state.playerIndex);
                        break;
                    case "sp":
                        state = gameService.playerSplit(state, state.playerIndex);
                    default:
                        System.out.println("Invalid turn. Try again.");
                }
            }
            System.out.println("\nFinal state: \n" + state);
            System.out.println("\nDo you want another game? (y)");
            String newGame = sc.next();
            if (!newGame.equals("y")) {
                break;
            }
        }
    }


    // TODO Add unit tests for GameService and verify all corner cases - 2 splits, doubles, hits, stays, dealer, player BJs, and others

    // TEST SPLIT On 2 ACES
    // Test BlackJack Dealer and Player
}
