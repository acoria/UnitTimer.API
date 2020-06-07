package com.acoria.unittimer.api

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.activity_integration_test.*

class IntegrationTestActivity : AppCompatActivity() {

    private val viewModel = IntegrationTestViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_integration_test)

        viewModel.liveDataViewState.observe(this, Observer { render(it) })

        btn_back.setOnClickListener { viewModel.backOneUnit() }
        btn_timer_toggle.setOnClickListener { viewModel.toggleState() }
        btn_forward.setOnClickListener { viewModel.skipUnit() }
    }

    private fun render(viewState: IntegrationTestViewState) {
        txt_exercise_title.text = viewState.exerciseName
        txt_time.text = viewState.remainingTime
    }

}
