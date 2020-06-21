package com.kevalpatel2106.fxratesample

import android.app.Application
import android.app.Instrumentation
import android.content.Context
import androidx.test.runner.AndroidJUnitRunner

class UiTestsRunner : AndroidJUnitRunner() {

    @Throws(
        InstantiationException::class,
        IllegalAccessException::class,
        ClassNotFoundException::class
    )
    override fun newApplication(cl: ClassLoader, className: String, context: Context): Application {

        // Use the test application class that can inject mock dependencies using dagger.
        return Instrumentation.newApplication(AndroidTestApplication::class.java, context)
    }
}
