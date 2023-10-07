package mongodb.support.jobs

import cats.Parallel
import cats.effect.Sync
import cats.syntax.all._
import cron4s._
import org.typelevel.log4cats.Logger
import org.typelevel.log4cats.slf4j.Slf4jLogger

import java.time.ZonedDateTime

class JobsRunner[F[_]: Sync: Parallel, Env](
    env: Env,
    jobRunner: SingleJobRunner[F, Env],
    cronJobRunner: CronJobRunner[F, Env],
    config: JobsRunnerConfig,
  )(implicit
    logger: Logger[F]
  ) {
  def run: F[Unit] =
    for {
      _ <- logger.info("Job runner has started")
      periodicJobs = config.jobs.flatMap(PeriodicJob.fromClassPath[F, Env])
      supervisedPeriodicJobs = periodicJobs.map(SupervisedPeriodicJob.make(_))

      supervisedCronJobs <- config.cronJobs.flatTraverse(parseCronJob(_).map(_.toList))

      periodicJobsFibers <- supervisedPeriodicJobs.parTraverse { job =>
        jobRunner.run(job, env, JobRunSettings(config.mode, config.runOnBootstrap))
      }

      cronJobsFibers <- supervisedCronJobs.parTraverse { job =>
        cronJobRunner.run(job, env, config.mode)
      }

      startedPeriodicJobNames = periodicJobs.map(job => displayJob(job.name)).mkString("\n")
      launchedCronJobNames <- supervisedCronJobs
        .traverse { job =>
          for {
            nextRun <- job.getNextRun
          } yield displayCronJob(
            job.job.name,
            job.interval,
            nextRun,
          )
        }
        .map(_.mkString("\n"))

      _ <- logger.info(
        Console.YELLOW + s"Started following jobs:" + Console.RESET + s"\n$startedPeriodicJobNames"
      )
      _ <- logger.info(
        Console.YELLOW + s"Launched following cron jobs:" + Console.RESET + s"\n$launchedCronJobNames"
      )

      _ <- (periodicJobsFibers ++ cronJobsFibers).parTraverse_(_.join)
      _ <- logger.info("Job runner has finished")
    } yield ()

  private def parseCronJob(cronJobConfig: CronJobConfig): F[Option[SupervisedCronJob[F, Env]]] = {
    val jobOpt = CronJob.fromClassPath[F, Env](cronJobConfig.path)
    val cronExprOpt = Cron.parse(cronJobConfig.interval).toOption

    (jobOpt, cronExprOpt) match {
      case (Some(job), Some(cronExpr)) => Sync[F].delay(SupervisedCronJob.make(job, cronExpr).some)
      case (Some(job), _) =>
        logger
          .error(s"Could not parse cron expression ${cronJobConfig.interval} for job ${job.name}")
          .as(None)
      case _ => logger.error(s"Could not find job ${cronJobConfig.path}").as(None)
    }
  }

  private def displayCronJob(
      name: String,
      interval: CronExpr,
      nextRun: Option[ZonedDateTime],
    ) =
    s"""name=${Console.GREEN + name + Console.RESET} interval='$interval' nextRun=${Console.BLUE +
        nextRun.map(_.toString).getOrElse("--") +
        Console.RESET}"""
  private def displayJob(name: String) = s"""name=${Console.GREEN + name + Console.RESET}"""
}

object JobsRunner {
  def make[F[_]: Sync: Parallel, Env](
      env: Env,
      jobRunner: SingleJobRunner[F, Env],
      cronJobRunner: CronJobRunner[F, Env],
      config: JobsRunnerConfig,
    ): JobsRunner[F, Env] = {
    implicit val logger: Logger[F] = Slf4jLogger.getLogger[F]
    new JobsRunner[F, Env](env, jobRunner, cronJobRunner, config)
  }
}
