package com.ashish.qrscanner.views

import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.speech.tts.Voice
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.ashish.qrscanner.MainActivity
import com.ashish.qrscanner.R
import com.ashish.qrscanner.api.NewsModel
import com.ashish.qrscanner.databinding.FragmentReadNewsBinding
import com.ashish.qrscanner.utils.Cons
import java.io.BufferedReader
import java.io.ByteArrayInputStream
import java.io.IOException
import java.io.InputStreamReader
import java.util.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "URL"
private const val ARG_PARAM2 = "title"
private const val ARG_PARAM3 = "content"


class ReadNewsFragment() : Fragment(),TextToSpeech.OnInitListener{

    private var newsUrl:String? = null
    private var title:String? = "Breaking News"
    private lateinit var binding: FragmentReadNewsBinding
    private lateinit var adservers: StringBuilder
    private lateinit var tts: TextToSpeech
    var content : String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            newsUrl = it.getString(ARG_PARAM1)
            title = it.getString(ARG_PARAM2)
            content = it.getString(ARG_PARAM3)!!

        }
        (activity as MainActivity).supportActionBar?.title = title.apply {
            this!!.split(" ")[0] + this!!.split(" ")[1]
        }
        Log.i("NewsReadFragment ","onCreate "+title!!.split(" ")[0])

    }

    override fun onResume() {
        super.onResume()
        Log.i("NewsReadFragment ","onResume")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentReadNewsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initWebView(newsUrl)

    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ReadNewsFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String): ReadNewsFragment {
            return ReadNewsFragment().apply {

            }
        }
    }

    private fun initWebView(newsUrl: String?) {
        readAdServers()

        binding.webView.apply {
            settings.apply {
                domStorageEnabled = true
                loadsImagesAutomatically = true
                mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
                javaScriptEnabled = true
                setSupportZoom(true)
                builtInZoomControls = true
                displayZoomControls = false
                loadWithOverviewMode = true
                layoutAlgorithm = WebSettings.LayoutAlgorithm.NARROW_COLUMNS
                domStorageEnabled = true
                cacheMode = if (Cons.isNetworkAvailable(context)) WebSettings.LOAD_DEFAULT else WebSettings.LOAD_CACHE_ELSE_NETWORK
                allowFileAccess = true
                javaScriptEnabled = true
                javaScriptCanOpenWindowsAutomatically = false
                mediaPlaybackRequiresUserGesture = true
            }
            webChromeClient = WebChromeClient()
            scrollBarStyle = WebView.SCROLLBARS_OUTSIDE_OVERLAY
            isScrollbarFadingEnabled = true
           isLongClickable = true
           setLayerType(View.LAYER_TYPE_HARDWARE, null)
            webViewClient = MyWebViewClient()

            //text to speech
            tts = TextToSpeech(context, this@ReadNewsFragment)

            binding.webView.evaluateJavascript("(function() { return ('<html>'+document.getElementsByTagName('html')[0].innerText+'</html>'); })();")
            { html ->
                Toast.makeText(context, html, Toast.LENGTH_SHORT).show()
                // code here
                Log.d("TAG_","ReadNewsActivity1 "+html)
            }

            if(content.isNullOrEmpty()) binding.fab.hide()
            Log.d("TAG_","ReadNewsActivity "+content)
            binding.fab.setOnClickListener {
                if (tts.isSpeaking) {
                    binding.fab.setImageDrawable(resources.getDrawable(R.drawable.baseline_play))
                    tts.stop()
                } else {
                    binding.fab.setImageDrawable(resources.getDrawable(R.drawable.baseline_pause))
                    playNews()
                }
            }

        }



        if (newsUrl != null) {
            binding.webView.loadUrl(newsUrl)
        }

    }


    private fun readAdServers()
    {
        adservers = StringBuilder()

        var line: String? = ""
        val inputStream = this.resources.openRawResource(R.raw.adblockserverlist)
        val br = BufferedReader(InputStreamReader(inputStream))

        try
        {
            while (br.readLine().also { line = it } != null)
            {
                adservers.append(line)
                adservers.append("\n")
            }
        }
        catch (e: IOException)
        {
            e.printStackTrace()
        }
    }
    inner class MyWebViewClient : WebViewClient()
    {
        override fun shouldInterceptRequest(view: WebView, request: WebResourceRequest): WebResourceResponse?
        {
            val empty = ByteArrayInputStream("".toByteArray())
            val kk5 = adservers.toString()

            if (kk5.contains(":::::" + request.url.host))
                return WebResourceResponse("text/plain", "utf-8", empty)

            return super.shouldInterceptRequest(view, request)
        }
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            val result = tts.setLanguage(Locale.ENGLISH)
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Toast.makeText(context, "TTS Not Supported for this news", Toast.LENGTH_LONG)
                    .show()
            }
        }
    }

    private fun playNews() {
        tts.voice = addedVoices.elementAt(1)
        tts.speak(content, TextToSpeech.QUEUE_FLUSH, null, "")
    }

    // Adding voices
    private val voice1: Voice = Voice(
        "en-US-SMTf00",
        Locale("en", "USA"),
        300,
        300,
        false,
        setOf("NA", "f00", "202009152", "female", null)
    )
    private val voice2: Voice = Voice(
        "en-IN-SMTf00",
        Locale("en", "IND"),
        300,
        300,
        false,
        setOf("NA", "f00", "202007071", "male", null)
    )
    private val addedVoices: Set<Voice> = setOf(voice1, voice2)

    override fun onDestroy() {
        tts.stop()
        tts.shutdown()
        super.onDestroy()
    }
}