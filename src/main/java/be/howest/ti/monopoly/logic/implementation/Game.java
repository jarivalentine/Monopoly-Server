package be.howest.ti.monopoly.logic.implementation;

import be.howest.ti.monopoly.logic.exceptions.MonopolyResourceNotFoundException;
import be.howest.ti.monopoly.logic.implementation.factories.TileFactory;
import be.howest.ti.monopoly.logic.implementation.tiles.Property;
import be.howest.ti.monopoly.logic.implementation.tiles.Street;
import be.howest.ti.monopoly.logic.implementation.tiles.Tile;

import java.security.SecureRandom;
import java.util.*;
import be.howest.ti.monopoly.logic.exceptions.IllegalMonopolyActionException;

public class Game {

    private final List<Tile> tiles;

    private static int games = 1;

    private final int numberOfPlayers;
    private final String prefix;
    private final List<Player> players;
    private final List<Turn> turns;
    private final String id;
    private boolean started;
    private boolean ended;
    private boolean canRoll;
    private Player currentPlayer;
    private Player winner;
    private int availableHouses;
    private int availableHotels;

    public Game(int numberOfPlayers, String prefix) {
        this.tiles = new TileFactory().createTiles();
        this.numberOfPlayers = numberOfPlayers;
        this.prefix = prefix;
        this.players = new ArrayList<>();
        this.id = prefix + "_" + games;
        games += 1;
        this.started = false;
        this.ended = false;
        this.canRoll = true;
        this.currentPlayer = null;
        this.winner = null;
        this.availableHouses = 32;
        this.availableHotels = 12;
        this.turns = new ArrayList<>();
    }

    public void startGame() {
        this.started = true;
    }

    public void addPlayer(Player player) {
        if (players.size() == numberOfPlayers) throw new IllegalMonopolyActionException("Game already full");
        if (started) throw new IllegalMonopolyActionException("Game already started");
        players.add(player);
        if (currentPlayer == null) currentPlayer = player;
        if (players.size() == numberOfPlayers) { started = true; }
    }

    public void rollDice() {
        if (canRoll && started) {
            SecureRandom random = new SecureRandom();
            int dice1 = random.nextInt(6) + 1;
            int dice2 = random.nextInt(6) + 1;
            int total = dice1 + dice2;
            Tile nextTile = getNextTile(currentPlayer.getCurrentTile(), total);
            if (checkIfGoToJail(nextTile, dice1, dice2)) {
                turnGoToJail(dice1, dice2);
            } else if (currentPlayer.isJailed()) {
                turnInJail(dice1, dice2, nextTile);
            } else {
                currentPlayer.moveTile(nextTile.getName());
                turns.add(new Turn(currentPlayer.getName(), dice1, dice2));
                if (dice1 != dice2) currentPlayer = getNextPlayer();
                if (isProperty(nextTile)) canRoll = false;
            }
        } else throw new IllegalMonopolyActionException("You can't roll your dice");
    }

    public boolean checkIfGoToJail(Tile nextTile, int dice1, int dice2) {
         if (Objects.equals(nextTile.getType(), "Go to Jail")) {
             return true;
         } else {
             Turn lastTurn = turns.get(turns.size() - 1);
             Turn secondLastTurn = turns.get(turns.size() - 2);
             if (!Objects.equals(lastTurn.getName(), currentPlayer.getName()) || !Objects.equals(secondLastTurn.getName(), currentPlayer.getName())) {
                 return false;
             } else
                 return dice1 == dice2 && lastTurn.isDouble() && secondLastTurn.isDouble();
         }
    }

    private void turnGoToJail(int dice1, int dice2) {
        currentPlayer.goToJail();
        turns.add(new Turn(currentPlayer.getName(), dice1, dice2));
        currentPlayer = getNextPlayer();
    }

    private void turnInJail(int dice1, int dice2, Tile nextTile) {
        if (dice1 == dice2) {
            currentPlayer.getOutOfJailDouble();
        } else {
            if (currentPlayer.getTriesToGetOutOfJail() < 3) {
                currentPlayer.addTrieToGetOutOfJail();
                nextTile = getTile("Repair");
            } else {
                currentPlayer.getOutOfJailFine();
            }
        }
        currentPlayer.moveTile(nextTile.getName());
        turns.add(new Turn(currentPlayer.getName(), dice1, dice2));
        currentPlayer = getNextPlayer();
        if (isProperty(nextTile)) canRoll = false;
    }

