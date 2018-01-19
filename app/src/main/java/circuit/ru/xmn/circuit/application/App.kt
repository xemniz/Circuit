package circuit.ru.xmn.circuit.application

import android.app.Application
import android.content.Context

class App : Application() {

    companion object {
        lateinit var component: ApplicationComponent
        lateinit var CONTEXT: Context
    }

    override fun onCreate() {
        super.onCreate()
        initializeDagger()
    }

    fun initializeDagger() {
        component = DaggerApplicationComponent.builder()
                .applicationModule(ApplicationModule(this))
                .build()
        CONTEXT = this
    }
}