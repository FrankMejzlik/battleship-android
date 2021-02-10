package com.example.battleship

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_main.*

class MainFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_main, container, true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        btn_main_play.setOnClickListener {
            // Open to SetPlayersActivity.
            val intent = Intent(activity, SetPlayersActivity::class.java)
            // Start activity.
            startActivity(intent)

        }

    }
}