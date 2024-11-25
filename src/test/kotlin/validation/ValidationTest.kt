package validation

import arrow.core.Either
import arrow.core.nonEmptyListOf
import item.RawEmployee
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import validate
import kotlin.test.Test
import kotlin.test.fail

class ValidationTest {

    @Test
    fun `should return employees`() {
        val rawEmployees = listOf(
            RawEmployee("1", "John Doe", "Engineering", "john.doe@example.com", "123-4567-8901"),
            RawEmployee("2", "Jane Smith", "Marketing", "jane.smith@example.com", "234-5678-9012")
        )

        val result = validate(rawEmployees)

        assertTrue(result is Either.Right && result.value.size == 2)
    }

    @Test
    fun `should return empty list`() {
        val rawEmployees = emptyList<RawEmployee>()

        val result = validate(rawEmployees)

        assertTrue(result is Either.Right && result.value.isEmpty())
    }

    @Test
    fun `should return errors`() {
        val rawEmployees = listOf(
            RawEmployee("1", "John Doe", "Engineering", "john.doe#example.com", "123-4567-8901"),
            RawEmployee("2", "Jane Smith", "Marketing", "jane.smith@example.com", "78-9012"),
            RawEmployee("", "", "", "", "")
        )

        val result = validate(rawEmployees)

        result.fold(
            ifLeft = { errors ->
                assertEquals(
                    nonEmptyListOf(
                        EmployeeValidationError.InvalidEmail,
//                        EmployeeValidationError.InvalidPhone, FIXME
                    ),
                    errors,
                )
            },
            ifRight = { _ -> fail() }
        )
    }
}