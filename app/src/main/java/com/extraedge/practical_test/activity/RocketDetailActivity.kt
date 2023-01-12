package com.extraedge.practical_test.activity

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import com.extraedge.practical_test.R
import com.extraedge.practical_test.adapter.ImageAdapter
import com.extraedge.practical_test.databinding.ActivityRocketDetailBinding
import com.extraedge.practical_test.room.RocketModel
import com.extraedge.practical_test.sealed.MainEvent
import com.extraedge.practical_test.viewmodel.RocketDetailViewModel
import com.google.android.material.tabs.TabLayoutMediator
import org.koin.androidx.viewmodel.ext.android.viewModel


class RocketDetailActivity : AppCompatActivity() {

    private val viewModel by viewModel<RocketDetailViewModel>()
    private lateinit var binding: ActivityRocketDetailBinding
    val adapter by lazy {
        ImageAdapter()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_rocket_detail)

        val id = intent.getStringExtra("ID")

        viewModel.setObserver()

        lifecycleScope.launchWhenStarted {
            viewModel.event.collect{
                when(it)
                {
                    MainEvent.ShowProgressBar->{
                        binding.progressBar.visibility = View.VISIBLE
                    }
                    is MainEvent.ResponseList<*>->{
                        val rocketDetail = it.data as RocketModel
                        setRocketDetail(rocketDetail)
                        binding.progressBar.visibility = View.GONE
                    }
                    is MainEvent.ShowToast<*>->{
                        binding.progressBar.visibility = View.GONE
                        Toast.makeText(this@RocketDetailActivity, it.message, Toast.LENGTH_LONG).show()
                        id?.let { it1 -> viewModel.getRocketDetailLocally(it1) }
                    }
                    else -> {}
                }
            }
        }

        id?.let {
            if(isNetworkAvailable())
            {
                viewModel.getRockerDetail(it)
            }
            else
            {
                viewModel.showToast(getString(R.string.internet_connection_not_available))
            }
        }

        setAllClickListener()
    }

    private fun setAllClickListener() {
        binding.wekepediaLink.setOnClickListener {
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(binding.wekepediaLink.text.toString()))
            startActivity(browserIntent)
        }
    }

    private fun setRocketDetail(rocketDetail: RocketModel)
    {
        rocketDetail.flicker_image?.imagelist?.let { adapter.imagelist.addAll(it) }
        binding.viewPager.adapter = adapter

        binding.rocketDetail = rocketDetail

        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
        }.attach()
    }


    private fun isNetworkAvailable(): Boolean {
        val connectivityManager=this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo=connectivityManager.activeNetworkInfo
        return  networkInfo!=null && networkInfo.isConnected
    }
}