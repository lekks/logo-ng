
package com.ldir.logo.music.micromod;

public class Channel {
	private static final int
		FP_SHIFT = 15,
		FP_ONE = 1 << FP_SHIFT,
		FP_MASK = FP_ONE - 1;

	private static final short[] keyToPeriod = { 1814,
	/*   C-0   C#0   D-0   D#0   E-0   F-0   F#0   G-0   G#0   A-0  A#0  B-0 */
		1712, 1616, 1524, 1440, 1356, 1280, 1208, 1140, 1076, 1016, 960, 907,
		 856,  808,  762,  720,  678,  640,  604,  570,  538,  508, 480, 453,
		 428,  404,  381,  360,  339,  320,  302,  285,  269,  254, 240, 226,
		 214,  202,  190,  180,  170,  160,  151,  143,  135,  127, 120, 113,
		 107,  101,   95,   90,   85,   80,   75,   71,   67,   63,  60,  56,
		  53,   50,   47,   45,   42,   40,   37,   35,   33,   31,  30,  28
	};

	private static final short[] fineTuning = {
		4096, 4067, 4037, 4008, 3979, 3951, 3922, 3894,
		4340, 4308, 4277, 4247, 4216, 4186, 4156, 4126
	};

	private static final short[] arpTuning = {
		4096, 4340, 4598, 4871, 5161, 5468, 5793, 6137,
		6502, 6889, 7298, 7732, 8192, 8679, 9195, 9742
	};

	private static final short[] sineTable = {
		   0,  24,  49,  74,  97, 120, 141, 161, 180, 197, 212, 224, 235, 244, 250, 253,
		 255, 253, 250, 244, 235, 224, 212, 197, 180, 161, 141, 120,  97,  74,  49,  24
	};

	private Module module;
	private int noteKey, noteEffect, noteParam;
	private int noteIns, instrument, assigned;
	private int sampleIdx, sampleFra, freq;
	private int volume, panning, fineTune, ampl;
	private int period, portaPeriod, portaSpeed, fxCount;
	private int vibratoType, vibratoPhase, vibratoSpeed, vibratoDepth;
	private int tremoloType, tremoloPhase, tremoloSpeed, tremoloDepth;
	private int tremoloAdd, vibratoAdd, arpeggioAdd;
	private int id, randomSeed;
	public int plRow;

	public Channel( Module module, int id ) {
		this.module = module;
		this.id = id;
		switch( id & 0x3 ) {
			case 0: case 3: panning =  51; break;
			case 1: case 2: panning = 204; break;
		}
		randomSeed = ( id + 1 ) * 0xABCDEF;
	}
	
	public void resample( int[] outBuf, int offset, int length, int sampleRate, boolean interpolate ) {
		if( ampl <= 0 ) return;
		int lAmpl = ampl * panning >> 8;
		int rAmpl = ampl * ( 255 - panning ) >> 8;
		int samIdx = sampleIdx;
		int samFra = sampleFra;
		int step = ( freq << ( FP_SHIFT - 3 ) ) / ( sampleRate >> 3 );
		Instrument ins = module.instruments[ instrument ];
		int loopLen = ins.loopLength;
		int loopEp1 = ins.loopStart + loopLen;
		byte[] sampleData = ins.sampleData;
		int outIdx = offset << 1;
		int outEp1 = offset + length << 1;
		if( interpolate ) {
			while( outIdx < outEp1 ) {
				if( samIdx >= loopEp1 ) {
					if( loopLen <= 1 ) break;
					while( samIdx >= loopEp1 ) samIdx -= loopLen;
				}
				int c = sampleData[ samIdx ];
				int m = sampleData[ samIdx + 1 ] - c;
				int y = ( m * samFra >> FP_SHIFT - 8 ) + ( c << 8 );
				outBuf[ outIdx++ ] += y * lAmpl >> FP_SHIFT;
				outBuf[ outIdx++ ] += y * rAmpl >> FP_SHIFT;
				samFra += step;
				samIdx += samFra >> FP_SHIFT;
				samFra &= FP_MASK;
			}
		} else {
			while( outIdx < outEp1 ) {
				if( samIdx >= loopEp1 ) {
					if( loopLen <= 1 ) break;
					while( samIdx >= loopEp1 ) samIdx -= loopLen;
				}
				int y = sampleData[ samIdx ];
				outBuf[ outIdx++ ] += y * lAmpl >> FP_SHIFT - 8;
				outBuf[ outIdx++ ] += y * rAmpl >> FP_SHIFT - 8;
				samFra += step;
				samIdx += samFra >> FP_SHIFT;
				samFra &= FP_MASK;
			}
		}
	}

