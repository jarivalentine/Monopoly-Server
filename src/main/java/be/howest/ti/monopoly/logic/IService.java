package be.howest.ti.monopoly.logic;

import be.howest.ti.monopoly.logic.implementation.Tile;

import java.util.List;

public interface IService {
    String getVersion();
    List<Tile> getTiles();
    Tile getTile(int position);
    Tile getTile(String name);
    Object getChance();
    Object getCommunityChest();

    Object rollDice(String gameId, String playerName);
    Object declareBankruptcy(String gameId, String playerName);

    Object getOutOfJailFine();
    Object getOutOfJailFree();
}
