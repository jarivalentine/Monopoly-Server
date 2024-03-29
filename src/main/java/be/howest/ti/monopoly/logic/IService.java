package be.howest.ti.monopoly.logic;

import be.howest.ti.monopoly.logic.implementation.Auction;
import be.howest.ti.monopoly.logic.implementation.Game;
import be.howest.ti.monopoly.logic.implementation.tiles.Tile;

import java.util.List;

public interface IService {

    String getVersion();
    List<Tile> getTiles();
    Tile getTile(int position);
    Tile getTile(String name);
    List<String> getChance();
    List<String> getCommunityChest();

    List<Game> getGames(boolean started, int numberOfPlayers, String prefix);
    Game createGames(String prefix, int numberOfPlayers);
    Object joinGame(String playerName, String gameId);
    Object clearGameList();

    Game getGame(String gameId);
    Game getDummyGame();

    Object rollDice(String gameId, String playerName);
    Object declareBankruptcy(String gameId, String playerName);

    Object useEstimateTax(String gameId, String playerName);
    Object useComputeTax(String gameId, String playerName);

    Object buyProperty(String gameId, String playerName, String propertyName);
    Object dontBuyProperty(String gameId, String playerName, String propertyName);

    Object buyHouse(String gameId, String playerName, String propertyName);
    Object sellHouse(String gameId, String playerName, String propertyName);
    Object buyHotel(String gameId, String playerName, String propertyName);
    Object sellHotel(String gameId, String playerName, String propertyName);

    Object takeMortgage(String gameId, String playerName, String propertyName);
    Object settleMortgage(String gameId, String playerName, String propertyName);

    Object collectDebt(String gameId, String playerName, String propertyName, String debtorName);
    Object trade(String gameId, String playerName);

    Object getOutOfJailFine(String gameId, String playerName);
    Object getOutOfJailFree(String gameId, String playerName);

    List<Auction> getBankAuctions(String gameId);
    Object placeBidOnBankAuction(String gameId, String propertyName, String bidder, int amount);
}
