package dev.nmullaney.tesladashboard

import android.icu.util.DateInterval
import android.util.Log
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow
import java.net.*
import kotlin.random.Random


@ExperimentalCoroutinesApi
class PandaService  {
    private val TAG = PandaService::class.java.simpleName

    private lateinit var speedFlow : Flow<Int>
    @ExperimentalCoroutinesApi
    private val carStateFlow = MutableStateFlow(CarState())
    private val carState : CarState = CarState()
    private val random = Random
    // For PIWIS-WLAN
    private val ipAddress = "192.168.2.4"
    // For CANServer
    //private val ipAddress = "192.168.4.1"
    private val port = 1338
    private var shutdown = false
    private val heartbeat = "ehllo"
    private var lastHeartbeatTimestamp = 0L
    private val heartBeatIntervalMs = 5_000
    private val signalHelper = CANSignalHelper()

    @ExperimentalCoroutinesApi
    fun carState() : Flow<CarState> {
        return carStateFlow
    }

    @ExperimentalCoroutinesApi
    suspend fun startRequests() {
        shutdown = false
        withContext(Dispatchers.IO) {
            try {
                val socket = DatagramSocket()
                socket.soTimeout = heartBeatIntervalMs
                socket.reuseAddress = true

                Log.d(TAG, "Sending heartbeat")
                sendHello(socket)

                while (!shutdown) {

                    if (System.currentTimeMillis() > (lastHeartbeatTimestamp + heartBeatIntervalMs)) {
                        Log.d(TAG, "Sending heartbeat")
                        sendHello(socket)
                    }

                    val buf = ByteArray(16)
                    val packet = DatagramPacket(buf, buf.size, serverAddress())
                    Log.d(TAG, "C: Waiting to receive...")

                    try {
                        socket.receive(packet)
                    } catch (socketTimeoutException : SocketTimeoutException) {
                        Log.w(TAG, "Socket timed out without receiving a packet")
                        continue
                    }

                    Log.d(TAG, "Packet from: " + packet.address + ":" + packet.port)

                    val pandaFrame = PandaFrame(buf)
                    Log.d(TAG, "FrameId = " + pandaFrame.frameIdHex.hexString)
                    Log.d(TAG, "BusId = " + pandaFrame.busId)
                    Log.d(TAG, "FrameLength = " + pandaFrame.frameLength())

                    if (pandaFrame.frameId == 6L && pandaFrame.busId == 15L) {
                        // It's an ack
                        sendFilter(socket)
                    }
                    else if (!signalHelper.getSignalsForFrame(pandaFrame.frameIdHex).isEmpty()) {
                        signalHelper.getSignalsForFrame(pandaFrame.frameIdHex).forEach { channel ->
                            val value = pandaFrame.getPayloadValue(
                                channel.startBit,
                                channel.bitLength
                            ) * channel.factor + channel.offset
                            Log.d(TAG, channel.name + " = " + value)
                            carState.updateValue(channel.name, value)
                            carStateFlow.value = CarState(HashMap(carState.carData))
                        }
                    }
                }
                socket.disconnect()
                socket.close()
            } catch (exception: Exception) {
                Log.e(TAG, "Exception while sending or receiving data", exception)
            }
        }
    }

    private fun handleFrame(frame: PandaFrame) {
        val signalList = signalHelper.getSignalsForFrame(frame.frameIdHex)
        signalList.forEach {

        }
    }

    fun shutdown() {
        shutdown = true
    }

    private fun sendHello(socket: DatagramSocket) {
        // prepare data to be sent
        val udpOutputData = heartbeat

        // prepare data to be sent
        val buf: ByteArray = udpOutputData.toByteArray()

        sendData(socket, buf)
        lastHeartbeatTimestamp = System.currentTimeMillis()
    }

    private fun sendFilter(socket: DatagramSocket) {
        sendData(socket, signalHelper.socketFilterToInclude())
        // Uncomment this to send all data
        //sendData(socket, byteArrayOf(0x0C))
    }

    private fun sendData(socket: DatagramSocket, buf: ByteArray) {
        // create a UDP packet with data and its destination ip & port
        val packet = DatagramPacket(buf, buf.size, serverAddress())
        Log.d(TAG, "C: Sending: '" + String(buf) + "'")

        // send the UDP packet

        socket.send(packet)
    }

    private fun serverAddress() : InetSocketAddress =
        InetSocketAddress(InetAddress.getByName(ipAddress), port)
}