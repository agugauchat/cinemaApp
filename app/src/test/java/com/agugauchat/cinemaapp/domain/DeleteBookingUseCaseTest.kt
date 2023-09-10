import com.agugauchat.cinemaapp.data.BookingRepository
import com.agugauchat.cinemaapp.domain.DeleteBookingUseCase
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.just
import io.mockk.runs
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class DeleteBookingUseCaseTest {

    @MockK
    private lateinit var bookingRepository: BookingRepository

    private lateinit var deleteBookingUseCase: DeleteBookingUseCase

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        deleteBookingUseCase = DeleteBookingUseCase(bookingRepository)
    }

    @Test
    fun `invoke should call removeBooking in repository with correct bookingId`() = runBlocking {
        // Given
        val bookingId = 123
        coEvery { bookingRepository.removeBooking(any()) } just runs

        // When
        deleteBookingUseCase(bookingId)

        // Then
        coVerify(exactly = 1) { bookingRepository.removeBooking(bookingId.toString()) }
    }
}