package com.kevalpatel2106.fxratesample.utils

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.experimental.runners.Enclosed
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.junit.runners.Parameterized
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import java.util.*

@RunWith(Enclosed::class)
class ExtensionTest {

    @RunWith(JUnit4::class)
    class DisposableKtTest {

        private lateinit var compositeDisposable: CompositeDisposable

        @Mock
        lateinit var disposable: Disposable

        @Before
        fun before() {
            compositeDisposable = CompositeDisposable()
            MockitoAnnotations.initMocks(this@DisposableKtTest)
        }

        @Test
        fun testAddToComposite() {
            Assert.assertEquals(0, compositeDisposable.size())
            disposable.addTo(compositeDisposable)
            Assert.assertEquals(1, compositeDisposable.size())
        }
    }


    @RunWith(Parameterized::class)
    class DoubleExtensionsKtTest(private val input: Double, private val output: String) {
        companion object {
            @JvmStatic
            @Parameterized.Parameters
            fun data(): ArrayList<Array<Any>> {
                return arrayListOf(
                    arrayOf(0.0, "0.00"),
                    arrayOf(10.00000, "10.00"),
                    arrayOf(21.005, "21.01"),
                    arrayOf(21.004, "21.00"),
                    arrayOf(12.12, "12.12")
                )
            }
        }

        @Test
        fun checkDayMinDate() {
            Assert.assertEquals(output, input.toStringUpToTwoDecimal())
        }
    }

    @RunWith(JUnit4::class)
    class NullSafeObserverExtensionTest {
        private val testString = "test"

        @Rule
        @JvmField
        val rule = InstantTaskExecutorRule()

        private val testLiveData = MutableLiveData<String>()

        private var mockLifecycleOwner: LifecycleOwner = LifecycleOwner {
            object : Lifecycle() {
                override fun addObserver(observer: LifecycleObserver) = Unit
                override fun removeObserver(observer: LifecycleObserver) = Unit
                override fun getCurrentState(): State = State.CREATED
            }
        }

        @Before
        fun setUp() {
            MockitoAnnotations.initMocks(this@NullSafeObserverExtensionTest)
        }

        @Test
        fun checkNullSafeObserver() {
            testLiveData.nullSafeObserve(mockLifecycleOwner) {
                Assert.assertNotNull(it)
                Assert.assertEquals(testString, it)
            }

            testLiveData.value = testString
            testLiveData.value = null
        }
    }
}
