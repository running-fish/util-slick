package org.sazabi.util.slick.bijection

import com.twitter.bijection.{ImplicitBijection, Injection}

import scala.reflect.ClassTag

import slick.ast.BaseTypedType
import slick.jdbc.{JdbcType, MappedJdbcType}

trait JdbcTypes {
  private[this] type SlickType[A] = JdbcType[A] with BaseTypedType[A]

  def Bijection2JdbcType[A: ClassTag, B: SlickType](
    implicit bij: ImplicitBijection[A, B]): SlickType[A] =
      MappedJdbcType.base[A, B](bij, bij.invert)

  def Injection2JdbcType[A: ClassTag, B: SlickType](
    implicit inj: Injection[A, B]): SlickType[A] =
      MappedJdbcType.base[A, B](inj, inj.invert(_).get)
}

object jdbc extends JdbcTypes
