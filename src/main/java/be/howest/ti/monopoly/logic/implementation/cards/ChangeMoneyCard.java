package be.howest.ti.monopoly.logic.implementation.cards;

import be.howest.ti.monopoly.logic.implementation.Game;
import be.howest.ti.monopoly.logic.implementation.Player;
import be.howest.ti.monopoly.logic.implementation.Turn;

public class ChangeMoneyCard extends Card {

    private int changeBalance;

    public ChangeMoneyCard(String description, int changeBalance) {
        super(description);
        this.changeBalance = changeBalance;
    }

    @Override
    public void executeCard(Player currentPlayer, Game game, Turn turn) {
    }
}
