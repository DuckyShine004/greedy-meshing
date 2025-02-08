package com.duckyshine.app.sound;

import java.nio.IntBuffer;
import java.nio.ShortBuffer;

import static org.lwjgl.system.MemoryStack.*;

import static org.lwjgl.system.MemoryUtil.*;

import static org.lwjgl.stb.STBVorbis.*;

import static org.lwjgl.openal.AL11.*;

public class Sound {
    private final float GAIN = 0.3f;

    private int bufferId;
    private int sourceId;
    private int audioFormat;

    private boolean isPlaying;

    private String filepath;

    private IntBuffer channelsBuffer;
    private IntBuffer sampleRateBuffer;

    private ShortBuffer audioBuffer;

    public Sound(String filepath) {
        this.audioFormat = -1;

        this.isPlaying = false;

        this.filepath = filepath;

        this.initialise();
    }

    private void initialise() {
        this.allocateMemoryBuffers();

        this.audioBuffer = stb_vorbis_decode_filename(this.filepath, this.channelsBuffer, this.sampleRateBuffer);

        if (this.audioBuffer == null) {
            this.freeMemoryBuffers();

            return;
        }

        this.temp();
    }

    private void temp() {
        int channels = this.channelsBuffer.get();
        int sampleRate = this.sampleRateBuffer.get();

        this.freeMemoryBuffers();

        this.setAudioFormat(channels);

        this.setupAudioParameters(sampleRate);
    }

    private void setupAudioParameters(int sampleRate) {
        this.bufferId = alGenBuffers();

        alBufferData(this.bufferId, this.audioFormat, this.audioBuffer, sampleRate);

        this.sourceId = alGenSources();

        alSourcei(this.sourceId, AL_BUFFER, this.bufferId);
        alSourcei(this.sourceId, AL_POSITION, 0);
        alSourcef(this.sourceId, AL_GAIN, this.GAIN);

        memFree(this.audioBuffer);
    }

    private void setAudioFormat(int channels) {
        switch (channels) {
            case 1:
                this.audioFormat = AL_FORMAT_MONO16;
                break;
            case 2:
                this.audioFormat = AL_FORMAT_STEREO16;
                break;
            default:
                break;
        }
    }

    private void allocateMemoryBuffers() {
        stackPush();

        this.channelsBuffer = stackMallocInt(1);
        this.sampleRateBuffer = stackMallocInt(1);
    }

    private void freeMemoryBuffers() {
        stackPop();
    }

    public void delete() {
        alDeleteSources(this.sourceId);
        alDeleteBuffers(this.bufferId);
    }

    public void play() {
        int state = alGetSourcei(this.sourceId, AL_SOURCE_STATE);

        if (state == AL_STOPPED) {
            this.isPlaying = false;

            alSourcei(this.sourceId, AL_POSITION, 0);
        }

        if (!this.isPlaying) {
            alSourcePlay(this.sourceId);

            this.isPlaying = true;
        }
    }

    public void stop() {
        if (this.isPlaying) {
            alSourceStop(this.sourceId);

            this.isPlaying = false;
        }
    }

    public String getFilepath() {
        return this.filepath;
    }

    public boolean isPlaying() {
        int state = alGetSourcei(this.sourceId, AL_SOURCE_STATE);

        if (state == AL_STOPPED) {
            this.isPlaying = false;
        }

        return this.isPlaying;
    }
}
