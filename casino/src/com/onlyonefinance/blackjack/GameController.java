package com.onlyonefinance.blackjack;

import com.onlyonefinance.authentication.AuthenticationSystem;
import com.onlyonefinance.balance.BalanceSystem;

import java.util.*;

public class GameController {

    BalanceSystem balanceSystem;
    AuthenticationSystem authenticationSystem = new AuthenticationSystem();

    Map<String, GameService.UserVisibleState> games = new HashMap<>();
    GameService gameService = new GameService();

    Map<String, String> activeGameForClient = new HashMap<>();

    public synchronized GameObject startGame(String clientId, int numberOfPlayers, double[] playerBets) {
        if (!authenticationSystem.isAuthenticated(clientId)) {
            throw new RuntimeException("Client is not authenticated.");
        }

        String oldGameId = activeGameForClient.get(clientId);
        if (oldGameId != null) {
            throw new RuntimeException("Already active game for client" + clientId + ". Game id is: " + oldGameId);
        }

        GameService.UserVisibleState state = gameService.startGame(numberOfPlayers, playerBets, RandomGenerator.secureRandom);
        activeGameForClient.put(clientId, state.gameId);
        games.put(state.gameId, state);

        return populateGameObject(state);
    }

    public synchronized boolean finishGame(String clientId, String gameId) {
        String gameInMap = activeGameForClient.get(clientId);
        if (!gameId.equals(gameInMap)) {
            throw new RuntimeException("Invalid gameId: " + gameId);
        }
        activeGameForClient.remove(clientId);
        games.remove(gameId);

        return true;
    }

    public synchronized GameObject acceptAction(String clientId, String gameId, String action, String playerId, int playerIndex) {
        AvailableAction availableAction = newAction(playerId, playerIndex, Action.valueOf(action));
        String gameIdForCheck = activeGameForClient.get(clientId);
        if (!gameIdForCheck.equals(gameId)) {
            throw new RuntimeException("Invalid gameId: " + gameId);
        }
        GameService.UserVisibleState gameState = games.get(gameId);
        boolean playerOk = false;
        for (int i = 0; i < gameState.playersCards.length; ++i) {
            if (playerId.equals(gameState.playerIds[i]) && playerIndex == i) {
                playerOk = true;
                break;
            }
        }
        if (!playerOk) {
            throw new RuntimeException("Player id idx mismatch.");
        }

        if (availableAction.action == Action.HIT) {
            gameState = gameService.playerHit(gameState, playerIndex);
        } else if (availableAction.action == Action.DOUBLE) {
            gameState = gameService.playerDouble(gameState, playerIndex);
        } else if (availableAction.action == Action.STAY) {
            gameState = gameService.playerStay(gameState, playerIndex);
        } else if (availableAction.action == Action.SPLIT) {
            gameState = gameService.playerSplit(gameState, playerIndex);
        } else if (availableAction.action == Action.SURRENDER) {
            gameState = gameService.playerSurrender(gameState, playerIndex);
        }

        games.put(gameId, gameState);

        return populateGameObject(gameState);
    }

    GameObject populateGameObject(GameService.UserVisibleState userVisibleState) {
        GameObject result = new GameObject();
        result.gameId = userVisibleState.gameId;
        result.dealerCards = userVisibleState.dealerVisibleCards;
        result.dealerScore = userVisibleState.dealerScore;
        result.playersCards = new List[userVisibleState.playersCards.length];
        result.playersSoftScore = new int[userVisibleState.playersSoftScore.length];
        result.playersHardScore = new int[userVisibleState.playersHardScore.length];
        for (int i = 0; i < result.playersCards.length; ++i) {
            result.playersCards[i] = userVisibleState.playersCards[i];
            result.playersSoftScore[i] = userVisibleState.playersSoftScore[i];
            result.playersHardScore[i] = userVisibleState.playersHardScore[i];
        }

        result.availableActions = new ArrayList<>();

        result.userVisibleState = userVisibleState;


        int playerIndex = userVisibleState.playerIndex;
        if (playerIndex == userVisibleState.playerIds.length) {
            return result;
        }
        String playerId = userVisibleState.playerIds[playerIndex];
        result.availableActions.add(newAction(playerId, playerIndex, Action.HIT));
        result.availableActions.add(newAction(playerId, playerIndex, Action.STAY));
        if (result.playersCards[playerIndex].size() == 2) {
            result.availableActions.add(newAction(playerId, playerIndex, Action.DOUBLE));
            if (result.playersSoftScore[playerIndex] != 21) {
                result.availableActions.add(newAction(playerId, playerIndex, Action.SURRENDER));
            }
            if (result.playersCards[playerIndex].get(0).type == result.playersCards[playerIndex].get(1).type) {
                if (userVisibleState.splitsLeft[playerIndex] > 0) {
                    result.availableActions.add(newAction(playerId, playerIndex, Action.SPLIT));
                }
            }
        }

        return result;
    }

    public class GameObject {
        public String gameId;
        public List<Card> dealerCards;
        public List<Card> playersCards[];
        public int dealerScore;
        public int playersSoftScore[];
        public int playersHardScore[];
        public List<AvailableAction> availableActions;
        public GameService.UserVisibleState userVisibleState;
    }

    public enum Action {
        HIT, DOUBLE, STAY, SPLIT, SURRENDER, INSURANCE, EQUAL_MONEY;
    };

    public class AvailableAction {
        public String playerId;
        public int playerIdx;
        public Action action;

        @Override
        public String toString() {
            return "\nAvailableAction{" +
                    "playerId='" + playerId + '\'' +
                    ", playerIdx=" + playerIdx +
                    ", action=" + action +
                    '}';
        }
    }

    public AvailableAction newAction(String playerId, int playerIdx, Action action) {
        AvailableAction res = new AvailableAction();
        res.playerId = playerId;
        res.playerIdx = playerIdx;
        res.action = action;
        return res;
    }
}
