package com.ashish.qrscanner.views

import android.app.Application
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import android.speech.tts.Voice
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.ashish.qrscanner.MainActivity
import com.ashish.qrscanner.R
import com.ashish.qrscanner.databinding.FragmentJokeBinding
import com.ashish.qrscanner.viewmodel.NewsViewModel
import com.ashish.qrscanner.viewmodel.NewsViewModelFactory
import com.ashish.qrscanner.views.adapters.JokesAdapter
import java.util.*

//https://v2.jokeapi.dev/joke/Any?amount=10

class JokeFragment : Fragment(), TextToSpeech.OnInitListener {
    lateinit var  newsViewModel: NewsViewModel
    private lateinit var jokesAdapter: JokesAdapter
    private lateinit var binding: FragmentJokeBinding
    private lateinit var tts: TextToSpeech
    var content : String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }

    }
    override fun onResume() {
        super.onResume()
        (activity as MainActivity).supportActionBar?.title = getString(R.string.jokes_fragment_title)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentJokeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
       initView()
    }

    private fun initView() {
        //text to speech
        tts = TextToSpeech(context, this@JokeFragment)

        newsViewModel = ViewModelProvider(this, NewsViewModelFactory(application = Application()))[NewsViewModel::class.java]
        newsViewModel.fetchJokes("")
        jokesAdapter = JokesAdapter(requireContext(),object :JokesAdapter.JokesPlayButtonListener{
            override fun onJokesPlay(string: String) {
               content = string
            }

        })
        setRecyclerView()
        setAdapter()

        binding.fab.setOnClickListener {
            newsViewModel.fetchJokes("")
            if (tts.isSpeaking) {
                binding.fabPlay.setImageResource(R.drawable.baseline_play)
                tts.stop()
            }
        }

        binding.fabPlay.setOnClickListener {
            if (tts.isSpeaking) {
                binding.fabPlay.setImageResource(R.drawable.baseline_play)
                tts.stop()
            } else{ playNews()
                binding.fabPlay.setImageResource(R.drawable.baseline_pause)
            }
        }
    }

    private fun setRecyclerView(){
        binding.jokesRecyclerView.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = jokesAdapter
        }
    }
    private fun setAdapter(){

        newsViewModel.jokesData?.observe(viewLifecycleOwner, Observer { data ->
            jokesAdapter.setList(data.jokes)
        })

    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            val result = tts.setLanguage(Locale.ENGLISH)
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Toast.makeText(context, "TTS Not Supported for this news", Toast.LENGTH_LONG)
                    .show()
            }

            tts.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
                override fun onStart(utteranceId: String) {
                    Log.i("TextToSpeech", "On Start")
                }

                override fun onDone(utteranceId: String) {
                    Log.i("TextToSpeech", "On Done")
                    binding.fabPlay.setImageResource(R.drawable.baseline_play)
                }

                override fun onError(utteranceId: String) {
                    Log.i("TextToSpeech", "On Error")
                }
            })
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