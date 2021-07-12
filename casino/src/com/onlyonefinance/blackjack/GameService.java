package com.onlyonefinance.blackjack;

import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * TODO Return read only objects.
 */
public class GameService {

    public final static int MAX_NUMBER_OF_PLAYERS = 8;

    public UserVisibleState startGame(int numberOfPlayers, double[] playerBets, SecureRandom randomGenerator) {
        if (numberOfPlayers < 1 || numberOfPlayers > MAX_NUMBER_OF_PLAYERS) {
            throw new RuntimeException("Invalid number of players for one game: " + numberOfPlayers);
        }
        final GameState res = new GameState(numberOfPlayers, playerBets, randomGenerator);

        if (res.dealerBlackjack) {
            res.playerIndex = res.players.size();
        }

        return new UserVisibleState(res);
    }

    void advancePlayer(GameState state) {
        if (state.playerIndex == state.numberOfPlayers) {
            throw new RuntimeException("Already last player");
        }
        while (state.playerIndex < state.numberOfPlayers) {
            state.playerIndex += 1;
            if (state.players.size() == state.playerIndex) {
                break;
            }
            if (state.players.get(state.playerIndex).isDone) {
                continue;
            }
            break;
        }
    }

    public UserVisibleState playerHit(UserVisibleState ustate, int playerIndex) {

        GameState state = ustate.state;

        if (state.playerIndex == state.numberOfPlayers) {
            throw new RuntimeException("Game is already finished.");
        }

        Player player = state.players.get(playerIndex);

        if (player.isDone) {
            throw new RuntimeException("Player finished his turn.");
        }

        int hardScore = state.getHardScore(player.cards);
        if (hardScore > 20) {
            throw new RuntimeException("Player cannot hit with score: " + hardScore);
        }

        player.cards.add(state.getRandomCard());
        hardScore = state.getHardScore(player.cards);
        if (hardScore > 20) {
            player.isDone = true;
            advancePlayer(state);
        }

        return new UserVisibleState(state);
    }


    public UserVisibleState playerDouble(UserVisibleState ustate, int playerIndex) {

        GameState state = ustate.state;

        if (state.playerIndex == state.numberOfPlayers) {
            throw new RuntimeException("Game is already finished.");
        }

        state.playerBets[playerIndex] *= 2;

        Player player = state.players.get(playerIndex);
        if (player.isDone) {
            throw new RuntimeException("Player finished his turn.");
        }

        int hardScore = state.getHardScore(player.cards);
        if (hardScore > 20) {
            throw new RuntimeException("Player cannot hit with score: " + hardScore);
        }

        player.cards.add(state.getRandomCard());
        player.isDone = true;
        advancePlayer(state);

        return new UserVisibleState(state);
    }

    public UserVisibleState playerSplit(UserVisibleState ustate, int playerIndex) {

        GameState state = ustate.state;

        if (state.playerIndex == state.numberOfPlayers) {
            throw new RuntimeException("Game is already finished.");
        }

        Player player = state.players.get(playerIndex);

        if (player.isDone) {
            throw new RuntimeException("Player is done.");
        }
        boolean canSplit = false;
        if (player.cards.size() == 2) {
            if (player.cards.get(0).type == player.cards.get(1).type) {
                canSplit = true;
            }
        }
        if (!canSplit) {
            throw new RuntimeException("Can split only 2 equal cards.");
        }
        if (player.canSplit < 1) {
            throw new RuntimeException("Player can split only twice");
        }
        double[] newBets = new double[state.playerBets.length + 1];
        for (int i = 0; i < state.playerBets.length; ++i) {
            if (i < playerIndex) {
                newBets[i] = state.playerBets[i];
            } else {
                newBets[playerIndex] = state.playerBets[playerIndex];
                newBets[i+1] = state.playerBets[i];
            }
        }
        state.playerBets = newBets;

        player.canSplit -= 1;
        Card cardForNewPlayer = player.cards.remove(1);

        Player newPlayer = new Player();
        newPlayer.canSplit = player.canSplit;
        player.cards.add(state.getRandomCard());
        newPlayer.cards.add(cardForNewPlayer);
        newPlayer.cards.add(state.getRandomCard());
        state.numberOfPlayers += 1;
        state.players.add(playerIndex + 1, newPlayer);
        if (cardForNewPlayer.type == Card.Type.ACE) {
            advancePlayer(state);
            advancePlayer(state);
            player.isDone = true;
            newPlayer.isDone = true;
        }

        return new UserVisibleState(state);
    }

