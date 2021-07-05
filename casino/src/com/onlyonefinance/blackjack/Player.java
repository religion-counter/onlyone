package com.onlyonefinance.blackjack;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Player {

    // TODO add insurance, blackjack check, surrender and other states
    List<Card> cards = new ArrayList<>();
    boolean isDone = false;
    boolean hasBlackjack = false;
    boolean canHit = false;
    boolean canDouble = false;
    int canSplit = 2;
    boolean canSurrender = false; // TODO implement
    boolean canInsurance = false; // TODO implement
    public String id = UUID.randomUUID().toString();

    Player() {
    }
}