	public void updateSampleIdx( int length, int sampleRate ) {
		int step = ( freq << ( FP_SHIFT - 3 ) ) / ( sampleRate >> 3 );
		sampleFra += step * length;
		sampleIdx += sampleFra >> FP_SHIFT;
		Instrument ins = module.instruments[ instrument ];
		int loopStart = ins.loopStart;
		int loopLength = ins.loopLength;
		int loopOffset = sampleIdx - loopStart;
		if( loopOffset > 0 ) {
			sampleIdx = loopStart;
			if( loopLength > 1 ) sampleIdx += loopOffset % loopLength;
		}
		sampleFra &= FP_MASK;
	}

	public void row( int key, int ins, int effect, int param ) {
		noteKey = key;
		noteIns = ins;
		noteEffect = effect;
		noteParam = param;
		vibratoAdd = tremoloAdd = arpeggioAdd = fxCount = 0;
		if( effect != 0x1D ) trigger();
		switch( effect ) {
			case 0x3: /* Tone Portamento.*/
				if( param > 0 ) portaSpeed = param;
				break;
			case 0x4: /* Vibrato.*/
				if( ( param & 0xF0 ) > 0 ) vibratoSpeed = param >> 4;
				if( ( param & 0x0F ) > 0 ) vibratoDepth = param & 0xF;
				vibrato();
				break;
			case 0x6: /* Vibrato + Volume Slide.*/
				vibrato();
				break;
			case 0x7: /* Tremolo.*/
				if( ( param & 0xF0 ) > 0 ) tremoloSpeed = param >> 4;
				if( ( param & 0x0F ) > 0 ) tremoloDepth = param & 0xF;
				tremolo();
				break;
			case 0x8: /* Set Panning. Not for Protracker. */
				if( module.c2Rate == Module.C2_NTSC ) panning = param;
				break;
			case 0x9: /* Set Sample Position.*/
				sampleIdx = param << 8;
				sampleFra = 0;
				break;
			case 0xC: /* Set Volume.*/
				volume = param > 64 ? 64 : param;
				break;
			case 0x11: /* Fine Portamento Up.*/
				period -= param;
				if( period < 0 ) period = 0;
				break;
			case 0x12: /* Fine Portamento Down.*/
				period += param;
				if( period > 65535 ) period = 65535;
				break;
			case 0x14: /* Set Vibrato Waveform.*/
				if( param < 8 ) vibratoType = param;
				break;
			case 0x17: /* Set Tremolo Waveform.*/
				if( param < 8 ) tremoloType = param;
				break;
			case 0x1A: /* Fine Volume Up.*/
				volume += param;
				if( volume > 64 ) volume = 64;
				break;
			case 0x1B: /* Fine Volume Down.*/
				volume -= param;
				if( volume < 0 ) volume = 0;
				break;
			case 0x1C: /* Note Cut.*/
				if( param <= 0 ) volume = 0;
				break;
			case 0x1D: /* Note Delay.*/
				if( param <= 0 ) trigger();
				break;
		}
		updateFrequency();
	}

