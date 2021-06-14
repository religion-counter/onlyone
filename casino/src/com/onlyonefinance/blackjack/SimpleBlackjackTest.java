package com.onlyonefinance.blackjack;

import java.security.SecureRandom;
import java.util.*;

public class SimpleBlackjackTest {

    static Random rand = new SecureRandom();

    static final int NUMBER_OF_DECKS = 8;

    static enum Card {
        ACE,
        TWO,
        THREE,
        FOUR,
        FIVE,
        SIX,
        SEVEN,
        EIGHT,
        NINE,
        TEN,
        JACK,
        QUEEN,
        KING;

        @Override
        public String toString() {
            switch (this) {
                case ACE: return "A";
                case TWO: return "2";
                case THREE: return "3";
                case FOUR: return "4";
                case FIVE: return "5";
                case SIX: return "6";
                case SEVEN: return "7";
                case EIGHT: return "8";
                case NINE: return "9";
                case TEN: return "10";
                case JACK: return "J";
                case QUEEN: return "Q";
                case KING: return "K";
                default: throw new RuntimeException("Invalid card " + this);
            }
        }
    }

    static enum Kind {
        CLUBS,
        DIAMONDS,
        HEARTS,
        SPADE;

        @Override
        public String toString() {
            switch (this) {
                case CLUBS:
                    return "♣";
                case DIAMONDS:
                    return "♦";
                case HEARTS:
                    return "♥";
                case SPADE:
                    return "♠";
                default:
                    throw new RuntimeException("Invalid kind: " + this);
            }
        }
    }

    static Map<Card, Integer> cardValues = new HashMap<>();

    static List<CardWithKind> cardsForDraw = new ArrayList<>();
    static List<CardWithKind> drawnCards = new ArrayList<>();

    static CardWithKind drawCard() {
        if (cardsForDraw.isEmpty()) {
            throw new RuntimeException("Deck is empty.");
        }
        int index = rand.nextInt(cardsForDraw.size());
        CardWithKind result = cardsForDraw.get(index);
        cardsForDraw.remove(index);
        return result;
    }

    static {
        cardValues.put(Card.ACE, 1);
        cardValues.put(Card.TWO, 2);
        cardValues.put(Card.THREE, 3);
        cardValues.put(Card.FOUR, 4);
        cardValues.put(Card.FIVE, 5);
        cardValues.put(Card.SIX, 6);
        cardValues.put(Card.SEVEN, 7);
        cardValues.put(Card.EIGHT, 8);
        cardValues.put(Card.NINE, 9);
        cardValues.put(Card.TEN, 10);
        cardValues.put(Card.JACK, 10);
        cardValues.put(Card.QUEEN, 10);
        cardValues.put(Card.KING, 10);

        for (int i = 0; i < NUMBER_OF_DECKS; ++i) {
            for (Card card : Card.values()) {
                for (Kind kind : Kind.values()) {
                    cardsForDraw.add(new CardWithKind(card, kind));
                }
            }
        }
    }

    static ArrayList<CardWithKind> playerCards = new ArrayList<>();
    static ArrayList<CardWithKind> dealerCards = new ArrayList<>();

    static Scanner input = new Scanner(System.in);

    /**
     * @param cards
     * @return pair [soft, hard] scores;
     */
    static int[] calculateScore(List<CardWithKind> cards) {
        int resS = 0, resH = 0;
        for (CardWithKind card : cards) {
            if (card.card.equals(Card.ACE)) {
                if (resS <= 10) {
                    resS += 11;
                } else {
                    resS += 1;
                }
                resH += 1;
            }  else {
                resS += cardValues.get(card.card);
                resH += cardValues.get(card.card);
            }
        }
        return new int[] { resS, resH };
    }