    public void buyProperty(String playerName, String propertyName) {
        Player player = getPlayer(playerName);
        Tile tile = getTile(propertyName);
        player.buyProperty((Property) tile);
        setCanRoll(true);
    }

    public void buyHouse(String playerName, String propertyName) {
        if (availableHouses > 0) {
            Player player = getPlayer(playerName);
            PlayerProperty playerProperty = getPlayerProperty(player.getProperties(), propertyName);
            int amount = getStreet(playerProperty.getName()).getHousePrice();
            if (playerProperty.getHouseCount() < 4) {
                player.spendMoney(amount);
                playerProperty.increaseHouseCount();
                availableHouses--;
            } else {
                throw new IllegalMonopolyActionException("You already have 4 houses on this property.");
            }
        } else {
            throw new IllegalMonopolyActionException("There are no more houses left.");
        }
    }

    public void sellHouse(String playerName, String propertyName) {
        Player player = getPlayer(playerName);
        PlayerProperty playerProperty = getPlayerProperty(player.getProperties(), propertyName);
        int amount = getStreet(playerProperty.getName()).getHousePrice();
        if (playerProperty.getHouseCount() > 0) {
            player.receiveMoney(amount);
            playerProperty.decreaseHouseCount();
            availableHouses ++;
        } else {
            throw new IllegalMonopolyActionException("You don't have any houses on this property.");
        }
    }

    public void buyHotel(String playerName, String propertyName) {
        if (availableHotels > 0) {
            Player player = getPlayer(playerName);
            PlayerProperty playerProperty = getPlayerProperty(player.getProperties(), propertyName);
            int amount = getStreet(playerProperty.getName()).getHousePrice();
            if (playerProperty.getHouseCount() == 4 && playerProperty.getHotelCount() == 0) {
                player.spendMoney(amount);
                playerProperty.increaseHotelCount();
                availableHotels--;
            } else {
                throw new IllegalMonopolyActionException("You already have a hotel or you have not enough houses on this property.");
            }
        } else {
            throw new IllegalMonopolyActionException("There are no more hotels left.");
        }
    }

    public void sellHotel(String playerName, String propertyName) {
        Player player = getPlayer(playerName);
        PlayerProperty playerProperty = getPlayerProperty(player.getProperties(), propertyName);
        int amount = getStreet(playerProperty.getName()).getHousePrice();
        if (playerProperty.getHotelCount() == 1) {
            player.receiveMoney(amount);
            playerProperty.decreaseHotelCount();
            availableHotels ++;
        } else {
            throw new IllegalMonopolyActionException("You don't have a hotel on this property.");
        }
    }

    public void collectDebt(String playerName, String propertyName, String debtorName) {
        Player debtor = getPlayer(debtorName);
        Player receiver = getPlayer(playerName);
        int amountOfHouses = getPlayerProperty(receiver.getProperties(), propertyName).getHouseCount();
        int amountOfHotels = getPlayerProperty(receiver.getProperties(), propertyName).getHotelCount();
        int debtValue;
        if (amountOfHotels > 0) {
            debtValue = getStreet(propertyName).getRentWithHotel();
        } else {
             debtValue = getDebtValue(amountOfHouses, propertyName);
        }
        debtor.giveMoney(debtValue);
        receiver.receiveMoney(debtValue);
    }

    public int getDebtValue(int amountOfHouses, String propertyName) {
        int debtValue;
        switch (amountOfHouses) {
            case 1:
                debtValue = getStreet(propertyName).getRentWithOneHouse();
                break;
            case 2:
                debtValue = getStreet(propertyName).getRentWithTwoHouses();
                break;
            case 3:
                debtValue = getStreet(propertyName).getRentWithThreeHouses();
                break;
            case 4:
                debtValue = getStreet(propertyName).getRentWithFourHouses();
                break;
            default:
                debtValue = getStreet(propertyName).getRent();
                break;
        }
        return debtValue;
    }

