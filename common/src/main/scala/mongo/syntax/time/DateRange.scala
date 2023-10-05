package mongo.syntax.time

import cats.{Eq, Show}
import cats.data.NonEmptyList
import eu.timepit.refined.types.numeric._
import io.circe.generic.JsonCodec

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

/** Represents a left-inclusive right-exclusive date range. */

@JsonCodec
final case class DateRange(start: LocalDate, end: LocalDate) extends Iterable[LocalDate] {
  def contains(localDate: LocalDate): Boolean =
    localDate.isEqual(start) || (localDate.isAfter(start) && localDate.isBefore(end))

  /** Checks whether this range overlaps with another range. */
  def overlapsWith(other: DateRange): Boolean =
    !( // format: off
      other.start.isAfter(end) ||
        other.start.isEqual(end) ||
        other.end.isBefore(start) ||
        other.end.isEqual(start)
      ) // format: on

  def isAfter(date: LocalDate): Boolean = start.isAfter(date)

  def includes(other: DateRange): Boolean =
    (start.isBefore(other.start) || start.isEqual(other.start)) && (end.isAfter(other.end) || end
      .isEqual(other.end))

  /** The total number of nights included in this period. */
  def length: Long = start.until(end, ChronoUnit.DAYS)
  def nights: PosLong = PosLong.unsafeFrom(length)

  def asInclusive: DateRange = DateRange.inclusive(start, end)

  def asNonInclusive: DateRange = DateRange(start, end.minusDays(1))

  def endInclusive: LocalDate = end.minusDays(1)

  def updated(newStart: Option[LocalDate] = None, newEnd: Option[LocalDate] = None): DateRange =
    copy(start = newStart.getOrElse(start), end = newEnd.getOrElse(end))

  def format: String = {
    val format: LocalDate => String = _.format(DateTimeFormatter.ofPattern("MM/dd/yyyy"))
    s"${format(start)} - ${format(end)}"
  }

  def nel: NonEmptyList[LocalDate] =
    NonEmptyList.fromListUnsafe(iterator.toList)

  override def iterator: Iterator[LocalDate] =
    LazyList.iterate(start, length.toInt)(_.plusDays(1)).iterator

  override def toString: String = s"DateRange([$start,$end))"

  private def invalidMessage: String =
    s"${DateRange.InvalidMessage} start=$start end=$end."
}

object DateRange {
  val InvalidMessage: String = "Invalid date range."

  @inline def exclusive(start: LocalDate, end: LocalDate): DateRange = DateRange(start, end)

  def inclusive(start: LocalDate, end: LocalDate): DateRange = DateRange(start, end.plusDays(1))

  def singleDate(date: LocalDate): DateRange = inclusive(date, date)

  implicit val show: Show[DateRange] = Show.show(d => s"[${d.start}, ${d.end})")

  implicit val eq: Eq[DateRange] =
    Eq.instance {
      case (x, y) =>
        x.start.isEqual(y.start) && x.end.isEqual(y.end)
    }
}
