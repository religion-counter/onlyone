package com.onlyonefinance.blackjack.test;

import com.onlyonefinance.blackjack.Card;
import com.onlyonefinance.blackjack.GameController;
import com.onlyonefinance.blackjack.GameState;
import com.onlyonefinance.blackjack.RandomGenerator;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class GameControllerTestSimulator {

    static String clientId = "1234";
    static GameController gameController = new GameController();


    public static void main(String[] args) {


        double money = 10000;
        double bet = 10;

        int iterations = 100000;

        while (iterations-- > 0) {
            money -= bet;
            GameController.GameObject game = gameController.startGame(clientId, 1, new double[] { bet });

            while (game.userVisibleState.playerIndex != game.userVisibleState.playersSoftScore.length) {
                GameController.AvailableAction action = chooseActionBestStrategy(game);
                if (action.action.toString().equals("SPLIT")) {
                    money -= bet;
                }
                game = gameController.acceptAction(clientId, game.gameId, action.action.toString(), action.playerId, action.playerIdx);
            }
            if (!gameController.finishGame(clientId, game.gameId)) {
                throw new RuntimeException("Cannot finish game: " + game.gameId);
            }
            for (int i = 0; i < game.userVisibleState.playerWinMultiplier.length; ++i) {
                money += bet * game.userVisibleState.playerWinMultiplier[i];
            }
        }
        System.out.println("End money: " + money);
    }

    static GameController.AvailableAction chooseActionBestStrategy(GameController.GameObject game) {
        int playerIdx = game.userVisibleState.playerIndex;

        // SPLITS
        if (game.playersCards[playerIdx].get(0).type == game.playersCards[playerIdx].get(1).type) {
            GameController.AvailableAction splitAction = getAction("SPLIT", game.availableActions);
            if (splitAction != null) {
                Card.Type cardType = game.playersCards[playerIdx].get(0).type;
                if ((cardType == Card.Type.TWO || cardType == Card.Type.THREE) && game.userVisibleState.dealerScore < 8) {
                    return splitAction;
                }
                if ((cardType == Card.Type.TWO || cardType == Card.Type.THREE) && game.userVisibleState.dealerScore >= 8) {
                    return getAction("HIT", game.availableActions);
                }
                if ((cardType == Card.Type.FOUR) && (game.userVisibleState.dealerScore >= 7 || game.userVisibleState.dealerScore <= 4)) {
                    return getAction("HIT", game.availableActions);
                }
                if ((cardType == Card.Type.FOUR) && (game.userVisibleState.dealerScore < 7 && game.userVisibleState.dealerScore > 4)) {
                    return splitAction;
                }
                if ((cardType == Card.Type.SIX) && (game.userVisibleState.dealerScore < 7)) {
                    return splitAction;
                }
                if ((cardType == Card.Type.SIX) && (game.userVisibleState.dealerScore >= 7)) {
                    return getAction("HIT", game.availableActions);
                }
                if ((cardType == Card.Type.SEVEN) && (game.userVisibleState.dealerScore >= 8)) {
                    return getAction("HIT", game.availableActions);
                }
                if ((cardType == Card.Type.SEVEN) && (game.userVisibleState.dealerScore < 8)) {
                    return splitAction;
                }
                if ((cardType == Card.Type.EIGHT)) {
                    return splitAction;
                }
                if ((cardType == Card.Type.NINE) && (game.userVisibleState.dealerScore == 7 || game.userVisibleState.dealerScore > 9)) {
                    return getAction("STAY", game.availableActions);
                }
                if ((cardType == Card.Type.NINE)) {
                    return splitAction;
                }
                if ((cardType == Card.Type.ACE)) {
                    return splitAction;
                }
            }
        }

        // SOFT STRATEGY
        if (game.playersSoftScore[playerIdx] != game.playersHardScore[playerIdx]){
            if ((game.playersSoftScore[playerIdx] == 13 || game.playersSoftScore[playerIdx] == 14)
                    && (game.userVisibleState.dealerScore < 5 || game.userVisibleState.dealerScore > 6)) {
                GameController.AvailableAction hitAction = getAction("HIT", game.availableActions);
                if (hitAction != null) {
                    return hitAction;
                }
            }
            if ((game.playersSoftScore[playerIdx] == 13 || game.playersSoftScore[playerIdx] == 14) &&
                    (game.userVisibleState.dealerScore == 5 || game.userVisibleState.dealerScore == 6)) {
                GameController.AvailableAction action = getAction("DOUBLE", game.availableActions);
                if (action != null) {
                    return action;
                } else {
                    GameController.AvailableAction hitAction = getAction("HIT", game.availableActions);
                    if (hitAction != null) {
                        return hitAction;
                    }
                }
            }
            if ((game.playersSoftScore[playerIdx] == 15 || game.playersSoftScore[playerIdx] == 16)
                    && (game.userVisibleState.dealerScore < 4 || game.userVisibleState.dealerScore > 6)) {
                GameController.AvailableAction hitAction = getAction("HIT", game.availableActions);
                if (hitAction != null) {
                    return hitAction;
                }
            }
            if ((game.playersSoftScore[playerIdx] == 15 || game.playersSoftScore[playerIdx] == 16) &&
                    (game.userVisibleState.dealerScore >= 4 && game.userVisibleState.dealerScore <= 6)) {
                GameController.AvailableAction action = getAction("DOUBLE", game.availableActions);
                if (action != null) {
                    return action;
                } else {
                    GameController.AvailableAction hitAction = getAction("HIT", game.availableActions);
                    if (hitAction != null) {
                        return hitAction;
                    }
                }
            }
            if ((game.playersSoftScore[playerIdx] == 17) &&
                    (game.userVisibleState.dealerScore >= 3 && game.userVisibleState.dealerScore <= 6)) {
                GameController.AvailableAction action = getAction("DOUBLE", game.availableActions);
                if (action != null) {
                    return action;
                } else {
                    GameController.AvailableAction hitAction = getAction("HIT", game.availableActions);
                    if (hitAction != null) {
                        return hitAction;
                    }
                }
            }
            if ((game.playersSoftScore[playerIdx] == 17) &&
                    (game.userVisibleState.dealerScore < 3 || game.userVisibleState.dealerScore > 6)) {
                GameController.AvailableAction hitAction = getAction("HIT", game.availableActions);
                if (hitAction != null) {
                    return hitAction;
                }
            }
            if ((game.playersSoftScore[playerIdx] == 18) &&
                    (game.userVisibleState.dealerScore < 3 || (game.userVisibleState.dealerScore > 6 && game.userVisibleState.dealerScore < 9))) {
                return getAction("STAY", game.availableActions);
            }
            if ((game.playersSoftScore[playerIdx] == 18) &&
                    (game.userVisibleState.dealerScore >= 3 && game.userVisibleState.dealerScore <= 6)) {
                GameController.AvailableAction doubleAction = getAction("DOUBLE", game.availableActions);
                if (doubleAction != null) {
                    return doubleAction;
                }
                return getAction("STAY", game.availableActions);
            }
            if ((game.playersSoftScore[playerIdx] == 18) &&
                    (game.userVisibleState.dealerScore >= 9)) {
                GameController.AvailableAction hitAction = getAction("HIT", game.availableActions);
                if (hitAction != null) {
                    return hitAction;
                }
            }
            if ((game.playersSoftScore[playerIdx] >= 19)) {
                return getAction("STAY", game.availableActions);
            }
        }

        // HARD STRATEGY
        if ((game.playersHardScore[playerIdx] >= 4 && game.playersHardScore[playerIdx] <= 8)) {
            return getAction("HIT", game.availableActions);
        }
        if ((game.playersHardScore[playerIdx] == 9 && game.userVisibleState.dealerScore >= 3 && game.userVisibleState.dealerScore <= 6)) {
            GameController.AvailableAction doubleAction = getAction("DOUBLE", game.availableActions);
            if (doubleAction != null) {
                return doubleAction;
            }
            return getAction("HIT", game.availableActions);
        }
        if (game.playersHardScore[playerIdx] == 9) {
            return getAction("HIT", game.availableActions);
        }
        if ((game.playersHardScore[playerIdx] == 10 && game.userVisibleState.dealerScore <= 9)) {
            GameController.AvailableAction doubleAction = getAction("DOUBLE", game.availableActions);
            if (doubleAction != null) {
                return doubleAction;
            }
            return getAction("HIT", game.availableActions);
        }
        if (game.playersHardScore[playerIdx] == 10) {
            return getAction("HIT", game.availableActions);
        }
        if ((game.playersHardScore[playerIdx] == 11 && game.userVisibleState.dealerScore <= 10)) {
            GameController.AvailableAction doubleAction = getAction("DOUBLE", game.availableActions);
            if (doubleAction != null) {
                return doubleAction;
            }
            return getAction("HIT", game.availableActions);
        }
        if ((game.playersHardScore[playerIdx] == 11)) {
            return getAction("HIT", game.availableActions);
        }
        if ((game.playersHardScore[playerIdx] == 12 && game.userVisibleState.dealerScore > 3 && game.userVisibleState.dealerScore < 7)) {
            return getAction("STAY", game.availableActions);
        }
        if (game.playersHardScore[playerIdx] == 12) {
            return getAction("HIT", game.availableActions);
        }
        if ((game.playersHardScore[playerIdx] == 13 || game.playersHardScore[playerIdx] == 14) && game.userVisibleState.dealerScore < 7) {
            return getAction("STAY", game.availableActions);
        }
        if ((game.playersHardScore[playerIdx] == 13 || game.playersHardScore[playerIdx] == 14)) {
            return getAction("HIT", game.availableActions);
        }
        if ((game.playersHardScore[playerIdx] == 13 || game.playersHardScore[playerIdx] == 14)) {
            return getAction("HIT", game.availableActions);
        }
        if ((game.playersHardScore[playerIdx] == 15) && game.userVisibleState.dealerScore == 10) {
            GameController.AvailableAction surrender = getAction("SURRENDER", game.availableActions);
            if (surrender != null) {
                return surrender;
            }
            return getAction("HIT", game.availableActions);
        }
        if ((game.playersHardScore[playerIdx] == 15) && game.userVisibleState.dealerScore > 6) {
            return getAction("HIT", game.availableActions);
        }
        if ((game.playersHardScore[playerIdx] == 16) && game.userVisibleState.dealerScore > 8) {
            GameController.AvailableAction surrender = getAction("SURRENDER", game.availableActions);
            if (surrender != null) {
                return surrender;
            }
            return getAction("HIT", game.availableActions);
        }
        if ((game.playersHardScore[playerIdx] == 16) && game.userVisibleState.dealerScore > 6) {
            return getAction("HIT", game.availableActions);
        }
        return getAction("STAY", game.availableActions);
    }

    static GameController.AvailableAction getAction(String actionString, List<GameController.AvailableAction> actions) {
        for (GameController.AvailableAction action : actions) {
            if (action.action.toString().equals(actionString)) {
                return action;
            }
        }
        return null;
    }


}
