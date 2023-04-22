package com.ashish.qrscanner.views

import android.app.Application
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.ashish.qrscanner.MainActivity
import com.ashish.qrscanner.R
import com.ashish.qrscanner.databinding.FragmentNewsBinding
import com.ashish.qrscanner.utils.Cons
import com.ashish.qrscanner.viewmodel.NewsViewModel
import com.ashish.qrscanner.viewmodel.NewsViewModelFactory
import com.ashish.qrscanner.views.adapters.NewsAdapter


// api key 868b41a610b14a2c96676d1b32569287

class NewsFragment : Fragment(), View.OnClickListener {

    private lateinit var binding: FragmentNewsBinding
    lateinit var  newsViewModel: NewsViewModel
    lateinit var newsAdapter: NewsAdapter
    private lateinit var oldView:ArrayList<View>
    private var tempView:View? =null
    private var newsCountry:String = "in"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
        Log.i("NewsFragment","onCreate")
    }

    override fun onResume() {
        super.onResume()
        (activity as MainActivity).supportActionBar?.title = getString(R.string.news_fragment_title)
        Log.i("NewsFragment","onResume")
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        menu.clear()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentNewsBinding.inflate(inflater, container, false)
        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            NewsFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initiateViews()
        setRecyclerView()
        setAdapter()
    }
    private fun initiateViews(){

        newsViewModel = ViewModelProvider(this,NewsViewModelFactory(application = Application()))[NewsViewModel::class.java]
        Log.i("MSG_ ","NewsFragment "+1)
        newsAdapter = NewsAdapter(requireContext(),object : NewsAdapter.onItemClickListener{
            override fun onItemClick(url: String,title: String,content:String) {
                val newsFragment = ReadNewsFragment()
                val args = Bundle()
                args.putString("URL",url)
                args.putString("title",title)
                args.putString("content",content)
                newsFragment.arguments = args
                requireActivity().supportFragmentManager.beginTransaction().replace(R.id.fragment_container_view, newsFragment,"tagName").addToBackStack(null).commit()
            }
        })

        oldView = ArrayList<View>()
        oldView.add(binding.generalNews)
        oldView.add(binding.businessNews)
        oldView.add(binding.entertainmentNews)
        oldView.add(binding.scienceNews)
        oldView.add(binding.techNews)
        oldView.add(binding.sportsNews)
        oldView.add(binding.healthNews)

        binding.generalNews.setOnClickListener(this)
        binding.businessNews.setOnClickListener(this)
        binding.entertainmentNews.setOnClickListener(this)
        binding.scienceNews.setOnClickListener(this)
        binding.techNews.setOnClickListener(this)
        binding.sportsNews.setOnClickListener(this)
        binding.healthNews.setOnClickListener(this)
        binding.globalNews.setOnClickListener(this)
    }
    private fun setRecyclerView(){
        binding.newsRecyclerView.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = newsAdapter
            Log.i("MSG_ ","NewsFragment "+2)

        }
    }
    private fun setAdapter(){
        if (Cons.isNetworkAvailable(requireContext())){
            newsViewModel.fetchNews("general",newsCountry)
        }
        newsViewModel.newsData?.observe(viewLifecycleOwner, Observer { data ->
            Log.i("MSG_ ","NewsFragment0 "+data)
            newsAdapter.setList(data.articles)
        })

    }

    override fun onClick(v: View?) {

        when(v){
            binding.globalNews -> {

               if (binding.globalNews.text.contentEquals("World",true)){
                   newsCountry = ""
                   binding.globalNews.text = "India"
               } else {
                   binding.globalNews.text = "World"
                   newsCountry ="in"
               }
                binding.globalNews.setBackgroundColor(resources.getColor(R.color.colorAccent))
                binding.globalNews.setTextColor(resources.getColor(R.color.white))
                newsViewModel.fetchNews("general",newsCountry)
                tempView = binding.generalNews
            }
            binding.generalNews -> {
                newsViewModel.fetchNews("general",newsCountry)
                tempView = binding.generalNews
            }
            binding.businessNews -> {
                newsViewModel.fetchNews("business",newsCountry)
                tempView = binding.businessNews
            }
            binding.entertainmentNews -> { newsViewModel.fetchNews("entertainment",newsCountry)
                tempView = binding.entertainmentNews
            }
            binding.scienceNews ->{
                newsViewModel.fetchNews("science",newsCountry)
                tempView = binding.scienceNews
            }
            binding.sportsNews -> {
                newsViewModel.fetchNews("sports",newsCountry)
                tempView = binding.sportsNews
            }
            binding.techNews -> {
                newsViewModel.fetchNews("technology",newsCountry)
                tempView = binding.techNews
            }
            binding.healthNews -> {
                newsViewModel.fetchNews("health",newsCountry)
                tempView = binding.healthNews
            }
        }
        applyNewsFilter()
    }
    private fun applyNewsFilter(){

        for (items in oldView) {
            Log.i("MSG_ ","NewsFragment5 "+items.id+" , "+tempView)
            val button:Button = items as Button
            if(items==tempView) {
                items.setBackgroundColor(resources.getColor(R.color.app_color))
                button.setTextColor(resources.getColor(R.color.white))

            }else {
                items.setBackgroundColor(resources.getColor(R.color.white))
                button.setTextColor(resources.getColor(R.color.black))
            }

        }

    }
}