    public UserVisibleState playerSurrender(UserVisibleState ustate, int playerIndex) {

        GameState state = ustate.state;

        if (state.playerIndex == state.numberOfPlayers) {
            throw new RuntimeException("Game is already finished.");
        }

        if (state.playerIndex != playerIndex) {
            throw new RuntimeException("Player is not on turn. Expected player: " + state.playerIndex + ", Player received: " + playerIndex);
        }
        Player player = state.players.get(playerIndex);
        if (player.isDone) {
            throw new RuntimeException("Player " + playerIndex + " is done.");
        }
        if (player.hasBlackjack) {
            throw new RuntimeException("Player can surrender only if he doesn't have blackjack.");
        }

        if (player.cards.size() > 2) {
            throw new RuntimeException("Player can surrender only before drawing any cards.");
        }
        player.surrendered = true;
        player.isDone = true;


        advancePlayer(state);
        return new UserVisibleState(state);
    }

    public UserVisibleState playerStay(UserVisibleState ustate, int playerIndex) {

        GameState state = ustate.state;

        if (state.playerIndex == state.numberOfPlayers) {
            throw new RuntimeException("Game is already finished.");
        }

        if (state.playerIndex != playerIndex) {
            throw new RuntimeException("Not expected stay because player is not on turn.");
        }
        Player player = state.players.get(playerIndex);
        if (player.isDone) {
            throw new RuntimeException("Player is done with his turns.");
        }
        player.isDone = true;
        advancePlayer(state);

        return new UserVisibleState(state);
    }

    public class UserVisibleState {
        public final List<Card> dealerVisibleCards;
        public final int dealerScore;
        public final boolean dealerBlackjack;
        public final List<Card>[] playersCards;
        public final int[] playersSoftScore;
        public final int[] playersHardScore;
        public int playerIndex;
        public final boolean[] playerBlackjackStatus;
        public final int splitsLeft[]; // TODO implement split
        public final boolean canCurrentPlayerDouble = false; // TODO implement double
        private final GameState state;
        public final double[] playerWinMultiplier;
        public final String gameId;
        public String[] playerIds;

