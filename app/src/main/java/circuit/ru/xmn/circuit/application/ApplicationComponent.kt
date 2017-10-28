package circuit.ru.xmn.circuit.application

import circuit.ru.xmn.circuit.screens.main.CircuitViewModel
import circuit.ru.xmn.circuit.screens.settings.MidiSettingsViewModel
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = arrayOf(
        ApplicationModule::class,
        MidiServiceModule::class
))
interface ApplicationComponent {
    fun inject(midiSettingsViewModel: MidiSettingsViewModel)
    fun inject(midiSettingsViewModel: CircuitViewModel)
}

