package com.onlyonefinance.blackjack.test;

import com.onlyonefinance.blackjack.GameService;

import java.lang.reflect.Method;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GameServiceUnitTests {

    public static void main(String[] args) throws Exception {
        GameServiceUnitTests tests = new GameServiceUnitTests();

        final Method[] declaredTests = GameServiceUnitTests.class.getDeclaredMethods();

        for (Method test : declaredTests) {
            if (test.getName().contains("main")) {
                continue;
            }
            test.invoke(tests);
        }
    }

    public void testSplit2Aces2More() {

        GameService gameService = new GameService();
        GameService.UserVisibleState ustate = gameService.startGame(1, new double[] { 1 }, new RandomMock(Arrays.asList(0,30,51,52, 50, 50)));

        ustate = gameService.playerSplit(ustate, 0);

        System.out.println(ustate);

        if (ustate.playersSoftScore[0] != 12) {
            throw new RuntimeException("Wrong score");
        }
        if (ustate.playersSoftScore[1] != 12) {
            throw new RuntimeException("Wrong score");
        }
        if (ustate.playersHardScore[0] != 2) {
            throw new RuntimeException("Wrong score");
        }
        if (ustate.playersHardScore[1] != 2) {
            throw new RuntimeException("Wrong score");
        }
        if (ustate.playerWinMultiplier[0] != 0) {
            throw new RuntimeException("Wrong win multiplier");
        }
        if (ustate.playerWinMultiplier[1] != 0) {
            throw new RuntimeException("Wrong win multiplier");
        }

    }

    public void testSplit2Aces2BJs() {

        GameService gameService = new GameService();
        GameService.UserVisibleState ustate = gameService.startGame(1, new double[] { 1 }, new RandomMock(Arrays.asList(0,30,51,52, 35, 35)));

        ustate = gameService.playerSplit(ustate, 0);

        if (ustate.playerWinMultiplier[0] != 2.0) {
            throw new RuntimeException("Unexpected win multiplier for player 0: " + ustate.playerWinMultiplier[0]);
        }
        if (ustate.playerWinMultiplier[1] != 2.0) {
            throw new RuntimeException("Unexpected win multiplier for player 1: " + ustate.playerWinMultiplier[1]);
        }

        System.out.println(ustate);
    }

    public void testSplit2AcesAgainstBJ() {

        GameService gameService = new GameService();
        GameService.UserVisibleState ustate = gameService.startGame(1, new double[] { 1 }, new RandomMock(Arrays.asList(0,36,51,52, 36, 36)));

        System.out.println(ustate);
        if (ustate.playerWinMultiplier[0] != 0.0) {
            throw new RuntimeException("Unexpected win multiplier for player 0: " + ustate.playerWinMultiplier[0]);
        }
    }

    public void testNaturals() {

        GameService gameService = new GameService();
        GameService.UserVisibleState ustate = gameService.startGame(1, new double[] { 1 }, new RandomMock(Arrays.asList(0,36,0,36)));

        System.out.println(ustate);
        if (ustate.playerWinMultiplier[0] != 1.0) {
            throw new RuntimeException("Unexpected win multiplier for player 0: " + ustate.playerWinMultiplier[0]);
        }
    }

    private class RandomMock extends SecureRandom {

        List<Integer> forDraw;
        int currentIndex = 0;

        public RandomMock(List<Integer> forDraw) {
            this.forDraw = forDraw;
        }

        @Override
        public int nextInt(int bound) {
            return forDraw.get(currentIndex++);
        }
    }
}
