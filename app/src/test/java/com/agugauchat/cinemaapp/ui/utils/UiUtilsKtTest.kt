package com.agugauchat.cinemaapp.ui.utils

import org.junit.Assert.assertEquals
import org.junit.Test
import java.util.Calendar

internal class UtilsUiTest {

    @Test
    fun `fun toDayOfWeek should return correct day of the week`() {
        // Given
        val dateString = "10/09/2023" // Sunday

        // When
        val dayOfWeek = dateString.toDayOfWeek()

        // Then
        assertEquals(Calendar.SUNDAY, dayOfWeek)
    }

    @Test
    fun `fun toDayOfWeek should return null for invalid date format`() {
        // Given
        val invalidDateString = "invalid date"

        // When
        val dayOfWeek = invalidDateString.toDayOfWeek()

        // Then
        assertEquals(null, dayOfWeek)
    }

    @Test
    fun `fun allNotNull should return true when all elements are not null`() {
        // Given
        val element1 = "Movie 1"
        val element2 = 123
        val element3 = true

        // When
        val result = allNotNull(element1, element2, element3)

        // Then
        assertEquals(true, result)
    }

    @Test
    fun `fun allNotNull should return false when at least one element is null`() {
        // Given
        val element1 = "Movie 1"
        val element2 = null
        val element3 = true

        // When
        val result = allNotNull(element1, element2, element3)

        // Then
        assertEquals(false, result)
    }

    @Test
    fun `fun allNotNull should return false when all elements are null`() {
        // Given
        val element1 = null
        val element2 = null
        val element3 = null

        // When
        val result = allNotNull(element1, element2, element3)

        // Then
        assertEquals(false, result)
    }
}