package com.onlyonefinance.blackjack;

import java.util.HashMap;
import java.util.Map;

public class Card {

    static Map<Type, Integer> VALUES = new HashMap<>();

    public final Type type;
    public final Kind kind;

    Card(Type type, Kind kind) {
        this.type = type;
        this.kind = kind;
    }

    static {
        VALUES.put(Type.ACE, 1);
        VALUES.put(Type.TWO, 2);
        VALUES.put(Type.THREE, 3);
        VALUES.put(Type.FOUR, 4);
        VALUES.put(Type.FIVE, 5);
        VALUES.put(Type.SIX, 6);
        VALUES.put(Type.SEVEN, 7);
        VALUES.put(Type.EIGHT, 8);
        VALUES.put(Type.NINE, 9);
        VALUES.put(Type.TEN, 10);
        VALUES.put(Type.JACK, 10);
        VALUES.put(Type.QUEEN, 10);
        VALUES.put(Type.KING, 10);
    }

    public enum Type {
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

    public enum Kind {
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

    @Override
    public String toString() {
        return type.toString() + kind;
    }
}
