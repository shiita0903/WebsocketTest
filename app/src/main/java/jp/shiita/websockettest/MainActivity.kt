package jp.shiita.websockettest

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.firebase.messaging.FirebaseMessaging
import com.tinder.scarlet.Scarlet
import com.tinder.scarlet.State
import com.tinder.scarlet.lifecycle.android.AndroidLifecycle
import com.tinder.scarlet.messageadapter.moshi.MoshiMessageAdapter
import com.tinder.scarlet.websocket.okhttp.newWebSocketFactory
import com.tinder.streamadapter.coroutines.CoroutinesStreamAdapterFactory
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import timber.log.Timber

@ExperimentalCoroutinesApi
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Timber.plant(Timber.DebugTree())
        FirebaseMessaging.getInstance().subscribeToTopic("all")
        testWebSocket()
    }

    private fun testWebSocket() {
        val okHttp = OkHttpClient.Builder().build()

        val scarlet = Scarlet.Builder()
            .webSocketFactory(okHttp.newWebSocketFactory("ws://192.168.2.2:8080/ws"))
            .lifecycle(AndroidLifecycle.ofApplicationForeground(application).combineWith())
            .addMessageAdapterFactory(MoshiMessageAdapter.Factory())
            .addStreamAdapterFactory(CoroutinesStreamAdapterFactory())
            .build()

        val service = scarlet.create(Service::class.java)

        service.sendText("before connected") // 送信できない
        lifecycleScope.launch {
            launch {
                service.observeEvent().consumeEach {
                    Timber.d("event = $it")
                }
            }
            launch {
                service.observeText().consumeEach {
                    Timber.d(it.toString())
                }
            }
            launch {
                service.observeState().consumeEach {
                    Timber.d("state = $it")
                    if (it is State.Connected) {
                        service.sendText("after connected")
                    }
                }
            }
        }
    }
}
