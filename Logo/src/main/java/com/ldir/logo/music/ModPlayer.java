package com.ldir.logo.music;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.util.Log;

import com.ldir.logo.music.micromod.Micromod;
import com.ldir.logo.music.micromod.Module;

import java.io.IOException;
import java.io.InputStream;

public class ModPlayer extends Thread {

    private final static int BIT_RATE = 44100;
	private boolean stopped = false;  // Тут мьютекс не принципиален
    private Module module;

	public ModPlayer(byte[] data) {
		super();
        module = new Module( data );
		android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_AUDIO);
		start();
	}

    public ModPlayer(InputStream file) throws IOException {
        super();
        int size = file.available();
        Log.i("Verbose", "size is " + size);
        byte[]  buffer = new byte[size];
        buffer = new byte[size];
        file.read(buffer);
        file.close();
        module = new Module( buffer );
        android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_AUDIO);
        start();
    }

	@Override
    public void run() {
		AudioTrack track = null;
        Micromod micromod = new Micromod( module, BIT_RATE );
        int duration = micromod.calculateSongDuration();

        Log.i("Mod", "Song name: " + module.songName);
        Log.i("Mod", "Song duration: " + duration / BIT_RATE);

		try {
			int minSize = AudioTrack.getMinBufferSize(BIT_RATE,
                    AudioFormat.CHANNEL_OUT_STEREO,
                    AudioFormat.ENCODING_PCM_16BIT);
            track = new AudioTrack(AudioManager.STREAM_MUSIC, BIT_RATE,
					AudioFormat.CHANNEL_OUT_STEREO,
					AudioFormat.ENCODING_PCM_16BIT, minSize,
					AudioTrack.MODE_STREAM);
            int[] mixBuf = new int[ micromod.getMixBufferLength() ];
            short[] outBuf = new short[ mixBuf.length * 2 ];
            track.play();
            while (  duration >0  && !stopped) {
                int samples = micromod.getAudio( mixBuf );
                int outIdx = 0;
                for( int mixIdx = 0, mixEnd = samples * 2; mixIdx < mixEnd; mixIdx++ ) {
                    int sam = mixBuf[ mixIdx ];
                    if( sam > 32767 ) sam = 32767;
                    if( sam < -32768 ) sam = -32768;
                    sam /=2; // Громкость
                    outBuf[ outIdx++ ] = ( short ) sam;
                }
                duration -= samples;
                int wpos = 0;
                while(wpos<outIdx && !stopped)
                    wpos+=track.write( outBuf, wpos, outIdx-wpos);
            }
		} catch (Throwable x) {
            x.printStackTrace();
		} finally {
			if (track != null) {
				track.stop();
				track.release();
			}
		}
	}

	public void close() {
		stopped = true;
		try {
			join(1000);
		} catch (InterruptedException e) {
            e.printStackTrace();
		}
	}
}
