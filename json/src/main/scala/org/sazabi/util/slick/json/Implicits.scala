package org.sazabi.util.slick.json

import org.json4s.JValue
import org.json4s.native.JsonMethods.{compact, parse, render}

import slick.jdbc.{GetResult, SetParameter}
import slick.lifted.MappedTypeMapper
import slick.session.PositionedParameters

trait Implicits {
  implicit val setJValue: SetParameter[JValue] = SetParameter {
    (json: JValue, pp: PositionedParameters) =>
      pp.setString(convert(json))
  }

  implicit val getJValue: GetResult[JValue] = GetResult { r =>
    invert(r.nextString())
  }

  implicit val jValueMapper = MappedTypeMapper.base[JValue, String](
    convert, invert
  )

  private[this] def convert(json: JValue): String = json.toOption match {
    case Some(j) => compact(render(j))
    case None => ""
  }

  private[this] def invert(str: String): JValue = parse(str)
}

object Implicits extends Implicits
