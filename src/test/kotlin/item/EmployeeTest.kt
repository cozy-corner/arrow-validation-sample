package item

import Employee
import EmployeeValidationError
import arrow.core.nonEmptyListOf
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class EmployeeTest {
    @Test
    fun `create employee with valid data`() {
        val re =
            RawEmployee(
                employeeID = "1",
                name = "John Doe",
                department = "IT",
                email = "example@example.com",
                phone = "123-4567-8901",
            )
        Employee.create(re).fold(
            { fail("Validation failed: $it") },
            { employee ->
                assertEquals(1, employee.employeeID.value)
                assertEquals("John Doe", employee.name.value)
                assertEquals("IT", employee.department.value)
                assertEquals("example@example.com", employee.email.value)
            },
        )
    }

    @Test
    fun `errors with invalid data`() {
        val re =
            RawEmployee(
                employeeID = "526572e7-1e2e-42a8-aeed-41e20711abb7",
                name = "",
                department = "",
                email = "",
                phone = "",
            )
        Employee.create(re).fold(
            {
                assertEquals(
                    it,
                    nonEmptyListOf(
                        EmployeeValidationError.EmployeeIdMustBeNumber,
                        EmployeeValidationError.EmptyName,
                        EmployeeValidationError.EmptyDepartment,
                        EmployeeValidationError.EmptyEmail,
                        EmployeeValidationError.InvalidEmail,
                        EmployeeValidationError.EmptyPhone,
                        EmployeeValidationError.InvalidPhone,
                    ),
                )
            },
            { _ -> fail("Validation should fail") },
        )
    }
}
