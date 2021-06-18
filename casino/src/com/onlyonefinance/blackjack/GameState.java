package com.onlyonefinance.blackjack;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class GameState {

    final int NUMBER_OF_DECKS = 6;

    final String gameId;
    final List<Player> players;
    final List<Card> dealerCards;
    int playerIndex;
    int numberOfPlayers;

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

    Card getRandomCard() {
        if (cardsForDraw.isEmpty()) {
            throw new RuntimeException("Deck is empty.");
        }
        final int cardIndex = RandomGenerator.secureRandom.nextInt(cardsForDraw.size());
        Card result = cardsForDraw.get(cardIndex);
        cardsForDraw.remove(cardIndex);
        return result;
    }

    GameState(final int numberOfPlayers) {
        gameId = UUID.randomUUID().toString();
        players = new ArrayList<>();
        for (int i = 0; i < numberOfPlayers; ++i) {
            players.add(new Player());
        }
        dealerCards = new ArrayList<>();
        this.numberOfPlayers = numberOfPlayers;
        playerIndex = 0;

        while (getSoftScore(dealerCards) < 16 && getHardScore(dealerCards) < 16) {
            dealerCards.add(getRandomCard());
        }

        fillDeck();
        for (int player = 0; player < players.size(); ++player) {
            players.get(player).cards.add(getRandomCard());
            players.get(player).cards.add(getRandomCard());
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
