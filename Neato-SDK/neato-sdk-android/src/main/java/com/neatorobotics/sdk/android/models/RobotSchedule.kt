/*
 * Copyright (c) 2019.
 * Neato Robotics Inc.
 */

package com.neatorobotics.sdk.android.models

import android.os.Parcelable
import com.neatorobotics.sdk.android.robotservices.scheduling.model.ScheduleEvent
import kotlinx.android.parcel.Parcelize

@Parcelize
class RobotSchedule(val enabled: Boolean, val events: ArrayList<ScheduleEvent>) : Parcelable