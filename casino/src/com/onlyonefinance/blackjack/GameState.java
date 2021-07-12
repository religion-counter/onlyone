package com.onlyonefinance.blackjack;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class GameState {

    final int NUMBER_OF_DECKS = 8;
    final int DEALER_LIMIT = 17;
    final int BLACKJACK_SCORE = 21;

    final String gameId;
    final List<Player> players;
    final List<Card> dealerCards;
    boolean dealerBlackjack = false;
    int playerIndex;
    int numberOfPlayers;
    double[] playerBets;
    final SecureRandom randomGenerator;

    final List<Card> cardsForDraw = new ArrayList<>();

    void fillDeck() {
        for (int i = 0; i < NUMBER_OF_DECKS; ++i) {
            for (Card.Type type : Card.Type.values()) {
                for (Card.Kind kind : Card.Kind.values()) {
                    cardsForDraw.add(new Card(type, kind));
                }
            }
        }
    }

    public Card getRandomCard() {
        if (cardsForDraw.isEmpty()) {
            throw new RuntimeException("Deck is empty.");
        }
        final int cardIndex = this.randomGenerator.nextInt(cardsForDraw.size());
        Card result = cardsForDraw.get(cardIndex);
        cardsForDraw.remove(cardIndex);
        return result;
    }

    public GameState(final int numberOfPlayers, double[] playerBets, SecureRandom randomGenerator) {
        this.randomGenerator = randomGenerator;
        this.playerBets = playerBets;
        gameId = UUID.randomUUID().toString();
        players = new ArrayList<>();
        for (int i = 0; i < numberOfPlayers; ++i) {
            players.add(new Player());
        }
        dealerCards = new ArrayList<>();
        this.numberOfPlayers = numberOfPlayers;
        playerIndex = 0;

        fillDeck();

        while (getSoftScore(dealerCards) <= DEALER_LIMIT && getHardScore(dealerCards) < DEALER_LIMIT) {
            dealerCards.add(getRandomCard());
        }

        if (dealerCards.size() == 2 && getSoftScore(dealerCards) == BLACKJACK_SCORE) {
            dealerBlackjack = true;
        }

        for (int playerIdx = 0; playerIdx < players.size(); ++playerIdx) {
            final Player player = players.get(playerIdx);
            for (int j = 0; j < 2; ++j) {
                player.cards.add(getRandomCard());
            }
            if (getSoftScore(player.cards) == BLACKJACK_SCORE) {
                player.hasBlackjack = true;
            }
        }
    }

    int getSoftScore(List<Card> cards) {
        int res = 0;
        for (Card c : cards) {
            if (c.type == Card.Type.ACE) {
                if (res <= 10) {
                    res += 11;
                } else {
                    res += 1;
                }
            } else {
                res += Card.VALUES.get(c.type);
            }
        }
        return res;
    }

    int getHardScore(List<Card> cards) {
        int res = 0;
        for (Card c : cards) {
            res += Card.VALUES.get(c.type);
        }
        return res;
    }

}
