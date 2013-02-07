package org.sazabi.util.slick.scalendar

import java.sql.{Date, Timestamp}

import scalendar.Scalendar

import slick.jdbc.{GetResult, SetParameter}
import slick.lifted.MappedTypeMapper
import slick.session.PositionedParameters

trait Implicits {
  implicit val setScalendar = SetParameter {
    (scal: Scalendar, pp: PositionedParameters) =>
      pp.setTimestamp(convert(scal))
  }

  implicit val getScalendar = GetResult(r => invert(r.nextTimestamp()))

  implicit val scalendarMapper = MappedTypeMapper.base[Scalendar, Timestamp](
    convert, invert
  )

  private[this] def convert(scal: Scalendar): Timestamp = new Timestamp(scal.time)
  private[this] def invert(ts: Timestamp): Scalendar = Scalendar(ts.getTime)
}

object Implicits extends Implicits
