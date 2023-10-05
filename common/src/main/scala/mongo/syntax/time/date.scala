package mongo.syntax.time

import java.time._
import java.time.chrono.ChronoLocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

object date extends DateSyntax

trait DateSyntax {
  val defaultTimeZone: ZoneId = ZoneId.of("Asia/Tashkent")
  val defaultLocale: Locale = Locale.US

  implicit def localDateAlgebraic(date: LocalDate): LocalDateAlgebraic =
    new LocalDateAlgebraic(date)

  implicit def toEpochSeconds(localDate: LocalDate): Long =
    localDate.atStartOfDay(defaultTimeZone).toEpochSecond

  final implicit def localDateSyntax(date: LocalDate): LocalDateSyntax = new LocalDateSyntax(date)
  final implicit def localDateFormatSyntax(date: LocalDate): LocalDateFormatSyntax =
    new LocalDateFormatSyntax(date)

  final implicit def localTimeSyntax(dateTime: LocalTime): LocalTimeSyntax =
    new LocalTimeSyntax(dateTime)

  final implicit def localDateTimeSyntax(dateTime: LocalDateTime): LocalDateTimeSyntax =
    new LocalDateTimeSyntax(dateTime)

  final implicit def zonedDateTimeSyntax(dateTime: ZonedDateTime): ZonedDateTimeSyntax =
    new ZonedDateTimeSyntax(dateTime)

  final implicit def instantSyntax(dateTime: Instant): InstantSyntax =
    new InstantSyntax(dateTime)

  final implicit def offsetDateTimeSyntax(dateTime: OffsetDateTime): OffsetDateTimeSyntax =
    new OffsetDateTimeSyntax(dateTime)

  final implicit def localDateTimeFormatSyntax(dateTime: LocalDateTime): LocalDateTimeFormatSyntax =
    new LocalDateTimeFormatSyntax(dateTime)
}

final class LocalDateSyntax(private val date: LocalDate) extends AnyVal {
  def is(dayOfWeek: DayOfWeek): Boolean = date.getDayOfWeek == dayOfWeek
  def isOneOf(days: Set[DayOfWeek]): Boolean = days.contains(date.getDayOfWeek)
  def isNot(days: Set[DayOfWeek]): Boolean = !days.contains(date.getDayOfWeek)
  def earliest(other: LocalDate): LocalDate = if (date.isBefore(other)) date else other
  def latest(other: LocalDate): LocalDate = if (date.isAfter(other)) date else other

  def isBetween(from: LocalDate, to: LocalDate): Boolean =
    date.compareTo(from) >= 0 && date.compareTo(to) <= 0

  def isBeforeOrEqual(that: ChronoLocalDate): Boolean =
    date.isBefore(that) || date.isEqual(that)

  def isAfterOrEqual(that: ChronoLocalDate): Boolean =
    date.isAfter(that) || date.isEqual(that)
}

final class LocalDateFormatSyntax(private val date: LocalDate) extends AnyVal {
  def formatMMdd: String = date.format(DateTimeFormatter.ofPattern("MM/dd"))
  def formatMMddyyyy: String = date.format(DateTimeFormatter.ofPattern("MM/dd/yyyy"))
  def formatyyyyMMdd: String = date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
}

final class LocalTimeSyntax(private val self: LocalTime) extends AnyVal {
  def noNanos: LocalTime =
    self.plusNanos(500).truncatedTo(java.time.temporal.ChronoUnit.MICROS)
}

final class LocalDateTimeSyntax(private val self: LocalDateTime) extends AnyVal {
  def earliest(other: LocalDateTime): LocalDateTime =
    if (self.isBefore(other)) self else other

  def latest(other: LocalDateTime): LocalDateTime =
    if (self.isAfter(other)) self else other

  def noNanos: LocalDateTime =
    self.plusNanos(500).truncatedTo(java.time.temporal.ChronoUnit.MICROS)

  def toEpochSecondUtc: Long =
    self.toEpochSecond(ZoneOffset.of("Z"))
}

final class ZonedDateTimeSyntax(private val self: ZonedDateTime) extends AnyVal {
  def noNanos: ZonedDateTime =
    self.plusNanos(500).truncatedTo(java.time.temporal.ChronoUnit.MICROS)
}

final class InstantSyntax(private val self: Instant) extends AnyVal {
  def noNanos: Instant =
    self.plusNanos(500).truncatedTo(java.time.temporal.ChronoUnit.MICROS)
}

final class OffsetDateTimeSyntax(private val self: OffsetDateTime) extends AnyVal {
  def noNanos: OffsetDateTime =
    self.plusNanos(500).truncatedTo(java.time.temporal.ChronoUnit.MICROS)
}

final class LocalDateTimeFormatSyntax(private val dateTime: LocalDateTime) extends AnyVal {
  def formatFull: String = dateTime.format(DateTimeFormatter.ofPattern("MM/dd/yyyy h:mm a"))
  def formatyyyyMMddHHmmss: String =
    dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
}

final class LocalDateAlgebraic(private val date: LocalDate) extends AnyVal {
  def +(days: Int): LocalDate = date.plusDays(days.toLong)
  def -(days: Int): LocalDate = date.minusDays(days.toLong)
}
