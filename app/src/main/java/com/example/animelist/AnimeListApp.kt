package com.example.animelist

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp  // ← КЛЮЧЕВАЯ АННОТАЦИЯ!
class AnimeListApp : Application()