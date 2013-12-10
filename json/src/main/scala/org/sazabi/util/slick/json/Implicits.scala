package org.sazabi.util.slick.json

import org.json4s.JValue
import org.json4s.JsonMethods

import slick.ast.BaseTypedType
import slick.driver.JdbcDriver.simple._
import slick.jdbc.{GetResult, SetParameter}
import slick.jdbc.{JdbcType, MappedJdbcType}
import slick.jdbc.PositionedParameters

trait Implicits {
  implicit def setJValue[A](implicit m: JsonMethods[A]):
    SetParameter[JValue] = SetParameter {
      (json: JValue, pp: PositionedParameters) =>
        pp.setString(convert(json))
    }

  implicit def getJValue[A](implicit m: JsonMethods[A]):
    GetResult[JValue] = GetResult { r =>
      invert(r.nextString())
    }

  implicit def jValueType[A](implicit m: JsonMethods[A]):
    JdbcType[JValue] with BaseTypedType[JValue] =
      MappedJdbcType.base[JValue, String](convert[A], invert[A])

  private[this] def convert[A](json: JValue)(implicit m: JsonMethods[A]):
    String = json.toOption map(j => m.compact(m.render(j))) getOrElse ""

  private[this] def invert[A](str: String)(implicit m: JsonMethods[A]):
    JValue = m.parse(str)
}

object Implicits extends Implicits
