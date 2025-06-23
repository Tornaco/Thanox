package github.tornaco.android.thanos.core.os

import android.annotation.SuppressLint
import android.os.Parcel
import android.os.Parcelable
import android.os.RemoteException
import android.os.SystemClock
import android.util.Log
import github.tornaco.android.thanos.core.Logger
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.withContext
import java.time.Duration
import java.util.Objects
import java.util.concurrent.TimeoutException
import java.util.concurrent.atomic.AtomicReference

class SynchronousResultReceiver : Parcelable {
    private var mLocal = false
    private var mIsCompleted = false

    private var mFuture: AtomicReference<Result?> = AtomicReference(null)
    private var mReceiver: ISynchronousResultReceiver? = null

    companion object {
        private val logger = Logger("SynchronousResultReceiver")

        fun get(): SynchronousResultReceiver {
            return SynchronousResultReceiver()
        }

        @JvmField
        val CREATOR: Parcelable.Creator<SynchronousResultReceiver?> =
            object : Parcelable.Creator<SynchronousResultReceiver?> {
                override fun createFromParcel(`in`: Parcel): SynchronousResultReceiver {
                    return SynchronousResultReceiver(`in`)
                }

                override fun newArray(size: Int): Array<SynchronousResultReceiver?> {
                    return arrayOfNulls(size)
                }
            }
    }

    private constructor() {
        mLocal = true
        mIsCompleted = false
    }

    private constructor(p: Parcel) {
        mLocal = false
        mIsCompleted = false
        mReceiver = ISynchronousResultReceiver.Stub.asInterface(p.readStrongBinder())
    }

    class Result : Parcelable {
        private val mObject: ByteArrayWrapper?
        private val mErrorMessage: String?

        constructor(exception: String?) {
            mObject = null
            mErrorMessage = exception
        }

        constructor(`object`: ByteArrayWrapper?) {
            mObject = `object`
            mErrorMessage = null
        }

        /**
         * Return the stored value
         * May throw a [RuntimeException] thrown from the client
         */
        fun getValue(defaultValue: ByteArrayWrapper?): ByteArrayWrapper? {
            if (mErrorMessage != null) {
                throw RuntimeException(mErrorMessage)
            }
            return mObject ?: defaultValue
        }

        override fun describeContents(): Int {
            return 0
        }

        override fun writeToParcel(out: Parcel, flags: Int) {
            out.writeParcelable(mObject, 0)
            out.writeString(mErrorMessage)
        }

        private constructor(`in`: Parcel) {
            mObject =
                `in`.readParcelable<Parcelable>(ByteArrayWrapper::class.java.classLoader) as ByteArrayWrapper?
            mErrorMessage = `in`.readString()
        }

        companion object {
            @JvmField
            val CREATOR: Parcelable.Creator<Result> = object : Parcelable.Creator<Result> {
                override fun createFromParcel(`in`: Parcel): Result {
                    return Result(`in`)
                }

                override fun newArray(size: Int): Array<Result?> {
                    return arrayOfNulls(size)
                }
            }
        }
    }


    private fun complete(result: Result) {
        check(!mIsCompleted) { "Receiver has already been completed" }
        mIsCompleted = true
        logger.d("complete: $result mLocal = $mLocal")
        if (mLocal) {
            mFuture.set(result)
        } else {
            val rr: ISynchronousResultReceiver?
            synchronized(this) { rr = mReceiver }
            if (rr != null) {
                try {
                    rr.send(result)
                } catch (e: RemoteException) {
                    logger.w("Failed to complete future")
                }
            }
        }
    }

    /**
     * Deliver a result to this receiver.
     *
     * @param resultData Additional data provided by you.
     */
    fun send(resultData: ByteArrayWrapper?) {
        complete(Result(resultData))
    }

    /**
     * Deliver an [Exception] to this receiver
     *
     * @param e exception to be sent
     */
    fun propagateException(e: RuntimeException) {
        Objects.requireNonNull(e, "RuntimeException cannot be null")
        logger.d("propagateException")
        complete(Result(Log.getStackTraceString(e)))
    }

    /**
     * Blocks waiting for the result from the remote client.
     *
     *
     * If it is interrupted before completion of the duration, wait again with remaining time until
     * the deadline.
     *
     * @param timeout The duration to wait before sending a [TimeoutException]
     * @return the Result
     * @throws TimeoutException if the timeout in milliseconds expired.
     */
    @SuppressLint("NewApi")
    @Throws(TimeoutException::class)
    suspend fun awaitResultNoInterrupt(timeout: Duration): Result {
        return withContext(Dispatchers.Default) {
            Objects.requireNonNull(timeout, "Null timeout is not allowed")
            val startWaitNanoTime = SystemClock.elapsedRealtimeNanos()
            var remainingTime = timeout
            while (!remainingTime.isNegative && isActive) {
                val result = mFuture.get()
                if (result != null) {
                    return@withContext result
                }
                delay(100)
                remainingTime = timeout.minus(
                    Duration.ofNanos(SystemClock.elapsedRealtimeNanos() - startWaitNanoTime)
                )
            }
            throw TimeoutException()
        }
    }

    @Throws(TimeoutException::class)
    suspend fun awaitResultNoInterruptCompat(timeoutMillis: Long): Result {
        return withContext(Dispatchers.Default) {
            require(timeoutMillis > 0) { "Timeout must be positive" }

            val startTime = SystemClock.elapsedRealtime()
            var elapsedTime: Long
            var remainingTime = timeoutMillis

            while (remainingTime > 0 && isActive) {
                val result = mFuture.get()
                if (result != null) {
                    return@withContext result
                }

                delay(100)

                elapsedTime = SystemClock.elapsedRealtime() - startTime
                remainingTime = timeoutMillis - elapsedTime
            }

            throw TimeoutException("Timeout after $timeoutMillis ms")
        }
    }

    private class MyResultReceiver(val receiver: SynchronousResultReceiver) :
        ISynchronousResultReceiver.Stub() {
        override fun send(result: Result) {
            logger.d("MyResultReceiver send: $result")
            receiver.mFuture.set(result)
        }
    }


    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(out: Parcel, flags: Int) {
        synchronized(this) {
            if (mReceiver == null) {
                mReceiver = MyResultReceiver(this)
            }
            out.writeStrongBinder(mReceiver!!.asBinder())
        }
    }
}