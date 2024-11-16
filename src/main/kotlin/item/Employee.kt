import arrow.core.Either
import arrow.core.NonEmptyList
import arrow.core.raise.either
import arrow.core.raise.ensure
import arrow.core.raise.zipOrAccumulate
import item.RawEmployee

@ConsistentCopyVisibility
data class Employee private constructor(
    val employeeID: EmployeeID,
    val name: Name,
    val department: Department,
    val email: Email,
    val phone: Phone,
) {
    companion object {
        fun create(re: RawEmployee): Either<NonEmptyList<EmployeeValidationError>, Employee> =
            either {
                zipOrAccumulate(
                    { ensure(re.employeeID.toIntOrNull() !== null) { EmployeeValidationError.EmployeeIdMustBeNumber } },
                    { ensure(re.employeeID.isNotEmpty()) { EmployeeValidationError.EmptyEmployeeID } },
                    { ensure(re.name.isNotEmpty()) { EmployeeValidationError.EmptyName } },
                    { ensure(re.department.isNotEmpty()) { EmployeeValidationError.EmptyDepartment } },
                    { ensure(re.email.isNotEmpty()) { EmployeeValidationError.EmptyEmail } },
                    { ensure(Email.isValid(re.email)) { EmployeeValidationError.InvalidEmail } },
                    { ensure(re.phone.isNotEmpty()) { EmployeeValidationError.EmptyPhone } },
                    { ensure(Phone.isValid(re.phone)) { EmployeeValidationError.InvalidPhone } },
                ) { _, _, _, _, _, _, _, _ ->
                    Employee(
                        EmployeeID(re.employeeID.toInt()),
                        Name(re.name),
                        Department(re.department),
                        Email(re.email),
                        Phone(re.phone),
                    )
                }
            }
    }
}

data class EmployeeID(
    val value: Int,
)

data class Name(
    val value: String,
)

data class Department(
    val value: String,
)

data class Email(
    val value: String,
) {
    init {
        require(isValid(value)) { "Email must contain '@'." }
    }

    companion object {
        fun isValid(value: String): Boolean = value.contains("@")
    }
}

data class Phone(
    val value: String,
) {
    init {
        require(isValid(value)) { "Phone must be in the format XXX-XXXX-XXXX." }
    }

    companion object {
        fun isValid(value: String): Boolean = value.matches(Regex("\\d{3}-\\d{4}-\\d{4}"))
    }
}

sealed class EmployeeValidationError {
    data object EmptyEmployeeID : EmployeeValidationError()

    data object EmployeeIdMustBeNumber : EmployeeValidationError()

    data object EmptyName : EmployeeValidationError()

    data object EmptyDepartment : EmployeeValidationError()

    data object EmptyEmail : EmployeeValidationError()

    data object InvalidEmail : EmployeeValidationError()

    data object EmptyPhone : EmployeeValidationError()

    data object InvalidPhone : EmployeeValidationError()
}
