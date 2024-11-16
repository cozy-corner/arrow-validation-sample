package validation

import Employee
import EmployeeValidationError
import arrow.core.Either
import arrow.core.NonEmptyList
import item.RawEmployee

object Validation

fun validate(res: List<RawEmployee>): Either<NonEmptyList<EmployeeValidationError>, List<Employee>> = TODO()
