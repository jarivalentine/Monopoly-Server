package be.howest.ti.monopoly.web;

import be.howest.ti.monopoly.logic.IService;
import be.howest.ti.monopoly.logic.ServiceAdapter;
import be.howest.ti.monopoly.logic.implementation.Auction;
import be.howest.ti.monopoly.logic.implementation.Game;
import be.howest.ti.monopoly.logic.implementation.tiles.Tile;

import java.util.List;


public class TestService implements IService {

    IService delegate = new ServiceAdapter();

    void setDelegate(IService delegate) {
        this.delegate = delegate;
    }

    @Override
    public String getVersion() {
        return delegate.getVersion();
    }

    @Override
    public List<Tile> getTiles() {
        return delegate.getTiles();
    }

    @Override
    public Tile getTile(int position) {
        return delegate.getTile(position);
    }

    @Override
    public Tile getTile(String name) {
        return delegate.getTile(name);
    }

    @Override
    public List<Game> getGames(boolean started, int numberOfPlayers, String prefix) {
        return delegate.getGames(started, numberOfPlayers, prefix);
    }

    @Override
    public Game createGames(String prefix, int numberOfPlayers) {
        return delegate.createGames(prefix, numberOfPlayers);
    }

    @Override
    public List<String> getChance() {
        return delegate.getChance();
    }

    @Override
    public List<String> getCommunityChest() {
        return delegate.getCommunityChest();
    }

    @Override
    public List<Auction> getBankAuctions(String gameId) {
        return delegate.getBankAuctions(gameId);
    }

    @Override
    public Object placeBidOnBankAuction(String gameId, String propertyName, String bidder, int amount) {
        return delegate.placeBidOnBankAuction(gameId, propertyName, bidder, amount);
    }

    @Override
    public Object collectDebt(String gameId, String playerName, String propertyName, String debtorName) {
        return delegate.collectDebt(gameId, playerName, propertyName, debtorName);
    }

    @Override
    public Object trade(String gameId, String playerName) {
        return delegate.trade(gameId, playerName);
    }

    @Override
    public Game getGame(String gameId) {
        return delegate.getGame(gameId);
    }

    @Override
    public Game getDummyGame() {
        return delegate.getDummyGame();
    }

    public Object rollDice(String gameId, String playerName) {
        return delegate.rollDice(gameId, playerName);
    }

    @Override
    public Object declareBankruptcy(String gameId, String playerName) {
        return delegate.declareBankruptcy(gameId, playerName);
    }

    public Object joinGame(String playerName, String gameId) {
        return delegate.joinGame(playerName, gameId);
    }

    @Override
    public Object clearGameList() {
        return delegate.clearGameList();
    }

    @Override
    public Object getOutOfJailFine(String gameId, String playerName) {
        return delegate.getOutOfJailFine(gameId, playerName);
    }

    @Override
    public Object getOutOfJailFree(String gameId, String playerName) {
        return delegate.getOutOfJailFree(gameId, playerName);
    }

    @Override
    public Object buyProperty(String gameId, String playerName, String propertyName) {
        return delegate.buyProperty(gameId, playerName, propertyName);
    }

    @Override
    public Object dontBuyProperty(String gameId, String playerName, String propertyName) {
        return delegate.dontBuyProperty(gameId, playerName, propertyName);
    }

    @Override
    public Object buyHouse(String gameId, String playerName, String propertyName) {
        return delegate.buyHouse(gameId, playerName, propertyName);
    }

    @Override
    public Object sellHouse(String gameId, String playerName, String propertyName) {
        return delegate.sellHouse(gameId, playerName, propertyName);
    }

    @Override
    public Object buyHotel(String gameId, String playerName, String propertyName) {
        return delegate.buyHotel(gameId, playerName, propertyName);
    }

    @Override
    public Object sellHotel(String gameId, String playerName, String propertyName) {
        return delegate.sellHotel(gameId, playerName, propertyName);
    }

    @Override
    public Object useEstimateTax(String gameId, String playerName) {
        return delegate.useEstimateTax(gameId, playerName);
    }

    @Override
    public Object useComputeTax(String gameId, String playerName) {
        return delegate.useComputeTax(gameId, playerName);
    }

    public Object takeMortgage(String gameId, String playerName, String propertyName) {
        return delegate.takeMortgage(gameId, playerName, propertyName);
    }

    @Override
    public Object settleMortgage(String gameId, String playerName, String propertyName) {
        return delegate.settleMortgage(gameId, playerName, propertyName);
    }
}
