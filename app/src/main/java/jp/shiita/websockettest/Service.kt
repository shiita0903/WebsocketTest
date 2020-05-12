package jp.shiita.websockettest

import com.tinder.scarlet.State
import com.tinder.scarlet.WebSocket.Event
import com.tinder.scarlet.ws.Receive
import com.tinder.scarlet.ws.Send
import kotlinx.coroutines.channels.ReceiveChannel

interface Service {
    @Receive
    fun observeState(): ReceiveChannel<State>

    @Receive
    fun observeEvent(): ReceiveChannel<Event>

    @Receive
    fun observeText(): ReceiveChannel<TmpData>

    @Send
    fun sendText(message: String): Boolean
}