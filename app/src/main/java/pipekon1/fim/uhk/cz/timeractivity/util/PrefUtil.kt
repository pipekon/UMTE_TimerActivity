package pipekon1.fim.uhk.cz.timeractivity.util

import android.content.Context
import pipekon1.fim.uhk.cz.timeractivity.MainActivity


class PrefUtil(context: Context) {



    companion object {


        fun getTimerLength(context: Context): Int {
            return 1
        }

        private val PREVIOUS_TIMER_LENGTH_SECONDS_ID = "pipekon1.fim.uhk.cz.timer.previous_timer_length"



        fun getPreviousTimerLengthSeconds(context: Context): Long {
            val preference = context.getSharedPreferences(PREVIOUS_TIMER_LENGTH_SECONDS_ID, Context.MODE_PRIVATE)
            return preference.getLong(PREVIOUS_TIMER_LENGTH_SECONDS_ID, 0)
        }


        fun setPreviousTimerLengthSeconds(seconds: Long, context: Context) {
            val preference = context.getSharedPreferences(PREVIOUS_TIMER_LENGTH_SECONDS_ID, Context.MODE_PRIVATE)
            val editor = preference.edit()
            editor.putLong(PREVIOUS_TIMER_LENGTH_SECONDS_ID, seconds)
            editor.apply()

        }

        private val TIMER_STATE_ID = "pipekon1.fim.uhk.cz.timer.timer_state"

        fun getTimerState(context: Context): MainActivity.TimerState {
            val preference = context.getSharedPreferences(PREVIOUS_TIMER_LENGTH_SECONDS_ID, Context.MODE_PRIVATE)
            val ordinal = preference.getInt(TIMER_STATE_ID, 0)
            return MainActivity.TimerState.values()[ordinal]

        }

        fun setTimerState(state: MainActivity.TimerState, context: Context) {
            val preference = context.getSharedPreferences(PREVIOUS_TIMER_LENGTH_SECONDS_ID, Context.MODE_PRIVATE)
            val editor = preference.edit()
            val ordinal = state.ordinal
            editor.putInt(TIMER_STATE_ID, ordinal)
            editor.apply()
        }

        private val SECONDS_REMAINING_ID = "pipekon1.fim.uhk.cz.timer.seconds_remaining"

        fun getSecondsRemaining(context: Context): Long {
            val preference = context.getSharedPreferences(PREVIOUS_TIMER_LENGTH_SECONDS_ID, Context.MODE_PRIVATE)
            return preference.getLong(SECONDS_REMAINING_ID, 0)
        }


        fun setSecondsRemaining(seconds: Long,context: Context) {
            val preference = context.getSharedPreferences(PREVIOUS_TIMER_LENGTH_SECONDS_ID, Context.MODE_PRIVATE)
            val editor = preference.edit()
            editor.putLong(SECONDS_REMAINING_ID, seconds)
            editor.apply()

        }

    }
}