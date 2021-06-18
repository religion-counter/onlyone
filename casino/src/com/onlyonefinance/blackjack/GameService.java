package com.onlyonefinance.blackjack;

public class GameService {

    public GameState startGame(int numberOfPlayers) {
        final GameState res = new GameState(numberOfPlayers);

        return res;
    }

    public GameState playerHit(GameState state, int playerIndex) {

        // Check if player can hit and player score

        state.players.get(playerIndex).cards.add(state.getRandomCard());

        return state;
    }


    public GameState playerDouble(GameState state, int playerIndex) {

        // Check if player can double and player score
        state.players.get(playerIndex).cards.add(state.getRandomCard());
        state.players.get(playerIndex).isDone = true;

        return state;
    }

    public GameState playerStay(GameState state, int playerIndex) {

        // Check if player can stay and player score
        state.players.get(playerIndex).isDone = true;

        return state;
    }

    

}
