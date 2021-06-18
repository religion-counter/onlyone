package com.onlyonefinance.blackjack;

import java.util.ArrayList;
import java.util.List;

public class Player {

    // TODO add insurance, blackjack check, surrender and other states
    List<Card> cards;
    boolean isDone;

    Player() {
        cards = new ArrayList<>();
        isDone = false;
    }
}
