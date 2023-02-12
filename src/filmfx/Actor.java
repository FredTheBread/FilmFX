package filmfx;

import javafx.beans.property.SimpleStringProperty;

public class Actor {

    public SimpleStringProperty actor, character, ageOfActor, numberOfScenes, selectedGender;

    public String getActor() {
        return actor.get();
    }

    public void setActor(String actor) {
        this.actor = new SimpleStringProperty(actor);
    }
    
    public String getCharacter() {
        return character.get();
    }

    public void setCharacter(String character) {
        this.character = new SimpleStringProperty(character);
    }
    
    public String getSelectedGender() {
        return selectedGender.get();
    }

    public void setSelectedGender(String selectedGender) {
        this.selectedGender = new SimpleStringProperty(selectedGender);
    }

    public String getAgeOfActor() {
        return ageOfActor.get();
    }

    public void setAgeOfActor(String ageOfActor) {
        this.ageOfActor = new SimpleStringProperty(ageOfActor);
    }

    public String getNumberOfScenes() {
        return numberOfScenes.get();
    }

    public void setNumberOfScenes(String numberOfScenes) {
        this.numberOfScenes = new SimpleStringProperty(numberOfScenes);
    }
    
    public Actor(String actor, String character, String ageOfActor, String numberOfScenes, String selectedGender) {
        this.actor = new SimpleStringProperty(actor);
        this.character = new SimpleStringProperty(character);
        this.ageOfActor = new SimpleStringProperty(ageOfActor);
        this.numberOfScenes = new SimpleStringProperty(numberOfScenes);
        this.selectedGender = new SimpleStringProperty(selectedGender);
    }
    
    public String toString() {
        //return "Actor Name: " + getActor() + "\nCharacter Name: " + getCharacter() + "\nAge of Actor: " + getAgeOfActor() + "\nNumber of Scenes: " + getNumberOfScenes() + "\nActor's Gender: " + getSelectedGender() + "\n\n";
        return getActor() + ", " + getCharacter() + ", " + getAgeOfActor() + ", " + getNumberOfScenes() + ", " + getSelectedGender();
    }
}
