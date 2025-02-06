package com.duckyshine.app.sound;

import org.lwjgl.openal.*;

import com.duckyshine.app.debug.Debug;
import com.duckyshine.app.math.RandomNumber;
import com.duckyshine.app.utilities.ResourceFinder;

import java.util.List;
import java.util.Set;
import java.util.ArrayList;
import java.util.HashSet;

import static org.lwjgl.openal.ALC11.*;

public class SoundPlayer {
    private long audioDevice;
    private long audioContext;

    private String deviceName;

    private Sound music;

    private Set<String> cache;

    private List<String> playlist;

    public SoundPlayer() {
        this.music = null;

        this.cache = new HashSet<>();

        this.playlist = new ArrayList<>();

        this.initialise();
    }

    private void initialise() {
        int[] attributes = { 0 };

        this.deviceName = alcGetString(0, ALC_DEFAULT_DEVICE_SPECIFIER);
        this.audioDevice = alcOpenDevice(this.deviceName);
        this.audioContext = alcCreateContext(this.audioDevice, attributes);

        alcMakeContextCurrent(this.audioContext);

        ALCCapabilities alcCapabilities = ALC.createCapabilities(this.audioDevice);
        ALCapabilities alCapabilities = AL.createCapabilities(alcCapabilities);

        if (!alCapabilities.OpenAL10) {
            assert false : "Audio library is not supported";
        }

        this.initialisePlaylist();
    }

    private void initialisePlaylist() {
        List<String> files = ResourceFinder.getFiles("sound/music/");

        if (files != null) {
            this.playlist.addAll(files);
        }
    }

    public void playMusic() {
        if (this.music == null || !this.music.isPlaying()) {
            this.music = this.getRandomMusic();
            this.music.play();
        }
    }

    private Sound getRandomMusic() {
        int index = RandomNumber.getRandomInteger(this.playlist.size());

        String filepath = this.playlist.get(index);
        String cacheFilepath = ResourceFinder.getResourcePath(".cache");

        // if (this.cache.contains(filepath)) {
        // return this.getSoundFromCache(filepath);
        // }

        // this.cache.add(filepath);

        Sound music = new Sound(filepath);

        return music;
    }

    private Sound getSoundFromCache(String filepath) {
        return null;
    }

    public void cleanUp() {
        alcDestroyContext(this.audioContext);
        alcCloseDevice(this.audioDevice);
    }
}
