import arrow.core.Either
import arrow.core.NonEmptyList
import arrow.core.raise.either
import arrow.core.toNonEmptyListOrNull
import item.RawEmployee

// this is not accumulative
fun validate(res: List<RawEmployee>): Either<NonEmptyList<EmployeeValidationError>, List<Employee>> =
    res.map { Employee.create(it) }.let { it -> either { it.bindAll() } }

fun validateAndAccumulate(res: List<RawEmployee>): Either<NonEmptyList<EmployeeValidationError>, List<Employee>> {
    val (errors, employees) = res.map { Employee.create(it) }.partitionEither()
    val nelErrors = errors.flatten().toNonEmptyListOrNull()
    return if (nelErrors != null) {
        Either.Left(nelErrors)
    } else {
        Either.Right(employees)
    }
}

// this is honest code
// fun <A, B> List<Either<A, B>>.partitionEither(): Pair<List<A>, List<B>> {
//    val lefts = mutableListOf<A>()
//    val rights = mutableListOf<B>()
//    for (either in this) {
//        when (either) {
//            is Either.Left -> lefts.add(either.value)
//            is Either.Right -> rights.add(either.value)
//        }
//    }
//    return lefts to rights
// }

// TODO return Pair<NonEmptyList<A>, List<B>>
fun <A, B> List<Either<A, B>>.partitionEither(): Pair<List<A>, List<B>> =
    this.fold(initial = emptyList<A>() to emptyList<B>()) { (lefts, rights), either ->
        when (either) {
            is Either.Left -> (lefts + either.value) to rights
            is Either.Right -> lefts to (rights + either.value)
        }
    }
