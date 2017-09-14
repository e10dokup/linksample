package xyz.dokup.linksample

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.TextView
import android.widget.Toast

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val textView = findViewById<TextView>(R.id.sample)

        textView.text = "https://google.com はつよい".createSpannable { Toast.makeText(this, it, Toast.LENGTH_SHORT).show() }
        textView.movementMethod = MyMovementMethod.instance

        textView.setOnLongClickListener { _ ->
            Toast.makeText(this, "つらい", Toast.LENGTH_SHORT).show(); true
        }
    }
}