    static void printInitialDealerScore() {
        CardWithKind hidden = dealerCards.remove(dealerCards.size() - 1);
        int[] dealerScores = calculateScore(dealerCards);
        if (dealerScores[0] != dealerScores[1]) {
            System.out.println("Dealer has: " + dealerCards.get(0) +
                    ": " + dealerScores[1] + ", " + dealerScores[0]);
        } else {
            System.out.println("Dealer has: " + dealerCards.get(0) +
                    ": " + dealerScores[0]);
        }
        dealerCards.add(hidden);
    }

    static int[] printDealerScore() {
        int[] dealerScores = calculateScore(dealerCards);
        StringBuilder sb = new StringBuilder("Dealer has: ");
        for (int i = 0; i < dealerCards.size(); ++i) {
            sb.append(dealerCards.get(i)).append(
                    i == dealerCards.size() - 1
                            ? ": "
                            : ", ");
        }
        if (dealerScores[0] != dealerScores[1]) {
            sb.append(dealerScores[1] + ", " + dealerScores[0]);
        } else {
            sb.append(dealerScores[0]);
        }
        System.out.println(sb);
        return dealerScores;
    }

    static int[] printPlayerScore() {
        StringBuilder sb = new StringBuilder("Player has: ");
        for (int i = 0; i < playerCards.size(); ++i) {
            sb.append(playerCards.get(i)).append(
                    i == playerCards.size() - 1
                        ? ": "
                        : ", ");
        }
        int[] scores = calculateScore(playerCards);
        if (scores[0] != scores[1]) {
            sb.append(scores[1] + ", " + scores[0]);
        } else {
            sb.append(scores[0]);
        }
        System.out.println(sb);
        return scores;
    }

    public static void main(String[] args) {
        playerCards.add(drawCard());
        dealerCards.add(drawCard());
        playerCards.add(drawCard());
        dealerCards.add(drawCard());
        printInitialDealerScore();
        int[] playerScore;

        WHILE: while (true) {
            playerScore = printPlayerScore();
            if (playerScore[1] > 21) {
                System.out.println("Player busted. Dealer wins.");
                return;
            }
            System.out.println("What's your turn: Hit(H), Double(D), Stand(S), Split(SP), Surrender(U)?");
            String turn = input.next();
            System.out.println("Player choose: " + turn);
            switch (turn) {
                case "H":
                    playerCards.add(drawCard());
                    break;
                case "D":
                case "S":
                    break WHILE;
                case "SP":
                case "U":
                    throw new RuntimeException("Not implemented yet.");
                default:
                    throw new RuntimeException("Invalid turn: " + turn);
            }
        }

        int[] dealerScore = printDealerScore();
        while (dealerScore[0] < 17 && dealerScore[1] < 17 ||
                dealerScore[0] > 21 && dealerScore[1] < 17) {
            dealerCards.add(drawCard());
            dealerScore = printDealerScore();
            if (dealerScore[1] > 21) {
                System.out.println("Dealer busted. Player wins.");
                return;
            }
        }
        int dscore, pscore;
        if (Math.max(dealerScore[0], dealerScore[1]) > 21) {
            dscore = Math.min(dealerScore[0], dealerScore[1]);
        } else {
            dscore = Math.max(dealerScore[0], dealerScore[1]);
        }
        if (Math.max(playerScore[0], playerScore[1]) > 21) {
            pscore = Math.min(playerScore[0], playerScore[1]);
        } else {
            pscore = Math.max(playerScore[0], playerScore[1]);
        }

        if (dscore < pscore) {
            System.out.println("Player wins with " + pscore + " over " + dscore);
        } else if (dscore > pscore) {
            System.out.println("Dealer wins with " + dscore + " over " + pscore);
        } else {
            System.out.println("Draw. Player has the same score as dealer: " + pscore);
        }
    }

    // Blackjack turns:
    // Give players one card
    // Give dealer one hidden card
    // Give players second card
    // Give dealer second open card


    static class CardWithKind {
        Card card;
        Kind kind;

        CardWithKind(Card card, Kind kind) {
            this.card = card;
            this.kind = kind;
        }

        @Override
        public String toString() {
            return this.card.toString() +  this.kind;
        }
    }
}
