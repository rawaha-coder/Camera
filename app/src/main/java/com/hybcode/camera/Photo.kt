package com.hybcode.camera

import android.net.Uri
import android.os.Parcelable
import kotlinx.parcelize.Parcelize
@Parcelize
data class Photo(val id: Long, val uri: Uri) : Parcelable
