package mongo.syntax

import java.time._

trait JavaTimeSyntax {
  final implicit def localTimeOps(dateTime: LocalTime): LocalTimeOps =
    new LocalTimeOps(dateTime)

  implicit def localDateTimeOps(ldt: LocalDateTime): LocalDateTimeOps =
    new LocalDateTimeOps(ldt)

  final implicit def zonedDateTimeOps(dateTime: ZonedDateTime): ZonedDateTimeOps =
    new ZonedDateTimeOps(dateTime)

  final implicit def instantSyntax(dateTime: Instant): InstantOps =
    new InstantOps(dateTime)
}

final class LocalTimeOps(private val self: LocalTime) extends AnyVal {
  def noNanos: LocalTime =
    self.plusNanos(500).truncatedTo(java.time.temporal.ChronoUnit.MICROS)
}

final class LocalDateTimeOps(private val self: LocalDateTime) {
  def endOfDay: LocalDateTime = noNanos.withHour(23).withMinute(59).withSecond(59)
  def startOfDay: LocalDateTime = noNanos.withHour(0).withMinute(0).withSecond(0)
  def noNanos: LocalDateTime =
    self.plusNanos(500).truncatedTo(java.time.temporal.ChronoUnit.MICROS)
}

final class ZonedDateTimeOps(private val self: ZonedDateTime) extends AnyVal {
  def endOfDay: ZonedDateTime = noNanos.withHour(23).withMinute(59).withSecond(59)
  def startOfDay: ZonedDateTime = noNanos.withHour(0).withMinute(0).withSecond(0)
  def noNanos: ZonedDateTime =
    self.plusNanos(500).truncatedTo(java.time.temporal.ChronoUnit.MICROS)
}
final class InstantOps(private val self: Instant) extends AnyVal {
  def noNanos: Instant =
    self.plusNanos(500).truncatedTo(java.time.temporal.ChronoUnit.MICROS)
}
