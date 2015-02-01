package com.example.dmitry.handheld_dictionary

import android.content.Context
import android.widget.Toast

/**
 * @author Dmitry Nikitin [nikitin.da.90@gmail.com]
 */
class HelloKotlinOnAndroid {
    public fun yo(context: Context, text: String) {
        Toast.makeText(context, text, Toast.LENGTH_LONG).show();
    }
}