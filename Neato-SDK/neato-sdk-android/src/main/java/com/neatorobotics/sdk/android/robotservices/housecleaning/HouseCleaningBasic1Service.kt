/*
 * Copyright (c) 2019.
 * Neato Robotics Inc.
 */

package com.neatorobotics.sdk.android.robotservices.housecleaning

class HouseCleaningBasic1Service : HouseCleaningService() {

    override val isMultipleZonesCleaningSupported: Boolean
        get() = false

    override val isEcoModeSupported: Boolean
        get() = true

    override val isTurboModeSupported: Boolean
        get() = true

    override val isExtraCareModeSupported: Boolean
        get() = false

    override val isCleaningAreaSupported: Boolean
        get() = false

    override val isCleaningFrequencySupported: Boolean
        get() = false

    companion object {

        private const val TAG = "HouseCleaningBasic1"
    }
}
