import sbt.*

object Dependencies {
  object Versions {
    lazy val cats = "2.8.0"
    lazy val `cats-effect` = "3.3.14"
    lazy val circe = "0.14.3"
    lazy val fs2 = "3.3.0"
    lazy val log4cats = "2.5.0"
    lazy val logback = "1.4.4"
    lazy val ciris = "2.3.2"
    lazy val refined = "0.10.1"
    lazy val tsec = "0.4.0"
    lazy val monocle = "3.1.0"
    lazy val `cats-retry` = "3.1.0"
    lazy val newtype = "0.4.4"
    lazy val derevo = "0.13.0"
    lazy val `mu-rpc` = "0.29.0"
    lazy val enumeratum = "1.7.2"
    lazy val `fs2-kafka` = "2.5.0"
    lazy val mongo4cats = "0.6.7"
    lazy val cron4s = "0.6.1"
    lazy val `fs2-cron4s` = "0.7.2"
    lazy val chimney = "0.6.2"
    lazy val pureconfig = "0.17.2"

    lazy val weaver = "0.8.0"
    lazy val `test-container` = "1.17.4"
  }

  trait LibGroup {
    def all: Seq[ModuleID]
  }

  object Libraries {
    object Circe extends LibGroup {
      private def circe(artifact: String): ModuleID =
        "io.circe" %% s"circe-$artifact" % Versions.circe

      lazy val core: ModuleID = circe("core")
      lazy val generic: ModuleID = circe("generic")
      lazy val parser: ModuleID = circe("parser")
      lazy val refined: ModuleID = circe("refined")
      lazy val `generic-extras`: ModuleID = circe("generic-extras")
      override def all: Seq[ModuleID] = Seq(core, generic, parser, refined, `generic-extras`)
    }

    object Ciris extends LibGroup {
      private def ciris(artifact: String): ModuleID =
        "is.cir" %% artifact % Versions.ciris

      lazy val core: ModuleID = ciris("ciris")
      lazy val enum: ModuleID = ciris("ciris-enumeratum")
      lazy val refined: ModuleID = ciris("ciris-refined")
      override def all: Seq[ModuleID] = Seq(core, enum, refined)
    }

    object Derevo extends LibGroup {
      private def derevo(artifact: String): ModuleID =
        "tf.tofu" %% s"derevo-$artifact" % Versions.derevo

      lazy val core: ModuleID = derevo("core")
      lazy val cats: ModuleID = derevo("cats")
      override def all: Seq[ModuleID] = Seq(core, cats)
    }

    object Refined extends LibGroup {
      private def refined(artifact: String): ModuleID =
        "eu.timepit" %% artifact % Versions.refined

      lazy val core: ModuleID = refined("refined")
      lazy val cats: ModuleID = refined("refined-cats")
      lazy val pureconfig: ModuleID = refined("refined-pureconfig")
      override def all: Seq[ModuleID] = Seq(core, cats, pureconfig)
    }

    object Fs2Cron4s extends LibGroup {
      private def repo(artifact: String): ModuleID =
        "eu.timepit" %% artifact % Versions.`fs2-cron4s`

      lazy val core: ModuleID = repo("fs2-cron-cron4s")
      override def all: Seq[ModuleID] = Seq(core)
    }

    object Cats extends LibGroup {
      lazy val retry = "com.github.cb372" %% "cats-retry"  % Versions.`cats-retry`
      lazy val core = "org.typelevel"     %% "cats-core"   % Versions.cats
      lazy val effect = "org.typelevel"   %% "cats-effect" % Versions.`cats-effect`
      def all: Seq[ModuleID] = Seq(core, retry, effect)
    }

    object Logging extends LibGroup {
      lazy val log4cats = "org.typelevel" %% "log4cats-slf4j"  % Versions.log4cats
      lazy val logback = "ch.qos.logback"  % "logback-classic" % Versions.logback
      override def all: Seq[ModuleID] = Seq(log4cats, logback)
    }

    object GRPC extends LibGroup {
      private def muRpc(artifact: String): ModuleID =
        "io.higherkindness" %% artifact % Versions.`mu-rpc`

      lazy val service: ModuleID = muRpc("mu-rpc-service")
      lazy val server: ModuleID = muRpc("mu-rpc-server")
      lazy val fs2: ModuleID = muRpc("mu-rpc-fs2")
      override def all: Seq[ModuleID] = Seq(service, server, fs2)
    }

    object Enumeratum extends LibGroup {
      private def enumeratum(artifact: String): ModuleID =
        "com.beachape" %% artifact % Versions.enumeratum
      lazy val core: ModuleID = enumeratum("enumeratum")
      lazy val circe: ModuleID = enumeratum("enumeratum-circe")
      lazy val cats: ModuleID = enumeratum("enumeratum-cats")
      override def all: Seq[ModuleID] = Seq(core, circe, cats)
    }
    object FS2 extends LibGroup {
      private def fs2(artifact: String): ModuleID =
        "co.fs2" %% s"fs2-$artifact" % Versions.fs2
      lazy val core: ModuleID = fs2("core")
      override def all: Seq[ModuleID] = Seq(core)
    }
    object MongoDB extends LibGroup {
      private def mongo(artifact: String): ModuleID =
        "io.github.kirill5k" %% s"mongo4cats-$artifact" % Versions.mongo4cats
      lazy val core: ModuleID = mongo("core")
      lazy val circe: ModuleID = mongo("circe")
      override def all: Seq[ModuleID] = Seq(core, circe)
    }
    object Cron4s extends LibGroup {
      private def cron4s(artifact: String): ModuleID =
        "com.github.alonsodomin.cron4s" %% s"cron4s-$artifact" % Versions.cron4s
      lazy val core: ModuleID = cron4s("core")
      override def all: Seq[ModuleID] = Seq(core)
    }
    object PureConfig extends LibGroup {
      private def repo(artifact: String): ModuleID =
        "com.github.pureconfig" %% artifact % Versions.pureconfig
      lazy val core: ModuleID = repo("pureconfig")
      lazy val enumeratum: ModuleID = repo("pureconfig-enumeratum")
      override def all: Seq[ModuleID] = Seq(core, enumeratum)
    }

    object Testing extends LibGroup {
      lazy val `log4cats-noop` = "org.typelevel"     %% "log4cats-noop"      % Versions.log4cats
      lazy val `refined-scalacheck` = "eu.timepit"   %% "refined-scalacheck" % Versions.refined
      lazy val `weaver-cats` = "com.disneystreaming" %% "weaver-cats"        % Versions.weaver
      lazy val `weaver-discipline` = "com.disneystreaming"  %% "weaver-discipline" % Versions.weaver
      lazy val `weaver-scala-check` = "com.disneystreaming" %% "weaver-scalacheck" % Versions.weaver
      lazy val `test-container` = "org.testcontainers" % "mongodb" % Versions.`test-container`
      override def all: Seq[ModuleID] = Seq(
        `log4cats-noop`,
        `refined-scalacheck`,
        `weaver-cats`,
        `weaver-discipline`,
        `weaver-scala-check`,
        `test-container`,
      )
    }
    lazy val newtype = "io.estatico"       %% "newtype"      % Versions.newtype
    lazy val `monocle-core` = "dev.optics" %% "monocle-core" % Versions.monocle
    val chimney = "io.scalaland"           %% "chimney"      % Versions.chimney
  }
}
