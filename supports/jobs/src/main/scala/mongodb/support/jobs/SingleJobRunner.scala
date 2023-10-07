package mongodb.support.jobs

import cats.Applicative
import cats.effect.{Concurrent, Fiber, Temporal}
import cats.effect.syntax.all._
import cats.syntax.all._

import java.util.concurrent.TimeUnit
import scala.concurrent.duration._

/** Entity that knows how to run and control lifecycle of a single periodic job */
class SingleJobRunner[F[_]: Concurrent: Applicative, Env](
    implicit
    temporal: Temporal[F]
  ) {
  def run(
      job: SupervisedPeriodicJob[F, Env],
      env: Env,
      settings: JobRunSettings,
    ): F[Fiber[F, Throwable, Unit]] = {
    val initialDelay =
      if (settings.immediatelyOnBootstrap) 0.seconds
      else getJobExecutionDelay(job)
    (for {
      _ <- temporal.sleep(initialDelay)
      _ <- loop(job, env, settings.mode)
    } yield ()).start
  }

  private def loop(
      supervisedJob: SupervisedPeriodicJob[F, Env],
      env: Env,
      mode: JobRunMode,
    ): F[Unit] =
    for {
      _ <- supervisedJob.job.logger.info("Running the job")
      jobOutcome <- supervisedJob.job.run(env).attempt
      _ <- jobOutcome match {
        case Left(err) => supervisedJob.job.logger.error(err)(s"Job execution error")
        case Right(_) => supervisedJob.job.logger.info("Job run has finished")
      }
      _ <- mode match {
        case JobRunMode.Once =>
          /* Stop the loop */
          Applicative[F].unit
        case JobRunMode.Forever =>
          val delay = getJobExecutionDelay(supervisedJob)
          for {
            _ <- supervisedJob.job.logger.info(s"Next run is scheduled in $delay")
            _ <- temporal.sleep(delay)
            _ <- loop(supervisedJob, env, mode)
          } yield ()
      }
    } yield ()

  private def getJobExecutionDelay(
      supervisedPeriodicJob: SupervisedPeriodicJob[F, Env]
    ): FiniteDuration =
    FiniteDuration(supervisedPeriodicJob.job.interval.toMillis, TimeUnit.MILLISECONDS)
}
