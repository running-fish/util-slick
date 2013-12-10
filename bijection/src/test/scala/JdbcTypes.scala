package org.sazabi.util.slick.bijection

import com.twitter.bijection._

import org.scalatest._

import scala.util.Success

import scala.slick.ast.BaseTypedType
import slick.driver.JdbcDriver.simple._
import scala.slick.jdbc.JdbcType

class JdbcTypesSpec extends FunSpec with Matchers with JdbcTypes {
  describe("if `JdbcType[B] with BaseTypedType[B]` is given") {
    case class SomeType(value: String)

    implicit val someTypeToString =
      Bijection.build[SomeType, String](_.value)(SomeType(_))

    implicit val someTypeToStringInjection =
      Injection.buildCatchInvert[SomeType, String](_.value)(SomeType(_))

    describe("Bijection2JdbcType") {
      it("should create `JdbcType[A] with BaseTypedType[A]` from `Bijection[A, B]`") {
        val t = Bijection2JdbcType[SomeType, String]
        t shouldBe a [JdbcType[_]]
        t shouldBe a [BaseTypedType[_]]
      }
    }

    describe("Injection2JdbcType") {
      it("should create `JdbcType[A] with BaseTypedType[A]` from `Injection[A, B]`") {
        val t = Injection2JdbcType[SomeType, String]
        t shouldBe a [JdbcType[_]]
        t shouldBe a [BaseTypedType[_]]
      }
    }
  }
}
