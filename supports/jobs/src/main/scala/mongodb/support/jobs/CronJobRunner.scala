package mongodb.support.jobs

import cats.effect.{Async, Fiber, Temporal}
import cats.effect.syntax.all._
import cats.syntax.all._
import cron4s.CronExpr
import eu.timepit.fs2cron.Scheduler
import eu.timepit.fs2cron.cron4s.Cron4sScheduler

class CronJobRunner[F[_]: Async, Env](
    implicit
    temporal: Temporal[F]
  ) {
  private val cronScheduler: Scheduler[F, CronExpr] = Cron4sScheduler.systemDefault[F]

  def run(
      supervisedJob: SupervisedCronJob[F, Env],
      env: Env,
      mode: JobRunMode,
    ): F[Fiber[F, Throwable, Unit]] =
    (for {
      _ <- mode match {
        case JobRunMode.Once => runJob(supervisedJob, env)
        case JobRunMode.Forever => loopJob(supervisedJob, env)
      }
    } yield ()).start

  private def runJob(
      supervisedJob: SupervisedCronJob[F, Env],
      env: Env,
    ): F[Unit] =
    for {
      _ <- supervisedJob.job.logger.info("Running the job")
      jobOutcome <- supervisedJob.job.run(env).attempt
      _ <- jobOutcome match {
        case Left(err) => supervisedJob.job.logger.error(err)(s"Job execution error")
        case Right(_) => supervisedJob.job.logger.info("Job run has finished")
      }
    } yield ()

  /** Awake stream by cron scheduler and run job */
  private def loopJob(
      supervisedJob: SupervisedCronJob[F, Env],
      env: Env,
    ): F[Unit] =
    cronScheduler
      .awakeEvery(supervisedJob.interval)
      .evalTap(_ => runJob(supervisedJob, env))
      .compile
      .drain
}
