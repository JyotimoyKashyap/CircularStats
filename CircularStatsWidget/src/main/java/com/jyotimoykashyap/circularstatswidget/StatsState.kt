package com.jyotimoykashyap.circularstatswidget


/**
 * An enum class for the state of the widget
 *
 * SCALED_UP -> This state represent the scaled up version of it
 * NORMAL -> Normal state is the state for the widget to be normal size
 *
 * This states are necessary for animating the component
 * from one state to another when we receive the desired condition
 * to be true
 */
enum class StatsState {
    SCALED_UP, NORMAL
}