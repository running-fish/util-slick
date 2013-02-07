package org.sazabi.util.slick

import com.twitter.util.Future

import slick.jdbc.{GetResult, SetParameter}
import slick.session.{PositionedParameters, Session => SlickSession}

trait Implicits { db: DB =>
  object Imports {
    implicit class Session(self: SlickSession) {
      def transaction[A](f: => A): Future[A] = futurePool {
        self.withTransaction(f)
      }

      def transaction[A](level: IsolationLevel)
        (f: => A): Future[A] = futurePool {
          withIsolationLevel(level)(f)(self)
        }

      def retrying[A](f: => A): Future[A] = futurePool {
        db.trying(f)(self)
      }

      def retrying[A](level: IsolationLevel)
        (f: => A): Future[A] = futurePool {
          db.trying(level, f)(self)
        }
    }

    implicit val setBytes: SetParameter[Array[Byte]] = SetParameter {
      (bytes: Array[Byte], pp: PositionedParameters) =>
        pp.setBytes(bytes)
    }

    implicit val getBytes: GetResult[Array[Byte]] = GetResult { r =>
      r.nextBytes()
    }
  }
}