    public void takeMortgage(String playerName, String propertyName) {
        Player player = getPlayer(playerName);
        Property property = getProperty(propertyName);
        PlayerProperty playerProperty = getPlayerProperty(player.getProperties(), propertyName);
        if (!playerProperty.isMortgage()) {
            if (playerProperty.getHotelCount() == 0 && playerProperty.getHouseCount() == 0) {
                int amount = property.getMortgage();
                player.receiveMoney(amount);
                playerProperty.setMortgage(true);
            } else {
                throw new MonopolyResourceNotFoundException("You first have to sell all the houses end hotels.");
            }
        } else {
            throw new MonopolyResourceNotFoundException("Already mortgaged.");
        }
    }

    public void settleMortgage(String playerName, String propertyName) {
            Player player = getPlayer(playerName);
            Property property = getProperty(propertyName);
            PlayerProperty playerProperty = getPlayerProperty(player.getProperties(), propertyName);
        if (playerProperty.isMortgage()) {
            int amount = property.getMortgage();
            player.spendMoney((int) Math.round(amount * 0.10));
            playerProperty.setMortgage(false);
        } else {
            throw new MonopolyResourceNotFoundException("Not mortgaged.");
        }
    }

    public Tile getTile(String name) {
        for (Tile tile : tiles) {
            if (Objects.equals(tile.getName(), name)) {
                return tile;
            }
        }
        throw new MonopolyResourceNotFoundException("Did not find the requested tile: " + name);
    }

    public Property getProperty(String name) {
        for (Tile tile : tiles) {
            if (Objects.equals(tile.getName(), name)) {
                return (Property) tile;
            }
        }
        throw new MonopolyResourceNotFoundException("Did not find the requested property: " + name);
    }

    public Street getStreet(String name) {
        for (Tile tile : tiles) {
            if (Objects.equals(tile.getName(), name)) {
                return (Street) tile;
            }
        }
        throw new MonopolyResourceNotFoundException("Did not find the requested street: " + name);
    }

    private boolean isProperty(Tile nextTile) {
        try {
            Property property = (Property) nextTile;
            int cost = property.getCost();
            return cost >= 0;
        } catch (Exception ex) {
            return false;
        }
    }

    private Player getNextPlayer() {
        boolean currentFlag = false;
        for (Player player : players) {
            if (currentFlag) return player;
            if (Objects.equals(player.getName(), currentPlayer.getName())) {
                currentFlag = true;
            }
        }
        return players.get(0);
    }

    private Tile getNextTile(String currentTile, int total) {
        for (Tile tile : tiles) {
            if (tile.getName().equals(currentTile)) {
                int nextPosition = tile.getPosition() + total;
                if (nextPosition > tiles.size()) nextPosition = 0;
                return tiles.get(nextPosition);
            }
        }
        throw new MonopolyResourceNotFoundException("Can't find next tile.");
    }

    public Player getPlayer(String playerName) {
        for (Player player : players) {
            if (player.getName().equals(playerName)) {
                return player;
            }
        }
        throw new MonopolyResourceNotFoundException("Did not found the requested player.");
    }

    public PlayerProperty getPlayerProperty(List<PlayerProperty> playerProperties, String propertyName) {
        for (PlayerProperty playerProperty : playerProperties) {
            if (playerProperty.getName().equals(propertyName)) {
                return playerProperty;
            }
        }
        throw new MonopolyResourceNotFoundException("Did not found the requested playerProperty.");
    }

    public void setCanRoll(boolean canRoll) {
        this.canRoll = canRoll;
    }

    public void addTurn(Turn turn) {
        turns.add(turn);
    }

    public List<Tile> getTiles() {
        return tiles;
    }

    public int getNumberOfPlayers() {
        return numberOfPlayers;
    }

    public String getPrefix() {
        return prefix;
    }

    public boolean isStarted() {
        return started;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public String getId() { return id; }

    public static int getGames() {
        return games;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public boolean isCanRoll() {
        return canRoll;
    }

    public boolean isEnded() {
        return ended;
    }

    public Player getWinner() {
        return winner;
    }

    public int getAvailableHouses() {
        return availableHouses;
    }

    public int getAvailableHotels() {
        return availableHotels;
    }

    public List<Turn> getTurns() {
        return turns;
    }

    public void getOutOfJailFine() {
        currentPlayer.getOutOfJailFine();
    }

    public void getOutOfJailFree() {
        currentPlayer.getOutOfJailFree();
    }
}
