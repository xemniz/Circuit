package circuit.ru.xmn.circuit.application

import android.content.Context
import android.content.pm.PackageManager
import android.media.midi.MidiManager
import circuit.ru.xmn.circuit.midiservice.MidiInputPortProvider
import dagger.Component
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Singleton
@Component(modules = arrayOf(
        ApplicationModule::class,
        MidiServiceModule::class
))
interface ApplicationComponent {
}

@Module
class MidiServiceModule {
    fun provideInputMidiManager(context: Context): MidiManager? {
        val mMidiManager: MidiManager?
        if (context.packageManager.hasSystemFeature(PackageManager.FEATURE_MIDI)) {
            mMidiManager = context.getSystemService(Context.MIDI_SERVICE) as MidiManager?
            if (mMidiManager == null) {
                return null
            }
            return mMidiManager
        } else {
            return null
        }
    }

    @Provides
    fun provideInputPortProvider(midiManager: MidiManager?): MidiInputPortProvider? = midiManager?.let { MidiInputPortProvider(midiManager) }
}