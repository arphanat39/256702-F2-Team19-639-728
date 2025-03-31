package com.rabbithop;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * Manages game sound effects and music
 */
public class SoundManager {
    
    private Map<String, String> soundPaths;
    private boolean soundEnabled = true;
    
    public SoundManager() {
        soundPaths = new HashMap<>();
        initializeSoundPaths();
    }
    
    /**
     * Initialize sound file paths
     */
    private void initializeSoundPaths() {
        soundPaths.put("jump", "/sounds/jump.wav");
        soundPaths.put("coin_collect", "/sounds/coin_collect.wav");
        soundPaths.put("hurt", "/sounds/hurt.wav");
        soundPaths.put("game_over", "/sounds/game_over.wav");
        soundPaths.put("level_complete", "/sounds/level_complete.wav");
        soundPaths.put("bgm", "/sounds/background_music.wav");
    }
    
    /**
     * Play a sound by name
     * @param name Sound name
     */
    public void playSound(String name) {
        if (!soundEnabled) return;
        
        try {
            String path = soundPaths.get(name);
            if (path == null) {
                System.out.println("Sound not found: " + name);
                return;
            }
            
            URL resource = getClass().getResource(path);
            if (resource == null) {
                System.out.println("Sound resource not found: " + path);
                return;
            }
            
            Media sound = new Media(resource.toURI().toString());
            MediaPlayer mediaPlayer = new MediaPlayer(sound);
            
            mediaPlayer.setOnError(() -> {
                System.out.println("Media error: " + mediaPlayer.getError().getMessage());
            });
            
            mediaPlayer.play();
            
            // Automatically dispose the player when finished
            mediaPlayer.setOnEndOfMedia(() -> {
                mediaPlayer.dispose();
            });
            
        } catch (Exception e) {
            System.out.println("Error playing sound '" + name + "': " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Enable or disable sounds
     * @param enabled Whether sounds should be enabled
     */
    public void setSoundEnabled(boolean enabled) {
        this.soundEnabled = enabled;
    }
    
    /**
     * Check if sound is enabled
     * @return true if sound is enabled
     */
    public boolean isSoundEnabled() {
        return soundEnabled;
    }
    
    /**
     * Toggle sound on/off
     * @return New state of sound (true=enabled)
     */
    public boolean toggleSound() {
        soundEnabled = !soundEnabled;
        return soundEnabled;
    }

    private MediaPlayer bgmPlayer; // ตัวแปรเก็บ MediaPlayer สำหรับ BGM

public void playBackgroundMusic() {
    if (!soundEnabled) return;

    try {
        String path = soundPaths.get("bgm");
        if (path == null) {
            System.out.println("BGM not found");
            return;
        }

        URL resource = getClass().getResource(path);
        if (resource == null) {
            System.out.println("BGM resource not found: " + path);
            return;
        }

        Media bgm = new Media(resource.toURI().toString());
        bgmPlayer = new MediaPlayer(bgm);
        bgmPlayer.setCycleCount(MediaPlayer.INDEFINITE); // เล่นวนลูป
        bgmPlayer.play();
        
    } catch (Exception e) {
        System.out.println("Error playing BGM: " + e.getMessage());
        e.printStackTrace();
    }
}

// ฟังก์ชันหยุดเพลงพื้นหลัง
public void stopBackgroundMusic() {
    if (bgmPlayer != null) {
        bgmPlayer.stop();
    }
}

}  