package mongodb.support.jobs

/** @param mode
  *  in what mode job will be ran
  * @param immediatelyOnBootstrap
  *  when true, initial run of the job will be performed immediately,
  *  not depending on previous time of execution
  */
case class JobRunSettings(
    mode: JobRunMode,
    immediatelyOnBootstrap: Boolean,
  )
