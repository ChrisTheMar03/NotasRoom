package com.christhemar.notasroom

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.christhemar.notasroom.fragments.MenuFragment
import com.christhemar.notasroom.fragments.SaveFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if(savedInstanceState==null){
            supportFragmentManager.beginTransaction()
                .setReorderingAllowed(true)
                .add(R.id.container,MenuFragment())
                .commit()
        }

    }
}