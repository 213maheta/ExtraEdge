package com.extraedge.practical_test.activity

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import com.extraedge.practical_test.R
import com.extraedge.practical_test.adapter.MainAdapter
import com.extraedge.practical_test.databinding.ActivityMainBinding
import com.extraedge.practical_test.iinterface.GenericClickListener
import com.extraedge.practical_test.model.RocketResponse
import com.extraedge.practical_test.room.RocketModel
import com.extraedge.practical_test.sealed.MainEvent
import com.extraedge.practical_test.sealed.NetworkResult
import com.extraedge.practical_test.viewmodel.MainViewModel
import org.koin.android.ext.android.get
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity(), GenericClickListener {

    private val viewModel by viewModel<MainViewModel>()
    private lateinit var binding: ActivityMainBinding
    private val adapter by lazy {
        MainAdapter(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        binding.rcvRocket.adapter = adapter
        lifecycleScope.launchWhenStarted {
            viewModel.event.collect{
                when(it)
                {
                    MainEvent.ShowProgressBar->{
                        binding.progressBar.visibility = View.VISIBLE
                    }
                    is MainEvent.ResponseList<*>->{
                        val list = it.data as List<RocketModel>
                        if(list.size>0)
                        {
                            adapter.rocketlist.clear()
                            adapter.rocketlist.addAll(list)
                            adapter.notifyDataSetChanged()
                        }
                        binding.progressBar.visibility = View.GONE
                    }
                    is MainEvent.ShowToast<*>->{
                        binding.progressBar.visibility = View.GONE
                        val message = it.message
                        Toast.makeText(this@MainActivity, message, Toast.LENGTH_LONG).show()
                        viewModel.getLocallyRocketList()
                    }
                    else -> {}
                }
            }
        }

        viewModel.setObserver()
    }

    override fun onResume() {
        super.onResume()
        if(isNetworkAvailable())
        {
            viewModel.getRocketList()
        }
        else
        {
            viewModel.showToast(getString(R.string.internet_connection_not_available))
        }
    }

    private fun isNetworkAvailable(): Boolean {
        val connectivityManager=this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo=connectivityManager.activeNetworkInfo
        return  networkInfo!=null && networkInfo.isConnected
    }

    override fun onClick(id: String) {
        val intent = Intent(this, RocketDetailActivity::class.java)
        intent.putExtra("ID", id)
        startActivity(intent)
    }
}