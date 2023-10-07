package mongodb.support.jobs

/** @param jobs
  *  List of jobs that will be executed by JobRunner.
  * @param runOnBootstrap
  *  If JobRunner will run all provided jobs on bootstrap or will schedule them properly.
  * @param mode
  *  JobRun mode for all of the provided jobs.
  */
case class JobsRunnerConfig(
    jobs: List[String],
    cronJobs: List[JobsRunnerConfig.CronJobConfig],
    runOnBootstrap: Boolean,
    mode: JobRunMode,
  )

object JobsRunnerConfig {
  case class CronJobConfig(
      path: String,
      interval: String,
    )
}
