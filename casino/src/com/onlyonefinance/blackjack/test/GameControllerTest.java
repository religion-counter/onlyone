package com.onlyonefinance.blackjack.test;

import com.onlyonefinance.blackjack.GameController;

import java.util.Arrays;
import java.util.Scanner;

public class GameControllerTest {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        GameController gameController = new GameController();
        String clientId = "1234";

        while (true) {
            System.out.println("New Game:");
            GameController.GameObject game = gameController.startGame(clientId, 1, new double[] { 10 });
            System.out.println(game.userVisibleState);

            while (game.availableActions.size() > 0) {
                System.out.println("Available actions: " + Arrays.toString(game.availableActions.toArray()));
                String action = sc.next();
                for (GameController.AvailableAction availableAction : game.availableActions) {
                    if (action.equals(availableAction.action.toString())) {
                        game = gameController.acceptAction(clientId, game.gameId, action, availableAction.playerId, availableAction.playerIdx);
                        break;
                    }
                }
                System.out.println(game.userVisibleState);
            }

            System.out.println("Game result:\n" + game.userVisibleState);
            if (!gameController.finishGame(clientId, game.gameId)) {
                throw new RuntimeException("Cannot finish game: " + game.gameId);
            }
        }

    }
}
