package com.gradle.game;

import com.gradle.game.entities.player.Player;
import com.gradle.game.entities.player.PlayerManager;
import de.gurkenlabs.litiengine.Game;
import de.gurkenlabs.litiengine.resources.Resources;
import de.gurkenlabs.litiengine.util.io.XmlUtilities;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlTransient;

import java.io.File;

@XmlRootElement(name = "savegame")
public class SaveGame {

    @XmlElement(name = "name")
    private String name;

    @XmlElement(name = "map")
    private String map;

    public SaveGame() {
    }

    public SaveGame(String saveName) {
        SaveGame saveGame = loadSavedGameFile(saveName);
        this.name = saveGame.getName();
        this.map = saveGame.getMap();
    }

    public SaveGame(String name, String map) {
        this.name = name;
        this.map = map;
    }

    public void saveGame(String name) {
        //Player player = PlayerManager.get(playerId);

        setName(name);
        try {
            setMap(GameManager.getRoomName());
        } catch (NullPointerException e) {
            setMap("mansion");
        }

        String dir = System.getProperty("user.home") + "/.testGame/savefiles/";
        File dirFile = new File(dir);
        dirFile.mkdirs();
        String savegamePath = dir + this.getName() + ".xml";
        XmlUtilities.save(this, savegamePath);
    }

    public static SaveGame loadSavedGameFile(String name) {
        String path = System.getProperty("user.home") + "/.testGame/savefiles/" + name + ".xml";
        SaveGame saveGame;
        try {
            saveGame = XmlUtilities.read(SaveGame.class, Resources.getLocation(path));
        } catch (jakarta.xml.bind.JAXBException e) {
            System.err.println("ERROR: failed to load save game. Instantiating new save. XML message: " + e.getMessage());
            saveGame = new SaveGame();
            saveGame.saveGame(name);
        }
        return saveGame;
    }

    @XmlTransient
    public String getName() {
        return this.name;
    }

    @XmlTransient
    public String getMap() {
        return this.map;
    }

    public void setName(String characterName) {
        this.name = characterName;
    }

    public void setMap(String map) {
        this.map = map;
    }
}
