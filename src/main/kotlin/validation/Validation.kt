import arrow.core.Either
import arrow.core.NonEmptyList
import arrow.core.raise.either
import arrow.core.sequence
import arrow.core.traverseEither
import item.RawEmployee


// this is not accumulative
fun validate(res: List<RawEmployee>): Either<NonEmptyList<EmployeeValidationError>, List<Employee>> =
    res.map { Employee.create(it) }.let { it -> either { it.bindAll() } }
