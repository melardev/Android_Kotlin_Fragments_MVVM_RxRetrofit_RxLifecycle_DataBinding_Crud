package com.melardev.android.crud.extensions

import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment


fun AppCompatActivity.replaceFragmentInActivity(fragment: Fragment, @IdRes frameId: Int) {
    val fragManager = supportFragmentManager.beginTransaction()
    // fragManager.setCustomAnimations(R.anim.fade_in, R.anim.fade_out, R.anim.fade_in, R.anim.fade_out)
    fragManager.replace(frameId, fragment)
    fragManager.commit()
}

