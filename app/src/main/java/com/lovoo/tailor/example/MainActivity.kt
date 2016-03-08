package com.lovoo.tailor.example

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.lovoo.tailor.ButtonEditText
import com.lovoo.tailor.CompoundDrawableOnTouchListener
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        delete_button_edit_text.onCompoundDrawableClickListener = object : ButtonEditText.OnCompoundDrawableClickListener {
            override fun onCompoundDrawableClicked(direction: CompoundDrawableOnTouchListener.Direction) {
                if (direction == CompoundDrawableOnTouchListener.Direction.DIRECTION_RIGHT) {
                    // the right compound drawable has been clicked, delete the text
                    delete_button_edit_text.setText("");
                }
            }
        }
    }
}
