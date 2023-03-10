package it.polimi.ingsw.model.character;

import it.polimi.ingsw.model.student.Student;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Represents the Character Card
 */
public class CharacterCard implements Serializable {
    private int costCharacter;
    private final CardEffect cardEffect;
    private boolean coinOnCard;
    private ArrayList<Student> studentsOnCard = new ArrayList<>();
    private final String description;
    private int xCardOnCard;

    /**
     * Default constructor
     * @param costCharacter number of coin necessary to activate a certain Character Card
     * @param cardEffect type of effect activated by the card
     * @param description of the effect of a certain Character Card
     */
    public CharacterCard(int costCharacter, CardEffect cardEffect, String description) {
        this.costCharacter = costCharacter;
        this.cardEffect = cardEffect;
        this.coinOnCard = false;
        this.description = description;

    }

    public int getCostCharacter() {
        return costCharacter;
    }

    public CardEffect getCardEffect() {
        return cardEffect;
    }

    public boolean getCoinOnCard() {    //o isCoinOnCard() ?
        return coinOnCard;
    }

    public void setCoinOnCard(boolean coinOnCard) {
        this.coinOnCard = coinOnCard;
    }

    public String getDescription() {
        return description;
    }

    public ArrayList<Student> getStudentsOnCard() {
        return studentsOnCard;
    }

    public int getXCardOnCard() {
        return xCardOnCard;
    }

    public void setXCardOnCard(int xCardOnCard) {
        this.xCardOnCard = xCardOnCard;
    }

}
