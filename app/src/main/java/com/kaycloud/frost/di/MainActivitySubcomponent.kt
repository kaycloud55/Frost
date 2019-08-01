package com.kaycloud.frost.di

import com.kaycloud.frost.MainActivity
import dagger.Subcomponent
import dagger.android.AndroidInjector

/**
 * Created by kaycloud on 2019-07-18
 */

interface MainActivitySubcomponent : AndroidInjector<MainActivity> {
    @Subcomponent.Builder
    abstract class Builder
}