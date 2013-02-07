package org.sazabi.util.slick

import com.twitter.util.{Future, FuturePool, Return, Throw, Try}

import java.sql.SQLTransactionRollbackException

import scala.annotation.tailrec

import slick.jdbc.{StaticQuery => Q}
import slick.session.{Database, Session}

trait DB {
  val futurePool: FuturePool

  def connect(): Database

  def session[A](f: Session => A): Future[A] = futurePool {
    connect().withSession(s => f(s))
  }

  def transaction[A](f: Session => A): Future[A] = futurePool {
    connect().withTransaction(s => f(s))
  }

  type IsolationLevel = IsolationLevel.Value

  object IsolationLevel extends Enumeration {
    val ReadCommitted = Value("READ COMMITTED")
    val RepeatableRead = Value("REPEATABLE READ")
    val Default = RepeatableRead
  }

  def transaction[A](level: IsolationLevel)(f: Session => A):
      Future[A] = futurePool {
    connect().withSession { implicit s =>
      withIsolationLevel(level)(f(s))
    }
  }

  def retrying[A](f: Session => A): Future[A] = futurePool {
    connect().withSession { implicit s => trying(f(s)) }
  }

  def retrying[A](level: IsolationLevel)(f: Session => A):
      Future[A] = futurePool {
    connect().withSession { implicit s => trying(level, f(s)) }
  }

  private[slick] def withIsolationLevel[A](level: IsolationLevel)(f: => A)
      (implicit s: Session): A = {
    Q.updateNA("SET SESSION TRANSACTION ISOLATION LEVEL " +
      level.toString).execute
    try {
      s.withTransaction(f)
    } finally {
      Q.updateNA("SET SESSION TRANSACTION ISOLATION LEVEL " +
        IsolationLevel.Default.toString).execute
    }
  }

  @tailrec
  private[slick] final def trying[A](f: => A)(implicit s: Session): A = {
    Try(s.withTransaction(f)) match {
      case Return(r) => r
      case Throw(_: SQLTransactionRollbackException) => trying(f)
      case Throw(e) => throw e
    }
  }

  @tailrec
  private[slick] final def trying[A](level: IsolationLevel, f: => A)
      (implicit s: Session): A = {
    Try(withIsolationLevel(level)(f)) match {
      case Return(r) => r
      case Throw(_: SQLTransactionRollbackException) => trying(level, f)
      case Throw(e) => throw e
    }
  }
}
