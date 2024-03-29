package be.howest.ti.monopoly.logic.implementation;

import be.howest.ti.monopoly.logic.implementation.enums.TaxSystems;
import be.howest.ti.monopoly.logic.implementation.tiles.Property;
import be.howest.ti.monopoly.logic.exceptions.MonopolyResourceNotFoundException;

import java.util.*;

public class Player {

    private final String name;
    private String currentTile;
    private boolean jailed;
    private int money;
    private boolean bankrupt;
    private int outOfJailFreeCards;
    private int triesToGetOutOfJail;
    private TaxSystems taxSystem;
    private final List<PlayerProperty> properties;

    public Player(String name) {
        this.name = name;
        this.currentTile = "Boot";
        this.jailed = false;
        this.money = 1500;
        this.bankrupt = false;
        this.outOfJailFreeCards = 0;
        this.triesToGetOutOfJail = 0;
        this.properties = new ArrayList<>();
        this.taxSystem = TaxSystems.ESTIMATE;
    }

    public void moveTile(String tile) {
        currentTile = tile;
    }

    public void buyProperty(Property property) {
        spendMoney(property.getCost());
        properties.add(new PlayerProperty(property.getName()));
    }

    public void spendMoney(int amount) {
        if (money >= amount) {
            money -= amount;
        } else {
            throw new MonopolyResourceNotFoundException("Not enough money");
        }
    }

    public void giveMoney(int amount) {
        if (money >= amount) {
            money -= amount;
        } else {
            goBankrupt();
        }
    }

    public void goBankrupt() {
        bankrupt = true;
    }

    public void receiveMoney(int amount) {
        money += amount;
    }

    public void addProperty(String property) {
        properties.add(new PlayerProperty(property));
    }

    public void receiveProperty(PlayerProperty playerProperty) {
        properties.add(playerProperty);
    }

    public void deleteProperties() {
        properties.clear();
    }

    public void goToJail() {
        jailed = true;
        moveTile("Repair");
    }

    public void getOutOfJailFine() {
        if (triesToGetOutOfJail == 3) {
            giveMoney(50);
        } else {
            spendMoney(50);
        }
        jailed = false;
        triesToGetOutOfJail = 0;
    }

    public void getOutOfJailFree() {
        if (outOfJailFreeCards > 0) {
            jailed = false;
            outOfJailFreeCards -= 1;
            triesToGetOutOfJail = 0;
        } else {
            throw new MonopolyResourceNotFoundException("You don't have any out-of-repair-free cards");
        }
    }

    public void addOutOfJailFreeCard() {
        outOfJailFreeCards += 1;
    }

    public void getOutOfJailDouble() {
        jailed = false;
        triesToGetOutOfJail = 0;
    }

    public void addTrieToGetOutOfJail() {
        triesToGetOutOfJail += 1;
    }

    public void deleteOutOfJailFreeCards() {
        outOfJailFreeCards = 0;
    }

    public void setTaxSystem(TaxSystems taxSystem) {
        this.taxSystem = taxSystem;
    }

    public String getName() {
        return name;
    }

    public String getCurrentTile() {
        return currentTile;
    }

    public boolean isJailed() {
        return jailed;
    }

    public int getMoney() {
        return money;
    }

    public boolean isBankrupt() {
        return bankrupt;
    }

    public int getGetOutOfJailFreeCards() {
        return outOfJailFreeCards;
    }

    public List<PlayerProperty> getProperties() {
        return properties;
    }

    public int getTriesToGetOutOfJail() {
        return triesToGetOutOfJail;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Player player = (Player) o;
        return Objects.equals(name, player.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    public TaxSystems getTaxSystem() {
        return taxSystem;
    }
}
