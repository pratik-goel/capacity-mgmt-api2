package com.homeretailgroup.microservices.capacitymanagement;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;
import java.util.ListIterator;

import javax.xml.bind.annotation.XmlRootElement;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * 
 * @author pratik.goel
 *
 */
@Document
public class Capacity {

    @Id String collectionPointId;
    private ArrayList<Availability> availabilities;

    public Capacity (String collectionPointId, ArrayList<Availability> availabilities) {
        this.collectionPointId = collectionPointId;
        this.setAvailabilities(availabilities);
    }

    public String getCollectionPointId(){
        return collectionPointId;
    }

    public ArrayList<Availability> getAvailabilities(){
        return availabilities;
    }

    public void setAvailabilities(ArrayList<Availability> availabilities) {

        availabilities.forEach(availability ->
            availability.setId(getUniqueIdForToken(availability.getDate().toString()))
        );

        this.availabilities = availabilities;
    }

    /**
     * Removes a specific card from a customer's set of stored cards
     * @param token tokenised card number to delete
     */
    /*public void removeCard(String token){

        ListIterator listIterator = this.getCards().listIterator();

        while ( listIterator.hasNext()) {
            Card currentCard = (Card) listIterator.next();
            if(currentCard.getToken().equals(token)) {
                listIterator.remove();
            }
        }

    }*/

    /**
     * Adds a new card to a customer, or updates the card details if it already exists
     * @param providedCard The supplied card details to update or insert
     */
    public void updateAvailability(Availability providedAvailability){

        ListIterator listIterator = this.getAvailabilities().listIterator();


        while ( listIterator.hasNext()) {
            Availability currentAvailability = (Availability) listIterator.next();
            if(currentAvailability.getDate().toString().equals(providedAvailability.getDate().toString())) {
            	currentAvailability.setCapacity(providedAvailability.getCapacity());
            	return;
            }
        }

        // If we get to this point the card hasn't been found, therefore add it
        providedAvailability.setId(this.getUniqueIdForToken(providedAvailability.getDate().toString()));
        this.availabilities.add(providedAvailability);

    }
    
    /**
     * Returns availability for a particular date
     * @param date Date for which availability is required.
     * @return Availability
     */
    public Availability getAvailability(Date date) {
    	ListIterator<Availability> listIterator = this.getAvailabilities().listIterator();
    	while (listIterator.hasNext()) {
    		Availability currentAvailability = listIterator.next();
    		if(currentAvailability.getDate().toString().equals(date.toString())) {
    			return currentAvailability;
    		}
    	}
    	//At this point, availability at the requested date is not found. Return NULL
    	return null;
    }

    /**
     * Generate a unique ID for a stored card using the customer's email and the tokenised card number
     * @param token tokenised card number
     * @return uniqueID
     */
    private String getUniqueIdForToken(String token) {

        String collectionIdAndToken = this.getCollectionPointId() + token;
        MessageDigest messageDigest = null;
        try {
            messageDigest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        byte[] hashBytes = messageDigest.digest(collectionIdAndToken.getBytes());
        StringBuffer hexString = new StringBuffer();

        for (int i = 0; i < hashBytes.length; i++){
            hexString.append(Integer.toHexString(0xFF & hashBytes[i]));
        }

        String generatedID = new String(hexString);

        return generatedID;
    }

}
