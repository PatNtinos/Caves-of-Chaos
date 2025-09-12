package game;

import javax.sound.sampled.*;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class AudioPlayer {
    private static Clip musicClip; // for looping background music
    private static final Map<String, Clip> soundCache = new HashMap<>(); // reuse sound effects

    // Play background music (loop)
    public static void playMusic(String path) {
        stopMusic();
        try {
            URL url = AudioPlayer.class.getResource(path);
            if (url == null) throw new IllegalArgumentException("Music file not found: " + path);

            AudioInputStream audioStream = AudioSystem.getAudioInputStream(url);
            musicClip = AudioSystem.getClip();
            musicClip.open(audioStream);
            musicClip.loop(Clip.LOOP_CONTINUOUSLY);
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    // Stop music
    public static void stopMusic() {
        if (musicClip != null && musicClip.isRunning()) {
            musicClip.stop();
            musicClip.close();
        }
    }

    // Play a one-shot sound effect
    public static void playSound(String path) {
        try {
            Clip clip;
            if (soundCache.containsKey(path)) {
                clip = soundCache.get(path);
            } else {
                URL url = AudioPlayer.class.getResource(path);
                if (url == null) throw new IllegalArgumentException("Sound file not found: " + path);

                AudioInputStream audioStream = AudioSystem.getAudioInputStream(url);
                clip = AudioSystem.getClip();
                clip.open(audioStream);
                soundCache.put(path, clip);
            }

            // rewind if already played
            clip.setFramePosition(0);
            clip.start();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }
}