	public void tick() {
		fxCount++;
		switch( noteEffect ) {
			case 0x1: /* Portamento Up.*/
				period -= noteParam;
				if( period < 0 ) period = 0;
				break;
			case 0x2: /* Portamento Down.*/
				period += noteParam;
				if( period > 65535 ) period = 65535;
				break;
			case 0x3: /* Tone Portamento.*/
				tonePortamento();
				break;
			case 0x4: /* Vibrato.*/
				vibratoPhase += vibratoSpeed;
				vibrato();
				break;
			case 0x5: /* Tone Porta + Volume Slide.*/
				tonePortamento();
				volumeSlide( noteParam );
				break;
			case 0x6: /* Vibrato + Volume Slide.*/
				vibratoPhase += vibratoSpeed;
				vibrato();
				volumeSlide( noteParam );
				break;
			case 0x7: /* Tremolo.*/
				tremoloPhase += tremoloSpeed;
				tremolo();
				break;
			case 0xA: /* Volume Slide.*/
				volumeSlide( noteParam );
				break;
			case 0xE: /* Arpeggio.*/
				if( fxCount > 2 ) fxCount = 0;
				if( fxCount == 0 ) arpeggioAdd = 0;
				if( fxCount == 1 ) arpeggioAdd = noteParam >> 4;
				if( fxCount == 2 ) arpeggioAdd = noteParam & 0xF;
				break;
			case 0x19: /* Retrig.*/
				if( fxCount >= noteParam ) {
					fxCount = 0;
					sampleIdx = sampleFra = 0;
				}
				break;
			case 0x1C: /* Note Cut.*/
				if( noteParam == fxCount ) volume = 0;
				break;
			case 0x1D: /* Note Delay.*/
				if( noteParam == fxCount ) trigger();
				break;
		}
		if( noteEffect > 0 ) updateFrequency();
	}

	private void updateFrequency() {
		int period = this.period + vibratoAdd;
		if( period < 7 ) period = 6848;
		freq = module.c2Rate * 428 / period;
		freq = ( freq * arpTuning[ arpeggioAdd ] >> 12 ) & 0x7FFFF;
		int volume = this.volume + tremoloAdd;
		if( volume > 64 ) volume = 64;
		if( volume < 0 ) volume = 0;
		ampl = ( volume * module.gain * FP_ONE ) >> 13;
	}

	private void trigger() {
		if( noteIns > 0 && noteIns <= module.numInstruments ) {
			assigned = noteIns;
			Instrument assignedIns = module.instruments[ assigned ];
			fineTune = assignedIns.fineTune;
			volume = assignedIns.volume >= 64 ? 64 : assignedIns.volume & 0x3F;
			if( assignedIns.loopLength > 0 && instrument > 0 ) instrument = assigned;
		}
		if( noteEffect == 0x15 ) fineTune = noteParam;
		if( noteKey > 0 && noteKey <= 72 ) {
			int per = ( keyToPeriod[ noteKey ] * fineTuning[ fineTune & 0xF ] ) >> 11;
			portaPeriod = ( per >> 1 ) + ( per & 1 );
			if( noteEffect != 0x3 && noteEffect != 0x5 ) {
				instrument = assigned;
				period = portaPeriod;
				sampleIdx = sampleFra = 0;
				if( vibratoType < 4 ) vibratoPhase = 0;
				if( tremoloType < 4 ) tremoloPhase = 0;
			}
		}
	}
	
	private void volumeSlide( int param ) {
		int vol = volume + ( param >> 4 ) - ( param & 0xF );
		if( vol > 64 ) vol = 64;
		if( vol < 0 ) vol = 0;
		volume = vol;
	}

	private void tonePortamento() {
		int source = period;
		int dest = portaPeriod;
		if( source < dest ) {
			source += portaSpeed;
			if( source > dest ) source = dest;
		} else if( source > dest ) {
			source -= portaSpeed;
			if( source < dest ) source = dest;
		}
		period = source;
	}

	private void vibrato() {
		vibratoAdd = waveform( vibratoPhase, vibratoType ) * vibratoDepth >> 7;
	}
	
	private void tremolo() {
		tremoloAdd = waveform( tremoloPhase, tremoloType ) * tremoloDepth >> 6;
	}

	private int waveform( int phase, int type ) {
		int amplitude = 0;
		switch( type & 0x3 ) {
			case 0: /* Sine. */
				amplitude = sineTable[ phase & 0x1F ];
				if( ( phase & 0x20 ) > 0 ) amplitude = -amplitude;
				break;
			case 1: /* Saw Down. */
				amplitude = 255 - ( ( ( phase + 0x20 ) & 0x3F ) << 3 );
				break;
			case 2: /* Square. */
				amplitude = ( phase & 0x20 ) > 0 ? 255 : -255;
				break;
			case 3: /* Random. */
				amplitude = ( randomSeed >> 20 ) - 255;
				randomSeed = ( randomSeed * 65 + 17 ) & 0x1FFFFFFF;
				break;
		}
		return amplitude;
	}
}
