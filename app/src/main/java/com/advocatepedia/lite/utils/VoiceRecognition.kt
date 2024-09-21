package com.advocatepedia.lite.utils

import android.Manifest
import android.content.Context
import android.util.Log
import androidx.appcompat.widget.SearchView
import com.advocatepedia.lite.ui.MainViewState
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.single.PermissionListener
import net.gotev.speech.GoogleVoiceTypingDisabledException
import net.gotev.speech.Speech
import net.gotev.speech.SpeechDelegate
import net.gotev.speech.SpeechRecognitionNotAvailable
import net.gotev.speech.ui.SpeechProgressView

class VoiceRecognition constructor(
    context: Context,
    mainViewState: MainViewState,
    searchView: SearchView,
    speechProgressView: SpeechProgressView
) {
    var context: Context
    var viewState: MainViewState
    var searchView: SearchView
    private var speechProgressView: SpeechProgressView

    init {
        this.context = context
        viewState = mainViewState
        this.searchView = searchView
        this.speechProgressView = speechProgressView
    }

    fun listenMic() {
        try {
            // you must have android.permission.RECORD_AUDIO granted at this point
            Speech.getInstance().startListening(speechProgressView, object : SpeechDelegate {
                override fun onStartOfSpeech() {
                    viewState.micListenerState = true;
                    Log.i("speech", "speech recognition is now active")
                }

                override fun onSpeechRmsChanged(value: Float) {
                    Log.d("speech", "rms is now: $value")
                }

                override fun onSpeechPartialResults(results: List<String>) {
                    val str = StringBuilder()
                    for (res in results) {
                        str.append(res).append(" ")
                    }
                    Log.i("speech", "partial result: " + str.toString().trim { it <= ' ' })
                }

                override fun onSpeechResult(result: String) {
                    viewState.micListenerState = false;
                    if (result.isNotBlank()) {
                        searchView.setQuery(result, true)
                    }
                    Log.i("speech", "result: $result")
                }
            })
        } catch (exc: SpeechRecognitionNotAvailable) {
            Log.e("speech", "Speech recognition is not available on this device!")
        } catch (exc: GoogleVoiceTypingDisabledException) {
            Log.e("speech", "Google voice typing must be enabled!")
        }

    }

    fun takeMicPermission() {
        Dexter.withContext(context)
            .withPermission(Manifest.permission.RECORD_AUDIO)
            .withListener(object : PermissionListener {
                override fun onPermissionGranted(response: PermissionGrantedResponse?) { /* ... */
                    listenMic()
                }

                override fun onPermissionDenied(response: PermissionDeniedResponse?) { /* ... */
                }

                override fun onPermissionRationaleShouldBeShown(
                    p0: com.karumi.dexter.listener.PermissionRequest?,
                    token: PermissionToken?
                ) { /* ... */

                }
            }).check()
    }


}