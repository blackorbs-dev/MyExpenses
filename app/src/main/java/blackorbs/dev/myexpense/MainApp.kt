package blackorbs.dev.myexpense

import android.app.Application

class MainApp: Application() {
    val appModule by lazy {
        AppModule(this)
    }
}