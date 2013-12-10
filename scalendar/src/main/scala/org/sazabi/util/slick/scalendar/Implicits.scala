package org.sazabi.util.slick.scalendar

import java.sql.{Date, Timestamp}

import scalendar.Scalendar

import slick.jdbc.{GetResult, SetParameter}

import slick.jdbc.PositionedParameters

import slick.ast.BaseTypedType
import slick.driver.JdbcDriver.simple._
import slick.jdbc.{JdbcType, MappedJdbcType}

trait Implicits {
  implicit val setScalendar = SetParameter {
    (scal: Scalendar, pp: PositionedParameters) =>
      pp.setTimestamp(convert(scal))
  }

  implicit val getScalendar = GetResult(r => invert(r.nextTimestamp()))

  implicit val scalendarMapper = MappedJdbcType.base[Scalendar, Timestamp](
    convert, invert
  )

  private[this] def convert(scal: Scalendar): Timestamp = new Timestamp(scal.time)
  private[this] def invert(ts: Timestamp): Scalendar = Scalendar(ts.getTime)
}

object Implicits extends Implicits