        UserVisibleState(GameState state) {
            gameId = state.gameId;
            splitsLeft = new int[state.numberOfPlayers];
            final List<Card> dealerCards = state.dealerCards;
            if (state.playerIndex == state.numberOfPlayers) {
                dealerVisibleCards = Collections.unmodifiableList(dealerCards);
                int dealerSoftScore = state.getSoftScore(dealerCards);
                int dealerHardScore = state.getHardScore(dealerCards);
                if (dealerHardScore > 16) {
                    dealerScore = dealerHardScore;
                } else {
                    dealerScore = dealerSoftScore;
                }
                if (dealerSoftScore == 21 && dealerCards.size() == 2) {
                    dealerBlackjack = true;
                } else {
                    dealerBlackjack = false;
                }
            } else if (state.dealerBlackjack) {
                dealerVisibleCards = Collections.unmodifiableList(dealerCards);
                dealerScore = state.BLACKJACK_SCORE;
                dealerBlackjack = true;
            } else {
                // First we return only the first card of the dealer
                dealerVisibleCards = Collections.singletonList(dealerCards.get(0));
                dealerScore = state.getSoftScore(dealerVisibleCards);
                dealerBlackjack = false;
            }

            playerWinMultiplier = new double[state.numberOfPlayers];

            int numPlayers = state.players.size();
            playersCards = new List[numPlayers];
            playersSoftScore = new int[numPlayers];
            playersHardScore = new int[numPlayers];
            playerBlackjackStatus = new boolean[numPlayers];
            playerIndex = state.playerIndex;
            playerIds = new String[numPlayers];
            for (int i = 0; i < state.players.size(); ++i) {
                final Player player = state.players.get(i);
                splitsLeft[i] = player.canSplit;
                playerIds[i] = player.id;
                playersCards[i] = Collections.unmodifiableList(player.cards);
                playersSoftScore[i] = state.getSoftScore(playersCards[i]);
                playersHardScore[i] = state.getHardScore(playersCards[i]);
                if (playersSoftScore[i] == state.BLACKJACK_SCORE && player.cards.size() == 2) {
                    if (i == state.playerIndex) {
                        advancePlayer(state);
                        playerIndex = state.playerIndex;
                    }
                    playerBlackjackStatus[i] = true;
                    player.isDone = true;
                    if (dealerBlackjack) {
                        playerWinMultiplier[i] = 1;
                    } else {
                        if (player.canSplit == 2) {
                            playerWinMultiplier[i] = 2.5;
                        } else {
                            playerBlackjackStatus[i] = false;
                            playerWinMultiplier[i] = 2;
                        }
                    }
                }
                if (state.playerIndex == numPlayers) {
                    // Game is finished. Calculate winnings.
                    if (playersSoftScore[i] < 22 && playersSoftScore[i] > dealerScore) {
                        if (playerWinMultiplier[i] < 1) {
                            playerWinMultiplier[i] = 2;
                        }
                    } else if (playersHardScore[i] < 22 && playersHardScore[i] > dealerScore) {
                        if (playerWinMultiplier[i] < 1) {
                            playerWinMultiplier[i] = 2;
                        }
                    } else if (dealerScore > 21 && playersHardScore[i] < 22) {
                        if (playerWinMultiplier[i] < 1) {
                            playerWinMultiplier[i] = 2;
                        }
                    } else if (playersSoftScore[i] < 22 && playersSoftScore[i] == dealerScore) {
                        if (playerWinMultiplier[i] < 1) {
                            playerWinMultiplier[i] = 1;
                        }
                    } else if (playersHardScore[i] < 22 && playersHardScore[i] == dealerScore) {
                        if (playerWinMultiplier[i] < 1) {
                            playerWinMultiplier[i] = 1;
                        }
                    } else {
                        playerWinMultiplier[i] = 0;
                    }
                }
                if (playersHardScore[i] < 22 && player.surrendered) {
                    playerWinMultiplier[i] = 0.5;
                }
            }
            this.state = state;
        }

        @Override
        public String toString() {
            boolean showWinnings = playerIndex == state.numberOfPlayers;
            StringBuilder playerCards = new StringBuilder();
            for (int i = 0; i < playersCards.length; ++i) {
                playerCards.append("Player " + i + " cards: " + Arrays.toString(playersCards[i].toArray()) + ": ");
                if (playerBlackjackStatus[i]) {
                    playerCards.append("Black Jack");
                } else if (playersSoftScore[i] != playersHardScore[i]) {
                    playerCards.append("(" + playersHardScore[i] + ", " + playersSoftScore[i] + ")");
                } else {
                    playerCards.append(playersSoftScore[i]);
                }
                if (showWinnings) {
                    playerCards.append(", Win multiplier: " + playerWinMultiplier[i]);
                    playerCards.append(", bet is: " + state.playerBets[i]);
                }
                playerCards.append('\n');
            }
            String result = "Dealer cards: " + Arrays.toString(dealerVisibleCards.toArray()) + ": " +
                    (dealerBlackjack ? "Black Jack" : dealerScore) + "\n" +
                    playerCards + "\n";
            if (playerIndex < playersCards.length) {
                result += "Player " + playerIndex + " turn: ";
            } else {
                result += "========================================";
            }
            return result;
        }
    }
}
